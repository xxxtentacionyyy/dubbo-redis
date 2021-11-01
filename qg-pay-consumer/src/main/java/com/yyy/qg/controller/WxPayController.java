package com.yyy.qg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.yyy.qg.common.Constants;
import com.yyy.qg.config.QgWxPayConfig;
import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.dto.ReturnResultUtils;
import com.yyy.qg.exception.PayException;
import com.yyy.qg.service.LocalPayService;
import com.yyy.qg.service.QgGoodsService;
import com.yyy.qg.service.QgOrderService;
import com.yyy.qg.utils.EmptyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Controller
@RequestMapping("/api")
public class WxPayController {

    @Autowired
    private QgWxPayConfig qgWxPayConfig;

    @Reference(timeout = 60000)
    private QgGoodsService qgGoodsService;

    @Reference(timeout = 60000)
    private QgOrderService qgOrderService;

    @Autowired
    private LocalPayService localPayService;

    private String hello;

    /****
     * 请求微信统一下单接口获取codeUrl
     *
     * codeUrl:是微信官方提供的生成二维码的链接
     *
     * @return
     */

    @RequestMapping("/v/reqWxCode")
    @ResponseBody
    public ReturnResult reqWxCode(String orderId) throws Exception{
        Map<String, String> wxMap = localPayService.getWxMap(orderId);
        log.info("微信支付响应包含code_url的结果集为：{}",wxMap);
        if (wxMap.get("result_code").equals("SUCCESS") && wxMap.get("return_code").equals("SUCCESS")){
            String code_url = wxMap.get("code_url");
            Map<String, String> map = new HashMap<>();
            map.put("codeUrl",code_url);
            return ReturnResultUtils.returnSuccess(map);
        }else{
            return ReturnResultUtils.returnFail(PayException.WX_PAY_BUZY.getCode(), PayException.WX_PAY_BUZY.getMessage());
        }
    }

    /**
     * 接收微信官方响应的异步通知
     * @param res
     * @param req
     */
    @RequestMapping("/wxPayNotify")
    public void wxPayNotify(HttpServletResponse res , HttpServletRequest req){
        try {
            String notifyXml = getNotifyXml(req);
            Map<String, String> xmlMap = WXPayUtil.xmlToMap(notifyXml);
            log.info(">>>>>>>>>>>>>>>回调地址的结果集为:{}",xmlMap);
            boolean flag = WXPayUtil.isSignatureValid(xmlMap, qgWxPayConfig.getKey(), WXPayConstants.SignType.HMACSHA256);
            if (flag){
                // 执行业务逻辑
                String out_trade_no = xmlMap.get("out_trade_no");
                String transaction_id = xmlMap.get("transaction_id");
                //判断是否交易成功
                boolean dealFlag = localPayService.validateDealPaySuccess(out_trade_no);
                if (!dealFlag){
                    localPayService.dealPaySuccess(out_trade_no,transaction_id,Constants.PayMethod.wxPay);
                }
                // 给微信平台反馈
                Map<String, String> map = WXPayUtil.xmlToMap(notifyXml);
                map.put("return_code","SUCCESS");
                map.put("return_msg","OK");
                String toXml = WXPayUtil.mapToXml(map);
                hello = toXml;
                log.info("**********响应到前段的XML为: {}",toXml);
                res.getWriter().print(toXml);
                res.getWriter().flush();
                res.getWriter().close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取微信传递的xml
     * @param req
     * @return
     * @throws Exception
     */
    private String getNotifyXml(HttpServletRequest req) throws Exception{
        InputStream inputStream = req.getInputStream();
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        String xml = "";
        while ((xml = bufferedReader.readLine()) != null){
            stringBuffer.append(xml);
        }
        if (EmptyUtils.isNotEmpty(bufferedReader)){
            bufferedReader.close();
        }
        if (EmptyUtils.isNotEmpty(inputStream)){
            inputStream.close();
        }
        return stringBuffer.toString();

    }

    /**
     * 轮询查询订单是否支付成功
     * @param orderId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/v/checkOrderSuccess")
    public ReturnResult checkOrderSuccess(String orderId) throws Exception{
        return localPayService.checkOrderSuccess(orderId);
    }

    @RequestMapping("/wxCode.html")
    public String wxCode(){
        return "wxCode";
    }

    @RequestMapping("/paySuccess.html")
    public String paySuccess(){
        return "paySuccess";
    }

    @ResponseBody
    @RequestMapping("hello")
    public String hello(){
        return hello;
    }
}
