package com.chris.news.behavior.service;

import com.chris.news.model.behavior.dto.ReadBehaviorDto;
import com.chris.news.model.common.dto.ResponseResult;

public interface ApReadBehaviorService {
    /**
     * 保存阅读行为
     */
    ResponseResult readBehavior(ReadBehaviorDto dto);
}
