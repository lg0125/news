package com.chris.news.user.feign;

import com.chris.news.feignapi.user.IUserClient;
import com.chris.news.model.user.pojo.ApUser;
import com.chris.news.user.service.ApUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserClient implements IUserClient {
    @Resource
    private ApUserService apUserService;

    @Override
    @GetMapping("/api/v1/user/{id}")
    public ApUser findUserById(@PathVariable("id") Integer id) {
        return apUserService.getById(id);
    }
}
