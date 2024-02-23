package com.chris.news.schedule.feign;

import com.chris.news.feignapi.schedule.IScheduleClient;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.schedule.dto.Task;
import com.chris.news.schedule.service.TaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class ScheduleClient implements IScheduleClient {
    @Resource
    private TaskService taskService;
    /**
     * 添加延迟任务
     */
    @PostMapping("/api/v1/task/add")
    public ResponseResult addTask(@RequestBody Task task) {
        return ResponseResult.okResult(taskService.addTask(task));
    }

    /**
     * 取消任务
     */
    @GetMapping("/api/v1/task/{taskId}")
    public ResponseResult cancelTask(@PathVariable("taskId") long taskId){
        return ResponseResult.okResult(taskService.cancelTask(taskId));
    }

    /**
     * 按照类型和优先级拉取任务
     */
    @GetMapping("/api/v1/task/{type}/{priority}")
    public ResponseResult poll(@PathVariable("type") int type,@PathVariable("priority") int priority) {
        return ResponseResult.okResult(taskService.poll(type,priority));
    }
}
