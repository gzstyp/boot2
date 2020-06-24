<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%
    final String path = request.getContextPath();
    final String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<head>
    <meta charset="utf-8"/>
    <base href="<%=basePath%>">
    <title>jqGrid应用</title>
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>lib/jquery_ui/jquery-ui.theme.css" />
    <link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>lib/jqgrid/ui.jqgrid.css" />
    <style type="text/css">
        a:link,a:visited,a:hover,a:active{
            text-decoration: none;
            outline:none;
            font-size:14px;
        }
    </style>
</head>
<body>

<form id="searchForm" name="searchForm" >
    <table id="table_search" border="0">
        <tr>
            <td><input type="text" placeholder="登录名" id="account" name="account"/></td>
            <td><input type="text" placeholder="登录次数" id="times" name="times"/></td>
            <td><a href="javascript:thisPage.search();">查询</a></td>
            <td><a href="javascript:thisPage.add();">添加</a></td>
            <td><a href="javascript:thisPage.edit();">编辑</a></td>
            <td><a href="javascript:thisPage.delById();">删除</a></td>
        </tr>
    </table>
</form>
<table id="jqGrid"></table>
<div id="jqGridPager"></div>
<div id="editPWd" style="display: none;padding:10px;">
    <form id="form_edit" name="form_edit" >
        <table id="table_edit" border="0">
            <tr>
                <td>密码</td>
                <td><input type="password" id="pwd" name="pwd"/></td>
            </tr>
        </table>
    </form>
</div>
<div id="div_add" style="display: none;padding:10px;">
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
</div>
<script type="text/javascript" src="<%=basePath%>lib/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>lib/layer/layer.js"></script>
<script type="text/javascript" src="<%=basePath%>lib/jqgrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="<%=basePath%>lib/jqgrid/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="<%=basePath%>lib/jquery_ui/jquery-ui.js"></script>
<script type="text/javascript" src="<%=basePath%>lib/page.common.js"></script>
<script type="text/javascript" src="<%=basePath%>system/js/module/page.jqgrid.js"></script>
</body>
</html>