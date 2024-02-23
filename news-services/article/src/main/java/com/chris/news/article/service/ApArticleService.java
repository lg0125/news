package com.chris.news.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chris.news.model.article.dto.ArticleDto;
import com.chris.news.model.article.dto.ArticleHomeDto;
import com.chris.news.model.article.dto.ArticleInfoDto;
import com.chris.news.model.article.pojo.ApArticle;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.message.ArticleVisitStreamMessage;

public interface ApArticleService extends IService<ApArticle> {
    /**
     * 加载文章列表
     * @param type  1 加载更多   2 加载最新
     */
    public ResponseResult load(ArticleHomeDto dto,Short type);

    /**
     * 加载文章列表
     * @param type  1 加载更多   2 加载最新
     * @param firstPage  true  是首页  false 非首页
     */
    public ResponseResult load2(ArticleHomeDto dto, Short type, boolean firstPage);

    /**
     * 保存app端相关文章
     */
    public ResponseResult saveArticle(ArticleDto dto);

    /**
     * 加载文章详情 数据回显
     */
    ResponseResult loadArticleBehavior(ArticleInfoDto dto);

    /**
     * 更新文章的分值  同时更新缓存中的热点文章数据
     */
    void updateScore(ArticleVisitStreamMessage message);
}
