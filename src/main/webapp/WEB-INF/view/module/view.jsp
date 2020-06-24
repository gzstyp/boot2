<%@ page language="java" pageEncoding="UTF-8"%>
<%
final String path = request.getContextPath();
final String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>表单</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" type="text/css" href="css/table.view.css" media="screen"/>
	<link rel="stylesheet" type="text/css" href="system/css/core/page.core.control.css" media="screen"/>
  </head>
  <body>
    <form action="">
    	<table cellspacing="0" cellpadding="0" class="view" width="98%" style="margin-left:10px;margin-right:10px;margin-top:10px;">
			<thead>
				<tr><td>编号</td><td>名字</td><td>性别</td></tr>
			</thead>
			<tbody>
				<tr><td>1001</td><td>孙悟空</td><td>男</td></tr>
				<tr><td>1003</td><td>花千骨</td><td>女</td></tr>
				<tr><td>1005</td><td>秦始皇</td><td><a href="javascript:thisPage.msg('男');">男</a></td></tr>
				<tr><td>1006</td><td>武则天</td><td><a href="javascript:thisPage.msg('女');">女</a></td></tr>
			</tbody>
		</table>
    </form>
     <script type="text/javascript" src="lib/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="lib/layer/layer.js"></script>
    <script type="text/javascript" src="lib/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="lib/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="lib/page.common.js"></script>
    <script type="text/javascript">
        ;(function () {
            thisPage = {
	    		init : function(){
	    		},
	    		tips : function(dom){
	    			layerFn.tips(dom,'那个贱货是女的');
	    		},
	    		msg : function(msg){
	    			layerFn.center(AppKey.code.code200,msg);
	    		},
	    	};
	    	thisPage.init();
	    })(jQuery);
    </script>
  </body>
</html>