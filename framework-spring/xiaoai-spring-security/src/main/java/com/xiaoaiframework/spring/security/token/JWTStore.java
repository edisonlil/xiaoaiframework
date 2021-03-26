package com.xiaoaiframework.spring.security.token;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author edison
 */
public class JWTStore {


    /**
     *
     * 通过HS512签名算法生成JWT-Token
     * @param id 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
     * @param issuer jwt签发人
     * @param body 载体
     * @param subject 主要信息一般是用户名
     * @param time 时间值
     * @param timeUnit 过期时间单位
     * @param secretKey 密钥 Base64
     * @return
     */
    public static String generate(String id,String issuer,String subject,Map<String, Object> body, long time, TimeUnit timeUnit,String secretKey){


        //JWT有效期
        Date date = new Date(System.currentTimeMillis()+timeUnit.toMillis(time));

        String token = Jwts.builder()
                .addClaims(body)
                .setId(id)
                .setIssuedAt(new Date())
                .setIssuer(issuer)
                .setSubject(subject)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        return token;

    }


    public static String generate(String subject,Map<String, Object> body, long time, TimeUnit timeUnit,String secretKey){

        return generate(null,null,subject, body, time, timeUnit, secretKey);

    }

    /**
     * 解析token
     * @param token jwt-token
     * @param secretKey 密钥
     * @return
     */
    public static Claims parse(String token,String secretKey){

        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(secretKey)                 //设置签名的秘钥
                .parseClaimsJws(token).getBody();     //设置需要解析的jwt

        return claims;
    }


    public static void main(String[] args) {

    }








}
