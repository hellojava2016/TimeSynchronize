<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
                url: "${ctx}/asset/upsinfo/checkname.do?id=${id}",  
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
			range:[0,100000]
		},
		outputVoltage: {
			required: true ,
			digits:true,
			range:[0,100000]
		},
		inputFrequency: {
			required: true ,
			digits:true,
			range:[0,100000]
		},
		outputFrequency: {
			required: true ,
			digits:true,
			range:[0,100000]
		},
		power: {
			required: true ,
			digits:true,
			range:[0,10000]
		},
		roomUV:{
			required: true 
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
		type:"请输入机房UPS类型，长度(1-32)" ,
		roomUV:"请选择机房",
		inputVoltage:"请输入输入电压，范围0-10000" ,
		outputVoltage:"请输入输出电压，范围0-10000" ,
		inputFrequency:"请输入UPS输入频率，范围0-100000" ,
		outputFrequency:"请输入UPS输出频率，范围0-100000" ,
		power:"请输入各相有功功率，范围0-10000" ,
		name:"请输入UPS名称，长度(1-32)" 
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
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>配置信息</a> > <a>机房UPS</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">机房UPS</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="sysavalableform" role="form" 
            <c:choose> <c:when test="${id==0}">action="<%=request.getContextPath()%>/asset/upsinfo/add.do"</c:when>
            <c:otherwise>action="<%=request.getContextPath()%>/asset/upsinfo/update.do"</c:otherwise>
            </c:choose> method="post">
            <input type="hidden" name="id" value="${id}">
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">编号：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="uniqueVal" name="uniqueVal" <c:if test="${id!=0}"> readonly</c:if> value="${ups.uniqueVal}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">所属机房编号：</label>
                <div class="col-xs-5">
                  <select name="roomUV"  class="selectcsss form-control">
                    <c:forEach items="${crs}" var="cr">
                      <option value=${cr.crId} <c:if test="${ups.computerRoomInfo.crId==cr.crId}"> selected</c:if>>${cr.uniqueVal}</option>
		            </c:forEach>
		           </select>
		        </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">机房UPS类型：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="type" name="type" value="${ups.type}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">UPS名称：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="name" name="name" value="${ups.name}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">输入电压(V)：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="inputVoltage" name="inputVoltage" value="${ups.inputVoltage}">                            
                </div>
              </div>
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">输出电压(V)：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="outputVoltage" name="outputVoltage" value="${ups.outputVoltage}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">UPS输入频率(HZ)：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="inputFrequency" name="inputFrequency" value="${ups.inputFrequency}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">UPS输出频率(HZ)：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="outputFrequency" name="outputFrequency" value="${ups.outputFrequency}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">各相有功功率(KVA)：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="power" name="power" value="${ups.power}">                            
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