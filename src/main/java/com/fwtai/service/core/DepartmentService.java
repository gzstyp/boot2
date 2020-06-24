package com.fwtai.service.core;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.dao.DaoBase;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolProperties;
import com.fwtai.tool.ToolString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * 组织机构
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
 */
@Service
public class DepartmentService{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DaoBase dao;

    /**添加*/
    public String add(final HttpServletRequest request)throws Exception{
        final String login_key = ToolClient.loginKey(request,ConfigFile.LOGIN_KEY);
        final PageFormData pageFormData = ToolClient.getFormData(request);
        final String kid = pageFormData.getString("kid");
        final String default_parent = ToolProperties.getInstance("config/config.properties").getString("default_parent","kid00000000000000000000000001024");
        final String parent_id = ToolString.isBlank(kid) ? default_parent : kid;
        pageFormData.put("creator",login_key);
        final String isParent = pageFormData.getString("isParent");
        final int dep_exist = isParent.equalsIgnoreCase("1") ? 1 : 0;
        pageFormData.put("dep_exist",dep_exist);
        pageFormData.put("parent_id",parent_id);
        pageFormData.put("kid",ToolString.getIdsChar32());
        return ToolClient.executeRows(dao.execute("sys_department.add",pageFormData));
    }

    /**编辑*/
    @Transactional
    public String edit(final HttpServletRequest request)throws Exception{
        final PageFormData pageFormData = ToolClient.getFormData(request);
        if(!ToolClient.validateField(pageFormData,new String[]{"kid"}))return ToolClient.jsonValidateField();
        final int dep_exist = pageFormData.getString("isParent").equalsIgnoreCase("1") ? 1 : 0;
        pageFormData.put("dep_exist",dep_exist);
        final int rows = dao.execute("sys_department.edit",pageFormData);
        final String parent_id = pageFormData.getString("kid");
        final int total = dao.queryForInteger("sys_department.queryTotalDep",parent_id);/*查询该节点是否hi啊有子节点*/
        if(total > 0){
            dao.execute("sys_department.updateParents",parent_id);/*编辑时如果该节点还有子节点则更新为是父节点*/
        }
        return ToolClient.executeRows(rows);
    }

    /**行删除*/
    @Transactional
    public String delById(final PageFormData pageFormData)throws Exception{
        if(!ToolClient.validateField(pageFormData,new String[]{"kid","parent_id"}))return ToolClient.jsonValidateField();
        final int rows = dao.queryForInteger("sys_department.queryOccupyDep",pageFormData.getString("kid"));
        if(rows > 0){
            return ToolClient.createJson(ConfigFile.code199,"该节点已被使用不能删除!");
        }
        final String parent_id = pageFormData.getString("parent_id");
        final int result = dao.execute("sys_department.delById",pageFormData.getString("kid"));
        final int total = dao.queryForInteger("sys_department.queryTotalDep",parent_id);/*如果删除当前的节点后的父节点没有子节点的话,更新为没有子节点1*/
        if(total <= 0){
            dao.execute("sys_department.editParents",parent_id);/*如果删除当前的节点后的父节点没有子节点的话,更新为没有子节点2*/
        }
        return ToolClient.executeRows(result);
    }

    /**根据id获取全字段数据*/
    public String queryById(final PageFormData pageFormData){
        final String json = ToolClient.validateFields(pageFormData,new String[]{"id"});
        if(!ToolString.isBlank(json))return json;
        try {
            return ToolClient.queryJson(dao.queryForHashMap("sys_department.queryById",pageFormData.getString("id")));
        } catch (Exception e){
            logger.error("DepartmentService的queryById出现异常",e);
            return ToolClient.exceptionJson();
        }
    }

    /**
     * 查询所有的部门机构,用于添加或编辑
     * @param pageFormData
     * @作者 田应平
     * @创建时间 2017年1月21日 上午1:09:20
     * @QQ号码 444141300
     * @官网 http://www.fwtai.com
     */
    public String queryAllDepartment(final PageFormData pageFormData){
        final String parent_id = ToolProperties.getInstance("config/config.properties").getString("default_parent","kid00000000000000000000000001024");
        String id = pageFormData.getString("kid");
        id = ToolString.isBlank(id)?parent_id:id;
        try {
            return ToolClient.queryJson(dao.queryForListMap("sys_department.queryAllDepartment",id));
        } catch (Exception e){
            return ToolClient.exceptionJson();
        }
    }
}