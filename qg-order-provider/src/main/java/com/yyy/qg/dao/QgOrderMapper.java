package com.yyy.qg.dao;

import com.yyy.qg.pojo.QgOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface QgOrderMapper{

    QgOrder getQgOrderById(@Param(value = "id") String id)throws
            Exception;
    List<QgOrder> getQgOrderListByMap(Map<String, Object> param)throws
            Exception;
    Integer getQgOrderCountByMap(Map<String, Object> param)throws
            Exception;
    Integer insertQgOrder(QgOrder qgOrder)throws Exception;
    Integer updateQgOrder(QgOrder qgOrder)throws Exception;
    Integer deleteQgOrderById(@Param(value = "id") String id)throws
            Exception;
    Integer batchDeleteQgOrder(Map<String, List<String>> params);

}
