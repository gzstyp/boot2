package com.fwtai.tool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;

/**
 * 读取properties配置文件工具类
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017年1月12日 上午12:16:20
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
 */
public final class ToolProperties {

	private Properties prop;

	private ToolProperties(final String propFilePath){
		prop = new Properties();
		try {
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propFilePath));
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public final String getString(final String key){
		return prop.getProperty(key);
	}

	public final int getInteger(final String key){
		return Integer.parseInt(prop.getProperty(key));
	}

	public final String getString(final String key,final String defaultValue){
		return prop.getProperty(key,defaultValue);
	}

	public final int getInteger(final String key,final String defaultValue){
		return Integer.parseInt(prop.getProperty(key,defaultValue));
	}

	public final boolean getBoolean(final String key){
		return Boolean.parseBoolean(prop.getProperty(key));
	}

	public final static ToolProperties getInstance(final String propFilePath){
		return new ToolProperties(propFilePath);
	}

	/**用法:HashMap<String,String> map = new ToolProperties().getHashMap("properties/excelheader.properties");*/
	public final HashMap<String,String> getHashMap(final String propFilePath){
		final HashMap<String,String> map = new HashMap<String,String>();
		final Properties pro = new Properties();
		try {
			pro.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propFilePath));
		}catch (IOException e){
			e.printStackTrace();
			return map;
		}
		for (final Object key : pro.keySet()){
			try {
				map.put(new String(key.toString().getBytes("ISO8859-1"),"UTF-8"),new String(pro.getProperty(key.toString()).getBytes("ISO8859-1"),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

    public final Object setValue(final String key,final String value){
        return prop.setProperty(key,value);
    }
}