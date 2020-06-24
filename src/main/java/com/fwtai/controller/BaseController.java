package com.fwtai.controller;

import com.fwtai.bean.PageFormData;
import com.fwtai.tool.ToolClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * 生成PageFormData对象,方便获取表单的数据及处理表单
 * @作者 田应平
 * @作用 生成PageFormData对象;生成HttpServletRequest对象;生成HttpServletResponse对象;
 * @版本 v1.0
 * @创建时间 2017年1月12日 上午12:01:15
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public class BaseController{

    private Logger logger = LoggerFactory.getLogger(getClass());

	/**获取PageFormData*/
	public final PageFormData getPageFormData(){
		return new PageFormData(getRequest());
	}

	/**获取request对象*/
	public final HttpServletRequest getRequest(){
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	/**获取rResponse对象*/
	public final HttpServletResponse getResponse(){
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}
	
	/**获取表单参数*/
	public final HashMap<String,String> getFormParams(){
		return ToolClient.getFormParams(getRequest());
	}
	
	/**获取session*/
	public final HttpSession getSession(){
		return getRequest().getSession(false);
	}
	
	/**获取访问者真实的IP地址*/
	public final String getIp(){
		return ToolClient.getIp(getRequest());
	}
}