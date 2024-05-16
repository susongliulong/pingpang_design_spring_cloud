package com.loong.entity.vo;

import com.loong.entity.Match;
import com.loong.entity.SignUp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpVo{

    private Match match;
    private SignUp signUp;
    private Integer awards;

}
