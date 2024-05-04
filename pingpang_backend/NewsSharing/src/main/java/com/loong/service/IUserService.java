package com.loong.service;

import com.loong.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author loong
 * @since 2024-05-04
 */
public interface IUserService extends IService<User> {

    void addInterests(Long id, List<Integer> interests);

    void updateInterests(Long id, List<Integer> interests);
}
