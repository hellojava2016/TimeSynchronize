<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- Bootstrap -->
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
      <jsp:include page="left-analyse.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> &gt; <a>风险分析</a> &gt; <a>单机构单期指标同比环比监测</a></div>
      
      <div class="row whitebackground mb_10">
        <div class="span12">
          <h3 class="show_tit">查询</h3>
          <div class="show_conment">
            <form class="form-horizontal" action="${ctx}/report/systemavailablerate/query.do?">          
                <div class="col-xs-12 row">      
                 <label   class="col-xs-1 control-label pr_0">金融机构：</label>        
                  <div class="col-xs-3 pr_0 pl_0 mr_5">
                  <select name="Organization"  class="selectcsss" id="Organization">
                    <c:forEach items="${orgs}" var="org">
                      <option value=${org.orgId} <c:if test="${selectedorgid==org.orgId}">selected</c:if>>${org.name}</option>
		            </c:forEach>
		           </select>
                  </div>
                  
                   <label   class="col-xs-1 control-label pr_0">风险指标：</label>
                    <div class="col-xs-3 pr_0 pl_0 mr_5">
                  <select name="risktarget"  class="selectcsss" id="risktarget">
                    <c:forEach items="${risktargets}" var="target">
                      <option value=${target.riskCode} <c:if test="${target.riskCode==selectedrisk}">selected</c:if>>${target.riskName}</option>
		            </c:forEach>
		           </select>
                  </div>
              </div>    
               <div class="col-xs-12 row dpheight15"></div>
                  <div class="col-xs-12 row">    
                    <label   class="col-xs-1 control-label pr_0">指标日期：</label>
                     <div class="col-xs-3  pr_0 pl_0 mr_10">  
                   <div id="submit_time" class="input-append date wid_60">
                    <input type="text" class="pick_timeshow selectcsss" id="exactDate" name="exactDate" value="${exactDate}"/>
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                  </div>                    
                  </div>
              
                 <div class="col-xs-12 row dpheight15"></div>
		<div class="col-xs-12 row">  <div class="col-xs-3 pr_0 pl_0 mr_5"> </div>
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
      
     
<div class="clearfix"></div>
      </div>
      <div class="row show mb_10" style="min-height:500px">
        <div class="span12">
          <div class="show_tit3">
           <div id="operateBtns">
	<div class="pull-left">
	<h4 class="showtit4">单机构单期指标同比环比监测</h4>
	</div>
	<div class="pull-right">
		<button class="btn btn-primary mr_20" type="button" id="exportButton" onclick="exportSystem()">
			<span class="glyphicon glyphicon-new-window"></span>&nbsp;&nbsp;导出
		</button>
	</div>
</div>
          </div>
          <div class="show_conment pad_5">
          <table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>所属机构</th><th>指标</th><th>指标明细</th><th>指标日期(期数)</th><th>指标值</th><th>同比增长率</th><th>环比增长率</th></tr></thead>
		<tbody>
		<c:choose>
			 	<c:when test="${!empty rates.result }">
		<c:forEach items="${rates.result}" var="rate">
			<tr>
				<td>${rate.org}</td>
				<td>${rate.riskmearment}</td>
				<td>${rate.mearmentdetail}</td>
				<td>${rate.showDate}</td>
				<td>${rate.value}</td>	
				<td>${rate.tongRate}</td>	
				<td>${rate.huanRate}</td>			
			</tr>
		          </c:forEach>
	       	     </c:when>
						<c:otherwise>
							<tr>
								<td colspan="8" class="emptyResut">暂无数据</td>
							</tr>
						</c:otherwise>
					</c:choose>		    
		</tbody>
	</table>
        </div></div>
      </div>
    </div>
  </div>
<jsp:include page="../commons/footer.jsp"></jsp:include>
</div>
<script type="text/javascript">
var pageNo =${pageNo};
function refresh(){
	window.location.href="${ctx}/report/report/report1.do?"+getQueryParam();
}
function exportSystem(){
	window.location.href="${ctx}/report/report/export1.do?"+getQueryParam();
}
function getQueryParam(){
	var Organization = $("#Organization").val();
	var exactDate = $("#exactDate").val();
	var risktarget=$("#risktarget").val();
	return "Organization="+Organization+"&exactDate="+exactDate+"&prov="+risktarget+"&pageNo="+pageNo+"&risktarget="+risktarget;
}
</script>
</body>
</html>