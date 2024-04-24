package com.loong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.loong.common.R;
import com.loong.entity.User;
import com.loong.entity.dto.UserDto;
import com.loong.feigns.UserFeign;
import com.loong.service.UserService;
import com.loong.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    private RedisUtil redisUtil;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private UserFeign userFeign;

    @GetMapping("/test")
    public String test() {
        String path = "http://localhost:12345/avatar/download?filename=";

        // 查询id，根据id匹配对应的图像
        List<User> users = userService.list();
        users.forEach(user -> {
            user.setAvatar(path + "test" + user.getId() % 6 + ".jpg");
            user.setDescription("这个人很懒什么都没有留下！");
            userService.updateById(user);
        });
        return "test";
    }

    @GetMapping("/login")
    public R login(String account, String password, HttpServletRequest request) {

        // 在用户未登录成功时，使用用户登录的账号作为存入redis中的键
        return userService.login(account, password);
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
                boolean result=userFeign.checkCode(userDto.getCheckCode());
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
    public R update(@RequestBody UserDto userDto) {

        userService.saveOrUpdate(userDto);
        userService.updateInterestsMessage(userDto.getId(), userDto.getInterests());
        return R.success("账号信息修改成功");
    }

    /**
     * 根据用户Id查询感兴趣的话题
     *
     * @param UserId
     * @return
     */
    @GetMapping("/interests/{UserId}")
    public R interests(@PathVariable long UserId) {

        List<Integer> interests = userService.interests(UserId);
        return R.success(interests);
    }
}
