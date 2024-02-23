package com.chris.news.feignapi.article;

import com.chris.news.model.article.dto.ArticleDto;
import com.chris.news.model.common.dto.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface IArticleClient {
    @PostMapping("/api/v1/article/save")
    ResponseResult saveArticle(@RequestBody ArticleDto dto);
}
