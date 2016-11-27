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
				ip: {
					required: true
				},
				port:{
					required: true ,
					digits:true,
					range:[1,65535] 
				}  ,
				xusername: {
					required: true,
					rangelength:[1,24]
				},
				xpassword: {
					rangelength:[0,24]
				},
				daily: {
					required: true,
					digits:true,
					range:[0,23]  
				}
			},
			messages: {
				ip:"请输入合法的数据库IP",
				port:"请输入数据库端口号(1-65535)",
				xusername: "请输入数据库用户名(1-24)",
				xpassword: "请输入数据库密码(0-24)",
				daily: "请输入数据库备份时间"
			}
		});

});	

function update(){
	if($("#reportform").valid()){
		 $.ajax({ url: "<%=request.getContextPath()%>/backupdb/update.do", 
			 data:{
				 ip:$("#ip").val(),
				 port:$("#port").val(),
				 username:$("#xusername").val(),
				 password:$("#xpassword").val(),
				 daily:$("#daily").val()
			 },
			 success: function(msg){
				 $.messager.alert("系统提示","修改配置成功");
		 }});
	}
}
var nowstatus =${backup.taskstatus};
function changestatus(){
	var alertmessage ="";
	 if(nowstatus){
		 alertmessage ="您确定挂起数据库备份任务？";
	 }else{
		 alertmessage ="您确定开启数据库备份任务？";
	 }
	$.messager.model = { 
	        ok:{ text: "确定", classed: 'btn-default' },
	        cancel: { text: "取消", classed: 'btn-error' }
	      };
	      $.messager.confirm("系统提示", alertmessage, function() { 
	    	  $.ajax({ url: "<%=request.getContextPath()%>/backupdb/changeStatus.do", 
	 			 success: function(msg){
	 				window.location.href="<%=request.getContextPath()%>/backupdb/backup.do";
	 		 }});
	      }); 
		
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
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>系统配置</a> > <a>数据库备份</a> > </div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">备份参数设置</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="reportform" role="form" action="<%=request.getContextPath()%>/backup/update.do" method="post">
             <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">数据库所在IP：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="ip" name="ip" value="${backup.ip}">                            
                </div>
              </div>
                <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">数据库所占用端口：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="port" name="port" value="${backup.port}">                            
                </div>
              </div>
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">数据库用户名：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="xusername" name="xusername" value="${backup.username}">                            
                </div>
              </div>
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left">数据库密码：</label>
                <div class="col-xs-5">           
                  <input type="password" class="form-control" id="xpassword" name="xpassword" value="${backup.password}">                            
                </div>
              </div>
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">数据库每天备份时间(时)：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="daily" name="daily" value="${backup.daily}">                            
                </div>
              </div>
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">数据库备份任务状态：</label>
                <div class="col-xs-5">   
                  <span id="statustext" class="form-control">
                  <c:if test="${backup.taskstatus}">已经开启</c:if>
                  <c:if test="${!backup.taskstatus}">当前挂起</c:if>
                  </span>        
                </div>
              </div>
              <div class="form-group">
                <div class="col-xs-offset-5 col-xs-10">
                  <button type="button" class="btn btn-primary mr_20" onclick="update();return false;">修改配置</button>
                  <button id="taskbutton" type="button" class="btn btn-default" onclick="changestatus();return false;">
                   <c:if test="${backup.taskstatus}">挂起</c:if>
                   <c:if test="${!backup.taskstatus}">开启</c:if>
                  </button>
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