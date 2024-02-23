package com.chris.news.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.wemedia.dto.WmNewsDto;
import com.chris.news.model.wemedia.dto.WmNewsPageReqDto;
import com.chris.news.model.wemedia.pojo.WmNews;

public interface WmNewsService extends IService<WmNews> {
    /**
     * 条件查询文章列表
     */
    ResponseResult findList(WmNewsPageReqDto dto);

    /**
     * 发布修改文章或保存为草稿
     */
    ResponseResult submitNews(WmNewsDto dto);

    /**
     * 文章的上下架
     */
    ResponseResult downOrUp(WmNewsDto dto);
}
