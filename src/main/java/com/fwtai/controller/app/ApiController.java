package com.fwtai.controller.app;

import com.fwtai.controller.BaseController;
import com.fwtai.response.BusinessException;
import com.fwtai.response.EmBusinessError;
import com.fwtai.service.module.ApiService;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 对外公布api调用接口
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2018-01-09 16:33
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@RestController //注意是否需要的是 @Controller
@RequestMapping("/api")
public final class ApiController extends BaseController{

    @Autowired
    private ApiService service;

    // http://192.168.1.102:81/api/read/1
    @GetMapping("/read/{rows}")
    public final void read(@PathVariable Integer rows,final HttpServletResponse response){
        final String json = ToolClient.executeRows(rows);
        ToolClient.responseJson(json,response);
    }

    // http://192.168.1.102:81/api/get
    @GetMapping("/get")
    public final void get(final HttpServletRequest request,final HttpServletResponse response){
        final HashMap<String,String> params = ToolClient.getFormParams(request);
        final String json = ToolClient.queryJson(params);
        ToolClient.responseJson(json,response);
    }

    // http://192.168.1.102:81/api/post
    @PostMapping("/post")
    public final void post(final HttpServletRequest request,final HttpServletResponse response){
        final HashMap<String,String> params = ToolClient.getFormParams(request);
        final String json = ToolClient.queryJson(params);
        ToolClient.responseJson(json,response);
    }

    /**需要的是 @Controller*/
    @GetMapping("/hello")
    public String hello(final Model m){
        m.addAttribute("name", "spring-boot");
        return "hello";
    }

    @GetMapping("/list")
    public String list(final Model model){
        final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>(0);
        for (int i = 0; i < 5; i++) {
            final HashMap<String,String> map = new HashMap<String,String>();
            map.put("name","存储"+i);
            list.add(map);
        }
        model.addAttribute("message","下午好");
        model.addAttribute("list", list);
        return "listUser";
    }

    /**thymeleaf只支持HTML5,需要的是 @Controller*/
    @GetMapping("/tf")
    public String helloHtml(final HashMap<String,Object> map){
        map.put("hello","from ApiController.tf");
        return"tf/hello";
    }

    /**公钥加密-私钥解密*/
    @GetMapping("/publicPrivate")//http://localhost/api/publicPrivate?rows=1
    public final void publicPrivate(final HttpServletRequest request,final HttpServletResponse response){
        final String rows = request.getParameter("rows");
        try {
            final int row = Integer.valueOf(rows);
            final String json = service.publicPrivate(row);
            ToolClient.responseObj(json,response);
        } catch (Exception e) {
            e.printStackTrace();
            ToolClient.responseException(response);
        }
    }

    /**私钥加密-->公钥解密*/
    @GetMapping("/privatePublic")//http://localhost/api/privatePublic?rows=0
    public final void privatePublic(final HttpServletRequest request,final HttpServletResponse response){
        final String rows = request.getParameter("rows");
        try {
            final int row = Integer.valueOf(rows);
            final String json = service.privatePublic(row);
            ToolClient.responseObj(json,response);
        } catch (final Exception e) {
            e.printStackTrace();
            ToolClient.responseException(response);
        }
    }

    @GetMapping("/handlerException")// http://127.0.0.1/api/handlerException?values=hgltzz
    public final void handlerException(final HttpServletRequest request,final HttpServletResponse response) throws BusinessException{
        final String rows = request.getParameter("values");
        HashMap<String,String> formParams = getFormParams();
        //int i = 5/0;//在测试可以把本行注释掉
        HashMap<String,Object> map = null;
        if(map == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        map.put("k",1024);
    }

    // 生成jwt令牌,http://127.0.0.1/api/jwtBuilder?userId=ffffffff911cc575ffffffffffa39dff&userName=admin
    /*
        iss: jwt签发者
        sub: jwt所面向的用户
        aud: 接收jwt的一方
        exp: jwt的过期时间，这个过期时间必须要大于签发时间
        nbf: 定义在什么时间之前，该jwt都是不可用的.
        iat: jwt的签发时间
        jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
     */
    @GetMapping("jwtBuilder")
    public final void jwtBuilder(final HttpServletRequest request,final HttpServletResponse response){
        final String userId = request.getParameter("userId");
        final String userName = request.getParameter("userName");
        /*final JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS256,"yinlz.com")//头部信息,yinlz.com是加密的盐值
            .setId("88888888")//登录者id,载荷部分1
            .setSubject("admin")//登录名|jwt所面向的用户,载荷部分2
            .setIssuedAt(new Date())//登录时间|签发时间,载荷部分3
            .setExpiration(new Date(new Date().getTime() + 1000 * 60 * 2))
            .claim("role","add:del:edit");//添加自定义字段,支持K,V格式数据,比如角色
            //其签名是靠‘头部信息(Header)’和载荷部分(playload)生成的
        final String compact = jwtBuilder.compact();
        ToolClient.responseObj(compact,response);*/
        final String builder = ToolJWT.jwtBuilder(userId,userName);
        ToolClient.responseObj(builder,response);
    }

    //***********************************************ok*********************************************/
    @GetMapping("create") // http://127.0.0.1/api/create
    public final String create(){
        final JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS256,"yinlz.com")//头部信息,yinlz.com是加密的盐值
            .setId("88888888")//登录者id,载荷部分1
            .setSubject("admin")//登录名|jwt所面向的用户,载荷部分2
            .setIssuedAt(new Date())//登录时间|签发时间,载荷部分3
            .setExpiration(new Date(new Date().getTime() + 1000 * 60 * 2))//1000 * 60 * 1 是1分钟
            .claim("role","add:del:edit");//添加自定义字段,支持K,V格式数据,比如角色
        //其签名是靠‘头部信息(Header)’和载荷部分(playload)生成的
        final String compact = jwtBuilder.compact();
        return compact;
    }


    @GetMapping("parserToken") //http://127.0.0.1/api/parserToken?token=22
    public final void parserToken(final String token){
        final Map<String,Object> map = Jwts.parser().setSigningKey("yinlz.com")//指定加密的盐值
            .parseClaimsJws(token)//生成的jwt令牌数据,数据结构是K,V
            .getBody();
        final Claims claims = Jwts.parser().setSigningKey("yinlz.com")//指定加密的盐值
            .parseClaimsJws(token)//生成的jwt令牌数据
            .getBody();// Claims 其实就是个 map 数据结构类型
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getExpiration()));//过期时间
        System.out.println(claims.get("role"));//获取已添加的自定义字段
        System.out.println(map);
    }
    //***********************************************ok*********************************************/

    public final static String requestPost(final String uri,final HashMap<String,String> params){
        try {
            final URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            final StringBuffer sb = new StringBuffer();
            if(params !=null && params.size() > 0){
                for(final String key : params.keySet()){
                    sb.append(key + "=" + params.get(key) + "&");
                }
            }
            conn.getOutputStream().write(String.valueOf(sb).getBytes(Charset.forName("UTF-8")));
            conn.connect();
            final InputStream stream = conn.getInputStream();
            return StreamUtils.copyToString(stream,Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}