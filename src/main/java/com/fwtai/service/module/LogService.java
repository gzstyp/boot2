package com.fwtai.service.module;

import com.fwtai.bean.PageFormData;
import com.fwtai.dao.BaseService;
import com.fwtai.dao.DaoBase;
import com.fwtai.service.async.AsyncHandle;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**日志操作记录*/
@Service
public class LogService extends BaseService{

    private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DaoBase dao;

    @Autowired
    private AsyncHandle asyncHandle;
	
	private String unknown = "1010101";
	
	/**
	 * 登录模块日志入库保存
	 * @param name 操作的模块名称
	 * @param handle 操作关键字:1添加、2删除、3编辑、4查询、5批量添加、6批量删除,7批量更新
	 * @param ip 操作的ip地址
	 * @param info 系统操作日志的详细日志信息
	*/
	public Integer loginLog(final String name,final int handle,final String ip,final String info){
		return insert(unknown,name,handle,ip,info);
	}
	
	/**日志入库保存*/
	private Integer insert(final String uid,final String name,final int handle,final String ip,final String info){
		final HashMap<String,Object> params = new HashMap<String,Object>(0);
		final String id = ToolString.getIdsChar32();
		params.put("id",id);
		if(uid == null || uid.length() <= 0)params.put("uid",unknown);
		params.put("uid",uid);
		params.put("name",name);
		params.put("handle",handle);
		params.put("ctime",new ToolString().getTime());
		params.put("ip",ip);
		params.put("info",info);
		try {
			final Integer row = dao.execute("sys_log.add",params);
			if (row > 0){
                asyncHandle.loginLocation(id,ip);
			}
			return row;
		} catch (Exception e){
            logger.error("日志入库保存的insert失败",e);
			return -1;
		}
	}
	
	/**
	 * 日志入库保存
	 * @param uid 操作人的主键id
	 * @param name 操作的模块名称
	 * @param handle 操作关键字:1添加、2删除、3编辑、4查询、5批量添加、6批量删除,7批量更新
	 * @param ip 操作的ip地址
	 * @param info 系统操作日志的详细日志信息
	*/
	public Integer add(final String uid,final String name,final int handle,final String ip,final String info){
		return insert(uid,name,handle,ip,info);
	}
	
	/**数据列表*/
	public String listData(PageFormData pageFormData){
		pageFormData = ToolClient.datagridPagingMysql(pageFormData);
		return super.listData(pageFormData,"sys_log.listData","sys_log.listTotal");
	}
}