package com.chris.news.feignapi.article.fallback;

import com.chris.news.feignapi.article.IArticleClient;
import com.chris.news.model.article.dto.ArticleDto;
import com.chris.news.model.common.constant.AppHttpCodeEnum;
import com.chris.news.model.common.dto.ResponseResult;
import org.springframework.stereotype.Component;

@Component
public class IArticleClientFallback implements IArticleClient {
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        return ResponseResult.errorResult(
                AppHttpCodeEnum.SERVER_ERROR,
                "获取数据失败"
        );
    }
}
