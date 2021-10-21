package com.yyy.qg.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yyy.qg.dao.QgGoodsDao;
import com.yyy.qg.pojo.QgGoods;
import com.yyy.qg.service.QgGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Service
@Component
public class QgGoodsServiceImpl implements QgGoodsService {

    @Autowired
    private QgGoodsDao qgGoodsDao;

    @Override
    public QgGoods getQgGoodsById(String id) throws Exception {
        return qgGoodsDao.selectByPrimaryKey(id);
    }

    @Override
    public List<QgGoods> getQgGoodsListByMap(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public Integer getQgGoodsCountByMap(Map<String, Object> param) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxAddQgGoods(QgGoods qgGoods) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxModifyQgGoods(QgGoods qgGoods) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxDeleteQgGoodsById(String id) throws Exception {
        return null;
    }

    @Override
    public Integer qdtxBatchDeleteQgGoods(String ids) throws Exception {
        return null;
    }
}
