package com.security;


import com.utils.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        String localizedMessage = exception.getLocalizedMessage();

        Map<String, Object> map = new HashMap<>();
        map.put("message", localizedMessage);
        map.put("result",new Result.Builder<>().msg("用户名或密码错误").code(508).build());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(map.toString());
    }
}
