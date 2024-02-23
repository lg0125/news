package com.chris.news.article.service;

import com.chris.news.model.article.pojo.ApArticle;

public interface ArticleFreemarkerService {
    /**
     * 生成静态文件上传到minIO中
     */
    void buildArticleToMinIO(ApArticle apArticle, String content);
}
