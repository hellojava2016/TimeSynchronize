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
$("#sysavalableform").validate({
	rules: {
		uniqueVal: {
			required: true,
			minlength:1,
			remote: {
                url: "${ctx}/asset/precisionacinfo/checkname.do?id=${id}",  
                type: "get",   
                dataType: "json",      
                data: {                    
                	uniqueVal: function () {
                        return $("#uniqueVal").val();
                    }  
                }
			},
			maxlength:32
		},
		type: {
			required: true,
			minlength:1,
			maxlength:32
		},
		inputVoltage: {
			required: true ,
			digits:true,
			range:[0,10000]
		},
		supplyTemp: {
			required: true ,
			digits:true,
			range:[0,10000]
		},
		supplyHumidity: {
			required: true ,
			digits:true,
			range:[0,10000]
		},
		returnHumidity: {
			required: true ,
			digits:true,
			range:[0,10000]
		},
		name: {
			required: true,
			minlength:1,
			maxlength:32
		 } 
	},
	messages: {
		uniqueVal: {
			required: "请输入数据库编号",
			minlength:"编号长度最少1位",
			maxlength:"编号长度最长32位",
			remote:"编号不能重复"
		},
		type:"请输入精密空调型号，长度(1-32)" ,
		inputVoltage:"请输入输入电压，0-10000" ,
		supplyTemp:"请输入精密空调送风温度，0-10000" ,
		supplyHumidity:"请输入精密空调送风湿度，0-10000" ,
		returnHumidity:"请输入精密空调回风湿度，0-10000" ,
		name:"请输入精密空调名称，长度(1-32)" 
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
      <jsp:include page="left-asset.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>配置信息</a> > <a>精密空调</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">精密空调</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="sysavalableform" role="form" 
            <c:choose> <c:when test="${id==0}">action="<%=request.getContextPath()%>/asset/precisionacinfo/add.do"</c:when>
            <c:otherwise>action="<%=request.getContextPath()%>/asset/precisionacinfo/update.do"</c:otherwise>
            </c:choose> method="post">
            <input type="hidden" name="id" value="${id}">
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">编号：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="uniqueVal" name="uniqueVal" <c:if test="${id!=0}"> readonly</c:if> value="${ac.uniqueVal}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">所属机房编号：</label>
                <div class="col-xs-5">
                  <select name="roomUV"  class="selectcsss form-control">
                    <c:forEach items="${crs}" var="cr">
                      <option value=${cr.crId} <c:if test="${ac.computerRoomInfo.crId==cr.crId}"> selected</c:if>>${cr.uniqueVal}</option>
		            </c:forEach>
		           </select>
		        </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">精密空调名称：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="name" name="name" value="${ac.name}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">精密空调型号：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="type" name="type" value="${ac.type}">                            
                </div>
              </div>
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">精密空调送风温度(摄氏度)：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="supplyTemp" name="supplyTemp" value="${ac.supplyTemp}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">精密空调送风湿度(%RH)：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="supplyHumidity" name="supplyHumidity" value="${ac.supplyHumidity}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">精密空调回风湿度(%RH)：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="returnHumidity" name="returnHumidity" value="${ac.returnHumidity}">                            
                </div>
              </div>
              <div class="form-group">
                <div class="col-xs-offset-3 col-xs-6">
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
  <!-- #EndLibraryItem --> </div>
</body>
</html>