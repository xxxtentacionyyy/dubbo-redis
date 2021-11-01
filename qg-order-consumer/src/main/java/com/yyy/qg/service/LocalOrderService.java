package com.yyy.qg.service;

import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.pojo.QgOrder;
import com.yyy.qg.pojo.vo.OrderVo;

import java.util.List;

public interface LocalOrderService {
    /***
     * 查询订单列表
     * @return
     * @throws Exception
     */
    ReturnResult<List<OrderVo>> queryOrderList(String token)throws Exception;
    /***
     * 根据订单id查询订单
     * @param orderId
     * @param token
     * @return
     * @throws Exception
     */
    ReturnResult<QgOrder> queryOrderById(String orderId, String token)throws Exception;
}
