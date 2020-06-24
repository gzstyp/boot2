package com.fwtai.service.async;

import com.fwtai.dao.DaoBase;
import com.fwtai.tool.ToolString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 异步处理,注意，这里的异步方法，只能在本类的自身之外调用，在本类调用是无效的!!!,即在别的类调用即可
 * @注意 需要在启动类添加开启异步处理@EnableAsync注解类
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2019-05-08 17:13
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@Component
public class AsyncHandle{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DaoBase dao;

    /**
     * 异步处理登录信息处理
     * @param params
     * @param enabled 是否开启登录错误次数锁定功能:true开启false禁用
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/5/8 17:27
    */
    @Async
    public void updateLogin(final HashMap<String,String> params,final boolean enabled){
        try {
            final String uid = params.get("uid");
            dao.execute("sys_user.updatelogintime",params);/*最后登录时间*/
            dao.execute("sys_user.updateTimes",uid);/*更新登录次数*/
            if(enabled){
                dao.execute("sys_user.updateErrorTime",uid);/*登录成功把时间设置为当前默认时间*/
                dao.execute("sys_user.updateErrorCount",uid);/*登录成功把登录错误次数更改为0*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**根据iP异步更新位置信息*/
    @Async
    public void loginLocation(final String id,final String ip){
        final HashMap<String,Object> params = new HashMap<String,Object>(0);
        HashMap<String,String> map = ToolString.getLocation(ip);
        params.put("id",id);
        if(map.containsKey("ret") && map.get("ret").equalsIgnoreCase("1")){
            params.put("location",map.get("country")+map.get("province")+map.get("city"));
            try {
                dao.execute("sys_log.updateLog",params);
            } catch (Exception e) {
                logger.error("更新操作日志的location失败",e);
            }
        }else{
            map = ToolString.getLocation();
            params.put("location",map.get("cname"));
            params.put("ip",map.get("cip"));
            try {
                dao.execute("sys_log.updateIp",params);
            } catch (Exception e) {
                logger.error("更新操作日志的location失败",e);
            }
        }
    }
}