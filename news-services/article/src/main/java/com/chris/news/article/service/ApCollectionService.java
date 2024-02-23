package com.chris.news.article.service;

import com.chris.news.model.article.dto.CollectionBehaviorDto;
import com.chris.news.model.common.dto.ResponseResult;

public interface ApCollectionService {
    /**
     * 收藏
     */
    ResponseResult collection(CollectionBehaviorDto dto);
}
