package com.chris.news.behavior.controller;

import com.chris.news.behavior.service.ApUnlikesBehaviorService;
import com.chris.news.model.behavior.dto.UnLikesBehaviorDto;
import com.chris.news.model.common.dto.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/un_likes_behavior")
public class ApUnlikesBehaviorController {
    @Resource
    private ApUnlikesBehaviorService apUnlikesBehaviorService;

    @PostMapping
    public ResponseResult unLike(@RequestBody UnLikesBehaviorDto dto){
        return apUnlikesBehaviorService.unLike(dto);
    }
}
