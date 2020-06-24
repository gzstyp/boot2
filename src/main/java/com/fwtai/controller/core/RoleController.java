package com.fwtai.controller.core;

import com.fwtai.controller.BaseController;
import com.fwtai.service.core.RoleService;
import com.fwtai.tool.ToolClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**系统角色*/
@Controller
@RequestMapping("/role")
public final class RoleController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RoleService role;
	
	/**角色添加*/
	@RequestMapping("/add")
	@ResponseBody
	public final void add(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(role.add(request),response);
		} catch (Exception e){
			ToolClient.responseException(response);
		}
	}
	
	/**角色编辑*/
	@RequestMapping("/edit")
	@ResponseBody
	public final void edit(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(role.edit(request,getPageFormData()),response);
		} catch (Exception e){
			logger.error("类RoleController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**删除角色权限-行删除*/
	@RequestMapping("/delById")
	@ResponseBody
	public final void delById(final HttpServletResponse response){
		try {
			ToolClient.responseJson(role.delById(getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**删除[含批量删除]角色*/
	@RequestMapping("/delIds")
	@ResponseBody
	public final void delIds(final HttpServletResponse response){
		try {
			ToolClient.responseJson(role.delIds(getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**清空去除角色菜单*/
	@RequestMapping("/removeRoleMenu")
	@ResponseBody
	public final void removeRoleMenu(final HttpServletResponse response){
		try {
			ToolClient.responseJson(role.removeRoleMenu(getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**加载角色菜单[分配]*/
	@RequestMapping("/roleMenu")
	@ResponseBody
	public final void roleMenu(final HttpServletRequest request,final HttpServletResponse response){
		final String json = role.roleMenu(request,getPageFormData()).replaceAll("\"false\"", "false").replaceAll("\"true\"", "true");
		ToolClient.responseJson(json,response);
	}
	
	/**角色菜单的保存*/
	@RequestMapping("/saveIds")
	@ResponseBody
	public final void saveIds(final HttpServletResponse response){
		try {
			ToolClient.responseJson(role.saveIds(getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**角色权限的数据列表 */
	@RequestMapping("/listData")
	@ResponseBody
	public final void listData(final HttpServletResponse response){
		ToolClient.responseJson(role.listData(getPageFormData()),response);
	}
}