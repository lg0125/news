package com.chris.news.schedule.service;

import com.chris.news.model.schedule.dto.Task;

public interface TaskService {
    /**
     * 添加延迟任务
     */
    long addTask(Task task);

    /**
     * 取消任务
     */
    boolean cancelTask(long taskId);

    /**
     * 按照类型和优先级拉取任务
     */
    Task poll(int type, int priority);
}
