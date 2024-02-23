package com.chris.news.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.chris.news.behavior.service.ApLikesBehaviorService;
import com.chris.news.common.constant.HotArticleConstants;
import com.chris.news.common.redis.CacheService;
import com.chris.news.model.behavior.dto.LikesBehaviorDto;
import com.chris.news.model.common.constant.AppHttpCodeEnum;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.message.UpdateArticleMessage;
import com.chris.news.model.user.pojo.ApUser;
import com.chris.news.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
@Slf4j
public class ApLikesBehaviorServiceImpl implements ApLikesBehaviorService {
    @Resource
    private CacheService cacheService;

    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public ResponseResult like(LikesBehaviorDto dto) {
        //1.检查参数
        if (dto == null || dto.getArticleId() == null || checkParam(dto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.是否登录
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        UpdateArticleMessage mess = new UpdateArticleMessage();
        mess.setArticleId(dto.getArticleId());
        mess.setType(UpdateArticleMessage.UpdateArticleType.LIKES);

        //3.点赞  保存数据
        if(dto.getOperation() == 0){
            Object obj = cacheService.hGet(
                    "LIKE-BEHAVIOR-" + dto.getArticleId().toString(),
                    user.getId().toString()
            );
            if(obj != null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"已点赞");
            }

            // 保存当前key
            log.info("保存当前key:{} ,{}, {}", dto.getArticleId(), user.getId(), dto);
            cacheService.hPut(
                    "LIKE-BEHAVIOR-" + dto.getArticleId().toString(), user.getId().toString(),
                    JSON.toJSONString(dto)
            );
            mess.setAdd(1);
        }else {
            // 删除当前key
            log.info("删除当前key:{}, {}", dto.getArticleId(), user.getId());
            cacheService.hDelete(
                    "LIKE-BEHAVIOR-" + dto.getArticleId().toString(),
                    user.getId().toString()
            );
            mess.setAdd(-1);
        }

        //发送消息，数据聚合
        kafkaTemplate.send(
                HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC,
                JSON.toJSONString(mess)
        );

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 检查参数
     */
    private boolean checkParam(LikesBehaviorDto dto){
        return dto.getType() > 2 || dto.getType() < 0 || dto.getOperation() > 1 || dto.getOperation() < 0;
    }
}
