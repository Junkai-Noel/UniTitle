package com.security;

import com.alibaba.fastjson2.JSON;
import com.utils.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        String json = JSON.toJSONString(new Result.Builder<>().msg("用户"+username+"已登出").code(200).build());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }
}
