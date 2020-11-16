package com.yzh.demo.config.exception;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * 全局异常处理器
 *
 * @author yzh
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public Object CustomException(CustomException c){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",500);
        jsonObject.put("msg",c.getMessage());
        return jsonObject;
    }
}

