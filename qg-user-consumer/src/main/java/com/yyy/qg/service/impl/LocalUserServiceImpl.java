package com.yyy.qg.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yyy.qg.common.Constants;
import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.dto.ReturnResultUtils;
import com.yyy.qg.exception.UserException;
import com.yyy.qg.pojo.QgUser;
import com.yyy.qg.service.LocalUserService;
import com.yyy.qg.service.QgUserService;
import com.yyy.qg.utils.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LocalUserServiceImpl implements LocalUserService {
    @Reference(interfaceClass = QgUserService.class)
    QgUserService qgUserService;

    @Resource
    RedisUtil redisUtil;
    @Override
    public ReturnResult validateToken(String phone, String password) throws Exception {
        String token = null;
        Map<String, Object>map = new HashMap<>();
        QgUser qgUser = qgUserService.queryQgUserByPhoneAndPwd(phone, password);
        ReturnResult result = null;
        // 判断是否登陆过
        if (EmptyUtils.isNotEmpty(qgUser)){
            // 如果登录过，把原来的记录删除
            String str = redisUtil.getStr(qgUser.getId());
            if (EmptyUtils.isNotEmpty(str)){
                redisUtil.del(qgUser.getId());
                redisUtil.del(str);
            }
            // 没登录过把token存入缓存中
            token = Constants.tokenPrefix + TokenUtils.createToken(qgUser.getId(), qgUser.getPhone());
            redisUtil.setStr(qgUser.getId(),token,Constants.loginExpire);
            redisUtil.setStr(token, JSON.toJSONString(qgUser),Constants.loginExpire);
            map.put("token",token);
            map.put("code",0);
            result = ReturnResultUtils.returnSuccess(map);
        }else{
            map.put("code",-1);
            result = ReturnResultUtils.returnFail(UserException.USER_PASSWORD_ERROR.getCode(), UserException.USER_PASSWORD_ERROR.getMessage());
        }
        return result;
    }

    @Override
    public ReturnResult removeToken(String token) throws Exception {
        String str = redisUtil.getStr(token);
        QgUser qgUser = JSON.parseObject(str, QgUser.class);
        redisUtil.del(qgUser.getId());
        redisUtil.del(token);
        return ReturnResultUtils.returnSuccess();
    }

    @Override
    public String createWxUserToken(String userInfoJsonstr) throws Exception {
        YYY.print("获取微信用户信息成功，下一步创建Token>>>>>>>>>>>>>>>>>>");
        JSONObject jsonObject = JSONObject.parseObject(userInfoJsonstr);
        String openid = jsonObject.getString("openid");
        QgUser qgUser = new QgUser();
        qgUser.setId(IdWorker.getId());
        qgUser.setWxUserId(openid);
        qgUser.setRealName(jsonObject.getString("nickname"));
        qgUser.setCreatedTime(new Date());
        qgUser.setUpdatedTime(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("wxUserId",openid);
        Integer count = qgUserService.getQgUserCountByMap(map);
        //插入数据库完成
        if (count < 1 ){
            qgUserService.qdtxAddQgUser(qgUser);
        }
        YYY.print("Token创建成功，执行用户数据插入数据库成功>>>>>>>>>>>>");
        //存入redis
        if (EmptyUtils.isNotEmpty(redisUtil.getStr(qgUser.getId()))){
            redisUtil.del(redisUtil.getStr(qgUser.getId()));
            redisUtil.del(qgUser.getId());
        }
        String token = Constants.tokenPrefix + TokenUtils.createToken(qgUser.getId(),qgUser.getWxUserId());
        redisUtil.setStr(qgUser.getId(),token,Constants.loginExpire);
        redisUtil.setStr(token,JSONObject.toJSONString(qgUser),Constants.loginExpire);
        YYY.print("token存入redis");
        return token;
    }
}
