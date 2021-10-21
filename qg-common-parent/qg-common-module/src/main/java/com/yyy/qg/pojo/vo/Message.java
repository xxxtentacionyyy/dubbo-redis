package com.yyy.qg.pojo.vo;

import java.io.Serializable;

public class Message implements Serializable {

    private String userId;
    private String goodsId;
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

}
