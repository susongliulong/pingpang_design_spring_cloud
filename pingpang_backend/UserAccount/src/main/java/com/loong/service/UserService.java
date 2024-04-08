package com.loong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loong.entity.User;
import com.loong.entity.dto.UserDto;

import java.util.List;

public interface UserService extends IService<User> {

    void register(UserDto userDto);

    void updateInterestsMessage(long userId,List<Integer> interests);

    List<Integer> interests(long userId);
}
