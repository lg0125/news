package com.chris.news.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.chris.news.behavior.service.ApUnlikesBehaviorService;
import com.chris.news.common.redis.CacheService;
import com.chris.news.model.behavior.dto.UnLikesBehaviorDto;
import com.chris.news.model.common.constant.AppHttpCodeEnum;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.user.pojo.ApUser;
import com.chris.news.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ApUnlikesBehaviorServiceImpl implements ApUnlikesBehaviorService {
    @Resource
    private CacheService cacheService;

    @Override
    public ResponseResult unLike(UnLikesBehaviorDto dto) {

        if(dto.getArticleId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        if(dto.getType()==0){
            log.info("保存当前key:{} ,{}, {}", dto.getArticleId(), user.getId(), dto);
            cacheService.hPut("UNLIKE-BEHAVIOR-"+dto.getArticleId().toString(),user.getId().toString(), JSON.toJSONString(dto));
        }else {
            log.info("删除当前key:{} ,{}, {}", dto.getArticleId(), user.getId(), dto);
            cacheService.hDelete("UNLIKE-BEHAVIOR-"+dto.getArticleId().toString(),user.getId().toString());
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
