package com.yyy.qg;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
@MapperScan("com.yyy.qg.dao")
public class QgGoodsProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(QgGoodsProviderApplication.class, args);
    }

}
