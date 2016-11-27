<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
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
<script src="${ctx}/js/select/jquery.cityselect.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/moment-with-langs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js" ></script>
<script type="text/javascript">
$(function (){
	$("form label.require").each(function(){
        $(this).append('<font color="red">*</font>'); //然后将它追加到文档中  
    });
	$.validator.addMethod("compareNumd",function(value,element){
		try{
			 var assigntime = Number($("#minvalue").val());
			    var deadlinetime = Number($("#maxValuedp").val());
			    if(assigntime>deadlinetime){
			        return false;
			    }else{
			        return true;
			    }
		}catch(ex){
			return false;
		}
	   
	},"<font color='#E47068'>最大门限必须大于等于最小门限</font>");
	$.validator.addMethod("comparedate",function(value,element){
		try{
			 var assigntime = Number($("#startDay").val());
			    var deadlinetime = Number($("#endDay").val());
			    if(assigntime>=deadlinetime){
			        return false;
			    }else{
			        return true;
			    }
		}catch(ex){
			return false;
		}
	   
	},"<font color='#E47068'>上报结束日期必须大于上报开始日期</font>");
	$("#reportform").validate({
		rules: {
			startDay: {
				required: true,
				digits:true
			},
			endDay: {
				required: true,
				digits:true,
				comparedate:true
			},
			minvalue: {
				required: true,
				digits:true
			},
			maxValuedp: {
				required: true,
				digits:true,
				compareNumd:true
			}
		},
		messages: {
			startDay:"请输入上报起始天数",
		    endDay:{required:"请输入上报结束天数",comparedate:"上报结束天数必须大于上报起始天数"},
			minvalue:"请输入最小门限,且必须为数字",
			maxValuedp: {required:"请输入最大门限",digits:"最大门限值必须为数字",compareNumd:"最大门限必须大于等于最小门限"}
		}
	});
	
	var checnresult2 = $("#thradoptionsRadios1").is(':checked');
	if(checnresult2){
		$("#mindiv").hide();
		$("#maxdiv").hide();
	}else{
		$("#mindiv").show();
		$("#maxdiv").show();
	}
	
	$("#thradoptionsRadios1").change(function(){
		var checnresult = $("#thradoptionsRadios1").is(':checked');
		if(checnresult){
			$("#mindiv").hide();
			$("#maxdiv").hide();
		}else{
			$("#mindiv").show();
			$("#maxdiv").show();
		}
	});
	
	$("#thradoptionsRadios2").change(function(){
		var checnresult = $("#thradoptionsRadios1").is(':checked');
		if(checnresult){
			$("#mindiv").hide();
			$("#maxdiv").hide();
		}else{
			$("#mindiv").show();
			$("#maxdiv").show();
		}
	});
	
});	 	  
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
      <div class="breadcrumb"><span>您当前所在的位置：</span> &gt; <a>系统配置</a> &gt;<a>监控指标配置</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">监控指标配置</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="reportform" role="form" action="<%=request.getContextPath()%>/riskcategory/update.do" method="post">
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0">风险指标：</label>
                <div class="col-xs-4">
                  <input type="text" class="form-control" id="rickname" name="riskname"  readonly value="${rc.riskName}">
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0">周期：</label>
                <div class="col-xs-4">
                  <input type="text" class="form-control" id="qishu" name="cycle"  readonly value="${rc.cycleString}">
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">上报起始天数：</label>
                <div class="col-xs-4">
                  <input type="text" class="form-control" id="startDay" name="startDay"  value="${rc.startDay}">
                </div>
              </div>              
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">上报结束天数：</label>
                <div class="col-xs-4">
                  <input type="text" class="form-control" id="endDay" name="endDay"  value="${rc.endDay}">
                </div>
              </div>
              <c:if test="${rc.hasLeaf}">
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">上报天数级联到下级指标：</label>
                <div class="col-xs-5 row">
                  <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="radio" name="cascade" id="cascade1" value="false">否
                    </label> 
                 </div>
                  <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="radio" name="cascade" id="cascade2" value="true" checked>是
                    </label> 
                  </div>
                </div>
              </div>
              </c:if>              
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">是否允许上报：</label>
                <div class="col-xs-5 row">
                  <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="radio" name="allowReport" id="optionsRadios1" value="false" <c:choose><c:when test="${rc.allowReport}"></c:when><c:otherwise>checked</c:otherwise></c:choose>>否
                    </label> 
                 </div>
                 <c:if test="${reportenable}">
                  <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="radio" name="allowReport" id="optionsRadios2" value="true" <c:if test="${rc.allowReport}">checked</c:if>>是
                    </label> 
                  </div>
                 </c:if>
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">是否允许门限：</label>
                <div class="col-xs-5 row">
                  <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="radio" name="usethresholdvalue" id="thradoptionsRadios1" value="false" <c:choose><c:when test="${rc.useThresholdValue}"></c:when><c:otherwise>checked</c:otherwise></c:choose>>否
                    </label> 
                 </div>
                  <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="radio" name="usethresholdvalue" id="thradoptionsRadios2" value="true" <c:if test="${rc.useThresholdValue}">checked</c:if>>是
                    </label> 
                  </div>
                </div>
              </div>      
              <input type="hidden"   name="pageno" value="${pageno}"/>    
              <div class="form-group" id="mindiv">
                <label for="input" class="col-xs-4 control-label pr_0 require">最小门限：</label>
                <div class="col-xs-4">
                  <input type="text" class="form-control" id="minvalue"  name="minvalue" value="${rc.minValue}">
                </div>
              </div>
              <div class="form-group" id="maxdiv">
                <label for="input" class="col-xs-4 control-label pr_0 require">最大门限：</label>
                <div class="col-xs-4">
                  <input type="text" class="form-control" id="maxValuedp"  name="maxValuedp" value="${rc.maxValue}">
                </div>
                <input type="hidden" name="id" value="${rc.riskId}">
              </div>
              
              <div class="form-group">
                <div class="col-xs-offset-5 col-xs-10">
                  <button type="submit" class="btn btn-primary mr_20">确定</button>
                  <button type="button" class="btn btn-default" onclick="window.location.href=document.referrer;">取消</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
    <div class="clearfix"></div>
  </div>
  <jsp:include page="../commons/footer.jsp"></jsp:include>
</body>
</html>