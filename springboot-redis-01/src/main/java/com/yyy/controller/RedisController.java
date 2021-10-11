package com.yyy.controller;

import com.yyy.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    @Autowired
    RedisService redisService;
    @RequestMapping("/redis")
    public String test(){
        redisService.set("username",60,"admin");
        return "test-Redis";
    }

}
