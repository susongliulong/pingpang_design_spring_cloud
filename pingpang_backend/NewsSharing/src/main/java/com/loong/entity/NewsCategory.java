package com.loong.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsCategory {

    private long id;
    private String name;

    private Integer hots;
}
