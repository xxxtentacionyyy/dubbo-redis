package com.yyy.qg.utils;

import java.text.ParseException;
import java.util.Date;

/***
 * 生成token的工具类
 */
public class TokenUtils {

    public static String createToken(String prefix,String tail){
        String token= null;
        try {
            String timestemp=DateUtil.format(new Date(),"YYYY-MM-dd hh:mm:ss");
            String source=prefix+tail+timestemp;
            token= MD5.getMd5(source,16);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return token;
    }

}
