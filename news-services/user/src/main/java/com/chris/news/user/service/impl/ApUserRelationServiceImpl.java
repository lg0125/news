package com.chris.news.user.service.impl;

import com.chris.news.common.redis.CacheService;
import com.chris.news.model.common.constant.AppHttpCodeEnum;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.user.dto.UserRelationDto;
import com.chris.news.model.user.pojo.ApUser;
import com.chris.news.user.service.ApUserRelationService;
import com.chris.news.utils.thread.AppThreadLocalUtil;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

public class ApUserRelationServiceImpl implements ApUserRelationService {
    @Resource
    RedisTemplate redisTemplate;

    @Resource
    private CacheService cacheService;


    /**
     * 用户关注/取消关注
     */
    @Override
    public ResponseResult follow(UserRelationDto dto) {
        //1 参数校验
        if (dto.getOperation() == null ||
                dto.getOperation() < 0 || dto.getOperation() > 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2 判断是否登录
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        Integer apUserId = user.getId();

        //3 关注 apuser:follow:  apuser:fans:
        Integer followUserId = dto.getAuthorId();
        if (dto.getOperation() == 0) {
            // 将对方写入我的关注中
            cacheService.zAdd("apuser:follow:" + apUserId, followUserId.toString(), System.currentTimeMillis());
            // 将我写入对方的粉丝中
            cacheService.zAdd("apuser:fans:" + followUserId, apUserId.toString(), System.currentTimeMillis());

        } else {
            // 取消关注
            cacheService.zRemove("apuser:follow:" + apUserId, followUserId.toString());
            cacheService.zRemove("apuser:fans:" + followUserId, apUserId.toString());
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);

    }
}
