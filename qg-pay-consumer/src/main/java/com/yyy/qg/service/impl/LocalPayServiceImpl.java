package com.yyy.qg.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.github.wxpay.sdk.WXPay;
import com.yyy.qg.common.Constants;
import com.yyy.qg.config.AlipayConfig;
import com.yyy.qg.config.QgWxPayConfig;
import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.dto.ReturnResultUtils;
import com.yyy.qg.pojo.QgGoods;
import com.yyy.qg.pojo.QgGoodsTempStock;
import com.yyy.qg.pojo.QgOrder;
import com.yyy.qg.pojo.QgTrade;
import com.yyy.qg.service.*;
import com.yyy.qg.utils.IdWorker;
import com.yyy.qg.utils.YYY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
@Service
public class LocalPayServiceImpl implements LocalPayService {

    @Reference(timeout = 6000)
    private QgTradeService qgTradeService;

    @Reference
    private QgOrderService qgOrderService;

    @Reference
    private QgGoodsService qgGoodsService;

    @Reference
    private QgGoodsTempStockService qgGoodsTempStockService;

    @Autowired
    private QgWxPayConfig qgWxPayConfig;


    @Override
    public String dealPaySuccess(String orderNo, String tradeNo, Integer payMethod) throws Exception {
        QgOrder orderByOrderNo = qgOrderService.getOrderByOrderNo(orderNo);
        // 1.保存交易记录
        QgTrade qgTrade = new QgTrade();
        qgTrade.setId(IdWorker.getId());
        qgTrade.setTradeNo(tradeNo);
        qgTrade.setAmount(orderByOrderNo.getAmount());
        qgTrade.setPayMethod(payMethod);
        qgTrade.setCreatedTime(new Date());
        qgTrade.setUpdatedTime(new Date());
        qgTrade.setOrderNo(orderNo);
        qgTradeService.qdtxAddQgTrade(qgTrade);
        // 2.修改订单状态
        orderByOrderNo.setStatus(Constants.StockStatus.paySuccess);
        orderByOrderNo.setUpdatedTime(new Date());
        qgOrderService.qdtxModifyQgOrder(orderByOrderNo);
        // 3.修改临时库存信息
        QgGoodsTempStock tempStock = qgGoodsTempStockService.getQgGoodsTempStockById(orderByOrderNo.getStockId());
        tempStock.setStatus(Constants.StockStatus.paySuccess);
        tempStock.setUpdatedTime(new Date());
        qgGoodsTempStockService.qdtxModifyQgGoodsTempStock(tempStock);

        return orderByOrderNo.getId();
    }

    @Override
    public boolean validateDealPaySuccess(String tradeNo) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("tradeNo",tradeNo);
        Integer count = qgTradeService.getQgTradeCountByMap(map);

        return count > 0;
    }

    @Override
    public Map<String, String> getWxMap(String orderId) throws Exception {
        QgOrder qgOrder = qgOrderService.getQgOrderById(orderId);
        QgGoods qgGoods = qgGoodsService.getQgGoodsById(qgOrder.getGoodsId());
        WXPay wxPay = new WXPay(qgWxPayConfig);
        Map<String, String> map = new HashMap<>();
        map.put("body",qgGoods.getGoodsName());
        map.put("out_trade_no",qgOrder.getOrderNo());
        map.put("total_fee","1");
        map.put("spbill_create_ip","123.12.12.123");
        map.put("notify_url",qgWxPayConfig.getNotifyUrl());
        map.put("trade_type","NATIVE");
        map.put("product_id",qgGoods.getId());
        Map<String, String> stringStringMap = wxPay.unifiedOrder(map);

        return stringStringMap;
    }

    @Override
    public ReturnResult checkOrderSuccess(String orderId) throws Exception {
        QgOrder qgOrder = qgOrderService.getQgOrderById(orderId);
        if (qgOrder.getStatus() == (Constants.StockStatus.paySuccess)){
            YYY.print(">>>>>>>>>>>>>>>>>支付成功，Code为0<<<<<<<<<<<<<<");
            return ReturnResultUtils.returnSuccess();
        }
        return ReturnResultUtils.returnFail(9999,"支付失败！");
    }

    @Override
    public boolean validateAliPay(Map<String, String[]> requestParams) throws Exception {
        // 获取支付宝反馈的POST信息
        Map<String, String> params = new HashMap<>();
        for (Iterator<String> iter = requestParams.keySet().iterator();
             iter.hasNext();){
            String name = (String)iter.next();
            String valueStr = "";
            String values[] = (String[])requestParams.get(name);
            for (int i = 0; i < values.length; i++){
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码的时候使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"),"UTF-8");
            params.put(name,valueStr);
        }
        return AlipaySignature.rsaCheckV1(params,AlipayConfig.ALIPAYPUBLICKEY,AlipayConfig.CHARSET,AlipayConfig.SIGNTYPE);
    }

    @Override
    public String createAliForm(String orderId, Map<String, String[]> requestParams) throws Exception {
        Map<String, String> params = new HashMap<>();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();){
            String name = (String)iter.next();
            String valueStr = "";
            String values[] = (String[])requestParams.get(name);
            for (int i = 0; i < values.length; i++){
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码的时候使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"),"UTF-8");
            params.put(name,valueStr);
        }
        // 获得初始化的AliPayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.PRIVATEKEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAYPUBLICKEY, AlipayConfig.SIGNTYPE);
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        QgOrder order = qgOrderService.getQgOrderById(orderId);
        String out_trade_no = order.getOrderNo();
        String total_amount = order.getAmount() + "";
        QgGoods goods = qgGoodsService.getQgGoodsById(order.getGoodsId());
        String subject = goods.getGoodsName();
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        // 请求
        return alipayClient.pageExecute(alipayRequest).getBody();

    }


}
