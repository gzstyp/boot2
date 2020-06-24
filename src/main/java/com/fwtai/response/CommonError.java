package com.fwtai.response;

/**
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2019-03-25 1:21
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
 */
public interface CommonError{

    int getErrCode();

    String getErrMsg();

    CommonError setErrMsg(String errMsg);
}