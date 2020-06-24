package com.fwtai.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.boot.SpringBootConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义shiro框架Realm对象
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-02-15 23:15
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@SpringBootConfiguration
public class AuthShiro extends AuthorizingRealm{

    /**自定义realm名称*/
    @Override
    public void setName(String name){
        super.setName(name);
    }

    /**认证,功能及作用:根据用户密码(不含用户名)登录,若是登录成功则将用户的数据[安全数据]保存*/
    /*
        主要目的就是比较用户名和密码与数据库中是个一致，若一致则将安全数据存入到shiro进行保管
        参数 principalCollection 就是用登录时指定的{
        final Subject subject = SecurityUtils.getSubject();//获取客户端信息
        final UsernamePasswordToken token = new UsernamePasswordToken(username,pwd);
        subject.login(token);
        }构造方法,所以需要强转为 UsernamePasswordToken
    */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken authenticationToken) throws AuthenticationException{
        /*
            步骤:
                1.构造uptoken
                2.获取(前端)数据的用户密码[不含密码]
                3.根据用户名查询数据库
                4.比较用户密码和数据库中的密码是否一致(密码可能需要加密)
                5.如果成功,想shiro存入安全数据
                6.如果失败,抛出异常
        */
        final UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        final String username = token.getUsername();
        final String password = new String(token.getPassword());
        //此处是根据用户名去数据库查询得到密码再与 password 比较
        if(password.equals("123456")){
            //如果验证成功，最终这里返回的信息authenticationInfo 的值与传入的第一个参数的值相同,即第一个参数是安全数据
            final SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username,password,getName());//即第1个参数是安全数据，这个参数可以是User实体对象也可以是username;第2个参数是密码;第3个参数就是上面方法 setName 的 get方法的值
            return info;
            //成功
        }else{
            //失败
            throw new AuthenticationException("认证失败,用户名或密码错误");
        }
    }

    /**
     * 授权,其作用就是获取到用户的授权数据(说白了就是用户的权限数据),其目的就是 根据认证成功后的认证数据获取用户的权限信息[参数 principal 包含了认证成功后的所有数据,即 new SimpleAuthenticationInfo(username,password,getName());的第1个参数数据]
     * @param principal 参数 principal 包含了认证成功后的所有数据,即 new SimpleAuthenticationInfo(username,password,getName());的第1个参数数据
     * @return AuthorizationInfo 就是授权数据(含角色和权限数据)
    */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principal){
        /*
            步骤
                1.获取安全数据(就是上面的认证方法doGetAuthenticationInfo()里的final SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username,password,getName());的第1个参数)
                2.根据该用户安装数据username或id去数据库查询角色或权限数据
                3.构造返回
        */
        final String userName = (String)principal.getPrimaryPrincipal();//其实就是上面的 final SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username,password,getName());//即第1个参数的数据
        //final String username = (String) SecurityUtils.getSubject().getPrincipal();//这个值username是 new SimpleAuthenticationInfo(username,password,getName());的第1个参数的数据

        final List<String> primarys = new ArrayList<>();//假设权限数据
        primarys.add("user:insert");
        primarys.add("user:update");
        primarys.add("user:delete");
        primarys.add("user:query");

        final List<String> roles = new ArrayList<>();//假设角色数据
        roles.add("admin");
        roles.add("super");
        roles.add("user");

        final SimpleAuthorizationInfo infos = new SimpleAuthorizationInfo();
        infos.addStringPermissions(primarys);
        infos.addRoles(roles);

        return infos;
    }
}