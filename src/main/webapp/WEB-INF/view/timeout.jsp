<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>未登录或登录超时</title>
<link rel="shortcut icon" href="images/favicon.ico" />
</head>
<body>
<script type="text/javascript">
	var url = "<%=request.getContextPath()%>/login";
    if(self==top){
    	window.location.href = url;
    }else{
    	top.location.href = url;
    }
</script>
</body>
</html>