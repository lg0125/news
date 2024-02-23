package com.chris.news.model.article.dto;

import com.chris.news.model.article.pojo.ApArticle;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleDto extends ApArticle {
    /**
     * 文章内容
     */
    private String content;
}
