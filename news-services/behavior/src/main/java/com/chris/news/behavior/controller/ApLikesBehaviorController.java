package com.chris.news.behavior.controller;

import com.chris.news.behavior.service.ApLikesBehaviorService;
import com.chris.news.model.behavior.dto.LikesBehaviorDto;
import com.chris.news.model.common.dto.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/likes_behavior")
public class ApLikesBehaviorController {
    @Resource
    private ApLikesBehaviorService apLikesBehaviorService;

    @PostMapping
    public ResponseResult like(@RequestBody LikesBehaviorDto dto){
        return apLikesBehaviorService.like(dto);
    }
}
