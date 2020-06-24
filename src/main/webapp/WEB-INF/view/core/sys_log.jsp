<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<body>
<div fit="true" class="easyui-panel" border="false" style="padding:0px;">
	<div id="toolbar_sys_log" class="authClassToolbar" style="padding-top:2px; padding-bottom:2px;height:auto;">
		<table border="0">
			<tr>
				<td><input type="text" id='key_word' class="h24px w160px" style="width:150px;height:26px;line-height:26px;" placeholder="输入关键字" title="输入关键字"/></td>
				<td style="text-align:left;" title="选择操作类型" id="log_search_td_type">
					<select class="ui-select">
						<option value="">操作类型</option>
						<option value="1">添加</option>
						<option value="2">删除</option>
						<option value="3">编辑</option>
						<option value="4">查询</option>
						<option value="5">批量添加</option>
						<option value="6">批量删除</option>
						<option value="7">批量更新</option>
					</select>
					<input type="hidden" id="log_search_key_type" />
				</td>
				<td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.search();">搜索</button></td>
			</tr>
		</table>
	</div>
	<table id="datagrid_sys_log" border="0"></table>
</div>
<script type="text/javascript">
    ;(function ($) {
        var uri = 'log/';/*请求controller层的url*/
	    thisPage = {
            datagrid : '#datagrid_sys_log',
            toolbar : '#toolbar_sys_log',
            search_key_type : '#log_search_key_type',
            parent_type : '#log_search_td_type',
            urlList : uri+'listData',
            init : function(){
                thisPage.initAnthDom();
                thisPage.initDom();
                thisPage.initDatagrid();
                winFn.iePlaceholder();
            },
            initAnthDom : function(){
                authBtn('${authBtnId}');
            },
            initDom : function(){
                winFn.uiSelectInit(thisPage.parent_type,function(value){
                    $(thisPage.search_key_type).val(value);
                },'110px');
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
                    showFooter : false,
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
                        {field:'account',title:'操作人',width:80,align:'left',sortable:true,
                            formatter:function(value,rowData,index){
                                if(value == 'admin'){
                                    return '系统管理员';
                                }else{
                                    return value;
                                }
                            }
                        },
                        {field:'name',title:'模块名称',width:80,align:'left',sortable:true},
                        {field:'handle',title:'操作类型',width:40,align:'left',sortable:true,
                            formatter:function(value,rowData,index){
                                if(value === 1){
                                    return '添加';
                                }else if(value === 2){
                                    return '删除';
                                }else if(value === 3){
                                    return '编辑';
                                }else if(value === 4){
                                    return '查询';
                                }else if(value === 5){
                                    return '批量添加';
                                }else if(value === 6){
                                    return '批量删除';
                                }else if(value === 7){
                                    return '批量更新';
                                }else{
                                    return '';
                                }
                            }
                        },
                        {field:'ctime',title:'操作时间',width:60,align:'left',sortable:true},
                        {field:'ip',title:'ip地址',width:60,align:'left',sortable:true},
                        {field:'location',title:'来自',width:60,align:'left',
                            formatter:function(value,rowData,index){
                                if(value === '0'){
                                    return '';
                                }else{
                                    return value;
                                }
                            }
                        },
                        {field:'info',title:'详细信息',width:240,align:'left'},
                    ]]
                });
            },
            search : function(){
                var key_word = winFn.getDomValue('#key_word');
                var handle = winFn.getDomValue(thisPage.search_key_type);
                euiFn.refreshDatagrid(thisPage.datagrid,{key_word:key_word,handle:handle});
            }
	    };
	    thisPage.init();
    })(jQuery);
</script>
</body>