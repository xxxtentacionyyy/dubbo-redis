package com.yyy.qg.controller;

import com.alibaba.fastjson.JSONObject;
import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.pojo.QgUser;
import com.yyy.qg.pojo.vo.OrderVo;
import com.yyy.qg.service.LocalOrderService;
import com.yyy.qg.utils.EmptyUtils;
import com.yyy.qg.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private LocalOrderService localOrderService;
    @Resource
    private RedisUtil redisUtil;
    /***
     * 查询订单
     * @return
     */
    @ResponseBody
    @RequestMapping("/v/queryOrderList")
    public ReturnResult<List<OrderVo>> queryOrderList(String token) throws
            Exception {
        return localOrderService.queryOrderList(token);
    }
    @ResponseBody
    @RequestMapping("/v/prepay")
    public ReturnResult prepay(String orderId, String token) throws Exception {
        return localOrderService.queryOrderById(orderId,token);
    }

    @RequestMapping("/orderList.html/{token}")
    public String toOrderList(Model model, @PathVariable("token") String token, HttpServletResponse response) throws Exception{
        response.addCookie(new Cookie("token",token));

        return "orderList";
    }

    @RequestMapping("/orderDetail.html")
    public String orderDetail(){

        return "orderDetail";
    }
}
