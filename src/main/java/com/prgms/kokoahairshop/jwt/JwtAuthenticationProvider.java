package com.prgms.kokoahairshop.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtAuthenticationProvider {

    @Value("${jwt.client-secret}")
    private String clientSecret;

    @Value("${jwt.expire-seconds}")
    private long expireSeconds;

    // 토큰 발급
    public String createToken(String userName) {
        return JWT.create()
            .withSubject(userName)
            .withExpiresAt(new Date(System.currentTimeMillis() + expireSeconds * 1000))
            .withClaim("userName", userName)
            .sign(Algorithm.HMAC512(clientSecret));

    }

    // 토큰으로 userName 가져오기
    public String getUserNameFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(clientSecret)).build().verify(token)
            .getClaim("userName").asString();

    }

}

