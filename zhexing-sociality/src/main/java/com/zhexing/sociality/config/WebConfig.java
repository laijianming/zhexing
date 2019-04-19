package com.zhexing.sociality.config;

import com.zhexing.sociality.inteceptor.ReturnInceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 实现WebMvcConfigurerAdapter 的父类 WebMvcConfigurer以达到自定义springmvc部分配置的效果
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ReturnInceptor()).addPathPatterns("/**");
    }
}
