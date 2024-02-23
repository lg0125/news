package com.chris.news.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.wemedia.pojo.WmChannel;

public interface WmChannelService extends IService<WmChannel> {
    /**
     * 查询所有频道
     */
    ResponseResult findAll();
}
