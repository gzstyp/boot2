<#ftl encoding="utf-8"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<body>
<div fit="true" class="easyui-panel" border="false" style="padding:0px;">
    <div id="toolbar_${nameSpace}" class="authClassToolbar" style="padding-top:2px; padding-bottom:2px;height:auto;">
        <form id='search_${nameSpace}'>
            <table border="0">
                <tr>
                    <td><input type="text" id='key_work' class="h24px w160px" style="width:150px;height:26px;line-height:26px;" placeholder="关键字" title="输入搜索关键字"/></td>
                    <td><input type="text" id='work' class="h24px w160px" style="width:150px;height:26px;line-height:26px;" placeholder="名称关键字" title="输入搜索关键字"/></td>
                    <td style="text-align:left;" title="选择查询名称" id="${table}">
                        <select class="ui-select">
                            <#list listData as ld>
                                <#if "${keyId}" != "${ld.column_name}">
                            <option value="${ld.column_name}">${ld.column_comment}</option>
                                </#if>
                            </#list>
                        </select>
                        <input type="hidden" id="select_${table}" />
                    </td>
                    <td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.search();">搜索</button></td>
                    <td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.edit();">添加</button></td>
                    <td><button type="button" style="display:none;" class="authClassBtn btn_148x28_delete" onclick="thisPage.delByIds();">批量删除</button></td>
                </tr>
            </table>
        </form>
    </div>
    <table id="datagrid_${nameSpace}" border="0"></table>
</div>
<input type="hidden" id="${nameSpace}_edit_keyId"/><!-- 需修改时的keyId -->
<!-- 添加|编辑 -->
<div id='${nameSpace}_edit_dialog' style="display:none;">
    <table cellspacing="4" cellpadding="0" width="100%" class="view" border="0" style="margin-top:6px;">
		<#list listData as ld>
		<#if "${keyId}" == "${ld.column_name}">
        <#else>
		<tr>
            <td style="text-align:right;padding-right:4px;"><#if ld.column_comment ?? && ld.column_comment != ''>${ld.column_comment}<#else>字段注释</#if></td>
            <td style="text-align:left;"><input type="text" id='${ld.column_name}' name='${ld.column_name}' class="h24px w160px" placeholder="输入${ld.column_comment !'数据'}" title="请输入${ld.column_comment !'数据'}"/><span class="required">*</span></td>
        </tr>
        </#if>
        </#list>
    </table>
</div>
<script type="text/javascript">
    ;(function ($){
        var uri = '${nameSpace}/';/*请求controller层的url*/
        thisPage = {
            datagrid : '#datagrid_${nameSpace}',/*datagrid的id标识*/
            toolbar : '#toolbar_${nameSpace}',/*toolbar的id标识*/
            edit_keyId : '#${nameSpace}_edit_keyId',/*修改时的keyId标识*/
            dialog_edit : '#${nameSpace}_edit_dialog',/*编辑或添加容器id标识*/
            urlList : uri+'listData',/*获取datagrid数据的url*/
            urlAdd : uri+'add?',/*添加时提交的url*/
            urlEdit : uri+'edit?',/*编辑时提交的url*/
            urlQueryById : uri+'queryById',/*根据id查询全字段数据的url*/
            urlDelById : uri+'delById',/*单条删除的url*/
            urlDelIds : uri+'delIds',/*批量删除的url*/
            init : function(){
                this.initDom();
                this.initAnthDom();
                this.initDatagrid();
                winFn.iePlaceholder();
            },
            initDom : function(){
                winFn.uiSelectInit('#${table}',function(value){
                    $('#select_${table}').val(value);
                },'120px');
            },
            initAnthDom : function(){
                authBtn(${r"'${authBtnId}'"});
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
                    idField : '${keyId}',
                    onBeforeLoad : function(param){
                    },
                    onLoadSuccess:function(data){
                        $(thisPage.datagrid).datagrid('doCellTip',{'max-width':'300px','delay':500});
                    },
                    onLoadError:function(){
                        layerFn.alert(AppKey.msg_error);
                    },
                    onDblClickRow:function(index,row){
                        if('${r"${double_click}"}' != ''){
                            thisPage.edit(1,row.${keyId});
                        }
                    },
                    loadFilter : function(data){
                        return winFn.dataFilter(data);
                    },
                    frozenColumns:[[
                        {field:'${keyId}',width:36,checkbox:true},
                    ]],
                    columns : [[
						<#list listData as ld>
                            <#if "${keyId}" == "${ld.column_name}">
                            <#else>
						{field:'${ld.column_name}',title:'<#if ld.column_comment ?? && ld.column_comment != ''>${ld.column_comment}<#else>字段注释</#if>',width:40,align:'left'},
                            </#if>
                        </#list>
                        {field:'_${keyId}_',title:'操作',width:26,align:'left',hidden:('${r"${handle_row}"}'==''?true:false),
                            formatter:function(value,rowData,index){
                                return '${r"${row_click}"}';
                            }
                        }
                    ]]
                });
            },
            /**搜索*/
            search : function(){
                var params = {
                    <#list listData as ld>
                    <#if "${keyId}" != "${ld.column_name}">
                    ${ld.column_name} : winFn.getDomValue('#key_${ld.column_name}'),
                    </#if>
                    </#list>
                };
                euiFn.refreshDatagrid(thisPage.datagrid,params,1);
            },
            /**type=1编辑;2添加;其他未知;*/
            edit : function(type,${keyId}){
                var title = type === 1 ?"编辑":"添加";
                type = type === 1 ? 1 : 2;//1编辑;2添加;其他未知
                if (type === 1){
                    winFn.setDomValue(thisPage.edit_keyId,${keyId});
                    euiFn.queryRowDataById(thisPage.urlQueryById,${keyId},function(map){
                        thisPage.openDialog(type,title,map);
                    });
                } else {
                    winFn.setDomValue(thisPage.edit_keyId,'');
                    thisPage.openDialog(type,title,null);
                }
            },
            /**打开提交对话框,含编辑及添加*/
            openDialog : function(type,title,map){
                layerFn.addOrEdit(title,thisPage.dialog_edit,['500px','400px'],function(index){
					<#list listData as ld>
                    <#if "${keyId}" == "${ld.column_name}">
                    <#else>
                    if(verifyFn.inputRequired('#${ld.column_name}')){
                        layerFn.center(AppKey.code.code199,'${ld.column_comment !'数据'}不能为空');
                        return;
                    }
                    </#if>
                    </#list>
                    var params = {
                        editType : type,
                        ${keyId} : winFn.getDomValue(thisPage.edit_keyId),
						<#list listData as ld>
                        <#if "${keyId}" == "${ld.column_name}">
                        <#else>
                        ${ld.column_name} : winFn.getDomValue('#${ld.column_name}'),
                        </#if>
                        </#list>
                    };
                    thisPage.commit(type,index,params);
                });
                winFn.clearFormData(thisPage.dialog_edit);//清空表单
                if(map != null && map !=''){
				<#list listData as ld>
                    <#if "${keyId}" == "${ld.column_name}">
                    <#else>
					winFn.setDomValue('#${ld.column_name}',map.${ld.column_name}+'');
                    </#if>
                </#list>
                }else{
                }
            },
            /**删除行*/
            delById : function(${keyId}){
                layerFn.confirm('删除后将无法恢复,确定吗?',function(){
                    layerFn.submit(thisPage.urlDelById,{${keyId}:${keyId}},function(data){
                        euiFn.showRb(data.code,data.msg);
                        euiFn.refreshDatagrid(thisPage.datagrid,{},1);
                    })
                });
            },
            /**批量删除*/
            delByIds : function(){
                var ids = winFn.checkboxBatch('${keyId}');
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