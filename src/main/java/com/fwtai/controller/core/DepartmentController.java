package com.fwtai.controller.core;

import com.fwtai.controller.BaseController;
import com.fwtai.service.core.DepartmentService;
import com.fwtai.tool.ToolClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 组织机构
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@Controller
@RequestMapping("/sys_department")
public final class DepartmentController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DepartmentService service;

    /**跳转到目标页面*/
    @RequestMapping("/page")
    public final String page(){
        return "core/sys_department";
    }

    /**添加*/
    @RequestMapping("/add")
    @ResponseBody
    public final void add(final HttpServletRequest request,final HttpServletResponse response){
        try {
            ToolClient.responseJson(service.add(request),response);
        } catch (Exception e){
            logger.error("组织机构-添加",e);
            ToolClient.responseException(response);
        }
    }

    /**编辑*/
    @RequestMapping("/edit")
    @ResponseBody
    public final void edit(final HttpServletRequest request,final HttpServletResponse response){
        try {
            ToolClient.responseJson(service.edit(request),response);
        } catch (Exception e){
            logger.error("组织机构-编辑",e);
            ToolClient.responseException(response);
        }
    }

    /**行删除*/
    @RequestMapping("/delById")
    @ResponseBody
    public final void delById(final HttpServletResponse response){
        try {
            ToolClient.responseJson(service.delById(getPageFormData()),response);
        } catch (Exception e){
            logger.error("组织机构-行删除",e);
            ToolClient.responseException(response);
        }
    }

    /**根据id获取全字段数据 */
    @RequestMapping("/queryById")
    @ResponseBody
    public final void queryById(final HttpServletResponse response){
        ToolClient.responseJson(service.queryById(getPageFormData()),response);
    }

    /**查询所有的部门机构,用于添加或编辑菜单*/
    @RequestMapping("/queryAllDepartment")
    @ResponseBody
    public final void queryAllDepartment(final HttpServletResponse response){
        ToolClient.responseJson(service.queryAllDepartment(getPageFormData()),response);
    }
}