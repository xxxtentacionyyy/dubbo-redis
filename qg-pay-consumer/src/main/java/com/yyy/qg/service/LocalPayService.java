package com.yyy.qg.service;

import com.yyy.qg.dto.ReturnResult;

import java.util.Map;

public interface LocalPayService {
    String dealPaySuccess(String orderNo, String tradeNo, Integer
            payMethod) throws Exception;
    boolean validateDealPaySuccess(String tradeNo)throws Exception;
    Map<String,String> getWxMap(String orderId)throws Exception;
    ReturnResult checkOrderSuccess(String orderId)throws Exception;
}
