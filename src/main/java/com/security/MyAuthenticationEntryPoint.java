package com.security;

import com.alibaba.fastjson2.JSON;
import com.utils.Result;
import com.utils.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        String exception = JSON.toJSONString(authException.getLocalizedMessage());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(exception);
    }
}
