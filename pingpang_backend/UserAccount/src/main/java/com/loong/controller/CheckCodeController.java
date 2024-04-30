package com.loong.controller;


import com.loong.util.CheckCodeUtil;
import com.loong.util.RedisUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/code")
public class CheckCodeController {

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/get")
    public void generateCode(Integer number, String account, HttpServletResponse response) throws ServletException, IOException {

        try {
            String checkCode = CheckCodeUtil.geneCheckCode(4, response);
            redisUtil.set(account + "_checkCode", checkCode, 60);// 验证码有效期为1分钟
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
        优化方案：
        验证码有所种形式，有图形验证码，普通验证码还有图像验证码。如何进行设计比较好？
        设计模式里面内容！
     */
}
