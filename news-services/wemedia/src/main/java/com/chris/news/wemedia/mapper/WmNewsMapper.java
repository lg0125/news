package com.chris.news.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chris.news.model.wemedia.pojo.WmNews;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WmNewsMapper extends BaseMapper<WmNews> {
}
