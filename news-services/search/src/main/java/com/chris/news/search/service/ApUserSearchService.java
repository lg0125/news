package com.chris.news.search.service;

import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.search.dto.HistorySearchDto;

public interface ApUserSearchService {
    /**
     * 保存用户搜索历史记录
     */
    void insert(String keyword, Integer userId);

    /**
     * 查询搜索历史
     */
    ResponseResult findUserSearch();

    /**
     * 删除历史记录
     */
    ResponseResult delUserSearch(HistorySearchDto dto);
}
