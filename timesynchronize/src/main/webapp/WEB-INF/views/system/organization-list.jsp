<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
<script src="${ctx}/js/select/jquery.cityselect.js" type="text/javascript" ></script>
<script type="text/javascript">
$(function (){
	$("#dpminheight").css({"min-height":"600px"});
});	
function deleteRates() {
	var deleteroleids = "";
	$("input[name='rolecheckbox']:checked").each(function() {
		deleteroleids += $(this).val() + ",";
	});
	if ("" == deleteroleids) {
		$.messager.alert("系统提示", "请选择要删除数据！");
		return;
	}
	$.messager.confirm("系统提示", "您确定删除所选数据？", function() {
 	    window.location.href="${ctx}/organization/delete.do?orgId="+deleteroleids;
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
function newreport(){
	window.location.href="${ctx}/organization/add_pre.do";
}
function query(){
	var category = $("#category").val();	
	var name=$("#name").val();
	window.location.href="${ctx}/organization/query.do?category="+category+"&name="+name;
}
function resetquery(){
	$("#name").val("");
	$("#category").val(0);
}
function changekey(id){
	var messagetm="您确定更换该机构签名?";
	$.messager.model = { 
	        ok:{ text: "确定", classed: 'btn-primary' },
	        cancel: { text: "取消", classed: 'btn-error' }
	      };
	      $.messager.confirm("系统提示", messagetm, function() { 
	    	    ajaxOptions.url = "${ctx}/organization/changekey.do";
	    		ajaxOptions.params = {"orgId":id};
	    		ajaxOptions.success = function(msg){
	    			window.location.href="${ctx}/organization/list.do";
	    	    };
	    		simpleAjax(ajaxOptions);
	      });
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
      <div class="breadcrumb"><span>您当前所在的位置：</span> &gt; <a>系统配置</a> &gt; <a>机构配置</a></div>
      
      <div class="row whitebackground mb_10">
        <div class="span12">
          <h3 class="show_tit">查询</h3>
          <div class="show_conment">
            <form class="form-horizontal" action="${ctx}/organization/query.do?" method="post">          
                <div class="col-xs-12 row">
                  <div class="col-xs-3 pr_0 pl_0 mr_5"> <span>机构类型&nbsp;&nbsp;</span>
                   <select name="category"  class="selectcsss" id="category">
                      <option value=0>全部类型</option>
                      <option value=1 <c:if test="${category==1}"> selected</c:if>> 监管机构 </option>
                       <option value="2" <c:if test="${category==2}"> selected</c:if>>政策性及国家开发银行</option>
                   <option value="3" <c:if test="${category==3}"> selected</c:if>>大型商业银行</option>
                   <option value="4" <c:if test="${category==4}"> selected</c:if>>股份制商业银行</option>
                   <option value="5" <c:if test="${category==5}"> selected</c:if>>城市商业银行</option>
                   <option value="6" <c:if test="${category==6}"> selected</c:if>>农村商业银行</option>
                   <option value="7" <c:if test="${category==7}"> selected</c:if>>农村合作银行</option>
                   <option value="8" <c:if test="${category==8}"> selected</c:if>>农村信用社</option>
                   <option value="9" <c:if test="${category==9}"> selected</c:if>>邮政储蓄银行</option>
                   <option value="10" <c:if test="${category==10}"> selected</c:if>>金融资产管理公司</option>
                   <option value="11" <c:if test="${category==11}"> selected</c:if>>外资法人金融机构</option>
                   <option value="12" <c:if test="${category==12}"> selected</c:if>>中德住房储蓄银行</option>
                   <option value="13" <c:if test="${category==13}"> selected</c:if>>信托公司</option>
                   <option value="14" <c:if test="${category==14}"> selected</c:if>>企业集团财务公司</option>
                   <option value="15" <c:if test="${category==15}"> selected</c:if>>金融租赁公司</option>
                   <option value="16" <c:if test="${category==16}"> selected</c:if>>货币经纪公司</option>
                   <option value="17" <c:if test="${category==17}"> selected</c:if>>汽车金融公司</option>
                   <option value="18" <c:if test="${category==18}"> selected</c:if>>消费金融公司</option>
                   <option value="19" <c:if test="${category==19}"> selected</c:if>>村镇银行</option>
                   <option value="20" <c:if test="${category==20}"> selected</c:if>>贷款公司</option>
                   <option value="21" <c:if test="${category==21}"> selected</c:if>>农村资金互助社</option>
		            </select>
                  </div>
                  <div class="col-xs-3 pr_0 pl_0 mr_5"><span>机构名称：</span> 
					<input type="text" class="form-control ml_5 wid_65"  id="name" name="name" value="${name}">
				 </div>                                                                       
              </div>  
                             
             <div class="col-xs-12 row dpheight15"></div>
		<div class="col-xs-12 row">  <div class="col-xs-2 pr_0 pl_0 mr_5"> </div>
		 <button class="btn btn-primary mr_20" id="querybotton" type="button" onclick="query();">
			<span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;查询
		</button>
		<button class="btn btn-primary mr_20" id="querybotton" type="button" onclick="resetquery();">
			<span class="glyphicon glyphicon-repeat"></span>&nbsp;&nbsp;重置
		</button>
		</div>
		
               <div class="col-xs-12 row dpheight15"></div>  
            </form>
          </div>
        </div>
      </div>
      <div>
      </div>
      
     	<div class="row show mb_10" id="dpminheight">
        <div class="span12">
            <div class="show_tit3">
           <div id="operateBtns">
	<div class="pull-left">
	<h4 class="showtit4">机构列表</h4>
	</div>
	<div class="pull-right">
		<c:set var="permisions" value="${permisions}" scope="session" /> 
		<c:if test="${fn:contains(permisions, 'org:add')}">
		<button class="btn btn-primary mr_20" id="addtaskbotton" type="button" onclick="newreport();">
			<span class="glyphicon glyphicon-plus"></span>添加
		</button>
		 </c:if>
		 <c:if test="${fn:contains(permisions, 'org:delete')}">
		<button class="btn btn-primary mr_20" type="button" id="deleteButton" onclick="deleteRates();">
			<span class="glyphicon glyphicon-trash"></span>&nbsp;&nbsp;删除
		</button>
		 </c:if>
	</div>
</div>
          
          </div>
          <div class="show_conment pad_5">
          
          <table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th><input type="checkbox" id="allroleselected"
												onclick="selectAll(this)" /></th>
		<th>机构名称</th><th>所属区域</th><th>机构类型</th><th>机构地址</th><th>机构联系人</th><th>联系电话</th><th>资产规模</th><th>纳入指标监控</th><th>管理</th></tr></thead>
		<tbody>
		<c:forEach items="${organization.result}" var="organizations">
			<tr>
				<td><input type="checkbox" name="rolecheckbox" value="${organizations.orgId}" /></td>
				<td>${organizations.name}</td>				
				<td>${organizations.areaCity.name}</td>
				<td>${organizations.categoryStr}</td>
				<td>${organizations.address}</td>
				<td>${organizations.contactsName}</td>
				<td>${organizations.contactsCellphone}</td>
				<td>${organizations.moneyCount}</td>				
				<td><c:choose><c:when test="${organizations.canControl}">是</c:when><c:otherwise><font color="red">否</font></c:otherwise></c:choose></td>								
				<td>
				<c:if test="${fn:contains(permisions, 'org:modify')}"><a href="${ctx}/organization/update_pre.do?orgId=${organizations.orgId}">修改</a></c:if>
				<c:if test="${fn:contains(permisions, 'org:changekey')}"><a href="#" onclick="changekey(${organizations.orgId});return false;">更换签名</a></c:if></td>
			</tr>
		</c:forEach>
			 <tr >
					<th colspan="10"><tags:pagination page="${organization}" paginationSize="5"/></th>
			</tr>
		</tbody>
	</table>
	
        </div></div>
      </div>
    </div>
  </div>
<jsp:include page="../commons/footer.jsp"></jsp:include>
</div>
</body>
</html>