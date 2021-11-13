package com.yyy.qg.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "alipay-config")
public class AlipayConfig {

    /*// 商户appid
    public static String APPID = "2021000118618673";
    // 私钥 pkcs8格式的
    public static String PRIVATEKEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCevnszLeZhMRrtd3Eny9v1PM+3shqWUWormDDKD6Ilc7AtyWAfM1zLSSdeLEmIMhvUSOh3DgvdMfI5eE3c6nqHvBhyxI/9jsxhLaI5VUof9xcKuLz2jnjRfx/tT9t2sCWNnI6rnM8czbzEyQvNFTqhEooD7aJ03ZwHYZb12pVPuoGws3F3vFefUEjCmV/Nvr4iQeD9bi8pGh1G+15IGE49LUIvAc+UapjDEdelL6cCyT0e1yEoeXMZi6vIgbuyrv+ZJIadTsaUUKp9J6AfOZXDy0tCUKYjavUSYVIidr6e407kOT66plu0Twma/HJrQ+JJmYixKJidhA75kNoPUOuXAgMBAAECggEAZevq8htAVZrPrTQJJQIQwc4/l7P0uhqfzkmy2alb9CvpLBylaKLzq1nWuvI4bAewMoZ1KyuLloOhACEr7PFnpoqwL8/gm23571L7W1BVfVyKIMooGS20iN3UQ+wH/pZuJhQkzi6qE9ZMoxRqTUyaDhz3HDqRGfPHYD3aTRQA1eTn0RhpRZ4FxB/oKYnzrFd0qbDJY3C++lj3ashgg6JIPvA+kQ+S1muQCJwIOIkYV9kXVMKJolyYmwa8J60PMElQJLTITA5AMubfv+LeuwS1iSK60MHSdpDSminVqdCXOXp9t3P/MJCxNrYbWerTxpliJiLSBq0DYBfNVllxZU5TUQKBgQDcAK3wEoYhkHcLTUdPZliSHKq5nz3DqU0QJZBmX6JKR6iylqZppsWOFgftW1Vt+5ouw5uzrSHP0xDEKHZrtBI9JFoc6Lhp+dFro/V/vsSMb7yE2OaBFSgbq0bERchNfufGrKmBl5Go5085LYtymaYbq6AkaE06rEUQapGF5+aHzQKBgQC4t9gUnUYCQimbbyaX0YU+G8adNmMe9OSPoe+s8RItwq1sdWK8aukbJnMNgtOcOh87DMZCwUHvw9AxxhLW0ZNy1Fn//FzB5//6SjtMG9urS8HyhgFY+UOC6E/QYs5v5UMYpmy9dk9TGqJs4ccEEaL6qXqmbqf7UKKUatFJemoU8wKBgFgEvZJLFvbewYH4DQurOsxaQ8O3/hIF7hsDrF4I7Pki6k9pDnM+kdNi5REFdYxnGSyUffrIzbpEC3UvaxF0Hcfwf00Jnp5noG2cT2AohqJZIedUkAQc3ssTH2DvOlAm8bv+4GJErbYRtRR/MEdNSbPQWmW2hQ9wVMFYoZgdS5IRAoGAE9HmJZAaivN+c2chxPh+S4O/c+76A8BTJfZCOVRElxRkd1bBGWZ/TKJ7hd+bxQ+XekaMn20NHiTsmeqe6t3h7FhMr+LsMQIrvTdp1WjH0UF3tgYa7ukQDWPP7JQtgmQfJu30jP+80zXwZ65yJEWgLzUwK9uDuruUNkif3Dp+V3ECgYEApksP+yl0vdmjH9/Qr50zFN/Ps/XctMG9Vc2rXeMckWacrSm1LtgdD2gOKs2iZi1CFMe9LzdOvzauTzMPi6c6pU5Aq82ihaYPwB0fnCfOxjY4Vo08Naga+MLUBUv4DvUUDjmuDr7LbogD+6F73BsHo/w8RojEfGE+XGCjvh2GjIM=";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://wyo9w7q4h33t.ngrok2.xiaomiqiu.cn/notify_url";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://wyo9w7q4h33t.ngrok2.xiaomiqiu.cn/return_url";
    // 请求网关地址
    public static String URL = "https://openapi.alipaydev.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAYPUBLICKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjrL4Oq+yCDnq0Tr7G8F9hjOcfUoAnj40suKO90XnqKOwO2OpCDH5VDgdFau26YlVheTHMC1BzJekorxJ6WpAHHgWPGo82ovUtKL8zWi+h60GST8Ow8r3Vjn9Q1lU1Mev9Qlto1F4OQIoy3NVN9Lk4s/4caFNpw1I35qkUX3Se1NndfWS8lgn70Td41bkrr0oLlyWs/d++6YqAMpGExdHJ6BEz8vTgI22+6so5PruYJTeCvth69tUvB3rRX9i4LeYfYCPdxb9ktZzP6q9zXEVsclTRnWBBLEbcqt/hH1+oDTnu185hk/wzvCjWJaaWYy/UPwgWy7768tktg6pJeOi1wIDAQAB";
    // 日志记录目录
//public static String log_path = "/log";
// RSA2
    public static String SIGNTYPE = "RSA2";*/

    // 商户appid
    public static String APPID;
    // 私钥 pkcs8格式的
    public static String PRIVATEKEY;
// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url;
// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url;
    // 请求网关地址
    public static String URL;
    // 编码
    public static String CHARSET;
    // 返回格式
    public static String FORMAT;
    // 支付宝公钥
    public static String ALIPAYPUBLICKEY;
    // 日志记录目录
//public static String log_path = "/log";
// RSA2
    public static String SIGNTYPE;

    public String getPaySuccessUrl() {
        return paySuccessUrl;
    }

    public void setPaySuccessUrl(String paySuccessUrl) {
        AlipayConfig.paySuccessUrl = paySuccessUrl;
    }

    public String getPayFailUrl() {
        return payFailUrl;
    }

    public void setPayFailUrl(String payFailUrl) {
        AlipayConfig.payFailUrl = payFailUrl;
    }

    public static String paySuccessUrl;

    public static String payFailUrl;


    public String getAPPID() {
        return APPID;
    }

    public void setAPPID(String APPID) {
        AlipayConfig.APPID = APPID;
    }

    public String getPRIVATEKEY() {
        return PRIVATEKEY;
    }

    public void setPRIVATEKEY(String PRIVATEKEY) {
        AlipayConfig.PRIVATEKEY = PRIVATEKEY;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        AlipayConfig.notify_url = notify_url;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        AlipayConfig.return_url = return_url;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        AlipayConfig.URL = URL;
    }

    public String getCHARSET() {
        return CHARSET;
    }

    public void setCHARSET(String CHARSET) {
        AlipayConfig.CHARSET = CHARSET;
    }

    public String getFORMAT() {
        return FORMAT;
    }

    public void setFORMAT(String FORMAT) {
        AlipayConfig.FORMAT = FORMAT;
    }

    public String getALIPAYPUBLICKEY() {
        return ALIPAYPUBLICKEY;
    }

    public void setALIPAYPUBLICKEY(String ALIPAYPUBLICKEY) {
        AlipayConfig.ALIPAYPUBLICKEY = ALIPAYPUBLICKEY;
    }

    public String getSIGNTYPE() {
        return SIGNTYPE;
    }

    public void setSIGNTYPE(String SIGNTYPE) {
        AlipayConfig.SIGNTYPE = SIGNTYPE;
    }
}