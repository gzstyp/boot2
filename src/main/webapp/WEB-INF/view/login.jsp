<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="utf-8" />
	<title>${platformName}</title>
	<link rel="shortcut icon" href="images/favicon.ico" />
   	<link rel="stylesheet" type="text/css" href="system/css/core/page.core.login.css" />
   	<!-- 背景图片切换效果样式 -->
   	<link rel="stylesheet" type="text/css" href="home/css/supersized.css">
   	<!-- 插件库layui样式css引入 -->
   	<link rel="stylesheet" type="text/css" href="lib/layui/css/layui.css" media="all">
   	<!-- 插件库layer样式css引入 -->
	<link rel="stylesheet" type="text/css" href="lib/layer/skin/default/layer.css"/>
	</head>
	<body onload="parent.layer.closeAll();">
		<div id="page-index">
	  		<span class="title">${platformName}</span>
	  		<img class="img-left" src="images/home/file-faq.png" height="60" width="60"/>
	  		<img  class="img-right" src="images/home/login_lock.png" height="60" width="60"/>
	  		<div class="index-uname">
	  			<span class="span_display" >账号</span>
	  			<input type="text" class="login-input userName_pwd" autocomplete="off" style="width: 200px;" id="uname" maxlength="64" placeholder="请输入登录账号"/>
	  		</div>
	  		<div class="index-upwd">
	  			<span class="span_display">密码</span>
	  			<input type="password" class="login-input userName_pwd" onpaste="return false" oncontextmenu="return false" oncopy="return false" oncut="return false" style="width: 200px;" id="upwd" maxlength="64" placeholder="请输入账号密码"/>
	  		</div>
	  		<div class="index-code">
	  			<span class="span_display">验证码</span>
                <input type="text" class="login-input" style="width: 116px;position: relative;left:60px;top:-24px;" id="code" maxlength="10" placeholder="输入图形验证码"/>
                <img class="login-input" height="29" width="80" style="width:80px;position: relative;border:0px;height:36px;left:60px;top:-25px; cursor: pointer;" src="imgCode" id="imgCode" title="点击刷新验证码" />
	  		</div>
	  		<button id="button-login" class="btn-success layui-btn layui-btn-small layui-btn-normal" type="button">登录</button>
	  		<button id="button-reset" class="btn-danger layui-btn layui-btn-small layui-btn-danger" type="button">重置</button>
	  		<p>Copyright &copy; 2016-2026.服务台 www.fwtai.com</p>
	  	</div>
        <script type="text/javascript" src="lib/easyui/jquery.min.js"></script>
        <!-- 插件库layer样式js引入 -->
        <script type="text/javascript" src="lib/layer/layer.js"></script>
        <!-- 背景图片切换效果 -->
        <script type="text/javascript" src="home/js/supersized.3.2.7.min.js"></script>
        <script type="text/javascript" src="home/js/supersized-init.js"></script>
        <script type="text/javascript" src="lib/jquery.placeholder.min.js"></script>
        <script type="text/javascript" src="lib/page.common.js"></script>
        <script type="text/javascript" src="system/js/core/page.core.login.js"></script>
        <script type="text/javascript">
            layer.ready(function(){
                parent.layer.msg('正在加载,请稍候……',{icon:16,area:'auto',time:-1,shade:[0.3,'#000']});
            });
        </script>
	</body>
</html>