<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- Bootstrap -->
<link href="${ctx}/css/bootstrap/bootstrap.min.css" rel="stylesheet" media="screen">
<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/list.css"/>
<script type="text/javascript" src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<style>
.dpfont{
font-size: 15px;
}
</style>
</head>
<body>
<div class="container"> 
  <jsp:include page="../commons/header.jsp"></jsp:include>
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-system.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <form class="form-horizontal" id="userform" role="form" action="" method="post">
                            
              <div class="form-group">
                <div class="col-sm-12 alert alert-info dpfont">
                  <strong>系统错误，请重新尝试</strong>
                </div>
                <div class="col-sm-offset-5 col-sm-10">
                  <button type="button" class="btn btn-default" onclick="window.location.href=document.referrer;">返回</button>
                </div>
              </div>
      </form>
  </div> </div>
  <jsp:include page="../commons/footer.jsp" />
  	<script type="text/javascript">
  	$(function (){	 
  		//$.messager.alert("系统提示", "系统错误，请重新尝试!");
  	});	 
   
	 
	</script>
</div>
</body>
</html>