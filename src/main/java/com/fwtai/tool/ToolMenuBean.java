package com.fwtai.tool;

import com.fwtai.bean.Menu;

import java.util.List;

/**
 * 通过list树形菜单递归拼接无限级菜单
 * @用法
 * final List<Menu> lists = dao.selectListEntity("sys_core_menu.getBeanMenus");
 * final List<Menu> rootMenu = new ToolMenuEntity().initMenu(lists,"88888888888888888888888888888888");
 * final String rusult = new ToolMenuBean().meunForHtml(rootMenu);
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2018-06-02 10:02
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
 * @参考 <uri>https://blog.csdn.net/oqqMuSe/article/details/79413203</uri>
*/
public final class ToolMenuBean{

    public final String meunForHtml(final List<Menu> list){
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            final Menu menu = list.get(i);
            sb.append("<li class=\"\">");
            final Integer subset = menu.getSubset();
            if(subset == 1){
                sb.append("<a href=\"javascript:;\" class=\"dropdown-toggle\">");
            }else{
                sb.append("<a data-url=\""+ menu.getUrl()+"\" href=\"#"+ menu.getUrl()+"\" >");
            }
            sb.append("<i class=\""+menu.getIcon_style()+"\"></i>");
            sb.append("<span class=\"menu-text\">"+ menu.getName()+"</span>");
            final int size = menu.getChildren().size();
            if(size > 0){
                sb.append("<b class=\"arrow fa fa-angle-down\"></b>");
            }
            sb.append("</a>");
            sb.append("<b class=\"arrow\"></b>");
            if(size > 0){
                sb.append(MenuForSonHtml(menu.getChildren(),menu.getLevel()));
            }
            sb.append("</li>");
        }
        return sb.toString();
    }

    private final String MenuForSonHtml(final List<Menu> list,final int level){
        final StringBuilder sb = new StringBuilder();
        sb.append("<ul class=\"submenu\">");
        for (int i = 0; i < list.size(); i++){
            final Menu menu = list.get(i);
            sb.append("<li class=\"\">");
            final Integer subset = menu.getSubset();
            if(subset == 1){
                sb.append("<a href=\"javascript:;\" class=\"dropdown-toggle\">");
            }else{
                sb.append("<a data-url=\""+ menu.getUrl()+"\" href=\"#" + menu.getUrl() + "\" >");
            }
            if(level == 1){//有且只有第一级菜单有‘<’图标
                sb.append("<i class=\"menu-icon fa fa-caret-right\"></i>");
            }
            sb.append(menu.getName());
            final int size = menu.getChildren().size();
            if(size > 0){
                sb.append("<b class=\"arrow fa fa-angle-down\"></b>");
            }
            sb.append("</a>");
            sb.append("<b class=\"arrow\"></b>");
            if(size > 0){
                sb.append(MenuForSonHtml(menu.getChildren(),menu.getLevel()));
            }
            sb.append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
}