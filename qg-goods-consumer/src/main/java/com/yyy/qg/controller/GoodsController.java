package com.yyy.qg.controller;

import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.service.LocalGoodsService;
import com.yyy.qg.utils.RedisUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class GoodsController {
    @Resource
    private LocalGoodsService localGoodsService;
    @Resource
    private RedisUtil redisUtil;
    /**
     * 获取商品信息，获取库存信息
     */
    @RequestMapping("/queryGoodsById")
    public ReturnResult queryGoodsById(String id) throws Exception{
        return localGoodsService.queryGoodsById(id);
    }
    /**
     * 抢购商品
     * @param token
     * @param goodsId
     * @return
     * @throws Exception
     */
    @RequestMapping("/getGoods")
    public ReturnResult getGoods(String token,String goodsId) throws Exception{
        return localGoodsService.getGoods(token,goodsId);
    }
}
