<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title>商业银行</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- Bootstrap -->
<link href="${ctx}/css/bootstrap/bootstrap.min.css" rel="stylesheet" media="screen">
<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/list.css"/>
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <link rel="stylesheet" type="text/css" href="${ctx}/css/ie_8.css"/>
<![endif]-->
<script type="text/javascript" src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<script src="${ctx}/echarts/esl.js" type="text/javascript" ></script>
<script type="text/javascript">
require.config({
    paths:{ 
        'echarts' : '${ctx}/echarts/echarts',
        'echarts/chart/line' : '${ctx}/echarts/echarts'
    }
});

// 使用
require(
    [
        'echarts',
        'echarts/chart/line'
    ],
function(ec) {
        var myChart = ec.init(document.getElementById('data')); 
         option = ${cpu} 
           myChart.setOption(option); 
    }
);
</script>
</head>
<body>
<div class="container"> 
  <jsp:include page="../commons/header.jsp"></jsp:include>
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-system.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
     <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>系统监控</a> > <a>CPU</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <div id="data" style="background-color: white; border: solid 1px black; width: 100%;height:600px">
		  
	     </div>
        </div>
      </div>
    </div>
    <div class="clearfix"></div>
  </div>
<jsp:include page="../commons/footer.jsp"></jsp:include>
</body>
</html>