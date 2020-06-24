package com.fwtai.controller.module;

import com.fwtai.controller.BaseController;
import com.fwtai.service.module.LogService;
import com.fwtai.tool.ToolClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**系统日志*/
@Controller
@RequestMapping("/log")
public final class LogController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LogService logService;
	
	/**角色权限的数据列表 */
	@RequestMapping("/listData")
	@ResponseBody
	public final void listData(final HttpServletResponse response){
		ToolClient.responseJson(logService.listData(getPageFormData()),response);
	}
}