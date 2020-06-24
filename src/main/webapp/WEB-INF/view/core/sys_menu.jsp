<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<body>
<div fit="true" class="easyui-panel" border="false" style="padding:0px;">
	<div id="toolbar_sys_menu" class="authClassToolbar" style="padding-top:4px; padding-bottom:4px;height:auto;">
		<table border="0">
			<tr>
				<td><input type="text" id='menu_key' class="h24px w200px" style="width:150px;height:26px;line-height:26px;" placeholder="菜单名称" title="菜单名称"/></td>
				<td style="text-align:left;" title="选择菜单类型" id="menu_search_td_type">
					<select class="ui-select">
						<option value="">菜单类型</option>
						<option value="1">导航菜单</option>
						<option value="2">普通按钮</option>
						<option value="3">行按钮</option>
					</select>
					<input type="hidden" id="menu_search_key_type" />
				</td>
				<td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.search();">搜索</button></td>
				<td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.edit();">添加</button></td>
			</tr>
		</table>
	</div>
	<table id="datagrid_sys_menu" border="0"></table>
</div>
<input type="hidden" id="menu_edit_keyId"/><!-- 需修改时的keyId -->
<!-- 添加|编辑菜单 -->
<div id='sys_menu_edit' class="divLayer">
	<input type="hidden" id="sys_menu_select_type"/>
	<input type="hidden" id="sys_menu_layer"/>
	<table cellspacing="0" class="view" cellpadding="0" width="100%" border="0">
		<tr>
			<td style="text-align:right;padding-right:4px;">菜单名称</td><td style="text-align:left;"><input type="text" id='menu_name' class="h24px w200px" placeholder="输入菜单名称" title="输入菜单名称"/><span class="required">*</span></td>
		</tr>
		<tr>
			<td width="190" style="text-align:right;padding-right:4px;">菜单类型</td>
			<td style="text-align:left;" title="选择菜单类型" id="menu_table_tr_td_type">
				<select class="ui-select">
					<option value="">选择类型</option>
					<option value="1">导航菜单</option>
					<option value="2">普通按钮</option>
					<option value="3">行按钮</option>
				</select>
			</td>
		</tr>
		<tr>
			<td style="text-align:right;padding-right:4px;">父级菜单节点</td>
			<td style="text-align:left;">
				<input type="text" id='pName' class="h24px w200px" placeholder="无父级菜单节点" disabled="disabled"/>
				<input type="hidden" id="menu_pId" value="0" />
				<a href="javascript:thisPage.clearPid();" style="font-size:12px;" title="清除和删除父级菜单">删除父级菜单</a>
			</td>
		</tr>
		<tr>
			<td style="text-align:right;padding-right:4px;">选择父级菜单</td>
			<td>
				<div style="margin-left:-6px;margin-top:-6px;">
					<ul id="sys_edit_menu" class="ztree"></ul>
				</div>
			</td>
		</tr>
		<tr>
			<td style="text-align:right;padding-right:4px;">菜单是否还有子页面</td>
			<td style="text-align:left;" title="选择该菜单是否还有子页面" id="menu_table_tr_td_isParent">
				<select class="ui-select">
					<option value="">菜单选项</option>
					<option value="1">有子页面</option>
					<option value="0">无子页面</option>
				</select>
				<input type="hidden" id="isParent"/>
			</td>
		</tr>
		<tr>
			<td style="text-align:right;padding-right:4px;">菜单url路径</td><td style="text-align:left;"><input type="text" id='menu_uri' class="h24px w200px" placeholder="输入菜单url访问路径" title="当菜单类型为导航菜单且为空时说明还有子页面"/></td>
		</tr>
	</table>
</div>
<script type="text/javascript">
    ;(function ($) {
        var domTree = 'sys_edit_menu';
		var uri = 'menu/';/*请求controller层的url*/
		var setting = {
			view:{
				expandSpeed: 100,
				showLine : true,
				checked : true,
				showIcon : false,
				fontCss : {"color":"#000"}
			},
			async : {
				enable : true,
				url : "menu/queryAllMenu",
				cache : false,
				type: "POST",
				autoParam : ["id"],
				dataFilter: function(treeId,parentNode,result){
					result = layerFn.checkLogin(result);
					if (result.code == AppKey.code.code200)return result.listData;
						return '';
				}
			},
			callback : {
				beforeAsync : function(){
				},
				onAsyncSuccess : function(data){
				},
				onAsyncError : function(){
					layerFn.alert(AppKey.msg_error);
				},
				onClick : function(event,treeIdDom,node,clickFlag){
					if (verifyFn.inputRequired('#menu_name')){
						layerFn.center(AppKey.code.code201,'请输入菜单名称');
						return;
					}
					if (verifyFn.inputRequired('#sys_menu_select_type')){
						layerFn.center(AppKey.code.code201,'请先选择菜单类型');
						return;
					}
					var type = winFn.getDomValue('#sys_menu_select_type');
					if (type != '1' && verifyFn.isEmpty(node.uri)){//选择下拉类型不为导航菜单时,那必须判断所选的父级菜单的uri是否为空,为空时说明还有子页面,所以不能添加行按钮或普通按钮
						thisPage.checkMenuTree();
						return;
					}
					if (!verifyFn.isEmpty(node.uri) && type == 1){//选择下拉类型为导航菜单时,那必须判断所选的父级菜单的uri是否不为空,不为空说明只能添加行按钮或普通按钮
						thisPage.checkMenuTree();
						return;
					}
					var keyId = winFn.getDomValue('#menu_edit_keyId');
					if (!verifyFn.isEmpty(keyId) && node.id == keyId){//判断所选的目标导航菜单和当前的菜单是同一个菜单
						thisPage.checkMenuTree();
						return;
					}
					winFn.setDomValue('#pName',node.name);
					winFn.setDomValue('#menu_pId',node.id);
					winFn.setDomValue('#sys_menu_layer',node.layer);

				}
			}
		};
		thisPage = {
			datagrid : '#datagrid_sys_menu',/*datagrid元素*/
			toolbar : '#toolbar_sys_menu',/*toolbar元素*/
			sys_menu_edit : '#sys_menu_edit',/*菜单的编辑或添加容器id*/
			menu_type : '#menu_table_tr_td_type',/*导航菜单类型的父节点*/
			menu_isParent : '#menu_table_tr_td_isParent',/*父级的元素的id节点*/
			search_key_type : '#menu_search_key_type',/*搜索框的菜单类型隐藏域的id节点*/
			parent_type : '#menu_search_td_type',/*搜索框的搜索菜单类型父级id节点*/
			urlList : uri+'listData',/*获取datagrid数据的url*/
			urlAdd : uri+'add',/*添加时提交的url*/
			urlEdit : uri+'edit',/*编辑时提交的url*/
			urlDelById : uri+'delById',/*单条删除的url*/
			urlQueryById : uri+'queryById',/*根据id查询全字段数据的url*/
			init : function(){
				thisPage.initAnthDom();
				thisPage.initDom();
				thisPage.initDatagrid();
				winFn.iePlaceholder();
			},
			initDom : function(){
				winFn.uiSelectInit(thisPage.parent_type,function(value){
					$(thisPage.search_key_type).val(value);
				},'108px');
			},
			initAnthDom : function(){
				authBtn('${authBtnId}');
			},
			checkMenuTree : function(){
				winFn.setDomValue('#menu_pId','0');
				winFn.setDomValue('#pName','');
				winFn.setDomValue('#sys_menu_layer',1);
				layerFn.center(AppKey.code.code201,'该节点不是有效的导航菜单');
			},
			initDatagrid : function(){
				var _self = this;
				$(_self.datagrid).datagrid({
					fit : true,
					url : _self.urlList,
					pageSize : euiFn.datagrid.settings.pageSize,
					pageList : euiFn.datagrid.settings.pageList,
					checkOnSelect : false,
					pagination : true,
					fitColumns : true,
					striped : true,
					autoRowHeight : false,
					loadMsg : AppKey.loadMsg,
					toolbar : _self.toolbar,
					singleSelect : true,
					idField : 'id',
					onBeforeLoad : function(param){
					},
					onLoadSuccess:function(data){
					},
					onLoadError:function(){
						layerFn.alert(AppKey.msg_error);
					},
					onDblClickRow:function(index,row){
						if('${double_click}' != ''){
							thisPage.edit(1,row.id);
						}
					},
					loadFilter : function(data){
						return winFn.dataFilter(data);
					},
					columns : [[
						{field:'name',title:'菜单名称',width:65,align:'left',
							formatter:function(value,rowData,index){
								return '<span title='+value+'>'+value+'</span>';
							}
						},
						{field:'pName',title:'所属父级菜单',width:65,align:'left',
							formatter:function(value,rowData,index){
								if (value == null || value == ''){
									return '<span title='+rowData.name+'>'+rowData.name+'</span>';
								}else{
									return '<span title='+value+'>'+value+'</span>';
								}
							}
						},
						{field:'layer',title:'菜单级别',width:22,align:'left',
							formatter:function(value,rowData,index){
								var html = "";
								if (value == 1) {
									html = "一级菜单";
								}else if (value == 2){
									html = "二级菜单";
								}else if (value == 3){
									html = "三级菜单";
								}else if (value == 4){
									html = "四级菜单";
								}else if (value == 5){
									html = "五级菜单";
								}
								return html;
							}
						},
						{field:'type',title:'菜单类型',width:22,align:'left',
							formatter:function(value,rowData,index){
								var html = "";
								if (value == 1){
									html = "导航菜单";
								}else if (value == 2){
									html = "普通按钮";
								}else if (value == 3){
									html = "行按钮";
								}
								return html;
							}
						},
						{field:'uri',title:'访问路径',width:142,align:'left',hidden:('${login_user}'=='admin'?false:true)},
						{field:'_id_',title:'操作',width:24,align:'left',hidden:('${handle_row}'==''?true:false),
							formatter:function(value,rowData,index){
								return '${row_click}';
							}
						}
					]]
				});
			},
			search : function(){
				var type = winFn.getDomValue(thisPage.search_key_type);
				var key = winFn.getDomValue('#menu_key');
				euiFn.refreshDatagrid(thisPage.datagrid,{type:type,key:key});
			},
			/**1是编辑 ;否则添加;*/
			edit : function(type,id){
				var title = type === 1 ?"编辑菜单":"添加菜单";
				type = type === 1 ? 1 : 2;//1编辑;2添加;其他是未知
				if (type === 1){
					winFn.setDomValue('#menu_edit_keyId',id);
					euiFn.queryRowDataById(thisPage.urlQueryById,id,function(map){
						thisPage.openDialog(type,title,map);
					});
				} else {
					winFn.setDomValue('#menu_edit_keyId', '');
					thisPage.openDialog(type,title,null);
				}
			},
			/**打开提交对话框,含编辑及添加*/
			openDialog : function(type,title,map){
				layerFn.addOrEdit(title,thisPage.sys_menu_edit,['600px','550px'],function(_index){
					if (verifyFn.inputRequired('#menu_name')){//验证是否已输入菜单的名称
						layerFn.center(AppKey.code.code201,'请输入菜单名称');
						return;
					}
					if (verifyFn.inputRequired('#sys_menu_select_type')){//验证是否已选择下拉菜单
						layerFn.center(AppKey.code.code201,'请选择菜单类型');
						return;
					}
					var select_type = winFn.getDomValue('#sys_menu_select_type');
					var treeObj = $.fn.zTree.getZTreeObj(domTree);//获取已选中的节点的所有数据及数值1
					var data = treeObj.getSelectedNodes();//获取已选中的节点的所有数据及数值2
					//验证已选择的下拉菜单和父级菜单是不是相吻合!
					if (!verifyFn.isEmpty(data) && !verifyFn.isEmpty(data[0].uri) && select_type == '1'){
						winFn.setDomValue('#menu_pId','0');
						winFn.setDomValue('#pName','');
						layerFn.center(AppKey.code.code201,'该节点不是有效的导航菜单');
						return;
					}
					var pId = winFn.getDomValue('#menu_pId');
					if (pId == '0'){//验证已选择的下拉菜单和父级菜单是不是相吻合;验证当下拉菜单不为导航菜单时,那必须选择父级菜单,也可以采用逆向思维,也就是说当你选择为普通按钮或行按钮时那必须父级菜单!当也必须添加菜单url地址
						if(select_type == '2' || select_type == '3') {
							layerFn.center(AppKey.code.code201,'您没有选择父级菜单');
							return;
						}
					}
					if (select_type != '1' && verifyFn.inputRequired('#menu_uri')){//验证当下拉菜单不为导航菜单时,必须添加菜单url地址
						layerFn.center(AppKey.code.code201,'请输入菜单url路径');
						return;
					}
					var layer = 1;
					if (!verifyFn.isEmpty(data)){
						layer = parseInt(data[0].layer);
						var layerValue = (parseInt(data[0].layer)) + 1;//得到当前url菜单路径的level值
						if (layerValue > 4 && select_type == '1'){
							layerFn.center(AppKey.code.code201,'菜单已超过5级菜单');
							return;
						}
					}
					if (type === 1 && verifyFn.inputRequired('#menu_edit_keyId')){
						layerFn.center(AppKey.code.code204,'获取编辑的数据失败');
						return;
					}
					var uri = winFn.getDomValue('#menu_uri');
					var isParent = winFn.getDomValue('#isParent');
					if (select_type == 1 && verifyFn.isEmpty(uri)){
						if(verifyFn.isEmpty(isParent)){
							layerFn.center(AppKey.code.code199,'请选择该菜单是否还有子页面');
							return;
						}
					}
					if (pId == '0' && select_type != 1){
						var msg = select_type == '2' ?'普通按钮':'行按钮';
						layerFn.center(AppKey.code.code199,msg+'不能当导航菜单使用');
						return;
					}
					if (select_type == 1){
						if (isParent == 0){
							if (verifyFn.inputRequired('#menu_uri')){//验证是否已选择下拉菜单
								layerFn.center(AppKey.code.code201,'请输入菜单url路径');
								return;
							}
						}
						if (isParent == 1){
							uri = null;
						}
					}
					if (verifyFn.isEmpty(isParent)){
						isParent = 0;
					}
					var params = {
						name : winFn.getDomValue('#menu_name'),
						type : select_type,
						pId : pId,
						uri : uri,
						id : winFn.getDomValue('#menu_edit_keyId'),
						isParent : isParent,
						layer : layer
					};
					thisPage.commit(type,_index,params);
				},'<span style="color:#ff5722;">重置</span>',function(){
					thisPage.resetFormMenu();
					winFn.setDomValue('#sys_menu_select_type','');
					winFn.setDomValue('#sys_menu_layer','1');
					$.fn.zTree.getZTreeObj(domTree).expandAll(false);//全部折叠
				});
				$.fn.zTree.init($("#"+domTree),setting);//初始化树形菜单及并读取数据
				winFn.uiSelectInit(thisPage.menu_type,function(value){//初始化菜单类型下拉列表
					$('#sys_menu_select_type').val(value);
				});
				winFn.uiSelectInit(thisPage.menu_isParent,function(value){//初始化是否是父节点是isParent下拉列表
					$('#isParent').val(value);
				});
				thisPage.resetFormMenu();
				if (map != null && map !=''){
					var mapType = map.type;
					winFn.setDomValue('#menu_name',map.name);
					winFn.setDomValue('#sys_menu_select_type',mapType);
					winFn.setDomValue('#pName',map.pName);
					winFn.setDomValue('#menu_pId',map.pId);
					winFn.setDomValue('#menu_uri',map.uri);
					winFn.setDomValue('#sys_menu_layer',map.layer);
					var selectOfType = '选择类型';
					if(mapType == '1'){
						selectOfType = '导航菜单';
					}else if (mapType == '2'){
						selectOfType = '普通按钮';
					}else if (mapType == '3'){
						selectOfType = '行按钮';
					}
					//给下拉列表-菜单类型赋值
					winFn.uiSelectSetValue(thisPage.menu_type,selectOfType,mapType,function(){
						$('#sys_menu_select_type').val(mapType);
					});
					//判断是导航菜单,还是按钮或行按钮;1导航菜单;否则是按钮或行按钮
					if (map.type != '1'){
						winFn.uiSelectReset(thisPage.menu_isParent,'菜单选项',function(){
							$('#isParent').val('');
						});
					}else{
						var textParent = "";
						var ip = '' ;
						if (verifyFn.isEmpty(map.uri) && map.isParent == true){
							textParent = '有子页面';
							ip = 1 ;
						} else {
							textParent = '无子页面';
							ip = 0 ;
						}
						//给下拉列表-菜单选项赋值
						winFn.uiSelectSetValue(thisPage.menu_isParent,textParent,ip,function(){
							$('#isParent').val(ip);
						});
					}
					if (map.type != '1'){
						//禁用下拉列表-菜单选项
						winFn.uiSelectDisabled(thisPage.menu_isParent,1,function(){
							$('#menu_table_tr_td_isParent').attr("title","菜单类型为"+selectOfType+"时选项不可用");
						});
					}else {
						//启用下拉列表-菜单选项
						winFn.uiSelectDisabled(thisPage.menu_isParent,0,function(){
							$('#menu_table_tr_td_isParent').attr("title","选择该菜单是否还有子页面");
						});
					}
				}else{
					//给下拉列表-菜单类型赋值
					winFn.uiSelectSetValue(thisPage.menu_type,'选择类型','',function(){
						$('#sys_menu_select_type').val('');
					});
					//给下拉列表-菜单选项赋值
					winFn.uiSelectSetValue(thisPage.menu_isParent,'菜单选项','',function(){
						$('#isParent').val('');
					});
					//启用下拉列表-菜单选项
					winFn.uiSelectDisabled(thisPage.menu_isParent,0,function(){
						$('#menu_table_tr_td_isParent').attr("title","选择该菜单是否还有子页面");
					});
					winFn.setDomValue('#sys_menu_layer','1');
				}
			},
			clearPid : function(){
				winFn.setDomValue('#menu_pId','0');
				winFn.setDomValue('#pName','');
				$.fn.zTree.getZTreeObj(domTree).expandAll(false);//全部折叠
			},
			resetFormMenu : function(){
				winFn.clearFormData(thisPage.sys_menu_edit);//清空表单
				winFn.setDomValue('#menu_pId','0');//重置时把pId设置为0,最顶级菜单
				//清空下拉列表
				winFn.uiSelectReset(thisPage.menu_type,'选择类型',function(){
					$('#sys_menu_select_type').val('');
				});
				//清空下拉列表
				winFn.uiSelectReset(thisPage.menu_isParent,'菜单选项',function(){
					$('#isParent').val('');
				});
			},
			/**删除行*/
			delById : function(id){
				layerFn.handleVerify('删除之后,与之关联的子菜单、按钮、角色菜单、账号私有菜单都将会被删除且无法恢复,确定吗?',function(){
					layerFn.submit(thisPage.urlDelById,{id:id},function(data){
						euiFn.showRb(data.code,'操作成功');
                        thisPage.search();
					});
				});
			},
			commit : function(type,index,params){
				var url = thisPage.urlAdd;
				if (type == 1){
					url = thisPage.urlEdit;
				}
				layerFn.submit(url,params,function(data){
					layerFn.layerClose(index);//成功时才关闭对话框
					euiFn.showRb(data.code,'操作成功');
					euiFn.refreshPage();//刷新整个页面
				});
			}
		};
		thisPage.init();
	})(jQuery);
</script>
</body>