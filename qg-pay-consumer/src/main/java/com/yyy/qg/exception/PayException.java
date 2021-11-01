package com.yyy.qg.exception;

public enum PayException {

    WX_PAY_BUZY(1401,"微信支付繁忙,请使用其它的支付方式");
    PayException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    private Integer code;
    private String message;
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
