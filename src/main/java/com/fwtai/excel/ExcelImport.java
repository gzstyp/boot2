package com.fwtai.excel;

import com.fwtai.tool.ToolString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 实现抽象方法并调用方法返回数据,处理15w条数据以上!
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017年11月2日 上午9:47:52
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class ExcelImport extends ExcelHandler{

	private int index;
	private boolean bl = false;
	private HashMap<String,String> mapper;
	private List<String> listTitle = new ArrayList<String>();
	private ArrayList<String> keys = new ArrayList<String>();
	private ArrayList<HashMap<String,String>> listHashMaps = new ArrayList<HashMap<String,String>>();
	
	/**index 指定表头行,标题行必须放在第1行
	 * @用法 final ExcelImport excel = new ExcelImport(1);
	 * @调用 excel.readExcel("C:\\child_3.xlsx");
	 * @获取数据 final ArrayList<HashMap<String,String>> list = excel.getExcelData();
	*/
	public ExcelImport(final int index){
		this.index = index;
		this.bl = false;
	}
	
	/**title 指定表头行,mapper指定表头及数据字段名,标题行必须放在第1行
	 * @用法 final HashMap<String,String> mapper = new HashMap<String,String>();mapper.put("接种部位","jiezhongbuwei");
	 * @调用 excel.readExcel("C:\\child_3.xlsx");
	 * @获取数据 final ArrayList<HashMap<String,String>> list = excel.getExcelData();
	*/
	public ExcelImport(final int index,final HashMap<String,String> mapper){
		this.index = index;
		this.bl = true;
		this.mapper = mapper;
	}
	
	/**实现抽象方法*/
	@Override
	public void optRows(final int countrows,final ArrayList<String> titlelist,final ArrayList<String> rowlist){
		if(countrows == index){//指定表头行
			listTitle = Arrays.asList(titlelist.get(0).split(","));
			if(bl){
				for (int i = 0; i < listTitle.size(); i++){
					for(final String key : mapper.keySet()){
						if(listTitle.get(i).equalsIgnoreCase(key)){
							keys.add(mapper.get(key));
						}
					}
				}
			}
		}else{
			if(bl){
				final HashMap<String,String> map = new HashMap<String,String>();
				if(!ToolString.isBlank(rowlist)){
					final List<String> rows = Arrays.asList(rowlist.get(0).split(","));
					for(int i = 0; i < listTitle.size();i++){
					    map.put(keys.get(i),rows.get(i));
					}
					listHashMaps.add(map);
				}
			}else{
				final HashMap<String,String> map = new HashMap<String,String>();
				if(!ToolString.isBlank(rowlist)){
					final List<String> rows = Arrays.asList(rowlist.get(0).split(","));
					for(int i = 0; i < listTitle.size();i++){
					    map.put(listTitle.get(i),rows.get(i));
					}
					listHashMaps.add(map);
				}
			}
		}
	}
	
	/**获取Excel文件数据*/
	public ArrayList<HashMap<String,String>> getExcelData(){
		return listHashMaps;
	}
}