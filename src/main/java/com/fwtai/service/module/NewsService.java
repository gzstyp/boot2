package com.fwtai.service.module;

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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@Service
public class NewsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DaoBase dao;
	
	@Autowired
	private BaseService baseService;
	
	/**添加*/
	public String add(final HttpServletRequest request)throws Exception{
		final String login_key = baseService.getLoginKey(request);
		if(ToolString.isBlank(login_key)){
			return ToolClient.createJson(ConfigFile.code205, ConfigFile.msg205);
		}
		final PageFormData pageFormData = ToolClient.getFormData(request);
		pageFormData.put("nid", ToolString.getIdsChar32());
		pageFormData.put("add_date", new ToolString().getTime());
		pageFormData.put("uid",login_key);
		return ToolClient.executeRows(dao.execute("bs_news.add",pageFormData));
	}
	
	/**编辑*/
	public String edit(final HttpServletRequest request)throws Exception{
		final PageFormData pageFormData = ToolClient.getFormData(request);
		if(!ToolClient.validateField(pageFormData,new String[]{"nid"}))return ToolClient.jsonValidateField();
		return ToolClient.executeRows(dao.execute("bs_news.edit",pageFormData));
	}
	
	/**行删除*/
	public String delById(final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"nid"}))return ToolClient.jsonValidateField();
		return ToolClient.executeRows(dao.execute("bs_news.delById",pageFormData.getString("nid")));
	}
	
	/**删除[含批量删除]*/
	public String delIds(final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"ids"}))return ToolClient.jsonValidateField();
		final String ids = pageFormData.getString("ids");
		final ArrayList<String> listIds = ToolString.keysToList(ids,",");
		int rows = 0 ;
		if(listIds != null && listIds.size() > 0){
			rows = dao.executeBatch("bs_news.delIds",listIds);//批量删除
		}
		return ToolClient.executeRows(rows);
	}
	
	/**数据列表*/
	public String listData(PageFormData pageFormData){
		try{
			pageFormData = ToolClient.datagridPagingMysql(pageFormData);
			final HashMap<String, Object> map = dao.queryForPage(pageFormData,"bs_news.listData","bs_news.listTotal");
			return ToolClient.jsonDatagrid(map.get(ConfigFile.listData),map.get(ConfigFile.total));
		} catch (Exception e){
			logger.error("NewsService的方法listData(出现异常:",e);
			return ToolClient.exceptionJson();
		}
	}
	
	/**根据id获取全字段数据*/
	public String queryById(final PageFormData pageFormData){
		final String json = ToolClient.validateFields(pageFormData,new String[]{"id"});
		if(!ToolString.isBlank(json))return json;
		try {
			return ToolClient.queryJson(dao.queryForHashMap("bs_news.queryById",pageFormData.getString("id")));
		} catch (Exception e){
			logger.error("NewsService的queryById出现异常",e);
			return ToolClient.exceptionJson();
		}
	}

	/**查询全部数据,页面通过json的code为200时再操作*/
	public String listAll(final PageFormData pageFormData){
		try{
			return ToolClient.queryJson(dao.queryForListHashMap("bs_news.listAll",pageFormData));
		} catch (Exception e){
			logger.error("NewsService的方法listAll(出现异常:",e);
			return ToolClient.exceptionJson();
		}
	}
	
	/**查询listMap数据,页面通过json的code为200时再操作,即{"code":200,"listData":[{""}]}*/
	public String listMap(final HttpServletRequest request){
		final HashMap<String,String> params = ToolClient.getFormParams(request);
		try{
			return ToolClient.queryJson(dao.queryForListHashMap("bs_news.listMap",params));
		}catch(Exception e){
			logger.error("NewsService的方法listMap(出现异常:",e);
			return ToolClient.exceptionJson();
		}
	}
}