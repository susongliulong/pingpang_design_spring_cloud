package com.loong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.loong.common.R;
import com.loong.common.state.RofAccount;
import com.loong.entity.User;
import com.loong.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author loong
 * @since 2024-04-07
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/test")
    public String test(){
        return "hello world";
    }

    @GetMapping("/login")
    public R login(String account, String password){

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName,account).or()
                .eq(User::getTelephone,account).or()
                .eq(User::getEmail,account);

        User user = userService.getOne(queryWrapper);
        if(user.getPassword().equals(password) && user.getState()==1){
            return R.success(user, RofAccount.LOGIN_SUCCESS.getMessage());
        }else if(!user.getPassword().equals(password)){
            return R.error(RofAccount.LOGIN_ERROR.getMessage());
        }else{
            return R.error(RofAccount.AUTHORITY_ERROR.getMessage());
        }
    }
}
