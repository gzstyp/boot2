package com.fwtai.service.module;

import com.fwtai.config.ConfigFile;
import com.fwtai.controller.BaseController;
import com.fwtai.tool.ToolClient;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 面向切面编程,切面处理操作日志
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017年12月23日 13:14:33
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@Aspect
@Component/*把它添加到spring容器里*/
public final class LogAspect {

    @Autowired
    private LogService logService;

    //说明这个包com.fwtai.service.core及子子包下的所有的类的所有的方法,意思植入是在执行方法之前调用
    @Before("execution(* com.fwtai.service.core..*.*(..))")
    public void log(final JoinPoint point){
        final String method = point.getSignature().getName();/*获取方法名*/
        if(method.startsWith("add") || method.startsWith("update") || method.startsWith("edit") || method.startsWith("del")){
            String name = "区域管理模块";
            String info = "添加";
            int handle = 1;//2删除、3编辑、4查询、5批量添加、6批量删除,7批量更新
            if (method.startsWith("update") || method.startsWith("edit")) {
                handle = 3;
                info = "编辑";
            } else if(method.startsWith("delById")){
                handle = 2;
                info = "删除";
            }else if(method.startsWith("delIds")){
                handle = 6;
                info = "批量删除";
            }
            String _class = point.getTarget().getClass().getName();
            _class = _class.substring(_class.lastIndexOf(".")+1);
            if (_class.contains("MenuService")) {
                name = "菜单模块";
            } else if(_class.contains("RoleService")){
                name = "角色模块";
            } else if(_class.contains("UserService")){
                name = "帐号模块";
            }
            final HttpServletRequest request = new BaseController().getRequest();
            final String ip = ToolClient.getIp(request);
            logService.add(ToolClient.loginKey(request,ConfigFile.LOGIN_KEY),name,handle,ip,info);
            final List<Object> objs = Arrays.asList(point.getArgs());
            for(int i = 0; i < objs.size(); i++){
                //System.out.println("请求参数:"+objs.get(i));
            }
        }
    }
}