<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div region="north" border="false" style="padding:0px; overflow:hidden; height:45px; border: 0px;background:#1e9fff;">
	<div style="position:relative; right:0; top: 0;height:45px;width: 100%;">
		<div style="width: 360px; position:absolute; top: 0;height:45px;line-height:45px; left:0px; color: #fff; margin-left:8px;  font-family:微软雅黑;font-size:22px;">
			<label>${platformName}</label>
		</div>
		<div style="width: auto; position:absolute; top:0; height:45px; line-height:45px; right:0; margin-right: 20px;">
			<c:if test="${!empty switcher_login_user}">
				<a href="javascript:switcher();" class="button button-rounded button-small-select" title="切换原帐号">切换原帐号</a>
			</c:if>
			<c:if test="${code_open == '1'}">
				<a href="javascript:code_craete();" class="button button-rounded button-small-select" title="代码生成">代码生成</a>
			</c:if>
			<i style="color:#fff;font-size:14px;">${login_user}</i>
			<a href="javascript:;" id="editPassword" class="button button-rounded button-small-select" title="修改密码">修改密码</a>
			<a href="javascript:;" id="logout" class="button button-caution button-rounded button-small-huangse" title="退出">退出</a>
		</div>
	</div>
</div>
<div id='div_edit_pwd' style="visibility:hidden;">
	<input type="hidden" id="key_uid" value="${login_key}">
	<div style="margin-top: 15px; text-align: center;"><span style="font-size:14px;color:#6a6a67;">当前密码：</span><span><input style="width: 150px; color: #9ba0d1; height: 24px; line-height: 24px;text-align: left;padding-left:2px;" title="输入当前密码" id="password_current" type="password" placeholder="输入当前密码" /></span><span style="color: red; margin-left: 10px;" >*</span></div>
	<div style="margin-top: 10px; text-align: center;"><span style="font-size:14px;color:#6a6a67;">新的密码：</span><span><input style="width: 150px; color: #03831a; height: 24px; line-height: 24px;text-align: left;padding-left:2px;" title="输入新的密码" id="password_new" type="password" placeholder="输入新的密码" /></span><span style="color: red; margin-left: 10px;" >*</span></div>
	<div style="margin-top: 10px; text-align: center;"><span style="font-size:14px;color:#6a6a67;">确认密码：</span><span><input style="width: 150px; color: #03831a; height: 24px; line-height: 24px;text-align: left;padding-left:2px;" title="输入确认密码" id="password_confirm" type="password" placeholder="输入确认密码"/></span><span style="color: red; margin-left: 10px;" >*</span></div>
</div>
<c:if test="${login_user == 'admin'}">
	<div id='code_create' style="visibility:hidden;">
		<form action="code/create" name="form_code" id="form_code" method="post">
			<table cellspacing="4" cellpadding="0" width="100%" border="0" style="margin-top:7px;">
				<tr>
					<td style="text-align:right;padding-right:4px;font-size:12px;">MyBatis映射文件的命名空间</td><td style="text-align:left;"><input type="text" id='nameSpace' name='nameSpace' class="h24px w160px" placeholder="sql映射的命名,如:user" title="mybatis映射文件的命名空间,如:user,最终生成user.xml"/><span class="required">*</span></td>
				</tr>
				<tr>
					<td style="text-align:right;padding-right:4px;font-size:12px;">实体类名且首字母大写</td><td style="text-align:left;"><input type="text" id='className' name='className' class="h24px w160px" placeholder="实体类名,如:User" title="实体类名且首字母大写"/><span class="required">*</span></td>
				</tr>
				<tr>
					<td style="text-align:right;padding-right:4px;font-size:12px;">数据库表名</td><td style="text-align:left;"><input type="text" id='table' name='table' class="h24px w160px" placeholder="数据库表名" title="数据库表名"/><span class="required">*</span></td>
				</tr>
				<tr>
					<td style="text-align:right;padding-right:4px;font-size:12px;">表的主键字段</td><td style="text-align:left;"><input type="text" id='keyId' name='keyId' class="h24px w160px" placeholder="表的主键字段" title="数据库表的主键字段"/><span class="required">*</span></td>
				</tr>
			</table>
		</form>
	</div>
</c:if>