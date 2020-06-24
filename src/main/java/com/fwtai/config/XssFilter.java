package com.fwtai.config;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 过滤器
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017-12-21 20:31
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class XssFilter implements Filter {

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new XssWrapper((HttpServletRequest)request), response);
    }

    @Override
    public void destroy() {}
}