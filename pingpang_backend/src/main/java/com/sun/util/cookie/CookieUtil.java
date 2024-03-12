package com.sun.util.cookie;

import com.sun.istack.internal.NotNull;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 管理cookie
 */
public class CookieUtil {

    private CookieUtil() {

    }


    /**
     * 设置cookie属性
     *
     * @param maxAge
     * @param path
     * @param key
     * @param value
     * @param response
     * @param isEncode 是否加秘cookie数据
     * @param charset  加密字符编码
     */
    public static void setCookie(int maxAge, String path,
                                 String key, String value,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 boolean isEncode,
                                 String charset) {

        Cookie cookie = null;
        if (isEncode) {
            // 加密数据
            try {
                value = URLEncoder.encode(value, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        cookie = new Cookie(key, value);
        assert cookie != null;
        cookie.setPath(path);
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        cookie.setDomain(getDomainName(request));
        response.addCookie(cookie);
    }

    public static CookieItem getCookieValue(@NotNull String key,
                                            HttpServletRequest httpServletRequest,
                                            boolean isEncode,
                                            String charset) {
        Cookie[] cookies = httpServletRequest.getCookies();
        String value = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    if (isEncode) {
                        try {
                            value = URLDecoder.decode(cookie.getValue(), charset);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        value = cookie.getValue();
                    }
                }
                if (value != null) {
                    // 找到值
                    return new CookieItem(value, CookieState.SUCCESS);
                }
            }

            // value为null的时候，返回提示信息，后续代码优化

        }
        return new CookieItem(null, CookieState.LOGIN_EXPIRED);
    }


    public static CookieItem getCookieValue(String key, HttpServletRequest request) {
        return getCookieValue(key, request, true, "UTF8");
    }

    public static void setCookie(int maxAge, String path, String key, String value, HttpServletRequest request, HttpServletResponse response) {
        setCookie(maxAge, path, key, value, request, response, true, "UTF8");
    }

    /**
     * 根据key值删除Cookie
     *
     * @param key
     * @param request
     * @return
     */
    public static boolean deleteCookie(@NotNull String key, HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {

            if (key.equals(cookie.getName())) {
                cookie.setMaxAge(0);
                return true;
            }
        }
        return false;
    }

    private static final String getDomainName(HttpServletRequest request) {
        String domainName = null;
        String serverName = request.getRequestURL().toString();
        if (serverName.equals("")) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            // 处理以http://开头的路径，对于https://的就不适用
//            serverName = serverName.substring(7);
            serverName = serverName.split("//")[1];
            final int end = serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            if (serverName.indexOf(":") > 0) {
                String[] ary = serverName.split("\\:");
                serverName = ary[0];
            }

            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3 && !isIp(serverName)) {
                // www.xxx.com.cn
                domainName = "." + domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                // xxx.com or xxx.cn
                domainName = "." + domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }
        return domainName;
    }

    public static String trimSpaces(String IP) {//去掉IP字符串前后所有的空格
        while (IP.startsWith(" ")) {
            IP = IP.substring(1, IP.length()).trim();
        }
        while (IP.endsWith(" ")) {
            IP = IP.substring(0, IP.length() - 1).trim();
        }
        return IP;
    }

    public static boolean isIp(String IP) {//判断是否是一个IP
        IP = trimSpaces(IP);
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = IP.split("\\.");
            return Integer.parseInt(s[0]) < 255
                    && Integer.parseInt(s[1]) < 255
                    && Integer.parseInt(s[2]) < 255
                    && Integer.parseInt(s[3]) < 255;

        }
        return false;
    }
}
