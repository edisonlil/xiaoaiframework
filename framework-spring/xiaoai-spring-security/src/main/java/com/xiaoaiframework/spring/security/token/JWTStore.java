package com.xiaoaiframework.spring.security.token;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.impl.crypto.JwtSignatureValidator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author edison
 */
public class JWTStore {


    /**
     *
     * 通过HS512签名算法生成JWT-Token
     * @param body 载体
     * @param subject 主要信息一般是用户名
     * @param time 时间值
     * @param timeUnit 过期时间单位
     * @param secretKey 密钥
     * @return
     */
    public static String generate(Map<String, Object> body, String subject, long time, TimeUnit timeUnit,String secretKey){


        Date date = new Date(System.currentTimeMillis()+timeUnit.toMillis(time));

        String token = Jwts.builder()
                .addClaims(body)
                .setSubject(subject)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();


        return token;

    }

    /**
     *
     * @return
     */
    public static String verify(String token,String secretKey){

        return null;
    }

    public static void main(String[] args) {


        Map<String, Object> body = new HashMap<>();
        body.put("name","zzz");

        System.out.println(generate(body,"zst",1000,TimeUnit.HOURS,"123456"));
    }









}
