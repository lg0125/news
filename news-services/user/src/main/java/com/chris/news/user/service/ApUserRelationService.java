package com.chris.news.user.service;

import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.user.dto.UserRelationDto;

public interface ApUserRelationService {
    /**
     * 用户关注/取消关注
     */
    ResponseResult follow(UserRelationDto dto);
}
