package com.fwtai.controller.core;

import com.fwtai.controller.BaseController;
import com.fwtai.service.core.MenuService;
import com.fwtai.tool.ToolClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**系统菜单模块*/
@Controller
@RequestMapping("/menu")
public final class MenuController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MenuService menu;
	
	/**菜单的添加*/
	@RequestMapping("/add")
	@ResponseBody
	public final void add(final HttpServletResponse response){
		try {
			ToolClient.responseJson(menu.add(getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**菜单的编辑*/
	@RequestMapping("/edit")
	@ResponseBody
	public final void edit(final HttpServletResponse response){
		try {
			ToolClient.responseJson(menu.edit(getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**删除菜单-菜单列表的行删除*/
	@RequestMapping("/delById")
	@ResponseBody
	public final void delById(final HttpServletResponse response){
		try {
			ToolClient.responseJson(menu.delById(getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**登录者所拥有的左侧的菜单列表*/
	@RequestMapping("/ownerMenus")
	@ResponseBody
	public final void ownerMenus(final HttpServletRequest request,final HttpServletResponse response){
		ToolClient.responseJson(menu.ownerMenus(request),response);
	}
	
	/**查询所有的菜单,用于添加或编辑菜单*/
	@RequestMapping("/queryAllMenu")
	@ResponseBody
	public final void queryAllMenu(final HttpServletResponse response){
		ToolClient.responseJson(menu.queryAllMenu(getPageFormData()),response);
	}
	
	/**菜单的easyui数据列表 */
	@RequestMapping("/listData")
	@ResponseBody
	public final void listData(final HttpServletRequest request,final HttpServletResponse response){
		ToolClient.responseJson(menu.listData(request,getPageFormData()),response);
	}
	
	/**用户拥有导航菜单下的按钮权限*/
	@RequestMapping("/authBtn")
	@ResponseBody
	public final void authBtn(final HttpServletRequest request,final HttpServletResponse response){
		ToolClient.responseJson(menu.authBtn(request,getPageFormData()),response);
	}
	
	/**根据id获取全字段数据 */
	@RequestMapping("/queryById")
	@ResponseBody
	public final void queryById(final HttpServletResponse response){
		ToolClient.responseJson(menu.queryById(getPageFormData()),response);
	}

    @GetMapping("/getList")
    @ResponseBody
	public final void getList(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(menu.getList(request),response);
    }
}