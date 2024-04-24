package com.loong.common.state;

/**
 * 和账号处理相关的返回警告信息
 */
public enum  RofAccount {
    ACCOUNT_REGISTER_SUCCESS("账号注册成功"),
    NAME_REPEAT("昵称重复"),
    EMAIL_REPEAT("邮箱重复"),
    TELEPHONE_REPEAT("手机号重复"),
    LOGIN_EXPIRED("尚未登录"),
    LOGIN_ERROR("账号或者密码错误"),
    CHECK_CODE_ERROR("验证码错误"),
    ACCOUNT_DELETE_SUCCESS("账号注销成功"),
    LOGIN_SUCCESS("登录成功"), AUTHORITY_ERROR("用户登录权限不足");
    String message;

    RofAccount(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

}
