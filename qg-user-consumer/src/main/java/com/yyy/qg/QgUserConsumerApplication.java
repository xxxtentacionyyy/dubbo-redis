package com.yyy.qg;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class QgUserConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QgUserConsumerApplication.class, args);
    }

}
