package com.loong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loong.entity.User;
import com.loong.mapper.UserMapper;
import com.loong.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author loong
 * @since 2024-05-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public void addInterests(Long id, List<Integer> interests) {

        interests.forEach(interest->userMapper.addInterest(id,interest));
    }

    @Override
    public void updateInterests(Long id, List<Integer> interests) {
        interests.forEach(interest->userMapper.updateInterest(id,interest));
    }
}
