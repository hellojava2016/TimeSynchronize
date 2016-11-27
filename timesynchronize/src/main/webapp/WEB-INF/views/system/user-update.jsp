<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

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
</head>
<body>
<div class="container"> 
  <jsp:include page="../commons/header.jsp" />
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-system.jsp" />
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> &gt; <a>系统配置</a> &gt; <a>用户管理</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">修改用户</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="userform" role="form" action="${ctx}/user/update.do" method="post">
              <div class="form-group">
                <label for=passwd class="col-xs-4 control-label pr_0">组织机构：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="name" name="name" readonly value="${modifyuser.organization.name}">
                </div>
              </div>
                <input id="userId" name="userId" type="hidden" value="${modifyuser.userId}"/>
             
               <div class="form-group">
                <label for="orgCode" class="col-xs-4 control-label pr_0">角色：</label>
                <div class="col-xs-5">
                   <div class="showline">
                     <div id="jstree2"></div>
                   </div>
                </div>
              </div>
              
              <div class="form-group">
                <label for="username" class="col-xs-4 control-label pr_0">用户名：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="username" name="username" readonly value="${modifyuser.username}">
                </div>
              </div>
              
               <div class="form-group">
                <label for="realname" class="col-xs-4 control-label pr_0">真实姓名：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="realname" name="realname"  value="${modifyuser.realname}">
                </div>
              </div>
              
              <div class="form-group">
                <label for="telephone" class="col-xs-4 control-label pr_0">固定电话：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="telephone" name="telephone"  value="${modifyuser.telephone}">
                </div>
              </div>
              
              <div class="form-group">
                <label for="telephone" class="col-xs-4 control-label pr_0">移动电话：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="cellphone" name="cellphone"  value="${modifyuser.cellphone}">
                </div>
              </div>
              
             <div class="form-group">
                <label for="email" class="col-xs-4 control-label pr_0">邮箱：</label>
                <div class="col-xs-5">
                <input type="text" class="form-control" id="email" name="email"  value="${modifyuser.email}">
                </div>
             </div>
              
              
                <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">是否修改密码：</label>
                <div class="col-xs-5 row">
                  <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="radio" name="passradio" id="passradio1" value="false" checked>否
                    </label> 
                 </div>
                  <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="radio" name="passradio" id="passradio2" value="true">是
                    </label> 
                  </div>
                </div>
              </div>     
              
               <div class="form-group" id="passwddiv1">
                <label for=passwd class="col-xs-4 control-label pr_0">密码：</label>
                <div class="col-xs-5">
                  <input type="password" class="form-control" id="passwd" name="passwd"  value="">
                </div>
              </div>
              
                 <div class="form-group" id="passwddiv2">
                <label for=passwd class="col-xs-4 control-label pr_0">确认密码：</label>
                <div class="col-xs-5">
                  <input type="password" class="form-control" id="cpasswd" name="cpasswd"  value="">
                </div>
              </div>
              
             <div class="form-group">
                <label   class="col-xs-4 control-label pr_0"></label>
                <div class="col-xs-5">
                      <input type="button" value="确定" class="btn btn-primary mr_20" onclick="adduser()"/>
                        <input type="button" value="取消" class="btn btn-default" onclick="history.back()"/>
                </div>
              </div>              
            </form>
          </div>
        </div>
      </div>
    </div>
    <div class="clearfix"></div>
    <jsp:include page="../commons/footer.jsp" />
  </div>
</div>


<script type="text/javascript">
var  r=[];
$(function(){
	$("form label.require").each(function(){
        $(this).append('<font color="red">*</font>'); //然后将它追加到文档中  
    });
	$("#passwddiv2").hide();
	$("#passwddiv1").hide();
	$("#passradio1").change(function(){
		var checnresult = $("#passradio1").is(':checked');
		if(checnresult){
			$("#passwddiv2").hide();
			$("#passwddiv1").hide();
		}else{
			$("#passwddiv1").show();
			$("#passwddiv2").show();
		}
	});
	
	$("#passradio2").change(function(){
		var checnresult = $("#passradio1").is(':checked');
		if(checnresult){
			$("#passwddiv2").hide();
			$("#passwddiv1").hide();
		}else{
			$("#passwddiv1").show();
			$("#passwddiv2").show();
		}
	});
	
	$("#userform").validate({
		rules: {
			username: {
				required: true,
				minlength:1,
				maxlength:20
			},
			cellphone:{
				digits:true,
				rangelength: [0,11]
			},
			telephone:{
				digits:true,
				rangelength: [0,12]
			},
			email:{
				email:true
			},
			realname:{
				rangelength: [1,20]
			},
			passwd :{
				required: function(){return  $('#passradio2').is(':checked');},
				rangelength: [4,16]
			},
			cpasswd :{
				required: function(){return  $('#passradio2').is(':checked');},
				rangelength: [4,16],
				equalTo: "#passwd"
			}
		},
		messages: {
			username: {
				required: "请输入用户名",
				remote:"用户名已经存在",
				rangelength: "用户名长度为1-20"			},
			realname:{
				required: "请输入真实姓名",
				rangelength: "用户名长度为1-20"		
			},
			email:"Email格式必须合法",
			cellphone:{
				digits:"电话必须为数字",
				rangelength: "电话长度不能超过11位"
			},
			telephone:{
				digits:"电话必须为数字",
				rangelength: "电话长度不能超过11位"
			},
			passwd :{
				required: "请输入密码",
				rangelength: "密码长度为4-16"	
			},
			cpasswd :{
				required: "请输入确认密码",
				equalTo:"确认密码必须和原密码相同",
				rangelength: "密码长度为4-16"	
			}
		}
	});
	
	$('.input-append').datetimepicker({
        format: 'YYYY-MM-DD',
        language: 'zh-CN',
        pickDate: true,
        pickTime: false,
        inputMask: false
      });
	
	$('#jstree2').jstree({'plugins':["wholerow","checkbox"], 'core' : {
		'data' :${roles}
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
function adduser(){
	if($("#userform").valid()){
		var roles =getSelectedPermisions();
		if(""==roles){
			 $.messager.alert("系统提示", "请选择角色");
			return;
		}
		var checnresult = $("#passradio1").is(':checked');
		$.ajax({ url: "${ctx}/user/update.do", 
 			data:{
 				 "Organization": $("#Organization").val(),
				  "username": $("#username").val(),
				  "passwd": $("#passwd").val(),
				  "realname": $("#realname").val(),
				  "telephone": $("#telephone").val(),
				  "cellphone": $("#cellphone").val(),
				  "email": $("#email").val(),
				  "areaCode": $("#areaCode").val(),
				  "updatepassword":checnresult,
				  "userId": $("#userId").val(),
				  "roles": roles 
 			},
 			success: function(msg){
 				 if("1"==msg)
 					 $.messager.alert("系统提示","对不起，你所赋予的角色已超过自身的角色，请联系管理员！");
 					 else  
 						window.location.href="${ctx}/user/list.do?page=${pageNo}&message=2";
 	      }});
		
		
	}
}
</script>
</body>
</html>