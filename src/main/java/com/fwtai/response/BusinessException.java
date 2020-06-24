package com.fwtai.response;

/**
 * 返回业务异常处理,采用包装器业务异常实现
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2019-03-25 1:47
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public class BusinessException extends RuntimeException implements CommonError{

    private CommonError commonError;

    /**直接接收EmBusinessError的传参,用于构造业务异常*/
    public BusinessException(final CommonError commonError){
        super();//这个必须要有!
        this.commonError = commonError;
    }

    /**接收自定义的errMsg的方法构造业务异常*/
    public BusinessException(final CommonError commonError,final String errMsg){
        super();//这个必须要有!
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode(){
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg(){
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg){
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}