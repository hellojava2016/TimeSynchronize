<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<!-- Bootstrap -->

<link href="${ctx}/css/bootstrap/bootstrap.min.css" rel="stylesheet" media="screen">
<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/list.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/jtree/style.min.css"/>
<link rel="stylesheet" type="text/css" media="screen"  href="${ctx}/css/bootstrap/bootstrap-datetimepicker.min.css">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <link rel="stylesheet" type="text/css" href="${ctx}/css/ie_8.css"/>
      <script src="${ctx}/js/respond.min.js" ></script>
<![endif]-->
<script type="text/javascript" src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<script src="${ctx}/js/select/jquery.cityselect.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/moment-with-langs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js" ></script>
<script type="text/javascript" src="${ctx}/js/ztree/jstree.min.js" ></script>
<script type="text/javascript">
$(function (){
	$("form label.require").each(function(){
        $(this).append('<font color="red">*</font>');  
    });
	 
		$("#reportform").validate({
			rules: {
				systemavailable: {
					required: true ,
					digits:true,
					range:[0,100] 
				},
				systemtransaction: {
					required: true ,
					digits:true,
					range:[0,100] 
				},
				operationchanges: {
					required: true ,
					digits:true,
					range:[0,100] 
				},
				fakesiteattachment: {
					required: true ,
					digits:true,
					range:[0,100] 
				},
				continueDecline: {
					required: true ,
					digits:true,
					range:[1,100] 
				},
				deviateAverage: {
					required: true ,
					digits:true,
					min:0
				}
				
			},
			messages: {
				systemavailable:"范围0-100",
				systemtransaction:"范围0-100",
				operationchanges:"范围0-100",
				fakesiteattachment:"范围0-100",
				continueDecline:"范围1-100",
				deviateAverage:"最小为0"
			}
		});

});	

function update(){
	if($("#reportform").valid()){
		 $.ajax({ url: "<%=request.getContextPath()%>/globalparameter/update.do", 
			 data:{
				 systemavailable:$("#systemavailable").val(),
				 systemtransaction:$("#systemtransaction").val(),
				 operationchanges:$("#operationchanges").val(),
				 fakesiteattachment:$("#fakesiteattachment").val(),
				 continueDecline:$("#continueDecline").val(),
				 continueDeclineEnable:$("#continueDeclineEnable").prop("checked"),
				 deviateAverage:$("#deviateAverage").val(),
				 deviateAverageEnable:$("#deviateAverageEnable").prop("checked")
			 },
			 success: function(msg){
				 $.messager.alert("系统提示","修改配置成功");
		 }});
	}
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
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>系统配置</a> > <a>全局参数配置</a> > </div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">报表显示参数</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="reportform" role="form" action="<%=request.getContextPath()%>/globalparameter/update.do" method="post">
             <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">系统可用率最小百分比：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="systemavailable" name="systemavailable" value="${parameter.systemavailable}">                            
                </div>
              </div>
                <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">系统交易成功率最小百分比：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="systemtransaction" name="systemtransaction" value="${parameter.systemtransaction}">                            
                </div>
              </div>
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">投产变更成功率最小百分比：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="operationchanges" name="operationchanges" value="${parameter.operationchanges}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">假冒网站查封率最小百分比：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="fakesiteattachment" name="fakesiteattachment" value="${parameter.fakesiteattachment}">                            
                </div>
              </div>
              <h3 class="show_tit">预警参数</h3>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">是否允许连续指标下降预警：</label>
                <div class="col-xs-5 row">
                  <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="checkbox" name="continueDeclineEnable" id="continueDeclineEnable" value="true" <c:if test="${parameter.continueDeclineEnable}">checked</c:if>>
                    </label> 
                 </div>
                </div>
              </div>
              <div class="form-group" id="mindiv">
                <label for="input" class="wid_200p control-label fl_left require">连续指标下降期数：</label>
                <div class="col-xs-4">
                  <input type="text" class="form-control" id="continueDecline"  name="continueDecline" value="${parameter.continueDecline}">
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">是否允许偏离平均值预警：</label>
                <div class="col-xs-5 row">
                  <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="checkbox" name="deviateAverageEnable" id="deviateAverageEnable" value="true" <c:if test="${parameter.deviateAverageEnable}">checked</c:if>>
                    </label> 
                 </div>
                </div>
              </div>
              <div class="form-group" id="mindiv">
                <label for="input" class="wid_200p control-label fl_left require">偏离平均值百分比：</label>
                <div class="col-xs-4">
                  <input type="text" class="form-control" id="deviateAverage"  name="deviateAverage" value="${parameter.deviateAverage}">
                </div>
              </div>
              <div class="form-group">
                <div class="col-xs-offset-5 col-xs-10">
                  <button type="button" class="btn btn-primary mr_20" onclick="update();return false;">修改配置</button>                  
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
    <div class="clearfix"></div>
  </div>
  <!-- #BeginLibraryItem "/Library/footer.lbi" -->
  <jsp:include page="../commons/footer.jsp"></jsp:include>
</body>
</html>