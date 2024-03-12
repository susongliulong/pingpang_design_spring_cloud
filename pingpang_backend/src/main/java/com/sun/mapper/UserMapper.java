package com.sun.mapper;

import com.sun.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sun
 * @since 2024-03-12
 */
public interface UserMapper extends BaseMapper<User> {
    public List<User>findAll();
}
