package com.loong.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loong.entity.User;
import com.loong.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author loong
 * @since 2024-04-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IService<User> {

}
