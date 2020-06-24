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
   	<table cellspacing="0" cellpadding="0" class="view" width="98%" style="margin-left:10px;margin-right:10px;margin-top:10px;">
		<thead>
			<tr><td>编号</td><td>名字</td><td>性别</td></tr>
		</thead>
		<tbody>
			<tr><td>1001</td><td>孙悟空</td><td>男0</td></tr>
			<tr><td>1003</td><td>花千骨</td><td>0女</td></tr>
			<tr><td>1005</td><td>秦始皇</td><td><a href="javascript:thisPage.msg('男');">男</a></td></tr>
			<tr><td>1006</td><td>武则天</td><td><a class='tips' href="javascript:thisPage.tips('.tips');">女</a></td></tr>
		</tbody>
	</table>
	<table cellspacing="0" cellpadding="0" class="view" width="98%" style="margin-left:10px;margin-right:10px;margin-top:10px;">
		<tbody>
			<tr><td>账号:</td><td><input type="text" name='name' id='name' style="height:28px;line-height:28px;text-align:left;"/></td></tr>
			<tr><td>密码:</td><td><input type="text" name='pwd' id='pwd' style="height:28px;line-height:28px;text-align:left;"/></td></tr>
		</tbody>
	</table>
    <script type="text/javascript" src="lib/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="lib/layer/layer.js"></script>
    <script type="text/javascript" src="lib/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="lib/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="lib/page.common.js"></script>
    <script type="text/javascript">
        (function () {
            thisPage = {
	    		init : function(){
	    		},
	    		initAnthDom : function(){
	    		},
	    		addEvent : function(){
	    		},
	    		tips : function(dom){
	    			layerFn.tips(dom,'那个贱货是女的');
	    		},
	    		msg : function(msg){
	    			layerFn.center(AppKey.code.code200,msg);
	    		},
	    		submit : function(callBak){
	    		    if(verifyFn.inputRequired('#name')){
	       				layerFn.center(AppKey.code.code199,'哥,账号不能为空哦');
	       				return;
	       			}
	    			if (verifyFn.inputRequired('#pwd')){
	       				layerFn.tips('#pwd','哥,请输入密码!');
	       				return;
	       			}
	    			params = {
	    				name : $('#name').val(),
	    				pwd : $('#pwd').val()
	    			};
	    			layerFn.submit('form',params,callBak);
	    		}
	    	};
	    	thisPage.init();
	    })(jQuery);
    </script>
  </body>
</html>