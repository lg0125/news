package com.chris.news.search.service;

import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.search.dto.UserSearchDto;

public interface ApAssociateWordsService {
    /**
     * 搜索联想词
     */
    ResponseResult search(UserSearchDto dto);
}
