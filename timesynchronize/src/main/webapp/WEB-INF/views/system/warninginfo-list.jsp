<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>商业银行</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- Bootstrap -->
<link rel="stylesheet" type="text/css" href="${ctx}/css/powerFloat.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/xmenu.css" />
<link href="${ctx}/css/bootstrap/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/list.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="${ctx}/css/bootstrap/bootstrap-datetimepicker.min.css">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <link rel="stylesheet" type="text/css" href="${ctx}/css/ie_8.css"/>
      <script src="${ctx}/js/respond.min.js" ></script>
<![endif]-->
<script type="text/javascript" src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-powerFloat-min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-xmenu.js"></script>
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/moment-with-langs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/bootstrap-datetimepicker.min.js"></script>
<script src="${ctx}/js/select/jquery.cityselect.js" type="text/javascript" ></script> 
<script src="${ctx}/js/upload/jquery.ui.widget.js" type="text/javascript" ></script>
<script src="${ctx}/js/upload/jquery.iframe-transport.js"" type="text/javascript" ></script>
<script src="${ctx}/js/upload/jquery.fileupload.js" type="text/javascript" ></script>	
<script type="text/javascript">
	$(function() {
		$.ajax({ url: "<%=request.getContextPath()%>/report/infotechnologyriskeventcount/getOrgs.do", 
			 data:{},
			 success: function(msg){
		        $("#dappendhtml").empty().html(msg);
		        $("#selectdept").xMenu({
		    		width :600, 
		    		eventType: "click", 
		    		dropmenu:"#mdongp",
		    		hiddenID : "selectdeptidden"	
		    		
		    	});
		 }});
});
</script>
<body>
	<div id="mdongp" class="xmenu" style="display: none;">
		<div class="select-info">
			<ul id="dliremoveid">
			</ul>
			<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
				<span class="a-btn-text">确定</span>
			</a>
		</div>

		<dl id="dappendhtml">

		</dl>

	</div>
	<div class="container">
		<jsp:include page="../commons/header.jsp" />
		<div id="content" class="row mt_78">
			<div class="col-md-2" id="left">
				<jsp:include page="left-system.jsp" />
			</div>
			<div class="col-md-10" id="right">
				<div class="breadcrumb">
					<span>您当前所在的位置：</span> > <a>系统配置</a> > <a>风险预警信息列表</a>
				</div>

				<div class="row whitebackground mb_10">
					<div class="span12">
						<h3 class="show_tit">查询</h3>
						<div class="show_conment">
							<form class="form-horizontal"
								action="${ctx}/warninginfo/query.do?">
								<div class="col-xs-12 row">
									<label class="col-xs-1 control-label pr_0">预警类型：</label>
									<div class="col-xs-3 pr_0 pl_0 mr_5">
										<select name="warningtype" class="selectcsss" id="warningtype">
											<option value=1
												<c:if test="${selectedwarningtype==1}">selected</c:if>>指标连续下降</option>
											<option value=2
												<c:if test="${selectedwarningtype==2}">selected</c:if>>指标偏离平均值</option>
										</select>
									</div>

									<label class="col-xs-1 control-label pr_0 wid_10">金融机构：</label>
									<div class="col-xs-2  pr_0 pl_0 mr_10 topnav">
										<a id="selectdept" href="javascript:void(0);" class="as">
											<span>选择金融机构</span>
										</a> <input type="hidden" value="${category}" id="selectdeptidden" />
									</div>
								</div>

								<div class="form-group">
									<div class="col-xs-12 row">
										<div class="col-xs-5"></div>
										<div class="col-xs-2 pr_0 pl_0 mr_5 wid_20 buttonpadding">
											<button class="btn btn-primary mr_20" id="querybotton" type="button" onclick="refresh();">
			                                   <span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;查询
		                                    </button>
										</div>
									</div>
								</div>

								<div class="col-xs-12 row dpheight15"></div>
							</form>
						</div>
					</div>
				</div>
				<div>

					<div class="row show mb_10" id="dpminheight">
						<div class="span12">
							<div class="show_tit3">
								<div id="operateBtns">
									<div class="pull-left">
										<h4 class="showtit4">预警信息列表</h4>
									</div>
								</div>

							</div>
							<div class="show_conment pad_5">

								<table id="contentTable"
									class="table table-striped table-bordered table-condensed">
									<thead>
										<tr>
											<th>所属机构</th>
											<th>指标类型</th>
											<th>指标日期(期数)</th>
											<th>预警类型</th>
											<th>指标值</th>											
											<th>预警详情</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${!empty infos.result }">
												<c:forEach items="${infos.result}" var="info">
													<tr>
														<td>${info.org.name}</td>
														<td>${info.riskName}</td>
														<td>${info.showDate}</td>
														<td>${info.warningTypeStr}</td>
														<td>${info.value}</td>
														<td>${info.memos}</td>
													</tr>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr>
													<td colspan="10" class="emptyResut">暂无数据</td>
												</tr>
											</c:otherwise>
										</c:choose>

										<tr style="background-color: #eee">
											<th colspan="10"><tags:pagination page="${infos}"
													paginationSize="5" /></th>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			<jsp:include page="../commons/footer.jsp" />
		</div>
<script type="text/javascript">
var pageNo =${pageNo};
function getQueryParam(){
	var category =$("#selectdeptidden").val();	
	var warningtype =$("#warningtype").val();
	return "warningtype="+warningtype+"&category="+category+"&pageNo="+pageNo;
}
function refresh(){
	window.location.href="${ctx}/warninginfo/query.do?"+getQueryParam();
}
</script>
</body>
</html>