package com.chris.news.search.controller;

import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.search.dto.HistorySearchDto;
import com.chris.news.search.service.ApUserSearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/history")
public class ApUserSearchController {
    @Resource
    private ApUserSearchService apUserSearchService;

    @PostMapping("/load")
    public ResponseResult findUserSearch(){
        return apUserSearchService.findUserSearch();
    }

    @PostMapping("/del")
    public ResponseResult delUserSearch(@RequestBody HistorySearchDto dto){
        return apUserSearchService.delUserSearch(dto);
    }
}
