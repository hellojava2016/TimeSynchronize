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
<link rel="stylesheet" type="text/css" href="${ctx}/css/jtree/style.min.css"/>
<link rel="stylesheet" type="text/css" media="screen"  href="${ctx}/css/bootstrap/bootstrap-datetimepicker.min.css">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <link rel="stylesheet" type="text/css" href="${ctx}/css/ie_8.css"/>
<![endif]-->
<script type="text/javascript" src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/moment-with-langs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js" ></script>
<script type="text/javascript" src="${ctx}/js/ztree/jstree.min.js" ></script>
 <jsp:include page="../commons/bankajax.jsp"></jsp:include> 
<STYLE type="text/css">
input.error { border: 1px dotted red; }   
label.error {   
    color: red;display: none;   

}  
.showline { 
 border-style:  solid ;
 border-color:black;
border-width: 1px;
}

</STYLE>
</head>
<body>
<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
</c:if>
<div class="container"> 
  <jsp:include page="../commons/header.jsp"></jsp:include>
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-system.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> &gt; <a>系统管理</a> &gt; <a>公告发布</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">公告发布</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="roleform" role="form" action="${ctx}/message/add.do"  method="post">              
             <div class="form-group">
                <label for="roleName" class="col-xs-4 control-label pr_0 require">标题：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="title" name="title" >
                </div>
              </div>
              
              <div class="form-group">
              <label for="input" class="col-xs-4 control-label pr_0 require">紧急重要度：</label>
                <div class="col-xs-5">                 
                   <select name="critical" id="critical"   class="form-control">
                   <option value="1">普通 </option>
                   <option value="2">重要</option>
                   <option value="3">紧急</option>
                   </select>             
                </div>
              </div>
              
              <div class="form-group">
                <label for="description" class="col-xs-4 control-label pr_0 require">内容：</label>
                <div class="col-xs-5">
                  <textarea class="form-control" id="dmessage" name="dmessage" rows="10"></textarea>
                </div>
              </div>              
            
               <div class="form-group">
                <label  class="col-xs-4 control-label pr_0 require">选择接收机构：</label>
                <div class="col-xs-5">
                    <div class="showline">
                     <div id="jstree2"></div>
                   </div>
                </div>
              </div>
              
                <div class="form-group">
                <label   class="col-xs-4 control-label pr_0"></label>
                <div class="col-xs-5">
                       <input type="button" value="确定" class="btn btn-primary mr_20" onclick="updateRole();"/>
                        <button type="button" class="btn btn-default mr_20" onclick="window.location.href=document.referrer;">取消</button>
                </div>
              </div> 
            </form>
          </div>
        </div>
      </div>
    </div>
    <div class="clearfix"></div>
  </div>
</div>
<jsp:include page="../commons/footer.jsp"></jsp:include>
<script type="text/javascript">
var  r=[];
$(function(){
	$("form label.require").each(function(){
        $(this).append('<font color="red">*</font>'); //然后将它追加到文档中  
    });
	$("#roleform").validate({
		rules: {
			title:{
				required: true,
				rangelength: [1,255]
			} ,
			dmessage:{
				required: true,
				rangelength: [1,1024]
			}  
		},
		messages: {
			title:{
				required: "请输入消息标题",
				rangelength: "消息标题长度为1-255"		
			}  ,
			dmessage:{
				required: "请输入消息内容",
				rangelength: "消息内容长度为1-1024"		
			}  
		}
	});

	$('#jstree2').jstree({'plugins':["wholerow","checkbox"], 'core' : {
		'data' :${orgs}
	}});

	$('#jstree2').on('changed.jstree', function (e, data) {
	    var i, j;
	    r=[];
	    for(i = 0, j = data.selected.length; i < j; i++) {
	      r.push(data.instance.get_node(data.selected[i]).id);
	    }
	  }).jstree(); 
});

function getSelectedPermisions(){
	var result="";
	 if(r.length==0){
           
	  }else{
        for(var k=0;k< r.length;k++){
              result=result+r[k];
              if(k!=(r.length-1))
            	  result=result+',';
            }
	  }
	 return result;
}

function updateRole(){
	if($("#roleform").valid()){
		 ajaxOptions.url = "${ctx}/message/add.do";
 		 ajaxOptions.params = {
 				 "title": $("#title").val(),
 				  "message": $("#dmessage").val(),
 				 "critical": $("#critical").val(),
 				 "orgs": getSelectedPermisions()	 
 				 };
 		 ajaxOptions.success = function(msg){
 			window.location.href="${ctx}/message/list.do";
 	    };
 		simpleAjax(ajaxOptions);
	}
}
</script>
</body>
</html>