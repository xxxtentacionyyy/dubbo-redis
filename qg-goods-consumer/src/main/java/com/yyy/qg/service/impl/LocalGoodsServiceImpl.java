package com.yyy.qg.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.yyy.qg.common.Constants;
import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.dto.ReturnResultUtils;
import com.yyy.qg.exception.GoodsException;
import com.yyy.qg.pojo.*;
import com.yyy.qg.pojo.vo.GoodsVo;

import com.yyy.qg.service.*;
import com.yyy.qg.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.adapter.MessagingMessageListenerAdapter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class LocalGoodsServiceImpl implements LocalGoodsService {
    @Reference//远程调用对外暴露的接口
    private QgGoodsService qgGoodsService;
    @Reference//远程调用对外暴露的接口
    private QgGoodsTempStockService qgGoodsTempStockService;
    @Resource
    private ActiveMQUtils activeMQUtils;
    @Resource
    private RedisUtil redisUtil;
    @Reference
    private QgOrderService qgOrderService;
    @Reference
    QgGoodsMessageService qgGoodsMessageService;

    private Long lockExpire = 60L;
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
            // 计算当前的库存，需要查询出已经被购买过几个（状态1）和等待支付有几个（状态0）
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("goodsId",id);
            map.put("active",1);
            Integer qgGoodsTempStockCountByMap = qgGoodsTempStockService.getQgGoodsTempStockCountByMap(map);
//计算当前库存
            Integer currentStock = goodsVo.getStock() - qgGoodsTempStockCountByMap;
            goodsVo.setCurrentStock(currentStock);
//存入redis
//String token = Constants.goodsPrefix +TokenUtils.createToken(id,goodsVo.getId());
            redisUtil.setStr(Constants.goodsPrefix+id,JSONObject.toJSONString(goodsVo));
//redisUtil.setStr(token, JSONObject.toJSONString(goodsVo));
        }else{
            goodsVo = JSONObject.parseObject(str,GoodsVo.class);
        }
        return ReturnResultUtils.returnSuccess(goodsVo);
    }
    @JmsListener(destination = Constants.ActiveMQMessage.getMessage)
    public void getGoods(QgGoodsMessage message) throws Exception {
        //1.根据token获取用户
        String goodsId = message.getGoodsId();
        String userId = message.getUserId();
        // 判断用户是否上锁
        while(!redisUtil.lock(Constants.lockPrefix + goodsId,lockExpire)){
            Thread.sleep(300);
        }

        //3.判断库存是否大于0，如果大于0进入抢购环节
        String str1 = redisUtil.getStr(Constants.goodsPrefix + goodsId);
        GoodsVo goodsVo = JSONObject.parseObject(str1, GoodsVo.class);
        // 代表之前没有抢成功，并且库存充足，正式抢购成功
        // 4.更新库存， 临时库存表、更新redis
        QgGoodsTempStock qgGoodsTempStock = new QgGoodsTempStock();
        qgGoodsTempStock.setId(IdWorker.getId());
        qgGoodsTempStock.setUserId(userId);
        qgGoodsTempStock.setGoodsId(goodsVo.getId());
        qgGoodsTempStock.setStatus(Constants.StockStatus.lock);//0
        qgGoodsTempStock.setCreatedTime(new Date());
        qgGoodsTempStock.setUpdatedTime(new Date());
        qgGoodsTempStockService.qdtxAddQgGoodsTempStock(qgGoodsTempStock);
        goodsVo.setCurrentStock(goodsVo.getCurrentStock() -1);
        // 抢购下单操作（随便写一下）
        QgOrder qgOrder = new QgOrder();
        qgOrder.setId(IdWorker.getId());
        qgOrder.setStatus(Constants.StockStatus.lock);
        qgOrder.setGoodsId(qgGoodsTempStock.getGoodsId());
        qgOrder.setAmount(56.7);
        qgOrder.setStockId(qgGoodsTempStock.getId());
        qgOrder.setNum(1);
        qgOrder.setUserId(qgGoodsTempStock.getUserId());
        qgOrder.setOrderNo(IdWorker.getId());
        qgOrder.setCreatedTime(new Date());
        qgOrder.setUpdatedTime(new Date());
        qgOrderService.qdtxAddQgOrder(qgOrder);

        //redis
        redisUtil.setStr(Constants.goodsPrefix + goodsId, JSONObject.toJSONString(goodsVo));
        redisUtil.setStr(Constants.goodsPrefix + goodsId + ":" + userId,Constants.GetGoodsStatus.getSuccess);
        redisUtil.unLock(Constants.lockPrefix + goodsId);
    }

    @Override
    public ReturnResult goodsGetMessage(String token, String goodsId) throws Exception {
        String str = redisUtil.getStr(token);
        QgUser qgUser = JSONObject.parseObject(str, QgUser.class);
        QgGoodsMessage message = new QgGoodsMessage();
        message.setGoodsId(goodsId);
        message.setUserId(qgUser.getId());
        // 判断用户是否之前抢到过
        String flag = redisUtil.getStr(Constants.goodsPrefix + goodsId + ":" + qgUser.getId());
        String str1 = redisUtil.getStr(Constants.goodsPrefix + goodsId);
        GoodsVo goodsVo = JSONObject.parseObject(str1, GoodsVo.class);
        message.setId(IdWorker.getId());
        message.setAmount(goodsVo.getPrice());
        // 如果抢到了，就不用发送消息了
        if(EmptyUtils.isNotEmpty(flag) && flag.equals(Constants.GetGoodsStatus.getSuccess)){
            // 因为之前抢到过，现在又抢到了，所以解锁，继续让别的人来抢
            redisUtil.unLock(Constants.lockPrefix + goodsId);
            return ReturnResultUtils.returnFail(GoodsException.GOODS_REPEAT_GET.getCode(),GoodsException.GOODS_REPEAT_GET.getMessage());
        }else if(goodsVo.getCurrentStock() <= 0){
            redisUtil.unLock(Constants.lockPrefix + goodsId);
            redisUtil.setStr(Constants.goodsPrefix + goodsId + ":" + qgUser.getId(),Constants.GetGoodsStatus.getFail);
            return ReturnResultUtils.returnFail(GoodsException.GOODS_IS_CLEAR.getCode(), GoodsException.GOODS_IS_CLEAR.getMessage());
        }
        message.setStatus(Constants.GetGoodsStatus.getSuccess);
        message.setUserId(qgUser.getId());
        message.setCreatedTime(new Date());
        message.setUpdatedTime(message.getCreatedTime());
        qgGoodsMessageService.qdtxAddQgGoodsMessage(message);
        // 发送消息
        activeMQUtils.sendQueueMessage(Constants.ActiveMQMessage.getMessage,message);
        return ReturnResultUtils.returnSuccess();
    }

    @Override
    public ReturnResult flushGetGoodsStatus(String token, String goodsId) {
        String str = redisUtil.getStr(token);
        QgUser qgUser = JSONObject.parseObject(str, QgUser.class);
        String flag = redisUtil.getStr(Constants.goodsPrefix + goodsId + ":" + qgUser.getId());
        if (EmptyUtils.isEmpty(flag)){
            return ReturnResultUtils.returnFail(1103,null);
        }else if(EmptyUtils.isNotEmpty(flag) && flag.equals("0")){
            return ReturnResultUtils.returnFail(GoodsException.GOODS_IS_CLEAR.getCode(),GoodsException.GOODS_IS_CLEAR.getMessage());
        }else{
            return ReturnResultUtils.returnSuccess();
        }
    }

    @Override
    public List<QgGoods> getAll() throws Exception{
        return qgGoodsService.getQgGoodsListByMap(null);
    }
}
