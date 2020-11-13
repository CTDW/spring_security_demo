package com.yzh.demo.project.controller;

import com.yzh.demo.project.domain.LoginBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class Login {

    @GetMapping("/index")
    public String index(){
        return "/index";
    }

}
