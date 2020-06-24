package com.fwtai.tool;

import java.util.HashMap;

/**
 * token的保存,其使用方法在下面的注释里
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-03-12 22:42
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
public final class ToolThreadLocal{

    private static final ThreadLocal<HashMap<String,String>> local = new ThreadLocal<>();

    public final static void set(final HashMap<String,String> map){
        local.set(map);
    }

    public final static HashMap<String,String> get(){
        return local.get();
    }

    public final static void remove(){
        local.remove();
    }
}
/*
    示例一：

    private final static ThreadLocal<SystemUser> userThreadLocal = new ThreadLocal<SystemUser>();

    @Autowired
    private SystemLogService systemLogService;

    //设置当前用户
    public void setCurrentSystemUser(SystemUser user) {
        userThreadLocal.set(user);
    }
    //从本地线程中取当前用户
    public SystemUser getCurrentSystemUser() {
        SystemUser user = userThreadLocal.get();
        if (null == user) {
            user = (SystemUser) SecurityUtils.getSubject().getPrincipal();
        }
        return user;
    }

 */

/*
    示例二：

    //1.利用线程本地变量来保存登录信息(利用ThreadLocal实现中间类来保存登录信息)
    public class SessionCache {

        private static ThreadLocal<Session> threadLocal = new ThreadLocal<>();

        public static <T extends Session> void put(T t) {
            threadLocal.set(t);
        }

        @SuppressWarnings("unchecked")
        public static <T> T get() {
            return (T) threadLocal.get();
        }

        public static void remove() {
            threadLocal.remove();
        }
    }

    2.在程序登录（token）认证拦截器中实现保存和删除功能
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.lang.Nullable;
    import org.springframework.web.servlet.HandlerInterceptor;

    import com.example.demo.common.session.SessionCache;
    import com.example.demo.common.session.UserSession;

    public class LoginAuthInterceptor implements HandlerInterceptor {

        private static final Logger logger = LoggerFactory.getLogger(LoginAuthInterceptor.class);

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            final String token = request.getHeader("token");
            // 根据token获取登录信息
            UserSession session = new UserSession();
            session.setAccountId("test");
            session.setName("测试");
            SessionCache.put(session);//存入
            logger.info("请求方法方法前拦截");
            return true;
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,@Nullable Exception ex) throws Exception {
            SessionCache.remove();//移除
            logger.info("请求方法方法后处理");
        }
    }

    3.在需要当前登录信息的方法中使用
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import com.example.demo.common.session.SessionCache;
    import com.example.demo.common.session.UserSession;

    @RestController
    public class TestController {

        private static final Logger logger = LoggerFactory.getLogger(TestController.class);

        @RequestMapping(value = "/test/session")
        public String testSession(TestDto dto) {
            UserSession session = SessionCache.get();
            String userId = session.getAccountId();
            logger.info("当前登录用户ID为{}", userId);
            return "success：" + userId;
        }
    }
 */