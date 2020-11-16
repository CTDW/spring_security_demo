package com.yzh.demo.project.server.imp;

import com.yzh.demo.project.domain.User;
import com.yzh.demo.project.mapper.UserDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yzh
 */
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserDetailsMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = mapper.getUserByName(s);
        List<User.Role> list = mapper.getUserRoleByName(s);
        user.setRoles(list);
        return user;
    }
}
