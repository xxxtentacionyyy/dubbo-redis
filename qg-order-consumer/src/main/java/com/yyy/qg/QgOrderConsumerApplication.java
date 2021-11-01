package com.yyy.qg;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class QgOrderConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QgOrderConsumerApplication.class, args);
    }

}
