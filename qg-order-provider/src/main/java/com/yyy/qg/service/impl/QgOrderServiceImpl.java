package com.yyy.qg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yyy.qg.dao.QgOrderMapper;
import com.yyy.qg.pojo.QgOrder;
import com.yyy.qg.service.QgOrderService;
import com.yyy.qg.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
@Service
public class QgOrderServiceImpl implements QgOrderService {

    @Autowired
    private QgOrderMapper qgOrderMapper;

    @Override
    public QgOrder getQgOrderById(String id) throws Exception {
        return qgOrderMapper.getQgOrderById(id);
    }

    @Override
    public List<QgOrder> getQgOrderListByMap(Map<String, Object> param) throws Exception {
        return qgOrderMapper.getQgOrderListByMap(param);
    }

    @Override
    public Integer getQgOrderCountByMap(Map<String, Object> param) throws Exception {
        return qgOrderMapper.getQgOrderCountByMap(param);
    }

    @Override
    public Integer qdtxAddQgOrder(QgOrder qgOrder) throws Exception {
        return qgOrderMapper.insertQgOrder(qgOrder);
    }

    @Override
    public Integer qdtxModifyQgOrder(QgOrder qgOrder) throws Exception {
        return qgOrderMapper.updateQgOrder(qgOrder);
    }

    @Override
    public Integer qdtxDeleteQgOrderById(String id) throws Exception {
        return qgOrderMapper.deleteQgOrderById(id);
    }

    @Override
    public Integer qdtxBatchDeleteQgOrder(String ids) throws Exception {
        HashMap<String, List<String>> map = new HashMap<>();
        String[] idArray = ids.split(",");
        List<String> idList = new ArrayList<>();
        for(String id : idArray){
            idList.add(id);
        }
        map.put("ids",idList);
        return qgOrderMapper.batchDeleteQgOrder(map);
    }

    @Override
    public QgOrder getOrderByOrderNo(String orderNo) throws Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo",orderNo);
        List<QgOrder> qgOrderListByMap = qgOrderMapper.getQgOrderListByMap(map);
        if (EmptyUtils.isNotEmpty(qgOrderListByMap)){
            return qgOrderListByMap.get(0);
        }
        return null;
    }
}
