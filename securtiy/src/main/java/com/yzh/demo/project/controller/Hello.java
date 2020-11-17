package com.yzh.demo.project.controller;

import com.yzh.demo.config.TokenConfig;
import com.yzh.demo.config.exception.CustomException;
import com.yzh.demo.project.server.HelloSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yzh
 */
@RestController
public class Hello {
    @Autowired
    private HelloSerice helloSerice;
    @Autowired
    private TokenConfig settings;

/*    @PostMapping("/login")
    public String login(@RequestBody User user){
        return  helloSerice.login(user);
    }*/

    //允许被user角色访问接口
    //@PreAuthorize("hasRole('ROLE_NOMAL')")
    @GetMapping("/hello")
    public String hello() {
        return "hello jwt !";
    }

    //允许被admin角色访问的接口
    //@PreAuthorize("hasRole('NOMAL')")
    @GetMapping("/admin")
    public String admin() {
        return "hello admin !";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/ecp_test")
    public Object test(){
        throw new CustomException("测试");
    }
}