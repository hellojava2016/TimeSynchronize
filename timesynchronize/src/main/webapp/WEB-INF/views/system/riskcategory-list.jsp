<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
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
</head>
<script type="text/javascript">
$(function (){
	var t = ${riskId};
	if(0!=t){
		$.messager.model = { 
		        ok:{ text: "确定", classed: 'btn-success' },
		        cancel: { text: "取消", classed: 'btn-primary' }
		      };
		 $.messager.alert("商业银行信息科技风险综合监管平台", "修改监控指标成功！");
	}
});	  
</script>
<body>
<div class="container"> 
  <jsp:include page="../commons/header.jsp"></jsp:include>
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-system.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> &gt; <a>系统配置</a> &gt; <a>监控指标配置</a></div>

      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">查询</h3>
          <div class="show_conment pad_5">
          
          <table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>指标类型</th><th>上报周期</th><th>是否允许上报</th><th>是否允许门限</th><th>最小门限</th><th>最大门限</th><th>管理</th></tr></thead>
		<tbody>
		<c:choose>
			 	<c:when test="${!empty rates}">
		<c:forEach items="${rates}" var="rate">
			<tr <c:if test="${rate.riskId==riskId}">class="success"</c:if> style="height:50px;">
				<td>${rate.riskName}</td>
				<td>${rate.cycleString}</td>				
				<td><c:choose><c:when test="${rate.allowReport}">是</c:when><c:otherwise><font color="red">否</font></c:otherwise></c:choose></td>  
				<td><c:choose><c:when test="${rate.useThresholdValue}">是</c:when><c:otherwise><font color="red">否</font></c:otherwise></c:choose></td>  
				<td>${rate.minValue}</td>	
				<td>${rate.maxValue}</td>			
				<td>	<c:set var="permisions" value="${permisions}" scope="session" /> 
		<c:if test="${fn:contains(permisions, 'system:riskcategory')}"><a href="${ctx}/riskcategory/update_pre.do?id=${rate.riskId}">修改 </a> </c:if><c:if test="${rate.hasLeaf}"><a href="${ctx}/riskcategory/list.do?riskCode=${rate.riskCode}">查看子指标 </a></c:if></td>
			</tr>
		</c:forEach>
		 </c:when>
						<c:otherwise>
							<tr>
								<td colspan="7" class="emptyResut">暂无数据</td>
							</tr>
						</c:otherwise>
					</c:choose>
		</tbody>
	</table>
	


        </div></div>
      </div>
    </div>
  </div>
  <jsp:include page="../commons/footer.jsp" />
</div>
</body>
</html>