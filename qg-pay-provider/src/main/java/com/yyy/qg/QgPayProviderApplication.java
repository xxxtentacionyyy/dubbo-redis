package com.yyy.qg;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
@MapperScan("com.yyy.qg.mapper")
public class QgPayProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(QgPayProviderApplication.class, args);
    }

}
