<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- Bootstrap -->
<link rel="stylesheet" type="text/css" href="${ctx}/css/powerFloat.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/xmenu.css"/>
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
<script type="text/javascript" src="${ctx}/js/jquery-powerFloat-min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-xmenu.js"></script>
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/moment-with-langs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/bootstrap-datetimepicker.min.js"></script>
<script src="${ctx}/js/select/jquery.cityselect.js" type="text/javascript" ></script> 
<script src="${ctx}/js/upload/jquery.ui.widget.js" type="text/javascript" ></script>
<script src="${ctx}/js/upload/jquery.iframe-transport.js"" type="text/javascript" ></script>
</head>
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
<body>
<div class="container"> 
  <jsp:include page="../commons/header.jsp"></jsp:include>
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-analyse.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> <a>风险分析</a> &gt; <a>多机构指标超阈值监测</a></div>
   <div class="row whitebackground mb_10">
        <div class="span12">
          <h3 class="show_tit">查询</h3>
          <div class="show_conment">
            <form class="form-horizontal" action="${ctx}/report/fakesiteattachmentrate/analyse.do?">               
               <div class="col-sm-12 row dpheight15"></div>
                  <div class="col-sm-12 row">    
                    <label   class="col-sm-1 control-label pr_0 wid_10">起始指标日期：</label>
                     <div class="col-xs-3  pr_0 pl_0 mr_10">  
                   <div id="submit_time1" class="input-append date wid_60">
                    <input type="text" class="pick_timeshow selectcsss" id="starttime" name="starttime" value="${startdate}"/>
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                  </div>
                    <label   class="col-sm-1 control-label pr_0 wid_10">结束指标日期：</label>
                     <div class="col-xs-3 pr_0 pl_0 mr_10">  
                   <div id="submit_time2" class="input-append date wid_60">
                    <input type="text" class="pick_timeshow selectcsss" id="endtime" name="endtime" value="${enddate}"/>
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                  </div>
                  </div>
              
                     <div class="col-sm-12 row dpheight15"></div>
		<div class="col-sm-12 row">  <div class="col-xs-3 pr_0 pl_0 mr_5"> </div>
		 <button class="btn btn-primary mr_20" id="querybotton" type="button" onclick="refresh();">
			<span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;查询
		</button>
		</div>
		
               <div class="col-sm-12 row dpheight15"></div>    
            </form>
          </div>
        </div>
      </div>
      
      <div class="row whitebackground mb_10">
        <div class="span12">
        
         <div class="show_tit3">
           <div id="operateBtns">
	<div class="pull-left">
	<h4 class="showtit4">多机构指标超阈值监测</h4>
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
		<thead><tr><th>机构类型</th><th>机构名称</th><th>指标类型</th><th>指标日期(期数)</th><th>指标值</th><th>阈值</th></tr></thead>
		<tbody>
		<c:choose>
			 	<c:when test="${!empty rates.result }">
		<c:forEach items="${rates.result}" var="rate">
			<tr>
			    <td>${rate.org.categoryStr}</td>
				<td>${rate.org.name}</td>
				<td>${rate.riskCategory.riskName}</td>
				<td>${rate.showDate}</td>	
				<td>${rate.currentValue}</td>
				<td>${rate.riskCategory.minValue}~${rate.riskCategory.maxValue}</td>
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
					<th colspan="7"><tags:pagination page="${rates}" paginationSize="10"/></th>
			</tr>
		</tbody>
	</table>
              
        </div></div>
      </div>
    </div>
  </div>
  </div>
  
<jsp:include page="../commons/footer.jsp"></jsp:include>
<script type="text/javascript">
var pageNo =${pageNo};
function getQueryParam(){
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	return "startdate="+starttime+"&endtdate="+endtime+"&pageNo="+pageNo;
}
function refresh(){
	window.location.href="${ctx}/report/report/report4.do?"+getQueryParam();
}
function exportSystem(){
	window.location.href="${ctx}/report/report/export4.do?"+getQueryParam();
}
</script>
</body>
</html>