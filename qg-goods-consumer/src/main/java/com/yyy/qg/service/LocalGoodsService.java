package com.yyy.qg.service;

import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.pojo.QgGoods;
import com.yyy.qg.pojo.QgGoodsMessage;
import com.yyy.qg.pojo.vo.Message;

import java.util.List;

public interface LocalGoodsService {

    /**
     * 根据id查询
     * @param id
     * @return
     * @throws Exception
     */
    ReturnResult queryGoodsById(String id) throws Exception;
    /**
     * 抢购商品
     * @param message
     * @throws Exception
     */
    void getGoods(QgGoodsMessage message) throws Exception;

    /**
     * 发送消息并对用户抢购记录做判断
     * @param token
     * @param goodsId
     * @return
     * @throws Exception
     */
    ReturnResult goodsGetMessage(String token, String goodsId) throws Exception;

    /**
     * 刷新商品状态
     * @param token
     * @param goodsId
     * @return
     */
    ReturnResult flushGetGoodsStatus(String token, String goodsId);

    /**
     * 获取全部商品信息
     * @return
     */
    List<QgGoods> getAll() throws Exception;

}
