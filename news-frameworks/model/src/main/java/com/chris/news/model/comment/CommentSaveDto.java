package com.chris.news.model.comment;

import com.chris.news.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class CommentSaveDto {
    /**
     * 文章id
     */
    @IdEncrypt
    private Long articleId;

    /**
     * 评论内容
     */
    private String content;
}
