package com.chris.news.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chris.news.model.user.pojo.ApUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApUserMapper extends BaseMapper<ApUser> {
}
