package com.chris.news.user.controller;

import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.user.dto.LoginDto;
import com.chris.news.user.service.ApUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/login")
@Api(value = "app端用户登录",tags = "app端用户登录")
public class ApUserLoginController {
    @Resource
    private ApUserService apUserService;

    @PostMapping("/login_auth")
    @ApiOperation("用户登录")
    public ResponseResult login(@RequestBody LoginDto dto){
        return apUserService.login(dto);
    }
}
