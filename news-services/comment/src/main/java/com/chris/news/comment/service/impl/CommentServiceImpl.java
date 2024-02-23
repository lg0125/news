package com.chris.news.comment.service.impl;

import com.chris.news.comment.pojo.ApComment;
import com.chris.news.comment.pojo.ApCommentLike;
import com.chris.news.comment.pojo.CommentVo;
import com.chris.news.comment.service.CommentHotService;
import com.chris.news.comment.service.CommentService;
import com.chris.news.feignapi.user.IUserClient;
import com.chris.news.model.comment.CommentDto;
import com.chris.news.model.comment.CommentLikeDto;
import com.chris.news.model.comment.CommentSaveDto;
import com.chris.news.model.common.constant.AppHttpCodeEnum;
import com.chris.news.model.common.dto.ResponseResult;
import com.chris.news.model.user.pojo.ApUser;
import com.chris.news.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Resource
    private IUserClient userClient;

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public ResponseResult saveComment(CommentSaveDto dto) {
        //1.检查参数
        if(dto == null || StringUtils.isBlank(dto.getContent()) || dto.getArticleId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if(dto.getContent().length() > 140){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"评论内容不能超过140字");
        }

        //2.判断是否登录
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //3.安全检查 自行实现

        //4.保存评论
        ApUser dbUser = userClient.findUserById(user.getId());
        if(dbUser == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"当前登录信息有误");
        }
        ApComment apComment = new ApComment();
        apComment.setAuthorId(user.getId());
        apComment.setContent(dto.getContent());
        apComment.setCreatedTime(new Date());
        apComment.setEntryId(dto.getArticleId());
        apComment.setImage(dbUser.getImage());
        apComment.setAuthorName(dbUser.getName());
        apComment.setLikes(0);
        apComment.setReply(0);
        apComment.setType((short)0);
        apComment.setFlag((short)0);
        mongoTemplate.save(apComment);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Resource
    private CommentHotService commentHotService;

    @Override
    public ResponseResult like(CommentLikeDto dto) {
        //1.检查参数
        if(dto == null || dto.getCommentId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.判断是否登录
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        ApComment apComment = mongoTemplate.findById(dto.getCommentId(), ApComment.class);

        //3.点赞
        if(apComment != null && dto.getOperation() == 0){
            //更新评论点赞数量
            apComment.setLikes(apComment.getLikes()+1);
            mongoTemplate.save(apComment);

            //保存评论点赞数据
            ApCommentLike apCommentLike = new ApCommentLike();
            apCommentLike.setCommentId(apComment.getId());
            apCommentLike.setAuthorId(user.getId());
            mongoTemplate.save(apCommentLike);
        }else {
            //更新评论点赞数量
            assert apComment != null;
            int tmp = apComment.getLikes()-1;
            tmp = tmp < 1 ? 0 : tmp;
            apComment.setLikes(tmp);
            mongoTemplate.save(apComment);

            //删除评论点赞
            Query query = Query.query(Criteria.where("commentId").is(apComment.getId()).and("authorId").is(user.getId()));
            mongoTemplate.remove(query,ApCommentLike.class);
        }

        //4.取消点赞
        Map<String,Object> result = new HashMap<>();
        result.put("likes",apComment.getLikes());
        return ResponseResult.okResult(result);
    }

    @Override
    public ResponseResult findByArticleId(CommentDto dto) {
        //1.检查参数
        if(dto == null || dto.getArticleId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        int size = 10;

        //2.加载数据
        Query query = Query.query(Criteria.where("entryId").is(dto.getArticleId()).and("createdTime").lt(dto.getMinDate()));
        query.with(Sort.by(Sort.Direction.DESC,"createdTime")).limit(size);
        List<ApComment> list = mongoTemplate.find(query, ApComment.class);

        //3.数据封装返回
        //3.1 用户未登录
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.okResult(list);
        }

        //3.2 用户已登录
        //需要查询当前评论中哪些数据被点赞了
        List<String> idList = list.stream().map(ApComment::getId).collect(Collectors.toList());
        Query query1 = Query.query(Criteria.where("commentId").in(idList).and("authorId").is(user.getId()));
        List<ApCommentLike> apCommentLikes = mongoTemplate.find(query1, ApCommentLike.class);

        List<CommentVo> resultList = new ArrayList<>();
        list.forEach(x->{
            CommentVo vo = new CommentVo();
            BeanUtils.copyProperties(x,vo);
            for (ApCommentLike apCommentLike : apCommentLikes) {
                if(x.getId().equals(apCommentLike.getCommentId())){
                    vo.setOperation((short)0);
                    break;
                }
            }
            resultList.add(vo);
        });

        return ResponseResult.okResult(resultList);
    }
}
