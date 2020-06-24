package com.fwtai.response;

import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * 业务异常处理,继承即可实现controller层的异常处理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2019-03-25 2:43
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public class ExceptionControlerHandler{

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void exceptionHandler(final Exception exception,final HttpServletResponse response){
        exception.printStackTrace();
        if(exception instanceof BusinessException){
            final BusinessException businessException = (BusinessException) exception;
            ToolClient.responseJson(ToolClient.createJson(ConfigFile.code204,businessException.getErrMsg()),response);
        }else{
            ToolClient.responseJson(ToolClient.createJson(ConfigFile.code204,"00"),response);
        }
    }
}