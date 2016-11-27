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
  <jsp:include page="../commons/header.jsp"></jsp:include>
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-system.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a href="${ctx}/role/list.do">系统配置</a> > <a href="${ctx}/role/list.do">角色管理</a></div>

      <div class="row show mb_10">
        <div class="span12">
          <div class="show_tit3">
           <div id="operateBtns">
	<div class="pull-left">
	<h4 class="showtit4">用户列表</h4>
	</div>
	<div class="pull-right">
	    <c:if test="${fn:contains(permisions, 'role:add')}">
		<button class="btn btn-primary mr_20" id="addtaskbotton" type="button" onclick="newreport();">
			<span class="glyphicon glyphicon-plus"></span>添加角色
		</button>
		</c:if>
		<c:if test="${fn:contains(permisions, 'role:delete')}">
		<button class="btn btn-primary mr_20" onclick="deleteRole()">删除角色</button>
		</c:if>
		<c:set var="permisions" value="${permisions}" scope="session" /> 
	</div>
</div>          
          </div>
          <div class="show_conment pad_5">
          <table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th><input type="checkbox" id="allroleselected" onclick="selectAll(this)"/></th><th>角色名</th><th>角色描述</th><th>管理</th></tr></thead>
		<tbody>
		<c:forEach items="${roles.result}" var="role">
			<tr>
				<td><input type="checkbox" name="rolecheckbox" value="${role.roleId}"/></td>
				<td>${role.roleName}</td>
				<td>${role.description}</td>				
				<td><c:set var="permisions" value="${permisions}" scope="session" /> 
		<c:if test="${fn:contains(permisions, 'role:modify')}"><a href="${ctx}/role/update_pre.do?roleId=${role.roleId}">修改 </a> </c:if></td>
			</tr>
		</c:forEach>
			 <tr >
					<th colspan="4"><tags:pagination page="${roles}" paginationSize="5"/></th>
			</tr>
		</tbody>
	</table>
        </div> 
        </div>
         
      </div>
    </div>
  </div>
  <div>
	
	</div>
</div>
<jsp:include page="../commons/footer.jsp"></jsp:include>
<script type="text/javascript">
function newreport(){
	window.location.href="${ctx}/role/add_pre.do?pageNo=${pageNo}";
}
//响应删除角色按钮
function deleteRole(){
	var deleteroleids = "";
	$("input[name='rolecheckbox']:checked").each(function(){     
		deleteroleids+=$(this).val()+",";     
	    })  ;
	if(""==deleteroleids){
		 $.messager.alert("系统提示", "请选择要删除的角色！");
		 return;
	} 
	$.messager.model = { 
	        ok:{ text: "确定", classed: 'btn-default' },
	        cancel: { text: "取消", classed: 'btn-error' }
	      };
	      $.messager.confirm("系统提示", "您确定删除所选角色？", function() { 
	    		$.ajax({ url: "${ctx}/role/delete.do",  
	    			data:{"deleteids":deleteroleids},
	    			success: function(msg){
	    				window.location.href="${ctx}/role/list.do?message="+msg;
	    	      }});
	    		
	      });
}
//全选的动作
function selectAll(object){  
    if (object.checked) {  
    $("input[name='rolecheckbox']").prop("checked", true);  
    } else {  
    $("input[name='rolecheckbox']").prop("checked", false);  
    }  
    return false;
}  
</script>
</body>
</html>