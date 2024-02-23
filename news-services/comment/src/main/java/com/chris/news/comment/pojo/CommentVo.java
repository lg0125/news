package com.chris.news.comment.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentVo extends ApComment {
    /**
     * 0：点赞
     * 1：取消点赞
     */
    private Short operation;
}
