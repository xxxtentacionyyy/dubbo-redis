package com.yyy.qg.controller;

import com.yyy.qg.common.Constants;
import com.yyy.qg.pojo.vo.Message;
import com.yyy.qg.utils.ActiveMQUtils;
import com.yyy.qg.utils.RedisUtil;
import com.yyy.qg.utils.YYY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QgController {

    @Autowired
    RedisUtil redisUtil;

    private Long lockExpire = 60L;

    @Autowired
    ActiveMQUtils activeMQUtils;

    @RequestMapping("/qg")
    @ResponseBody
    public String qg(String userId , String goodsId){
        Message message = new Message();
        message.setUserId(userId);
        message.setGoodsId(goodsId);

        activeMQUtils.sendQueueMessage("qg_message",message);

        return "排队成功，请耐心等待...";
    }

    @JmsListener(destination = "qg_message")
    public void consumeMessage(Message message){
        String goodsId = message.getGoodsId();
        String userId = message.getUserId();
        String result = "";
        while(!redisUtil.lock("lock_"+goodsId,lockExpire)){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String str = redisUtil.getStr("goods_" + goodsId);
        int stock = Integer.parseInt(str);
        if (stock > 0){
            String flag = redisUtil.getStr(userId + ":" + goodsId);
            if (flag != null && flag.equals("1")){
                result="用户"+userId+"已经抢购过>>>>>>>>>>>>>>>";
            }else{
                stock = stock - 1;
                redisUtil.setStr("goods_"+goodsId,stock+"");
                redisUtil.setStr(userId + ":" + goodsId,"1");
                result="用户"+userId+"抢购到1件商品****************";
            }
        }else{
            result = "库存已经被一扫而空，请客官下次再来---------------";
        }
        redisUtil.unLock("lock_"+goodsId);
        YYY.print(result);

    }
}
