package com.loong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loong.entity.User;
import com.loong.entity.dto.UserDto;
import com.loong.mapper.UserMapper;
import com.loong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author loong
 * @since 2024-04-07
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public void register(UserDto userDto) {
        // 跟新用户数据
        userMapper.insert(userDto);

        userDto.getInterests().forEach(interest -> {
            userMapper.insertInterest(userDto.getId(), interest);
        });
    }

    @Override
    public void updateInterestsMessage(long userId, List<Integer> interests) {
        userMapper.deleteInterestsById(userId);
        interests.forEach(interest -> userMapper.insertInterest(userId, interest));
    }

    @Override
    public List<Integer> interests(long userId) {
        return userMapper.selectInterests(userId);
    }

}
