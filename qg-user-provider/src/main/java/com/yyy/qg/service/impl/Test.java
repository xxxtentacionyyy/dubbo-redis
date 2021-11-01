package com.yyy.qg.service.impl;

import com.yyy.qg.mapper.QgUserMapper;
import com.yyy.qg.pojo.QgUser;
import com.yyy.qg.utils.YYY;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        ApplicationContext ioc = new ClassPathXmlApplicationContext();
        QgUserMapper qgUserMapper = ioc.getBean("qgUserMapper", QgUserMapper.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("phone","13366055111");
        map.put("password","123456");
        QgUser qgUser = qgUserMapper.queryQgUserByPhoneAndPwd(map);
        YYY.print(qgUser);
    }
}
