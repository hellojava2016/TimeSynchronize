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
<script type="text/javascript" src="${ctx}/js/jQuery-jcDate.js" ></script>
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
$("#hostform").validate({
	rules: {
		uniqueVal: {
			required: true,
			minlength:1,
			remote: {
                url: "${ctx}/asset/host/checkname.do?id=${id}",  
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
		cpu:{
			required: true,
			minlength:1,
			maxlength:32
		},
		serialNumber:{
			required: true,
			minlength:1,
			maxlength:32
		},
		location: {
			required: true,
			minlength:1,
			maxlength:32
		},
		manufacturer: {
			required: true,
			minlength:1,
			maxlength:32
		},
		purpose: {
			required: true,
			minlength:1,
			maxlength:32
		},
		serviceTime: {
			required: true,
			minlength:1,
			maxlength:32
		},
		type: {
			required: true,
			minlength:1,
			maxlength:32
		},
		cpuCount: {
			required: true,
			digits:true,
			range:[1,10000]
		},
		hardDiskSize: {
			required: true,
			digits:true,
			range:[1,10000]
		},
		memorySize: {
			required: true,
			digits:true,
			range:[1,10000]
		},
		name: {
			required: true,
			minlength:1,
			maxlength:32
		 } 
	},
	messages: {
		uniqueVal: {
			required: "请输入主机编号",
			minlength:"编号长度最少1位",
			maxlength:"编号长度最长32位",
			remote:"编号不能重复"
		},
		cpu:"请输入主机CPU型号，长度(1-32)" ,
		serialNumber:"请输入序列号，长度(1-32)" ,
		ip:"请输入主机IP，长度(1-32)" ,
		location:"请输入主机位置，长度(1-32)" ,
		manufacturer:"请输入主机制造商，长度(1-32)" ,
		purpose:"请输入主机用途，长度(1-32)" ,
		serviceTime:"请输入主机时间" ,
		type:"请输入主机型号，长度(1-32)" ,
		cpuCount:"请输入主机CPU数量，1-10000" ,
		hardDiskSize:"请输入主机硬盘大小（GB），1-10000" ,
		memorySize:"请输入主机内存大小,1-10000" ,
		name:"请输入设备名称，长度(1-32)" 
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
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>配置信息</a> > <a>主机</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">主机</h3>          
          <div class="show_conment">
            <form class="form-horizontal" id="hostform" role="form"  
            <c:choose> <c:when test="${id==0}">action="<%=request.getContextPath()%>/asset/host/add.do"</c:when>
            <c:otherwise>action="<%=request.getContextPath()%>/asset/host/update.do"</c:otherwise>
            </c:choose> method="post">
            <input type="hidden" name="id" value="${id}">
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">编号：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="uniqueVal" name="uniqueVal" <c:if test="${id!=0}"> readonly</c:if> value="${host.uniqueVal}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">序列号：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="serialNumber" name="serialNumber" value="${host.serialNumber}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备生产产商：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="manufacturer" name="manufacturer" value="${host.manufacturer}">                            
                </div>             
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备生产型号：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="type" name="type" value="${host.type}">                            
                </div>
              </div>
             <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备名称：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="name" name="name" value="${host.name}">                            
                </div>
              </div>
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left">主机IP：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="ip" name="ip" value="${host.ip}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">主机用途：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="purpose" name="purpose" value="${host.purpose}">                            
                </div>
              </div>
                <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">主机开始服务时间：</label>
                <div class="col-xs-5">
                  <div id="submit_time" class="input-append date wid_60">
                    <input id="serviceTime" type="text" class="pick_timeshow" name="serviceTime" value="<fmt:formatDate value="${host.serviceTime}" pattern="yyyy-MM-dd"/>">
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                </div>
              </div>              
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">主机CPU型号：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="cpu" name="cpu" value="${host.cpu}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">主机的CPU数量：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="cpuCount" name="cpuCount" value="${host.cpuCount}">                            
                </div>
              </div>
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备内存大小(G)：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="memorySize" name="memorySize" value="${host.memorySize}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备硬盘大小(G)：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="hardDiskSize" name="hardDiskSize" value="${host.hardDiskSize}">                            
                </div>
              </div>
              
              
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备位置：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="location" name="location" value="${host.location}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left">设备类别：</label>
                <div class="col-xs-5">           
                      <select id="category" name="category" class="form-control">
                          <option value="0" <c:if test="${host.category==0}"> selected</c:if>>PC机器</option>
                          <option value="1" <c:if test="${host.category==1}"> selected</c:if>>小型机</option>
                          <option value="2" <c:if test="${host.category==2}"> selected</c:if>>大型机</option>
                      </select>                            
                </div>
              </div>
              
              <div class="form-group">
                <div class="col-xs-offset-3 col-xs-6">
                  <button type="submit" class="btn btn-primary mr_20">确定</button>
                  <button type="reset" class="btn btn-default" onclick="window.location.href=document.referrer;">取消</button>
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
   </div>
</body>
</html>