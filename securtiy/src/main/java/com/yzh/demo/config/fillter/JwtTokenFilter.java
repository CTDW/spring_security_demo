package com.yzh.demo.config.fillter;

import com.yzh.demo.config.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author yzh
 *
 * 授权阶段：授权时解析令牌和设置登录状态
 *
 * JWT包含三部分数据：
 *
 * Header：头部，通常头部有两部分信息：
 * 声明类型，这里是JWT
 * 加密算法，自定义
 * 我们会对头部进行Base64Url编码（可解码），得到第一部分数据。
 *
 * Payload：载荷，就是有效数据，在官方文档中(RFC7519)，这里给了7个示例信息：
 * iss (issuer)：表示签发人
 * exp (expiration time)：表示token过期时间
 * sub (subject)：主题
 * aud (audience)：受众
 * nbf (Not Before)：生效时间
 * iat (Issued At)：签发时间
 * jti (JWT ID)：编号
 * 这部分也会采用Base64Url编码，得到第二部分数据。
 *
 * Signature：签名，是整个数据的认证信息。一般根据前两步的数据，再加上服务的的密钥secret
 * （密钥保存在服务端，不能泄露给客户端），通过Header中配置的加密算法生成。用于验证整个数据完整和可靠性。
 *
 *
 * token验证过滤器
 * 首先从请求头中提取出 authorization 字段，这个字段对应的value就是用户的token。
 * 将提取出来的token字符串转换为一个Claims对象，再从Claims对象中提取出当前用户名和用户角色，
 * 创建一个UsernamePasswordAuthenticationToken放到当前的Context中，
 * 然后执行过滤链使请求继续执行下去。
 */
public class JwtTokenFilter extends GenericFilterBean {
    
    @Autowired
    private UserDetailsService detailsService;
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException,CustomException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String jwtToken = req.getHeader("authorization");
        if (jwtToken == null && jwtToken.startsWith("bearer")){
            throw new CustomException("效验失败，请求头无效内容");
        }
        System.out.println(jwtToken);
        //jwt解析
        Claims claims = Jwts.parser().setSigningKey("sang@123").parseClaimsJws(jwtToken.replace("Bearer",""))
                .getBody();
        String username = claims.getSubject();
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //获取权限
            UserDetails userDetails = detailsService.loadUserByUsername(username);
            if (userDetails != null){
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                        req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(req,servletResponse);
    }

    public static void main(String[] args) {
        String s = "eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6IiIsInN1YiI6ImFkbWluIiwiZXhwIjoxNjA1NTI3NzQ1fQ.WKAe7iO56KWrm5B9HuBT1xpmwRLd6CaH2jIIZNCubu5UCsoIEXJ9wneRnliPmkU4DvtnnenT-tdnfHFEtqt5sg";

    }

}