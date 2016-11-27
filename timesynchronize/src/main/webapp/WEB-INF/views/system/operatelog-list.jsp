<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- Bootstrap -->
<link href="${ctx}/css/bootstrap/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/list.css" />
<link rel="stylesheet" type="text/css" media="screen"  href="${ctx}/css/bootstrap/bootstrap-datetimepicker.min.css">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <link rel="stylesheet" type="text/css" href="${ctx}/css/ie_8.css"/>
<![endif]-->
<script type="text/javascript" src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/moment-with-langs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/bootstrap-datetimepicker.min.js"></script>
<script src="${ctx}/js/bootstrap/bootstrap.min.js"
	type="text/javascript"></script>
	<script type="text/javascript">
$(function (){
	$('.input-append').datetimepicker({
        format: 'YYYY-MM-DD',
        language: 'zh-CN',
        pickDate: true,
        pickTime: false,
        inputMask: false
      });
});	  
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
				<div class="breadcrumb">
					<span>您当前所在的位置：</span> &gt; <a>系统配置</a> &gt; <a>操作日志管理</a>
				</div>

				<div class="row whitebackground mb_10">
					<div class="span12">
						<h3 class="show_tit">查询</h3>
						<div class="show_conment">
							<form class="form-horizontal"
								action="${ctx}/operatelog/query.do?">
								<div class="col-xs-12 row">
									<div class="col-xs-2 pr_0 pl_0 mr_5 wid_20">
										<span>所属机构：</span> 
										<select name="Organization" class="selectcsss" style="width: 120px;"  id="Organization">
											<c:forEach items="${orgs}" var="org">
												<option value=${org.orgId} <c:if test="${selectedorgid==org.orgId}">selected</c:if>>${org.name}</option>
											</c:forEach>
										</select>
									</div>

									<div class="col-xs-2 pr_0 pl_0 mr_5 wid_20">
										<span>操作类型</span> <select name="action" class="selectcsss" style="width: 120px;">
											<option value="">所有类型</option>
											<c:forEach items="${actions}" var="temp">
												<option value="${temp.key}"
													<c:if test="${action==temp.key}">selected</c:if>>${temp.value}</option>
											</c:forEach>
										</select>
									</div>

									<label class="col-xs-1 control-label pr_0">起始时间</label>
									<div class="col-xs-2 input-append date">
										<input type="text" class="pick_timeshow" name="startdate"
											id="startdate" value="${startdate}" />
									</div>

									<label class="col-xs-1 control-label pr_0">结束时间</label>
									<div class="col-xs-2 input-append date">
										<input type="text" class="pick_timeshow" name="enddate"
											id="enddate" value="${enddate}" />
									</div>

								</div>


								<div class="form-group">
									<div class="col-xs-12 row">
										<div class="col-xs-5"></div>
										<div class="col-xs-2 pr_0 pl_0 mr_5 wid_20 buttonpadding">
											<button class="btn btn-primary mr_20" id="querybotton"
												type="submit">
												<span class="glyphicon glyphicon-search"></span>查询
											</button>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
				<div></div>
				<div class="row show mb_10">
					<div class="span12">
						<h3 class="show_tit">操作日志列表</h3>
						<div class="show_conment pad_5">
								<table id="contentTable"
									class="table table-striped table-bordered table-condensed">
									<thead>
										<tr>
											<th>所属机构</th>
											<th>操作用户</th>
											<th>操作类型</th>
											<th>操作时间</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${rates.result}" var="rate">
											<tr>
												<td>${rate.user.organization.name}</td>
												<td>${rate.user.username}</td>
												<td>${rate.action}</td>
												<td><fmt:formatDate value="${rate.operateTime}"
														pattern="yyyy-MM-dd HH:mm:ss"/></td>
											</tr>
										</c:forEach>
										 <tr >
					<th colspan="4"><tags:pagination page="${rates}" paginationSize="5"/></th>
			</tr>
									</tbody>
								</table>


						</div>
					</div>
				</div>
			</div>
		</div>
		<jsp:include page="../commons/footer.jsp"></jsp:include>
	</div>
</body>
</html>