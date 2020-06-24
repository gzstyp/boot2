<#ftl encoding="utf-8"/>
package com.fwtai.service.module;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fwtai.config.ConfigFile;
import com.fwtai.dao.BaseService;
import com.fwtai.dao.DaoBase;
import com.fwtai.service.core.LogService;
import com.fwtai.bean.PageFormData;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ${tableComment}业务层,带操作日志的
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 ${createDate}
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class ${className}Service{

	private Logger logger = LoggerFactory.getLogger(getClass());

	protected final String flagName = "${className}";
	
	@Autowired
	private DaoBase dao;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private BaseService baseService;
	
	/**添加*/
	public String add(final HttpServletRequest request)throws Exception{
		final String login_key = baseService.getLoginKey(request);
		if(ToolString.isBlank(login_key)){
			return ToolClient.createJson(ConfigFile.code205,ConfigFile.msg205);
		}
		final PageFormData pageFormData = ToolClient.getFormData(request);
		pageFormData.put("${keyId}",ToolString.getIdsChar32());
		final int rows = dao.execute("${nameSpace}.add",pageFormData);
		if (rows > 0){
			logService.add(login_key,flagName,1,baseService.getIp(request),baseService.getLoginUser(request)+"添加");
		}
		return ToolClient.executeRows(rows);
	}
	
	/**编辑*/
	public String edit(final HttpServletRequest request)throws Exception{
		final PageFormData pageFormData = ToolClient.getFormData(request);
		final int rows = dao.execute("${nameSpace}.edit",pageFormData);
		if (rows > 0){
			logService.add(baseService.getLoginKey(request),flagName,3,ToolClient.getIp(request),baseService.getLoginUser(request)+"编辑成功");
		}
		return ToolClient.executeRows(rows);
	}
	
	/**行删除*/
	public String delById(final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"${keyId}"}))return ToolClient.jsonValidateField();
		final String id = pageFormData.getString("${keyId}");
		final int rows = dao.execute("${nameSpace}.delById",id);//行删除
		return ToolClient.executeRows(rows);
	}
	
	/**删除[含批量删除]*/
	public String delIds(final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"ids"}))return ToolClient.jsonValidateField();
		final String ids = pageFormData.getString("ids");
		final ArrayList<String> listIds = ToolString.keysToList(ids,",");
		int rows = 0 ;
		if(listIds != null && listIds.size() > 0){
			rows = dao.executeBatch("${nameSpace}.delIds",listIds);//批量删除
		}
		return ToolClient.executeRows(rows);
	}
	
	/**数据列表*/
	public String listData(PageFormData pageFormData){
		try{
			pageFormData = ToolClient.datagridPagingMysql(pageFormData);
			final HashMap<String, Object> map = dao.queryForPage(pageFormData,"${nameSpace}.listData","${nameSpace}.listTotal");
			return ToolClient.jsonDatagrid(map.get(ConfigFile.listData),map.get(ConfigFile.total));
		} catch (Exception e){
			logger.error(e.getMessage());
			return ToolClient.exceptionJson();
		}
	}
	
	/**根据id获取全字段数据*/
	public String queryById(final PageFormData pageFormData){
		final String json = ToolClient.validateFields(pageFormData,new String[]{"id"});
		if(!ToolString.isBlank(json))return json;
		try {
			return ToolClient.queryJson(dao.queryForHashMap("${nameSpace}.queryById",pageFormData.getString("id")));
		} catch (Exception e){
			logger.error("${className}Service出现异常",e);
			return ToolClient.exceptionJson();
		}
	}
}