package com.yzh.demo.config.fillter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzh.demo.utils.JwtUtil;
import com.yzh.demo.utils.SpringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author yzh
 * 用户登录的过滤器，在用户的登录的过滤器中校验用户是否登录成功，如果登录成功，
 * 则生成一个token返回给客户端，登录失败则给前端一个登录失败的提示。
 */

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {


    public JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
    }

    /**
     * 登录第一步：生成一个未效验token储存与上下文
     * @param req
     * @param resp
     * @return
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationException, IOException, ServletException {
        //实现自定义user用户类实现userdetial
        // 可以在此覆写尝试进行登录认证的逻辑，登录成功之后等操作不再此方法内
        // 如果使用此过滤器来触发登录认证流程，注意登录请求数据格式的问题
        // 此过滤器的用户名密码默认从request.getParameter()获取，但是这种
        // 读取方式不能读取到如 application/json 等 post 请求数据，需要把
        // 用户名密码的读取逻辑修改为到流中读取request.getInputStream()
        String body = getBody(req);
        JSONObject jsonObject = JSON.parseObject(body);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }
        username = username.trim();
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username,password));
    }

    /**
     * 认证成功响应，生成令牌，将token储存于请求头中的authorities
     * @param req
     * @param resp
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        String userName = authResult.getName();
        String passWord = authResult.getCredentials().toString();
        StringBuffer as = new StringBuffer();
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        for (GrantedAuthority authority : authorities) {
            as.append(authority.getAuthority())
                    .append(",");
        }
        as = (as.charAt(as.length()) == ',')? as.deleteCharAt(as.length()-1) :as;
        map.put("authorities",as);
        String token = JwtUtil.generateToken(new StringBuffer().append(userName)
                .append(":").append(passWord) + "", map);
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.write(new ObjectMapper().writeValueAsString(token));
        out.flush();
        out.close();
    }

    /**
     * 认证失败
     * @param req
     * @param resp
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse resp, AuthenticationException failed) throws IOException, ServletException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.write("登录失败!");
        out.flush();
        out.close();
    }


    /**
     * 获取请求Body
     * @param request
     * @return
     */
    public String getBody(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}