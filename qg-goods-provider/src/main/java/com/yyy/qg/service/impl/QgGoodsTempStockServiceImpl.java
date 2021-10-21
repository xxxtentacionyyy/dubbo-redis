package com.yyy.qg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yyy.qg.dao.QgGoodsTempStockDao;
import com.yyy.qg.pojo.QgGoodsTempStock;
import com.yyy.qg.service.QgGoodsTempStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Service
@Component
public class QgGoodsTempStockServiceImpl implements QgGoodsTempStockService {

    @Autowired
    private QgGoodsTempStockDao qgGoodsTempStockDao;
    @Override
    public QgGoodsTempStock getQgGoodsTempStockById(String id) throws Exception {
        return qgGoodsTempStockDao.queryById(id);
    }

    @Override
    public List<QgGoodsTempStock> getQgGoodsTempStockListByMap(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public Integer getQgGoodsTempStockCountByMap(Map<String, Object> param) throws Exception {
        return qgGoodsTempStockDao.getQgGoodsTempStockCountByMap(param);
    }

    @Override
    public Integer qdtxAddQgGoodsTempStock(QgGoodsTempStock qgGoodsTempStock) throws Exception {
        return qgGoodsTempStockDao.insert(qgGoodsTempStock);
    }

    @Override
    public Integer qdtxModifyQgGoodsTempStock(QgGoodsTempStock qgGoodsTempStock) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxDeleteQgGoodsTempStockById(String id) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxBatchDeleteQgGoodsTempStock(String ids) throws Exception {
        return null;
    }
}
