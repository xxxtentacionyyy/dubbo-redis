package com.yyy.qg.service;

import com.yyy.qg.dto.ReturnResult;

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
     * @param token
     * @param goodsId
     * @return
     * @throws Exception
     */
    ReturnResult getGoods(String token,String goodsId) throws Exception;

}
