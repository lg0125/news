package com.chris.news.search.service;

import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.search.dto.UserSearchDto;

import java.io.IOException;

public interface ArticleSearchService {
    /**
     * es文章分页检索
     */
    ResponseResult search(UserSearchDto dto) throws IOException;
}
