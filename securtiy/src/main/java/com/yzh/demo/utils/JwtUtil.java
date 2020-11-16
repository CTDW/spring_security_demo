package com.yzh.demo.utils;

import com.yzh.demo.project.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * jwt工具类
 * @author yzh
 */
public class JwtUtil {


    public static final String SUBJECT = "onehee";

    //过期时间，毫秒，一周
    public static final long EXPIRE = 1000*60*60*24*7;

    //秘钥
    public static final  String APPSECRET = "onehee666";

    /**
     * 生成jwt
     * @param user
     * @return
     */
    public static String geneJsonWebToken(User user){

        if(user == null || user.getId() == null || user.getUsername() == null
                || user.getPassword()== null){
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id",user.getId())
                .claim("username",user.getUsername())
                .claim("password",user.getPassword())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE))
                .signWith(SignatureAlgorithm.HS256,APPSECRET).compact();

        return token;
    }


    /**
     * 校验token
     * @param token
     * @return
     */
    public static Claims checkJWT(String token ){

        try{
            final Claims claims =  Jwts.parser().setSigningKey(APPSECRET).
                    parseClaimsJws(token).getBody();
            return  claims;
        }catch (Exception e){ }
        return null;

    }



}