package com.loong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.loong.common.R;
import com.loong.entity.User;
import com.loong.entity.dto.UserDto;
import com.loong.service.UserService;
import com.loong.service.checkcode.impl.CheckCodeServiceImpl;
import com.loong.util.EncryptUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author loong
 * @since 2024-04-07
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CheckCodeServiceImpl checkCodeService;

    /**
     * 用户登录逻辑
     * @param account
     * @param password
     * @param checkCode
     * @return
     */
    @GetMapping("/login")
    public R login(String account, String password, String checkCode) {
        return userService.login(account, password,checkCode);
    }

    @DeleteMapping("/logout")
    public R logout(Long id){
        return userService.logout(id);
    }

    @PostMapping("/register")
    public R regster(@RequestBody UserDto userDto, HttpServletRequest request) {
        // 查询用户账号是否已经被注册
        String email = userDto.getEmail();
        String telephone = userDto.getTelephone();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getTelephone, telephone).or()
                .eq(User::getEmail, email);

        User user = userService.getOne(queryWrapper);
        if (user != null) {
            return R.warn("用户已经被注册");
        } else {
            // 判断两个密码是否相同
            if (!userDto.getPassword().equals(userDto.getConfirm())) {
                return R.warn("两次密码输入不一致");
            } else {
                // 检查验证码
                // 采用服务调用的方式
                boolean result;
                result=checkCodeService.checkCode(userDto.getCheckCode(),userDto.getTelephone());
                if (!result) {
                    return R.warn("验证码错误");
                } else {
                    userService.register(userDto);
                    return R.success("用户注册成功");
                }
            }
        }
    }

    @PostMapping("/update")
    @Transactional
    public R update(@RequestBody UserDto userDto) {
        // 在更新用户信息的时候，不更新账号信息同时对密码完成加密

        return userService.updateUser(userDto);
    }

    /**
     * 根据用户Id查询感兴趣的话题
     * @param UserId
     * @return
     */
    @GetMapping("/interests/{UserId}")
    public R interests(@PathVariable long UserId) {
        List<Integer> interests = userService.interests(UserId);
        return R.success(interests);
    }
}
