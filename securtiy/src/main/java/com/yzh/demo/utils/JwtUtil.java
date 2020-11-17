package com.yzh.demo.utils;


import com.yzh.demo.config.TokenConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 * @author yzh
 */
@Slf4j
public class JwtUtil {


    /**
     * 签发/生成token
     * issuer 签发人
     * subject 代表这个JWT的主体，即他的所有人，一般是用户ID
     * claims 储存在jwt里的信息(键值对)，一般是放些用户的权限/角色信息
     * ttlMillis 有效时间(毫秒)
     * secret 密钥
     */
    public static String generateToken(String subject, Map<String, Object> claims) {
        TokenConfig setting = SpringUtils.getBean(TokenConfig.class);
        //加密方式
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //当前时间戳，并转为日期
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //String printBase64Binary(byte[])就是将字节数组做base64编码，byte[] parseBase64Binary(String) 就是将Base64编码后的String还原成字节数组。
        byte[] signingKey = DatatypeConverter.parseBase64Binary(setting.getSecretKey());
        //这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder();
        //如果claims不为空，就加到JWT的载荷里面去
        if(null!=claims){
            builder.setClaims(claims);
        }
        if (!StringUtils.isEmpty(subject)) {
            builder.setSubject(subject);
        }
        builder.setIssuer(setting.getSecretKey());
        //签发时间
        builder.setIssuedAt(now);
        long ttlMillis = setting.getAccessTokenExpireTime().toMillis();
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            //过期时间
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        builder.signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }

    /**
     * 解析令牌 获取数据声明
     * 拿到用户及用户的角色、权限等信息
     */
    public static Claims getClaimsFromToken(String token) {
        TokenConfig setting = SpringUtils.getBean(TokenConfig.class);
        Claims claims;
        try {
            //用密钥(必字节数组)解析jwt，获取body（有效载荷）
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(setting.getSecretKey())).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            //解析不了，这个token就是无效的
            claims = null;
        }
        return claims;
    }





    /**
     * 验证token 是否过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            //首先解析，如果能解析成功，证明我服务器签发的
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            //过期时间和当前时间比较，如果过期时间在当前时间之前，返回true，表示已过期；否则返回false，没过期
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("error={}",e);
            //解析失败，抛出异常，返回true，表示已过期
            return true;
        }
    }
    /**
     * 校验令牌
     */
    public static Boolean validateToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return (null!=claimsFromToken && !isTokenExpired(token));
    }

    /**
     * 刷新token
     * 如果是过期刷新，claims/载荷 不变；
     * 如果主动刷新，claims/载荷 改变【一般是权限/角色改变的时候去主动刷新】
     */
    public static String refreshToken(String refreshToken,Map<String, Object> claims) {
        String refreshedToken;
        try {
            Claims parserclaims = getClaimsFromToken(refreshToken);
            /**
             * 如果传入的claims为空，说明是过期刷新，原先的用户信息不变，claims引用上个token里的内容
             */
            if(null==claims){
                claims=parserclaims;
            }
            /**
             * 不为空，根据传入的claims【用户信息】，生成新的Token
             */
            refreshedToken = generateToken(parserclaims.getSubject(),claims);
        } catch (Exception e) {
            refreshedToken = null;
            log.error("error={}",e);
        }
        return refreshedToken;
    }

    /**
     * 获取token的剩余过期时间
     */
    public static long getRemainingTime(String token){
        long result=0;
        try {
            long nowMillis = System.currentTimeMillis();
            result= getClaimsFromToken(token).getExpiration().getTime()-nowMillis;
        } catch (Exception e) {
            log.error("error={}",e);
        }
        return result;
    }

}
