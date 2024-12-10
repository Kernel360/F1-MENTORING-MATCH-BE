package com.biengual.userapi.config;

import com.biengual.userapi.filter.ReadableRequestWrapperFilter;
import com.biengual.userapi.filter.ServerTimingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class HttpRequestFilterConfig {

    @Bean
    public FilterRegistrationBean<ReadableRequestWrapperFilter> readableRequestWrapperFilter() {
        FilterRegistrationBean<ReadableRequestWrapperFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ReadableRequestWrapperFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("readableRequestWrapperFilter");
        return registrationBean;
    }

    @Bean
    @Profile({"local", "dev"})
    public FilterRegistrationBean<ServerTimingFilter> serverTimingFilter() {
        FilterRegistrationBean<ServerTimingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ServerTimingFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("serverTimingFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
