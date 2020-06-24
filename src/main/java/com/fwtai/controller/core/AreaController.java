package com.fwtai.controller.core;

import com.fwtai.controller.BaseController;
import com.fwtai.service.core.AreaService;
import com.fwtai.tool.ToolClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 区域地域
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2019/3/30 14:35
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Controller
@RequestMapping("/area")
public final class AreaController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AreaService service;
	
	/**跳转到目标页面*/
	@RequestMapping("/page")
	public final String page(){
		return "core/sys_area";
	}
	
	/**添加*/
	@RequestMapping("/add")
	@ResponseBody
	public final void add(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(service.add(request,getPageFormData()),response);
		} catch (Exception e){
			logger.error("区域地域-添加",e.getMessage());
			ToolClient.responseException(response);
		}
	}
	
	/**编辑*/
	@RequestMapping("/edit")
	@ResponseBody
	public final void edit(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(service.edit(request,getPageFormData()),response);
		} catch (Exception e){
			logger.error("区域地域-编辑",e.getMessage());
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
			logger.error("区域地域-行删除",e.getMessage());
			ToolClient.responseException(response);
		}
	}
	
	/**根据id获取全字段数据 */
	@RequestMapping("/queryById")
	@ResponseBody
	public final void queryById(final HttpServletResponse response){
		ToolClient.responseJson(service.queryById(getPageFormData()),response);
	}
	
	/**数据显示-根据父级id查询数据*/
	@RequestMapping("/getData")
	@ResponseBody
	public final void getData(final HttpServletResponse response){
		ToolClient.responseJson(service.getData(getPageFormData()),response);
	}
	
	/**省市县选择-根据父级id查询数据*/
	@RequestMapping("/queryArea")
	@ResponseBody
	public final void queryArea(final HttpServletResponse response){
		ToolClient.responseJson(service.queryArea(getPageFormData()),response);
	}
}