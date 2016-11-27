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
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <link rel="stylesheet" type="text/css" href="${ctx}/css/ie_8.css"/>
<![endif]-->
<script type="text/javascript" src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
</head>
<body>
<div class="container"> 
  <jsp:include page="../commons/header.jsp" />
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-system.jsp" />
    </div>
    
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>系统配置</a> > <a>用户列表</a></div>
      <div class="row show mb_10">
        <div class="span12">
           <div class="show_tit3">
           <div id="operateBtns">
	<div class="pull-left">
	<h4 class="showtit4">用户列表</h4>
	</div>
	<div class="pull-right">
	    <c:if test="${fn:contains(permisions, 'user:add')}">
		<button class="btn btn-primary mr_20" id="addtaskbotton" type="button" onclick="newreport();">
			<span class="glyphicon glyphicon-plus"></span>添加用户
		</button>
		</c:if>
		<c:set var="permisions" value="${permisions}" scope="session" /> 
	</div>
</div>
          
          </div>
          <div class="show_conment pad_5">
          	<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th><input type="checkbox" id="alluserselected" onclick="selectAll(this)"/></th>
						<th>用户名</th>
						<th>机构</th>
						<th>角色</th>						
						<th>电话</th>						
						<th>创建时间</th>
						<th>管理</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${users.result}" var="user">
					<tr>
					   <td><input type="checkbox" name="usercheckbox" value="${user.userId}"/></td>
						<td>${user.username}</td>
						<td>${user.organization.name}</td>
						<td>${user.roleStr}</td>
						<td>${user.telephone}</td>
						<td><fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd"/></td>
											<td><c:choose>
													<c:when test="${fn:contains(permisions, 'user:modify')}">
														<a href="${ctx}/user/update_pre.do?userId=${user.userId}">修改</a>
													</c:when>
													<c:otherwise>
														<c:if test="${user.userId==currentuserid}"><a href="${ctx}/user/update_pre.do?userId=${user.userId}&pageNo=${pageNo}">修改</a></c:if>
													</c:otherwise>
												</c:choose> 
												<c:if test="${fn:contains(permisions, 'user:disable')}">
													<a href="#"
														onclick="deleteUser(${user.userId},${user.status});return false;"><c:choose>
															<c:when test="${user.status==0}">禁用</c:when>
															<c:otherwise>使能</c:otherwise>
														</c:choose></a>
												</c:if>
												<c:if test="${fn:contains(permisions, 'user:resetpassword')}">
													<a href="#"
														onclick="resetpassword(${user.userId});return false;">重置密码</a>
												</c:if>
												</td>
										</tr>
					</c:forEach>
					<tr>
						<th colspan="9">
							<tags:pagination page="${users}" paginationSize="5"/>
						</th>
					</tr>
				</tbody>
			
			</table>
        
             </div>
		</div> 
        </div>	
    </div>
  </div>
  <jsp:include page="../commons/footer.jsp"/>
</div>

<script type="text/javascript">
function newreport(){
	window.location.href="${ctx}/user/add_pre.do?pageNo=${pageNo}";
}
function deleteUser(id,status){
	var messagetm="您确定使能所选用户?";
	if(0==status)
		messagetm="您确定禁用所选用户?";
	$.messager.model = { 
	        ok:{ text: "确定", classed: 'btn-primary' },
	        cancel: { text: "取消", classed: 'btn-error' }
	      };
	      $.messager.confirm("系统提示", messagetm, function() { 
	    	    ajaxOptions.url = "${ctx}/user/delete.do";
	    		ajaxOptions.params = {"deleteids":id};
	    		ajaxOptions.success = function(msg){
	    			window.location.href="${ctx}/user/list.do";
	    	    };
	    		simpleAjax(ajaxOptions);
	      });
}

function resetpassword(id){
	var messagetm="您确定重置该用户密码?";
	$.messager.model = { 
	        ok:{ text: "确定", classed: 'btn-primary' },
	        cancel: { text: "取消", classed: 'btn-error' }
	      };
	      $.messager.confirm("系统提示", messagetm, function() { 
	    	    ajaxOptions.url = "${ctx}/user/resetpassword.do";
	    		ajaxOptions.params = {"userId":id};
	    		ajaxOptions.success = function(msg){
	    			window.location.href="${ctx}/user/list.do";
	    	    };
	    		simpleAjax(ajaxOptions);
	      });
}
 
//全选的动作
function selectAll(object){  
    if (object.checked) {  
    $("input[name='usercheckbox']").prop("checked", true);  
    } else {  
    $("input[name='usercheckbox']").prop("checked", false);  
    }  
    return false;
}  
</script>
</body>
</html>