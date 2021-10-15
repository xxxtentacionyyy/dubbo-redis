package com.yyy.qg.controller;

import com.alibaba.fastjson.JSONObject;
import com.yyy.qg.config.WxConfig;
import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.dto.ReturnResultUtils;
import com.yyy.qg.service.LocalUserService;
import com.yyy.qg.utils.KafkaUtil;
import com.yyy.qg.utils.UrlUtils;
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
    @Autowired
    KafkaUtil kafkaUtil;
    @Autowired
    WxConfig wxConfig;
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

    @RequestMapping("/wx/login")
    public String toWxLogin() throws Exception{
        return "redirect:"+wxConfig.reqCodeUrl();
    }
    @RequestMapping("/wx/callBack")
    public String callBack(String code) throws Exception{
        //获取access_token的json字符串
        String accessToken = UrlUtils.loadURL(wxConfig.reqAccessTokenUrl(code));
        //写入日志
        kafkaUtil.sendInfoMessage(accessToken);
        JSONObject jsonObject = JSONObject.parseObject(accessToken);
        String access_token = jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");
        //获取用户信息
        String userInfoJson = UrlUtils.loadURL(wxConfig.reqUserInfoUrl(access_token, openid));
        //写入日志
        kafkaUtil.sendInfoMessage(userInfoJson);
        String wxUserToken = localUserService.createWxUserToken(userInfoJson);
//跳转至前端
        return "redirect:"+wxConfig.getSuccessUrl()+"?token="+wxUserToken;
    }
}
