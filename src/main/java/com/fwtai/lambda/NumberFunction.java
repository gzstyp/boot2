package com.fwtai.lambda;

/**
 * 自定义函数式接口
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2019-06-12 16:34
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@FunctionalInterface
public interface NumberFunction{

    Integer getValue(final Integer number);

}