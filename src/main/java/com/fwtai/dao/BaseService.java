package com.fwtai.dao;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

/**Service层基本操作处理工具类*/
@Service
public class BaseService{

    private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DaoBase dao;
	
	/**获取登录者的id主键编号标识*/
	public String getLoginKey(final HttpServletRequest request){
		return ToolClient.loginKey(request,ConfigFile.LOGIN_KEY);
	}
	
	/**获取登录者的账号*/
	public String getLoginUser(final HttpServletRequest request){
		return ToolClient.loginKey(request,ConfigFile.LOGIN_USER);
	}
	
	/**根据id查询全字段(含关联查询)数据,客户端响应时判断code为200,取值是data.map解析后判断data.code == AppKey.code.code200 即可,*/
	public String queryById(final String sqlMapId,final String id){
		if(ToolString.isBlank(id))return ToolClient.jsonValidateField();
		try {
			return ToolClient.queryJson(dao.queryForMap(sqlMapId,id));
		} catch (Exception e){
			logger.error("BaseService出现异常:",e);
			return ToolClient.exceptionJson();
		}
	}
	
	/**根据id删除数据,不含关联级联删除,解析后判断data.code == AppKey.code.code200 即可*/
	public String delById(final String sqlMapId,final String id)throws Exception{
		if(ToolString.isBlank(id))return ToolClient.jsonValidateField();
		return ToolClient.executeRows(dao.execute(sqlMapId,id));
	}
	
	/**批量删除数据,不含关联级联删除,解析后判断data.code == AppKey.code.code200 即可*/
	public String batchDel(final String sqlMapId,final ArrayList<String> listIds)throws Exception{
		if(ToolString.isBlank(listIds))return ToolClient.jsonValidateField();
		return ToolClient.executeRows(dao.executeBatch(sqlMapId,listIds));
	}
	
	/**批量添加保存,解析后判断data.code == AppKey.code.code200 即可*/
	public String batchAdd(final String sqlMapId,final ArrayList<HashMap<String, Object>> listMaps)throws Exception{
		if(ToolString.isBlank(listMaps))return ToolClient.jsonValidateField();
		return ToolClient.executeRows(dao.executeBatch(sqlMapId,listMaps));
	}
	
	/**查询禁用启用状态,先判断账号是否存在后再调用本方法 */
	public String queryUserEnabled(final String uid)throws Exception{
		return dao.queryForString("sys_user.queryEnabled",uid);
	}
	
	/**
	 * datagrid数据列表
	 * @param pageFormData 表单请求参数
	 * @param sqlMapIdListData 获取list集合的sql映射
	 * @param sqlMapIdTotal 获取list集合总条数的sql映射
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2017年9月6日 20:54:10
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public String listData(final PageFormData pageFormData,final String sqlMapIdListData,final String sqlMapIdTotal){
		try{
			final HashMap<String, Object> map = dao.queryForPage(pageFormData,sqlMapIdListData,sqlMapIdTotal);
			return ToolClient.jsonDatagrid(map.get(ConfigFile.listData),map.get(ConfigFile.total));
		} catch (Exception e){
			logger.error("BaseService出现异常:",e);
			return ToolClient.exceptionJson();
		}
	}
}