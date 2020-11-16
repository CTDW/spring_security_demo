package com.yzh.demo.config.fillter;

import com.yzh.demo.project.server.imp.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(name);
        if (userDetails != null){
            return new UsernamePasswordAuthenticationToken(name, password,userDetails.getAuthorities());
        }
        else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
