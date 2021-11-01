package com.yyy.qg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.yyy.qg.common.Constants;
import com.yyy.qg.config.WxConfig;
import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.dto.ReturnResultUtils;
import com.yyy.qg.dto.exception.CommonException;
import com.yyy.qg.pojo.QgGoods;
import com.yyy.qg.pojo.QgUser;
import com.yyy.qg.pojo.vo.GoodsVo;
import com.yyy.qg.service.LocalUserService;
import com.yyy.qg.service.QgGoodsService;
import com.yyy.qg.service.QgGoodsTempStockService;
import com.yyy.qg.utils.*;
import org.apache.catalina.Session;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class UserController {

    @Autowired
    LocalUserService localUserService;
    @Autowired
    KafkaUtil kafkaUtil;
    @Autowired
    WxConfig wxConfig;
    @Resource
    RedisUtil redisUtil;
    @Reference
    private QgGoodsTempStockService qgGoodsTempStockService;
    @Reference
    private QgGoodsService qgGoodsService;

    @RequestMapping("/login.html")
    public String hello(){
        return "login";
    }

    /***
     * 用户登录的方法
     * @param phone
     * @param password
     * @return
     */
//@RequestMapping("/doLogin")
    @ResponseBody
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
        YYY.print("开始向kafka发送日志>>>>>>>>>>>>>>>>>>");
        //写入日志
        kafkaUtil.sendInfoMessage(accessToken);
        JSONObject jsonObject = JSONObject.parseObject(accessToken);
        String access_token = jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");
        //获取用户信息
        String userInfoJson = UrlUtils.loadURL(wxConfig.reqUserInfoUrl(access_token, openid));
        //写入日志
        kafkaUtil.sendInfoMessage(userInfoJson);
        YYY.print("日志写入完毕>>>>>>>>>>>>>>>>>>>>>>");
        String wxUserToken = localUserService.createWxUserToken(userInfoJson);
//跳转至前端
        return "redirect:"+wxConfig.getSuccessUrl()+"/"+wxUserToken;
    }


    @RequestMapping("/v/loginOut")
    @ResponseBody
    public ReturnResult loginOut(HttpSession session, String token){
        String str = redisUtil.getStr(token);
        Map<String,Object> map = new HashMap<>();
        if (EmptyUtils.isNotEmpty(str)){
            QgUser qgUser = JSONObject.parseObject(str, QgUser.class);
            redisUtil.del(token);
            redisUtil.del(qgUser.getId());
            session.invalidate();
            return ReturnResultUtils.returnSuccess(map);
        }

        return ReturnResultUtils.returnFail(CommonException.USER_NO_LOGIN.getCode(),CommonException.USER_NO_LOGIN.getMessage());
    }
}
