package com.fwtai.bean;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 客户端统一响应json数据,必须调用build()生成json字符串
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-02-11 2:33
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class ClientResult implements Serializable{

    /**请求状态码*/
    private int code;

    /**请求提示信息*/
    private String msg;

    /**请求响应code为200时才有数据*/
    private Object data;

    public ClientResult(final int code,final String msg){
        this.code = code;
        this.msg = msg;
    }

    public ClientResult(final int code,final String msg,final Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String build(){
        final JSONObject json = new JSONObject();
        json.put("code",this.code);
        json.put("msg",this.msg);
        json.put("data",this.data);
        return json.toJSONString();
    }

    public String success(){
        final JSONObject json = new JSONObject();
        json.put("code",200);
        json.put("msg","操作成功");
        return json.toJSONString();
    }

    public String success(final String msg){
        final JSONObject json = new JSONObject();
        json.put("code",200);
        json.put("msg",msg);
        return json.toJSONString();
    }

    public String success(final Object data){
        final JSONObject json = new JSONObject();
        json.put("code",200);
        json.put("msg","操作成功");
        json.put("data",this.data);
        return json.toJSONString();
    }

    public String failure(){
        final JSONObject json = new JSONObject();
        json.put("code",199);
        json.put("msg","操作失败");
        return json.toJSONString();
    }

    public String failure(final String msg){
        final JSONObject json = new JSONObject();
        json.put("code",199);
        json.put("msg",msg);
        return json.toJSONString();
    }

    public String failure(final int code,final String msg){
        final JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        return json.toJSONString();
    }

    public void setCode(final int code){
        this.code = code;
    }

    public void setMsg(final String msg){
        this.msg = msg;
    }

    public void setData(final Object data){
        this.data = data;
    }
}