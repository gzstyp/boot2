/**删除没有权限的按钮*/
function removeAuth(){
    var buttons = $('.authClassToolbar').find("button");
    if (buttons.length > 0){
        $(buttons).each(function(i){
            var id = $(this).attr('id');
            if(id == '' || id == undefined || id == null){
                $(this).remove();
            }
        });
    }
}
/**查询按钮权限控制,调用方法:authBtn('${authBtnId}');*/
function authBtn(authBtnId){
    $.ajax({
        url: 'menu/authBtn',
        type: "POST",
        dataType: "json",
        data:{type:2,authBtnId:authBtnId},
        success: function(result){
            var $_obj = $('.authClassToolbar').find("button");
            if ($_obj.length > 0){
                if (result.code == AppKey.code.code200){
                    result = result.listData;
                    $($_obj).each(function(i){
                        var onclick = $(this).attr('onclick');
                        var _self = this;
                        for(var x = 0; x < result.length; x++){
                            var b = (result[x].uri == onclick);
                            if(b){
                                $(_self).attr('id',i);
                                $(_self).attr('title',result[x].name);
                                $(_self).css('display','inline');
                                break;
                            }
                        }
                    });
                    removeAuth();
                }else{
                    $($_obj).each(function(i){
                        $(this).remove();
                    });
                }
            }else{
                removeAuth();
            }
        },
        error: function(){
            removeAuth();
        }
    });
}
/** 权限树形菜单 */
;(function($){
    var domTree = 'sysTreeMenu';
    var is_loaded = true;
    var setting = {
        view:{
            expandSpeed: 100,
            showLine : true,
            checked : true,
            showIcon : false,
            fontCss : {"color":"#3385ff"}
        },
        async : {
            enable : true,
            url : "menu/ownerMenus",
            cache : false,
            type: "POST",
            autoParam : ["id"],
            dataFilter: function(treeId,parentNode,result){
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
                $.fn.zTree.getZTreeObj(domTree).expandNode(node);
                if($.trim(node.uri).length > 0){
                    var url = node.uri;
                    if(url.indexOf("?")>-1){
                        url +="&treeid="+node.id;
                    }else{
                        url +="?treeid="+node.id;
                    }
                    euiFn.toPage(url,node.name);//跳转页面
                }
            }
        }
    };
    thisPage = {
        init:function(){
            $.fn.zTree.init($("#"+domTree),setting);
        },
    };
    thisPage.init();
})(jQuery);