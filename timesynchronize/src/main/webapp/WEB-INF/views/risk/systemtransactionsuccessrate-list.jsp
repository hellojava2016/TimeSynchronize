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
<link href="${ctx}/css/bootstrap/bootstrap.min.css" rel="stylesheet" media="screen">
<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/list.css"/>
<link rel="stylesheet" type="text/css" media="screen"  href="${ctx}/css/bootstrap/bootstrap-datetimepicker.min.css">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <link rel="stylesheet" type="text/css" href="${ctx}/css/ie_8.css"/>
<![endif]-->
<script type="text/javascript" src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/moment-with-langs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/bootstrap-datetimepicker.min.js"></script>
<script src="${ctx}/js/select/jquery.cityselect.js" type="text/javascript" ></script> 
<script src="${ctx}/js/upload/jquery.ui.widget.js" type="text/javascript" ></script>
<script src="${ctx}/js/upload/jquery.iframe-transport.js"" type="text/javascript" ></script>
<script src="${ctx}/js/upload/jquery.fileupload.js" type="text/javascript" ></script>	
<script type="text/javascript">
var pageNo =${pageNo};
$(function (){
$("#city_5").citySelect({
	url:"<%=request.getContextPath()%>/report/systemtransactionsuccessrate/gettypes.do",
	prov:"${prov}",
	city:"${city}",
	dist:"${dist}",
	nodata:"none"
});
$('.input-append').datetimepicker({
    format: 'YYYY-MM-DD',
    language: 'zh-CN',
    pickDate: true,
    pickTime: false,
    inputMask: false
  }); 
var url = '${ctx}/report/systemtransactionsuccessrate/upload.do';
$('#fileupload').fileupload({
    autoUpload: true, 
    url: url, 
    dataType: 'text',
    add: function (e, data) { 
           data.submit();
    },
    done: function (e, data) { 
    	 window.location.href = "${ctx}/report/systemtransactionsuccessrate/list.do?message="+data.result;
    }
    
});
$("#fileupload").hide();
});	 
</script>
</head>
<body>
<div class="container"> 
  <jsp:include page="../commons/header.jsp"></jsp:include>
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-report.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>数据采集</a> &gt; <a>系统交易成功率</a></div>
      
      <div class="row whitebackground mb_10">
        <div class="span12">
          <h3 class="show_tit">查询</h3>
          <div class="show_conment">
            <form class="form-horizontal" action="${ctx}/report/systemtransactionsuccessrate/query.do?">          
                   <div class="col-xs-12 row">      
                 <label   class="col-xs-1 control-label pr_0 wid_10">金融机构：</label>        
                  <div class="col-xs-3 pr_0 pl_0 mr_5">
                  <select name="Organization"  class="selectcsss" id="Organization">
                    <c:forEach items="${orgs}" var="org">
                      <option value=${org.orgId} <c:if test="${selectedorgid==org.orgId}">selected</c:if>>${org.name}</option>
		            </c:forEach>
		           </select>
                  </div>
                  
                   <label   class="col-xs-1 control-label pr_0 wid_10">风险指标：</label>
                   <div id="city_5">
  		              <select class="prov" name="prov" id="prov"></select>
  		              <select class="city" name="city" id="city" disabled="disabled"></select>
  		              <select class="dist" name="dist" id="dist" disabled="disabled"></select>
                  </div>
              </div>    
               <div class="col-xs-12 row dpheight15"></div>
                  <div class="col-xs-12 row">    
                    <label   class="col-xs-1 control-label pr_0 wid_10">起始指标日期：</label>
                     <div class="col-xs-3  pr_0 pl_0 mr_10">  
                   <div id="submit_time" class="input-append date wid_60">
                    <input type="text" class="pick_timeshow selectcsss" id="starttime" name="starttime" value="${startdate}"/>
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                  </div>
                    <label   class="col-xs-1 control-label pr_0 wid_10">结束指标日期：</label>
                     <div class="col-xs-3 pr_0 pl_0 mr_10">  
                   <div id="submit_time" class="input-append date wid_60">
                    <input type="text" class="pick_timeshow selectcsss" id="endtime" name="endtime" value="${enddate}"/>
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                  </div>
                  </div>
              
              
                   <div class="col-xs-12 row dpheight15"></div>
		<div class="col-xs-12 row">  <div class="col-xs-4 pr_0 pl_0 mr_5"> </div>
		 <button class="btn btn-primary mr_20" id="querybotton" type="button" onclick="refresh();">
			<span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;查询
		</button>
		</div>
		
               <div class="col-xs-12 row dpheight15"></div>
            </form>
          </div>
        </div>
      </div>
      <div>
       
      <div class="row show mb_10">        
					<div class="span12">
					     <div class="show_tit3">
           <div id="operateBtns">
	<div class="pull-left">
	<h4 class="showtit4">系统交易成功率列表</h4>
	</div>
<div class="pull-right">
		<c:set var="permisions" value="${permisions}" scope="session" /> 
		<c:if test="${fn:contains(permisions, 'risk:add')}">
            <button class="btn btn-primary mr_20" id="addtaskbotton" type="button" onclick="newreport();">
			<span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;新上报
		</button>
       </c:if>
		
		<c:if test="${fn:contains(permisions, 'risk:delete')}">
		<button class="btn btn-primary mr_20" type="button" id="deleteButton" onclick="deleteRates();">
			<span class="glyphicon glyphicon-trash"></span>&nbsp;&nbsp;删除
		</button>
		  </c:if>
		  
		  <c:if test="${fn:contains(permisions, 'risk:import')}">
		<button class="btn btn-primary mr_20" type="button" id="importButton" onclick="importsystem()">
			<span class="glyphicon glyphicon-log-in"></span>&nbsp;&nbsp;导入
		</button>
		  </c:if>
		    <c:if test="${fn:contains(permisions, 'risk:export')}">
		<button class="btn btn-primary mr_20" type="button" id="exportButton" onclick="exportSystem()">
			<span class="glyphicon glyphicon-new-window"></span>&nbsp;&nbsp;导出
		</button>
		  </c:if>
		<input id="fileupload" type="file" name="files" multiple >  
	</div>
</div>
          
          </div>
						<div class="show_conment pad_5">
							<table id="contentTable"
								class="table table-striped table-bordered table-condensed">
								<thead>
									<tr>
										<th><input type="checkbox" id="allroleselected" onclick="selectAll(this)"/></th>								
										<th>所属机构</th>
										<th>指标类型</th>
										<th>指标日期(期数)</th>
										<th>是否补报</th>
										<th>交易总量</th>
										<th>交易成功量</th>
									</tr>
								</thead>
								<tbody>
								<c:choose>
			 	<c:when test="${!empty rates.result }">
									<c:forEach items="${rates.result}" var="rate">
										<tr>
											<td><input type="checkbox" name="rolecheckbox" value="${rate.id}"/></td>
											<td>${rate.organization.name}</td>
											<td>${rate.riskCategory.riskName}</td>
											<td>${rate.showDate}</td>
											<td><c:choose><c:when test="${rate.extral}"><font color="red">补报</font></c:when><c:otherwise>正常上报</c:otherwise></c:choose></td>
											<td>${rate.aost}</td>
											<td>${rate.aosst}</td>											
										</tr>
									  </c:forEach>
	       	     </c:when>
						<c:otherwise>
							<tr>
								<td colspan="7" class="emptyResut">暂无数据</td>
							</tr>
						</c:otherwise>
					</c:choose>
		    <tr style="background-color:#eee">
					<th colspan="7"><tags:pagination page="${rates}" paginationSize="5"/></th>
			</tr>
								</tbody>
							</table>			 
              
        </div></div>
      </div>
    </div>
  </div>
<jsp:include page="../commons/footer.jsp"></jsp:include>
<script type="text/javascript">
//响应删除角色按钮
function deleteRates(){
	var deleteroleids = "";
	$("input[name='rolecheckbox']:checked").each(function(){     
		deleteroleids+=$(this).val()+",";     
	    })  ;
	if(""==deleteroleids){
		 $.messager.alert("系统提示", "请选择要删除数据！");
		 return;
	} 
	$.messager.model = { 
	        ok:{ text: "确定", classed: 'btn-default' },
	        cancel: { text: "取消", classed: 'btn-error' }
	      };
	      $.messager.confirm("系统提示", "您确定删除所选数据？", function() { 
	    	    ajaxOptions.url = "${ctx}/report/systemtransactionsuccessrate/delete.do?";
	    		ajaxOptions.params = {	"pageNo":pageNo,"deleteids":deleteroleids};
	    		ajaxOptions.success = function(msg){
	    			window.location.href="${ctx}/report/systemtransactionsuccessrate/query.do?"+getQueryParam();
	    	    };
	    		simpleAjax(ajaxOptions);
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
function importsystem(){
	  $("#fileupload").click();
}
function newreport(){
	window.location.href="${ctx}/report/systemtransactionsuccessrate/add_pre.do";
}
function refresh(){
	window.location.href="${ctx}/report/systemtransactionsuccessrate/query.do?"+getQueryParam();
}
function exportSystem(){
	window.location.href="${ctx}/report/systemtransactionsuccessrate/export.do?"+getQueryParam();
}
function getQueryParam(){
	var Organization = $("#Organization").val();
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var prov=$("#prov").val();
	var city=$("#city").val();
	var dist=$("#dist").val();
	return "Organization="+Organization+"&startdate="+starttime+"&endtdate="+endtime+"&prov="+prov+"&city="+city+"&dist="+dist+"&pageNo="+pageNo;
}
</script>
</body>
</html>