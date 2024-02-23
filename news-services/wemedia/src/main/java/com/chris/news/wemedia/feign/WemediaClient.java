package com.chris.news.wemedia.feign;

import com.chris.news.feignapi.wemedia.IWemediaClient;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.wemedia.service.WmChannelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class WemediaClient implements IWemediaClient {
    @Resource
    private WmChannelService wmChannelService;

    @GetMapping("/api/v1/channel/list")
    @Override
    public ResponseResult getChannels() {
        return wmChannelService.findAll();
    }
}
