package com.fwtai.bean;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能强大的接收请求的参数
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-05-14 15:03
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class FormData extends JSONObject{

    /**获取表单的数据,用法:FormData.getFormData(request);*/
    public final static FormData getFormData(final HttpServletRequest request){
        final FormData formData = new FormData();
        final Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()){
            final String key = paramNames.nextElement();
            if(key.equals("_"))continue;
            final String value = request.getParameter(key);
            if(value != null && value.length() > 0){
                if(value.length() == 1 && value.equals("_"))
                    continue;
                formData.put(key,value.trim());
            }
        }
        return formData;
    }

    /**获取的json数据格式,用法:FormData.buildInputStream(request);*/
    public final static FormData buildInputStream(final HttpServletRequest request){
        final FormData formData = new FormData();
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
            final StringBuilder sb = new StringBuilder();
            String s = "";
            while((s = in.readLine()) != null){
                sb.append(s);
            }
            in.close();
            if(sb.length() > 0){
                final String str = sb.toString().trim();
                final JSONObject json = JSONObject.parseObject(str);
                if(!json.isEmpty()){
                    for(final String key : json.keySet()){
                        if(key.equals("_"))continue;
                        final Object obj = json.get(key);
                        if(obj != null){
                            final String value = String.valueOf(obj).trim();
                            if(value.length() <= 0)
                                continue;
                            if(value.length() == 1 && value.equals("_"))
                                continue;
                            formData.put(key,obj);
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return formData;
    }

    /**获取的json数据格式,用法:FormData.getHashMap(request);*/
    public final static HashMap<String,String> getHashMap(final HttpServletRequest request){
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
            final StringBuilder sb = new StringBuilder();
            String s = "";
            while((s = in.readLine()) != null){
                sb.append(s);
            }
            in.close();
            return JSONObject.parseObject(String.valueOf(sb),HashMap.class);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**获取的json数据格式,用法:FormData.getBody(request);*/
    public final static Map<String,String> getBody(final ServletRequest request){
        final Map<String ,String > dataMap = new HashMap<>();
        if (request.getAttribute("body") != null) {
            return (Map<String,String>)request.getAttribute("body");
        } else {
            try {
                return JSONObject.parseObject(request.getInputStream(),Map.class);
            }catch (IOException e){
                e.printStackTrace();
            }
            return dataMap;
        }
    }
}