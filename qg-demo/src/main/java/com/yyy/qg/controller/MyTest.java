package com.yyy.qg.controller;

import com.yyy.qg.utils.UrlUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyTest {
    static ExecutorService executorService = Executors.newFixedThreadPool(50);
    public static void main(String[] args) {
        for (int i=1;i<=100;i++){
            final int fi=i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    int mod=(int)(1+Math.random()*(2));
                    String dev1="http://localhost:8881";
                    String dev2="http://localhost:8882";
                    String url=(mod==1?dev1:dev2)+"/qg?goodsId=1"+"&userId="+fi;
                    String result= UrlUtils.loadURL(url);
                    System.out.print(result);
                }
            });
        }
    }
}
