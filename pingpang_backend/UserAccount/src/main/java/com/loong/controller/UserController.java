package com.loong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.loong.common.R;
import com.loong.common.state.RofAccount;
import com.loong.entity.User;
import com.loong.entity.dto.UserDto;
import com.loong.entity.vo.UserVo;
import com.loong.service.UserService;
import com.loong.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/test")
    public String test() {
        return "hello world";
    }

    @GetMapping("/login")
    public R login(String account, String password) {

        if(redisUtil.get("suspend_login") != null){
            return R.error("失败次数超过三次，将暂停一分钟登录");
        }else{
            if(redisUtil.get("login_fail_count") != null && (Integer)redisUtil.get("login_fail_count") >= 3){
                redisUtil.del("login_fail_count");
            }
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getTelephone, account).or()
                .eq(User::getEmail, account);

        User user = userService.getOne(queryWrapper);
        if (user!=null &&user.getPassword().equals(password) && user.getState() == 1) {
            //登录成功，删除键值
            redisUtil.del("login_fail_count","suspend_login");
            // 查询用户感兴趣话题
            List<Integer>interests = userService.interests(user.getId());
            UserVo userVo = new UserVo();
            userVo.setUser(user);userVo.setInterests(interests);
            return R.success(userVo, RofAccount.LOGIN_SUCCESS.getMessage());
        }
        // 超过三次登录失败此时应当暂停登录一分钟
        Object count = redisUtil.get("login_fail_count");
        if (count == null) {
            redisUtil.set("login_fail_count", 1);
        } else {
            redisUtil.set("login_fail_count", (Integer) count + 1);
            if ((Integer) count + 1 >= 3) {
                // 暂停登录一分钟
                redisUtil.set("suspend_login", "true", 60);
            }
        }
        if(user==null){
            return R.warn("当前用户不存在");
        }else{
            if (!user.getPassword().equals(password)) {
                return R.warn(RofAccount.LOGIN_ERROR.getMessage());
            }

            if(user.getState() != 1){ // 1表示允许登录
                return R.warn("用户已经被禁止登录");
            }
        }
        return R.warn("用户登录失败");
    }


    @PostMapping("/register")
    public R regster(@RequestBody UserDto userDto, HttpServletRequest request){
        // 查询用户账号是否已经被注册
        String email = userDto.getEmail();
        String telephone = userDto.getTelephone();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getTelephone,telephone).or()
                .eq(User::getEmail,email);

        User user = userService.getOne(queryWrapper);
        if(user != null){
            return R.warn("用户已经被注册");
        }else{
        // 判断两个密码是否相同
            if(! userDto.getPassword().equals(userDto.getConfirm())){
                return R.warn("两次密码输入不一致");
            }else{
                // 检查验证码
                Object captcha = request.getSession().getServletContext().getAttribute("captcha");
                if(captcha==null){
                    return R.warn("验证码为空");
                }else{
                    if(! captcha.toString().equals(userDto.getCheckCode())){
                        return R.warn("验证码错误");
                    }else{
                        userService.register(userDto);
                        return R.success("用户注册成功");
                    }
                }
            }
        }
    }

    @PostMapping("/update")
    public R update(@RequestBody UserDto userDto){

        userService.saveOrUpdate(userDto);
        userService.updateInterestsMessage(userDto.getId(),userDto.getInterests());
        return R.success("账号信息修改成功");
    }

    /**
     * 根据用户Id查询感兴趣的话题
     * @param UserId
     * @return
     */
    @GetMapping("/interests/{UserId}")
    public R interests(@PathVariable long UserId){

        List<Integer> interests = userService.interests(UserId);
        return R.success(interests);
    }
}
