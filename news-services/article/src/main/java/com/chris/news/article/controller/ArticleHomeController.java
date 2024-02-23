package com.chris.news.article.controller;

import com.chris.news.article.service.ApArticleService;
import com.chris.news.common.constant.ArticleConstants;
import com.chris.news.model.article.dto.ArticleHomeDto;
import com.chris.news.model.common.dto.ResponseResult;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/article")
public class ArticleHomeController {

    @Resource
    private ApArticleService apArticleService;

    /**
     * 加载首页
     */
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto){
        // return apArticleService.load(dto, ArticleConstants.LOADTYPE_LOAD_MORE);
        return apArticleService.load2(dto, ArticleConstants.LOADTYPE_LOAD_MORE,true);
    }

    /**
     * 加载更多
     */
    @PostMapping("/loadmore")
    public ResponseResult loadmore(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto, ArticleConstants.LOADTYPE_LOAD_MORE);
    }

    /**
     * 加载最新
     */
    @PostMapping("/loadnew")
    public ResponseResult loadnew(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto, ArticleConstants.LOADTYPE_LOAD_NEW);
    }
}
