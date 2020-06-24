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
</head>
<body>
<form id="form_reg" name="form_reg">
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
            <td colspan="2">
                <a href="javascript:thisPage.register();">注册</a>
            </td>
        </tr>
    </table>
</form>
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
<script type="text/javascript" src="/js/user_edit.js"></script>
</body>
</html>