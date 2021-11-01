package com.yyy.qg.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.yyy.qg.common.Constants;
import com.yyy.qg.dto.ReturnResult;
import com.yyy.qg.pojo.QgGoods;
import com.yyy.qg.pojo.QgUser;
import com.yyy.qg.pojo.vo.GoodsVo;
import com.yyy.qg.service.LocalGoodsService;
import com.yyy.qg.service.QgGoodsTempStockService;
import com.yyy.qg.utils.EmptyUtils;
import com.yyy.qg.utils.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
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
    @PostMapping("/v/queryGoodsById")
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
    @PostMapping("/v/getGoods")
    public ReturnResult getGoods(String token,String goodsId) throws Exception{
        return localGoodsService.goodsGetMessage(token,goodsId);
    }

    @PostMapping("/v/flushResult")
    @ResponseBody
    public ReturnResult flushResult(String token,String goodsId){
        return localGoodsService.flushGetGoodsStatus(token,goodsId);
    }

    @RequestMapping("/index.html/{token}")
    public String index(Model model, @PathVariable("token")String token, HttpServletResponse response){
        response.addCookie(new Cookie("token",token));
        String str = redisUtil.getStr(Constants.goodsPrefix + "1");
        GoodsVo goodsVo = JSONObject.parseObject(str, GoodsVo.class);
        model.addAttribute("goods",goodsVo);
        return "index";
    }


}
