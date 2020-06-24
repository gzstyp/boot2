package com.fwtai.service.core;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.dao.BaseService;
import com.fwtai.dao.DaoBase;
import com.fwtai.service.async.AsyncHandle;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolListOrMap;
import com.fwtai.tool.ToolProperties;
import com.fwtai.tool.ToolSHA;
import com.fwtai.tool.ToolString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**系统账号(用户)*/
@Service
public class UserService{

    private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DaoBase dao;
	
	@Autowired
	private BaseService baseService;

	@Autowired
    private AsyncHandle asyncHandle;

	/**用于递归查询存储数据*/
	private ArrayList<String> listPids = null;
	
	/**组成密码*/
	private final String createSHA(final String account,final String pwd){
		return ToolSHA.encoder(account,pwd);
	}
	
	/**存入登录者信息到Session*/
	private final void setSession(final HttpSession session,final String uid,final String account){
		session.setAttribute(ConfigFile.LOGIN_KEY,uid);//登录人的系统id主键编号标识
		session.setAttribute(ConfigFile.LOGIN_USER,account);
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
	
	/**根据id查询账号*/
	private final String queryUserById(final String login_key)throws Exception{
		return dao.queryForString("sys_user.queryUserById",login_key);
	}
	
	/**根据id及账号查询是否存在,存在则不为空,否则为空*/
	private final String queryUserByHashMap(final String uid,final String account)throws Exception{
		final HashMap<String, String> map = new HashMap<String, String>();
		map.put("uid",uid);
		map.put("account",account);
		return dao.queryForString("sys_user.queryUserByHashMap",map);
	}
	
	/**根据账号id删除对应的账号角色 */
	private final int delUserRoleByUid(final String id)throws Exception{
		return dao.execute("sys_user.delUserRoleByUid",id);
	}
	
	/**根据账号id删除对应的账号私有菜单 */
	private final int delUserMenuByUid(final String id)throws Exception{
		return dao.execute("sys_user.delUserMenuByUid",id);
	}
	
	/**根据账号id查询是否有分配角色 */
	private final int queryRoleByUid(final String id)throws Exception{
		return dao.queryForInteger("sys_user.queryRoleByUid",id);
	}
	
	/**根据账号id查询是否有私有菜单*/
	private final int queryMenuByUid(final String id)throws Exception{
		return dao.queryForInteger("sys_user.queryMenuByUid",id);
	}
	
	/**批量删除账号角色 */
	private final int batchDelUserRole(final ArrayList<String> listIds)throws Exception{
		return dao.execute("sys_user.delUserRole",listIds);
	}
	
	/**删除移除session数据值*/
	private final void removeSwitcher(final HttpSession session){
		try {
			session.removeAttribute(ConfigFile.switcher_login_key);
			session.removeAttribute(ConfigFile.switcher_login_user);
		} catch (Exception e){
			logger.error("UserService的方法removeSwitcher出现异常:",e);
		}
	}
	
	/**账号登录验证*/
	public String userLogin(final HttpServletRequest request,final PageFormData pageFormData){
		ConfigFile.ip = ToolClient.getIp(request);
		final boolean lock = ToolProperties.getInstance("config/config.properties").getInteger("login_lock","1") == 1 ? true : false;//是否开启登录错误次数锁定功能:1开启0禁用
		try{
			String code = "code";//验证码
			String pwd = "pwd";//密码
			final String account = ToolString.wipeSpace(pageFormData.getString("account"));
			pwd = ToolString.wipeSpace(pageFormData.getString(pwd));
			code = ToolString.wipeSpace(pageFormData.getString(code));
			if(ToolString.isBlank(account)){
				return ToolClient.createJson(ConfigFile.code199,"请输入登录账号");
			}
			if(ToolString.isBlank(pwd)){
				return ToolClient.createJson(ConfigFile.code199,"请输入登录账号密码");
			}
			if(ToolString.isBlank(code)){
				return ToolClient.createJson(ConfigFile.code198,"请输入验证码");
			}
			final HttpSession session = request.getSession(false);
			if(ToolString.isBlank(session)){
				return ToolClient.createJson(ConfigFile.code198,"请刷新图形验证码");
			}
			//获取session中验证码
			final Object obj_code = session.getAttribute(ConfigFile.imageCode);
			if(obj_code == null){
				return ToolClient.createJson(ConfigFile.code198,"请刷新图形验证码");
			}
			if(!obj_code.toString().equalsIgnoreCase(code)){
				return ToolClient.createJson(ConfigFile.code198,"输入的验证码不正确");
			}
			if(lock){
				final Long errors = dao.queryForLong("sys_user.query_errors",account);
				if(ToolString.isBlank(errors)){
					return ToolClient.createJson(ConfigFile.code206,ConfigFile.msg206);
				}
				if(errors < 0){
					final String lockTime = dao.queryForString("sys_user.lockTime",account);/*查询被锁定的时间*/
					final String time = lockTime.contains(".")?lockTime.substring(0,lockTime.lastIndexOf(':')):lockTime;
					return ToolClient.createJson(ConfigFile.code199,"当前帐号的密码连续错误3次<br/>已被系统临时锁定<br/>请在"+time+"后重新登录");
				}
				final String enabled = dao.queryForString("sys_user.queryUserEnabled",account);
				if(enabled.equals("1")){
					return ToolClient.createJson(ConfigFile.code199,account+"账号已被冻结");
				}
				pageFormData.put("pwd",createSHA(account,pwd));
				final String uid = dao.queryForString("sys_user.login",pageFormData);
				if(ToolString.isBlank(uid)){
					dao.execute("sys_user.superposition",account);/*每错误一次就累加叠加1*/
					final Integer error_count = dao.queryForInteger("sys_user.query_error_count",account);/*查询错误次数*/
					if (error_count >= 3){
						dao.execute("sys_user.update_time",account);/*当错误3次时更新错误的时刻就锁定*/
						return ToolClient.createJson(ConfigFile.code199,"当前帐号的密码连续错误3次<br/>已被系统临时锁定,请30分钟后重试");
					}
					return ToolClient.createJson(ConfigFile.code206,ConfigFile.msg206);
				}else{
					ConfigFile.userId = uid;
					ConfigFile.userName = account;
					setSession(session,uid,account);
					final HashMap<String,String> params = new HashMap<String,String>();
					params.put("uid",uid);
					params.put("logintime",new ToolString().getTime());
                    asyncHandle.updateLogin(params,true);
					return ToolClient.createJson(ConfigFile.code200,"登录成功");
				}
			}else{
				final String id = accountExist(account);
				if(ToolString.isBlank(id)){
					return ToolClient.createJson(ConfigFile.code206,ConfigFile.msg206);
				}
				final String enabled = dao.queryForString("sys_user.queryUserEnabled",account);
				if(enabled.equals("1")){
					return ToolClient.createJson(ConfigFile.code199,account+"账号已被冻结");
				}
				pageFormData.put("pwd",createSHA(account,pwd));
				final String uid = dao.queryForString("sys_user.login",pageFormData);
				if(ToolString.isBlank(uid)){
					return ToolClient.createJson(ConfigFile.code206,ConfigFile.msg206);
				}else{
					ConfigFile.userId = uid;
					ConfigFile.userName = account;
					setSession(session,uid,account);
					final HashMap<String,String> params = new HashMap<String,String>();
					params.put("uid",uid);
					params.put("logintime",new ToolString().getTime());
                    asyncHandle.updateLogin(params,false);
					return ToolClient.createJson(ConfigFile.code200,"登录成功");
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		    return ToolClient.exceptionJson();
		}
	}
	
	/**账号的数据列表*/
	public String listData(PageFormData pageFormData){
		try {
			pageFormData = ToolClient.datagridPagingMysql(pageFormData);
			final HashMap<String, Object> map = dao.queryForPage(pageFormData, "sys_user.listData", "sys_user.listTotal");
			return ToolClient.jsonDatagrid(map.get(ConfigFile.listData),map.get(ConfigFile.total));
		} catch (Exception e){
			logger.error("UserService的方法listData出现异常:",e);
			return ToolClient.exceptionJson();
		}
	}
	
	/**查询账号是否已存在,为空时说明不存在,否则存在*/
	private final String accountExist(final String account)throws Exception{
		return dao.queryForString("sys_user.queryAccountExist",account);
	}
	
	/**系统账号的添加*/
    @Transactional
    public String add(final PageFormData pageFormData)throws Exception{
        final String p_account = "account";
        final String p_pwd = "pwd";
        final String p_repwd = "repwd";
        if(!ToolClient.validateField(pageFormData,new String[]{p_account,p_pwd,p_repwd}))return ToolClient.jsonValidateField();
        final String account = pageFormData.getString(p_account);
        final String pwd = pageFormData.getString(p_pwd);
        final String repwd = pageFormData.getString(p_repwd);
        if(!pwd.equals(repwd)){
            return ToolClient.createJson(ConfigFile.code199,"输入的两次密码不一致");
        }
        final String id = accountExist(account);
        if(!ToolString.isBlank(id)){
            return ToolClient.createJson(ConfigFile.code199,"账号'"+account+"'已存在");
        }
        pageFormData.put("uid",ToolString.getIdsChar32());
        pageFormData.put("pwd",createSHA(account,pwd));
        pageFormData.put("added",new ToolString().getTime());
        pageFormData.put("type",1);
        pageFormData.put("KID",ToolString.getIdsChar32());
        dao.execute("sys_user.addDepUser",pageFormData);
        final int rows = dao.execute("sys_user.add",pageFormData);
        return ToolClient.executeRows(rows);
    }
	
	/**账号列表删除账号*/
	public String delById(final HttpServletRequest request,final PageFormData pageFormData)throws Exception{
		final String json = ToolClient.validateFields(pageFormData,new String[]{"id"});
		if (!ToolString.isBlank(json))return json;
		final String id = pageFormData.getString("id");
		final String login_user = queryUserById(id);
		if(ToolString.isBlank(login_user)){
			return ToolClient.createJson(ConfigFile.code199,"该账号已被删除");
		}
		if(id.equals(ToolClient.loginKey(request,ConfigFile.LOGIN_KEY))){
			return ToolClient.createJson(ConfigFile.code199,"你不能删除自己账号");
		}
		if(login_user.equals(ToolClient.loginKey(request,ConfigFile.LOGIN_USER))){
			return ToolClient.createJson(ConfigFile.code199,"你不能删除自己账号");
		}
		int rows = dao.execute("sys_user.delById",id);
		delUserRoleByUid(id);
		delUserMenuByUid(id);
        dao.execute("sys_user.delDepUser",id);
		return ToolClient.executeRows(rows);
	}
	
	/**批量删除账号*/
	@Transactional
	public String delIds(final HttpServletRequest request,final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"ids"}))return ToolClient.jsonValidateField();
		final String ids = pageFormData.getString("ids");
		final ArrayList<String> listIds = ToolString.keysToList(ids,",");
		final String login_key = ToolClient.loginKey(request,ConfigFile.LOGIN_KEY);
		int rows = 0 ;
		if(listIds != null && listIds.size() > 0){
			for(int i = 0; i < listIds.size(); i++){
				if(listIds.get(i).equals(login_key)){
					listIds.remove(i);
					break;
				}
			}
			rows = dao.executeBatch("sys_user.del",listIds);//批量删除账号
            dao.executeBatch("sys_user.delsDepUser",listIds);//删除|批量删除组织机构部门账号
			batchDelUserRole(listIds);
			dao.execute("sys_user.delUserMenu",listIds);//批量删除对应的账号私有菜单
		}
		return ToolClient.executeRows(rows);
	}
	
	/**登录者修改自身的密码*/
	public String alterPwd(final HttpServletRequest request,final PageFormData pageFormData)throws Exception{
		final String p_current = "pass_current";
		final String p_new = "pass_new";
		final String p_confirm = "pass_confirm";
		if(!ToolClient.validateField(pageFormData,new String[]{"uid",p_current,p_new,p_confirm}))return ToolClient.jsonValidateField();
		final String current = pageFormData.getString(p_current);
		final String pwd = pageFormData.getString(p_new);
		final String repwd = pageFormData.getString(p_confirm);
		if(!pwd.equals(repwd)){
			return ToolClient.createJson(ConfigFile.code199,"输入的两次密码不一致");
		}
		if(current.equals(repwd)){
			return ToolClient.createJson(ConfigFile.code199,"新密码和旧密码不能相同");
		}
		final HttpSession session = request.getSession(false);
		if(ToolString.isBlank(session)){
			return ToolClient.createJson(ConfigFile.code205,ConfigFile.msg205);
		}
		final String uid = session.getAttribute(ConfigFile.LOGIN_KEY).toString();
		final String login_key = pageFormData.getString("uid");
		final String account = session.getAttribute(ConfigFile.LOGIN_USER).toString();
		if(!uid.equals(login_key)){
			return ToolClient.createJson(ConfigFile.code207,ConfigFile.illegality_handle);
		}
		final String login_user = queryUserById(login_key);
		if(ToolString.isBlank(login_user)){
			return ToolClient.createJson(ConfigFile.code207,ConfigFile.msg207);
		}
		pageFormData.put("account",account);
		pageFormData.put("pwd_current",createSHA(account,current));
		final String id = dao.queryForString("sys_user.verificationLogin",pageFormData);//验证登录信息
		if(ToolString.isBlank(id)){
			return ToolClient.createJson(ConfigFile.code199,"当前的密码不正确");
		}
		pageFormData.put("pwd",createSHA(account,repwd));
		pageFormData.remove("pass_new");
		pageFormData.remove("pass_current");
		pageFormData.remove("pass_confirm");
		pageFormData.remove("pwd_current");
		final int rows = dao.execute("sys_user.alterPwd",pageFormData);
		return ToolClient.executeRows(rows);
	}

    /**编辑-账号列表重置密码*/
    @Transactional
    public String editPwd(final PageFormData pageFormData)throws Exception{
        final String p_account = "account";
        final String p_pwd = "pwd";
        final String p_repwd = "repwd";
        if(!ToolClient.validateField(pageFormData,new String[]{"id"}))return ToolClient.jsonValidateField();
        final String account = pageFormData.getString(p_account);
        final String pwd = pageFormData.getString(p_pwd);
        final String repwd = pageFormData.getString(p_repwd);
        if(!pwd.equals(repwd)){
            return ToolClient.createJson(ConfigFile.code199,"输入的两次密码不一致");
        }
        final String id = accountExist(account);
        if(ToolString.isBlank(id)){
            return ToolClient.createJson(ConfigFile.code199,account+"账号已被删除");
        }
        pageFormData.remove("pwd");
        pageFormData.remove("repwd");
        int rows = 0;
        if(!ToolString.isBlank(pwd) && !ToolString.isBlank(repwd)){
            pageFormData.put("newPwd",createSHA(account,pwd));
            rows = dao.execute("sys_user.editPwd",pageFormData);
        }
        final String DEP_ID = pageFormData.getString("DEP_ID");
        final String uid = pageFormData.getString("id");
        if(!ToolString.isBlank(DEP_ID)){
            final int total = dao.queryForInteger("sys_user.queryDepUser",uid);
            if(total > 0){
                rows = dao.execute("sys_user.updateDepUser",pageFormData);
            }else {
                pageFormData.put("KID",ToolString.getIdsChar32());
                pageFormData.put("uid",uid);
                rows = dao.execute("sys_user.addDepUser",pageFormData);
            }
        }
        if(ToolString.isBlank(pwd) && ToolString.isBlank(repwd) && ToolString.isBlank(DEP_ID)){
            return ToolClient.createJson(ConfigFile.code199,"请输入密码或选择组织机构");
        }
        return ToolClient.executeRows(rows);
    }

	/**加载读取账号(用户)私有菜单[分配](如果登录者是'admin'账号查询所有菜单)*/
	public String userMenu(final HttpServletRequest request,final PageFormData pageFormData){
		try {
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
				return ToolClient.queryJson(dao.queryForListMap("sys_user.userMenu",pageFormData));
			}else{
				pageFormData.put("login_key",login_key);
				return ToolClient.queryJson(dao.queryForListMap("sys_user.userMenuByLoginKey",pageFormData));//UNION外加目标账号自身的私有菜单
			}
		} catch (Exception e){
			return ToolClient.exceptionJson();
		}
	}
	
	/**查看账号的菜单数据*/
	public String viewMenuUid(final HttpServletRequest request,final PageFormData pageFormData){
		if(!ToolClient.validateField(pageFormData,new String[]{"keyUid"}))return ToolClient.jsonValidateField();
		try {
			final String login_user = ToolClient.loginKey(request,ConfigFile.LOGIN_USER);
			final String exist = queryUserByHashMap(ToolClient.loginKey(request,ConfigFile.LOGIN_KEY),login_user);
			if(ToolString.isBlank(exist)){
				return ToolClient.createJson(ConfigFile.code207,ConfigFile.msg207);
			}
			final String keyUid = pageFormData.getString("keyUid");
			final String user = queryUserById(keyUid);
			if(ToolString.isBlank(user)){
				return ToolClient.createJson(ConfigFile.code199,ConfigFile.user_inexistence);
			}
			final String pId = pageFormData.getString("id");
			pageFormData.put("pId",ToolString.isBlank(pId)?"0":pId);
			if(login_user.equals(ConfigFile.KEY_SUPER)){
				return ToolClient.queryJson(dao.queryForListMap("sys_user.viewMenuAdmin",pageFormData));//admin专属
			}
			return ToolClient.queryJson(dao.queryForListMap("sys_user.viewMenuUid",pageFormData));//自身的私有菜单
		} catch (Exception e){
			return ToolClient.exceptionJson();
		}
	}
	
	/**加载读取账号(用户)角色(如果登录者是'admin'账号查询所有角色;如果为其他账号查询该账号下所有角色)*/
	public String userRole(final HttpServletRequest request,final PageFormData pageFormData){
		try {
			final String login_key = ToolClient.loginKey(request,ConfigFile.LOGIN_KEY);
			final String login_user = queryUserById(login_key);
			if(ToolString.isBlank(login_user)){
				return ToolClient.createJson(ConfigFile.code207,ConfigFile.msg207);
			}
			if(!login_user.equals(ToolClient.loginKey(request,ConfigFile.LOGIN_USER))){
				return ToolClient.createJson(ConfigFile.code207,ConfigFile.illegality_handle);
			}
			if(login_user.equals(ConfigFile.KEY_SUPER)){
				return ToolClient.queryJson(dao.queryForListMap("sys_user.userRole",pageFormData));
			}else{
				pageFormData.put("login_key",login_key);
				return ToolClient.queryJson(dao.queryForListMap("sys_user.userRoleByLoginKey",pageFormData));//UNION外加目标账号自身的角色
			}
		} catch (Exception e){
			return ToolClient.exceptionJson();
		}
	}
	
	/**切换此账号登录*/
	public String switcherUser(final HttpServletRequest request,final PageFormData pageFormData){
		final String p_uid = "uid";
		if(!ToolClient.validateField(pageFormData,new String[]{p_uid}))return ToolClient.jsonValidateField();
		final String uid = pageFormData.getString(p_uid);
		try {
			final String login_key = ToolClient.loginKey(request,ConfigFile.LOGIN_KEY);
			final String login_user = queryUserById(login_key);
			if(ToolString.isBlank(login_user)){
				return ToolClient.createJson(ConfigFile.code207,ConfigFile.msg207);
			}
			if(!login_user.equals(ToolClient.loginKey(request,ConfigFile.LOGIN_USER))){
				return ToolClient.createJson(ConfigFile.code207,ConfigFile.illegality_handle);
			}
			final String account = queryUserById(uid);
			if(ToolString.isBlank(account)){
				return ToolClient.createJson(ConfigFile.code199,ConfigFile.user_inexistence);
			}
			final String status = baseService.queryUserEnabled(login_key);
			if(status.equals("1")){
				return ToolClient.createJson(ConfigFile.code207,ConfigFile.YOUR_DISABLE);
			}
			if(login_key.equals(uid)){
				return ToolClient.createJson(ConfigFile.code199,"不能切换到当前已登录的账号");
			}
			final String enabled = baseService.queryUserEnabled(uid);
			if(enabled.equals("1")){
				return ToolClient.createJson(ConfigFile.code199,ConfigFile.user_disable);
			}
			final HttpSession session = request.getSession(false);
			if(ToolString.isBlank(session)){
				return ToolClient.createJson(ConfigFile.code205,ConfigFile.msg205);
			}
			final Object switcher_login_key = session.getAttribute(ConfigFile.switcher_login_key);
			final Object switcher_login_user = session.getAttribute(ConfigFile.switcher_login_user);
			if(!ToolString.isBlank(switcher_login_key) || !ToolString.isBlank(switcher_login_user)){
				return ToolClient.createJson(ConfigFile.code199,"你已切换过了不能再次切换");
			}
			session.setAttribute(ConfigFile.switcher_login_key,login_key);
			session.setAttribute(ConfigFile.switcher_login_user,login_user);
			setSession(session,uid,account);
			return ToolClient.createJson(ConfigFile.code200,"切换成功");
		} catch (Exception e){
			return ToolClient.exceptionJson();
		}
	}
	
	/**切换原帐号登录*/
	public String switcher(final HttpServletRequest request,final PageFormData pageFormData){
		try {
			final HttpSession session = request.getSession(false);
			if(ToolString.isBlank(session)){
				return ToolClient.createJson(ConfigFile.code207,ConfigFile.msg207);//code为207便于跳转到登录页面
			}
			final Object uid = session.getAttribute(ConfigFile.switcher_login_key);
			final Object account = session.getAttribute(ConfigFile.switcher_login_user);
			if(ToolString.isBlank(uid) || ToolString.isBlank(account)){
				return ToolClient.createJson(ConfigFile.code207,ConfigFile.illegality_handle);
			}
			final String exist = queryUserByHashMap(String.valueOf(uid),String.valueOf(account));
			if(ToolString.isBlank(exist)){
				removeSwitcher(session);
				session.removeAttribute(ConfigFile.LOGIN_KEY);
				session.removeAttribute(ConfigFile.LOGIN_USER);
				return ToolClient.createJson(ConfigFile.code207,"原帐号已被删除");
			}
			final String status = baseService.queryUserEnabled(String.valueOf(uid));
			if(status.equals("1")){
				return ToolClient.createJson(ConfigFile.code207,ConfigFile.YOUR_DISABLE);
			}
			removeSwitcher(session);
			setSession(session,String.valueOf(uid),String.valueOf(account));
			return ToolClient.createJson(ConfigFile.code200,"切换成功");
		} catch (Exception e){
			return ToolClient.exceptionJson();
		}
	}
	
	/**登录者为admin时账号批量分配角色的角色读取*/
	public String queryAllotRole(final HttpServletRequest request,final PageFormData pageFormData){
		try{
			final String login_key = ToolClient.loginKey(request,ConfigFile.LOGIN_KEY);
			final String login_user = queryUserById(login_key);
			if(ToolString.isBlank(login_user)){
				return ToolClient.createJson(ConfigFile.code207,ConfigFile.msg207);
			}
			if(login_user.equals(ConfigFile.KEY_SUPER)){
				return ToolClient.queryJson(dao.queryForListMap("sys_user.userRole",pageFormData));
			}else{
				return ToolClient.createJson(ConfigFile.code198,"无操作权限");
			}
		} catch (Exception e){
			return ToolClient.exceptionJson();
		}
	}
	
	/**账号|用户私有菜单的保存*/
	@Transactional
	public String saveIds(final HttpServletRequest request,final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"uid"}))return ToolClient.jsonValidateField();
		final String ids = pageFormData.getString("ids");
		final String uid = pageFormData.getString("uid");
		if(uid.equals(ToolClient.loginKey(request,ConfigFile.LOGIN_KEY))){
			return ToolClient.createJson(ConfigFile.code199,"你不能更改自己的私有菜单");
		}
		final String login_user = queryUserById(uid);
		if(ToolString.isBlank(login_user)){
			return ToolClient.createJson(ConfigFile.code199,ConfigFile.user_inexistence);
		}
		ArrayList<String> listIds = ToolString.keysToList(ids,",");
		final int rows = dao.execute("sys_user.delUserMenuByRid",uid);//删除原先的账号(用户)的菜单数据
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
					map.put("uid",uid);
					map.put("mid",listIds.get(i));
					listMaps.add(map);
				}
				bath = dao.executeBatch("sys_user.saveIds",listMaps);//批量插入账号(用户)菜单表 
			}else{
				final ArrayList<HashMap<String, Object>> listMaps = new ArrayList<HashMap<String, Object>>();
				for(int i = 0; i < listIds.size(); i++){
					final HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("id",ToolString.getIdsChar32());
					map.put("uid",uid);
					map.put("mid",listIds.get(i));
					listMaps.add(map);
				}
				bath = dao.executeBatch("sys_user.saveIds",listMaps);//批量插入账号(用户)菜单表 
			}
		}
		if(bath == 0 && rows == 0){
			return ToolClient.createJson(ConfigFile.code199,"请选择菜单");
		}
		return ToolClient.executeRows((rows > 0 || bath > 0)?1:0);
	}
	
	/**账号|用户角色的保存*/
	@Transactional
	public String saveRoleIds(final HttpServletRequest request,final PageFormData pageFormData)throws Exception{
		final String p_uid = "uid";
		if(!ToolClient.validateField(pageFormData,new String[]{p_uid}))return ToolClient.jsonValidateField();
		final String ids = pageFormData.getString("ids");
		final String uid = pageFormData.getString(p_uid);
		if(uid.equals(ToolClient.loginKey(request,ConfigFile.LOGIN_KEY))){
			return ToolClient.createJson(ConfigFile.code199,"你不能更改自己的角色");
		}
		final String login_user = queryUserById(uid);
		if(ToolString.isBlank(login_user)){
			return ToolClient.createJson(ConfigFile.code199,ConfigFile.user_inexistence);
		}
		final ArrayList<String> listIds = ToolString.keysToList(ids,",");
		final int rows = delUserRoleByUid(uid);//删除原先账号(用户)角色的数据
		int bath = 0 ;
		if(listIds != null && listIds.size() > 0){
			final ArrayList<HashMap<String, Object>> listMaps = new ArrayList<HashMap<String, Object>>();
			for(int i = 0; i < listIds.size(); i++){
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id",ToolString.getIdsChar32());
				map.put("uid",uid);
				map.put("rid",listIds.get(i));
				listMaps.add(map);
			}
			bath = dao.executeBatch("sys_user.saveRoleIds",listMaps);//批量插入用户角色表
		}
		if(bath == 0 && rows == 0){
			return ToolClient.createJson(ConfigFile.code199,"请选择角色");
		}
		return ToolClient.executeRows((rows > 0 || bath > 0)?1:0);
	}
	
	/**批量账号角色分配的保存*/
	@Transactional
	public String batchAllotRole(final HttpServletRequest request,final PageFormData pageFormData)throws Exception{
		final String p_rids = "rids";
		final String p_uids = "uids";
		if(!ToolClient.validateField(pageFormData,new String[]{p_rids,p_uids}))return ToolClient.jsonValidateField();
		final String rids = pageFormData.getString(p_rids);
		final String uids = pageFormData.getString(p_uids);
		final ArrayList<String> listRids = ToolString.keysToList(rids,",");
		final ArrayList<String> listUids = ToolString.keysToList(uids,",");
		if(listRids == null || listRids.size() <= 0){
			return ToolClient.createJson(ConfigFile.code202,"请选择角色");
		}
		if(listUids == null || listUids.size() <= 0){
			return ToolClient.createJson(ConfigFile.code202,"请选择要分配角色的账号");
		}
		int bath = 0 ;
		final ArrayList<HashMap<String, Object>> listMaps = new ArrayList<HashMap<String, Object>>();
		for(int i = 0; i < listUids.size(); i++){
			final String uid = listUids.get(i);
			bath = delUserRoleByUid(uid);//删除原先账号角色的数据
			for(int j = 0; j < listRids.size(); j++){
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id",ToolString.getIdsChar32());
				map.put("uid",uid);
				map.put("rid",listRids.get(j));
				listMaps.add(map);
			}
		}
		final int rows = dao.executeBatch("sys_user.saveRoleIds",listMaps);//批量插入用户角色表
		return ToolClient.executeRows((rows > 0 || bath > 0)?1:0);
	}
	
	/**去除删除账号的私有菜单*/
	public String removeUserMenu(final HttpServletRequest request,final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"id"}))return ToolClient.jsonValidateField();
		final String id = pageFormData.getString("id");
		if(id.equals(ToolClient.loginKey(request,ConfigFile.LOGIN_KEY))){
			return ToolClient.createJson(ConfigFile.code199,"你不能去除自己的私有菜单");
		}
		final String login_user = queryUserById(id);
		if(ToolString.isBlank(login_user)){
			return ToolClient.createJson(ConfigFile.code199,ConfigFile.user_inexistence);
		}
		final int total = queryMenuByUid(id);
		if(total <= 0){
			return ToolClient.createJson(ConfigFile.code199,"该账号未分配私有菜单");
		}
		final int rows = delUserMenuByUid(id);
		return ToolClient.executeRows(rows);
	}
	
	/**去除删除账号的角色*/
	public String removeUserRole(final HttpServletRequest request,final PageFormData pageFormData)throws Exception{
		if(!ToolClient.validateField(pageFormData,new String[]{"id"}))return ToolClient.jsonValidateField();
		final String id = pageFormData.getString("id");
		if(id.equals(ToolClient.loginKey(request,ConfigFile.LOGIN_KEY))){
			return ToolClient.createJson(ConfigFile.code199,"你不能去除自己的角色");
		}
		final String login_user = queryUserById(id);
		if(ToolString.isBlank(login_user)){
			return ToolClient.createJson(ConfigFile.code199,ConfigFile.user_inexistence);
		}
		final int total = queryRoleByUid(id);
		if(total <= 0){
			return ToolClient.createJson(ConfigFile.code199,"该账号未分配角色");
		}
		final int rows = delUserRoleByUid(id);
		return ToolClient.executeRows(rows);
	}
	
	/**操作:(禁用[冻结]|启用)*/
	public String enabled(final HttpServletRequest request,final PageFormData pageFormData)throws Exception{
		final String p_uid = "uid";
		final String p_enabled = "enabled";
		if(!ToolClient.validateField(pageFormData,new String[]{p_uid,p_enabled}))return ToolClient.jsonValidateField();
		final String uid = pageFormData.getString(p_uid);
		final String enabled = pageFormData.getString(p_enabled);
		final String user = queryUserById(uid);
		if(ToolString.isBlank(user)){
			return ToolClient.createJson(ConfigFile.code199,ConfigFile.user_inexistence);
		}
		final String login_key = ToolClient.loginKey(request,ConfigFile.LOGIN_KEY);
		final String login_user = ToolClient.loginKey(request,ConfigFile.LOGIN_USER);
		if(uid.equals(login_key)){
			return ToolClient.createJson(ConfigFile.code199,"你不能操作自己的账号");
		}
		final String exist = queryUserByHashMap(login_key,login_user);
		if(ToolString.isBlank(exist)){
			return ToolClient.createJson(ConfigFile.code207,ConfigFile.msg207);
		}
		final String result = baseService.queryUserEnabled(uid);
		if(!result.equals(enabled)){
			return ToolClient.createJson(ConfigFile.code207,ConfigFile.illegality_handle);
		}
		int type = 1 ;
		if(result.equals("1")){
			type = 0 ;
		}
		pageFormData.put("type",type);
		return ToolClient.executeRows(dao.execute("sys_user.changeEnabled",pageFormData));
	}

	/**批量去除账号角色*/
	public String batchRemoveRole(final HttpServletRequest request,final PageFormData pageFormData)throws Exception{
        final String p_ids = "ids";
        if(!ToolClient.validateField(pageFormData,new String[]{p_ids}))return ToolClient.jsonValidateField();
        final String uids = pageFormData.getString(p_ids);
        final String login_key = ToolClient.loginKey(request,ConfigFile.LOGIN_KEY);
        final ArrayList<String> listUids = ToolString.keysToList(uids,",");
        if(listUids == null || listUids.size() <= 0){
            return ToolClient.createJson(ConfigFile.code199,"请选择去除角色的账号");
        }
        for(int i = 0; i < listUids.size(); i++){
            if(listUids.get(i).equals(login_key)){
                listUids.remove(i);
                break;
            }
        }
        final int rows = batchDelUserRole(listUids);
        return ToolClient.executeRows(rows,"去除角色成功","所选的账号都没有角色");
	}
}