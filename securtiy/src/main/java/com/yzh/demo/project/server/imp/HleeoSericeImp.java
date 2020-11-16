package com.yzh.demo.project.server.imp;

import com.yzh.demo.project.domain.User;
import com.yzh.demo.project.server.HelloSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HleeoSericeImp implements HelloSerice {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Resource
    private UserDetailsService userDetailsService;



    @Override
    public String login(User user) {
/*        String username = user.getUsername();
        String password = user.getPassword();
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken( username, password ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //final String token = jwtTokenUtil.generateToken(userDetails);
        return token;*/
        return null;
    }
}
