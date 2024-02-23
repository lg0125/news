package com.chris.news.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.wemedia.dto.WmLoginDto;
import com.chris.news.model.wemedia.pojo.WmUser;

public interface WmUserService extends IService<WmUser> {
    /**
     * 自媒体端登录
     */
    ResponseResult login(WmLoginDto dto);
}
