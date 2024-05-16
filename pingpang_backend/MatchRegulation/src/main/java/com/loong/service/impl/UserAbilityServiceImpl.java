package com.loong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loong.entity.UserAbility;
import com.loong.mapper.UserAbilityMapper;
import com.loong.service.IUserAbilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author loong
 * @since 2024-04-19
 */
@Service
public class UserAbilityServiceImpl extends ServiceImpl<UserAbilityMapper, UserAbility> implements IUserAbilityService {

    @Autowired
    private UserAbilityMapper userAbilityMapper;
    @Override
    public String[] getAbilities(String name) {

        return userAbilityMapper.getAbilities(name);
    }

    @Override
    public UserAbility generateAbilityCoefficient(Integer[] serves, Integer[] returns, Integer[] asserts, Integer[] defends) {
        // 计算生成各项能力值
        Integer assault = asserts[0];
        Integer defend = defends[0];
        Integer serve = (int) (serves[0] * 0.05 +
                serves[1] * 0.05 +
                serves[2] * 0.05 +
                serves[3] * 0.1 +
                serves[4] * 0.15 +
                serves[5] * 0.05 +
                serves[6] * 0.1 +
                serves[7] * 0.05 +
                serves[8] * 0.15 +
                serves[9] * 0.1 +
                serves[10] * 0.05 +
                serves[11] * 0.1);

        Integer returnValue = (int) (returns[0] * 0.05 +
                returns[1] * 0.2 +
                returns[2] * 0.2 +
                returns[3] * 0.1 +
                returns[4] * 0.1 +
                returns[5] * 0.1 +
                returns[6] * 0.2 +
                returns[7] * 0.05);

        UserAbility userAbility = new UserAbility();
        userAbility.setServe(serve);
        userAbility.setReceive(returnValue);
        userAbility.setAssault(assault);
        userAbility.setDefend(defend);
        userAbility.setLastUpdateTime(LocalDateTime.now());
        return userAbility;
    }
}
