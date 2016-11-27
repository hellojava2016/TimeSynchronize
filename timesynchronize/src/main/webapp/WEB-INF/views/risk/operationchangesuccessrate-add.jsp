<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
<script src="${ctx}/js/select/jquery.cityselect.js" type="text/javascript" ></script>
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/moment-with-langs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js" ></script>
<script type="text/javascript">
$(function (){
	$("form label.require").each(function(){
        $(this).append('<font color="red">*</font>'); //然后将它追加到文档中  
    });
	$('.input-append').datetimepicker({
        format: 'YYYY-MM-DD',
        language: 'zh-CN',
        pickDate: true,
        pickTime: false,
        inputMask: false
      });
$("#city_5").citySelect({
	url:"<%=request.getContextPath()%>/report/operationchangesuccessrate/gettypes.do?defaultChoose=true",
	prov:"3001",
	city:"",
	dist:"",
	nodata:"none"
});    
$("#operform").validate({
	rules: {
		reportdate : {
			required : true,
			date:true,
		},
		dc: {
			required: true,
			digits:true
		},
		sdc: {
			required: true,
			digits:true
		} 
	},
	messages: {
		reportdate : {
			required : "请输入报送日期",
			date : "报送日期格式为2014-10-06"
		},
		dc: {
			required: "请输入实施总数量",
			digits:"实施总封数量必须为整数"
		},
		sdc: {
			required: "请输入成功实施总数量",
			digits:"成功实施总数量必须为整数"
		} 
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
      <jsp:include page="left-report.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> &gt; <a>数据采集</a> &gt; <a>投产变更成功率</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">投产变更成功率</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="operform" action="<%=request.getContextPath()%>/report/operationchangesuccessrate/add.do" method="post">
           <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">风险指标：</label>
                <div id="city_5">  &nbsp; &nbsp; &nbsp; 
  		          <select class="prov" name="prov" id="prov"></select>
		          <select class="city" name="city" disabled="disabled" id="city"></select>
		          <select class="dist" name="dist" disabled="disabled" id="dist"></select>
                </div>                
              </div>
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">报送日期(月)：</label>
                <div class="col-xs-4">
                  <div id="submit_time" class="input-append date wid_60">
                    <input type="text" class="pick_timeshow" name="reportdate">
                    </input>
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                </div>
              </div>                            
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">实施总数量：</label>
                <div class="col-xs-4">
                  <input type="text" class="form-control" id="dc" name="dc" >
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">成功实施数量：</label>
                <div class="col-xs-4">
                  <input type="text" class="form-control" id="sdc" name="sdc" >
                </div>
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