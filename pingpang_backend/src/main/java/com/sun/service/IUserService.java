package com.sun.service;

import com.sun.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sun
 * @since 2024-03-12
 */
public interface IUserService extends IService<User> {
    public List<User>findAll();
}
