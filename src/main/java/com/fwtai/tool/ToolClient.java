package com.fwtai.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fwtai.bean.BeanJson;
import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端请求|服务器端响应工具类
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017年1月11日 19:20:50
 * @QQ号码 444141300
 * @主页 http://www.fwtai.com
 */
public final class ToolClient implements Serializable{

	private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(ToolClient.class);

	/**
	 * 生成简单类型json字符串,仅用于查询返回,客户端只需判断code是否为200才操作,仅用于查询操作,除了list集合之外都可以用data.map获取数据,list的是data.listData,字符串或数字对应是obj
	 * @作者 田应平
	 * @注意 如果传递的是List则在客户端解析listData的key值,即data.listData;是map或HashMap或PageFormData解析map的key值,即data.map;否则解析obj的key值即data.obj或data.map
	 * @用法 解析后判断data.code == AppKey.code.code200 即可
	 * @返回值类型 返回的是json字符串,内部采用JSONObject封装,不可用于redis缓存value
	 * @创建时间 2017年1月11日 上午10:27:53
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String queryJson(final Object object){
        if(object == null || object.toString().trim().length() <= 0){
            return queryEmpty();
        }
        final JSONObject json = new JSONObject();
        if (object instanceof Exception) {
            json.put(ConfigFile.code,ConfigFile.code204);
            json.put(ConfigFile.msg,ConfigFile.msg204);
            json.put(ConfigFile.obj,object);
            logger.error("queryJson出现错误,{}",object);
            return json.toJSONString();
        }
        if(object instanceof Map<?,?>){
            final Map<?,?> map = (Map<?,?>) object;
            if(map == null || map.size() <= 0){
                queryEmpty();
            }else {
                json.put(ConfigFile.code,ConfigFile.code200);
                json.put(ConfigFile.msg,ConfigFile.msg200);
                json.put(ConfigFile.map,object);
                return json.toJSONString();
            }
        }
        if(object instanceof List<?>){
            final List<?> list = (List<?>) object;
            if(list == null || list.size() <= 0){
                return queryEmpty();
            }else {
                if (ToolString.isBlank(list.get(0))){
                    return queryEmpty();
                }else {
                    json.put(ConfigFile.code,ConfigFile.code200);
                    json.put(ConfigFile.msg,ConfigFile.msg200);
                    json.put(ConfigFile.listData,object);
                    final String jsonObj = json.toJSONString();
                    final JSONObject j = JSONObject.parseObject(jsonObj);
                    final String listData = j.getString(ConfigFile.listData);
                    if (listData.equals("[{}]")){
                        return queryEmpty();
                    }
                    return jsonObj;
                }
            }
        }
        if(object instanceof PageFormData){
            final PageFormData pageFormData = (PageFormData)object;
            if(pageFormData == null || pageFormData.size() <= 0){
                return queryEmpty();
            }else {
                json.put(ConfigFile.code,ConfigFile.code200);
                json.put(ConfigFile.msg,ConfigFile.msg200);
                json.put(ConfigFile.map,object);
                return json.toJSONString();
            }
        }
        if(String.valueOf(object).toLowerCase().equals("null") || String.valueOf(object).replaceAll("\\s*", "").length() == 0){
            return queryEmpty();
        }else {
            json.put(ConfigFile.code,ConfigFile.code200);
            json.put(ConfigFile.msg,ConfigFile.msg200);
            json.put(ConfigFile.obj,object);
            final String jsonObj = json.toJSONString();
            final JSONObject j = JSONObject.parseObject(jsonObj);
            final String obj = j.getString(ConfigFile.obj);
            if (obj.equals("{}")){
                return queryEmpty();
            }
            return jsonObj;
        }
	}

	/***
	 * 生成 BeanJson对象,仅用于查询数据,适用于redis缓存的value;一般在service层调用,如果不用于缓存redis不推荐使用本方法
	 * @作者 田应平
     * @deprecated：不推荐使用的方法
	 * @返回值类型 BeanJson,内部采用BeanJson封装处理,适用于redis缓存的value;
	 * @创建时间 2017年2月22日 下午9:45:06
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	protected final static BeanJson queryBeanJson(final Object objBeanJson,final String redisKey){
		final BeanJson json = new BeanJson();
		if(ToolString.isBlank(objBeanJson)){
			return emptyBeanJson();
		}
		if(objBeanJson instanceof ArrayList<?>){
			final ArrayList<?> list = (ArrayList<?>)objBeanJson;
			if(list == null || list.size() <= 0){
				return emptyBeanJson();
			}else {
				json.setCode(ConfigFile.code200);
				json.setMsg(ConfigFile.msg200);
				json.setArrayList(list);
				return json;
			}
		}else if(objBeanJson instanceof List<?>){
			final List<?> list = (List<?>)objBeanJson;
			if(list == null || list.size() <= 0){
				return emptyBeanJson();
			}else {
				json.setCode(ConfigFile.code200);
				json.setMsg(ConfigFile.msg200);
				json.setListObject(list);
				return json;
			}
		}else if(objBeanJson instanceof HashMap<?,?>){
			final HashMap<?,?> hashMap = (HashMap<?,?>)objBeanJson;
			if(hashMap == null || hashMap.size() <= 0){
				return emptyBeanJson();
			}else{
				json.setCode(ConfigFile.code200);
				json.setMsg(ConfigFile.msg200);
				json.setHashMap(hashMap);
				return json;
			}
		}else if(objBeanJson instanceof Map<?,?>){
			final Map<?,?> map = (Map<?,?>)objBeanJson;
			if(map == null || map.size() <= 0){
				return emptyBeanJson();
			}else{
				json.setCode(ConfigFile.code200);
				json.setMsg(ConfigFile.msg200);
				json.setMap(map);
				return json;
			}
		}else if(objBeanJson instanceof PageFormData){
			final PageFormData pageFormData = (PageFormData)objBeanJson;
			if(pageFormData == null || pageFormData.size() <= 0){
				return emptyBeanJson();
			}else {
				json.setCode(ConfigFile.code200);
				json.setMsg(ConfigFile.msg200);
				json.setPageFormData(pageFormData);
				return json;
			}
		}else{
			json.setCode(ConfigFile.code200);
			json.setMsg(ConfigFile.msg200);
			json.setObj(objBeanJson);
			return json;
		}
	}

	/**
	 * 查询时得到的数据为空返回的json字符串
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2017年1月11日 下午9:40:21
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	private static final String queryEmpty(){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,ConfigFile.code201);
		json.put(ConfigFile.msg,ConfigFile.msg201);
		return json.toJSONString();
	}

	/**
	 * 查询时得到的数据为空返回的BeanJson对象
	 * @作者 田应平
	 * @返回值类型 BeanJson
	 * @创建时间 2017年1月11日 下午9:40:21
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	private static final BeanJson emptyBeanJson(){
		final BeanJson beanJson = new BeanJson();
		beanJson.setCode(ConfigFile.code201);
		beanJson.setMsg(ConfigFile.msg201);
		return beanJson;
	}

	/**
	 * 生成json字符串对象,直接采用JSONObject封装,执行效率会更高;适用于为增、删、改操作时,判断当前的rows是否大于0来确定是否操作成功,一般在service调用,偷懒的人可以使用本方法
	 * @param rows 执行后受影响的行数
	 * @用法 解析后判断data.code == AppKey.code.code200即可操作
	 * @作者 田应平
	 * @创建时间 2016年12月25日 下午5:44:23
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static final String executeRows(final int rows){
		final JSONObject json = new JSONObject();
		if(rows > 0){
			json.put(ConfigFile.code,ConfigFile.code200);
			json.put(ConfigFile.msg,ConfigFile.msg200);
			json.put(ConfigFile.obj,rows);
			return json.toJSONString();
		}else{
			json.put(ConfigFile.code,ConfigFile.code199);
			json.put(ConfigFile.msg,ConfigFile.msg199);
			json.put(ConfigFile.obj,rows);
			return json.toJSONString();
		}
	}

    /**
     * 操作成功生成json字符串对象,失败信息是ConfigFile.msg199,直接采用JSONObject封装,执行效率会更高;适用于为增、删、改操作,一般在service调用
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/1/19 11:31
    */
    public static final String executeRows(final int rows,final String success){
        final JSONObject json = new JSONObject();
        if(rows > 0){
            json.put(ConfigFile.code,ConfigFile.code200);
            json.put(ConfigFile.msg,success);
            json.put(ConfigFile.obj,rows);
            return json.toJSONString();
        }else{
            json.put(ConfigFile.code,ConfigFile.code199);
            json.put(ConfigFile.msg,ConfigFile.msg199);
            json.put(ConfigFile.obj,rows);
            return json.toJSONString();
        }
    }

	/**
	 * 生成自定义的json对象,直接采用JSONObject封装,执行效率会更高;适用于为增、删、改操作,一般在service调用
	 * @param rows 执行后受影响的行数
	 * @param success 执行成功的提示消息
	 * @param failure 执行失败的提示消息
	 * @作者 田应平
	 * @创建时间 2016年12月25日 下午5:50:22
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static final String executeRows(final int rows,final String success,final String failure){
		final JSONObject json = new JSONObject();
		if(rows > 0){
			json.put(ConfigFile.code,ConfigFile.code200);
			json.put(ConfigFile.msg,success);
			json.put(ConfigFile.obj,rows);
			return json.toJSONString();
		}else{
			json.put(ConfigFile.code,ConfigFile.code199);
			json.put(ConfigFile.msg,failure);
			json.put(ConfigFile.obj,rows);
			return json.toJSONString();
		}
	}

	/**
	 * 生成json字符串对象,直接采用JSONObject封装,执行效率会更高;适用于为增、删、改、查操作时,判断当前的code固定值生成json字符串,一般在service调用,偷懒的人可以使用本方法
	 * @作者 田应平
	 * @返回值类型 采用JSONObject封装的json字符串
	 * @注意 code的可选值为:<br/>
	 * ConfigFile.code199[操作失败]<br/>
	 * ConfigFile.code200[操作成功]<br/>
	 * ConfigFile.code201[暂无数据]<br/>
	 * ConfigFile.code202[请求参数不完整]<br/>
	 * ConfigFile.code203[密钥验证失败]<br/>
	 * ConfigFile.code204[系统异常]<br/>
	 * ConfigFile.code205[未登录或登录超时]<br/>
	 * ConfigFile.code206[账号或密码不正确]<br/>
	 * ConfigFile.code207[非法操作!或你的账号已被删除|你的账号已被禁用]
	 * @创建时间 2017年3月14日 16:08:21
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	 */
	public static final String jsonCode(final int code){
		final JSONObject json = new JSONObject();
		switch (code){
			case ConfigFile.code199:
				json.put(ConfigFile.code,code);
				json.put(ConfigFile.msg,ConfigFile.msg199);
				break;
			case ConfigFile.code200:
				json.put(ConfigFile.code,code);
				json.put(ConfigFile.msg,ConfigFile.msg200);
				break;
			case ConfigFile.code201:
				json.put(ConfigFile.code,code);
				json.put(ConfigFile.msg,ConfigFile.msg201);
				break;
			case ConfigFile.code202:
				json.put(ConfigFile.code,code);
				json.put(ConfigFile.msg,ConfigFile.msg202);
				break;
			case ConfigFile.code203:
				json.put(ConfigFile.code,code);
				json.put(ConfigFile.msg,ConfigFile.msg203);
				break;
			case ConfigFile.code204:
				json.put(ConfigFile.code,code);
				json.put(ConfigFile.msg,ConfigFile.msg204);
				break;
			case ConfigFile.code205:
				json.put(ConfigFile.code,code);
				json.put(ConfigFile.msg,ConfigFile.msg204);
				break;
			case ConfigFile.code206:
				json.put(ConfigFile.code,code);
				json.put(ConfigFile.msg,ConfigFile.msg206);
				break;
			case ConfigFile.code207:
				json.put(ConfigFile.code,code);
				json.put(ConfigFile.msg,ConfigFile.msg207);
				break;
			default:
				json.put(ConfigFile.code,code);
				json.put(ConfigFile.msg,ConfigFile.msg199);
				break;
		}
		return json.toJSONString();
	}

	/**
	 * 生成json格式字符串,code和msg的key是固定的,直接采用JSONObject封装,执行效率会更高,用于增、删、改、查操作,一般在service层调用
	 * @作者 田应平
	 * @返回值类型 返回的是json字符串,内部采用JSONObject封装
	 * @用法 解析后判断data.code == AppKey.code.code200即可处理操作
	 * @创建时间 2016年12月25日 18:11:16
	 * @QQ号码 444141300
	 * @param code 相关参数协议
	 * @主页 http://www.fwtai.com
	*/
	public static final String createJson(final int code,final String msg){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,code);
		json.put(ConfigFile.msg,msg);
		return json.toJSONString();
	}

    /**
     * 生成json格式字符串,直接采用JSONObject封装,执行效率会更高,用于增、删、改、查操作,一般在service层调用
     * @作者 田应平
     * @返回值类型 返回的是json字符串,内部采用JSONObject封装
     * @用法 解析后判断data.code == AppKey.code.code200即可处理操作
     * @创建时间 2018年7月3日 09:20:05
     * @QQ号码 444141300
     * @param code 相关参数协议
     * @主页 http://www.fwtai.com
    */
    public static final String createJson(final String code,final String msg){
        final JSONObject json = new JSONObject();
        json.put(ConfigFile.code,code);
        json.put(ConfigFile.msg,msg);
        return json.toJSONString();
    }

    /**
     * 生成json格式字符串,直接采用JSONObject封装,执行效率会更高,用于增、删、改、查操作,一般在service层调用
     * @作者 田应平
     * @返回值类型 返回的是json字符串,内部采用JSONObject封装
     * @创建时间 2018年7月3日 09:20:17
     * @QQ号码 444141300
     * @param hashMap 相关参数协议
     * @主页 http://www.fwtai.com
    */
    public static final String createJson(final HashMap<String,Object> hashMap){
        return JSONObject.toJSONString(hashMap);
    }

    /**
     * 生成json格式字符串,直接采用JSONObject封装,执行效率会更高,用于增、删、改、查操作,一般在service层调用
     * @作者 田应平
     * @返回值类型 返回的是json字符串,内部采用JSONObject封装
     * @创建时间 2018年7月3日 09:20:31
     * @QQ号码 444141300
     * @param map 相关参数协议
     * @主页 http://www.fwtai.com
    */
    public static final String createJson(final Map<String,Object> map){
        return JSONObject.toJSONString(map);
    }

    /**
     * 生成json数组
     * @param listData
     * @return
     * @作者 田应平
     * @返回值类型 String
     * @创建时间 2017年1月12日 下午9:28:55
     * @QQ号码 444141300
     * @官网 http://www.fwtai.com
    */
    public final static String createJson(final List<Map<String, Object>> listData){
        return JSONArray.toJSONString(listData);
    }

    /**
     * 生成code为199的json格式数据且msg是提示信息
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/7/29 15:00
    */
    public final static String createJsonFail(final String msg){
        final JSONObject json = new JSONObject();
        json.put(ConfigFile.code,ConfigFile.code199);
        json.put(ConfigFile.msg,msg);
        return json.toJSONString();
    }

    /**
     * 生成code为200的json格式数据且msg是提示信息
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/7/29 15:00
    */
    public final static String createJsonSuccess(final String msg){
        final JSONObject json = new JSONObject();
        json.put(ConfigFile.code,ConfigFile.code200);
        json.put(ConfigFile.msg,msg);
        return json.toJSONString();
    }

	/**
	 * 验证密钥key的返回json格式专用,先调用方法validateKey(pageFormData)返回值false后再直接调用本方法返回json字符串
	 * @作者 田应平
	 * @返回值类型 返回的是json字符串,内部采用JSONObject封装
	 * @创建时间 2017年1月11日 下午7:38:48
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	private static final String jsonValidateKey(){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,ConfigFile.code203);
		json.put(ConfigFile.msg,ConfigFile.msg203);
		return json.toJSONString();
	}

	/**
	 * 验证必要的参数字段是否为空的返回json格式专用,先调用方法validateField()返回值false后再直接调用本方法返回json字符串
	 * @作者 田应平
	 * @返回值类型 返回的是json字符串,内部采用JSONObject封装
	 * @创建时间 2017年1月11日 下午7:38:48
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static final String jsonValidateField(){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,ConfigFile.code202);
		json.put(ConfigFile.msg,ConfigFile.msg202);
		return json.toJSONString();
	}

	/**
	 * 验证必要的字段是否为空,不验证ckey密钥,一般在service层调用,如果返回为 null 则验证成功,否则失败;适用于增、删、改、查操作!
	 * @fields 需要验证的form字段
     * @用法 final String validate = ToolClient.validateFields(pageFormData,new String[]{"id"});if(!ToolString.isBlank(json))return validate;
	 * @作者 田应平
	 * @返回值类型 返回的是json字符串,内部采用JSONObject封装,如果返回为 null 则验证成功!
	 * @创建时间 2017年2月23日 下午10:10:34
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String validateFields(final PageFormData pageFormData,final String[] fields){
		if(ToolString.isBlank(pageFormData) || ToolString.isBlank(fields)){
			return jsonValidateField();
		}
        boolean flag = false;
        for (final String p : fields){
            if(ToolString.isBlank(pageFormData.get(p))){
                flag = true;
                break;
            }
        }
        if(flag)return jsonValidateField();
		return null;
	}

	/**
	 * 验证传入的值是否有值
	 * @param 
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2020/5/22 11:30
	*/
    public final static String validateField(final String... fields){
        if(fields == null || fields.length <= 0){
            return jsonValidateField();
        }
        boolean flag = false;
        for (final String p : fields){
            if(p == null || p.length() <= 0){
                flag = true;
                break;
            }
        }
        if(flag)return jsonValidateField();
        return null;
    }

    /**
     * 验证必要的字段是否为空,不验证ckey密钥,一般在service层调用,如果返回为 null 则验证成功,否则失败;适用于增、删、改、查操作!
     * @param formData,fields
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/10/31 13:54
    */
    public final static String validateForm(final HashMap<String,String> formData,final String[] fields){
        if(ToolString.isBlank(formData) || ToolString.isBlank(fields)){
            return jsonValidateField();
        }
        boolean flag = false;
        for(final String p : fields){
            if(ToolString.isBlank(formData.get(p))){
                flag = true;
                break;
            }
        }
        if(flag)return jsonValidateField();
        return null;
    }

	/**
	 * 验证必要的字段是否为空,含验证ckey密钥,一般在service层调用,如果返回为 null 则验证成功,否则失败;适用于增、删、改、查操作!
	 * @作者 田应平
	 * @fields 需要验证的form字段
	 * @返回值类型 如果返回为 null 则验证成功,否则失败;
	 * @创建时间 2017年2月23日 下午9:54:25
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String validateKeyFields(final PageFormData pageFormData,final String[] fields){
		if(ToolString.isBlank(pageFormData) || ToolString.isBlank(fields))return jsonValidateField();
		if(!validateKey(pageFormData))return jsonValidateKey();
		boolean flag = false;
		for(final String p : fields){
			if(ToolString.isBlank(pageFormData.get(p))){
				flag = true;
				break;
			}
		}
		if(flag)return jsonValidateField();
		return null;
	}

	/**
	 * 验证密钥key是否成功,验证成功返回true,否则返回false
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2017年1月11日 下午7:59:15
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	private final static boolean validateKey(final PageFormData pageFormData){
		if(ToolString.isBlank(pageFormData))return false;
		return validateEncrypt(pageFormData.getString(ConfigFile.ckey));
	}

	/**
	 * 验证必要的参数字段是否不为空,不为空返回true,否则返回false,如果需要验证key值的话,先调用validateKey(pageFormData)验证方法返回true再调用本方法!
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @注意 如果需要验证key值的话,先调用validateKey(pageFormData)验证方法返回true再调用本方法!
	 * @创建时间 2017年1月11日 下午8:06:39
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static boolean validateField(final PageFormData pageFormData,final String[] fields){
		if(ToolString.isBlank(pageFormData) || ToolString.isBlank(fields)){
			return false;
		}else{
			boolean flag = true;
			for (final String p : fields){
				if(ToolString.isBlank(pageFormData.get(p))){
					flag = false;
					break;
				}
			}
			return flag;
		}
	}

	/**
	 * 生成json字符串对象,一般在service查询时调用
	 * @作者 田应平
	 * @注意 code和msg是必填
	 * @返回值类型 String,采用JSONObject封装
	 * @创建时间 2016年11月22日 上午9:35:51
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String createJsonObject(final BeanJson bean){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,bean.getCode());
		json.put(ConfigFile.msg,bean.getMsg());
		if(!ToolString.isBlank(bean.getTotal()))json.put(ConfigFile.total,bean.getTotal());
		if(!ToolString.isBlank(bean.getTotalPage()))json.put(ConfigFile.totalPage,bean.getTotalPage());
		if(!ToolString.isBlank(bean.getCallback()))json.put(ConfigFile.callback,bean.getCallback());
		if(!ToolString.isBlank(bean.getMap()))json.put(ConfigFile.map,bean.getMap());
		if(!ToolString.isBlank(bean.getHashMap()))json.put(ConfigFile.map,bean.getHashMap());
		if(!ToolString.isBlank(bean.getPageFormData()))json.put(ConfigFile.map,bean.getPageFormData());
		if(!ToolString.isBlank(bean.getObj()))json.put(ConfigFile.obj,bean.getObj());
		if(!ToolString.isBlank(bean.getListSerializable()))json.put(ConfigFile.listData,bean.getListSerializable());
		if(!ToolString.isBlank(bean.getListObject()))json.put(ConfigFile.listData,bean.getListObject());
		if(!ToolString.isBlank(bean.getArrayList()))json.put(ConfigFile.listData,bean.getArrayList());
		if(!ToolString.isBlank(bean.getList()))json.put(ConfigFile.listData,bean.getList());
		if(!ToolString.isBlank(bean.getListHashMap()))json.put(ConfigFile.listData,bean.getListHashMap());
		if(!ToolString.isBlank(bean.getListPageFormData()))json.put(ConfigFile.listData,bean.getListPageFormData());
		return json.toJSONString();
	}

	/**
	 * 生成|计算总页数
	 * @作者 田应平
	 * @返回值类型 Integer
	 * @创建时间 2016年12月2日 下午1:20:53
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static Integer totalPage(final Integer total,final Integer pageSize){
		return (total%pageSize) == 0 ? (total/pageSize):(total/pageSize)+1; //总页数
	}

	/**
	 * 生成带分页返回json对象的ajax无刷新分页技术-用于一般分页技术
	 * @作者 田应平
	 * @param listData list集合数据
	 * @param pageSize 每页大小,注意每页大小不能小于等于0
	 * @param currentPage 当前页,注意当前页不能小于1
	 * @param total 总记录数|总条数
	 * @返回值类型 JSONObject 字符串
	 * @创建时间 2016年8月29日 17:21:17
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String jsonPaging(final Object listData,final Object total,final Integer pageSize,final Integer currentPage){
		final JSONObject json = new JSONObject();
		if(currentPage < 1 || pageSize <= 0)return createJson(ConfigFile.code202,ConfigFile.paging_error_msg001);
		if(ToolString.isBlank(listData)){
			json.put(ConfigFile.code,ConfigFile.code201);
			json.put(ConfigFile.msg,ConfigFile.msg201);
			return json.toJSONString();
		}else{
			try {
				final Integer record = Integer.parseInt(String.valueOf(total));
				final Integer totalPage = (record%pageSize) == 0 ? (record/pageSize):(record/pageSize)+1; //总页数
				json.put(ConfigFile.total,record);
				json.put(ConfigFile.totalPage,totalPage);
			} catch (Exception e){
				logger.error("ToolClient.jsonPaging出异常",e);
				return createJson(ConfigFile.code204,ConfigFile.paging_error_msg002);
			}
			json.put(ConfigFile.pageSize,pageSize);
			json.put(ConfigFile.current,currentPage);
			json.put(ConfigFile.code,ConfigFile.code200);
			json.put(ConfigFile.msg,ConfigFile.msg200);
			json.put(ConfigFile.listData, listData);
			return json.toJSONString();
		}
	}

	/**
	 * 生成带分页返回json对象的ajax无刷新分页技术-用于一般分页技术
	 * @作者 田应平
	 * @param listData list集合数据
	 * @param pageSize 每页大小,注意每页大小不能小于等于0
	 * @param currentPage 当前页,注意当前页不能小于1
	 * @param total 总记录数|总条数
	 * @返回值类型 JSONObject 字符串
	 * @创建时间 2016年8月29日 17:21:17
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String jsonPaging(final Object listData,final Integer total,final Integer pageSize,final Integer currentPage){
		final JSONObject json = new JSONObject();
		if(currentPage < 1 || pageSize <= 0)return createJson(ConfigFile.code202,ConfigFile.paging_error_msg001);
		if(ToolString.isBlank(listData)){
			json.put(ConfigFile.code,ConfigFile.code201);
			json.put(ConfigFile.msg,ConfigFile.msg201);
			return json.toJSONString();
		}else {
			json.put(ConfigFile.pageSize,pageSize);
			json.put(ConfigFile.total,total);
			json.put(ConfigFile.current,currentPage);
			final Integer totalPage = (total%pageSize) == 0 ? (total/pageSize):(total/pageSize)+1; //总页数
			json.put(ConfigFile.totalPage, totalPage);
			json.put(ConfigFile.code,ConfigFile.code200);
			json.put(ConfigFile.msg,ConfigFile.msg200);
			json.put(ConfigFile.listData,listData);
			return json.toJSONString();
		}
	}

	/**
	 * 生成带分页返回json对象的ajax无刷新分页技术-用于一般分页技术
	 * @作者 田应平
	 * @param listData list集合数据
	 * @param pageSize 每页大小,注意每页大小不能小于等于0
	 * @param currentPage 当前页,注意当前页不能小于1
	 * @param total 总记录数|总条数
	 * @返回值类型 JSONObject 字符串
	 * @创建时间 2016年8月29日 17:21:17
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String jsonPaging(final List<Map<String, Object>> listData,final Integer total,final Integer pageSize,final Integer currentPage){
		final JSONObject json = new JSONObject();
		if(currentPage < 1 || pageSize <= 0)return createJson(ConfigFile.code202,ConfigFile.paging_error_msg001);
		if(ToolString.isBlank(listData)){
			json.put(ConfigFile.code,ConfigFile.code201);
			json.put(ConfigFile.msg,ConfigFile.msg201);
			return json.toJSONString();
		}else {
			json.put(ConfigFile.pageSize,pageSize);
			json.put(ConfigFile.total,total);
			json.put(ConfigFile.current, currentPage);
			final Integer totalPage = (total%pageSize) == 0 ? (total/pageSize):(total/pageSize)+1; //总页数
			json.put(ConfigFile.totalPage, totalPage);
			json.put(ConfigFile.code,ConfigFile.code200);
			json.put(ConfigFile.msg,ConfigFile.msg200);
			json.put(ConfigFile.listData, listData);
			return json.toJSONString();
		}
	}

	/**
	 * 生成带分页返回json对象的ajax无刷新分页技术-用于一般分页技术
	 * @作者 田应平
	 * @param listData list集合数据
	 * @param pageSize 每页大小,注意每页大小不能小于等于0
	 * @param currentPage 当前页,注意当前页不能小于1
	 * @param total 总记录数|总条数
	 * @返回值类型 JSONObject 字符串
	 * @创建时间 2017年1月10日 21:21:21
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String jsonPaging(final ArrayList<HashMap<String, Object>> listData,final Integer total,final Integer pageSize,final Integer currentPage){
		final JSONObject json = new JSONObject();
		if(currentPage < 1 || pageSize <= 0)return createJson(ConfigFile.code202,ConfigFile.paging_error_msg001);
		if(ToolString.isBlank(listData)){
			json.put(ConfigFile.code,ConfigFile.code201);
			json.put(ConfigFile.msg,ConfigFile.msg201);
			return json.toJSONString();
		}else {
			json.put(ConfigFile.pageSize,pageSize);
			json.put(ConfigFile.total,total);
			json.put(ConfigFile.current,currentPage);
			final Integer totalPage = (total%pageSize) == 0 ? (total/pageSize):(total/pageSize)+1; //总页数
			json.put(ConfigFile.totalPage, totalPage);
			json.put(ConfigFile.code,ConfigFile.code200);
			json.put(ConfigFile.msg,ConfigFile.msg200);
			json.put(ConfigFile.listData, listData);
			return json.toJSONString();
		}
	}

	/**
	 * 生成带分页返回json对象的ajax无刷新分页技术-用于一般分页技术
	 * @param listData list集合数据
	 * @param pageSize 每页大小,注意每页大小不能小于等于0
	 * @param currentPage 当前页,注意当前页不能小于1
	 * @param total 总记录数|总条数
	 * @return JSONObject 字符串
	 * @作者 田应平
	 * @创建时间 2016年12月29日 下午10:21:01
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String jsonPagingPageFormData(final List<PageFormData> listData,final Integer total,final Integer pageSize,final Integer currentPage){
		final JSONObject json = new JSONObject();
		if(currentPage < 1 || pageSize <= 0)return createJson(ConfigFile.code202,ConfigFile.paging_error_msg001);
		if(ToolString.isBlank(listData)){
			json.put(ConfigFile.code,ConfigFile.code201);
			json.put(ConfigFile.msg,ConfigFile.msg201);
			return json.toJSONString();
		}else {
			json.put(ConfigFile.pageSize,pageSize);
			json.put(ConfigFile.total, total);
			json.put(ConfigFile.current, currentPage);
			final Integer totalPage = (total%pageSize) == 0 ? (total/pageSize):(total/pageSize)+1; //总页数
			json.put(ConfigFile.totalPage, totalPage);
			json.put(ConfigFile.code,ConfigFile.code200);
			json.put(ConfigFile.msg,ConfigFile.msg200);
			json.put(ConfigFile.listData, listData);
			return json.toJSONString();
		}
	}

	/**
	 * 用于Easyui里的datagrid数据表格
	 * @param total 总条数|总记录数
	 * @param listData 数据库返回的list集合数据
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2017年1月10日 下午8:47:50
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String jsonDatagrid(Object listData,Object total){
		if(ToolString.isBlank(listData)){
			listData = new ArrayList<HashMap<String,Object>>();
			total = 0 ;
		}
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.total,total);
		json.put(ConfigFile.rows,listData);
		return json.toJSONString();
	}

	/**
	 * 用于Easyui里的datagrid数据表格
	 * @param total 总条数|总记录数
	 * @param listData 数据库返回的list集合数据
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2017年1月10日 下午8:47:50
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String jsonDatagrid(ArrayList<HashMap<String,Object>> listData,Integer total){
		if(ToolString.isBlank(listData)){
			listData = new ArrayList<HashMap<String,Object>>();
			total = 0 ;
		}
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.total,total);
		json.put(ConfigFile.rows,listData);
		return json.toJSONString();
	}

	/**
	 * 用于Easyui里的datagrid数据表格
	 * @param total 总条数|总记录数
	 * @param listData 数据库返回的list集合数据
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2017年1月10日 21:20:36
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String jsonDatagrid(List<Map<String,Object>> listData,Integer total){
		if(ToolString.isBlank(listData)){
			listData = new ArrayList<Map<String,Object>>();
			total = 0;
		}
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.total,total);
		json.put(ConfigFile.rows,listData);
		return json.toJSONString();
	}

	/**
	 * 用于生成出现异常信息时的json固定code:204字符串提示,返回给controller层调用,一般在service层调用
	 * @作者 田应平
	 * @返回值类型 String,内部采用JSONObject封装,msg 为系统统一的‘系统出现错误’
	 * @创建时间 2017年1月10日 21:40:23
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String exceptionJson(){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,ConfigFile.code204);
		json.put(ConfigFile.msg,ConfigFile.msg204);
		return json.toJSONString();
	}

	/**
	 * 用于生成出现异常信息时的json固定code:204字符串提示,返回给controller层调用,一般在service层调用
	 * @param msg 自定义提示的异常信息
	 * @作者 田应平
	 * @返回值类型 String,内部采用JSONObject封装
	 * @创建时间 2017年1月10日 21:40:23
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String exceptionJson(final String msg){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,ConfigFile.code204);
		json.put(ConfigFile.msg,msg);
		return json.toJSONString();
	}

	/**
	 * 返回给客户端系统出现错误的提示信息,已返回给客户端,只能在controller层调用,用于增、删、改、查操作的异常返回给客户端
	 * @注意 不能在service层调用
	 * @作者 田应平
	 * @创建时间 2016年12月25日 下午5:07:16
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static void responseException(final HttpServletResponse response){
		responseJson(exceptionJson(),response);
		return;
	}

	/**
	 * 返回给客户端系统出现错误的提示信息,已返回给客户端,只能在controller层调用,用于增、删、改、查操作的异常返回给客户端
	 * @param msg 自定义提示的异常信息
	 * @注意 不能在service层调用
	 * @作者 田应平
	 * @创建时间 2016年12月25日 下午5:07:16
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static void responseException(final HttpServletResponse response,final String msg){
		responseJson(exceptionJson(msg),response);
		return;
	}

	/**
	 * 未登录提示信息,json格式
	 * @作者 田应平
	 * @创建时间 2017年1月14日 上午12:46:08
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String jsonNotLogin(){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,ConfigFile.code205);
		json.put(ConfigFile.msg,ConfigFile.msg205);
		return json.toJSONString();
	}

	/**
	 * 通用的响应json返回json对象,只能在是controller层调用
	 * @param jsonObject,可以是Bean对象,map;HashMap;List
	 * @param response
	 * @注意 不能在service层调用
	 * @作者 田应平
	 * @创建时间 2016年8月18日 17:53:18
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static void responseJson(final Object jsonObject,final HttpServletResponse response){
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
            if(jsonObject == null){
                writer.write(createJson(ConfigFile.code201,ConfigFile.msg201));
                writer.flush();
                writer.close();
                return;
            }
			if(jsonObject instanceof String){
				writer.write(JSON.parse(jsonObject.toString()).toString());
				writer.flush();
				writer.close();
				return;
			}else{
				writer.write(JSONArray.toJSONString(jsonObject));
				writer.flush();
				writer.close();
				return;
			}
		}catch (IOException e){
			e.printStackTrace();
			logger.error("类ToolClient的方法responseJson出现异常",e);
		}finally{
			if(!ToolString.isBlank(writer)){
				writer.close();
			}
		}
	}

	/**
	 * 响应返回客户端字符串,该obj对象字符串不是标准的json字符串!
	 * @param
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2018年1月7日 17:31:10
	*/
	public final static void responseObj(final Object obj,final HttpServletResponse response){
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(String.valueOf(obj));
			writer.flush();
			writer.close();
		}catch (IOException e){
			e.printStackTrace();
			logger.error("类ToolClient的方法responseWrite出现异常",e);
		}finally{
			if(writer != null){
				writer.close();
			}
		}
	}

	/**
	 * 远程接口密钥校验,验证成功返回true,否则返回false
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2016年11月22日 上午9:34:52
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	private static boolean validateEncrypt(String wkey){
		return encrypt("hguo","www.fwtai.com").equals(wkey);
	}

	/**
	 * SHA-1加密[encrypt("hguo","www.fwtai.com")==aa71fbcb9b7ae8aa650e5e4bccf97d312d1ff327]
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年11月22日 上午9:35:01
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	private static String encrypt(final Object source,final Object salt){
		return new SimpleHash("SHA-1", source, salt).toString();
	}

	/**
	 * 判断是否已经登录-返回true时说明已登录.一般用在权限控制或判断是否已登录
	 * @param request
	 * @return 返回true说明未登录或登录超时,false已登录且登录未超时
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2015-7-14 下午12:05:45
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static boolean checkLogin(final HttpServletRequest request){
		final HttpSession session = request.getSession(false);
		if(session == null){
			return false;
		}
		final Object login_key = session.getAttribute(ConfigFile.LOGIN_KEY);
		final Object login_user = session.getAttribute(ConfigFile.LOGIN_USER);
		return login_key != null && login_user != null;
	}

	/**
	 * 获取登录人的Session信息,根据key获取相应的值,有账号主键id(ConfigFile.LOGIN_KEY)、登录账号ConfigFile.LOGIN_USER
	 * @param request
	 * @return 返回登录人的Session信息,如userid
	 * @作者 田应平
	 * @创建时间 2015-8-26 下午2:30:01
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String loginKey(final HttpServletRequest request,String key){
		return (String) request.getSession(false).getAttribute(key);
	}

	/**
	 * 解答回答问题时所提交的图片上传路径处理,记得判断是否为空null,返回的相对地址
	 * @param request
	 * @param base64String 图片的base64字符串
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2015年10月24日 17:37:54
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String uploadBase64Images(final HttpServletRequest request,final String base64String){
		final String sys = File.separator;
		final String dirImage = "resources"+sys+ConfigFile.dir_root+sys+ConfigFile.dir_image;
		// 保存图片文件
		byte[] iconStreamStr = Base64.decodeBase64(base64String);
		InputStream input = null;
		FileOutputStream out = null;
		final String x = ToolString.getIdsChar32()+ ".jpg";
		String uploadPath = request.getSession().getServletContext().getRealPath(dirImage)+sys;
		File ff = new File(uploadPath);
		if(!ff.exists()){
			ff.mkdir();
		}
		String fileNames = uploadPath+x;
		String fileName = dirImage +"/"+x;
		try {
			input = new ByteArrayInputStream(iconStreamStr);
			// 写入文件
			final File file = new File(fileNames);
			final File directory = new File(uploadPath+sys);
			if(!directory.exists()){
				directory.mkdir();
			}
			out = new FileOutputStream(file);
			int i;
			while ((i = input.read()) != -1){
				out.write(i);
			}
		}catch (Exception e){
			e.printStackTrace();
			return null;
		} finally{
			try {
				if(out != null){
					out.close();
				}
				if(input != null){
					input.close();
				}
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		return fileName;
	}

	/**
	 * 单文件上传,先判断是否不为空再操作
	 * @作者 田应平
	 * @param type 是文件类型 1为文件;2为图片;
	 * @返回值类型 String
	 * @创建时间 2017年1月12日 下午2:04:23
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String uploadFile(final HttpServletRequest request,final int type) throws Exception{
		final MultipartHttpServletRequest mhsr = (MultipartHttpServletRequest) request;
		if(mhsr == null)return null;
		final DiskFileItemFactory fac = new DiskFileItemFactory();
		final ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		mhsr.setCharacterEncoding("utf-8");
		final String sys = File.separator;
		String dirType = sys;
		if(type == 2){
			dirType = sys+"resources"+sys+ConfigFile.dir_root+sys+ConfigFile.dir_image;
		}else {
			dirType = sys+"resources"+sys+ConfigFile.dir_root+sys+ConfigFile.dir_file;
		}
		final String savePath = mhsr.getSession().getServletContext().getRealPath(dirType);
		final File directory = new File(savePath);
		if(!directory.exists()){
			directory.mkdirs();
		}
		String strfiles = "";
		final Map<String, MultipartFile> files = mhsr.getFileMap();
		for (String key : files.keySet()){
			final MultipartFile mf = mhsr.getFile(key);
			final String name = mf.getOriginalFilename();
			String extName = "";
			if(name == null || name.trim().equals(""))continue;
			if(name.lastIndexOf(".") >= 0){
				extName = name.substring(name.lastIndexOf("."));
			}
			final File file = new File(savePath + sys + ToolString.getIdsChar32() + extName);
			if(!file.exists()){
				file.createNewFile();
			}
			mf.transferTo(file);
			if(strfiles.length() > 0)
				strfiles += ",";
			strfiles += file.getPath();
		}
		return strfiles;
	}

	/**
	 * 多(单)文件上传,先判断是否不为空再操作,支持多文件上传.返回的是ArrayList的数据,其中每个list就是一个HashMap文件信息,其中key为originals是原文件名;key为filePaths是全路径的文件名
	 * @作者 田应平
	 * @param type 是文件类型 1为文件;2为图片;
	 * @throws Exception
	 * @返回值类型 返回的是ArrayList的数据,其中一个list就是一个HashMap文件信息,其中key为originals是原文件名;key为filePaths是全路径的文件名
	 * @创建时间 2017年11月25日 11:11:49
	 * @QQ号码 444141300
	 * @主页 http://www.yinlz.com
	*/
	public final static ArrayList<HashMap<String,String>> uploadFiles(final HttpServletRequest request,final int type) throws Exception{
		final MultipartHttpServletRequest mhsr = (MultipartHttpServletRequest) request;
		final ArrayList<HashMap<String,String>> listFile = new ArrayList<HashMap<String,String>>();
		final HashMap<String,String> map = new HashMap<String,String>(2);
		if(mhsr == null)return listFile;
		final DiskFileItemFactory fac = new DiskFileItemFactory();
		final ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		mhsr.setCharacterEncoding("utf-8");
		final String sys = File.separator;
		String dirType = sys;
		if(type == 2){
			dirType = sys+"resources"+sys+ConfigFile.dir_root+sys+ConfigFile.dir_image;
		}else {
			dirType = sys+"resources"+sys+ConfigFile.dir_root+sys+ConfigFile.dir_file;
		}
		final String savePath = mhsr.getSession().getServletContext().getRealPath(dirType);
		final File directory = new File(savePath);
		if(!directory.exists()){
			directory.mkdirs();
		}
		final Map<String, MultipartFile> files = mhsr.getFileMap();
		for (String key : files.keySet()){
			final MultipartFile mf = mhsr.getFile(key);
			final String name = mf.getOriginalFilename();
			String extName = "";
			if(name == null || name.trim().equals(""))continue;
			if(name.lastIndexOf(".") >= 0){
				extName = name.substring(name.lastIndexOf("."));
			}
			final File file = new File(savePath + sys + ToolString.getIdsChar32() + extName);
			if(!file.exists()){
				file.createNewFile();
			}
			mf.transferTo(file);
			map.put("filePaths",file.getPath());//单个文件的绝对的物理路径
			map.put("originals",name);//单个文件的原文件名
			listFile.add(map);
		}
		return listFile;
	}

	/**
	 * 多(单)文件上传,先判断是否不为空再操作,支持多文件上传.返回的是HashMap的数据,其中key为originals是多个原文件名;key为filePaths是多个全路径的文件名
	 * @作者 田应平
	 * @param type 是文件类型 1为文件;2为图片;
	 * @throws Exception
	 * @返回值类型 返回的是HashMap的数据,其中key为originals是多个原文件名;key为filePaths是多个全路径的文件名
	 * @创建时间 2017年10月27日 09:48:07
	 * @QQ号码 444141300
	 * @主页 http://www.yinlz.com
	*/
	public final static HashMap<String,ArrayList<String>> uploadFilesMap(final HttpServletRequest request,final int type) throws Exception{
		final MultipartHttpServletRequest mhsr = (MultipartHttpServletRequest) request;
		if(mhsr == null)return null;
		final HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		final ArrayList<String> listOriginal = new ArrayList<String>();
		final ArrayList<String> listPath = new ArrayList<String>();
		final DiskFileItemFactory fac = new DiskFileItemFactory();
		final ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		mhsr.setCharacterEncoding("utf-8");
		final String sys = File.separator;
		String dirType = sys;
		if(type == 2){
			dirType = sys+"resources"+sys+ConfigFile.dir_root+sys+ConfigFile.dir_image;
		}else {
			dirType = sys+"resources"+sys+ConfigFile.dir_root+sys+ConfigFile.dir_file;
		}
		final String savePath = mhsr.getSession().getServletContext().getRealPath(dirType);
		final File directory = new File(savePath);
		if(!directory.exists()){
			directory.mkdirs();
		}
		final Map<String, MultipartFile> files = mhsr.getFileMap();
		for (String key : files.keySet()){
			final MultipartFile mf = mhsr.getFile(key);
			final String name = mf.getOriginalFilename();
			String extName = "";
			if(name == null || name.trim().equals(""))continue;
			if(name.lastIndexOf(".") >= 0){
				extName = name.substring(name.lastIndexOf("."));
			}
			final File file = new File(savePath + sys + ToolString.getIdsChar32() + extName);
			if(!file.exists()){
				file.createNewFile();
			}
			mf.transferTo(file);
			listPath.add(file.getPath());//绝对的物理路径
			listOriginal.add(name);//原文件名
		}
		map.put("originals",listOriginal);
		map.put("filePaths",listPath);
		return map;
	}

	/**
	 * 多(单)文件上传,先判断是否不为空再操作,返回的是相对路径,文件存放非项目路径,即图片|文件和项目分离
	 * @作者 田应平
	 * @param type 是文件类型 1为文件;2为图片;
	 * @throws Exception
	 * @返回值类型 String
	 * @创建时间 2017年9月27日 11:19:12
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String uploadDisk(final HttpServletRequest request,final int type) throws Exception{
		final MultipartHttpServletRequest multipar = (MultipartHttpServletRequest) request;
		if(multipar == null)return null;
		final DiskFileItemFactory fac = new DiskFileItemFactory();
		final ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		multipar.setCharacterEncoding("utf-8");
		final String sys = File.separator;//服务器上目录,所以用File.separator目录分隔符
		final String disk = "disk";//这个要和path=值要一致,本处采用了文件和项目分离的<Context path="disk" ……/>
		String dirType = sys;
		final String web = getWebRoot(request);// 获取项目的物理路径 D:\MyTools\tomcat-8.0.46\webapps\pqmis √
		final String prev = web.substring(0,web.lastIndexOf(sys));
		final String prev_dir = prev.substring(0,prev.lastIndexOf(sys))+sys+disk;//和项目在同一级目录,D:\MyTools\tomcat-8.0.46\disk
		if(type == 2){
			dirType = prev_dir+sys+ConfigFile.dir_image;
		}else {
			dirType = prev_dir+sys+ConfigFile.dir_file;
		}
		final File directory = new File(dirType);
		if(!directory.exists()){
			directory.mkdirs();
		}
		String strfiles = "";
		final Map<String,MultipartFile> files = multipar.getFileMap();
		for (String key : files.keySet()){
			final MultipartFile mf = multipar.getFile(key);
			final String name = mf.getOriginalFilename();
			String extName = "";
			if(name == null || name.trim().equals(""))continue;
			if(name.lastIndexOf(".") >= 0){
				extName = name.substring(name.lastIndexOf("."));
			}
			final String _file = ToolString.getIdsChar32() + extName;
			final File file = new File(dirType + sys + _file);
			if(!file.exists()){
				file.createNewFile();
			}
			mf.transferTo(file);//创建生成文件
			String file_dir = "";
			final String uri_slash = "/";//返回给网页显示的,所以是斜杠/
			if(type == 2){
				file_dir = disk+uri_slash+ConfigFile.dir_image;
			}else {
				file_dir = disk+uri_slash+ConfigFile.dir_file;
			}
			final String temp = file_dir + uri_slash + _file;
			if(strfiles.length() > 0)
				strfiles += ",";
			strfiles += temp;
		}
		return strfiles;
	}

	/**
	 * 文件下载
	 * @param filePath 文件物理路径
	 * @param response
	 * @作者 田应平
	 * @创建时间 2015-10-17 下午6:01:36
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static boolean download(final HttpServletResponse response,final String filePath){
		try {
			// filePath是指欲下载的文件的全路径。
			final File file = new File(filePath);
			if(!file.exists()){
				logger.info("类ToolClient.java下的方法download():文件不存在");
				return false;
			}
			// 取得文件名。
			final String filename = file.getName();
			// 取得文件的后缀名。
			final String ext = filename.substring(filename.lastIndexOf(".") + 1);
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename="+ new String((filename + ext).getBytes("utf-8"), "ISO-8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			return true;
		} catch (IOException ex){
			ex.printStackTrace();
			logger.error("类ToolClient.java下的方法download():出现异常",ex);
			return false;
		}
	}

	/**
	 * 获取项目物理根路径
	 * @返回结果 {"code":"200","msg":"E:\workspace\manager"}
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年1月5日 12:32:51
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String getWebRoot(){
		return RequestContext.class.getResource("/../../").getPath();
	}

	/**
	 * 获取项目所在的物理路径,推荐使用
	 * @param request
	 * @作者 田应平
	 * @创建时间 2017年9月25日 下午3:47:29
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String getWebRoot(final HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath(File.separator);
	}

	/**
	 * 获取访问项目的网站域名,如http://api.yinlz.com
	 * @param request
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年1月16日 15:18:55
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String getDomainName(final HttpServletRequest request){
		return request.getScheme()+"://"+request.getServerName();
	}

	/**
	 * 统计处理
	 * @作者 田应平
	 * @参数 List 是数据的数据条数
	 * @参数 keyTotal是count字段或该字段别名
	 * @参数 decimalFormat是统计时的数据格式化,如0、0.0、0.00
	 * @创建时间 2016年9月12日 下午7:34:01
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static List<Map<String, Object>> statistics(final List<Map<String, Object>> list,final String keyTotal,final String decimalFormat){
		Integer total = 0;
		for(int i = 0; i < list.size(); i++){
			final Map<String, Object> map = list.get(i);
			for(String key : map.keySet()){
				if(key.equals(keyTotal)){
					total += Integer.parseInt(map.get(key).toString());//计算总数
				}
			}
		}
		final List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < list.size(); i++){
			final Map<String, Object> map = list.get(i);
			final Map<String, Object> m = new HashMap<String, Object>();
			for(String key : map.keySet()){
				if(key.equals(keyTotal)){
					float f = (float)(Integer.parseInt(map.get(key).toString()))/total * 100;//求平均数
					final DecimalFormat df = new DecimalFormat(decimalFormat);//格式化小数,如0.0或0或0.00
					m.put(key,Double.parseDouble(df.format(f)));
				}else {
					m.put(key,map.get(key));
				}
			}
			result.add(m);
		}
		return result;
	}

	/**
	 * 生成带分页的参数查询参数
	 * @param params
	 * @param pageSize 每页大小
	 * @param current 当前页
	 * @作者 田应平
	 * @返回值类型 HashMap<String,Object>
	 * @创建时间 2016年12月29日 下午10:06:03
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static HashMap<String, Object> pageParams(final HashMap<String, Object> params,final Integer pageSize,final Integer current){
		params.put(ConfigFile.section,(current - 1) * pageSize);//读取区间
		params.put(ConfigFile.pageSize,pageSize);//每页大小
		return params;
	}

	/**
	 * 数据库为Mysql的Easyui组装Datagrid固定分页参数,section读取区间;pageSize每页大小;
	 * @作者 田应平
	 * @返回值类型 PageFormData
	 * @创建时间 2017年1月23日 下午3:45:06
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static PageFormData datagridPagingMysql(final PageFormData pageFormData){
		String page = pageFormData.getString("page");//当前页
		String rows = pageFormData.getString("rows");//每页大小
		final Object order = pageFormData.get("order");//排序关键字
		final Object sort = pageFormData.get("sort");//排序的字段
		if(ToolString.isBlank(page))page = "1";
		if(ToolString.isBlank(rows))rows = "50";
		Integer pageSize = ConfigFile.size_default;
		Integer current = 1;
		try{
			pageSize = Integer.parseInt(rows);
			if(pageSize > 200)pageSize = ConfigFile.size_default;
			current = Integer.valueOf(page);//当前页
			if(current < 1)current = 1;
		}catch(Exception e){
			pageSize = ConfigFile.size_default;current = 1;
		}
		pageFormData.put(ConfigFile.section,(current - 1) * pageSize);//读取区间
		pageFormData.put(ConfigFile.pageSize,pageSize);//每页大小
		pageFormData.put("sort",ToolString.isBlank(sort)?null:ToolString.sqlInject(String.valueOf(sort)));//排序字段 order by name desc
		pageFormData.put("order",ToolString.isBlank(order)?null:ToolString.sqlInject(String.valueOf(order)));//排序关键字
		return pageFormData;
	}

	/**
	 * 数据库为Mysql的Easyui组装Datagrid固定分页参数,section读取区间;pageSize每页大小;
	 * @作者 田应平
	 * @返回值类型 PageFormData
	 * @创建时间 2017年10月21日 15:30:18
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static HashMap<String,Object> datagridPagingMysql(final HashMap<String,Object> hashMap){
		Object page = hashMap.get("page");//当前页
		Object rows = hashMap.get("rows");//每页大小
		final Object order = hashMap.get("order");//排序关键字
		final Object sort = hashMap.get("sort");//排序的字段
		if(ToolString.isBlank(page))page = "1" ;
		if(ToolString.isBlank(rows))rows = "50" ;
		Integer pageSize = ConfigFile.size_default;
		Integer current = 1;
		try{
			pageSize = Integer.parseInt(rows.toString());
			if(pageSize > 200)pageSize = ConfigFile.size_default;
			current = Integer.valueOf(page.toString());//当前页
			if(current < 1)current = 1;
		}catch(Exception e){
			pageSize = ConfigFile.size_default;current = 1;
		}
		hashMap.put(ConfigFile.section,(current - 1) * pageSize);//读取区间
		hashMap.put(ConfigFile.pageSize,pageSize);//每页大小
		hashMap.put("sort",ToolString.isBlank(sort)?null:ToolString.sqlInject(String.valueOf(sort)));//排序字段 order by name desc
		hashMap.put("order",ToolString.isBlank(order)?null:ToolString.sqlInject(String.valueOf(order)));//排序关键字
		return hashMap;
	}

	/**
	 * 数据库为Oracle的Easyui组装Datagrid固定分页参数,currentPage当前页;pageSize每页大小;
	 * @作者 田应平
	 * @返回值类型 pageFormData
	 * @创建时间 2017年5月26日 11:27:47
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static PageFormData datagridPagingOracle(final PageFormData pageFormData){
		String page = pageFormData.getString("page");//当前页
		String rows = pageFormData.getString("rows");//每页大小
		if(ToolString.isBlank(page))page = "1" ;
		if(ToolString.isBlank(rows))rows = "50" ;
		Integer pageSize = ConfigFile.size_default;
		Integer current = 1;
		try{
			pageSize = Integer.parseInt(rows);
			if(pageSize > 200)pageSize = ConfigFile.size_default;
			current = Integer.valueOf(page);//当前页
			if(current < 1)current = 1;
		}catch(Exception e){
			pageSize = ConfigFile.size_default;current = 1;
		}
		pageFormData.put(ConfigFile.KEYROWS,current * pageSize);//<=#{KEYROWS}
		pageFormData.put(ConfigFile.KEYRN,(current * pageSize - pageSize + 1));//>=#{KEYRN}
		return pageFormData;
	}

	/**
	 * 组装jqGrid带分页的mysql查询参数
	 * @用法 final HashMap<String,Object> params  = ToolClient.jqGridPageParams(request);//获取请求带分页的参数
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2018-01-01 21:22
	*/
	public final static HashMap<String,Object> jqGridPageParamsMysql(final HttpServletRequest request){
		final HashMap<String,String> params = getFormParams(request);
		final String rows = params.get("rows");//每页大小
		final String current = params.get("page");//当前页
		final String order = params.get("order");//排序关键字
		final String sort = params.get("sidx");//排序字段
		Integer currentPage = 1;
		Integer pageSize = ConfigFile.size_default;
		final HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			currentPage = Integer.parseInt(current);
			pageSize = Integer.parseInt(rows);
			if(pageSize > 200)pageSize = ConfigFile.size_default;
			if(currentPage < 1)currentPage = 1;
		}catch(Exception e){
			pageSize = ConfigFile.size_default;currentPage = 1;
		}
		map.put(ConfigFile.section,(currentPage - 1) * pageSize);//读取区间
		map.put(ConfigFile.pageSize,pageSize);//每页大小
		map.put("sort",ToolString.isBlank(sort)?null:ToolString.sqlInject(sort));//排序的字段 order by name desc
		map.put("order",ToolString.isBlank(order)?null:ToolString.sqlInject(order));//排序关键字
		map.put(ConfigFile.current,currentPage);//当前页
		map.putAll(params);
		return map;
	}

	/**
	 * 拆分jqGrid 的查询参数
	 * @param params
	 * @注意 jqGridParams 是jqGrid搜索的固定的值
	 * @用法 params = ToolClient.jqGridParams(params);
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2018年1月20日 15:51:48
	*/
	public static final HashMap<String,Object> jqGridParams(final HashMap<String, Object> params){
		final String jqGridParams = "jqGridParams";
		if(!ToolString.isBlank(params.get(jqGridParams))){
			String words = params.get(jqGridParams).toString();
			String[] arr = words.split("&");
			for(int x = 0; x < arr.length; x++){
				final String array = arr[x];
				int split = array.lastIndexOf("=");
				final String key = array.substring(0,split);
				final String value = array.substring(split+1,array.length());
				if(value != null && value.length() > 0){
                    if(value.length() == 1 && value.equals("_"))
                        continue;
					params.put(key,value.trim());
				}
			}
			params.remove(jqGridParams);
		}
		return params;
	}

	/**
	 * 返回jqGrid数据列表的key及相应的数据,用法直接返回:return ToolClient.jqGridPageReader(list,ToolClient.getJqGridCurrentPage(params),ToolClient.getJqGridPageSize(params),listTotal);
	 * @param list 数据列表
	 * @param currentPage 当前页
	 * @param pageSize 每页大小
	 * @param total 总记录数|总条数
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2018-01-01 15:16
	*/
	public final static String jqGridPageReader(final List<?> list,final Integer currentPage,final Integer pageSize,final Integer total){
		final JSONObject json = new JSONObject();
		json.put("list",list);//数据显示
		json.put("page",currentPage);//当前页数
		json.put("totalPage",(total%pageSize) == 0 ? (total/pageSize):(total/pageSize)+1);//总页数
		json.put("records",total);//总记录数
		return json.toJSONString();
	}

	/**
	 * 从带分页jqGrid请求参数里获取当前页
	 * @param params
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2018年1月2日 14:52:36
	*/
	public final static Integer getJqGridCurrentPage(final HashMap<String,Object> params){
		return Integer.parseInt(String.valueOf(params.get(ConfigFile.current)));
	}

	/**
	 * 从带分页jqGrid请求参数里获取每页大小
	 * @param params
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2018年1月2日 14:56:08
	*/
	public final static Integer getJqGridPageSize(final HashMap<String,Object> params){
		return Integer.parseInt(String.valueOf(params.get(ConfigFile.pageSize)));
	}

    /**
     * 获取表单的请求参数,不含文件域,返回的是HashMap<String,String>
     * @param request
     * @作者:田应平
     * @创建时间 2019年11月13日 19:14:15
     * @主页 www.fwtai.com
    */
    public final static HashMap<String,String> getFormParams(final HttpServletRequest request){
        final HashMap<String,String> params = new HashMap<String,String>();
        final Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()){
            final String key = paramNames.nextElement();
            final String value = request.getParameter(key);
            if(value != null && value.length() >0){
                if(value.length() == 1 && value.equals("_"))
                    continue;
                params.put(key,value.trim());
            }
        }
        return params;
    }

    /**
     * 获取表单的请求参数,不含文件域,返回的是线程安全的ConcurrentHashMapString,String>
     * @param request
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/11/13 19:29
    */
    public final static ConcurrentHashMap<String,String> getFormParam(final HttpServletRequest request){
        final ConcurrentHashMap<String,String> params = new ConcurrentHashMap<String,String>();
        final Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()){
            final String key = paramNames.nextElement();
            final String value = request.getParameter(key);
            if(value != null && value.length() > 0){
                if(value.length() == 1 && value.equals("_"))
                    continue;
                params.put(key,value.trim());
            }
        }
        return params;
    }

	/**
	 * 获取表单的请求参数,不含文件域
	 * @param request
	 * @作者:田应平
	 * @创建时间 2017年10月21日 16:03:16
	 * @主页 www.fwtai.com
	*/
	public final static PageFormData getFormData(final HttpServletRequest request){
		final PageFormData params = new PageFormData();
		final Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()){
            final String key = paramNames.nextElement();
            final String value = request.getParameter(key);
            if(value != null && value.length() > 0){
                if(value.length() == 1 && value.equals("_"))
                    continue;
                params.put(key,value.trim());
            }
        }
		return params;
	}

	/**
	 * 获取表单的所有请求参数
	 * @param request
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2020/1/8 21:25
	*/
    public final static JSONObject getRequestData(final HttpServletRequest request){
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
            final StringBuilder sb = new StringBuilder();
            String s = "";
            while((s = in.readLine()) != null){
                sb.append(s);
            }
            in.close();
            return JSONObject.parseObject(sb.toString().trim());
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把form表单数据转为对象[Map<String,String> | HashMap<String,Object> | bean]
     * @param request
     * @param bean Map<String,String> | HashMap<String,Object> | bean
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019年4月1日 02:42:04
    */
    public final static void formConvertBean(final HttpServletRequest request,final Object bean){
        try {
            org.apache.commons.beanutils.BeanUtils.populate(bean,request.getParameterMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * 获取表单的请求参数并组装为MySQL的Datagrid带分页的参数,不含文件域
	 * @作者 田应平
	 * @创建时间 2017年10月23日 上午3:41:35
	 * @QQ号码 444141300
	 * @官网 http://www.yinlz.com
	*/
	public final static HashMap<String,Object> getDatagridPagingMysql(final HttpServletRequest request){
		final HashMap<String,Object> params = new HashMap<String,Object>();
		final Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()){
			final String key = paramNames.nextElement();
			final String[] values = request.getParameterValues(key);
			String value = "";
			if(values == null){
				value = "";
			}else {
				for (int i = 0; i < values.length; i++){
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			}
			params.put(key,value.trim());
		}
		Object page = params.get("page");//当前页
		Object rows = params.get("rows");//每页大小
		if(ToolString.isBlank(page))page = "1" ;
		if(ToolString.isBlank(rows))rows = "50" ;
		Integer pageSize = ConfigFile.size_default;
		Integer current = 1;
		try{
			pageSize = Integer.parseInt(rows.toString());
			if(pageSize > 200)pageSize = ConfigFile.size_default;
			current = Integer.valueOf(page.toString());//当前页
			if(current < 1)current = 1;
		}catch(Exception e){
			pageSize = ConfigFile.size_default;current = 1;
		}
		params.put(ConfigFile.section,(current - 1) * pageSize);//读取区间
		params.put(ConfigFile.pageSize,pageSize);//每页大小
		return params;
	}

	/**
	 * 获取表单的请求参数并组装为MySQL的Datagrid带分页的参数,不含文件域
	 * @作者 田应平
	 * @创建时间 2017年10月23日 上午3:41:35
	 * @QQ号码 444141300
	 * @官网 http://www.yinlz.com
	*/
	public final static PageFormData getDatagridPageMysql(final HttpServletRequest request){
		final PageFormData pageFormData = new PageFormData();
		final Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()){
			final String key = paramNames.nextElement();
			final String[] values = request.getParameterValues(key);
			String value = "";
			if(values == null){
				value = "";
			}else {
				for (int i = 0; i < values.length; i++){
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			}
			pageFormData.put(key,value.trim());
		}
		String page = pageFormData.getString("page");//当前页
		String rows = pageFormData.getString("rows");//每页大小
		if(ToolString.isBlank(page))page = "1" ;
		if(ToolString.isBlank(rows))rows = "50" ;
		Integer pageSize = ConfigFile.size_default;
		Integer current = 1;
		try{
			pageSize = Integer.parseInt(rows);
			if(pageSize > 200)pageSize = ConfigFile.size_default;
			current = Integer.valueOf(page);//当前页
			if(current < 1)current = 1;
		}catch(Exception e){
			pageSize = ConfigFile.size_default;current = 1;
		}
		pageFormData.put(ConfigFile.section,(current - 1) * pageSize);//读取区间
		pageFormData.put(ConfigFile.pageSize,pageSize);//每页大小
		return pageFormData;
	}

	/**获取访问者真实的IP地址*/
	public final static String getIp(final HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		if (ip != null && ip.length() > 0){
			if(ip.contains(",")){
				String[] ips = ip.split(ip);
				if(ips.length > 0){
					return ips[0];
				}
			}
		}
		return ip;
	}

	/**
	 * 获取由HttpClient发送的数据的HttpServletRequest请求参数
	 * @作者 田应平
	 * @QQ 444141300
     * @param request 请求参数,默认的字符编码为"UTF-8"
	 * @创建时间 2018年7月3日 09:33:19
	*/
    public final static String getHttpClientRequest(final HttpServletRequest request) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final InputStream is = request.getInputStream();
        final InputStreamReader isr = new InputStreamReader(is,"UTF-8");
        final BufferedReader br = new BufferedReader(isr);
        String s = "";
        while ((s = br.readLine()) != null){
            sb.append(s);
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    /**
     * 获取由HttpClient发送的数据的HttpServletRequest请求参数
     * @作者 田应平
     * @QQ 444141300
     * @param request 请求参数
     * @param charsetName 字符编码,如 "UTF-8"
     * @创建时间 2018年7月3日 09:39:00
    */
    public final static String getHttpClientRequest(final HttpServletRequest request,final String charsetName) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final InputStream is = request.getInputStream();
        final InputStreamReader isr = new InputStreamReader(is,charsetName);
        final BufferedReader br = new BufferedReader(isr);
        String s = "";
        while ((s = br.readLine()) != null){
            sb.append(s);
        }
        return sb.length() > 0 ? sb.toString() : null;
    }
}