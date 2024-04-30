package com.loong.util;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.security.crypto.bcrypt.BCrypt;


public class EncryptUtil {

    /**
     * 使用Bcrypt对密码进行加密
     * @param password
     * @return
     */
    public static String bcrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt("$2a"+password));
    }

    public static boolean bcryptMatch(String password,String encryptPassword){
        return BCrypt.checkpw(password, encryptPassword);
    }

    /**
     *使用MD5加密时，随机生成salt
     * @param password
     * @return
     */
    public static String md5(String password){
        String salt = BCrypt.gensalt(password);
        String newPassword=password+salt;
        return MD5Encoder.encode(newPassword.getBytes());
    }
}
