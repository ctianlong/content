package com.netease.homework.content.config;

import com.netease.snailreader.common.component.intercept.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/2/8
 */
@Configuration
public class AopConfig {

    @Bean
    Interceptor interceptor() {
        return new Interceptor();
    }

}
