<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
final String path = request.getContextPath();
final String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="utf-8" />
    <base href="<%=basePath%>">
    <title>${platformName}</title>
    <link rel="shortcut icon" href="images/favicon.ico" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
  </head>
  <body>
   <script type="text/javascript" >
		window.location.href = "<%=basePath%>login";
	</script>
  </body>
</html>