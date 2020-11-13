package com.yzh.demo.project.server.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDao.findUserByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("找不到该账户信息！");//抛出异常，会根据配置跳到登录失败页面

        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();//GrantedAuthority是security提供的权限类，

        getRoles(user, list);//获取角色，放到list里面

        org.springframework.security.core.userdetails.User auth_user = new
                org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), list);//返回包括权限角色的User给security
        return auth_user;
    }

    /**
     * 获取所属角色
     *
     * @param user
     * @param list
     */
    public void getRoles(User user, List<GrantedAuthority> list) {
        for (String role : user.getRoles().split(",")) {
            //权限如果前缀是ROLE_，security就会认为这是个角色信息，而不是权限，例如ROLE_MENBER就是MENBER角色，CAN_SEND就是CAN_SEND权限
            list.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
    }

}