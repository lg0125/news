package com.chris.news.behavior.service;

import com.chris.news.model.behavior.dto.LikesBehaviorDto;
import com.chris.news.model.common.dto.ResponseResult;


public interface ApLikesBehaviorService {
    /**
     * 存储喜欢数据
     */
    ResponseResult like(LikesBehaviorDto dto);
}
