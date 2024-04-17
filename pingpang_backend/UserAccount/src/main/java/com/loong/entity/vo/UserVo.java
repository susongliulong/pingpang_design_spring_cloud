package com.loong.entity.vo;

import com.loong.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserVo {
    private User user;
    // 感兴趣话题编号
    private List<Integer>interests;
}
