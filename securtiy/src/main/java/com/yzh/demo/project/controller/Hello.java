package com.yzh.demo.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.WriterException;
import com.yzh.demo.config.TokenConfig;
import com.yzh.demo.config.exception.CustomException;
import com.yzh.demo.project.server.HelloSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
    @PreAuthorize("hasRole('ROLE_NOMAL')")
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

    @GetMapping("/image")
    public ResponseEntity<byte[]> get(){
        String info = "This is my first QR Code";
        byte[] qrcode = null;
        try {
            qrcode = QRCodeGenerator.getQRCodeImage(info, 360, 360);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }

        // Set headers
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]> (qrcode, headers, HttpStatus.CREATED);
    }
}