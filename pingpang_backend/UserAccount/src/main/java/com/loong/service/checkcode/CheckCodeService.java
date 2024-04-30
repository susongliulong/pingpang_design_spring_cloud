package com.loong.service.checkcode;

import jakarta.servlet.http.HttpServletResponse;

public interface CheckCodeService {

    public String generateCode(Integer number, String account, HttpServletResponse response);

    public boolean checkCode(String code,String account);
}
