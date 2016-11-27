<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title>商业银行</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- Bootstrap -->
<link href="${ctx}/css/bootstrap/bootstrap.min.css" rel="stylesheet" media="screen">
<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/list.css"/>
<script type="text/javascript" src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <link rel="stylesheet" type="text/css" href="${ctx}/css/ie_8.css"/>
      <script src="${ctx}/js/respond.min.js" ></script>
<![endif]-->
</head>
<script type="text/javascript">
$(function (){
	$("#dpminheight").css({"min-height":"600px"});
});	
var pageNo=${pageNo};
function bubaobutton(){
	window.location.href="${ctx}/reportadditional/add_pre.do?type=bank&pageNo="+pageNo;
}
function bubaobutton_cbrc(){
	window.location.href="${ctx}/reportadditional/add_pre.do?type=cbrc&pageNo="+pageNo;
}
function shouquan(id){
	$.messager.model = { 
	        ok:{ text: "确定", classed: 'btn-primary' },
	        cancel: { text: "取消", classed: 'btn-error' }
	      };
	      $.messager.confirm("系统提示", "您确定授权所选补报？", function() { 
	    	  window.location.href="${ctx}/reportadditional/auth.do?id="+id+"&pageNo="+pageNo;
	      }); 
}
function deleteRates() {
	var deleteroleids = "";
	$("input[name='rolecheckbox']:checked").each(function() {
		deleteroleids += $(this).val() + ",";
	});
	if ("" == deleteroleids) {
		$.messager.alert("系统提示", "请选择要删除数据！");
		return;
	}
	$.messager.model = {
		ok : {
			text : "确定",
			classed : 'btn-primary'
		},
		cancel : {
			text : "取消",
			classed : 'btn-error'
		}
	};
	$.messager.confirm("系统提示", "您确定删除所选数据？", function() {
		window.location.href="${ctx}/reportadditional/delete.do?id="+deleteroleids;
	});
}
function selectAll(object) {
	if (object.checked) {
		$("input[name='rolecheckbox']").prop("checked", true);
	} else {
		$("input[name='rolecheckbox']").prop("checked", false);
	}
	return false;
}

</script>
<body>
<div class="container"> 
  <jsp:include page="../commons/header.jsp" />
		<div id="content" class="row mt_78">
			<div class="col-md-2" id="left">
				<jsp:include page="left-system.jsp" />
			</div>
			<div class="col-md-10" id="right">
				<div class="breadcrumb">
					<span>您当前所在的位置：</span> > <a>系统配置</a> > <a>补报配置</a>
				</div>
				
		<div class="row whitebackground mb_10">
        <div class="span12">
          <h3 class="show_tit">查询</h3>
          <div class="show_conment">
            <form class="form-horizontal" action="${ctx}/reportadditional/query.do?">
								<div class="col-xs-12 row">
									<label class="col-xs-1 control-label pr_0">金融机构：</label>
									<div class="col-xs-3 pr_0 pl_0 mr_5">
										<select name="Organization" class="selectcsss"
											id="Organization">
											<c:forEach items="${orgs}" var="org">
												<option value=${org.orgId
													} <c:if test="${selectedorgid==org.orgId}">selected</c:if>>${org.name}</option>
											</c:forEach>
										</select>
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
	<h4 class="showtit4">补报列表</h4>
	</div>
	<div class="pull-right">
	<c:set var="permisions" value="${permisions}" scope="session" /> 
		<c:if test="${fn:contains(permisions, 'risk:append:request')}">
		<button class="btn btn-primary mr_20" id="addtaskbotton" type="button" onclick="bubaobutton();">
			<span class="glyphicon glyphicon-plus"></span>申请补报
		</button>
		 </c:if>
		 <c:if test="${fn:contains(permisions, 'system:append:auth')}">
		<button class="btn btn-primary mr_20" type="button" id="modifyButton" onclick="bubaobutton_cbrc();">
			<span class="glyphicon glyphicon-edit"></span>添加补报
		</button>
		 </c:if>
		 <c:if test="${fn:contains(permisions, 'system:append:delete')}">
		 <button class="btn btn-primary mr_20" type="button" id="deleteButton" onclick="deleteRates();">
			<span class="glyphicon glyphicon-trash"></span>&nbsp;&nbsp;删除
		</button>
		 </c:if>
	</div>
</div>
          
          </div>
						<div class="show_conment pad_5">

							<table id="contentTable"
								class="table table-striped table-bordered table-condensed">
								<thead>
									<tr>
									<th><input type="checkbox" id="allroleselected"
												onclick="selectAll(this)" /></th>
										<th>所属机构</th>
										<th>补报类型</th>
										<th>补报申请人</th>
										<th>补报日期</th>
										<th>补报开始时间</th>
										<th>补报结束时间</th>
										<th>授权状态</th>
										<th>补报完成</th>
										<th>管理</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${!empty additionals.result }">
											<c:forEach items="${additionals.result}" var="additional">
												<tr>
													<td><input type="checkbox" name="rolecheckbox" value="${additional.id}" /></td>
													<td>${additional.organization.name}</td>
													<td>${additional.riskCategory.riskName}</td>
													<td>${additional.user.username}</td>
													<td><fmt:formatDate value="${additional.reportDate}" pattern="yyyy-MM-dd"/></td>
													<td><fmt:formatDate value="${additional.beginTime}" pattern="yyyy-MM-dd"/></td>
													<td><fmt:formatDate value="${additional.endTime}" pattern="yyyy-MM-dd"/></td>
													<td><c:choose>
															<c:when test="${additional.audit}">已授权</c:when>
															<c:otherwise>
																<font color="red">未授权</font>
															</c:otherwise>
														</c:choose></td>
													<td><c:choose>
															<c:when test="${additional.reported}">已补报</c:when>
															<c:otherwise>
																<font color="red">未补报</font>
															</c:otherwise>
														</c:choose></td>
													<td>
														<c:if test="${fn:contains(permisions, 'system:append:auth')}">
													<c:if test="${additional.audit!=true}"><a href="${ctx}/reportadditional/auth.do?id=${additional.id}" onClick="shouquan('${additional.id}');return false;">授权补报</a></c:if>
													 </c:if>
													</td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td colspan="10" class="emptyResut">暂无数据</td>
											</tr>
										</c:otherwise>
									</c:choose>
									
										<tr style="background-color:#eee">
											<th colspan="10"><tags:pagination page="${additionals}" paginationSize="5"/></th>
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

</body>
</html>