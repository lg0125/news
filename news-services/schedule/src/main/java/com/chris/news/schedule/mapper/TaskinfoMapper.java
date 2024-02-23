package com.chris.news.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chris.news.model.schedule.pojo.Taskinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface TaskinfoMapper extends BaseMapper<Taskinfo> {
    List<Taskinfo> queryFutureTime(
            @Param("taskType")int type,
            @Param("priority")int priority,
            @Param("future") Date future
    );
}
