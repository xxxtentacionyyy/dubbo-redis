package com.yyy.qg.service;

import com.yyy.qg.dto.ReturnResult;

public interface LocalUserService {

    ReturnResult validateToken(String phone,String password) throws Exception;

    ReturnResult removeToken(String token) throws Exception;

    /**
     * 微信登录功能
     * @param userInfoJsonstr
     * @return
     * @throws Exception
     */
    String createWxUserToken(String userInfoJsonstr) throws Exception;


}
