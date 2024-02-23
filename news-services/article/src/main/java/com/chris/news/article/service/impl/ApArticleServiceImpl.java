package com.chris.news.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chris.news.article.mapper.ApArticleConfigMapper;
import com.chris.news.article.mapper.ApArticleContentMapper;
import com.chris.news.article.mapper.ApArticleMapper;
import com.chris.news.article.service.ApArticleService;
import com.chris.news.article.service.ArticleFreemarkerService;
import com.chris.news.common.constant.ArticleConstants;
import com.chris.news.common.redis.CacheService;
import com.chris.news.model.article.dto.ArticleDto;
import com.chris.news.model.article.dto.ArticleHomeDto;
import com.chris.news.model.article.dto.ArticleInfoDto;
import com.chris.news.model.article.pojo.ApArticle;
import com.chris.news.model.article.pojo.ApArticleConfig;
import com.chris.news.model.article.pojo.ApArticleContent;
import com.chris.news.model.article.vo.HotArticleVo;
import com.chris.news.model.common.constant.AppHttpCodeEnum;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.message.ArticleVisitStreamMessage;
import com.chris.news.model.user.pojo.ApUser;
import com.chris.news.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ApArticleServiceImpl
        extends ServiceImpl<ApArticleMapper, ApArticle>
        implements ApArticleService {
    @Resource
    private ApArticleMapper apArticleMapper;

    private final static  short MAX_PAGE_SIZE = 50;

    /**
     * 加载文章列表
     * @param type 1 加载更多   2 加载最新
     */
    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        // 1.检验参数
        // 分页条数的校验
        Integer size = dto.getSize();
        if(size == null || size == 0){
            size = 10;
        }

        // 分页的值不超过50
        size = Math.min(size, MAX_PAGE_SIZE);

        // 校验参数  -->type
        if(!type.equals(ArticleConstants.LOADTYPE_LOAD_MORE) &&
                !type.equals(ArticleConstants.LOADTYPE_LOAD_NEW)){
            type = ArticleConstants.LOADTYPE_LOAD_MORE;
        }

        // 频道参数校验
        if(StringUtils.isBlank(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }

        // 时间校验
        if(dto.getMaxBehotTime() == null)dto.setMaxBehotTime(new Date());
        if(dto.getMinBehotTime() == null)dto.setMinBehotTime(new Date());

        // 2.查询
        List<ApArticle> articleList = apArticleMapper.loadArticleList(dto, type);
        // 3.结果返回
        return ResponseResult.okResult(articleList);
    }

    @Resource
    private ApArticleConfigMapper apArticleConfigMapper;

    @Resource
    private ApArticleContentMapper apArticleContentMapper;

    @Resource
    @Lazy
    private ArticleFreemarkerService articleFreemarkerService;

    /**
     * 保存app端相关文章

     */
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        //1.检查参数
        if(dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto,apArticle);

        // 2.判断是否存在id
        if(dto.getId() == null){
            // 2.1 不存在id  保存  文章  文章配置  文章内容

            // 保存文章
            save(apArticle);

            // 保存配置
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());
            apArticleConfigMapper.insert(apArticleConfig);

            // 保存 文章内容
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.insert(apArticleContent);
        }else {
            //2.2 存在id   修改  文章  文章内容

            //修改  文章
            updateById(apArticle);

            //修改文章内容
            ApArticleContent apArticleContent =
                    apArticleContentMapper.selectOne(
                            Wrappers.<ApArticleContent>lambdaQuery()
                                    .eq(ApArticleContent::getArticleId, dto.getId())
                    );
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.updateById(apArticleContent);
        }

        //异步调用 生成静态文件上传到minio中
        articleFreemarkerService.buildArticleToMinIO(apArticle,dto.getContent());


        //3.结果返回  文章的id
        return ResponseResult.okResult(apArticle.getId());
    }

    @Resource
    private StringRedisTemplate redisTemplate;

    @Resource
    private CacheService cacheService;

    @Override
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto) {

        //0.检查参数
        if (dto == null || dto.getArticleId() == null || dto.getAuthorId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        boolean isfollow = false, islike = false, isunlike = false, iscollection = false;

        ApUser user = AppThreadLocalUtil.getUser();
        if(user != null){
            //喜欢行为
            String likeBehaviorJson =
                    (String) cacheService.hGet(
                            "LIKE-BEHAVIOR-" + dto.getArticleId().toString(),
                            user.getId().toString()
                    );
            if(StringUtils.isNotBlank(likeBehaviorJson)){
                islike = true;
            }

            // 不喜欢的行为
            String unLikeBehaviorJson =
                    (String) cacheService.hGet(
                            "UNLIKE-BEHAVIOR-" + dto.getArticleId().toString(),
                            user.getId().toString()
                    );
            if(StringUtils.isNotBlank(unLikeBehaviorJson)){
                isunlike = true;
            }

            // 是否收藏
            String collectionJson =
                    (String) cacheService.hGet(
                            "COLLECTION-BEHAVIOR-"+dto.getArticleId(),
                            user.getId().toString()
                    );
            if(StringUtils.isNotBlank(collectionJson)){
                iscollection = true;
            }

            //是否关注
            Double score =
                    cacheService.zScore(
                            "apuser:follow:" + user.getId(),
                            dto.getAuthorId().toString()
                    );
            if(score != null){
                isfollow = true;
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isfollow", isfollow);
        resultMap.put("islike", islike);
        resultMap.put("isunlike", isunlike);
        resultMap.put("iscollection", iscollection);

        return ResponseResult.okResult(resultMap);
    }

    /**
     * 加载文章列表
     * @param type      1 加载更多   2 加载最新
     * @param firstPage true  是首页  false 非首页
     */
    @Override
    public ResponseResult load2(ArticleHomeDto dto, Short type, boolean firstPage) {
        if(firstPage){
            String jsonStr =
                    cacheService.get(ArticleConstants.HOT_ARTICLE_FIRST_PAGE + dto.getTag());

            if(StringUtils.isNotBlank(jsonStr)){
                List<HotArticleVo> hotArticleVoList =
                        JSON.parseArray(
                                jsonStr,
                                HotArticleVo.class
                        );
                return ResponseResult.okResult(hotArticleVoList);
            }
        }
        return load(dto,type);
    }

    /**
     * 更新文章的分值  同时更新缓存中的热点文章数据
     */
    @Override
    public void updateScore(ArticleVisitStreamMessage message) {
        // 1.更新文章的阅读、点赞、收藏、评论的数量
        ApArticle apArticle = updateArticle(message);

        // 2.计算文章的分值
        Integer score = computeScore(apArticle);
        score = score * 3;

        // 3.替换当前文章对应频道的热点数据
        replaceDataToRedis(
                apArticle,
                score,
                ArticleConstants.HOT_ARTICLE_FIRST_PAGE + apArticle.getChannelId()
        );

        // 4.替换推荐对应的热点数据
        replaceDataToRedis(
                apArticle,
                score,
                ArticleConstants.HOT_ARTICLE_FIRST_PAGE + ArticleConstants.DEFAULT_TAG
        );
    }

    /**
     * 替换数据并且存入到redis
     */
    private void replaceDataToRedis(ApArticle apArticle, Integer score, String s) {
        String articleListStr = cacheService.get(s);
        if (StringUtils.isNotBlank(articleListStr)) {
            List<HotArticleVo> hotArticleVoList =
                    JSON.parseArray(
                            articleListStr,
                            HotArticleVo.class
                    );

            boolean flag = true;

            // 如果缓存中存在该文章，只更新分值
            for (HotArticleVo hotArticleVo : hotArticleVoList) {
                if (hotArticleVo.getId().equals(apArticle.getId())) {
                    hotArticleVo.setScore(score);
                    flag = false;
                    break;
                }
            }

            // 如果缓存中不存在，查询缓存中分值最小的一条数据，进行分值的比较，如果当前文章的分值大于缓存中的数据，就替换
            if (flag) {
                if (hotArticleVoList.size() >= 30) {
                    hotArticleVoList =
                            hotArticleVoList.stream()
                                .sorted(Comparator.comparing(HotArticleVo::getScore).reversed())
                                .collect(Collectors.toList());

                    HotArticleVo lastHot = hotArticleVoList.get(hotArticleVoList.size() - 1);
                    if (lastHot.getScore() < score) {
                        hotArticleVoList.remove(lastHot);
                        HotArticleVo hot = new HotArticleVo();
                        BeanUtils.copyProperties(apArticle, hot);
                        hot.setScore(score);
                        hotArticleVoList.add(hot);
                    }
                } else {
                    HotArticleVo hot = new HotArticleVo();
                    BeanUtils.copyProperties(apArticle, hot);
                    hot.setScore(score);
                    hotArticleVoList.add(hot);
                }
            }

            //缓存到redis
            hotArticleVoList =
                    hotArticleVoList.stream()
                            .sorted(Comparator.comparing(HotArticleVo::getScore).reversed())
                            .collect(Collectors.toList());

            cacheService.set(
                    s,
                    JSON.toJSONString(hotArticleVoList)
            );
        }
    }

    /**
     * 更新文章行为数量
     */
    private ApArticle updateArticle(ArticleVisitStreamMessage message) {
        ApArticle apArticle = getById(message.getArticleId());
        apArticle.setCollection(
                apArticle.getCollection() == null ? 0 : apArticle.getCollection()+message.getCollect()
        );
        apArticle.setComment(
                apArticle.getComment() == null ? 0 : apArticle.getComment()+message.getComment()
        );
        apArticle.setLikes(
                apArticle.getLikes() == null ? 0 : apArticle.getLikes()+message.getLike()
        );
        apArticle.setViews(
                apArticle.getViews() == null ? 0 : apArticle.getViews()+message.getView()
        );
        
        updateById(apArticle);
        
        return apArticle;
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
