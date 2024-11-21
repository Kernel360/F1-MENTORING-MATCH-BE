package com.biengual.userapi.config;

import com.biengual.userapi.filter.ReadableRequestWrapperFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
