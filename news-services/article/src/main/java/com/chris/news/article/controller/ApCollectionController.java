package com.chris.news.article.controller;

import com.chris.news.article.service.ApCollectionService;
import com.chris.news.model.article.dto.CollectionBehaviorDto;
import com.chris.news.model.common.dto.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/collection_behavior")
public class ApCollectionController {
    @Resource
    private ApCollectionService apCollectionService;

    @PostMapping
    public ResponseResult collection(@RequestBody CollectionBehaviorDto dto) {
        return apCollectionService.collection(dto);
    }
}
