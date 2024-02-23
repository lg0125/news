package com.chris.news.comment.controller;

import com.chris.news.comment.service.CommentRepayService;
import com.chris.news.model.comment.CommentRepayDto;
import com.chris.news.model.comment.CommentRepayLikeDto;
import com.chris.news.model.comment.CommentRepaySaveDto;
import com.chris.news.model.common.dto.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/comment_repay")
public class CommentRepayController {
    @Resource
    private CommentRepayService commentRepayService;

    @PostMapping("/load")
    public ResponseResult loadCommentRepay(@RequestBody CommentRepayDto dto){
        return commentRepayService.loadCommentRepay(dto);
    }

    @PostMapping("/save")
    public ResponseResult saveCommentRepay(@RequestBody CommentRepaySaveDto dto){
        return commentRepayService.saveCommentRepay(dto);
    }

    @PostMapping("/like")
    public ResponseResult saveCommentRepayLike(@RequestBody CommentRepayLikeDto dto){
        return commentRepayService.saveCommentRepayLike(dto);
    }
}
