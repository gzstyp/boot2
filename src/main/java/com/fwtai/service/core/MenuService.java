package com.fwtai.service.core;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.dao.BaseService;
import com.fwtai.dao.DaoBase;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolListOrMap;
import com.fwtai.tool.ToolMenuMap;
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
import java.util.Map;

/**系统菜单模块*/
@Service
public class MenuService extends BaseService{

    private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DaoBase dao;
	
	/**用于递归查询存储数据*/
	private ArrayList<String> listIds = new ArrayList<String>();
	
	/**查询所有的子节点的菜单,递归查询*/
	private final void queryParentNode(final List<Map<String, Object>> list)throws Exception{
		for(int i = 0; i < list.size(); i++){
			final String id = list.get(i).get("id").toString();
			listIds.add(id);
			queryParentNode(queryIdByPid(id));
		}
	}
	
	/**查询子节点,从父节点往子节点查询节点*/
	private final List<Map<String, Object>> queryIdByPid(final String id)throws Exception{
		 return dao.queryForListMap("sys_menu.queryIdByPid",id);
	}
	
	/**查询菜单层次关系*/
	private final String findRelationById(final String pId)throws Exception{
		return dao.queryForString("sys_menu.findRelationById",pId);
	}
	
	/**查询菜单菜单的类型*/
	private final String findTypeById(final String pId)throws Exception{
		return dao.queryForString("sys_menu.findTypeById",pId);
	}
	
	/**查询父级id*/
	private final String findPidById(final String id)throws Exception{
		return dao.queryForString("sys_menu.findPidById",id);
	}
	
	private final int findChildrenTreeById(final String pId)throws Exception{
		return dao.queryForInteger("sys_menu.findChildrenTreeById",pId);
	}
	
	private final int findChildrenClickById(final String pId)throws Exception{
		return dao.queryForInteger("sys_menu.findChildrenClickById",pId);
	}
	
	/**更改菜单的state状态*/
	private final int editSState(final String pId,final int state)throws Exception{
		final HashMap<String,Object> hashMap = new HashMap<String,Object>();
		hashMap.put("id",pId);
		hashMap.put("state",state);
		return dao.execute("sys_menu.editSState",hashMap);
	}
	
	/**更改菜单的ustate状态*/
	private final int editUState(final String pId,final int state)throws Exception{
		final HashMap<String,Object> hashMap = new HashMap<String,Object>();
		hashMap.put("id",pId);
		hashMap.put("state",state);
		return dao.execute("sys_menu.editUState",hashMap);
	}
	
	/**根据账号id查询账号*/
	private final String queryUserById(final String login_key)throws Exception{
		return dao.queryForString("sys_user.queryUserById",login_key);
	}
	
	/**添加或编辑菜单时提示菜单名太长*/
	private final String menuLong(){
		return ToolClient.createJson(ConfigFile.code199,"菜单名太长");
	}
	
	/**admin所拥有某个页面里的按钮或行按钮权限|查询权限按钮,type=2查询按钮;type=3查询行按钮*/
	private final List<Map<String,Object>> queryTypeByAdmin(final PageFormData pageFormData)throws Exception{
		return dao.queryForListMap("sys_menu.adminOwnerMenuType",pageFormData);
	}
	
	/**登录者不是admin时的账号拥有导航菜单下的行按钮权限,返回到拦截器AuthInterceptor.java遍历调用,type=2或type=3*/
	private final List<Map<String,Object>> queryTypeByUserId(final PageFormData pageFormData)throws Exception{
		return dao.queryForListMap("sys_menu.authTypeByUserId",pageFormData);
	}
	
	/**菜单的添加*/
	@Transactional
	public String add(final PageFormData pageFormData) throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"name","type","pId","isParent","layer"}))return ToolClient.jsonValidateField();
		final Integer layer = Integer.parseInt(pageFormData.getString("layer")) + 1;
		//判断菜单的层次不能大于5级
		if(layer > 4 && pageFormData.getString("type").equals("1"))return ToolClient.createJson(ConfigFile.code199,"菜单已超过5级菜单");
		final String pId = pageFormData.getString("pId");
		final String mtype = pageFormData.getString("type");
		final String name = pageFormData.getString("name");
		if(name.length() > 16){
			return menuLong();
		}
		if(layer == 2 && name.length() > 15){
			return menuLong();
		}
		Integer level = 1 ;
		final String id = ToolString.getIdsChar32();
		pageFormData.put("layer",1);
		if(!pId.equals("0")){
			level = dao.queryForInteger("sys_menu.queryLayerBypId",pId);
			final int len = level + 1;
			if(len > 3 && name.length() > 11){
				return menuLong();
			}
			if(len > 2 && name.length() > 12){
				return menuLong();
			}
			pageFormData.put("layer",level+1);	
		}
		pageFormData.put("relation",("0@" + id));
		if(!pId.equals("0")){
			final String relation = findRelationById(pId);
			pageFormData.put("relation",(relation + "@" + id));
		}
		pageFormData.put("state",1);
		pageFormData.put("ustate",1);
		pageFormData.put("id",id);
		final int rows = dao.execute("sys_menu.add",pageFormData);
		if(rows > 0){
			if(!pId.equals("0")){
				String mtype_p = findTypeById(pId);
				if(mtype_p.equals("1") && mtype_p.equals(mtype)){
					editSState(pId,0);
					editUState(pId,0);
				} else{
					final List<Map<String, Object>> list = dao.queryForListMap("sys_menu.querylistByPid",id);
					if(list.size() > 0){
						final Object mid = list.get(0).get("id");
						if(mid != null){
							editSState(mid.toString(),1);
						}
					}
					editUState(pId,0);
				}
			}
			return ToolClient.executeRows(rows);
		}
		return ToolClient.executeRows(rows);
	}
	
	/**菜单的编辑*/
	@Transactional
	public String edit(final PageFormData pageFormData) throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"name","type","pId","isParent","layer","id"}))return ToolClient.jsonValidateField();
		final Integer layer = Integer.parseInt(pageFormData.getString("layer")) + 1;
		final String type = pageFormData.getString("type");
		//判断菜单的层次不能大于5级
		if(layer > 4 && type.equals("1"))return ToolClient.createJson(ConfigFile.code199,"菜单已超过5级菜单");
		final String pId = pageFormData.getString("pId");
		final String id = pageFormData.getString("id");
		final String name = pageFormData.getString("name");
		if(name.length() > 16){
			return menuLong();
		}
		if(layer == 2 && name.length() > 15){
			return menuLong();
		}
		Integer level = 1 ;
		if(pId.equals("0")){
			pageFormData.put("layer",1);
		}else{
			level = dao.queryForInteger("sys_menu.queryLayerBypId",pId);
			final int len = level + 1;
			if(len > 3 && name.length() > 11){
				return menuLong();
			}
			if(len > 2 && name.length() > 12){
				return menuLong();
			}
			pageFormData.put("layer",level+1);
		}
		pageFormData.put("relation",("0@" + id));
		if(!pId.equals("0")){
			final String relation = findRelationById(pId);
			pageFormData.put("relation",(relation+"@"+ id));
		}
		if(id.equals(pId)){
			return ToolClient.createJson(ConfigFile.code199,"不能修改自己为上一级菜单");
		}
		//根据主键id提取ustate
		final HashMap<String,Object> map = dao.queryForHashMap("sys_menu.findMenussById",id);
		pageFormData.put("ustate",map.get("ustate").toString().equals("true")?1:0);
		pageFormData.put("state",map.get("state").toString().equals("true")?1:0);
		final int rows = dao.execute("sys_menu.edit",pageFormData);
		if(rows > 0){
			// 判断上级菜单是否存在
			if(!pId.equals("0")){//存在
				// 提取上级菜单类别
				final String mtype_p = findTypeById(pId);
				if("1".equals(mtype_p)){
					int c = findChildrenTreeById(pId);
					if(c <= 0){
						int t = findChildrenClickById(pId);
						if(t > 0){
							editUState(pId,0);
						} else {
							editUState(pId,1);
						}
					}else{
						//修改上级菜单状态
						editSState(pId,0);
						editUState(pId,0);
					}
				}
			}
			// 判断当前菜单是否有下级菜单tree
			final int childCount = findChildrenTreeById(id);
			if(childCount > 0){
				// 修改当前菜单状态
				editSState(id,0);
				editUState(id,0);
			}else{
				// 修改当前菜单状态
				editSState(id,1);
				// 判断当前菜单是否有下级菜单按钮或列表行
				final int count = findChildrenClickById(id);
				if(count > 0){
					editUState(id,0);
				} else {
					editUState(id,1);
				}
			}
			// 判断修改之前的上级节点是否还存在子节点
			final String keyPid = map.get("pId").toString();
			final String p_id = findPidById(keyPid);
			if("0".equals(p_id)){
				// 修改当前菜单状态
				editSState(keyPid,0);
				editUState(keyPid,0);
			}
			// 判断当前菜单是否有下级菜单tree
			int pidCount = findChildrenTreeById(keyPid);
			if(pidCount > 0){
				// 修改当前菜单状态
				editSState(keyPid,0);
				editUState(keyPid,0);
			} else {
				// 修改当前菜单状态
				editSState(keyPid,1);
				// 判断当前菜单是否有下级菜单按钮或列表行
				int countPid = findChildrenClickById(keyPid);
				if(countPid > 0){
					editUState(keyPid,0);
				} else {
					editUState(keyPid,1);
				}
			}
			return ToolClient.executeRows(rows);
		}
		return ToolClient.executeRows(rows);
	}
	
	/**删除菜单-行删除*/
	@Transactional
	public String delById(final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"id"}))return ToolClient.jsonValidateField();
		final String id = pageFormData.getString("id");
		final int rows = dao.execute("sys_menu.delById",id);//删除菜单
		if(rows > 0){
			dao.execute("sys_menu.del_menu_role",id);//删除与该id相关联的角色菜单表;
			dao.execute("sys_menu.del_menu_account",id);//删除与该id相关联的账号私有菜单表;
			queryParentNode(queryIdByPid(id));
			if(listIds != null && listIds.size() > 0){
				listIds = ToolListOrMap.listRemoveRepetition(listIds);
				final int result = dao.executeBatch("sys_menu.batchDelPid",listIds);
				if(result > 0){
					final String pid0 = findPidById(id);
					if(!ToolString.isBlank(pid0) && !pid0.equals("0")){
						final String pid = findPidById(pid0);
						final String mtype = findTypeById(pid0);
						final String mtype_p = findTypeById(pid);
						if(mtype_p.equals("1") && mtype_p.equals(mtype)){
							editSState(pid,0);
							editUState(pid,0);
						} else {
							if(mtype_p.equals("1")){
								editSState(pid,1);
							}else {
								editUState(pid,0);
							}
						}
					}
				}
			}
			dao.execute("sys_menu.del_menu_pid",id);//删除与该id作为pId的都删除
		}
		return ToolClient.executeRows(rows);
	}
	
	/**登录者所拥有的左侧的菜单列表{分为admin和登录者的uid}*/
	public String ownerMenus(final HttpServletRequest request){
		final HashMap<String, Object> params = new HashMap<String, Object>();
		String id = request.getParameter("id");
		id = ToolString.isBlank(id)?"0":id;
		params.put("id",id);
		try {
			final String login_key = ToolClient.loginKey(request,ConfigFile.LOGIN_KEY);
			final String login_user = queryUserById(login_key);
			if(!login_user.equals(ToolClient.loginKey(request,ConfigFile.LOGIN_USER))){
				return ToolClient.createJson(ConfigFile.code199,"非法操作");
			}
			if(!ToolString.isBlank(login_user)){
				if(login_user.equals(ConfigFile.KEY_SUPER)){
					return ToolClient.queryJson(dao.queryForListMap("sys_menu.ownerMenus",params));
				}else {
					params.put("uid",login_key);
					return ToolClient.queryJson(dao.queryForListMap("sys_menu.queryMenuByUserId",params));
				}
			}
			return ToolClient.queryJson(null);
		} catch (Exception e){
			e.printStackTrace();
			return ToolClient.exceptionJson();
		}
	}
	
	/**用户{分为admin和登录者的uid}拥有导航菜单下的按钮权限,返回界面的,此处的type肯定为2*/
	public String authBtn(final HttpServletRequest request,final PageFormData pageFormData){
		try {
			final String uid = ToolClient.loginKey(request, ConfigFile.LOGIN_KEY);
			final String login_user = queryUserById(uid);
			if(!login_user.equals(ToolClient.loginKey(request,ConfigFile.LOGIN_USER))){
				return ToolClient.createJson(ConfigFile.code199,"非法操作");
			}
			pageFormData.put("pId",pageFormData.getString("authBtnId"));
			if(!ToolString.isBlank(login_user)){
				if(login_user.equals(ConfigFile.KEY_SUPER)){
					return ToolClient.queryJson(queryTypeByAdmin(pageFormData));
				}else {
					pageFormData.put("uid",uid);
					return ToolClient.queryJson(queryTypeByUserId(pageFormData));
				}
			}
			return ToolClient.queryJson(null);
		} catch (Exception e){
			return ToolClient.exceptionJson();
		}
	}
	
	/**用户{分为admin和登录者的uid}拥有导航菜单下的行按钮权限,返回到拦截器AuthInterceptor.java遍历调用,此处的type肯定为3*/
	public List<Map<String, Object>> authRow(final HttpServletRequest request,final PageFormData pageFormData){
		try {
			final String uid = ToolClient.loginKey(request,ConfigFile.LOGIN_KEY);
			final String login_user = queryUserById(uid);
			if(!login_user.equals(ToolClient.loginKey(request,ConfigFile.LOGIN_USER))){
				return null;
			}
			if(!ToolString.isBlank(login_user)){
				if(login_user.equals(ConfigFile.KEY_SUPER)){
					return queryTypeByAdmin(pageFormData);
				}else{
					pageFormData.put("uid",uid);
					return queryTypeByUserId(pageFormData);
				}
			}
			return null;
		} catch (Exception e){
			return new ArrayList<Map<String, Object>>();
		}
	}
	
	/**
	 * 查询所有的菜单,用于添加或编辑菜单
	 * @param pageFormData
	 * @作者 田应平
	 * @创建时间 2017年1月21日 上午1:09:20
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public String queryAllMenu(final PageFormData pageFormData){
		String id = pageFormData.getString("id");
		id = ToolString.isBlank(id)?"0":id;
		try {
			return ToolClient.queryJson(dao.queryForListMap("sys_menu.queryAllMenu",id));
		} catch (Exception e){
			return ToolClient.exceptionJson();
		}
	}
	
	/**easyui的菜单数据列表*/
	public String listData(final HttpServletRequest request,PageFormData pageFormData){
		try {
			pageFormData = ToolClient.datagridPagingMysql(pageFormData);
			final String uid = ToolClient.loginKey(request, ConfigFile.LOGIN_KEY);
			final String login_user = queryUserById(uid);
			if(login_user.equals(ConfigFile.KEY_SUPER)){
				final HashMap<String, Object> map = dao.queryForPage(pageFormData,"sys_menu.listData","sys_menu.listTotal");
				return ToolClient.jsonDatagrid(map.get(ConfigFile.listData),map.get(ConfigFile.total));
			}else{
				final HashMap<String, Object> map = dao.queryForPage(pageFormData,"sys_menu.listDataUser","sys_menu.listTotal");
				return ToolClient.jsonDatagrid(map.get(ConfigFile.listData),map.get(ConfigFile.total));
			}
		} catch (Exception e){
			logger.error("MenuService的方法listData(出现异常:",e);
			return ToolClient.exceptionJson();
		}
	}
	
	/**根据id获取全字段数据*/
	public String queryById(final PageFormData pageFormData){
		final String json = ToolClient.validateFields(pageFormData,new String[]{"id"});
		if(!ToolString.isBlank(json))return json;
		return super.queryById("sys_menu.queryById",pageFormData.getString("id"));
	}

    /**基于账号id获取所有的账号的拥有的菜单*/
    public List<HashMap<String, Object>> queryMenusByUserId(final HttpServletRequest request){
        final String login_key = ToolClient.loginKey(request,ConfigFile.LOGIN_KEY);
        List<HashMap<String,Object>> result = new ArrayList<HashMap<String,Object>>();
        try {
            final String login_user = queryUserById(login_key);
            if(!login_user.equals(ConfigFile.KEY_SUPER)){
                result = dao.queryForListHashMap("sys_menu.queryMenusByUserId",login_key);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("MenuService的方法queryMenusByUserId(出现异常:",e);
            return result;
        }
    }

    /**获取菜单*/
    public String getList(final HttpServletRequest request){
        try{
            final List<HashMap<String,Object>> allMenu = queryListMap();
            if(allMenu != null && allMenu.size() > 0){
                final List<HashMap<String,Object>> rootMenu = new ToolMenuMap("id","pid","children").initMenu(allMenu,"88888888888888888888888888888888");
                return ToolClient.queryJson(rootMenu);
            }
            return ToolClient.queryJson(null);
        }catch(Exception e){
            e.printStackTrace();
            return ToolClient.exceptionJson("系统出现异常!");
        }
    }

    private final List<HashMap<String,Object>> queryListMap(){
        try{
            return dao.queryForListHashMap("sys_menu.queryMenu");
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}