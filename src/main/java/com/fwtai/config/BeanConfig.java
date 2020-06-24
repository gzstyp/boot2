package com.fwtai.config;

import com.fwtai.controller.ImgCodeServlet;
import com.fwtai.tool.ServletImageCode;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpServlet;

/**
 * 配置,添加bean,要添加 @Bean 否则是被执行多次
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2018-01-10 9:20
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@SpringBootConfiguration
public class BeanConfig {

    /**添加配置Filter*/
    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new XssFilter());
        registration.addUrlPatterns("/*/*");
        return registration;
    }

    /**
     * 添加配置Servlet,验证码,url http://192.168.1.106/imgCode
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/7/13 14:05
    */
    @Bean
    public ServletRegistrationBean servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new ImgCodeServlet());
        registration.addUrlMappings("/imgCode");
        return registration;
    }

    /** http://127.0.0.1/arithmetic */
    @Bean
    public ServletRegistrationBean<HttpServlet> arithmetic() {
        return new ServletRegistrationBean(new ServletImageCode("arithmetic"),"/arithmetic");
    }

    /**添加配置listener*/
    @Bean
    public ServletListenerRegistrationBean<InitListener> listenerRegistration(){
        ServletListenerRegistrationBean<InitListener> registration = new ServletListenerRegistrationBean<InitListener>(new InitListener());
        return registration;
    }
}