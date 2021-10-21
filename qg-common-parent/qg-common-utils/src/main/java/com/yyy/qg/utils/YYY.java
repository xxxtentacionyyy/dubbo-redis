package com.yyy.qg.utils;

public class YYY {
    private static final boolean flag = true;

    public static void print(Object message){
        if (flag){
            System.err.println(message);
        }
    }

}
