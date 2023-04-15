package com.my.mall.config;

import com.my.mall.common.LimitInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @description: 加载限流配置
 * @author: wzh
 * @date: 2023/3/2 17:18
 */
@Configuration
public class LimitConfig implements WebMvcConfigurer {

    @Resource
    private LimitInterceptor limitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(limitInterceptor);
    }
}
