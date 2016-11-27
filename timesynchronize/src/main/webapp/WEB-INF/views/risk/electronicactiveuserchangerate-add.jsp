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
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/moment-with-langs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/bootstrap-datetimepicker.min.js"></script>
<script src="${ctx}/js/select/jquery.cityselect.js" type="text/javascript" ></script>
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
});	  
</script>
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <link rel="stylesheet" type="text/css" href="${ctx}/css/ie_8.css"/>
      <script src="${ctx}/js/html5shiv.js"></script>
      <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container"> 
  <jsp:include page="../commons/header.jsp"></jsp:include>
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-report.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>数据采集</a> &gt; <a>主要电子渠道活跃用户变化率</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">主要电子渠道活跃用户变化率</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="electronicactiveform" role="form" action="<%=request.getContextPath()%>/report/electronicactiveuserchangerate/add.do" method="post">
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
                        <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> 
                      </div>
                   </div>
             </div>           
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">当前活跃数：</label>
                <div class="col-xs-4">
                  <input type="text" class="form-control" id="count"  name="count">
                </div>
              </div>
              </div>
              
              <div class="form-group">
                <div class="col-xs-offset-5 col-xs-10">
                  <button type="submit" class="btn btn-primary mr_20">确定</button>
                  <button type="button" class="btn btn-default" onclick="toList()">取消</button>
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
  <!-- #EndLibraryItem --> </div>
<!-- #BeginLibraryItem "/Library/script.lbi" --> 
<!-- #EndLibraryItem -->
<script type="text/javascript">
		$(function() {
			$("#electronicactiveform").validate({
				rules : {
					reportdate : {
						required : true,
						date:true,
					},
					count : {
						required : true,
						digits:true,
						max:100000000,
					}
				},
				messages : {
					reportdate : {
						required : "请输入报送日期",
						date : "报送日期格式为2014-10-06"
					},
					count : {
						required : "请输入当前交易量",
						digits : "当前交易量必须为整数",
						max:"当前活动数目不能超过100000000",
					}
				}
			});
			
			$("#city_5").citySelect({
				url:"<%=request.getContextPath()%>/report/electronicactiveuserchangerate/gettypes.do?defaultChoose=true",
				prov:"8001",
				city:"",
				dist:"",
				nodata:"none"
			});
			
			$('.input-append').datetimepicker({
				format : 'YYYY-MM-DD',
				language : 'zh-CN',
				pickDate : true,
				pickTime : true,
				hourStep : 1,
				minuteStep : 15,
				secondStep : 30,
				inputMask : true
			});
		});
		
		function toList() {
			window.location.href = "${ctx}/report/electronicactiveuserchangerate/list.do";
		}
		
	</script>
</body>
</html>