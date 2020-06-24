package com.fwtai.controller.module;

import com.fwtai.controller.BaseController;
import com.fwtai.service.module.NewsService;
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
 * 控制层|路由层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@Controller
@RequestMapping("/bs_news")
public final class NewsController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private NewsService service;
	
	/**跳转到目标页面*/
	@RequestMapping({"/","/page"})
	public final String page(){
		return "module/news";
	}
	
	/**添加*/
	@RequestMapping("/add")
	@ResponseBody
	public final void add(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(service.add(request),response);
		} catch (Exception e){
			logger.error("控制层|路由层-添加",e);
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
			logger.error("控制层|路由层-编辑",e);
			ToolClient.responseException(response);
		}
	}
	
	/**行删除*/
	@RequestMapping("/delById")
	@ResponseBody
	public final void delById(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(service.delById(getPageFormData()),response);
		} catch (Exception e){
			logger.error("控制层|路由层-行删除",e);
			ToolClient.responseException(response);
		}
	}
	
	/**删除|批量删除*/
	@RequestMapping("/delIds")
	@ResponseBody
	public final void delIds(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(service.delIds(getPageFormData()),response);
		} catch (Exception e){
			logger.error("控制层|路由层-删除|批量删除",e);
			ToolClient.responseException(response);
		}
	}
	
	/**数据列表 */
	@RequestMapping("/listData")
	@ResponseBody
	public final void listData(final HttpServletRequest request,final HttpServletResponse response){
		ToolClient.responseJson(service.listData(getPageFormData()),response);
	}
	
	/**根据id获取全字段数据 */
	@RequestMapping("/queryById")
	@ResponseBody
	public final void queryById(final HttpServletResponse response){
		ToolClient.responseJson(service.queryById(getPageFormData()),response);
	}
}