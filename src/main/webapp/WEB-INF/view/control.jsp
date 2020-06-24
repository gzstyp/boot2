<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${platformName}</title>
		<link rel="shortcut icon" href="images/favicon.ico" />
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
		<!-- icheck样式库 -->
		<link rel="stylesheet" type="text/css" href="lib/icheck/checkboxRadio.css"/>
		<link rel="stylesheet" type="text/css" href="lib/icheck/blue.css"/>
		<link rel="stylesheet" type="text/css" href="lib/easyui/themes/metro/easyui.css">
		<link rel="stylesheet" type="text/css" href="lib/easyui/themes/icon.css" />
		<link rel="stylesheet" type="text/css" href="system/css/core/page.core.control.css"/>
		<link rel="stylesheet" type="text/css" href="lib/ztree/css/metroStyle/metroStyle.css" media="screen"/>
		<link rel="stylesheet" type="text/css" href="css/select.css" media="screen"/>
		<link rel="stylesheet" type="text/css" href="css/buttons.css" media="screen"/>
		<link rel="stylesheet" type="text/css" href="css/table.view.css" media="screen"/>
		<!-- 下拉菜单样式 -->
		<link rel="stylesheet" type="text/css" href="css/drop-down.css" media="screen"/>
        <style>
            .panel-title,.panel-header{
                height:24px;
                line-height:24px;
                vertical-align: middle;
                font-size:14px;
            }
        </style>
        <script type="text/javascript">
            (function() {
                var OriginTitile = document.title, titleTime;
                document.addEventListener('visibilitychange', function() {
                if (document.hidden) {
                    document.title = '你已离开请注意退出';
                    clearTimeout(titleTime);
                } else {
                    document.title = '很好,欢迎回来!';
                    titleTime = setTimeout(function() {
                        document.title = OriginTitile;
                    },2000);
                    }
                });
            })();
        </script>
	</head>
<body class="easyui-layout">
	<jsp:include page="layout/north.jsp"/>
	<jsp:include page="layout/west.jsp"/>
	<div region="center" style="overflow: hidden;">
		<div id="bodyContainer" class="easyui-panel" data-options="fit:true,border:false,title:'欢迎使用本系统云计算管理服务平台',loadingMessage:'正在加载页面……',onBeforeLoad:function(){},onLoadError:function(res){if(res.status=='404'){layerFn.center(AppKey.code.code199,'主人,您访问的页面不存在!');}else if(res.status=='0'){layerFn.alert('网络中断,连接服务器失败!');}else{layerFn.alert('抱歉,系统出现错误,代码:'+res.status+','+res.statusText);}}" >
			<div style="height:100%;height:100%;"></div>
		</div>
	</div>
	<script type="text/javascript" src="lib/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="ueditor/ueditor.all.js"></script>
	<script type="text/javascript" src="ueditor/lang/zh-cn/zh-cn.js"></script>
	<script type="text/javascript" src="lib/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="lib/layer/layer.js"></script>
	<script type="text/javascript" src="lib/jquery-ui.min.js"></script>
	<script type="text/javascript" src="lib/select-widget-min.js"></script>
	<script type="text/javascript" src="lib/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="lib/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="lib/datagrid.tips.js"></script>
	<script type="text/javascript" src="lib/icheck/icheck.min.js"></script><!-- icheck库 -->
	<script type="text/javascript" src="lib/jquery.placeholder.min.js"></script>
	<script type="text/javascript" src="lib/page.common.js"></script><!-- 自定义封装及插件库 -->
	<script type="text/javascript" src="lib/jquery/select.area.js"></script>
	<script type="text/javascript" src="lib/ztree/js/jquery.ztree.core.min.js"></script>
	<script type="text/javascript" src="system/js/core/page.core.control.js"></script>
	<script type="text/javascript" src="system/js/core/page.core.menu.js"></script>
</body>
</html>