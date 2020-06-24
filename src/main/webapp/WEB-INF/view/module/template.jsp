<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<body>
<div fit="true" class="easyui-panel" border="false" style="padding:0px;">
	<div id="toolbar_template" class="authClassToolbar" style="padding-top:2px; padding-bottom:2px;height:auto;">
		<table border="0">
			<tr>
				<td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.edit();">添加</button></td>
			</tr>
		</table>
	</div>
	<table id="datagrid_template" border="0"></table>
</div>
<input type="hidden" id="template_edit_keyId"/><!-- 需修改时的keyId -->
<!-- 添加|编辑 -->
<div id='template_edit_dialog' style="display:none;">
	<table cellspacing="0" class="editTable" cellpadding="0" width="100%" border="0">
		<tr>
			<td>模版名称</td>
		</tr>
	</table>
</div>
<script type="text/javascript">
    ;(function () {
        thisPage = {
            datagrid : '#datagrid_template',/*datagrid的id标识*/
            toolbar : '#toolbar_template',/*toolbar的id标识*/
            edit_keyId : '#template_edit_keyId',/*修改时的keyId标识*/
            dialog_edit : '#template_edit_dialog',//编辑或添加容器id标识
            init : function(){
                this.initAnthDom();
                this.initDatagrid();
            },
            initAnthDom : function(){
                authBtn('${authBtnId}');
            },
            initDatagrid : function(){
                var _self = this;
                $(_self.datagrid).datagrid({
                    fit : true,
                    url : 'template/listData',
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
                    loadFilter : function(data){
                        return winFn.dataFilter(data);
                    },
                    columns : [[
                            {field:'column',title:'名称',width:40,align:'left',sortable:true},
                            {field:'_column_',title:'操作',width:26,align:'left',hidden:('${handle_row}'==''?true:false),
                                formatter:function(value,rowData,index){
                                    return '${row_click}';
                                }
                            }
                         ]]
                    }
                );
            },
            /**搜索*/
            search : function(){
                var key_work = winFn.getDomValue('#key_work');
                euiFn.refreshDatagrid(thisPage.datagrid,{key_work:key_work});
            },
            /**1是编辑 ;否则添加;*/
            edit : function(type,id){
                var title = type === 1 ?"编辑":"添加";
                type = type === 1 ? 1 : 2;//1编辑;2添加;其他是未知
                layerFn.addOrEdit(title,thisPage.dialog_edit,['600px','550px'],function(index){
                    if (verifyFn.inputRequired('#xx_name')){//验证是否已输入名称
                        layerFn.center(AppKey.code.code201,'请输入名称');
                        return;
                    }
                    var params = {
                        id : winFn.getDomValue(thisPage.edit_keyId)
                    };
                    thisPage.submit(type,index,params);
                });
                if (type === 1){
                    winFn.setDomValue(thisPage.edit_keyId,id);
                    euiFn.queryRowDataById('template/queryById',id,function(map){
                    });
                } else {
                    winFn.setDomValue(thisPage.edit_keyId,'');
                }
            },
            /**删除行*/
            delById : function(id){
                layerFn.handleVerify('数据删除之后且无法恢复,确定吗?',function(){
                    layerFn.submit('template/delById',{id:id},function(data){
                        euiFn.showRb(data.code,data.msg);
                        euiFn.refreshDatagrid(thisPage.datagrid,{});
                    });
                });
            },
            /**批量删除*/
            delByIds : function(){
                var ids = winFn.checkboxBatch('kId');
                if (verifyFn.isEmpty(ids)){
                    layerFn.center(AppKey.code.code201,'请选择要删除的数据!');
                    return;
                }
                layerFn.handleVerify("选中的"+(ids.split(',').length)+"条数据删除之后将无法恢复,确定吗?",function(){
                    layerFn.delBatchHint('template/delByIds',ids,'删除中,请稍候……',function(data){
                        euiFn.showRb(data.code,data.msg);
                        euiFn.refreshDatagrid(thisPage.datagrid,{});
                    });
                });
            },
            /**1编辑或2添加的提交*/
            submit : function(type,index,params){
                var url = 'template/add';
                if (type == 1){
                    url = 'template/edit';
                }
                layerFn.submit(url,params,function(data){
                    layerFn.layerClose(index);
                    euiFn.showRb(data.code,data.msg);
                    euiFn.refreshDatagrid(thisPage.datagrid,{});//刷新datagrid数据
                });
            }
        };
        thisPage.init();
    })(jQuery);
</script>
</body>