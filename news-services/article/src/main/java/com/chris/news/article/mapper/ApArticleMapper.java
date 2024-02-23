package com.chris.news.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chris.news.model.article.dto.ArticleHomeDto;
import com.chris.news.model.article.pojo.ApArticle;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ApArticleMapper extends BaseMapper<ApArticle> {
    /**
     * 加载文章列表
     * @param type  1  加载更多   2记载最新
     */
    public List<ApArticle> loadArticleList(ArticleHomeDto dto, Short type);

    public List<ApArticle> findArticleListByLast5days(@Param("dayParam") Date dayParam);
}
