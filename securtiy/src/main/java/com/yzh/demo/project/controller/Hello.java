package com.yzh.demo.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yzh
 */
@RestController
public class Hello {
    //允许被user角色访问接口
    @GetMapping("/hello")
    public String hello() {
        return "hello jwt !";
    }
    //允许被admin角色访问的接口
    @GetMapping("/admin")
    public String admin() {
        return "hello admin !";
    }
    @GetMapping("/index")
    public String index(){
        return "index";
    }
}