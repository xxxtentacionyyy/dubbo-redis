package com.yyy.qg.exception;

public enum GoodsException {
    GOODS_REPEAT_GET(1101,"您已经抢购过该商品"),
    GOODS_IS_CLEAR(1102,"商品已经被请购一空");
    private Integer code;
    private String message;
    GoodsException(Integer code,String message){
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
