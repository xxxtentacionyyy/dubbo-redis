package com.yyy.qg;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class QgGoodsConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QgGoodsConsumerApplication.class, args);
    }

}
