;(function($){
	window.thisPage = {
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
		   		rownumbers : false,
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
					{field:'C',title:'选择',align:'center',width:36,formatter:function(value,rowData,rowIndex){ 
							return '<input type="checkbox" name="ids" value="'+rowData.id+'" />';
						}
					},
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
		thisFn : {
			hint : function(){
				euiFn.showRb(AppKey.code.code200,'操作成功');
			}
		},
		menu_add : function(){
			//self.index = layerFn.dialogEdit('添加菜单','sys_menu_edit',['400px','350px'],function(){//也是可以的,但是推荐使用下面这个
			thisPage.index = layerFn.dialogEdit('添加菜单','sys_menu_edit',['400px','350px'],function(){
				if (verifyFn.inputRequired('#menu_name')){
					layerFn.center(AppKey.code.code200,'请输入菜单名');
				} else{
					//layer.close(self.index);
					layer.close(thisPage.index);
					var v = winFn.getDomValue('#menu_name');
					$('#key_span').text(v);
					euiFn.refreshDatagrid(thisPage.datagrid,{},1);
					thisPage.thisFn.hint();
					//tips();/**调用私有方法也是可以的*/
				}
			});
		}
	};
	thisPage.init();
	/**私有方法*/
	function tips(){
		euiFn.showRb(AppKey.code.code204,'操作成功');
	}
})(jQuery);