package com.fwtai.tool;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * jwt(JSON Web Token)令牌工具类
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-02-12 23:53
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
 * @参考1 shiro + jwt <uri>https://www.jianshu.com/p/c89cadcac21f</uri>
 * @参考2 shiro + jwt <uri>https://blog.csdn.net/baidu_33969289/article/details/86616058</uri>
*/
public final class ToolJWT{

    private final static String salt = "wwwYinlz.com";//加密的密盐,也可以称为服务端的私钥

    private final static long expire = 1000 * 60 * 5 ;//分钟数, 1000 * 60 * 1 是1分钟,final long l = (claims.getExpiration().getTime() - claims.getIssuedAt().getTime()) / (60000);// 1000 * 60

    private final static String issuer = "引路者";//jwt签发者,也可以理解为服务端的私钥

    /**
     * 生成token令牌
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/2/13 0:03
    */
    public final static String jwtBuilder(final String userId,final String userName){
        final Date date = new Date();
        final JwtBuilder jwtBuilder = Jwts.builder().setIssuer(issuer).signWith(SignatureAlgorithm.HS256,salt)//头部信息,yinlz.com是加密的盐值
            .setId(userId)//登录者id,载荷部分1,设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
            .setSubject(userName)//登录名|jwt所面向的用户,载荷部分2[代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志]
            .setIssuedAt(date)//登录时间|签发时间,载荷部分3
            .setExpiration(new Date(date.getTime() + expire));//过期时间
        return jwtBuilder.compact();//其签名是靠‘头部信息(Header)’和载荷部分(playload)生成的
    }

    /**
     * 生成token令牌
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/2/15 0:59
    */
    public final static String jwtBuilder(final String userId,final String userName,final HashMap<String,Object> claims){
        final Date date = new Date();
        final JwtBuilder jwtBuilder = Jwts.builder().setIssuer(issuer).signWith(SignatureAlgorithm.HS256,salt)//头部信息,yinlz.com是加密的盐值
            .setId(userId)//登录者id,载荷部分1,设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
            .setSubject(userName)//登录名|jwt所面向的用户,载荷部分2[代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志]
            .setIssuedAt(date)//登录时间|签发时间,载荷部分3
            .setExpiration(new Date(date.getTime() + expire));//过期时间
        //不能这样写:setClaims(claims) 它是覆盖了那些标准的声明的
        final Iterator<Map.Entry<String,Object>> iterator = claims.entrySet().iterator();
        while(iterator.hasNext()){
            final Map.Entry<String,Object> entry = iterator.next();
            jwtBuilder.claim(entry.getKey(),entry.getValue());//添加自定义字段,支持K,V格式数据,比如角色
        }
        return jwtBuilder.compact();//其签名是靠‘头部信息(Header)’和载荷部分(playload)生成的
    }

    /**
     * 创建jwt，json web token
     * @param id 用户id
     * @param subject 用户name
     * @param ttlMillis token有效时间
     * @return 字符串的 token
     * @throws Exception
    */
    public final static String create(final String id,final String subject,final long ttlMillis) throws Exception{
        final long nowMillis = System.currentTimeMillis();
        final JwtBuilder builder = Jwts.builder().setIssuer(issuer).setId(id).setIssuedAt(new Date(nowMillis)).setSubject(subject).signWith(SignatureAlgorithm.HS256,salt);
        if(ttlMillis >= 0){
            final long expMillis = nowMillis + ttlMillis;
            builder.setExpiration(new Date(expMillis));
        }
        return builder.compact();
    }

    /**
     * 解析token令牌,Claims就是 Map<String,Object>,也可以用 Map<String,Object> 类接收返回值
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/2/13 0:08
    */
    public final static Claims jwtParser(final String token){
        return Jwts.parser()
            .setSigningKey(salt)//指定加密的盐值
            .requireIssuer(issuer)//jwt签发者
            .parseClaimsJws(token)//jwt令牌数据
            .getBody();
    }

    /**
     * 验证token是否失效,返回true已失效,否则有效
     * @param token
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020年2月24日 16:11:10
    */
    public final static boolean tokenExpired(final String token) {
        try {
            return jwtParser(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException exp) {
            return true;
        }
    }

    //jwt令牌-解析
    private final static void exampleParser(final String token){
        try {
            final Claims claims = jwtParser(token);
            System.out.println(claims);
            System.out.println("发证时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
            System.out.println("过期时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getExpiration()));//过期时间
            System.out.println(claims.get("age"));
            System.out.println(claims.get("name"));
            final long issuedAt = claims.getIssuedAt().getTime();
            final long expiration = claims.getExpiration().getTime();
            final long result = (expiration - issuedAt) / (60000);// 1000 * 60
            System.out.println(result);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("签名已失效");
        }
    }

    //jwt令牌-生成
    private final static void exampleCreate(){
        final HashMap<String,Object> maps = new HashMap<>();
        maps.put("age",18);
        maps.put("name","田应平");
        final String token = jwtBuilder("id85225851257","admin",maps);
        System.out.println(token);
    }
}