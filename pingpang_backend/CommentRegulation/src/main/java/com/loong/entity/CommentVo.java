package com.loong.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {
    private User user;
    private Long id;

    private String ipAddress;

    private Long replyToId;

    private String replyToName;

    private String content;

    private LocalDateTime publishTime;

    private Integer likes;

    private Integer dislikes;

    private Integer state;

    private Integer number;

    private Long userId;
    private List<CommentVo>comments;
}
