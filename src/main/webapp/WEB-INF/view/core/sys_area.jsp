<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<body>
<style type="text/css">
<!--
	a {
		font-size: 12px;
		color: #3b8cff;
		outline:none;
	}
	a:link {
		text-decoration: none;
	}
	a:visited {
		text-decoration: none;
		color: #3b8cff;
	}
	a:hover {
		text-decoration: none;
		color: #3b8cff;
	}
	a:active {
		text-decoration: none;
		color: #3b8cff;
	}
	a.show{
		color: #444;
	}
-->
</style>
<div fit="true" class="easyui-panel" border="false" style="padding:0px;">
	<div id="toolbar_sys_area" class="authClassToolbar" style="padding-top:2px;height:auto;">
		<table border="0">
			<tr>
				<td><input type="text" id='key_work' class="h24px w160px" style="width:150px;height:26px;line-height:26px;" placeholder="输入当前父级区域关键字" title="输入搜索关键字"/></td>
				<td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.search();">搜索</button></td>
				<td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.edit();">添加</button></td>
			</tr>
		</table>
	</div>
	<table class="show" width="100%" style="margin-top:2px;font-size:12px;">
		<tbody>
			<tr>
				<td><a rel="0" title="所有省及直辖市" id="currentId" href="javascript:thisPage.next(0,'全国');">全国</a><a rel="全国" id="currentName" href="javascript:" style="display:none;"></a></td>
			</tr>
		</tbody>
	</table>
	<table class="view" id="table_show_area" width="100%" style="margin-top:5px;font-size:12px;">
		<thead>
			<tr><td>名称</td><td>全称</td><td>操作</td></tr>
		</thead>
		<tbody>
		<tr><td colspan="3">正在加载数据……</td></tr>
		</tbody>
	</table>
</div>
<input type="hidden" id="sys_area_edit_keyId"/><!--需修改时的keyId-->
<!-- 添加|编辑 -->
<div id='sys_area_edit_dialog' style="display:none;">
	<table cellspacing="4" cellpadding="0" class="show" width="100%" border="0" style="margin-top:6px;font-size:12px;">
		<tr>
			<td style="text-align:right;padding-right:4px;">名称</td>
			<td style="text-align:left;"><input type="text" id='name' class="h24px w160px" placeholder="输入名称" title="输入名称"/><span class="required">*</span></td>
		</tr>
		<tr>
			<td style="text-align:right;padding-right:4px;">全称</td>
			<td style="text-align:left;"><input type="text" id='full_name' class="h24px w160px" placeholder="输入全称" title="输入全称"/><span class="required">*</span></td>
		</tr>
		<tr>
			<td style="text-align:right;padding-right:4px;">排序</td>
			<td style="text-align:left;"><input type="text" id='orders' onkeyup="value=value.replace(/[^0-9]/g,'');" onafterpaste="this.value=this.value.replace(/\D/g,'');" class="h24px w160px" placeholder="输入排序" title="输入排序"/></td>
		</tr>
	</table>
</div>
<script type="text/javascript">
    ;(function ($) {
        var uri = 'area/';/*请求controller层的url*/
		var pIds = [0];
		var pNames = ['全国'];
		thisPage = {
			toolbar : '#toolbar_sys_area',/*toolbar的id标识*/
			edit_keyId : '#sys_area_edit_keyId',/*修改时的keyId标识*/
			dialog_edit : '#sys_area_edit_dialog',/*编辑或添加容器id标识*/
			pId : '#currentId',/*当前的父级id*/
			pName : '#currentName',/*当前的父级名称id*/
			urlAdd : uri+'add',/*添加时提交的url*/
			urlEdit : uri+'edit',/*编辑时提交的url*/
			urlQueryById : uri+'queryById',/*根据id查询全字段数据的url*/
			urlDelById : uri+'delById',/*单条删除的url*/
			urlGetData : uri+'getData',
			init : function(){
				initAnthDom();
				thisPage.next(0,'全国');
				winFn.iePlaceholder();
			},
			/**上一个;上一页*/
			prev : function(){
				pIds.pop();
				pNames.pop();
				var parent = pIds[(pIds.length-1)];
				var pName = pNames[(pNames.length-1)];
				queryData(parent);
				$(thisPage.pId).attr('rel',parent);
				$(thisPage.pName).attr('rel',pName);
			},
			/**下一个;下一页*/
			next : function(parent,name){
				if(parent != '0'){pIds.push(parent);pNames.push(name);}
				if(parent == '0'){pIds.splice(1,pIds.length);pNames.splice(1,pNames.length);}
				queryData(parent);
				$(thisPage.pId).attr('rel',parent);
				$(thisPage.pName).attr('rel',name);
			},
			/**搜索*/
			search : function(){
				var parent = $(thisPage.pId).attr('rel');
				layerFn.ajaxHint(thisPage.urlGetData,'正在读取数据……',{key_work:winFn.getDomValue('#key_work'),parent:parent},function(data){
					if(data.code === AppKey.code.code200){
						showData(data.listData,parent);
					}else{
						showEmpty(data.code,parent);
					}
				});
			},
			/**type=1编辑;2添加;其他未知;*/
			edit : function(type,id){
				var current = $(thisPage.pName).attr('rel');
				var title = type === 1 ?("父级区域[<span style='color:#3b8cff;'>"+current+"</span>]编辑"):("父级区域[<span style='color:#3b8cff;'>"+current+"</span>]添加");
				type = type === 1 ? 1 : 2;//1编辑;2添加;其他未知
				if (type === 1){
					winFn.setDomValue(thisPage.edit_keyId,id);
					euiFn.queryRowDataById(thisPage.urlQueryById,id,function(map){
						openDialog(type,title,map);
					});
				} else {
					winFn.setDomValue(thisPage.edit_keyId,'');
					openDialog(type,title,null);
				}
			},
			/**删除行*/
			delById : function(id){
				layerFn.confirm('删除后将无法恢复,确定吗?',function(){
					layerFn.submit(thisPage.urlDelById,{id:id},function(data){
						euiFn.showRb(data.code,data.msg);
						refresh();
					});
				});
			},
		};
		thisPage.init();
		/**打开提交对话框,含编辑及添加*/
		function openDialog(type,title,map){
			layerFn.addOrEdit(title,thisPage.dialog_edit,['430px','200px'],function(index){
				if(verifyFn.inputRequired('#name')){//验证是否已输入
					layerFn.center(AppKey.code.code201,'请输入名称');
					return;
				}
				if(verifyFn.inputRequired('#full_name')){//验证是否已输入
					layerFn.center(AppKey.code.code201,'请输入全称');
					return;
				}
				var orders = winFn.getDomValue('#orders');
				if (!verifyFn.isEmpty(orders)) {
					if(!verifyFn.checkIsNum(orders)){
						layerFn.center(AppKey.code.code201,'排序值必须是正整数');
						return;
					}
				}
				var params = {
					type : type,
					id : winFn.getDomValue(thisPage.edit_keyId),
					name : winFn.getDomValue('#name'),
					full_name : winFn.getDomValue('#full_name'),
					orders : orders,
					parent : $(thisPage.pId).attr('rel'),
				};
				commit(type,index,params);
			});
			winFn.clearFormData(thisPage.dialog_edit);//清空表单
			if (map != null && map !=''){
				winFn.setDomValue('#name',map.name);
				winFn.setDomValue('#full_name',map.full_name);
				winFn.setDomValue('#orders',map.orders);
			}
		}
		/**1编辑或2添加的提交*/
		function commit(type,index,params){
			var url = thisPage.urlAdd;
			if(type === 1){
				url = thisPage.urlEdit;
			}
			layerFn.submit(url,params,function(data){
				layerFn.layerClose(index);
				euiFn.showRb(data.code,data.msg);
				refresh();
			});
		}
		function refresh(){
			queryData($(thisPage.pId).attr('rel'));
		}
		/**数据为空或其他情况时表格拼接数据的处理*/
		function showEmpty(code,parent){
			var html = "";
			if(code === AppKey.code.code201){
				html = "<tr><td colspan='3'>暂无数据</td></tr>";
				if(parent == '0'){
					html += "<tr><td colspan='3'><a href='javascript:thisPage.edit();'>添加</a></td></tr>";
				}else{
					html += "<tr><td colspan='3'><a href='javascript:thisPage.edit();'>添加</a><span style='margin-left:4px;margin-right:4px;display:inline-block;'>|</span><a href='javascript:thisPage.prev();'>返回</a></td></tr>";
				}
			}else if(code === AppKey.code.code204){
				html = "<tr><td colspan='3'>系统异常</td></tr>";
			}
			createHtml(html);
		}
		/**上一页、下一页分开来写,思路更为清晰!*/
		function queryData(parent){
			layerFn.ajaxHint(thisPage.urlGetData,'正在读取数据……',{parent:parent},function(data){
				if(data.code === AppKey.code.code200){
					showData(data.listData,parent);
				}else{
					showEmpty(data.code,parent);
				}
			});
		}
		/**采用原生态表格拼接显示数据*/
		function showData(data,parent){
			var html = "";
			for( var i = 0; i < data.length; i++){
				html += "<tr ondblclick='thisPage.edit(1,\""+data[i].id+"\");'>";
				html += "<td><a class='show' title='查看区域' href='javascript:thisPage.next("+data[i].id+",\"" + data[i].name + "\");'>" + data[i].name + "</a></td>";
				html += "<td><a class='show' title='查看区域' href='javascript:thisPage.next("+data[i].id+",\"" + data[i].name + "\");'>" + data[i].full_name + "</a></td>";
				html += "<td><a title='查看区域' href='javascript:thisPage.next("+data[i].id+",\"" + data[i].name + "\");'>查看</a><span style='margin-left:4px;margin-right:4px;display:inline-block;'>|</span><a href='javascript:thisPage.edit(1,"+data[i].id+");'>编辑</a><span style='margin-left:4px;margin-right:4px;display:inline-block;'>|</span><a href='javascript:thisPage.delById("+data[i].id+");'>删除</a></td>";
				html += "</tr>";
				if((data.length == (i+1))){
					if(parent == '0'){
						html += "<tr><td colspan='3'><a href='javascript:thisPage.edit();'>添加</a></td></tr>";
					}else{
						html += "<tr><td colspan='3'><a href='javascript:thisPage.edit();'>添加</a><span style='margin-left:4px;margin-right:4px;display:inline-block;'>|</span><a href='javascript:thisPage.prev();'>返回</a></td></tr>";
					}
				}
			}
			createHtml(html);
		}
		/**采用原生态表格显示数据*/
		function createHtml(html){
			$("#table_show_area tbody").html(html);
			winFn.tableMouse('view');
		}
		/**初始化权限按钮*/
		function initAnthDom(){
			authBtn('${authBtnId}');
		}
	})(jQuery);
</script>
</body>