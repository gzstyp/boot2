package com.fwtai.response;

/**
 * 请求响应返回客户端的状态status为 success 或 fail
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2019-03-25 0:24
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
 */
public final class ReturnType{

    /**表明请求对应的处理结果status为 success 或 fail*/
    private String status;

    /**
     * 若status=success,则data内返回前端所需的json数据,若status=fail,则data内使用通用的错误码格式
    */
    private Object data;

    public ReturnType(){
    }

    public static ReturnType create(final Object result){
        return ReturnType.create(result,"success");
    }

    public static ReturnType create(final Object result,final String status){
        final ReturnType type = new ReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public Object getData(){
        return data;
    }

    public void setData(Object data){
        this.data = data;
    }
}