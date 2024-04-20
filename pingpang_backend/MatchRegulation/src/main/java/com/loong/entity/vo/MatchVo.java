package com.loong.entity.vo;

import com.loong.entity.Match;
import lombok.Data;

@Data
public class MatchVo{

    private Match match;
    private String holder;
    private boolean isSignUp;

}
