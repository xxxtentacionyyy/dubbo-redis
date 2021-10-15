package com.yyy.qg.common;

/***
 * 常量类
 */
public class Constants {
    //登录超时时间 30分钟
    public static final Long loginExpire=30L;

    public static final String tokenPrefix="token:";

    public static final String goodsPrefix = "goods:";

    public static final class StockStatus{
        public static final Integer lock = 0;

        public static final Integer paySuccess = 1;

        public static final Integer payOvertime= 2;
    }
}
