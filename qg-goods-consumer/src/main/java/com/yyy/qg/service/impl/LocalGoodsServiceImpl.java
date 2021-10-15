package com.yyy.qg.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.yyy.qg.common.Constants;
import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.dto.ReturnResultUtils;
import com.yyy.qg.exception.GoodsException;
import com.yyy.qg.pojo.QgGoods;
import com.yyy.qg.pojo.QgGoodsTempStock;
import com.yyy.qg.pojo.QgUser;
import com.yyy.qg.pojo.vo.GoodsVo;
import com.yyy.qg.service.LocalGoodsService;
import com.yyy.qg.service.QgGoodsService;
import com.yyy.qg.service.QgGoodsTempStockService;
import com.yyy.qg.utils.EmptyUtils;
import com.yyy.qg.utils.IdWorker;
import com.yyy.qg.utils.RedisUtil;
import com.yyy.qg.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class LocalGoodsServiceImpl implements LocalGoodsService {
    @Reference//远程调用对外暴露的接口
    private QgGoodsService qgGoodsService;
    @Reference//远程调用对外暴露的接口
    private QgGoodsTempStockService qgGoodsTempStockService;
    @Resource
    private RedisUtil redisUtil;
    @Override
    public ReturnResult queryGoodsById(String id) throws Exception {
        GoodsVo goodsVo = null;
        //1.首先从redis中进行获取
        // 2.redis中如果没有的话，则去数据库查询，再缓存到redis中
        // 3.redis中如果有的话，则去redis中
        String str = redisUtil.getStr(Constants.goodsPrefix + id);
        if(EmptyUtils.isEmpty(str)){
            goodsVo = new GoodsVo();
            QgGoods qgGoods = qgGoodsService.getQgGoodsById(id);
            BeanUtils.copyProperties(qgGoods,goodsVo);
//goodsVo.setGoodsImg(qgGoods.getGoodsImg());
//计算当前的库存，需要查询出已经被购买过几个（状态1）和等待支付有几个（状态1）
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("goodsId",id);
            map.put("active",1);
            Integer qgGoodsTempStockCountByMap =
                    qgGoodsTempStockService.getQgGoodsTempStockCountByMap(map);
//计算当前库存
            Integer currentStock = goodsVo.getStock() -
                    qgGoodsTempStockCountByMap;
            goodsVo.setCurrentStock(currentStock);
//存入redis
//String token = Constants.goodsPrefix +
            TokenUtils.createToken(id,goodsVo.getId());
            redisUtil.setStr(Constants.goodsPrefix+id,JSONObject.toJSONString(goodsVo));
//redisUtil.setStr(token, JSONObject.toJSONString(goodsVo));
        }else{
            goodsVo = JSONObject.parseObject(str,GoodsVo.class);
        }
        return ReturnResultUtils.returnSuccess(goodsVo);
    }
    @Override
    public ReturnResult getGoods(String token, String goodsId) throws Exception
    {
//1.根据token获取用户
        String str = redisUtil.getStr(token);
        QgUser qgUser = JSONObject.parseObject(str, QgUser.class);
//2.查看用户是否已经抢购过该商品，如果抢购成功未支付，或者已经支付成功，则不能抢购
//需要查询出已经被购买过几个（状态1）和等待支付有几个（状态1）
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("goodsId",goodsId);
        map.put("active",1);
        map.put("userId",qgUser.getId());
        Integer qgGoodsTempStockCountByMap =
                qgGoodsTempStockService.getQgGoodsTempStockCountByMap(map);
        if(qgGoodsTempStockCountByMap > 0){
            return
                    ReturnResultUtils.returnFail(GoodsException.GOODS_REPEAT_GET.getCode(),GoodsException.GOODS_REPEAT_GET.getMessage());
        }
//3.判断库存是否大于0，如果大于0进入抢购环节
        String str1 = redisUtil.getStr(Constants.goodsPrefix + goodsId);
        GoodsVo goodsVo = JSONObject.parseObject(str1, GoodsVo.class);
        if(goodsVo.getCurrentStock() <= 0){
            return
                    ReturnResultUtils.returnFail(GoodsException.GOODS_IS_CLEAR.getCode(),GoodsException.GOODS_IS_CLEAR.getMessage());
        }
//4.更新库存， 临时库存表、更新redis
        QgGoodsTempStock qgGoodsTempStock = new QgGoodsTempStock();
        qgGoodsTempStock.setId(IdWorker.getId());
        qgGoodsTempStock.setUserId(qgUser.getId());
        qgGoodsTempStock.setGoodsId(goodsVo.getId());
        qgGoodsTempStock.setStatus(Constants.StockStatus.lock);//0
        qgGoodsTempStock.setCreatedTime(new Date());
        qgGoodsTempStock.setUpdatedTime(new Date());
        qgGoodsTempStockService.qdtxAddQgGoodsTempStock(qgGoodsTempStock);
        goodsVo.setCurrentStock(goodsVo.getCurrentStock() -1);
//redis
        redisUtil.setStr(Constants.goodsPrefix + goodsId, JSONObject.toJSONString(goodsVo));
        return ReturnResultUtils.returnSuccess();
    }
}
