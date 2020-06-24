;(function($){
	thisPage = {
		datagrid : '#datagrid_sys_menu',
		toolbar : '#toolbar_sys_menu',
		url : 'menu/listData',
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
		   		idField : 'id',
		   		onBeforeLoad : function(param){
				},
		   		onLoadSuccess:function(data){
		   			euiFn.datagridTips(_self.datagrid);
				},
				onLoadError:function(){
					layerFn.alert(AppKey.msg_error);
				},
		   		singleSelect:true,
		   		frozenColumns:[[
					{field:'C',title:'选择',align:'center',width:36,formatter:function(value,rowData,rowIndex){/**此行请参考page.core.sys_user.js的写法*/
							return '<input type="checkbox" name="ids" value="'+rowData.id+'" />';
						}
					}
				]],
				columns : [[
						{field:'name',title:'菜单名称',width:100,align:'left'},
						{field:'uri',title:'访问路径',width:84,align:'left'},
					    {field:'_id_',title:'操作',width:190,align:'left',
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