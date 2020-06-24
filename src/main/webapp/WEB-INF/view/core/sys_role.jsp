<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<body>
<div fit="true" class="easyui-panel" border="false" style="padding:0px;">
    <div id="toolbar_sys_role" class="authClassToolbar" style="padding-top:2px; padding-bottom:2px;height:auto;">
        <table border="0">
            <tr>
                <td><input type="text" id='key_rname' class="h24px w160px" style="width:150px;height:26px;line-height:26px;" placeholder="角色名称" title="角色名称"/></td>
                <td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.search();">搜索</button></td>
                <td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.edit();">添加</button></td>
                <td><button type="button" style="display:none;" class="authClassBtn btn_148x28_delete" onclick="thisPage.del();">批量删除</button></td>
            </tr>
        </table>
    </div>
    <table id="datagrid_sys_role" border="0"></table>
</div>
<input type="hidden" id="role_edit_keyId"/><!-- 需修改时的keyId -->
<!-- 添加|编辑角色-->
<div id='sys_role_edit' style="display:none;">
    <div style="padding-top:40px;padding-left:50px;">
        角色名称:<input type="text" id='role_name' class="h24px w160px" placeholder="角色名称" title="输入角色名称" /><span class="required">*</span>
    </div>
</div>
<!-- 分配角色-->
<div id='sys_role_allot' style="padding-top:6px;padding-left:24px;">
    <div style="margin-left:-6px;margin-top:-6px;">
        <ul id="role_menu_allot" class="easyui-tree"></ul>
    </div>
</div>
<script type="text/javascript">
    ;(function ($) {
        var flag = true;
        var uri = 'role/';/*请求controller层的url*/
        thisPage = {
            datagrid : '#datagrid_sys_role',/*datagrid元素*/
            toolbar : '#toolbar_sys_role',/*toolbar元素*/
            div_role_edit : '#sys_role_edit',/*编辑或添加div容器*/
            edit_keyId : '#role_edit_keyId',/*需修改时的keyId*/
            role_menu : '#role_menu_allot',
            urlList : uri+'listData?',/*获取datagrid数据的url*/
            urlAdd : uri+'add',/*添加时提交的url*/
            urlEdit : uri+'edit',/*编辑时提交的url*/
            urlDelById : uri+'delById',/*单条删除的url*/
            urlDelIds : uri+'delIds',/*批量删除的url*/
            init : function(){
                thisPage.initAnthDom();
                thisPage.initDatagrid();
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
                    checkOnSelect : false,
                    pagination : true,
                    fitColumns : true,
                    showFooter : false,
                    striped : true,
                    autoRowHeight : false,
                    loadMsg : AppKey.loadMsg,
                    toolbar : _self.toolbar,
                    singleSelect : false,
                    idField : 'rid',
                    onBeforeLoad : function(param){
                    },
                    onLoadSuccess:function(data){
                    },
                    onLoadError:function(){
                        layerFn.alert(AppKey.msg_error);
                    },
                    onDblClickRow:function(index,row){
                        if('${double_click}' != ''){
                            thisPage.edit(1,row.rid,row.rname);
                        }
                    },
                    loadFilter : function(data){
                        return winFn.dataFilter(data);
                    },
                    frozenColumns:[[
                        {field:'rid',width:36,checkbox:true}
                    ]],
                    columns : [[
                        {field:'rname',title:"<span title='角色名称'>角色名称</span>",width:320,align:'left',sortable:true,
                            formatter:function(value,rowData,index){
                                return '<span title='+value+'>'+value+'</span>';
                            }
                        },
                        {field:'total',title:"<span title='分配量'>分配量</span>",width:60,align:'center',},
                        {field:'mtotal',title:"<span title='菜单数'>菜单数</span>",width:60,align:'center',},
                        {field:'_rid_',title:"<span title='操作'>操作</span>",width:210,align:'left',hidden:('${handle_row}'==''?true:false),
                            formatter:function(value,rowData,index){
                                return '${row_click}';
                            }
                        }
                    ]]
                });
            },
            addEvent : function(){
            },
            /**初始化自定义复选框元素*/
            initCheckboxRole : function(){
                $('td div.datagrid-header-check input[type=checkbox]').on('ifCreated ifClicked ifChanged ifChecked ifUnchecked ifDisabled ifEnabled ifDestroyed',function(event){
                }).iCheck({
                    checkboxClass : 'icheckbox_square-blue',
                    radioClass : 'iradio_square-blue',
                });
            },
            /**1是编辑 ;否则添加;*/
            edit : function(type,id,rname){
                var title = type === 1 ?"编辑角色":"添加角色";
                type = type === 1 ? 1 : 2;//1编辑;2添加;其他是未知
                layerFn.addOrEdit(title,thisPage.div_role_edit,['340px','200px'],function(index){
                    if (verifyFn.inputRequired('#role_name')){
                        layerFn.center(AppKey.code.code201,'请输入角色名称');
                        return;
                    }
                    var r_name = winFn.getDomValue('#role_name');
                    var params = {
                        rname : r_name,
                        rid : winFn.getDomValue(thisPage.edit_keyId)
                    };
                    var url = thisPage.urlAdd;
                    if (type === 1){
                        url = thisPage.urlEdit;
                    }
                    layerFn.submit(url,params,function(data){
                        layerFn.layerClose(index);//成功时才关闭对话框
                        euiFn.showRb(data.code,data.msg);
                        thisPage.search();//刷新datagrid数据
                    });
                });
                winFn.clearFormData(thisPage.div_role_edit);//清空表单
                if (type === 1){
                    winFn.setDomValue(thisPage.edit_keyId,id);
                    winFn.setDomValue('#role_name',rname);
                }else{
                    winFn.setDomValue(thisPage.edit_keyId, '');
                }
            },
            /**批量批量*/
            del : function(){
                var ids = winFn.checkboxBatch('rid');
                if (verifyFn.isEmpty(ids)){
                    layerFn.center(AppKey.code.code201,'请选择要删除的角色');
                    return;
                }
                layerFn.confirm('所选[ '+(ids.split(',').length)+' ]条数据删除后将无法恢复,确定吗?',function(){
                    layerFn.delBatchHint(thisPage.urlDelIds,ids,'删除中,请稍候……',function(data){
                        euiFn.showRb(data.code,'删除成功');
                        thisPage.search();
                    });
                });
            },
            /**删除行*/
            delById : function(id){
                layerFn.confirm('删除后将无法恢复,确定吗?',function(){
                    layerFn.delByIdHint(thisPage.urlDelById,id,'删除中,请稍候……',function(data){
                        euiFn.showRb(data.code,'删除成功');
                        thisPage.search();
                    });
                });
            },
            search : function(){
                var rname = winFn.getDomValue('#key_rname');
                euiFn.refreshDatagrid(thisPage.datagrid,{rname:rname},1);
            },
            /**角色菜单加载及保存*/
            roleAllot : function(rid,rname){
                flag = true;
                winFn.setDomValue(thisPage.edit_keyId,rid);
                $(thisPage.role_menu).empty();//清空
                layerFn.addOrEdit(rname+'菜单分配','#sys_role_allot',layerFn.area.h500xw450,function(index){
                    var chks = $(thisPage.role_menu).tree('getChecked');
                    var ids = '';
                    for(var i = 0;i < chks.length;i++){
                        if($.trim(ids).length > 0)
                            ids += ',';
                        ids += chks[i].id;
                    }
                    layerFn.submit(uri+'saveIds', {ids:ids,rid:rid},function(data){
                        layerFn.layerClose(index);
                        euiFn.showRb(data.code,'操作成功');
                    });
                },'<span style="color:#3b8cff;">全选</span>',function(){
                    var childen = $(thisPage.role_menu).tree('getChildren');
                    if(childen.length <= 0)return;
                    for(var a = 0;a < childen.length;a++){
                        $(thisPage.role_menu).tree('check',childen[a].target);
                    }
                });
                $(thisPage.role_menu).tree(
                    {
                        url : "role/roleMenu?rid="+rid,
                        animate : true,
                        dnd : false,
                        checkbox : true,
                        lines : true,
                        onlyLeafCheck : false,
                        onBeforeLoad : function(node,param){
                            if (flag){
                                thisPage.zIndex = layerFn.loading.show('正在加载……');
                            }
                        },
                        onLoadSuccess : function(node,data){
                            if (flag){
                                flag = false;
                                layerFn.loading.hide(thisPage.zIndex);
                            }
                            var chks = $(thisPage.role_menu).tree("getChecked");
                            for(var i=0;i<chks.length;i++){
                                $(thisPage.role_menu).tree('expand',chks[i].target);
                            }
                        },
                        loadFilter : function(data){
                            if (data.code == AppKey.code.code200){
                                return data.listData;
                            }else if (data.code == AppKey.code.code207){
                                layerFn.closeEvent(data.code,data.msg,function(){
                                    window.location.href = AppKey.loginUrl;
                                });
                                return '';
                            }else if (data.code == AppKey.code.code201){
                                layerFn.layerClose(index);
                                layerFn.center(data.code,'没有可用的菜单');
                                return '';
                            }else{
                                layerFn.alert(data.msg,data.code);
                                return '';
                            }
                        },
                        onClick : function(node){
                        },
                        onLoadError : function(response,err){
                            flag = false;
                            layerFn.timeoutHint(err);
                        }
                    }
                );
            },
            removeRoleMenu : function(id){
                layerFn.handleVerify('清空菜单之后将无法恢复,确定吗?',function(){
                    layerFn.delByIdHint(uri+'removeRoleMenu',id,'操作中,请稍候……',function(data){
                        euiFn.showRb(data.code,'操作成功');
                    });
                });
            }
        };
        thisPage.init();
    })(jQuery);
</script>
</body>