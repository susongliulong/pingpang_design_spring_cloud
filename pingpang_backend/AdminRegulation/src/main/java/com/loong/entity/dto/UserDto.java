package com.loong.entity.dto;

import com.loong.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto extends User {
    List<Integer>interests;
    String confirm;// 密码确认
    String checkCode;
}
