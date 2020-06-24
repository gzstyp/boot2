<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>ftl使用</title>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
    <!-- icheck样式库 -->
    <link rel="stylesheet" type="text/css" href="/lib/icheck/checkboxRadio.css"/>
    <link rel="stylesheet" type="text/css" href="/lib/icheck/blue.css"/>
    <link rel="stylesheet" type="text/css" href="/lib/easyui/themes/metro/easyui.css">
    <link rel="stylesheet" type="text/css" href="/lib/easyui/themes/icon.css" />
    <link rel="stylesheet" type="text/css" href="/lib/ztree/css/metroStyle/metroStyle.css" media="screen"/>
    <style type="text/css">
        a:link,a:visited,a:hover,a:active{
            text-decoration: none;
            outline:none;
        }
    </style>
</head>
<body>
<a href="javascript:thisPage.search();">查询</a>
<div id="lb"></div>
<script type="text/javascript" src="/lib/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/lib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="/lib/layer/layer.js"></script>
<script type="text/javascript" src="/lib/jquery-ui.min.js"></script>
<script type="text/javascript" src="/lib/select-widget-min.js"></script>
<script type="text/javascript" src="/lib/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/lib/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="/lib/datagrid.tips.js"></script>
<script type="text/javascript" src="/lib/icheck/icheck.min.js"></script><!-- icheck库 -->
<script type="text/javascript" src="/lib/jquery.placeholder.min.js"></script>
<script type="text/javascript" src="/lib/page.common.js"></script><!-- 自定义封装及插件库 -->
<script type="text/javascript" src="/lib/jquery/select.area.js"></script>
<script type="text/javascript" src="/lib/ztree/js/jquery.ztree.core.min.js"></script>
<script type="text/javascript" src="/js/user_list.js"></script>
<script type="text/javascript">
    ;(function($){
        thisPage = {
            init : function () {
                layerFn.ajaxGetHint('/user/listData','正在读取……',{},function (data){
                    if(data.code === AppKey.code.code200){
                        var html = '';
                        for(var i=0;i<data.listData.length;i++){
                            html += data.listData[i].account+","+data.listData[i].added+"<br/>";
                        }
                        $('#lb').html(html);
                    }else{
                        layerFn.center(data.code,data.msg)
                    }
                },function () {
                    layerFn.center(AppKey.code.code199,'连接服务器失败!')
                });
            },
            search : function () {
                thisPage.init();
            }
        }
        thisPage.init();
    })(jQuery);
</script>
</body>
</html>