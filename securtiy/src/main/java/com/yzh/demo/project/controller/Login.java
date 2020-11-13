package com.yzh.demo.project.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class Login {

    @GetMapping("/index")
    public String index(){
        return "/index";
    }

}
