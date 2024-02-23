package com.chris.news.comment.service;

import com.chris.news.model.comment.CommentDto;
import com.chris.news.model.comment.CommentLikeDto;
import com.chris.news.model.comment.CommentSaveDto;
import com.chris.news.model.common.dto.ResponseResult;

public interface CommentService {
    /**
     * 保存评论
     */
    ResponseResult saveComment(CommentSaveDto dto);

    /**
     * 点赞
     */
    ResponseResult like(CommentLikeDto dto);

    /**
     * 加载评论列表
     */
    ResponseResult findByArticleId(CommentDto dto);
}
