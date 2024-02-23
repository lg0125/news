package com.chris.news.user.controller;

import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.user.dto.UserRelationDto;
import com.chris.news.user.service.ApUserRelationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/user")
public class UserRelationController {
    @Resource
    private ApUserRelationService apUserRelationService;

    @PostMapping("/user_follow")
    public ResponseResult follow(@RequestBody UserRelationDto dto){
        return apUserRelationService.follow(dto);
    }
}
