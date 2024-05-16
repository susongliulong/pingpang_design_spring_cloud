package com.loong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loong.common.R;
import com.loong.entity.User;
import com.loong.entity.dto.UserDto;

import java.util.List;

public interface UserService extends IService<User> {

    void register(UserDto userDto);

    void updateInterestsMessage(long userId,List<Integer> interests);

    List<Integer> interests(long userId);

    R login(String account, String password,String checkCode);

    R logout(Long id);

    R updateUser(UserDto userDto);

    com.loong.entity.nosecrect.User getUserById(Long id);
}
