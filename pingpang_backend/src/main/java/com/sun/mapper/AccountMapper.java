package com.sun.mapper;

import com.sun.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.entity.User;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sun
 * @since 2024-03-12
 */
public interface AccountMapper extends BaseMapper<Account> {

    public List<User> findAll();
}
