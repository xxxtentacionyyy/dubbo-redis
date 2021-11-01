package com.yyy.qg;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.yyy.qg.dao")
@EnableDubboConfiguration
@SpringBootApplication
public class QgOrderProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(QgOrderProviderApplication.class, args);
    }

}
