package com.yyy.qg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yyy.qg.dao.QgGoodsMessageDao;
import com.yyy.qg.pojo.QgGoodsMessage;
import com.yyy.qg.service.QgGoodsMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Service
@Component
public class QgGoodsMessageServiceImpl implements QgGoodsMessageService {

    @Autowired
    private QgGoodsMessageDao qgGoodsMessageDao;
    @Override
    public QgGoodsMessage getQgGoodsMessageById(String id) throws Exception {
        return qgGoodsMessageDao.queryById(id);
    }

    @Override
    public List<QgGoodsMessage> getQgGoodsMessageListByMap(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public Integer getQgGoodsMessageCountByMap(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxAddQgGoodsMessage(QgGoodsMessage qgGoodsMessage) throws Exception {
        return qgGoodsMessageDao.insert(qgGoodsMessage);
    }

    @Override
    public Integer qdtxModifyQgGoodsMessage(QgGoodsMessage qgGoodsMessage) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxDeleteQgGoodsMessageById(String id) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxBatchDeleteQgGoodsMessage(String ids) throws Exception {
        return null;
    }
}
