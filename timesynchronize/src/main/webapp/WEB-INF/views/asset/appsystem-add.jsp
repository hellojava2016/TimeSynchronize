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
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.message.zh_CN.js" ></script>
<script type="text/javascript" src="${ctx}/js/ztree/jstree.min.js" ></script>
<script type="text/javascript">
$(function (){
	$("form label.require").each(function(){
        $(this).append('<font color="red">*</font>'); //然后将它追加到文档中  
    }); 
$("#dbform").validate({
	rules: {
		appName: {
			required: true,
			minlength:1,
			remote: {
                url: "${ctx}/asset/appsystem/checkname.do?id=${id}",  
                type: "get",   
                dataType: "json",      
                data: {                    
                	appName: function () {
                        return $("#appName").val();
                    }  
                }
			},
			maxlength:32
		},
		description: {
			required: true,
			minlength:1,
			maxlength:255
		}
	},
	messages: {
		uniqueVal: {
			required: "请输入应用系统名称",
			minlength:"唯一值长度最少1位",
			maxlength:"唯一值长度最长32位",
			remote:"应用系统名称不能重复"
		},
		description:"请输入应用系统描述，长度(1-255)"
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
      <div class="breadcrumb"><span>您当前所在的位置：</span> &gt; <a>配置信息</a> &gt; <a>应用系统</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">应用系统</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="dbform" role="form"  
            <c:choose> <c:when test="${id==0}">action="<%=request.getContextPath()%>/asset/appsystem/add.do"</c:when>
            <c:otherwise>action="<%=request.getContextPath()%>/asset/appsystem/update.do"</c:otherwise>
            </c:choose> method="post">
            <input type="hidden" name="id" value="${id}">
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">名称：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="appName" name="appName" value="${as.appName}">                            
                </div>
              </div>
              
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">描述：</label>
                <div class="col-xs-5">     
                  <textarea rows="4" cols="" id="description" name="description" class="form-control">${as.description}</textarea>      
                 </div>                          
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
   </div>
</body>
</html>