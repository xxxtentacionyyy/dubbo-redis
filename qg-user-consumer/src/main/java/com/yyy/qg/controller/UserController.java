package com.yyy.qg.controller;

import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.dto.ReturnResultUtils;
import com.yyy.qg.service.LocalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    LocalUserService localUserService;
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    /***
     * 用户登录的方法
     * @param phone
     * @param password
     * @return
     */
//@RequestMapping("/doLogin")
    @PostMapping("/doLogin")
    public ReturnResult doLogin(HttpServletResponse response, String phone,
                                String password) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        return localUserService.validateToken(phone, password);
    }
    @RequestMapping("/loginOut")
    public ReturnResult loginOut(String token) throws Exception{
        return localUserService.removeToken(token);
    }
}
