;(function($){
	thisPage = {
		datagrid : '#datagrid_sys_user',
		toolbar : '#toolbar_sys_user',
		url : 'user/listData',
		init : function(){
			thisPage.initAnthDom();
			thisPage.initDatagrid();
		},
		initAnthDom : function(){
			authBtn('${authBtnId}');
		},
		initDatagrid : function(){
			var _self = this;
			$(_self.datagrid).datagrid({
		   		fit : true,
		   		url : _self.url,
		   		pageSize : 50,
		   		pageList : [50,100,150,200,250,500],
		   		checkOnSelect : false,
		   		pagination : true,
		   		fitColumns : true,
		   		showFooter : false,
		   		striped : true,
		   		autoRowHeight : false,
		   		loadMsg : AppKey.loadMsg,
		   		toolbar : _self.toolbar,
		   		idField : 'uid',
		   		onBeforeLoad : function(param){
				},
		   		onLoadSuccess:function(data){
		   			euiFn.datagridTips(_self.datagrid);
				},
				onLoadError:function(){
					layerFn.alert(AppKey.msg_error);
				},
		   		singleSelect:false,
		   		frozenColumns:[[
					{field:'uid',width:36,checkbox:true,name:'ids'}/**推荐使用本方法,因为添加了name之后最终生成的是这个<input type="checkbox" value="c6ec0578a1aa48eaad610cb4206c8d8c" name="uid">*/
 				]],
				columns : [[
						{field:'account',title:'登录账号',width:100,align:'left'},
						{field:'enabled',title:'是否正常',width:84,align:'left',formatter:function(value,rowData,rowIndex){
							return value == 1 ? '异常' : '正常';
						}},
						{field:'type',title:'是否系统账号',width:84,align:'left',formatter:function(value,rowData,rowIndex){
							return value == 1 ? '是' : '否';
						}},
						{field:'deleted',title:'是否已删除',width:84,align:'left',formatter:function(value,rowData,rowIndex){
							return value == 1 ? '已删除' : '未删除';
						}},
						{field:'added',title:'添加日期',width:120,align:'left'},
					    {field:'_uid_',title:'操作',width:190,align:'left',
							formatter:function(value,rowData,index){
								return '${row_click}';
					        }
				        }
				     ]]
		   		}
	   		);
		},
		addEvent : function(){
		}
	};
	thisPage.init();
})(jQuery);