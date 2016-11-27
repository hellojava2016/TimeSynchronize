<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
<body>
<div class="container"> 
  <jsp:include page="../commons/header.jsp"></jsp:include>
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-system.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span>&gt;系统配置</a> &gt;<a>区域配置</a></div>

      <div class="row show mb_10">
        <div class="span12">
                 <div class="show_tit3">
           <div id="operateBtns">
	<div class="pull-left">
	<h4 class="showtit4">${parentname}</h4>
	</div>
	<c:set var="permisions" value="${permisions}" scope="session" /> 
	<div class="pull-right">
	  <c:if test="${fn:contains(permisions, 'area:add')}">
	  <c:if test="${canadd}">
		<button class="btn btn-primary mr_20" id="addtaskbotton" type="button" onclick="newreport(${parentid});">
			<span class="glyphicon glyphicon-plus"></span>&nbsp;${addlabel}
		</button>
	 </c:if>
	 </c:if>							
		<c:if test="${fn:contains(permisions, 'area:delete')}">
		<c:if test="${candelete}">
		 <button class="btn btn-primary mr_20" id="querybotton" type="button" onclick="deleteRates();">
			<span class="glyphicon glyphicon-trash"></span>&nbsp;删除
		</button>
		</c:if>
		 </c:if>
	</div>
</div>
          
          </div>
          <div class="show_conment pad_5">
          
          <table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th><input type="checkbox" id="allroleselected"
												onclick="selectAll(this)" /></th>
		<th>区域编号</th><th>区域名称</th><th>管理   </th></tr></thead>
		
		<tbody>
		<c:forEach items="${areacitys}" var="ac"> 
			<tr>	
				<td><input type="checkbox" name="rolecheckbox"
													value="${ac.areaId}" /></td>	    			
				<td>${ac.areeCode}</td>
				<td><a href="${ctx}/areacity/list.do?parentid=${ac.areaId}">${ac.name}</a></td>											
				<td><c:if test="${fn:contains(permisions, 'area:modify')}"><a href="${ctx}/areacity/update_pre.do?areaId=${ac.areaId}">修改 </a></c:if> 
				&nbsp;&nbsp;
				<c:if test="${fn:contains(permisions, 'area:add')}"><a href="${ctx}/areacity/add_pre.do?parentid=${ac.areaId}">新增下级区域</a> </c:if></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<input type="hidden" name="parentid" value="${parentid}">
        </div></div>
      </div>
    </div>
  </div>
  <jsp:include page="../commons/footer.jsp" />
  	<script type="text/javascript">
  	function newreturn(pp){
  		window.location.href="${ctx}/areacity/list.do?parentid="+pp;
  	}
	function newreport(pp){
  		window.location.href="${ctx}/areacity/add_pre.do?parentid="+pp;
  	}
		//响应删除角色按钮
		function deleteRates() {
			var deleteroleids = "";
			$("input[name='rolecheckbox']:checked").each(function() {
				deleteroleids += $(this).val() + ",";
			});
			if ("" == deleteroleids) {
				$.messager.alert("系统提示", "请选择要删除的区域！");
				return;
			}
			$.messager.model = {
				ok : {
					text : "确定",
					classed : 'btn-success'
				},
				cancel : {
					text : "取消",
					classed : 'btn-primary'
				}
			};
			$.messager.confirm("系统确认", "您确定删除所选区域？", function() {
				$.ajax({ url: "${ctx}/areacity/delete.do",  
	    			data:{"deleteids":deleteroleids},
	    			success: function(msg){
	    				if("fail"==msg)
						    window.location.href = "${ctx}/areacity/list.do?parentid=${parentid}&message=deleteerror";
						else
							window.location.href = "${ctx}/areacity/list.do?parentid=${parentid}&message=deletesuccess";
	    	      }});
			});
		}
		//全选的动作
		function selectAll(object) {
			if (object.checked) {
				$("input[name='rolecheckbox']").prop("checked", true);
			} else {
				$("input[name='rolecheckbox']").prop("checked", false);
			}
			return false;
		}
	</script>
</div>
</body>
</html>