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
	
	$("#dpminheight").css({"min-height":"600px"});
});	 
function deletemsg(id){
	 $.ajax({ url: "<%=request.getContextPath()%>/rmessage/delete.do", 
		 data:{id:id},
		 success: function(msg){
	        window.location.href="<%=request.getContextPath()%>/rmessage/list.do?page=${pageNo}"
	 }});
}
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
					<span>您当前所在的位置：</span> &gt; <a>系统配置</a> &gt; <a>我的消息</a>
				</div>

				<div class="row whitebackground mb_10">
					<div class="span12">
						<h3 class="show_tit">查询</h3>
						<div class="show_conment">
							<form class="form-horizontal"
								action="${ctx}/rmessage/list.do?">
								<div class="col-xs-12 row">

									<label class="col-xs-2 control-label pr_0">起始发布时间</label>
									<div class="col-xs-2 input-append date">
										<input type="text" class="pick_timeshow" name="startdate"
											id="startdate" value="${startdate}" />
									</div>

									<label class="col-xs-2 control-label pr_0">结束发布时间</label>
									<div class="col-xs-2 input-append date">
										<input type="text" class="pick_timeshow" name="enddate"
											id="enddate" value="${enddate}" />
									</div>

								</div>


								<div class="form-group">
									<div class="col-xs-12 row">
										<div class="col-xs-4"></div>
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
				
				<div class="row show mb_10" id="dpminheight">
					<div class="span12">
 <h3 class="show_tit">我的列表</h3>
          
						<div class="show_conment pad_5">
								<table id="contentTable"
									class="table table-striped table-bordered table-condensed">
									<thead>
										<tr>
											<th>公告标题</th>
											<th>发布机构</th>
											<th>发布时间</th>
											<th>紧急重要度</th>
											<th>公告状态</th>
											<th>下载状态</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
									<c:when test="${!empty bankmessages.result }">
										<c:forEach items="${bankmessages.result}" var="rate">
											<tr>
												<td>${rate.message.title}</td>
												<td>${rate.message.sendUser.name}</td>
												<td><fmt:formatDate value="${rate.message.sendTime}"
														pattern="yyyy-MM-dd HH:mm:ss" /></td>
												<td>${rate.message.criticalStr}</td>
													<td><c:choose><c:when test="${rate.hasRead}">已读</c:when><c:otherwise><font color='red'>未读</font></c:otherwise></c:choose></td>	
													<td><c:choose><c:when test="${rate.download}">已下载</c:when><c:otherwise><font color='red'>未下载</font></c:otherwise></c:choose></td>			
												<td><a href="${ctx}/rmessage/updatepre.do?id=${rate.id}">查看 </a>&nbsp;&nbsp;
												<c:if test="${rate.download}"><c:if test="${rate.hasRead}"><a href="" onclick="deletemsg(${rate.id});return false;">删除 </a></c:if></c:if>
												</td>		
											</tr>
										</c:forEach>
											</c:when>
										<c:otherwise>
											<tr>
												<td colspan="7" class="emptyResut">暂无数据</td>
											</tr>
										</c:otherwise>
										</c:choose>
										 <tr >
					                        <th colspan="7"><tags:pagination page="${bankmessages}" paginationSize="6"/></th>
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