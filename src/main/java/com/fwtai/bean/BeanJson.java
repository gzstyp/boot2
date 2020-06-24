package com.fwtai.bean;

import com.fwtai.config.ConfigFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 统一返回给客服端json数据
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2016年12月25日 15:47:13
 * @QQ号码 444141300
 * @主页 http://www.fwtai.com
*/
public final class BeanJson implements Serializable{

	private static final long serialVersionUID = 1L;

	/**提示信息code状态码,默认为199失败*/
	private int code = ConfigFile.code199;
	
	/**数据list集合对象*/
	private List<?> listObject;
	
	/**数据ArrayList集合对象*/
	private ArrayList<?> arrayList;
	
	/**数据list集合对象*/
	private List<PageFormData> listPageFormData;
	
	/**通用的键值对数据结构*/
	private PageFormData pageFormData;
	
	/**数据list集合对象*/
	private List<Serializable> listSerializable;
	
	/**数据list集合对象*/
	private List<Map<String,Object>> list;
	
	/**数据list集合对象*/
	private ArrayList<HashMap<String,Object>> listHashMap;
	
	/**Map对象*/
	private Map<?,?> map;
	
	/**HashMap对象*/
	private HashMap<?,?> hashMap;
	
	/**对象类型,不是键值对数据结构。调用方式是.obj就获取到结果值*/
	private Object obj;
	
	/**总条数总记录数*/
	private Integer total;
	
	/**总页数*/
	private Integer totalPage;
	
	/**提示信息,默认为操作失败*/
	private String msg = ConfigFile.msg199;
	
	/**api调用，页面返回调用方法 */
	private String callback;
	
	public Map<?,?> getMap(){
		return map;
	}
	/**设置Map对象*/
	public void setMap(Map<?, ?> map){
		this.map = map;
	}
	/**对象类型,不是键值对数据结构。调用方式是.obj就获取到结果值*/
	public Object getObj(){
		return obj;
	}
	/**对象类型,不是键值对数据结构。调用方式是.obj就获取到结果值*/
	public void setObj(Object obj){
		this.obj = obj;
	}
	/**获取提示信息*/
	public String getMsg(){
		return msg;
	}
	/**设置返回的提示信息*/
	public void setMsg(final String msg){
		this.msg = msg;
	}
	/**获取调用接口方法名*/
	public String getCallback(){
		return callback;
	}
	/**设置调用接口方法名*/
	public void setCallback(final String callback){
		this.callback = callback;
	}
	public HashMap<?, ?> getHashMap(){
		return hashMap;
	}
	/**设置HashMap对象*/
	public void setHashMap(final HashMap<?, ?> hashMap){
		this.hashMap = hashMap;
	}
	/**数据list集合对象*/
	public List<Map<String,Object>> getList(){
		return list;
	}
	/**设置list数据集合对象*/
	public void setList(final List<Map<String,Object>> list){
		this.list = list;
	}
	/**总条数总记录数*/
	public Integer getTotal(){
		return total;
	}
	/**总条数总记录数*/
	public void setTotal(final Integer total){
		this.total = total;
	}
	/**总页数*/
	public Integer getTotalPage(){
		return totalPage;
	}
	/**设置总页数*/
	public void setTotalPage(final Integer totalPage){
		this.totalPage = totalPage;
	}
	/**提示信息code状态码,默认为199失败*/
	public int getCode(){
		return code;
	}
	/**提示信息code状态码,默认为199失败*/
	public void setCode(final int code){
		this.code = code;
	}
	/**获取list集合对象*/
	public List<PageFormData> getListPageFormData(){
		return listPageFormData;
	}
	/**设置list集合对象*/
	public void setListPageFormData(final List<PageFormData> listPageFormData){
		this.listPageFormData = listPageFormData;
	}
	/**获取Map键值对集合对象*/
	public PageFormData getPageFormData(){
		return pageFormData;
	}
	/**设置Map键值对集合对象*/
	public void setPageFormData(final PageFormData pageFormData){
		this.pageFormData = pageFormData;
	}
	/**获取list集合对象*/
	public List<Serializable> getListSerializable(){
		return listSerializable;
	}
	/**设置list集合对象*/
	public void setListSerializable(final List<Serializable> listSerializable){
		this.listSerializable = listSerializable;
	}
	/**获取list集合对象*/
	public List<?> getListObject(){
		return listObject;
	}
	/**设置list集合对象*/
	public void setListObject(final List<?> listObject){
		this.listObject = listObject;
	}
	/**获取ArrayList集合对象*/
	public ArrayList<HashMap<String, Object>> getListHashMap(){
		return listHashMap;
	}
	/**设置ArrayList集合对象*/
	public void setListHashMap(final ArrayList<HashMap<String, Object>> listHashMap){
		this.listHashMap = listHashMap;
	}
	/**获取ArrayList集合对象*/
	public ArrayList<?> getArrayList(){
		return arrayList;
	}
	/**设置ArrayList集合对象*/
	public void setArrayList(final ArrayList<?> arrayList){
		this.arrayList = arrayList;
	}
}