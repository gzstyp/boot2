package com.fwtai.bean;

import java.util.List;

/**
 * 系统菜单
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-02-21 15:21
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class Menu{

    private String kid;

    private String name;

    private String pid;

    private String url;

    private Integer level;

    private Integer subset;

    private String icon_style;

    private List<Menu> children;

    public String getKid(){
        return kid;
    }

    public void setKid(String kid){
        this.kid = kid;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPid(){
        return pid;
    }

    public void setPid(String pid){
        this.pid = pid;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public Integer getLevel(){
        return level;
    }

    public void setLevel(Integer level){
        this.level = level;
    }

    public Integer getSubset(){
        return subset;
    }

    public void setSubset(Integer subset){
        this.subset = subset;
    }

    public String getIcon_style(){
        return icon_style;
    }

    public void setIcon_style(String icon_style){
        this.icon_style = icon_style;
    }

    public List<Menu> getChildren(){
        return children;
    }

    public void setChildren(List<Menu> children){
        this.children = children;
    }
}