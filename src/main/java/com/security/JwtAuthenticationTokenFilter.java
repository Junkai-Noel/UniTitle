package com.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component//extends OncePerRequestFilter
public class JwtAuthenticationTokenFilter {

    private final JwtSecurityProperties jwtSecurityProperties;

    @Autowired
    public JwtAuthenticationTokenFilter(JwtSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }

    /*@Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {


        String authToken = request.getHeader("token");
        if(authToken == null || authToken.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }
        authToken = authToken.substring(7);
        Integer uId = jwtSecurityProperties.getUIdFromToken(authToken);

    }*/
}
