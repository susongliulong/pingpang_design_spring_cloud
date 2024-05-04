package com.loong.feign.service;

import com.loong.common.R;
import com.loong.entity.dto.UserDto;
import org.springframework.web.bind.annotation.*;

/**
 * 提供统一操作user表的增删改查操作
 */
public interface UserService {

    @PostMapping(value = "/user/add",consumes = "application/json")
    public R addUser(UserDto user);

    @PutMapping("/user/update")
    public R updateUser(UserDto user);

    @DeleteMapping("/user/delete/{id}")
    public R deleteUser(@PathVariable Long id);

}
