package com.fwtai.tool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * List或Map的工具类
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2015年9月24日 16:25:16
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class ToolListOrMap{

	public final static HashMap<String,Object> getFieldValueMap(final Object bean){
		final HashMap<String,Object> map = new HashMap<String,Object>();
		final Class<?> cls = bean.getClass();
		final Field[] fields = cls.getDeclaredFields();
		for (Field field : fields){
            try {
            	final String fieldType = field.getType().getSimpleName();
            	final String fieldGetName = parGetName(field.getName());
            	final Method fieldGetMet = cls.getMethod(fieldGetName);
            	final Object fieldVal = fieldGetMet.invoke(bean);
                String result = null;
                if ("Date".equals(fieldType)){
                    result = fmtDate((Date) fieldVal);
                } else {
                    if (null != fieldVal) {
                        result = String.valueOf(fieldVal);
                    }
                }
                map.put(field.getName(),result);
            } catch (Exception e){
                continue;
            }
        }
		return map;
	}
	
	/** 
     * 拼接某属性的 get方法 
     * @param fieldName 
     * @return String 
     */ 
	private static String parGetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
	}

	/**
	 * 日期转化为String
	 * @param date
	 * @return date string
	 */
	private static String fmtDate(Date date) {
		if (null == date) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINESE);
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取list里的第0个到第前几Index个的数据(用于已排序的listMap))
	 * @param arrayListHashMap
	 * @param index
	 * @return
	 * @作者 田应平
	 * @返回值类型 ArrayList< HashMap< String,String>>
	 * @创建时间 2015年3月5日 13:27:07 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static ArrayList<HashMap<String, String>> fromZeroToIndex(final ArrayList<HashMap<String, String>> arrayListHashMap, int index){
		ArrayList<HashMap<String, String>> temp = new ArrayList<HashMap<String,String>>();
		if(!ToolString.isBlank(arrayListHashMap)){
			index = index <= 0 ? 1 :index ;
			index = index > arrayListHashMap.size()?arrayListHashMap.size():index;
			for (int i = 0; i < index; i++){
				temp.add(arrayListHashMap.get(i));
			}		
		}
		return temp ;
	}
	
	/**
	 * 对已排序的ListMap获取第index个的数据[当然不排序也适用的]<br/>
	   {欲获取第1个数据，则index=0(是从0下标开始的,0就是第一个);欲获取第2个数据，则index=1；欲获取第3个数据，则index=2}<br />
	   {欲获取倒数第1个数据也就是最后1个,则index=ListMap.size()-1} <br />
	   {欲获取倒数第2个数据也就是最后2个,则index=ListMap.size()-2} <br />
	   {欲获取倒数第3个数据也就是最后3个,则index=ListMap.size()-3} <br />以此类推【注意：temp.size()==temp.size()-1都是获取最后一个】
	 * @param arrayListHashMap
	 * @param index
	 * @return
	 * @作者 田应平
	 * @返回值类型 ArrayList<HashMap<String,String>>
	 * @创建时间 2015年3月5日 18:12:45
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static ArrayList<HashMap<String, String>> getIndex(final ArrayList<HashMap<String, String>> arrayListHashMap, int index){
		final ArrayList<HashMap<String, String>> temp = new ArrayList<HashMap<String,String>>();
		if(!ToolString.isBlank(arrayListHashMap)){
			index = index <= 0 ? 0 :index ;
			index = index >= arrayListHashMap.size()?arrayListHashMap.size()-1:index;
			temp.add(arrayListHashMap.get(index));
		}
		return temp ;
	}
		
	/**
	 * 对数据类型为List_Map_String_Object冒泡排序-升序
	 * @param list
	 * @return
	 * @作者 田应平
	 * @返回值类型 Integer
	 * @创建时间 2015-3-5 上午10:03:59 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	 */
	public final static List<Map<Object, Object>> sortBubbling(final List<Map<Object, Object>> list,String key){
		if(!ToolString.isBlank(key) && !ToolString.isBlank(list)){
			for (int i = 0; i < list.size(); i++){
				Map<Object, Object> tmp0 = list.get(i);
				int number0 = (Integer) tmp0.get(key);
				Map<Object, Object> tmp = null;
				for (int j = i; j < list.size(); j++){
					Map<Object, Object> tmp1 = list.get(j);
					int number1 = (Integer) tmp1.get(key);
					if(number0 > number1){
						tmp = tmp0;
						list.set(i, list.get(j));
						list.set(j, tmp);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 对数据类型为ArrayList_HashMap_String_String排序-升序
	 * @param list
	 * @param key
	 * @return
	 * @作者 田应平
	 * @返回值类型 ArrayList< HashMap< String,String>>
	 * @创建时间 2015-3-5 上午10:11:36 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	 */
	public final static ArrayList<HashMap<String, String>> sortBubbling(final ArrayList<HashMap<String, String>> list,String key){
		if(!ToolString.isBlank(key) && !ToolString.isBlank(list)){
			for (int i = 0; i < list.size(); i++){
				HashMap<String, String> tmp0 = list.get(i);
				int number0 = Integer.parseInt(tmp0.get(key));
				HashMap<String, String> tmp = null;
				for (int j = i; j < list.size(); j++){
					HashMap<String, String> tmp1 = list.get(j);
					int number1 = Integer.parseInt(tmp1.get(key));
					if(number0 > number1){
						tmp = tmp0;
						list.set(i, list.get(j));
						list.set(j, tmp);
					}
				}
			}
		}
		return list;
	}
	
	/**对 HashMap排序*/
	public final static Object[] sortHashMap(final HashMap<String,String> hashmap){
		Object[] key_arr = hashmap.keySet().toArray();     
		Arrays.sort(key_arr);
		return key_arr ;
	}
	
	/**去除重复的元素-保持原来的顺序*/
	public final static List<String> listRemoveRepetition(final List<String> list){
		if(!ToolString.isBlank(list)){
			final Set<String> set = new HashSet<String>();
			final ArrayList<String> newList = new ArrayList<String>();
			for (Iterator<String> iter = list.iterator(); iter.hasNext();){
				String element = iter.next();
				if(set.add(element))
					newList.add(element);
			}
			list.clear();
			list.addAll(newList);
			return list;
		}
		return null;
	}
	
	/**去除重复的元素-保持原来的顺序*/
	public final static ArrayList<String> listRemoveRepetition(final ArrayList<String> list){
		if(!ToolString.isBlank(list)){
			final Set<String> set = new HashSet<String>();
			final ArrayList<String> newList = new ArrayList<String>();
			for (Iterator<String> iter = list.iterator(); iter.hasNext();){
				String element = iter.next();
				if(set.add(element))
					newList.add(element);
			}
			list.clear();
			list.addAll(newList);
			return list;
		}
		return null;
	}
	
	/**去除重复的元素-不保持原来的顺序*/
	public final static List<String> removeRepetition(final List<String> list){
		if(!ToolString.isBlank(list)){
			final HashSet<String> hashSet = new HashSet<String>(list);
			list.clear();
			list.addAll(hashSet);
			return list;
		}
		return null;
	}
	
	/**
	 * 获取表字段列表,SELECT COLUMN_NAME大写,用于Oracle数据库
	 * @param listColumns 从数据库里的list字段名
	 * @param fields 需要移除去除的字段
	 * @作者 田应平
	 * @用法  fieldOracle(columns,new String[]{"KID"});
	 * @创建时间 2017年8月25日 上午11:10:54
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	 */
	public final static ArrayList<String> fieldOracle(final List<HashMap<String,Object>> listColumns,final String fields){
		final Iterator<HashMap<String,Object>> it = listColumns.iterator();
		final ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		final String key = "COLUMN_NAME";
		while(it.hasNext()){
			final HashMap<String,Object> map = it.next();
			final boolean b = ToolString.existKey(fields,map.get(key).toString());
		    if(b){
		        it.remove();
		    }else{
		    	list.add(map);
			}
		}
		final ArrayList<String> listField = new ArrayList<String>();
		for (int i = 0; i < list.size();i++){
			listField.add(list.get(i).get(key).toString());
		}
		return listField;
	}
	
	/**
	 * 获取表字段列表,SELECT column_name小写,用于MySQL数据库
	 * @param listColumns 从数据库里的list字段名
	 * @param fields 需要移除去除的字段
	 * @作者 田应平
	 * @用法  fieldOracle(columns,new String[]{"kid"});
	 * @创建时间 2017年8月25日 上午11:10:54
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	 */
	public final static ArrayList<String> fieldMySQL(final List<HashMap<String,Object>> listColumns,final String fields){
		final Iterator<HashMap<String,Object>> it = listColumns.iterator();
		final ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		final String key = "column_name";
		while(it.hasNext()){
			final HashMap<String,Object> map = it.next();
			final boolean b = ToolString.existKey(fields,map.get(key).toString());
		    if(b){
		        it.remove();
		    }else{
		    	list.add(map);
			}
		}
		final ArrayList<String> listField = new ArrayList<String>();
		for (int i = 0; i < list.size();i++){
			listField.add(list.get(i).get(key).toString());
		}
		return listField;
	}

    /**
     * 判断List集合是所有的元素是否相同,相同true,否则false
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018/6/14 20:28
    */
    public final static boolean checkListIdentical(final ArrayList<String> list) {
        final Set set = new HashSet(list);
        return (set.size()==1) ? true : false;
    }

    /**
     * 处理复检的样本号的值
     * @param list 数据集合
     * @param key 指定样本号的key
     * @param field 指定样本号结果值的key
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018年9月2日 16:17
    */
    public final static HashMap<String,ArrayList<String>> recheck(final List<HashMap<String,String>> list,final String key,final String field){
        final ArrayList<String> arrayList = new ArrayList<String>();
        for(final HashMap<String,String> hm : list){
            arrayList.add(hm.get(key));//获取样本号
        }
        final ArrayList<String> sampled = removeRepetition(arrayList);
        final HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
        for(int i = 0; i < sampled.size();i++){//遍历所有的样本号
            final String value = sampled.get(i);//样本号
            final ArrayList<String> values = new ArrayList<String>();
            for(int y = 0; y < list.size(); y++){
                final HashMap<String,String> hm = list.get(y);
                for(final String k : hm.keySet()){
                    if(hm.get(k).equalsIgnoreCase(value)){
                        values.add(hm.get(field));
                        break;
                    }
                }
            }
            map.put(value,values);
        }
        return map;
    }

    /**
     * list去重
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018年9月2日 17:08
    */
    public final static ArrayList<String> removeRepetition(final ArrayList<String> list){
        final HashSet<String> h = new HashSet<String>(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    //list转换HashMap
    public final static HashMap<String,String> listConvertMap(final List<HashMap<String,String>> list,final String fieldKey,final String fieldValue){
        final HashMap<String,String> map = new HashMap<>();
        for(int i = 0; i < list.size(); i++){
            final HashMap<String,String> temp = list.get(i);
            final String key = temp.get(fieldKey);
            final String value = temp.get(fieldValue);
            map.put(key,value);
        }
        return map;
    }
}