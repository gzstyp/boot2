<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<body>
<style type="text/css">
    div#right_button_menu {position:absolute; visibility:hidden; top:0; background-color: #f2f2f2;text-align: left;padding: 2px;}
    div#right_button_menu ul li{
        margin: 1px 0;
        padding: 0 5px;
        cursor: pointer;
        list-style: none outside none;
        background-color:#fff;/*li的背景颜色*/
    }
    div#right_button_menu ul{
        margin-top:-1px;/*列表顶部的距离*/
        margin-bottom:-1px;/*列表底部的距离*/
        color:#3287ff;/*字体颜色*/
    }
    /*鼠标滑过字体颜色*/
    div#right_button_menu ul li:hover{
        color:#505050;/*字体颜色*/
    }
</style>
<div fit="true" class="easyui-panel" border="false" style="padding-top:4px;padding-left:8px;">
    <div style="margin-left:-6px;margin-top:-6px;">
        <ul id="sys_department_tree" class="ztree"></ul>
    </div>
</div>
<div id="right_button_menu">
    <ul>
        <li id="m_add" title="添加节点" onclick="thisPage.addTreeNode();">添加机构</li>
        <li id="m_edit" title="编辑节点" onclick="thisPage.editTreeNode();">编辑机构</li>
        <li id="m_del" title="删除节点" onclick="thisPage.removeTreeNode();">删除机构</li>
        <li id="m_refresh_node" title="刷新当前的子节点" onclick="thisPage.refreshNode();">刷新节点</li>
        <li id="m_reset" title="重置刷新整个节点" onclick="thisPage.resetTree();">重置刷新</li>
    </ul>
</div>
<div id="div_dep_edit" style="padding-top:20px;padding-left:30px;">
    <input id="input_dep_edit" name="input_dep_edit" placeholder="组织机构名称" style="height:24px;width:164px;"/>
    <label style="cursor:pointer;">是父节点<input type="checkbox" id="input_is_parent" name="input_is_parent" style="display:inline-block;vertical-align:middle;margin-bottom:2px;"/></label>
</div>
<script type="text/javascript">
    var domTree = 'sys_department_tree';
    var is_loaded = true;
    ;(function ($){
        var uri = 'sys_department/';/*请求controller层的url*/
        var rightButtonMenu = $("#right_button_menu");
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
                url : uri + "queryAllDepartment",
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
                        parent.layer.msg('正在加载,请稍候……',{icon:16,area:'216px',time:-1,shade:[0.3,'#000']});
                    }
                },
                onAsyncSuccess : function(data){
                    layerFn.loading.closeAll();
                },
                onAsyncError : function(){
                    layerFn.alert(AppKey.msg_error);
                },
                onClick : function(event,treeIdDom,node,clickFlag){
                },
                onRightClick : function(event,treeId,treeNode){
                    var rightTree = $.fn.zTree.getZTreeObj(domTree);
                    if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
                        rightTree.cancelSelectedNode();
                        thisPage.showRMenu("root", event.clientX,event.clientY);
                    } else if (treeNode && !treeNode.noR){
                        rightTree.selectNode(treeNode);
                        thisPage.showRMenu("node",event.clientX,event.clientY);
                    }
                },
            }
        };
        thisPage = {
            init : function(){
                this.initDatagrid();
                $.fn.zTree.init($("#"+domTree),setting);//初始化树形菜单及并读取数据
                winFn.iePlaceholder();
            },
            initDatagrid : function(){
            },
            /**搜索*/
            search : function(){
                euiFn.refreshDatagrid(thisPage.datagrid,winFn.formAjaxParams('#search_sys_department'),1);
            },
            /**打开提交对话框,含编辑及添加*/
            openDialog : function(kid,name,isParent){
                var title = verifyFn.isEmpty(name) ? '添加':'编辑';
                var url = verifyFn.isEmpty(name) ? 'add':'edit';
                layerFn.addOrEdit(title + '组织机构','#div_dep_edit',['300px','150px'],function(index){
                    var dep_name = winFn.getDomValue('#input_dep_edit');
                    if (verifyFn.inputRequired('#input_dep_edit')){
                        layerFn.center(AppKey.code.code201,'请输入组织机构名称');
                        return;
                    }
                    var isChecked = winFn.checkboxSelect('#input_is_parent')
                    var params = {
                        kid : kid,
                        dep_name : dep_name,
                        isParent : (isChecked) ? 1 : 0,
                    };
                    layerFn.submit(uri + url,params,function(data){
                        layerFn.layerClose(index);//成功时才关闭对话框
                        euiFn.showRb(data.code,data.msg);
                        if(verifyFn.isEmpty(name)){
                            thisPage.refreshNode();
                        }else{
                            thisPage.refreshParentNode();
                        }
                    });
                });
                if(verifyFn.isEmpty(name)){
                    winFn.setDomValue('#input_dep_edit','');
                }else{
                    winFn.setDomValue('#input_dep_edit',name);
                }
                $('#input_is_parent').prop('checked',isParent);//是否是父节点
                if(verifyFn.isEmpty(name)){
                    $('#input_is_parent').prop('checked',false);
                }
            },
            /**删除行*/
            delById : function(kid,parent_id,name){
                layerFn.confirm(name+'<br/>删除后将无法恢复,确定吗?',function(){
                    layerFn.submit(uri + 'delById',{kid:kid,parent_id:parent_id},function(data){
                        euiFn.showRb(data.code,data.msg);
                        thisPage.refreshParentNode();
                    })
                });
            },
            /**显示右键菜单*/
            showRMenu : function(type, x, y){
                $("#right_button_menu ul").show();
                if (type=="root"){
                    $("#m_del").hide();
                    $("#m_edit").hide();
                    $("#m_refresh_node").hide();
                } else {
                    $("#m_del").show();
                    $("#m_edit").show();
                    $("#m_refresh_node").show();
                }
                y += document.body.scrollTop;
                x += document.body.scrollLeft;
                rightButtonMenu.css({"top":(y-36)+"px", "left":(x-256)+"px","visibility":"visible"});/*设置右键离列表的距离*/
                $("body").bind("mousedown",thisPage.onBodyMouseDown);
            },
            /**隐藏右键菜单*/
            hideRMenu : function(){
                if (rightButtonMenu) rightButtonMenu.css({"visibility":"hidden"});
                $("body").unbind("mousedown",thisPage.onBodyMouseDown);
            },
            onBodyMouseDown : function(event){
                if (!(event.target.id == "right_button_menu" || $(event.target).parents("#right_button_menu").length>0)) {
                    rightButtonMenu.css({"visibility":"hidden"});
                }
            },
            /**添加节点*/
            addTreeNode : function(){
                thisPage.hideRMenu();
                var getNodes = $.fn.zTree.getZTreeObj(domTree).getSelectedNodes()[0];
                if (getNodes){
                    var isParent = getNodes.isParent?1:0;/*是否是父级菜单(1为true0为false)*/
                    if(isParent ==0){
                        layerFn.showCT(AppKey.code.code199,'不是父节点不能添加');
                        return;
                    }else{
                        thisPage.openDialog(getNodes.kid);
                    }
                } else {
                    //zTree.addNodes(null,newNode);//此处添加的是最最顶级|上层的节点
                    <c:choose>
                    <c:when test="${login_user == 'admin'}">
                        thisPage.openDialog(null,null,false);//此处可以添加最最上层顶层
                    </c:when>
                    <c:otherwise>
                        layerFn.showCT(AppKey.code.code199,'无权限操作');
                    </c:otherwise>
                    </c:choose>
                }
            },
            /**编辑节点*/
            editTreeNode : function(){
                thisPage.hideRMenu();
                var getNodes = $.fn.zTree.getZTreeObj(domTree).getSelectedNodes()[0];
                if (getNodes){
                    thisPage.openDialog(getNodes.kid,getNodes.name,getNodes.isParent);
                }
            },
            /*删除节点*/
            removeTreeNode : function(){
                thisPage.hideRMenu();
                var delTree = $.fn.zTree.getZTreeObj(domTree);
                var nodes = delTree.getSelectedNodes();
                if(nodes.length > 0){
                    var isParent = nodes[0].isParent;
                    if(isParent){
                        layerFn.showCT(AppKey.code.code199,'请先删除子节点或取消父节点标识');
                    }else{
                        thisPage.delById(nodes[0].kid,nodes[0].parent_id,nodes[0].name);
                    }
                }else{
                    layerFn.showCT(AppKey.code.code199,'请选择节点');
                }
            },
            /**重置到只有最最上层的节点*/
            resetTree : function(){
                thisPage.hideRMenu();
                is_loaded = true;
                $.fn.zTree.init($("#"+domTree),setting);
            },
            /**刷新当前节点的子节点数据*/
            refreshNode :function(){
                thisPage.hideRMenu();
                /*根据 treeId dom获取 zTree 对象*/
                var refreshTree = $.fn.zTree.getZTreeObj(domTree);
                /*获取zTree 当前被选中的节点数据集合*/
                var nodes = refreshTree.getSelectedNodes();
                if(nodes.length > 0){
                    /*强行异步加载父节点的子节点。[setting.async.enable = true 时有效]*/
                    refreshTree.reAsyncChildNodes(nodes[0],"refresh",false);
                }else{
                    layerFn.showCT(AppKey.code.code199,'请选择节点');
                }
            },
            /**刷新当前节点的父级节点*/
            refreshParentNode : function(){
                thisPage.hideRMenu();
                var ParentTree = $.fn.zTree.getZTreeObj(domTree);
                var nodes = ParentTree.getSelectedNodes();
                if(nodes.length > 0){
                    /*根据zTree的唯一标识tId快速获取节点JSON数据对象*/
                    var parentNode = ParentTree.getNodeByTId(nodes[0].parentTId);
                    /*选中指定节点*/
                    ParentTree.selectNode(parentNode);
                    ParentTree.reAsyncChildNodes(parentNode,"refresh",false);
                }else{
                    layerFn.showCT(AppKey.code.code199,'请选择节点');
                }
            },
        };
        thisPage.init();
    })(jQuery);
</script>
</body>