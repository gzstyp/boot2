<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div region="center" style="overflow: hidden;">
	<div id="bodyContainer" class="easyui-panel" data-options="fit:true,border:false,title:'欢迎使用本系统云计算管理服务平台',loadingMessage:'正在加载页面……',onBeforeLoad:function(){},onLoadError:function(res){if(res.status=='404'){layerFn.center(AppKey.code.code199,'主人,您访问的页面不存在!');}else{layerFn.center(AppKey.code.code202,'抱歉,系统出现错误,代码:'+res.status+','+res.statusText);}}" >
		<div style="height:100%;height:100%;"></div>
	</div>
</div>