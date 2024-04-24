package com.loong.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long articleId;
    private Long userId;
    private String content;

    // 回复其他人的评论
    private Long commentId;
    private Long replyToId;
    private String replyToName;
}
