<%@ page pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>上传文件</title>
</head>
<body>
	<form action="${ctx }/upload.do" method="post" enctype="multipart/form-data">
		<input type="file" name="file" /> 
		<input type="submit" value="Submit" />
	</form>
</body>
</html>