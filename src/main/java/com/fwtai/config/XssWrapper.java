package com.fwtai.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 处理过滤实现
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017-12-21 20:34
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class XssWrapper extends HttpServletRequestWrapper {

    public XssWrapper(final HttpServletRequest request){super(request);}

    @Override
    public String getParameter(final String name) {
        return clearXss(super.getParameter(name));
    }

    @Override
    public String getHeader(final String name) {
        return clearXss(super.getHeader(name));
    }

    @Override
    public String[] getParameterValues(final String key) {
        final String[] values = super.getParameterValues(key);
        if (values == null) {
            return null;
        }
        final String[] newValues = new String[values.length];
        for (int i = 0; i < values.length; i++){
            String value = values[i];
            value = (value.length() == 1 && value.equals("_")) ? "" : value;
            newValues[i] = clearXss(value);
        }
        return newValues;
    }

    /**
     * 去除前后空格
     * @param value
     * @return
     */
    private String clearXss(final String value){
        if (value == null || "".equals(value)){
            return null;
        }
        return value.trim();
    }
}