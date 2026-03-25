package com.tanish.frauddetectionassignment.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

@Bean
    public FilterRegistrationBean<MDCFilter> mdcfilter(){

    FilterRegistrationBean<MDCFilter> registrationBean=new FilterRegistrationBean<>();

    registrationBean.setFilter(new MDCFilter());

    registrationBean.addUrlPatterns("/*");

    registrationBean.setOrder(1);

    return registrationBean;

}
}
