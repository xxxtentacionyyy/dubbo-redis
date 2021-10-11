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
import com.yyy.qg.utils.EmptyUtils;
import com.yyy.qg.utils.RedisUtil;
import com.yyy.qg.utils.TokenUtils;

import javax.annotation.Resource;
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
            Map<String, Object> map = new HashMap<>();
            map.put("token",token);
            result = ReturnResultUtils.returnSuccess(map);
        }else{
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
}
