package com.chris.news.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.wemedia.pojo.WmChannel;
import com.chris.news.wemedia.mapper.WmChannelMapper;
import com.chris.news.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WmChannelServiceImpl
        extends ServiceImpl<WmChannelMapper, WmChannel>
        implements WmChannelService {
    /**
     * 查询所有频道
     */
    @Override
    public ResponseResult findAll() {
        return ResponseResult.okResult(list());
    }
}
