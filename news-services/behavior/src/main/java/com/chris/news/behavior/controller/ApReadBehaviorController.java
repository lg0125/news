package com.chris.news.behavior.controller;

import com.chris.news.behavior.service.ApReadBehaviorService;
import com.chris.news.model.behavior.dto.ReadBehaviorDto;
import com.chris.news.model.common.dto.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/read_behavior")
public class ApReadBehaviorController {
    @Resource
    private ApReadBehaviorService apReadBehaviorService;

    @PostMapping
    public ResponseResult readBehavior(@RequestBody ReadBehaviorDto dto){
        return apReadBehaviorService.readBehavior(dto);
    }
}
