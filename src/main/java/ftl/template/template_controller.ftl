<#ftl encoding="utf-8"/>
package com.fwtai.controller.module;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fwtai.controller.BaseController;
import com.fwtai.service.module.${className}Service;
import com.fwtai.tool.ToolClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ${tableComment}控制层|路由层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 ${createDate}
 * @官网 <url>http://www.yinlz.com</url>
*/
@Controller
@RequestMapping("/${nameSpace}")
public final class ${className}Controller extends BaseController{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ${className}Service service;
	
	/**跳转到目标页面*/
	@RequestMapping("/page")
	public final String page(){
		return "module/${nameSpace}";
	}
	
	/**添加*/
	@RequestMapping("/add")
	@ResponseBody
	public final void add(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(service.add(request),response);
		} catch (Exception e){
			logger.error(e.getMessage());
            if(e instanceof DuplicateKeyException){
                ToolClient.responseException(response,"所添加的数据已存在");
                return;
            }
			ToolClient.responseException(response);
		}
	}
	
	/**编辑*/
	@RequestMapping("/edit")
	@ResponseBody
	public final void edit(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(service.edit(request),response);
		} catch (Exception e){
			logger.error(e.getMessage());
            if(e instanceof DuplicateKeyException){
                ToolClient.responseException(response,"所编辑的数据已存在");
                return;
            }
			ToolClient.responseException(response);
		}
	}
	
	/**行删除*/
	@RequestMapping("/delById")
	@ResponseBody
	public final void delById(final HttpServletResponse response){
		try {
			ToolClient.responseJson(service.delById(getPageFormData()),response);
		} catch (Exception e){
			logger.error(e.getMessage());
			ToolClient.responseException(response);
		}
	}
	
	/**删除|批量删除*/
	@RequestMapping("/delIds")
	@ResponseBody
	public final void delIds(final HttpServletResponse response){
		try {
			ToolClient.responseJson(service.delIds(getPageFormData()),response);
		} catch (Exception e){
			logger.error(e.getMessage());
			ToolClient.responseException(response);
		}
	}
	
	/**数据列表 */
	@RequestMapping("/listData")
	@ResponseBody
	public final void listData(final HttpServletResponse response){
		ToolClient.responseJson(service.listData(getPageFormData()),response);
	}
	
	/**根据id获取全字段数据 */
	@RequestMapping("/queryById")
	@ResponseBody
	public final void queryById(final HttpServletResponse response){
		ToolClient.responseJson(service.queryById(getPageFormData()),response);
	}
}