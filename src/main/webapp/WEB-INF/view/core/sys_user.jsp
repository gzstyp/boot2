<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<body>
<div fit="true" class="easyui-panel" border="false" style="padding:0px;">
    <div id="toolbar_sys_user" class="authClassToolbar" style="padding-top:2px; padding-bottom:2px;height:auto;">
        <form id='search_user'>
            <table border="0">
                <tr>
                    <td>
                        <input type="text" id='account' name="account" class="h24px w160px" style="width:150px;height:26px;line-height:26px;" placeholder="账号关键字" title="输入账号搜索"/>
                    </td>
                    <td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.search();">搜索</button></td>
                    <td><button type="button" style="display:none;" class="authClassBtn btn_102x28_blue" onclick="thisPage.edit();">添加</button></td>
                    <td><button type="button" style="display:none;" class="authClassBtn btn_190x28_blue" onclick="thisPage.batchAllotRole();">批量分配角色</button></td>
                    <td><button type="button" style="display:none;" class="authClassBtn btn_190x28_dhs" onclick="thisPage.batchRemoveRole();">批量去除角色</button></td>
                    <td><button type="button" style="display:none;" class="authClassBtn btn_148x28_delete" onclick="thisPage.del();">批量删除</button></td>
                </tr>
            </table>
        </form>
    </div>
    <table id="datagrid_sys_user" border="0"></table>
</div>
<input type="hidden" id="user_edit_keyId"/><!-- 需修改时的keyId -->
<!-- 添加|编辑账号 -->
<div id='sys_user_edit' class="divLayer" style="padding-left:50px;">
    <table cellspacing="4" cellpadding="0" width="100%" border="0">
        <tr>
            <td style="text-align:right;padding-right:4px;">登录账号</td><td style="text-align:left;"><input type="text" id='user_name' class="h24px w160px" placeholder="登录账号" title="登录账号"/><span class="required">*</span></td>
        </tr>
        <tr>
            <td style="text-align:right;padding-right:4px;">登录密码</td><td style="text-align:left;"><input type="password" id='user_pwd' class="h24px w160px" placeholder="输入账号密码" title="输入账号密码"/><span class="required">*</span></td>
        </tr>
        <tr>
            <td style="text-align:right;padding-right:4px;">确认密码</td><td style="text-align:left;"><input type="password" id='user_repwd' class="h24px w160px" placeholder="输入确认密码" title="输入确认密码"/><span class="required">*</span></td>
        </tr>
        <tr>
            <td style="text-align:right;padding-right:4px;">组织机构</td>
            <td style="text-align:left;">
                <input type="text" id='dep_name' name="dep_name" class="h24px w160px" disabled="disabled"/><span class="required">*</span>
                <input type="hidden" id="dep_kid" name="dep_kid"/>
            </td>
        </tr>
        <tr>
            <td style="text-align:right;padding-right:4px;">选择组织</td><td style="text-align:left;">
            <div style="margin-left:-10px;margin-top:-0px;">
                <ul id="sys_dep_user_tree" class="ztree"></ul>
            </div>
        </td>
        </tr>
    </table>
</div>
<!-- 设置私有菜单 -->
<div id='sys_user_self_menu'>
    <div style="margin-left:18px;margin-top:0px;">
        <ul id="user_self_menu" class="easyui-tree"></ul>
    </div>
</div>
<!-- 分配角色 -->
<div id='sys_user_allot_role'>
    <div style="margin-left:14px;margin-top:0px;">
        <ul id="self_allot_role" class="easyui-tree"></ul>
    </div>
</div>
<!-- 批量分配角色 -->
<div id='div_batch_allot_role'>
    <div style="margin-left:14px;margin-top:0px;">
        <ul id="ul_batch_allot_role" class="easyui-tree"></ul>
    </div>
</div>
<!-- 账号的菜单数据 -->
<div id='div_user_menu_data'>
    <div style="margin-left:18px;margin-top:0px;">
        <ul id="ul_user_menu_data" class="easyui-tree"></ul>
    </div>
</div>
<script type="text/javascript">
    var domTree = 'sys_dep_user_tree';
    var is_loaded = true;
    var loading_dep = null;
    ;(function ($){
        var indexSelfMenu = null ;/*私有菜单的索引值*/
        var indexAllotRole = null ;/*分配角色的索引值*/
        var indexBatchAllotRole = null ;/*分配角色的索引值*/
        var loadMenuData = true;/*账号的菜单数据是否已加载完毕*/
        var flag = true;
        var flagAllotRole = true;
        var flagBatchAllotRole = true;
        var flagMenuData = true ;/*账号的菜单数据*/
        var uri = 'user/';/*请求controller层的url*/
        var setting = {
            view:{
                expandSpeed : 100,
                showLine : true,
                checked : true,
                showIcon : false,
                fontCss : {"color":"#000"}
            },
            async : {
                enable : true,
                url : "sys_department/queryAllDepartment",
                cache : false,
                type : "POST",
                autoParam : ["kid"],
                dataFilter : function(treeId,parentNode,result){
                    result = layerFn.checkLogin(result);
                    if (result.code == AppKey.code.code200)return result.listData;
                    return '';
                }
            },
            callback : {
                beforeAsync : function(){
                    if(is_loaded){
                        is_loaded = false;
                        loading_dep = layerFn.loading.show('正在加载,请稍候……');
                    }
                },
                onAsyncSuccess : function(data){
                    layerFn.loading.hide(loading_dep);
                },
                onAsyncError : function(){
                    layerFn.alert(AppKey.msg_error);
                },
                onClick : function(event,treeIdDom,node,clickFlag){
                    winFn.setDomValue('#dep_name',node.name);
                    winFn.setDomValue('#dep_kid',node.kid);
                },
            }
        };
        thisPage = {
            datagrid : '#datagrid_sys_user',/*datagrid元素*/
            toolbar : '#toolbar_sys_user',/*toolbar元素*/
            div_user_edit : '#sys_user_edit',/*菜单的编辑或添加容器id*/
            div_self_menu : '#sys_user_self_menu',/*私有菜单块*/
            div_allot_role : '#sys_user_allot_role',/*分配角色块*/
            div_batch_allot : '#div_batch_allot_role',/*批量分配角色块*/
            div_menu_data : '#div_user_menu_data',/*账号的菜单数据div块*/
            ul_self_menu : '#user_self_menu',/*私有菜单节点*/
            ul_allot_role : '#self_allot_role',/*分配角色节点*/
            ul_batch_allot : '#ul_batch_allot_role',/*批量分配角色节点*/
            ul_menu_data : '#ul_user_menu_data',/*账号的菜单数据节点*/
            edit_keyId : '#user_edit_keyId',
            urlList : uri+'listData',/*获取datagrid数据的url*/
            urlAdd : uri+'add',/*添加时提交的url*/
            urlEdit : uri+'editPwd',/*编辑时提交的url*/
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
                    striped : true,
                    autoRowHeight : false,
                    loadMsg : AppKey.loadMsg,
                    toolbar : _self.toolbar,
                    singleSelect : false,
                    idField : 'uid',
                    onBeforeLoad : function(param){
                    },
                    onLoadSuccess:function(data){
                    },
                    onLoadError:function(){
                        layerFn.alert(AppKey.msg_error);
                    },
                    onDblClickRow:function(index,row){
                        if('${double_click}' != ''){
                            thisPage.edit(1,row.uid,row.account);
                        }
                    },
                    loadFilter : function(data){
                        return winFn.dataFilter(data);
                    },
                    frozenColumns:[[
                        {field:'uid',width:36,checkbox:true},
                    ]],
                    columns : [[
                        {field:'account',title:'账号',width:60,align:'left',sortable:true,formatter:function(value,rowData,rowIndex){
                                return '<span title='+value+'>'+value+'</span>';
                        }},
                        {field:'dep_name',title:'组织机构',width:50,align:'left'},
                        {field:'rname',title:'角色',width:60,align:'left',sortable:true,formatter:function(value,rowData,rowIndex){
                                if (value != null && value != ''){
                                    return '<span title='+value+'>'+value+'</span>';
                                }else {
                                    return '';
                                }
                            }},
                        {field:'enabled',title:'账号状态',width:40,align:'center',sortable:true,formatter:function(value,rowData,rowIndex){
                                return value == 1 ? '禁用' : '正常';
                            }},
                        {field:'added',title:'添加时间',width:70,align:'center',sortable:true},
                        {field:'_uid_',title:'操作',width:256,align:'left',hidden:('${handle_row}'==''?true:false),
                            formatter:function(value,rowData,index){
                                return '${row_click}';
                            }
                        }
                    ]]
                });
            },
            search : function(){
                euiFn.refreshDatagrid(thisPage.datagrid,winFn.formAjaxParams('#search_user'),1);
            },
            /**1是编辑 ;否则添加;*/
            edit : function(type,id,account,dep_name){
                is_loaded = true;
                var title = type === 1 ?"编辑账号":"添加账号";
                type = type === 1 ? 1 : 2;//1编辑;2添加;其他是未知
                layerFn.addOrEdit(title,thisPage.div_user_edit,['400px','300px'],function(indexEdit){
                    if(type === 2){
                        if (verifyFn.inputRequired('#user_name')){
                            layerFn.center(AppKey.code.code201,'请输入登录账号');
                            return;
                        }
                        if (verifyFn.inputRequired('#user_pwd')){
                            layerFn.center(AppKey.code.code201,'请输入登录密码');
                            return;
                        }
                        if (verifyFn.inputRequired('#user_repwd')){
                            layerFn.center(AppKey.code.code201,'请输入确认密码');
                            return;
                        }
                        if (verifyFn.inputRequired('#dep_kid')){
                            layerFn.center(AppKey.code.code201,'请选择所属组织机构');
                            return;
                        }
                    }
                    var user_name = winFn.getDomValue('#user_name');
                    var user_pwd = winFn.getDomValue('#user_pwd');
                    var user_repwd = winFn.getDomValue('#user_repwd');
                    if(verifyFn.checkEqual(user_pwd,user_repwd)){
                        layerFn.center(AppKey.code.code204,'输入的两次密码不一致');
                        return;
                    }
                    var dep_kid = winFn.getDomValue('#dep_kid');
                    var params = {
                        account : user_name,
                        pwd : user_pwd,
                        repwd : user_repwd,
                        DEP_ID : dep_kid,
                        id : winFn.getDomValue(thisPage.edit_keyId)
                    };
                    var editUrl = thisPage.urlAdd;
                    if(type === 1){
                        editUrl = thisPage.urlEdit;
                    }
                    layerFn.submit(editUrl,params,function(data){
                        layerFn.layerClose(indexEdit);//成功时才关闭对话框
                        euiFn.showRb(data.code,'操作成功');
                        thisPage.search();//刷新datagrid数据
                    });
                });
                if(type === 1){
                    winFn.setDomValue(thisPage.edit_keyId,id);
                    winFn.clearFormData(thisPage.div_user_edit);//清空表单
                    winFn.setDomValue('#user_name',account);
                    winFn.input.off('#user_name');//禁用输入框
                } else{
                    winFn.input.on('#user_name');//启用输入框
                    winFn.clearFormData(thisPage.div_user_edit);//清空表单
                }
                if(!verifyFn.isEmpty(dep_name)){
                    winFn.setDomValue('#dep_name',dep_name);
                }else{
                    winFn.setDomValue('#dep_name');//清空
                }
                winFn.setDomValue('#dep_kid');//清空
                $.fn.zTree.init($("#"+domTree),setting);//初始化树形菜单及并读取数据
            },
            /**批量删除*/
            del : function(){
                var ids = winFn.checkboxBatch('uid');
                if(verifyFn.isEmpty(ids)){
                    layerFn.center(AppKey.code.code201,'请选择要删除的账号');
                    return;
                }
                layerFn.confirm('所选[ '+(ids.split(',').length)+' ]条数据账号删除之后将无法恢复,确定吗?',function(){
                    layerFn.delBatchHint(thisPage.urlDelIds,ids,'删除中,请稍候……',function(data){
                        euiFn.showRb(data.code,'删除成功');
                        thisPage.search();
                    });
                });
            },
            /**行删除*/
            delById : function(id){
                layerFn.confirm('删除之后将无法恢复,确定吗?',function(){
                    layerFn.delByIdHint(thisPage.urlDelById,id,'删除中,请稍候……',function(data){
                        euiFn.showRb(data.code,'删除成功');
                        thisPage.search();
                    });
                });
            },
            /**账号私有菜单*/
            selfMenu : function(id,account){
                flag = true;
                $(thisPage.ul_self_menu).empty();//清空
                indexSelfMenu = layerFn.addOrEdit('账号['+account+']设置私有菜单',thisPage.div_self_menu,layerFn.area.h500xw450,function(){
                    var chks = $(thisPage.ul_self_menu).tree('getChecked');
                    var ids = '';
                    for(var i = 0;i < chks.length;i++){
                        if($.trim(ids).length > 0)
                            ids += ',';
                        ids += chks[i].id;
                    }
                    layerFn.submit(uri+'saveIds', {ids:ids,uid:id},function(data){
                        layer.close(indexSelfMenu);
                        euiFn.showRb(data.code,'操作成功');
                    });
                },'<span style="color:#3b8cff;">全选</span>',function(){
                    var childen = $(thisPage.ul_self_menu).tree('getChildren');
                    if(childen.length <= 0)return;
                    for(var a = 0;a < childen.length;a++){
                        $(thisPage.ul_self_menu).tree('check',childen[a].target);
                    }
                });
                $(thisPage.ul_self_menu).tree(
                    {
                        url : "user/userMenu?keyUid="+id,
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
                            var chks = $(thisPage.ul_self_menu).tree("getChecked");
                            for(var i=0;i<chks.length;i++){
                                $(thisPage.ul_self_menu).tree('expand',chks[i].target);
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
                                /*layer.close(indexSelfMenu);//关闭弹出层*/
                                /*layerFn.center(data.code,'当前没有可用的菜单');*/
                                return '';
                            }else{
                                layerFn.center(data.code,data.msg);
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
            /**去除菜单*/
            removeUserMenu : function(id,account){
                layerFn.handleVerify('确定要去除[<span style="color:#3b8cff;">'+account+'</span>]的私有菜单吗?',function(){
                    layerFn.delByIdHint(uri+'removeUserMenu',id,'操作中,请稍候……',function(data){
                        euiFn.showRb(data.code,data.msg);
                    });
                });
            },
            /**账号分配角色*/
            allotRole : function(id,account){
                flagAllotRole = true;
                $(thisPage.ul_allot_role).empty();//清空
                indexAllotRole = layerFn.addOrEdit('账号[<span style="color:#3b8cff;font-size:16px;">'+account+'</span>]分配角色',thisPage.div_allot_role,layerFn.area.h500xw450,function(){
                    var chks = $(thisPage.ul_allot_role).tree('getChecked');
                    var ids = '';
                    for(var i = 0;i < chks.length;i++){
                        if($.trim(ids).length > 0)
                            ids += ',';
                        ids += chks[i].id;
                    }
                    layerFn.submit(uri+'saveRoleIds', {ids:ids,uid:id},function(data){
                        layer.close(indexAllotRole);
                        euiFn.showRb(data.code,'操作成功');
                        thisPage.search();
                    });
                },'<span style="color:#3b8cff;">全选</span>',function(){
                    var childen = $(thisPage.ul_allot_role).tree('getChildren');
                    if(childen.length <= 0)return;
                    for(var a = 0;a < childen.length;a++){
                        $(thisPage.ul_allot_role).tree('check',childen[a].target);
                    }
                });
                $(thisPage.ul_allot_role).tree(
                    {
                        url : "user/userRole?keyUid="+id,
                        animate : true,
                        dnd : false,
                        checkbox : true,
                        lines : true,
                        onlyLeafCheck : false,
                        onBeforeLoad : function(node,param){
                            if (flagAllotRole){
                                thisPage.zIndex = layerFn.loading.show('正在加载……');
                            }
                        },
                        onLoadSuccess : function(node,data){
                            if (flagAllotRole){
                                flagAllotRole = false;
                                layerFn.loading.hide(thisPage.zIndex);
                            }
                            var chks = $(thisPage.ul_allot_role).tree("getChecked");
                            for(var i=0;i<chks.length;i++){
                                $(thisPage.ul_allot_role).tree('expand',chks[i].target);
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
                                layer.close(indexAllotRole);
                                layerFn.center(data.code,'没有可用的角色');
                                return '';
                            }else{
                                layerFn.center(data.code,data.msg);
                                return '';
                            }
                        },
                        onClick : function(node){
                        },
                        onLoadError : function(response,err){
                            flagAllotRole = false;
                            layerFn.timeoutHint(err);
                        }
                    }
                );
            },
            /**去除角色*/
            removeUserRole : function(id,account){
                layerFn.handleVerify('确定要去除[<span style="color:#3b8cff;font-size:16px;">'+account+'</span>]的角色吗?',function(){
                    layerFn.delByIdHint(uri+'removeUserRole',id,'操作中,请稍候……',function(data){
                        euiFn.showRb(data.code,data.msg);
                        thisPage.search();
                    });
                });
            },
            /**批量分配角色*/
            batchAllotRole : function(){
                var uids = winFn.checkboxBatch('uid');
                if (verifyFn.isEmpty(uids)){
                    layerFn.center(AppKey.code.code201,'请选择要分配角色的账号');
                    return;
                }
                flagBatchAllotRole = true;
                $(thisPage.ul_batch_allot).empty();//清空
                indexBatchAllotRole = layerFn.addOrEdit('批量分配角色',thisPage.div_batch_allot,layerFn.area.h500xw450,function(){
                    var chks = $(thisPage.ul_batch_allot).tree('getChecked');
                    var ids = '';
                    for(var i = 0;i < chks.length;i++){
                        if($.trim(ids).length > 0)
                            ids += ',';
                        ids += chks[i].id;
                    }
                    if (ids == null || ids == ''){
                        layerFn.center(AppKey.code.code199,'请选择角色');
                        return;
                    }
                    layerFn.handleVerify((uids.split(',').length)+'条数据批量分配角色将会删除账号本身原有的角色且无法恢复,确定吗?',function(){
                        layerFn.submit(uri+'batchAllotRole', {rids:ids,uids:uids},function(data){
                            layer.close(indexBatchAllotRole);
                            euiFn.showRb(data.code,data.msg);
                            thisPage.search();
                        });
                    });
                },'<span style="color:#3b8cff;">全选</span>',function(){
                    var childen = $(thisPage.ul_batch_allot).tree('getChildren');
                    if(childen.length <= 0)return;
                    for(var a = 0;a < childen.length;a++){
                        $(thisPage.ul_batch_allot).tree('check',childen[a].target);
                    }
                });
                $(thisPage.ul_batch_allot).tree(
                    {
                        url : "user/queryAllotRole",
                        animate : true,
                        dnd : false,
                        checkbox : true,
                        lines : true,
                        onlyLeafCheck : false,
                        onBeforeLoad : function(node,param){
                            if (flagBatchAllotRole){
                                thisPage.zIndex = layerFn.loading.show('正在加载……');
                            }
                        },
                        onLoadSuccess : function(node,data){
                            if (flagBatchAllotRole){
                                flagBatchAllotRole = false;
                                layerFn.loading.hide(thisPage.zIndex);
                            }
                            var chks = $(thisPage.ul_batch_allot).tree("getChecked");
                            for(var i=0;i<chks.length;i++){
                                $(thisPage.ul_batch_allot).tree('expand',chks[i].target);
                            }
                        },
                        loadFilter : function(data){
                            if (data.code == AppKey.code.code200){
                                return data.listData;
                            }else if(data.code == AppKey.code.code198){
                                layer.close(indexBatchAllotRole);
                                layerFn.center(data.code,data.msg);
                                return '';
                            }else if (data.code == AppKey.code.code207){
                                layerFn.closeEvent(data.code,data.msg,function(){
                                    window.location.href = AppKey.loginUrl;
                                });
                                return '';
                            }else if (data.code == AppKey.code.code201){
                                layer.close(indexBatchAllotRole);
                                layerFn.center(data.code,'没有可用的角色');
                                return '';
                            }else{
                                layerFn.center(data.code,data.msg);
                                return '';
                            }
                        },
                        onClick : function(node){
                        },
                        onLoadError : function(response,err){
                            flagBatchAllotRole = false;
                            layerFn.timeoutHint(err);
                        }
                    }
                );
            },
            /**批量去除账号角色*/
            batchRemoveRole : function(){
                var ids = winFn.checkboxBatch('uid');
                if (verifyFn.isEmpty(ids)){
                    layerFn.center(AppKey.code.code201,'请选择要去除角色的账号');
                    return;
                }
                layerFn.handleVerify((ids.split(',').length)+'条数据去除角色之后将无法恢复,确定吗?',function(){
                    layerFn.delBatchHint(uri+'batchRemoveRole',ids,'操作中,请稍候……',function(data){
                        euiFn.showRb(data.code,data.msg);
                        thisPage.search();
                    });
                });
            },
            /**查看账号的菜单数据*/
            viewMenuUid : function(id,account){
                flagMenuData = true;
                loadMenuData = true;
                $(thisPage.ul_menu_data).empty();//清空
                layerFn.viewDialog('账号['+account+']所有的菜单数据',thisPage.div_menu_data,layerFn.area.h500xw450);
                $(thisPage.ul_menu_data).tree(
                    {
                        url : "user/viewMenuUid?keyUid="+id,
                        animate : true,
                        dnd : false,
                        checkbox : true,
                        lines : true,
                        onlyLeafCheck : false,
                        onBeforeLoad : function(node,param){
                            if (flagMenuData){
                                thisPage.zIndex = layerFn.loading.show('正在加载……');
                            }
                        },
                        onLoadSuccess : function(node,data){
                            if (flagMenuData){
                                flagMenuData = false;
                                layerFn.loading.hide(thisPage.zIndex);
                            }
                            var chks = $(thisPage.ul_menu_data).tree("getChecked");
                            for(var i=0;i<chks.length;i++){
                                $(thisPage.ul_menu_data).tree('expand',chks[i].target);
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
                            }else if (data.code == AppKey.code.code201){//已加载完毕
                                if (loadMenuData){
                                    loadMenuData = false;
                                }
                                return '';
                            }else{
                                layerFn.center(data.code,data.msg);
                                return '';
                            }
                        },
                        onClick : function(node){
                        },
                        onLoadError : function(response,err){
                            flagMenuData = false;
                            layerFn.timeoutHint(err);
                        }
                    }
                );
            },
            /**切换此账号登录*/
            switcherUser : function(uid,account){
                layerFn.confirm('切换账号[<span style="color:#3b8cff;font-size:16px;">'+account+'</span>]登录吗?',function(){
                    layerFn.ajaxRequestHint(uri+'switcherUser',{uid:uid},function(data){
                        layerFn.pageControl(data.code,data.msg);
                    },'正在切换,请稍候……');
                });
            },
            /**操作:(禁用|启用)*/
            enabled : function(uid,account,enabled){
                var status = '启用';
                if(enabled == 0){
                    status = '禁用';
                }
                layerFn.handleVerify('确定要'+status+'[<span style="color:#3b8cff;font-size:16px;">'+account+'</span>]账号吗?',function(){
                    layerFn.submit(uri+'enabled',{uid:uid,enabled:enabled},function(data){
                        euiFn.showRb(data.code,data.msg);
                        thisPage.search();
                    });
                });
            }
        };
        thisPage.init();
    })(jQuery);
</script>
</body>