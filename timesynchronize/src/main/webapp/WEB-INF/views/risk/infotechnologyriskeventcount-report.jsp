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
<script src="${ctx}/js/upload/jquery.fileupload.js" type="text/javascript" ></script>	
<script src="${ctx}/echarts/esl.js" type="text/javascript" ></script>
</head>
<script type="text/javascript">
var flag =${flag};
$(function (){
	$("#city_5").citySelect({
		url:"<%=request.getContextPath()%>/report/infotechnologyriskeventcount/gettypes.do",
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
 if(flag){
	 $("#tableandachart").hide();
 }else{
	 require.config({
		    paths:{ 
		        'echarts' : '${ctx}/echarts/echarts',
		        'echarts/chart/line' : '${ctx}/echarts/echarts'
		    }
		});

		// 使用
		require(
		    [
		        'echarts',
		        'echarts/chart/line'
		    ],
		function(ec) {
		    	 var myChart = require('echarts').init(document.getElementById('datadiv'));
		    	 var option = ${chartdata} ;
		         myChart.setOption(option); 
		    }
		);
	
 }
 
 $.ajax({ url: "<%=request.getContextPath()%>/report/infotechnologyriskeventcount/getOrgs.do", 
	 data:{area: $("#area").val()},
	 success: function(msg){
        $("#dappendhtml").empty().html(msg);
        $("#selectdept").xMenu({
    		width :600, 
    		eventType: "click", 
    		dropmenu:"#mdongp",
    		hiddenID : "selectdeptidden"	
    		
    	});
 }});
 
 $("#area").change(function(){
	 $("#selectdeptidden").val("");
	 $("#dliremoveid").empty();
	 $.ajax({ url: "<%=request.getContextPath()%>/report/infotechnologyriskeventcount/getOrgs.do", 
		 data:{area: $("#area").val()},
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
 
 
});	
</script>
<body>
 <div id="mdongp" class="xmenu" style="display: none;">
			<div class="select-info">	
				<ul id="dliremoveid">
				</ul>
				<a  name="menu-confirm" href="javascript:void(0);" class="a-btn">
					<span class="a-btn-text">确定</span>
				</a> 
			</div>	
			 
			<dl id="dappendhtml">
				 
            </dl>
				 
		</div>
<div class="container"> 
  <jsp:include page="../commons/header.jsp"></jsp:include>
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-analyse.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> <a>风险分析</a> &gt; <a>信息科技风险事件数量</a></div>
   <div class="row whitebackground mb_10">
        <div class="span12">
          <h3 class="show_tit">查询</h3>
          <div class="show_conment">
            <form class="form-horizontal" action="${ctx}/report/fakesiteattachmentrate/analyse.do?">
               <div class="col-xs-12 row">      
                 <label   class="col-xs-1 control-label pr_0 wid_10">区域：</label>        
                  <div class="col-xs-2 pr_0 pl_0 mr_5">
                  <select name="area"  class="selectcsss" id="area">
                    <c:forEach items="${areas}" var="areat">
                      <option value=${areat.areeCode} <c:if test="${area==areat.areeCode}">selected</c:if>>${areat.name}</option>
		            </c:forEach>
		           </select>
                  </div>
                  
                  <label   class="col-xs-1 control-label pr_0 wid_10">金融机构：</label>
                    <div class="col-xs-2  pr_0 pl_0 mr_10 topnav">
					<a id="selectdept" href="javascript:void(0);" class="as">
						<span>选择金融机构</span>
					</a>					
					<input type="hidden" value="${category}" id="selectdeptidden" />
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
                     <div class="col-xs-2  pr_0 pl_0 mr_10">  
                   <div id="submit_time1" class="input-append date wid_60">
                    <input type="text" class="pick_timeshow selectcsss" id="starttime" name="starttime" value="${startdate}"/>
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                  </div>
                    <label   class="col-xs-1 control-label pr_0 wid_10">结束指标日期：</label>
                     <div class="col-xs-2 pr_0 pl_0 mr_10">  
                   <div id="submit_time2" class="input-append date wid_60">
                    <input type="text" class="pick_timeshow selectcsss" id="endtime" name="endtime" value="${enddate}"/>
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                  </div>
                  <label   class="col-xs-1 control-label pr_0 wid_10">排序：</label>        
                 <div class="col-xs-2 pr_0 pl_0 mr_10">  
                  <select name="reportorder"  class="selectcsss" id="reportorder">                      
                      <option value="4" <c:if test="${reportorder==4}">selected</c:if>>风险数量-降序</option> 
                      <option value="5" <c:if test="${reportorder==5}">selected</c:if>>风险数量-升序</option>                 
                      <option value="1" <c:if test="${reportorder==1}">selected</c:if>>时间</option>
                      <option value="2" <c:if test="${reportorder==2}">selected</c:if>>金融机构</option>
                      <option value="3" <c:if test="${reportorder==3}">selected</c:if>>指标类型</option> 
		           </select>
                  </div>
                  </div>
              
                     <div class="col-xs-12 row dpheight15"></div>
		<div class="col-xs-12 row">  <div class="col-xs-5 pr_0 pl_0 mr_5"> </div>
		 <button class="btn btn-primary mr_20" id="querybotton" type="button" onclick="refresh();">
			<span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;查询
		</button>
		</div>
		
               <div class="col-xs-12 row dpheight15"></div>    
            </form>
          </div>
        </div>
      </div>
      <div id="tableandachart">
      <div class="row show mb_10">
        <div class="span12">
          <div id="datadiv" style="background-color: white; border: solid 1px black; width: 100%;height:600px">
		  
	     </div>
        </div>
      </div>
      <div class="row whitebackground mb_10">
        <div class="span12">
        
         <div class="show_tit3">
           <div id="operateBtns">
	<div class="pull-left">
	<h4 class="showtit4">信息科技风险事件数量列表</h4>
	</div>
	<div class="pull-right">
	</div>
</div>
          
          </div>
          
          <div class="show_conment pad_5">
          
         <table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>所属机构</th><th>指标类型</th><th>指标日期(期数)</th><th>是否补报</th><th>风险数量</th></tr></thead>
		<tbody>
		<c:choose>
			 	<c:when test="${!empty rates.result }">
		<c:forEach items="${rates.result}" var="rate">
			<tr>
				<td>${rate.organization.name}</td>
				<td>${rate.riskCategory.riskName}</td>
				<td>${rate.showDate}</td>
				<td><c:choose><c:when test="${rate.extral}"><font color="red">补报</font></c:when><c:otherwise>正常上报</c:otherwise></c:choose></td> </td>
				<td>${rate.count}</td>
			</tr>
	 </c:forEach>
	       	     </c:when>
						<c:otherwise>
							<tr>
								<td colspan="6" class="emptyResut">暂无数据</td>
							</tr>
						</c:otherwise>
					</c:choose>
		    <tr style="background-color:#eee">
					<th colspan="6"><tags:pagination page="${rates}" paginationSize="5"/></th>
			</tr>
		</tbody>
	</table>
              
        </div></div>
      </div>
       
    </div>
  </div>
  </div>
  </div>
  
<jsp:include page="../commons/footer.jsp"></jsp:include>
<script type="text/javascript">
var pageNo =${pageNo};
function getQueryParam(){
	var area =$("#area").val();
	var category =$("#selectdeptidden").val();
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var reportorder= $("#reportorder").val();
	var prov=$("#prov").val();
	var city=$("#city").val();
	var dist=$("#dist").val();
	return "startdate="+starttime+"&endtdate="+endtime+"&category="+category+"&area="+area+"&pageNo="+pageNo+"&reportorder="+reportorder+"&prov="+prov+"&city="+city+"&dist="+dist;
}
function refresh(){
	window.location.href="${ctx}/report/infotechnologyriskeventcount/report.do?"+getQueryParam();
}
</script>
</body>
</html>        