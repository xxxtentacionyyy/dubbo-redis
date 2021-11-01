package com.yyy.qg.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.yyy.qg.common.Constants;
import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.dto.ReturnResultUtils;
import com.yyy.qg.exception.OrderException;
import com.yyy.qg.pojo.QgGoods;
import com.yyy.qg.pojo.QgOrder;
import com.yyy.qg.pojo.QgUser;
import com.yyy.qg.pojo.vo.GoodsVo;
import com.yyy.qg.pojo.vo.OrderVo;
import com.yyy.qg.service.LocalOrderService;
import com.yyy.qg.service.QgGoodsService;
import com.yyy.qg.service.QgOrderService;
import com.yyy.qg.utils.EmptyUtils;
import com.yyy.qg.utils.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocalOrderServiceImpl implements LocalOrderService {

    @Reference
    private QgOrderService qgOrderService;
    @Reference
    private QgGoodsService qgGoodsService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ReturnResult<List<OrderVo>> queryOrderList(String token) throws Exception {
        String str = redisUtil.getStr(token);
        List<OrderVo> orders = null;
        QgUser qgUser = JSONObject.parseObject(str, QgUser.class);
        Map<String, Object> param = new HashMap<>();
        param.put("userId",qgUser.getId());
        List<QgOrder> orderList = qgOrderService.getQgOrderListByMap(param);
        // 组装OrderVo信息
        if (EmptyUtils.isNotEmpty(orderList)){
            orders = new ArrayList<>();
            for (QgOrder order : orderList){
                OrderVo orderVo = new OrderVo();
                BeanUtils.copyProperties(order,orderVo);
                String goodsInfo = redisUtil.getStr(Constants.goodsPrefix + order.getGoodsId());
                if (EmptyUtils.isNotEmpty(goodsInfo)){
                    GoodsVo goodsVo = JSONObject.parseObject(goodsInfo, GoodsVo.class);
                    orderVo.setGoodsImg(goodsVo.getGoodsImg());
                }else{
                    QgGoods qgGoods = qgGoodsService.getQgGoodsById(order.getGoodsId());
                    orderVo.setGoodsImg(qgGoods.getGoodsImg());
                }
                orders.add(orderVo);
            }

            return ReturnResultUtils.returnSuccess(orders);
        }


        return ReturnResultUtils.returnFail(OrderException.ORDER_NOT_EXIST.getCode(), OrderException.ORDER_NOT_EXIST.getMessage());
    }

    @Override
    public ReturnResult<QgOrder> queryOrderById(String orderId, String token) throws Exception {
        QgOrder qgOrder = qgOrderService.getQgOrderById(orderId);
        String str = redisUtil.getStr(token);
        QgUser qgUser = JSONObject.parseObject(str, QgUser.class);
        if (EmptyUtils.isEmpty(qgOrder) || !qgOrder.getUserId().equals(qgUser.getId())){
            return ReturnResultUtils.returnFail(OrderException.ORDER_NOT_EXIST.getCode(), OrderException.ORDER_NOT_EXIST.getMessage());
        }
        return ReturnResultUtils.returnSuccess(qgOrder);
    }
}
