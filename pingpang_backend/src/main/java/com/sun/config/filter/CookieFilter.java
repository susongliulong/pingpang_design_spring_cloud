package com.sun.config.filter;

import com.alibaba.fastjson.JSON;
import com.sun.common.R;
import com.sun.common.state.RofAccount;
import com.sun.util.cookie.CookieItem;
import com.sun.util.cookie.CookieState;
import com.sun.util.cookie.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(4)
@WebFilter
@Component
public class CookieFilter extends HttpFilter {

    @Autowired
    private RestTemplate restTemplate;

    public String excludePath[]={
            "/admin/login",
            "/admin/register",
            "/admin/checkAdmin",
            "/user/test",
            "/admin",
            "/thing",
            "/"
    };

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 获取能够与“url-pattern”中匹配的路径，注意是完全匹配的部分，*的部分不包括
        String requestPath=request.getServletPath();
        boolean needFilter=true;
        for (String path : excludePath) {
            if(requestPath.startsWith(path)){
                needFilter=false;
                break;
            }

        }
        if(needFilter){
            // 需要检查cookie
            CookieItem cookieItem = CookieUtil.getCookieValue("adminId", request,true, "UTF8");
            CookieItem passwordCookie = CookieUtil.getCookieValue("password", request);
            if(cookieItem.getValue()!=null&&passwordCookie.getValue()!=null){
                // 请求后端接口，判断账号密码是否正确
                R result = this.restTemplate.getForObject("http://localhost:8888/admin/checkAdmin/{adminId}/{password}"
                        , R.class, cookieItem.getValue(), passwordCookie.getValue());
                if(result!=null&&result.getCode()==200){
                    super.doFilter(request,response,chain);
                    return;
                }
            }
            // 异常cookie情况，用户尚未登录
            if(cookieItem.getCookieState()== CookieState.LOGIN_EXPIRED){
                response.setContentType("application/json");
                response.setCharacterEncoding("utf8");
                response.getWriter().write(JSON.toJSONString(R.warn(RofAccount.LOGIN_EXPIRED.getMessage())));
                return;
            }
        }
        super.doFilter(request,response,chain);
    }
}
