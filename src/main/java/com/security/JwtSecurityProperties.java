package com.security;

import com.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "jwt.token")
public class JwtSecurityProperties {
    private long tokenExpiration;
    private String tokenSignKey;
    private static SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(tokenSignKey.getBytes());
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    /**
     * 生成token
     *
     * @return token:String
     */
    public String generateToken(User user) {
        Long uId = Long.valueOf(user.getUId());
        String userName = user.getUsername();
        return Jwts.builder()
                .header()
                .keyId(getUUID()) //token的唯一id
                .and()
                .subject("TOP-USER")
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 10))//有效时间两小时
                .issuedAt(new Date(System.currentTimeMillis()+604800000))
                .claim("uId", uId)
                .claim("username", userName)
                .signWith(key)
                .compact();
    }

    /**
     * 判断token是否过期
     *
     * @param token String
     * @return boolean
     */
    public boolean isTokenExpired(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date(System.currentTimeMillis()));
        } catch (JwtException e) {
            log.error(e.getMessage());
        }
        return true;
    }

    /**
     * 解析token
     * @param token String
     * @return String
     */
    private Claims parseToken(String token) {
         return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromToken(String token) {
        return parseToken(token).get("username", String.class);
    }
    public Integer getUIdFromToken(String token) {
        return parseToken(token).get("uId", Integer.class);
    }

}
