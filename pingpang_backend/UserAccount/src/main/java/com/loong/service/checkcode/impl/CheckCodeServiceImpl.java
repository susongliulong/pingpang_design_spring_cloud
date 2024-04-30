package com.loong.service.checkcode.impl;


import com.loong.service.checkcode.CheckCodeService;
import com.loong.util.CheckCodeUtil;
import com.loong.util.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckCodeServiceImpl implements CheckCodeService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String generateCode(Integer number, String account, HttpServletResponse response){

        try {
            String checkCode = CheckCodeUtil.geneCheckCode(4, response);
            redisUtil.set(account + "_checkCode", checkCode, 60);// 验证码有效期为1分钟
            return checkCode;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkCode(String code,String account) {
        Object o = redisUtil.get(account + "_checkCode");
        if (o != null) {
            String captcha = (String) o;
            return code.equalsIgnoreCase(captcha);
        }else{
            return false;
        }
    }
}
