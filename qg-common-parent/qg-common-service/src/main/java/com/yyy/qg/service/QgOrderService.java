package com.yyy.qg.service;
import com.yyy.qg.pojo.QgOrder;

import java.util.List;
import java.util.Map;
/**
* Created by shang-pc on 2015/11/7.
*/
public interface QgOrderService {

    QgOrder getQgOrderById(String id)throws Exception;

    List<QgOrder>	getQgOrderListByMap(Map<String,Object> param)throws Exception;

    Integer getQgOrderCountByMap(Map<String,Object> param)throws Exception;

    Integer qdtxAddQgOrder(QgOrder qgOrder)throws Exception;

    Integer qdtxModifyQgOrder(QgOrder qgOrder)throws Exception;

    Integer qdtxDeleteQgOrderById(String id)throws Exception;

    Integer qdtxBatchDeleteQgOrder(String ids)throws Exception;

    QgOrder getOrderByOrderNo(String orderNo) throws Exception;

}
