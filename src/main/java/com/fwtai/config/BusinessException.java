package com.fwtai.config;

/**
 * 仅在service层调用,业务处理异常,基于运行时的异常,事务可以回滚
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2019-05-05 13:41
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class BusinessException extends RuntimeException{

    public BusinessException(final String message){
        super(message);//这个必须要有!
    }

    public BusinessException(final String message,final Throwable cause){
        super(message,cause);//这个必须要有!
    }
}