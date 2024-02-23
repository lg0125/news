package com.chris.news.article.listener;

import com.alibaba.fastjson.JSON;
import com.chris.news.article.service.ApArticleService;
import com.chris.news.common.constant.HotArticleConstants;
import com.chris.news.model.message.ArticleVisitStreamMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class ArticleIncrHandleListener {
    @Resource
    private ApArticleService apArticleService;

    @KafkaListener(topics = HotArticleConstants.HOT_ARTICLE_INCR_HANDLE_TOPIC)
    public void onMessage(String mess){
        if(StringUtils.isNotBlank(mess)){
            ArticleVisitStreamMessage articleVisitStreamMess =
                    JSON.parseObject(
                            mess,
                            ArticleVisitStreamMessage.class
                    );
            apArticleService.updateScore(articleVisitStreamMess);
        }
    }
}
