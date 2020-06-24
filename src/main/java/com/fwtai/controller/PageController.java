package com.fwtai.controller;

import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 公共的页面跳转管理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2016年12月31日 17:44:52
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@Controller
public final class PageController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(getClass());

	/**跳转到首页页面*/
	@RequestMapping({"/",""})
	public final String index(){
		return "index";
	}

	/**跳转到登录页面*/
	@RequestMapping({"/login"})
	public final String login(){
		return "login";
	}
	
	/**登录成功后跳转页面,这里一般是后台操作中心*/
	@RequestMapping("/control")
	public final String control(){
		return "control";
	}
	
	/**系统账号页面*/
	@RequestMapping("/sys_user")
	public final String sys_user(){
		return "core/sys_user";
	}
	
	/**角色权限页面*/
	@RequestMapping("/sys_role")
	public final String sys_role(){
		return "core/sys_role";
	}
	
	/**系统日志页面*/
	@RequestMapping("/sys_log")
	public final String sys_log(){
		return "core/sys_log";
	}
	
	/**导航菜单list页面*/
	@RequestMapping("/sys_menu")
	public final String sys_menu(){
		return "core/sys_menu";
	}
	
	/**未登录或登录超时的返回页面提示*/
	@RequestMapping("/timeout")
	public final String timeout(){
		return ConfigFile.TIMEOUT;
	}
	
	/**按钮样式测试,用完了将其删除*/
	@RequestMapping("/button")
	public final String button(){
		return "module/button";
	}
	
	/**按钮样式测试,用完了将其删除,参数的接收*/
	@RequestMapping("/page")
	public final String page(final String page,@RequestParam("page") String pageName){
		return "module/"+page;
	}
	
	/**按钮样式测试,用完了将其删除,参数的接收*/
	@RequestMapping("/form")
	@ResponseBody
	public final void form(final HttpServletResponse response,final String name,final String pwd){
		String json;
		if (name.equalsIgnoreCase(pwd)){
			json = ToolClient.createJson(ConfigFile.code200,ConfigFile.msg200);
		}else {
			json = ToolClient.createJson(ConfigFile.code199,ConfigFile.msg199);
		}
		ToolClient.responseJson(json,response);
	}
}