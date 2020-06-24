package com.fwtai.controller;

import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 文件上传处理
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@Controller
@RequestMapping("/file")
public final class FileUploadController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(getClass());

	/**单文件上传,多文件上传时是用分号,隔开的*/
	@RequestMapping("/uploadSingle")
	@ResponseBody
	public final void upload(final HttpServletRequest request,final HttpServletResponse response){
		try {
			final String path = ToolClient.uploadFile(request,1);
			final HashMap<String,String> map = new HashMap<String,String>();
			if (ToolString.isBlank(path)){
				ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,ConfigFile.msg199),response);
				return;
			}else {
				map.put("path",path);
				final String json = ToolClient.queryJson(map);
				ToolClient.responseJson(json,response);
				return;
			}
		} catch (Exception e){
			logger.error("类FileUploadController的方法upload()出异常:"+e);
			ToolClient.responseException(response);
		}
	}
}