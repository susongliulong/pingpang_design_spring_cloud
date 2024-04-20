package com.loong.entity.dto;

import com.loong.entity.Match;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatchDTO{
    private Match match;
    private long userId;
    private String content;
}
