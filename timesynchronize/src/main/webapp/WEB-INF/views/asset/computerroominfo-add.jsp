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
<link rel="stylesheet" type="text/css" href="${ctx}/css/jtree/style.min.css"/>
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
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.message.zh_CN.js" ></script>
<script type="text/javascript" src="${ctx}/js/ztree/jstree.min.js" ></script>
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
$("#sysavalableform").validate({
	rules: {
		uniqueVal: {
			required: true,
			minlength:1,
			remote: {
                url: "${ctx}/asset/computerroominfo/checkname.do?id=${id}",  
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
		serverTime: {
			required: true,
			date:true
		},
		area: {
			required: true,
			number:true,
			min:0,
			max:100000
		},
		location: {
			required: true,
			minlength:1,
			maxlength:32
		},
		power: {
			required: true,
			digits:true,
			range:[1,100000]
		 },
		upsCount: {
			required: true,
			digits:true,
			range:[1,100000]
		} ,
		precisionAcCount: {
			required: true,
			digits:true,
			range:[1,100000]
		} 
	},
	messages: {	
	uniqueVal: {
			required: "请输入机房编号",
			minlength:"编号长度最少1位",
			maxlength:"编号长度最长32位",
			remote:"编号不能重复"
		}	,
		manufacturer: {
			required: "请输入制造商",
			minlength:"制造商长度为1-32",
			maxlength:"制造商长度为1-32"
		},
		purpose: {
			required: "请输入机房用途",
			minlength:"机房用途长度为1-32",
			maxlength:"机房用途长度为1-32"
		},
		serverTime: {
			required: "请输入机房服务时间",
			date:"服务时间必须为日期格式"
		},
		area:"请输入机房面积,0-100000",
		location: {
			required: "请输入机房位置",
			minlength:"机房位置长度为1-32",
			maxlength:"机房位置长度为1-32"
		},
		power:"请输入设施功率,1-100000",
		upsCount: "请输入UPS数量,1-100000",
		precisionAcCount: "请输入精密空调数量,1-100000"
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
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>配置信息</a> > <a>机房</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">机房</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="sysavalableform" role="form"
            <c:choose> <c:when test="${id==0}">action="<%=request.getContextPath()%>/asset/computerroominfo/add.do"</c:when>
            <c:otherwise>action="<%=request.getContextPath()%>/asset/computerroominfo/update.do"</c:otherwise>
            </c:choose> method="post">
            <input type="hidden" name="id" value="${id}">
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">编号：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="uniqueVal" name="uniqueVal" <c:if test="${id!=0}"> readonly</c:if> value="${cr.uniqueVal}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">机房所属商：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="manufacturer" name="manufacturer" value="${cr.manufacturer}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设施用途：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="purpose" name="purpose" value="${cr.purpose}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">机房及机柜所占面积(平方米)：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="area" name="area" value="${cr.area}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备位置：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="location" name="location" value="${cr.location}">                            
                </div>
              </div>
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设施功率(W)：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="power" name="power" value="${cr.power}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">开始服务时间：</label>
                 <div class="col-xs-5">
                  <div id="submit_time" class="input-append date wid_60">
                    <input type="text" class="pick_timeshow" id="serverTime" name="serverTime" value="<fmt:formatDate value="${cr.serverTime}" pattern="yyyy-MM-dd"/>">
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">UPS数量：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="upsCount" name="upsCount" value="${cr.upsCount}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">精密空调个数：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="precisionAcCount" name="precisionAcCount" value="${cr.precisionAcCount}">                            
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