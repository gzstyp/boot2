<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<body>
<div fit="true" class="easyui-panel" border="false" style="padding:0px;">
	<div id="toolbar_bs_news" class="authClassToolbar" style="padding-top:2px; padding-bottom:2px;height:auto;">
		<form id='search_bs_news'>
			<table border="0">
				<tr>
					<td><input type="text" id='key_work' class="h24px w160px" style="width:150px;height:26px;line-height:26px;" placeholder="关键字" title="输入搜索关键字"/></td>
					<td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.search();">搜索</button></td>
					<td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.edit();">添加</button></td>
					<td><button type="button" style="display:none;" class="authClassBtn btn_148x28_delete" onclick="thisPage.delByIds();">批量删除</button></td>
				</tr>
			</table>
		</form>
	</div>
	<table id="datagrid_bs_news" border="0"></table>
</div>
<input type="hidden" id="bs_news_edit_keyId"/><!-- 需修改时的keyId -->
<!-- 添加|编辑 -->
<div id='bs_news_edit_dialog' style="display:none;">
	<table cellspacing="4" cellpadding="0" width="100%" class="view" border="0" style="margin-top:6px;">
		<tr>
			<td style="text-align:right;padding-right:4px;">标题</td>
			<td style="text-align:left;"><input type="text" id='title' name='title' class="h24px w160px" placeholder="输入标题" title="请输入标题"/><span class="required">*</span></td>
		</tr>
		<tr>
			<td style="text-align:right;padding-right:4px;">内容</td>
			<td style="text-align:left;">
				<script id="editor" type="text/plain" style='width:680px;height: 320px;'></script>
			</td>
		</tr>
	</table>
</div>
<script type="text/javascript">
    ;(function ($){
        var uri = 'bs_news/';/*请求controller层的url*/
        var ue = null;
		thisPage = {
			datagrid : '#datagrid_bs_news',/*datagrid的id标识*/
			toolbar : '#toolbar_bs_news',/*toolbar的id标识*/
			edit_keyId : '#bs_news_edit_keyId',/*修改时的keyId标识*/
			dialog_edit : '#bs_news_edit_dialog',/*编辑或添加容器id标识*/
			urlList : uri+'listData',/*获取datagrid数据的url*/
			urlAdd : uri+'add?',/*添加时提交的url*/
			urlEdit : uri+'edit?',/*编辑时提交的url*/
			urlQueryById : uri+'queryById',/*根据id查询全字段数据的url*/
			urlDelById : uri+'delById',/*单条删除的url*/
			urlDelIds : uri+'delIds',/*批量删除的url*/
			init : function(){
				this.initAnthDom();
				this.initDatagrid();
				winFn.iePlaceholder();
			},
			initAnthDom : function(){
				authBtn('${authBtnId}');
			},
			initDatagrid : function(){
				var _self = this;
				$(_self.datagrid).datagrid({
					fit : true,
					url : _self.urlList,
					pageSize : euiFn.datagrid.settings.pageSize,
					pageList : euiFn.datagrid.settings.pageList,
					checkOnSelect : false,/*为true时单击行则自动勾选复选框*/
					pagination : true,
					fitColumns : true,
					striped : true,
					autoRowHeight : false,
					loadMsg : AppKey.loadMsg,
					toolbar : _self.toolbar,
					singleSelect : false,
					idField : 'nid',
					onBeforeLoad : function(param){
					},
					onLoadSuccess:function(data){
						//$(thisPage.datagrid).datagrid('doCellTip',{'max-width':'300px','delay':500});
					},
					onLoadError:function(){
						layerFn.alert(AppKey.msg_error);
					},
					onDblClickRow:function(index,row){
						if('${double_click}' != ''){
							thisPage.edit(1,row.nid);
						}
					},
					loadFilter : function(data){
						return winFn.dataFilter(data);
					},
					frozenColumns:[[
						{field:'nid',width:36,checkbox:true},
					]],
					columns : [[
						{field:'account',title:'创建人',width:40,align:'left'},
						{field:'title',title:'标题',width:40,align:'left'},
						{field:'add_date',title:'添加日期',width:40,align:'left'},
						{field:'details',title:'内容',width:40,align:'left'},
						{field:'_nid_',title:'操作',width:26,align:'left',hidden:('${handle_row}'==''?true:false),
							formatter:function(value,rowData,index){
								return '${row_click}';
							}
						}
					]]
				});
			},
			/**搜索*/
			search : function(){
				euiFn.refreshDatagrid(thisPage.datagrid,winFn.formAjaxParams('#search_bs_news'));
			},
			/**type=1编辑;2添加;其他未知;*/
			edit : function(type,nid){
				var title = type === 1 ?"编辑":"添加";
				type = type === 1 ? 1 : 2;//1编辑;2添加;其他未知
				if (type === 1){
					winFn.setDomValue(thisPage.edit_keyId,nid);
					euiFn.queryRowDataById(thisPage.urlQueryById,nid,function(map){
						thisPage.openDialog(type,title,map);
					});
				} else {
					winFn.setDomValue(thisPage.edit_keyId,'');
					thisPage.openDialog(type,title,null);
				}
			},
			/**打开提交对话框,含编辑及添加*/
			openDialog : function(type,title,map){
                layerFn.winUeditor(title,thisPage.dialog_edit,'editor',['800px','600px'],(map == null ? null : map.details),function(index){
                    if(verifyFn.inputRequired('#title')){
                        layerFn.center(AppKey.code.code199,'请填写标题');
                        return;
                    }
                    var details = UE.getEditor('editor').getContent();
					if(verifyFn.isEmpty(details)){
                        layerFn.center(AppKey.code.code199,'内容不能为空');
                        return;
                    }
                    var params = {
                        editType : type,
                        nid : winFn.getDomValue(thisPage.edit_keyId),
                        title : winFn.getDomValue('#title'),
                        details : details,
                    }
                    thisPage.commit(type,index,params);
				});
                winFn.clearFormData(thisPage.dialog_edit);//清空表单
                if(map != null && map !=''){
                    winFn.setDomValue('#title',map.title+'');
                }
			},
			/**删除行*/
			delById : function(nid){
				layerFn.confirm('删除后将无法恢复,确定吗?',function(){
					layerFn.submit(thisPage.urlDelById,{nid:nid},function(data){
						euiFn.showRb(data.code,data.msg);
						euiFn.refreshDatagrid(thisPage.datagrid,{},1);
					})
				});
			},
			/**批量删除*/
			delByIds : function(){
				var ids = winFn.checkboxBatch('nid');
				if (verifyFn.isEmpty(ids)){
					layerFn.center(AppKey.code.code201,'请选择要删除的数据!');
					return;
				}
				layerFn.confirm('所选[ '+(ids.split(',').length)+" ]条数据删除之后将无法恢复,确定吗?",function(){
					layerFn.delBatchHint(thisPage.urlDelIds,ids,'删除中,请稍候……',function(data){
						euiFn.showRb(data.code,data.msg);
						euiFn.refreshDatagrid(thisPage.datagrid,{},1);
					});
				});
			},
			/**1编辑或2添加的提交*/
			commit : function(type,index,params){
				var url = thisPage.urlAdd;
				if(type === 1){
					url = thisPage.urlEdit;
				}
				layerFn.submit(url,params,function(data){
				    layerFn.layerClose(index);
					euiFn.showRb(data.code,data.msg);
					euiFn.refreshDatagrid(thisPage.datagrid,{},1);//刷新datagrid数据
				});
			}
		};
		thisPage.init();
	})(jQuery);
</script>
</body>