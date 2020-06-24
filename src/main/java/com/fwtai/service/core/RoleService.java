package com.fwtai.service.core;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.dao.BaseService;
import com.fwtai.dao.DaoBase;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolListOrMap;
import com.fwtai.tool.ToolString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**系统角色*/
@Service
public class RoleService{

    private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**用于递归查询存储数据*/
	private ArrayList<String> listPids = null;
	
	@Autowired
	private DaoBase dao;
	
	@Autowired
	private BaseService baseService;
	
	/**根据id查询账号*/
	private final String queryUserById(final String login_key)throws Exception{
		return dao.queryForString("sys_user.queryUserById",login_key);
	}
	
	/**添加或编辑时查询角色名称是否已存在*/
	private final String queryRnameExist(final String rname)throws Exception{
		return dao.queryForString("sys_role.queryRnameExist",rname);
	}
	
	/**查询父级节点,从子节点往父节点查询节点*/
	private final ArrayList<String> queryParentsByIds(final ArrayList<String> listIds)throws Exception{
		if(listPids == null){
			listPids = new ArrayList<String>();
		}else{
			listPids.clear();
		}
		queryParentNode(dao.queryForListHashMap("sys_menu.queryParentById",listIds));//查询所有的父级菜单
		return ToolListOrMap.listRemoveRepetition(listPids);
	}
	
	/**查询所有的父级菜单,递归查询*/
	private final void queryParentNode(final List<HashMap<String, Object>> list)throws Exception{
		for (int i = 0; i < list.size(); i++){
			final String pId = list.get(i).get("pId").toString();
			listPids.add(pId);
			queryParentNode(queryParentNodeById(pId));
		}
	}
	
	/**根据id查询父级id,从子节点往父节点查询节点*/
	private final List<HashMap<String, Object>> queryParentNodeById(final String pId)throws Exception{
		return dao.queryForListHashMap("sys_menu.queryParentNodeById",pId);
	}
	
	/**系统角色的添加*/
	public String add(final HttpServletRequest request)throws Exception{
		final PageFormData pageFormData = ToolClient.getFormData(request);
		final String p_rname = "rname";
		if(!ToolClient.validateField(pageFormData,new String[]{p_rname}))return ToolClient.jsonValidateField();
		final String rname = pageFormData.getString(p_rname);
		final String exist_key = queryRnameExist(rname);
		if(!ToolString.isBlank(exist_key)){
			return ToolClient.createJson(ConfigFile.code199,"角色名'"+rname+"'已存在");
		}
		pageFormData.put("rid",ToolString.getIdsChar32());
		pageFormData.put("rname",rname);
		final String login_key = baseService.getLoginKey(request);
		if(ToolString.isBlank(login_key)){
			return ToolClient.createJson(ConfigFile.code205,ConfigFile.msg205);
		}
		pageFormData.put("uid",login_key);
		final int rows = dao.execute("sys_role.add",pageFormData);
		return ToolClient.executeRows(rows);
	}
	
	/**角色名称编辑*/
	public String edit(final HttpServletRequest request,final PageFormData pageFormData)throws Exception{
		final String p_rname = "rname";
		final String p_rid = "rid";
		if(!ToolClient.validateField(pageFormData,new String[]{p_rid,p_rname}))return ToolClient.jsonValidateField();
		final String rname = pageFormData.getString(p_rname);
		final String rid = pageFormData.getString(p_rid);
		final String exist_key = queryRnameExist(rname);//根据名称查询是否存在主键id
		if (ToolString.editExistKey(exist_key,rid)){
			return ToolClient.createJson(ConfigFile.code199,"请输入新的角色名称");
		}
		if (!ToolString.isBlank(exist_key)){
			return ToolClient.createJson(ConfigFile.code199,"角色名'"+rname+"'已存在");
		}
		final int rows = dao.execute("sys_role.edit",pageFormData);
		return ToolClient.executeRows(rows);
	}
	
	/**删除角色权限-行删除*/
	@Transactional
	public String delById(final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"id"}))return ToolClient.jsonValidateField();
		final String id = pageFormData.getString("id");
		final int rows = dao.execute("sys_role.delById",id);//删除角色权限-行删除
		dao.execute("sys_role.delRoleMenuById",id);//删除对应的角色菜单
		dao.execute("sys_role.delUserRoleById",id);//删除对应的账号角色
		return ToolClient.executeRows(rows);
	}
	
	/**删除[含批量删除]角色*/
	@Transactional
	public String delIds(final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"ids"}))return ToolClient.jsonValidateField();
		final String ids = pageFormData.getString("ids");
		final ArrayList<String> listIds = ToolString.keysToList(ids,",");
		int rows = 0 ;
		if(listIds != null && listIds.size() > 0){
			rows = dao.executeBatch("sys_role.del",listIds);//批量删除角色
			dao.execute("sys_role.delRoleMenu",listIds);//批量删除对应的角色菜单
			dao.execute("sys_role.delUserRole",listIds);//批量删除对应的账号角色
		}
		return ToolClient.executeRows(rows);
	}
	
	/**清空角色菜单*/
	public String removeRoleMenu(final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"id"}))return ToolClient.jsonValidateField();
		final String rid = pageFormData.getString("id");
		final int rows = dao.execute("sys_role.delRoleMenuByRid",rid);
		if(rows > 0){
			return ToolClient.createJson(ConfigFile.code200,"清空成功");
		} else {
			return ToolClient.createJson(ConfigFile.code199,"该角色没有分配菜单");
		}
	}
	
	/**角色菜单的保存*/
	@Transactional
	public String saveIds(final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"rid"}))return ToolClient.jsonValidateField();
		final String ids = pageFormData.getString("ids");
		final String rid = pageFormData.getString("rid");
		ArrayList<String> listIds = ToolString.keysToList(ids,",");
		final int rows = dao.execute("sys_role.delRoleMenuByRid",rid);//删除原先的数据
		int bath = 0 ;
		if(listIds != null && listIds.size() > 0){
			//选择菜单的上级菜单(不包含已选择菜单)
			final ArrayList<String> list = queryParentsByIds(listIds);
			if(list != null && list.size() > 0){
				for(int i = 0; i < list.size(); i++){
					if(list.get(i).equals("0")){
						list.remove(i);//移除0最最顶级的id
					}
				}
				listIds.addAll(list);
				listIds = ToolListOrMap.listRemoveRepetition(listIds);
				final ArrayList<HashMap<String, Object>> listMaps = new ArrayList<HashMap<String, Object>>();
				for(int i = 0; i < listIds.size(); i++){
					final HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("id",ToolString.getIdsChar32());
					map.put("rid",rid);
					map.put("mid",listIds.get(i));
					listMaps.add(map);
				}
				bath = dao.executeBatch("sys_role.saveIds",listMaps);//批量插入角色菜单表 
			}else{
				final ArrayList<HashMap<String, Object>> listMaps = new ArrayList<HashMap<String, Object>>();
				for(int i = 0; i < listIds.size(); i++){
					final HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("id",ToolString.getIdsChar32());
					map.put("rid",rid);
					map.put("mid",listIds.get(i));
					listMaps.add(map);
				}
				bath = dao.executeBatch("sys_role.saveIds",listMaps);//批量插入角色菜单表 
			}
		}
		if(bath == 0 && rows == 0){
			return ToolClient.createJson(ConfigFile.code199,"请选择菜单");
		}
		return ToolClient.executeRows((rows > 0 || bath > 0)?1:0);
	}
	
	/**加载角色菜单[为分配](分admin及一般账号)*/
	public String roleMenu(final HttpServletRequest request,final PageFormData pageFormData){
		try{
			final String login_key = ToolClient.loginKey(request,ConfigFile.LOGIN_KEY);
			final String login_user = queryUserById(login_key);
			if(ToolString.isBlank(login_user)){
				return ToolClient.createJson(ConfigFile.code207,ConfigFile.msg207);
			}
			if(!login_user.equals(ToolClient.loginKey(request,ConfigFile.LOGIN_USER))){
				return ToolClient.createJson(ConfigFile.code207,ConfigFile.illegality_handle);
			}
			final String pId = pageFormData.getString("id");
			pageFormData.put("pId",ToolString.isBlank(pId)?"0":pId);
			if(login_user.equals(ConfigFile.KEY_SUPER)){
				return ToolClient.queryJson(dao.queryForListMap("sys_role.roleMenu",pageFormData));
			}else {
				pageFormData.put("login_key",login_key);
				return ToolClient.queryJson(dao.queryForListMap("sys_role.roleMenuByLoginKey",pageFormData));
			}
		} catch (Exception e){
			return ToolClient.exceptionJson();
		}
	}
	
	/**数据列表*/
	public String listData(PageFormData pageFormData){
		try{
			pageFormData = ToolClient.datagridPagingMysql(pageFormData);
			final HashMap<String, Object> map = dao.queryForPage(pageFormData,"sys_role.listData","sys_role.listTotal");
			return ToolClient.jsonDatagrid(map.get(ConfigFile.listData),map.get(ConfigFile.total));
		} catch (Exception e){
			logger.error("RoleService的方法listData(出现异常:",e);
			return ToolClient.exceptionJson();
		}
	}
}