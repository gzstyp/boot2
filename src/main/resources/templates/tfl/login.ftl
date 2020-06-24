<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <title>登录页面</title>
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
<form id="form_login" name="form_login">
    <table border="0">
        <tr>
            <td>账号：</td>
            <td><input type="text" id="userName" name="userName"/></td>
        </tr>
        <tr>
            <td>密码：</td>
            <td><input type="password" id="passWord" name="passWord"/></td>
        </tr>
        <tr>
            <td><a href="javascript:thisPage.login();">登录</a></td>
            <td><a href="javascript:thisPage.register();">注册</a></td>
        </tr>
    </table>
</form>
<table border="0">
    <tr>
        <td><a href="javascript:confirm('你好,需要服务吗?',function() {alert('好的,谢谢!',function(){alert('嗯,再见!')})});">confirm_alert</a></td>
        <td><a href="javascript:fwtai.msg('什么情况?');">fwtai.msg</a></td>
    </tr>
</table>
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
<script type="text/javascript" src="/js/login.js"></script>
</body>
</html>