package com.fwtai.controller.core;

import com.fwtai.config.ConfigFile;
import com.fwtai.controller.BaseController;
import com.fwtai.service.core.UserService;
import com.fwtai.tool.ToolClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**系统账号(用户)*/
@Controller
@RequestMapping("/user")
public final class UserController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService user;
	
	/**验证账号和密码登录*/
	@RequestMapping("/userLogin")
	@ResponseBody
	public final void userLogin(final HttpServletRequest request,final HttpServletResponse response){
		ToolClient.responseJson(user.userLogin(request,getPageFormData()),response);
	}
	
	/**
	 * @功能:退出登陆并清除session
	 * @日期 2016年12月29日 03:03:54
	*/
	@RequestMapping("/logout")
	public final String logout(final HttpServletRequest request){
		final String redirect_url = "redirect:/login";//重定向,否则会提示找不到页面,重定向会再次请求服务器一次
		final HttpSession session = request.getSession(false);
		if(session == null)
			return redirect_url;
		final Object login_key = session.getAttribute(ConfigFile.LOGIN_KEY);
		if(login_key == null)
			return redirect_url;
		final Enumeration<String> e = session.getAttributeNames();
		while(e.hasMoreElements()){
			final String key = e.nextElement();
			session.removeAttribute(key);
		}
		return redirect_url;
	}
	
	/**账号的数据列表 */
	@RequestMapping("/listData")
	@ResponseBody
	public final void listData(final HttpServletResponse response){
		ToolClient.responseJson(user.listData(getPageFormData()),response);
	}
	
	/**系统账号的添加;默认为系统账号,即type=1*/
	@RequestMapping("/add")
	@ResponseBody
	public final void add(final HttpServletResponse response){
		try {
			ToolClient.responseJson(user.add(getPageFormData()),response);
		} catch (Exception e){
			ToolClient.responseException(response);
		}
	}
	
	/**账号列表删除账号*/
	@RequestMapping("/delById")
	@ResponseBody
	public final void delById(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(user.delById(request,getPageFormData()),response);
		} catch (Exception e){
			ToolClient.responseException(response);
		}
	}
	
	/**批量删除账号*/
	@RequestMapping("/delIds")
	@ResponseBody
	public final void delIds(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(user.delIds(request,getPageFormData()),response);
		} catch (Exception e){
			ToolClient.responseException(response);
		}
	}
	
	/**修改登录者的密码*/
	@RequestMapping("/alterPwd")
	@ResponseBody
	public final void alterPwd(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(user.alterPwd(request,getPageFormData()),response);
		} catch (Exception e){
			logger.error("类UsersController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**账号列表的重置密码*/
	@RequestMapping("/editPwd")
	@ResponseBody
	public final void editPwd(final HttpServletResponse response){
		try {
			ToolClient.responseJson(user.editPwd(getPageFormData()),response);
		} catch (Exception e){
			logger.error("类UsersController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**加载读取账号|用户私有菜单[分配]*/
	@RequestMapping("/userMenu")
	@ResponseBody
	public final void userMenu(final HttpServletRequest request,final HttpServletResponse response){
		final String json = user.userMenu(request,getPageFormData()).replaceAll("\"false\"", "false").replaceAll("\"true\"", "true");
		ToolClient.responseJson(json,response);
	}
	
	
	/**查看账号的菜单数据*/
	@RequestMapping("/viewMenuUid")
	@ResponseBody
	public final void viewMenuUid(final HttpServletRequest request,final HttpServletResponse response){
		final String json = user.viewMenuUid(request,getPageFormData()).replaceAll("\"false\"", "false").replaceAll("\"true\"", "true");
		ToolClient.responseJson(json,response);
	}
	
	/**加载读取账号|用户角色*/
	@RequestMapping("/userRole")
	@ResponseBody
	public final void userRole(final HttpServletRequest request,final HttpServletResponse response){
		final String json = user.userRole(request,getPageFormData()).replaceAll("\"false\"", "false").replaceAll("\"true\"", "true");
		ToolClient.responseJson(json,response);
	}
	
	/**切换此账号登录*/
	@RequestMapping("/switcherUser")
	@ResponseBody
	public final void switcherUser(final HttpServletRequest request,final HttpServletResponse response){
		ToolClient.responseJson(user.switcherUser(request,getPageFormData()),response);
	}
	
	/**切换原帐号登录*/
	@RequestMapping("/switcher")
	@ResponseBody
	public final void switcher(final HttpServletRequest request,final HttpServletResponse response){
		ToolClient.responseJson(user.switcher(request,getPageFormData()),response);
	}
	
	/**登录者为admin时账号批量分配角色的角色读取*/
	@RequestMapping("/queryAllotRole")
	@ResponseBody
	public final void queryAllotRole(final HttpServletRequest request,final HttpServletResponse response){
		final String json = user.queryAllotRole(request,getPageFormData()).replaceAll("\"false\"", "false").replaceAll("\"true\"", "true");
		ToolClient.responseJson(json,response);
	}
	
	/**账号|用户菜单菜单的保存*/
	@RequestMapping("/saveIds")
	@ResponseBody
	public final void saveIds(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(user.saveIds(request,getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**账号|用户角色的保存*/
	@RequestMapping("/saveRoleIds")
	@ResponseBody
	public final void saveRoleIds(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(user.saveRoleIds(request,getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**批量账号角色分配的保存*/
	@RequestMapping("/batchAllotRole")
	@ResponseBody
	public final void batchAllotRole(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(user.batchAllotRole(request,getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**去除删除账号的私有菜单*/
	@RequestMapping("/removeUserMenu")
	@ResponseBody
	public final void removeUserMenu(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(user.removeUserMenu(request,getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**去除删除账号的角色*/
	@RequestMapping("/removeUserRole")
	@ResponseBody
	public final void removeUserRole(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(user.removeUserRole(request,getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**批量去除账号角色*/
	@RequestMapping("/batchRemoveRole")
	@ResponseBody
	public final void batchRemoveRole(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(user.batchRemoveRole(request,getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
	
	/**操作:(禁用|启用)*/
	@RequestMapping("/enabled")
	@ResponseBody
	public final void enabled(final HttpServletRequest request,final HttpServletResponse response){
		try {
			ToolClient.responseJson(user.enabled(request,getPageFormData()),response);
		} catch (Exception e){
			logger.error("类MenuController的方法"+getClass().getMethods()[0].toGenericString(),e);
			ToolClient.responseException(response);
		}
	}
}