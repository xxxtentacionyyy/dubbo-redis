package com.yyy.qg.dao;

import com.yyy.qg.pojo.QgGoods;

/**
 * @Entity com.yyy.qg.pojo.QgGoods
 */
public interface QgGoodsDao {

    int deleteByPrimaryKey(String id);

    int insert(QgGoods record);

    int insertSelective(QgGoods record);

    QgGoods selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(QgGoods record);

    int updateByPrimaryKey(QgGoods record);

}
