package com.chris.news.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.user.dto.LoginDto;
import com.chris.news.model.user.pojo.ApUser;

public interface ApUserService extends IService<ApUser> {
    /**
     * app端登录功能
     */
    ResponseResult login(LoginDto dto);
}
