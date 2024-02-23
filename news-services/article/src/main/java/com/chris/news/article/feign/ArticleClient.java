package com.chris.news.article.feign;

import com.chris.news.article.service.ApArticleService;
import com.chris.news.feignapi.article.IArticleClient;
import com.chris.news.model.article.dto.ArticleDto;
import com.chris.news.model.common.dto.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ArticleClient implements IArticleClient {
    @Resource
    private ApArticleService apArticleService;

    @PostMapping("/api/v1/article/save")
    @Override
    public ResponseResult saveArticle(@RequestBody ArticleDto dto) {
        return apArticleService.saveArticle(dto);
    }
}
