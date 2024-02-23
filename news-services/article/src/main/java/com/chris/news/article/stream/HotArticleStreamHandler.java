package com.chris.news.article.stream;

import com.alibaba.fastjson.JSON;
import com.chris.news.common.constant.HotArticleConstants;
import com.chris.news.model.message.ArticleVisitStreamMessage;
import com.chris.news.model.message.UpdateArticleMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static com.chris.news.model.message.UpdateArticleMessage.UpdateArticleType.COLLECTION;

@Configuration
@Slf4j
public class HotArticleStreamHandler {
    @Bean
    public KStream<String,String> kStream(StreamsBuilder streamsBuilder){
        //接收消息
        KStream<String,String> stream = streamsBuilder.stream(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC);
        //聚合流式处理
        stream.map((key,value)->{
                    UpdateArticleMessage message = JSON.parseObject(value, UpdateArticleMessage.class);
                    //重置消息的key:1234343434   和  value: likes:1
                    return new KeyValue<>(
                            message.getArticleId().toString(),
                            message.getType().name() + ":" + message.getAdd()
                    );
                })
                //按照文章id进行聚合
                .groupBy((key,value)->key)
                //时间窗口
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                // 自行的完成聚合的计算
                .aggregate(
                        new Initializer<String>() {
                            /**
                             * 初始方法，返回值是消息的value
                             */
                            @Override
                            public String apply() {
                                return "COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0";
                            }
                            /*
                             * 真正的聚合操作，返回值是消息的value
                             */
                        },

                        (key, value, aggValue) -> {
                            if(StringUtils.isBlank(value)){
                                return aggValue;
                            }

                            String[] aggAry = aggValue.split(",");
                            int col = 0,com=0,lik=0,vie=0;
                            for (String agg : aggAry) {
                                String[] split = agg.split(":");
                                /*
                                 * 获得初始值，也是时间窗口内计算之后的值
                                 */
                                switch (UpdateArticleMessage.UpdateArticleType.valueOf(split[0])){
                                    case COLLECTION:
                                        col = Integer.parseInt(split[1]);
                                        break;
                                    case COMMENT:
                                        com = Integer.parseInt(split[1]);
                                        break;
                                    case LIKES:
                                        lik = Integer.parseInt(split[1]);
                                        break;
                                    case VIEWS:
                                        vie = Integer.parseInt(split[1]);
                                        break;
                                }
                            }
                            /*
                             * 累加操作
                             */
                            String[] valAry = value.split(":");
                            switch (UpdateArticleMessage.UpdateArticleType.valueOf(valAry[0])){
                                case COLLECTION:
                                    col += Integer.parseInt(valAry[1]);
                                    break;
                                case COMMENT:
                                    com += Integer.parseInt(valAry[1]);
                                    break;
                                case LIKES:
                                    lik += Integer.parseInt(valAry[1]);
                                    break;
                                case VIEWS:
                                    vie += Integer.parseInt(valAry[1]);
                                    break;
                            }
                            String formatStr = String.format("COLLECTION:%d,COMMENT:%d,LIKES:%d,VIEWS:%d", col, com, lik, vie);
                            System.out.println("文章的id:"+key);
                            System.out.println("当前时间窗口内的消息处理结果："+formatStr);
                            return formatStr;
                        },

                        Materialized.as("hot-article-stream-count-001")
                ).toStream()
                .map((key,value)-> new KeyValue<>(key.key(),formatObj(key.key(),value)))
                .to(HotArticleConstants.HOT_ARTICLE_INCR_HANDLE_TOPIC);

        return stream;
    }

    /**
     * 格式化消息的value数据
     */
    public String formatObj(String articleId,String value){
        ArticleVisitStreamMessage mess = new ArticleVisitStreamMessage();
        mess.setArticleId(Long.valueOf(articleId));
        //COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0
        String[] valAry = value.split(",");
        for (String val : valAry) {
            String[] split = val.split(":");
            switch (UpdateArticleMessage.UpdateArticleType.valueOf(split[0])){
                case COLLECTION:
                    mess.setCollect(Integer.parseInt(split[1]));
                    break;
                case COMMENT:
                    mess.setComment(Integer.parseInt(split[1]));
                    break;
                case LIKES:
                    mess.setLike(Integer.parseInt(split[1]));
                    break;
                case VIEWS:
                    mess.setView(Integer.parseInt(split[1]));
                    break;
            }
        }
        log.info("聚合消息处理之后的结果为:{}",JSON.toJSONString(mess));
        return JSON.toJSONString(mess);

    }
}
