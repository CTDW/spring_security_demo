package com.yzh.demo.config;


import com.yzh.demo.config.fillter.JwtLoginFilter;
import com.yzh.demo.config.fillter.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
//@EnableWebSecurity
//权限开启注解
//@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    /**
     * 密码加密
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 该方法用于访问数据库用户列表userDetial(待完善)
     * 这里写入临时用户至内存中
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin")
                .password("123").roles("admin")
                .and()
                .withUser("sang")
                .password("456")
                .roles("user");
    }

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
        http.authorizeRequests()
                //绑定user角色账户可访问/hello接口
                .antMatchers("/hello").hasRole("user")
                //admin角色可访问/admin接口
                .antMatchers("/admin").hasRole("admin")
                .antMatchers("/index").permitAll()
                //POST方法/login登录请求无需验证
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                //其余请求均需要认证
                .anyRequest().authenticated()
                .and()
                //登录页面路由配置
                .formLogin().loginPage("/login").permitAll()
                .and()
                //登出页面配置及登出重定向页面
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                //.logoutSuccessHandler()退出登录自定义配置
                .and()
                //addFilterBefore使用自定义过滤器替代原过滤器逻辑
                .addFilterBefore(new JwtLoginFilter("/login",authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenFilter(),UsernamePasswordAuthenticationFilter.class)
                //csrf 禁止使用
                .csrf().disable();
    }
}
