package com.chris.news.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chris.news.model.article.pojo.ApArticleConfig;

import java.util.Map;

public interface ApArticleConfigService extends IService<ApArticleConfig> {
    void updateByMap(Map map);
}
