package com.chris.news.behavior.service;

import com.chris.news.model.behavior.dto.UnLikesBehaviorDto;
import com.chris.news.model.common.dto.ResponseResult;

public interface ApUnlikesBehaviorService {
    /**
     * 不喜欢
     */
    ResponseResult unLike(UnLikesBehaviorDto dto);
}
