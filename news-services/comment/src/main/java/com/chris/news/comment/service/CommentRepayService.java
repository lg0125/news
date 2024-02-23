package com.chris.news.comment.service;

import com.chris.news.model.comment.CommentRepayDto;
import com.chris.news.model.comment.CommentRepayLikeDto;
import com.chris.news.model.comment.CommentRepaySaveDto;
import com.chris.news.model.common.dto.ResponseResult;

public interface CommentRepayService {
    /**
     * 查看更多回复内容
     */
    ResponseResult loadCommentRepay(CommentRepayDto dto);

    /**
     * 保存回复
     */
    ResponseResult saveCommentRepay(CommentRepaySaveDto dto);

    /**
     * 点赞回复的评论
     */
    ResponseResult saveCommentRepayLike(CommentRepayLikeDto dto);
}
