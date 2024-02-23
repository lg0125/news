package com.chris.news.comment.service;

import com.chris.news.comment.pojo.ApComment;

public interface CommentHotService {
    /**
     * 计算热点评论
     * @param entryId  文章id
     * @param apComment 当前评论对象
     */
    void findHotComment(Long entryId, ApComment apComment);
}
