package com.chris.news.search.controller;

import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.search.dto.UserSearchDto;
import com.chris.news.search.service.ApAssociateWordsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/associate")
public class ApAssociateWordsController {
    @Resource
    private ApAssociateWordsService apAssociateWordsService;

    @PostMapping("/search")
    public ResponseResult search(@RequestBody UserSearchDto dto){
        return apAssociateWordsService.search(dto);
    }
}
