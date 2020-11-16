package com.yzh.demo.config.fillter;
import com.yzh.demo.project.server.imp.UserDetailsServiceImp;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



/**
 * 自定义
 * 登录第二步 ：身份验证提供者
 * @author Louis
 * @date Nov 20, 2018
 */
public class JwtAuthenticationProvider extends DaoAuthenticationProvider {

    public JwtAuthenticationProvider(UserDetailsServiceImp userDetailsService) {
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("登录验证");
        // 可以在此处覆写整个登录认证逻辑
        return super.authenticate(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        System.out.println("dsf");
        // 可以在此处覆写密码验证逻辑
        super.additionalAuthenticationChecks(userDetails, authentication);
    }

}