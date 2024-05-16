package com.loong.service;

import com.loong.entity.UserAbility;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author loong
 * @since 2024-04-19
 */
public interface IUserAbilityService extends IService<UserAbility> {

    String[] getAbilities(String name);

    UserAbility generateAbilityCoefficient(Integer[] serves, Integer[] returns, Integer[] asserts, Integer[] defends);
}
