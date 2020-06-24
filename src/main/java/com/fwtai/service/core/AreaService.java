package com.fwtai.service.core;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.dao.BaseService;
import com.fwtai.dao.DaoBase;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**区域管理业务层*/
@Service
public class AreaService{

    private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DaoBase dao;
	
	@Autowired
	private BaseService baseService;
	
	/**添加*/
	public String add(final HttpServletRequest request,final PageFormData pageFormData)throws Exception{
		final String login_key = baseService.getLoginKey(request);
		if(ToolString.isBlank(login_key)){
			return ToolClient.createJson(ConfigFile.code205,ConfigFile.msg205);
		}
		final String date = new ToolString().getTime();
		pageFormData.put("date_create",date);
		pageFormData.put("date_modify",date);
		if(ToolString.isBlank(pageFormData.getString("orders"))){
			pageFormData.put("orders",null);
		}
		if(ToolString.isBlank(pageFormData.getString("parent"))){
			pageFormData.put("parent",0);
		}
		return ToolClient.executeRows(dao.execute("sys_area.add",pageFormData));
	}
	
	/**编辑*/
	public String edit(final HttpServletRequest request,final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"id"}))return ToolClient.jsonValidateField();
		pageFormData.put("date_modify",new ToolString().getTime());
		if(ToolString.isBlank(pageFormData.getString("orders"))){
			pageFormData.put("orders",null);
		}
		if(ToolString.isBlank(pageFormData.getString("parent"))){
			pageFormData.put("parent",0);
		}
		return ToolClient.executeRows(dao.execute("sys_area.edit",pageFormData));
	}
	
	/**行删除*/
	public String delById(final PageFormData pageFormData)throws Exception{
		final String id = "id";
		if(!ToolClient.validateField(pageFormData,new String[]{id}))return ToolClient.jsonValidateField();
		final Integer childs = dao.queryForInteger("sys_area.childs",pageFormData.getString(id));
		if (childs > 0){
			return ToolClient.createJson(ConfigFile.code199,"该区域名称还有下级区域,不能删除");
		}
		return ToolClient.executeRows(dao.execute("sys_area.delById",pageFormData.getString(id)));
	}
	
	/**根据id获取全字段数据*/
	public String queryById(final PageFormData pageFormData){
		final String json = ToolClient.validateFields(pageFormData,new String[]{"id"});
		if(!ToolString.isBlank(json))return json;
		try {
			return ToolClient.queryJson(dao.queryForHashMap("sys_area.queryById",pageFormData.getString("id")));
		} catch (Exception e){
			logger.error("区域管理业务层-根据id获取全字段数据",e);
			return ToolClient.exceptionJson();
		}
	}

	/**数据显示-根据父级id查询数据*/
	public String getData(final PageFormData pageFormData) {
		if (ToolString.isBlank(pageFormData.getString("parent"))) {
			pageFormData.put("parent","0");
		}
		try {
			return ToolClient.queryJson(dao.queryForListHashMap("sys_area.getData",pageFormData));
		} catch (Exception e){
			logger.error("区域管理业务层-数据显示-根据父级id查询数据",e);
			return ToolClient.exceptionJson();
		}
	}
	
	/**省市县选择-根据父级id查询数据*/
	public String queryArea(final PageFormData pageFormData) {
		if (ToolString.isBlank(pageFormData.getString("pId"))) {
			pageFormData.put("pId","0");
		}
		try {
			return ToolClient.queryJson(dao.queryForListHashMap("sys_area.queryArea",pageFormData));
		} catch (Exception e){
			logger.error("区域管理业务层-省市县选择-根据父级id查询数据",e);
			return ToolClient.exceptionJson();
		}
	}
}