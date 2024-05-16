package com.loong.controller;

import com.loong.common.R;
import com.loong.common.state.RofAccount;
import com.loong.feigns.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserFeign userFeign;


    @GetMapping("/test")
    public String test() {
        return userFeign.test();
    }

    @GetMapping("/login")
    public R login(String account,String password,String checkCode,Integer agree){
        if(agree<=0){
            return R.error("请先勾选同意协议");
        }
        // 调用服务判断账号密码是否匹配
        R result = userFeign.login(account, password);
        if(result.getCode()==200){
            // 判断验证码是否正确
            if(userFeign.checkCode(checkCode)){
                return result;
            }else{
                return R.error(RofAccount.CHECK_CODE_ERROR.getMessage());
            }
        }
        return result;
    }
}
