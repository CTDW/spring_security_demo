package com.yzh.demo.config;


import com.yzh.demo.config.fillter.JwtAuthenticationFilter;
import com.yzh.demo.config.fillter.JwtLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author 10037
 * 1. 访问路径URL的授权策略，如登录、Swagger访问免登录认证等
 *
 * 2. 指定了登录认证流程过滤器 JwtLoginFilter，由它来触发登录认证
 *
 * 3. 指定了自定义身份认证组件 JwtAuthenticationProvider，并注入 UserDetailsService
 *
 * 4. 指定了访问控制过滤器 JwtAuthenticationFilter，在授权时解析令牌和设置登录状态
 *
 * 5. 指定了退出登录处理器，因为是前后端分离，防止内置的登录处理器在后台进行跳转
 */
@Configuration
@EnableWebSecurity
@Component
//权限开启注解
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http     //csrf 禁止使用
                .csrf().disable()
                //设置访问地址权限
                .authorizeRequests()
                //允许所有用户访问登录的路由地址
                .antMatchers("/login").permitAll()
                //其他请求均需要认证
                .anyRequest().authenticated()
                .and()
                //自定义表单登录界面
                .formLogin()
                //指定登录页路由
                .loginPage("/login")
                //允许所有用户登录
                .permitAll()
                .and()
                //退出页面测试
                .logout()
                //推出路由
                .logoutUrl("/logout")
                //退出成功后指向页面
                .logoutSuccessUrl("/index")
                //.logoutSuccessHandler(//需要继承logoutSuccessHandler)  //指定成功注销后处理类 如果使用了logoutSuccessHandler()的话， logoutSuccessUrl()就会失效
                //注销处理类，解决注销后请求头及一系列的设置
                //.addLogoutHandler(logoutHandler)
                //.deleteCookies(cookieNamesToClear);//指定注销成功后remove cookies
        http


    }


}
