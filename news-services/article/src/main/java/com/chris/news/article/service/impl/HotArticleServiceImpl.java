package com.chris.news.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.chris.news.article.mapper.ApArticleMapper;
import com.chris.news.article.service.HotArticleService;
import com.chris.news.common.constant.ArticleConstants;
import com.chris.news.common.redis.CacheService;
import com.chris.news.feignapi.wemedia.IWemediaClient;
import com.chris.news.model.article.pojo.ApArticle;
import com.chris.news.model.article.vo.HotArticleVo;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.wemedia.pojo.WmChannel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class HotArticleServiceImpl implements HotArticleService {
    @Resource
    private ApArticleMapper apArticleMapper;

    /**
     * 计算热点文章
     */
    @Override
    public void computeHotArticle() {
        // 1.查询前5天的文章数据
        Date dateParam = DateTime.now().minusDays(50).toDate();
        List<ApArticle> apArticleList = apArticleMapper.findArticleListByLast5days(dateParam);

        // 2.计算文章的分值
        List<HotArticleVo> hotArticleVoList = computeHotArticle(apArticleList);

        // 3.为每个频道缓存30条分值较高的文章
        cacheTagToRedis(hotArticleVoList);
    }

    @Resource
    private IWemediaClient wemediaClient;

    @Resource
    private CacheService cacheService;

    /**
     * 为每个频道缓存30条分值较高的文章
     */
    private void cacheTagToRedis(List<HotArticleVo> hotArticleVoList) {
        // 每个频道缓存30条分值较高的文章
        ResponseResult responseResult   = wemediaClient.getChannels();
        if(responseResult.getCode().equals(200)){
            String channelJson          = JSON.toJSONString(responseResult.getData());
            List<WmChannel> wmChannels  = JSON.parseArray(channelJson, WmChannel.class);

            // 检索出每个频道的文章
            if(wmChannels != null && !wmChannels.isEmpty()){
                for (WmChannel wmChannel : wmChannels) {
                    List<HotArticleVo> hotArticleVos =
                            hotArticleVoList.stream()
                                    .filter(x -> x.getChannelId().equals(wmChannel.getId()))
                                    .collect(Collectors.toList());

                    // 给文章进行排序，取30条分值较高的文章存入redis  key：频道id   value：30条分值较高的文章
                    sortAndCache(
                            hotArticleVos,
                            ArticleConstants.HOT_ARTICLE_FIRST_PAGE + wmChannel.getId()
                    );
                }
            }
        }

        // 设置推荐数据
        // 给文章进行排序，取30条分值较高的文章存入redis  key：频道id   value：30条分值较高的文章
        sortAndCache(
                hotArticleVoList,
                ArticleConstants.HOT_ARTICLE_FIRST_PAGE + ArticleConstants.DEFAULT_TAG
        );
    }

    /**
     * 排序并且缓存数据
     */
    private void sortAndCache(List<HotArticleVo> hotArticleVos, String key) {
        hotArticleVos =
                hotArticleVos.stream()
                        .sorted(Comparator.comparing(HotArticleVo::getScore).reversed())
                        .collect(Collectors.toList());

        if (hotArticleVos.size() > 30) {
            hotArticleVos = hotArticleVos.subList(0, 30);
        }

        cacheService.set(key, JSON.toJSONString(hotArticleVos));
    }

    /**
     * 计算文章分值
     */
    private List<HotArticleVo> computeHotArticle(List<ApArticle> apArticleList) {

        List<HotArticleVo> hotArticleVoList = new ArrayList<>();

        if(apArticleList != null && !apArticleList.isEmpty()){
            for (ApArticle apArticle : apArticleList) {
                HotArticleVo hot = new HotArticleVo();

                BeanUtils.copyProperties(apArticle, hot);

                Integer score = computeScore(apArticle);
                hot.setScore(score);

                hotArticleVoList.add(hot);
            }
        }

        return hotArticleVoList;
    }

    /**
     * 计算文章的具体分值
     */
    private Integer computeScore(ApArticle apArticle) {
        int scere = 0;
        if(apArticle.getLikes() != null){
            scere += apArticle.getLikes() * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }
        if(apArticle.getViews() != null){
            scere += apArticle.getViews();
        }
        if(apArticle.getComment() != null){
            scere += apArticle.getComment() * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }
        if(apArticle.getCollection() != null){
            scere += apArticle.getCollection() * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }
        return scere;
    }
}
