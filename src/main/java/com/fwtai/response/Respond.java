package com.fwtai.response;

import java.io.Serializable;

/**
 * 通用的请求后响应数据模型
 * 用法：
 Respond respond = new Respond(StatusCode.Success);
 if(value == null){
    respond = new Respond(StatusCode.InvalidParams);
 }
 if(value != null && value.equals("198")){
    respond = new Respond("非法操作");
 }
 if(value != null && value.equals("201")){
    respond = new Respond(StatusCode.Fail);
 }
 if(value != null && value.equals("202")){
     respond = new Respond(202,"你输入的是"+value);
     final ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
     final HashMap<String,Object> map1 = new HashMap<String,Object>();
     final HashMap<String,Object> map2 = new HashMap<String,Object>();
     final HashMap<String,Object> map3 = new HashMap<String,Object>();
     map1.put("k1",1);
     map1.put("k2","typ");
     map2.put("t","typ");
     map3.put("v",1024);
     list.add(map1);
     list.add(map2);
     list.add(map3);
     respond.setData(list);
 }
 ToolClient.responseJson(respond,response);
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2019-03-24 22:51
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
 */
public final class Respond<T> implements Serializable{

    private Integer code;

    private String msg;

    /** code=200,则data内返回前端所需的json数据,若code=xxx,则msg内使用通用的错误提示信息格式*/
    private T data;

    public Respond(){
    }

    public Respond(final T data){
        this.code = 200;
        this.data = data;
    }

    public static Respond create(final Object data){
        return Respond.create(data,200);
    }

    public static Respond create(final Object data,final Integer code){
        final Respond respond = new Respond(data);
        respond.setCode(code);
        return respond;
    }

    /*code为198时就是自定义msg*/
    public Respond(final String msg){
        this.code = 198;
        this.msg = msg;
    }

    public Respond(final Integer code,final String msg){
        this.code = code;
        this.msg = msg;
    }

    public Respond(final StatusCode statusCode){
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    public Respond(final Integer code,final String msg,final T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode(){
        return code;
    }

    public void setCode(Integer code){
        this.code = code;
    }

    public String getMsg(){
        return msg;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }

    public T getData(){
        return data;
    }

    public void setData(T data){
        this.data = data;
    }
}