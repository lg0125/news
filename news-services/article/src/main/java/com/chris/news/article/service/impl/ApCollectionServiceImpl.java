package com.chris.news.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.chris.news.article.service.ApCollectionService;
import com.chris.news.common.redis.CacheService;
import com.chris.news.model.article.dto.CollectionBehaviorDto;
import com.chris.news.model.common.constant.AppHttpCodeEnum;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.user.pojo.ApUser;
import com.chris.news.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class ApCollectionServiceImpl implements ApCollectionService {
    @Resource
    private CacheService cacheService;

    @Override
    public ResponseResult collection(CollectionBehaviorDto dto) {
        //条件判断
        if(dto == null || dto.getEntryId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //判断是否登录
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //查询
        String collectionJson =
                (String) cacheService.hGet(
                        "COLLECTION-BEHAVIOR-" + dto.getEntryId(),
                        user.getId().toString()
                );
        if(StringUtils.isNotBlank(collectionJson) && dto.getOperation() == 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"已收藏");
        }

        // 收藏
        if(dto.getOperation() == 0){
            log.info("文章收藏，保存key:{},{},{}",dto.getEntryId(),user.getId().toString(), JSON.toJSONString(dto));

            cacheService.hPut(
                    "COLLECTION-BEHAVIOR-"+dto.getEntryId(),user.getId().toString(),
                    JSON.toJSONString(dto)
            );
        }else {
            // 取消收藏
            log.info("文章收藏，删除key:{},{},{}",dto.getEntryId(),user.getId().toString(), JSON.toJSONString(dto));

            cacheService.hDelete(
                    "COLLECTION-BEHAVIOR-"+dto.getEntryId(),
                    user.getId().toString()
            );
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
