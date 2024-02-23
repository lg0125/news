package com.chris.news.feignapi.schedule;

import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.schedule.dto.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("news-schedule")
public interface IScheduleClient {
    /**
     * 添加延迟任务
     */
    @PostMapping("/api/v1/task/add")
    ResponseResult addTask(@RequestBody Task task);

    /**
     * 取消任务
     */
    @GetMapping("/api/v1/task/{taskId}")
    ResponseResult cancelTask(@PathVariable("taskId") long taskId);

    /**
     * 按照类型和优先级拉取任务
     */
    @GetMapping("/api/v1/task/{type}/{priority}")
    ResponseResult poll(
            @PathVariable("type") int type,
            @PathVariable("priority") int priority
    );
}
