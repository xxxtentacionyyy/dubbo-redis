package com.yyy.qg.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafkaConfig")
public class KafkaConfig {

    private String moduleName;

    public static final class LogType{
        public static String INFO="info";
        public static String ERROR="error";
    }

    public static final String qgKey="qg";

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
