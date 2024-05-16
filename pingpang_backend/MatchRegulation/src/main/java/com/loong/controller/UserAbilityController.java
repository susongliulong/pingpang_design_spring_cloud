package com.loong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.loong.common.R;
import com.loong.entity.UserAbility;
import com.loong.service.impl.UserAbilityServiceImpl;
import com.loong.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author loong
 * @since 2024-04-19
 */
@RestController
@RequestMapping("/userAbility")
public class UserAbilityController {


    @Autowired
    private UserAbilityServiceImpl userAbilityService;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/isUpdateUserAbility")
    public R isUpdateUserAbility(Long userId) {
        UserAbility userAbility = userAbilityService.getOne(new LambdaQueryWrapper<UserAbility>().eq(UserAbility::getUserId, userId));
        int isUpdate = 2;
        if (userAbility.getLastUpdateTime() == null) {
            userAbility.setLastUpdateTime(LocalDateTime.now());
            userAbilityService.saveOrUpdate(userAbility);
            isUpdate = 0;
        } else if (LocalDateTime.now().minusDays(7L).isAfter(userAbility.getLastUpdateTime())) {
            isUpdate = 1;
        }
        return R.success(isUpdate);
    }

    @GetMapping("/ability")
    public R getAbilities(String name) {
        List<Object> abilities = redisUtil.lGet(name, 0, -1);
        if(abilities != null && !abilities.isEmpty()){
            return R.success(abilities);
        }
        String[] serviceAbilities = userAbilityService.getAbilities(name);
        redisUtil.lSet(name, Arrays.asList((String[])serviceAbilities), 60*60*60);
        return R.success(serviceAbilities);
    }

    @PostMapping("/post")
    public R post(
            Integer asserts[],
            Integer serves[],
            Integer returns[],
            Integer defends[],
            Long userId) {

        // 生成能力系数
        UserAbility userAbility = userAbilityService.generateAbilityCoefficient(serves, returns, asserts, defends);
        userAbility.setUserId(userId);

        userAbilityService.saveOrUpdate(userAbility);

        return R.success(null, "个人能力更新成功");
    }
}
