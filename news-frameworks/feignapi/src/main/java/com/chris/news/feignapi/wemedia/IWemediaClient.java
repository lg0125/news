package com.chris.news.feignapi.wemedia;

import com.chris.news.model.common.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("news-wemedia")
public interface IWemediaClient {
    @GetMapping("/api/v1/channel/list")
    ResponseResult getChannels();
}
