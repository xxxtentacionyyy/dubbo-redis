package com.yyy.qg.controller;

import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.service.LocalGoodsService;
import com.yyy.qg.utils.RedisUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/queryGoodsById")
    public ReturnResult queryGoodsById(String id) throws Exception{
        return localGoodsService.queryGoodsById(id);
    }
    /**
     * 发送消息，抢购商品
     * @param token
     * @param goodsId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/getGoods")
    public ReturnResult getGoods(String token,String goodsId) throws Exception{
        return localGoodsService.goodsGetMessage(token,goodsId);
    }

    @PostMapping("/flushResult")
    @ResponseBody
    public ReturnResult flushResult(String token,String goodsId){
        return localGoodsService.flushGetGoodsStatus(token,goodsId);
    }

}
