package com.yyy.qg.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;
import java.util.List;

/***
 * 配置拦截器
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    private static final String[] EXCLUDE_PATH= {"/","/css/**","/js/**","/img/**","/media/**","/api/doLogin"};

    @Bean
    public ExceptionInterceptor exceptionInterceptor(){
        return new ExceptionInterceptor();
    }

    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(exceptionInterceptor()).addPathPatterns("/api/**").excludePathPatterns(EXCLUDE_PATH);
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/api/v/**");
        super.addInterceptors(registry);
    }
}
