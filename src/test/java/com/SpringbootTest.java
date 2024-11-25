package com;

import com.security.JwtSecurityProperties;
import com.security.PasswordUtil;
import com.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@SpringBootTest
class SpringbootTest {

    static String key = "HuangJunkai030206WithHarutya961226";

    @Autowired
    private JwtSecurityProperties jwtSecurityProperties;
    @Autowired
    private PasswordUtil passwordUtil;

    @Test
    public void tokenTest() {
        //String token =jwtSecurityProperties.generateToken(2L);
       /* String token= "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJUT1AtVVNFUiIsImV4cCI6MjA0NzQ0ODk5NSwiaWF0IjoxNzMyMDg4OTk1LCJ1c2VySWQiOjJ9.N_RSUvqX27b6k-oK4E_VEYw7n-34togDLkMleftksOIBmH7fFvRuN91n9bAgtkUI";
        System.out.println("token: " + token);
        System.out.println(dateTransfer(jwtSecurityProperties.parseToken(token).getExpiration().toString()));
        System.out.println(dateTransfer(jwtSecurityProperties.parseToken(token)));
        System.out.println("UserID:"+jwtSecurityProperties.parseToken(token));
        System.out.println("userName:"+jwtSecurityProperties.parseToken(token));
        System.out.println("isExpired:"+jwtSecurityProperties.isTokenExpired(token));*/
    }

    @Test
    public void enumTest(){
        ResultCodeEnum[] enums = ResultCodeEnum.values();
        System.out.println(enums[0].name());
        System.out.println(enums[0].ordinal());
        System.out.println(ResultCodeEnum.valueOf("SUCCESS"));
    }

    @Test
    public void passwordEncode(){
        System.out.println(passwordUtil.encodePassword("harutya"));
    }
    private @NotNull String dateTransfer(String date){
        Instant instant = Instant.ofEpochMilli(Long.parseLong(date));
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}