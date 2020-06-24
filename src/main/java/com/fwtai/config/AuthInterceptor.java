package com.fwtai.config;

import com.fwtai.bean.PageFormData;
import com.fwtai.service.core.MenuService;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 自定义拦截器,Spring boot拦截器,其作用是进入到控制器Controller的请求之前进行调用[认证 implements HandlerInterceptor 和菜单权限拦截 extends HandlerInterceptorAdapter应该分开,各执其职]
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017-11-26 13:58
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public class AuthInterceptor implements HandlerInterceptor{

    private final String[] allowUrls = {"/","/login","/user/userLogin","/user/logout","/timeout"};//不拦截的url访问地址,一般包含注册|登录|退出,也可以在拦截器的方法addInterceptors配置

    /**指定提交http请求方式*/
    private final boolean methods(final String method){
        final String[] arr = {"get","post"};
        boolean b = false;
        for(int x = 0; x < arr.length; x++){
            final String array = arr[x];
            if(method.equalsIgnoreCase(array)){
                b = true;
                break;
            }
        }
        return b;
    }

    /**只有返回true才会继续向下执行，返回false取消当前请求*/
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {

        if(handler instanceof ResourceHttpRequestHandler){return true;}//静态资源配置1,否则被拦截
        //获取访问Controller层的方法上的RequestMapping注解
        if(handler instanceof HandlerMethod){//本方法要配合 controller层的 @GetMapping(value = "del",name = "user:del") 的name一起使用,万不得已的情况下使用
            final RequestMapping annotation = ((HandlerMethod)handler).getMethodAnnotation(RequestMapping.class);
            //获取当前请求的name属性
            final String name = annotation.name();//获取的是 @GetMapping(value = "authorization",name = "api-authorization") 的name的值
            final String[] pathArr = annotation.path();//获取的是 value = "del" 或 @GetMapping("del")　或 @GetMapping("/del") 的 del
            final RequestMethod[] methodArr = annotation.method();//获取的是请求方式,如 GET 或 POST,所以可不要
        }

        final String method = request.getMethod();
        if(!methods(method)){
            ToolClient.responseObj(ToolClient.createJson(ConfigFile.code207,ConfigFile.illegality_handle),response);
            return false;
        }
        final boolean login = ToolClient.checkLogin(request);
        final String path = request.getRequestURI().replace(request.getContextPath(), "");//访问路径
        if(!login){
            if(allowUrls != null && allowUrls.length >= 1){
                for (final String url : allowUrls){
                    if(path.equals(url)){
                        return true;
                    }
                }
            }
        }
        final StringBuilder row_click = new StringBuilder();
        final String treeid = request.getParameter("treeid");
        if(!ToolString.isBlank(treeid) && login){
            final PageFormData pageFormData = new PageFormData();
            pageFormData.put("type",3);//type为3时查询的是行按钮权限javascript:;
            pageFormData.put("pId",treeid);
            //使用WebApplicationContext 上下文对象来获取bean
            final BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            final MenuService menu = factory.getBean(MenuService.class);//解决spring boot无法注入bean的问题
            final List<Map<String, Object>> listRows = menu.authRow(request,pageFormData);//获取有行按钮
            if(listRows != null && listRows.size() > 0){
                for(final Map<String, Object> rows : listRows){
                    if(row_click.length() > 0)
                        row_click.append("<span style=\"margin-left:4px;margin-right:4px;display:inline-block;\">|</span>");
                    final Object text = rows.get("name");
                    if(text.toString().indexOf("删除") > -1){
                        row_click.append("<a href=\"javascript:;\" onclick=\"" + rows.get("uri") + "\" style=\"text-decoration:none;color:red;outline:none;\" title=\"" + rows.get("name") +"\">" + rows.get("name") + "</a>");
                    } else {
                        row_click.append("<a href=\"javascript:;\" onclick=\"" + rows.get("uri") + "\" style=\"text-decoration:none;color:#3b8cff;outline:none;\" title=\"" + rows.get("name") +"\">" + rows.get("name") + "</a>");
                    }
                    final Object uri = rows.get("uri");
                    if(uri.toString().indexOf("thisPage.edit(1") > -1){
                        request.setAttribute(ConfigFile.DOUBLE_CLICK,ConfigFile.DOUBLE_CLICK);
                    }
                }
            }
        }
        if(path.matches(ConfigFile.expression) || login){//不需要验证的url地址
            if(ToolString.isBlank(row_click)){
                request.setAttribute(ConfigFile.HANDLE_ROW,null);
                request.setAttribute("row_click",null);//行按钮的数据权限
            }else{
                request.setAttribute(ConfigFile.HANDLE_ROW,ConfigFile.HANDLE_ROW);
                request.setAttribute("row_click",row_click);//行按钮的数据权限
            }
            if(!ToolString.isBlank(treeid)){
                request.setAttribute("authBtnId",treeid);//设置session的父级菜单的id然后页面去查询按钮权限
            }
            return true;
        }else{
            response.sendRedirect(request.getContextPath() + "/"+ConfigFile.TIMEOUT);//说明未登录或登录超时,所以要求去登录url
            return false;
        }
    }
}