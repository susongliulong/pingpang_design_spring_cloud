package com.loong.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsDTO {

    private String title;
    private long authorId;
    private long categoryId;
    private String content;// 文章内容
    private String imageUrl;
}
