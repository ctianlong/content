package com.netease.homework.content.config;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.netease.homework.content.config.constant.Role;
import com.netease.homework.content.security.SecurityMetadataSource;
import com.netease.homework.content.security.csrf.CookieCsrfTokenManager;
import com.netease.homework.content.security.csrf.CsrfFilter;
import com.netease.homework.content.security.csrf.CsrfRequestDataValueProcessor;
import com.netease.homework.content.security.csrf.CsrfTokenManager;
import com.netease.homework.content.security.interceptor.SecurityInterceptor;
import com.netease.homework.content.security.thymeleaf.dialect.SecurityDialect;
import com.netease.snailreader.common.component.enumeration.IntEnum;
import com.netease.snailreader.common.component.enumeration.StringEnum;
import com.netease.snailreader.common.ext.jackson.IntEnumSerializer;
import com.netease.snailreader.common.ext.jackson.ObjectMapperFactory;
import com.netease.snailreader.common.ext.jackson.StringEnumSerializer;
import com.netease.snailreader.common.ext.spring.convert.CustomStringToEnumConverterFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.DispatcherType;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/16
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new CustomStringToEnumConverterFactory());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/show").setViewName("show");
        registry.addViewController("/detail").setViewName("detail");
        registry.addViewController("/account").setViewName("account");
    }

    /**
     * 存储访问控制元数据
     */
    @Bean
    public SecurityMetadataSource securityMetadataSource() {
        SecurityMetadataSource source = new SecurityMetadataSource();
        // 定义访问规则
        source.antMatchers("/", "/login", "/error/**", "/show").anonymous()
                .antMatchers("/api/user/common/**", "/api/content/common/**").anonymous()
                .antMatchers("/api/order/**", "/api/shopcart/**", "/account").hasRole(Role.BUYER)
                .antMatchers("/api/content/seller/**", "/detail").hasRole(Role.SELLER);
        return source;
    }

    /**
     * 访问控制拦截器
     */
    @Bean
    public SecurityInterceptor securityInterceptor() {
        return new SecurityInterceptor(securityMetadataSource());
    }

    /**
     * 自定义 jsckson2 json序列化，用于处理 IntEnum StringEnum
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() throws Exception {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapperFactory factory = new ObjectMapperFactory();
        Map<Class<?>, JsonSerializer<?>> serializers = new HashMap<>();
        serializers.put(IntEnum.class, IntEnumSerializer.INSTANCE);
        serializers.put(StringEnum.class, StringEnumSerializer.INSTANCE);
        factory.setSerializers(serializers);
        converter.setObjectMapper(factory.getObject());
        return converter;
    }

    /**
     * 基于cookie的csrfTokenManager
     * @return
     */
    @Bean
    public CsrfTokenManager csrfTokenManager() {
        return new CookieCsrfTokenManager();
    }

    /**
     * csrf 过滤器注册
     * @return
     */
    @Bean
    public FilterRegistrationBean csrfFilter(CsrfTokenManager csrfTokenManager) {
        CsrfFilter csrfFilter = new CsrfFilter(csrfTokenManager);
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(csrfFilter);
        // UrlPatterns不设置默认也会拦截 /*，也可以设置servlet相关，则会拦截相应servlet的请求
        registrationBean.addUrlPatterns("/*");
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return registrationBean;
    }

    /**
     * 用于在前端页面 from 中插入_csrf隐藏域
     * 注意此处 beanName 必须为 requestDataValueProcessor
     * @return
     */
    @Bean("requestDataValueProcessor")
    public CsrfRequestDataValueProcessor csrfRequestDataValueProcessor() {
        return new CsrfRequestDataValueProcessor();
    }

    /**
     * 自定义thymeleaf标签扩展，主要用于用于登录、权限等功能
     * @return
     */
    @Bean
    public SecurityDialect securityDialect() {
        return new SecurityDialect();
    }

}
