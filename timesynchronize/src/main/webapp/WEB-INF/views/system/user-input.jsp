<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>系统配置</a> > <a>用户管理</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">添加用户</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="userform" role="form" action="${ctx}/user/add.do" method="post">
              <div class="form-group">
                <label for="orgCode" class="col-xs-4 control-label pr_0 require">组织机构：</label>
                <div class="col-xs-5">
                  <select name="Organization"  class="form-control" id="Organization">
                  <option value=''>---请选择组织机构---</option>
                    <c:forEach items="${orgs}" var="org">
                      <option value=${org.orgId}>${org.name}</option>
		            </c:forEach>
		           </select>
                </div>
              </div>
                <input id="userId" type="hidden" value="${user.userId}"/>
             
               <div class="form-group">
                <label for="orgCode" class="col-xs-4 control-label pr_0 require">角色：</label>
                <div class="col-xs-5">
                     <div class="showline">
                     <div id="jstree2"></div>
                   </div>
                </div>
              </div>
              
              <div class="form-group">
                <label for="username" class="col-xs-4 control-label pr_0 require">用户名：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="username" name="username">
                </div>
              </div>
              
               <div class="form-group">
                <label for=passwd class="col-xs-4 control-label pr_0 require">密码：</label>
                <div class="col-xs-5">
                  <input type="password" class="form-control" id="passwd" name="passwd">
                </div>
              </div>
               <div class="form-group">
                <label for=passwd class="col-xs-4 control-label pr_0 require">确认密码：</label>
                <div class="col-xs-5">
                  <input type="password" class="form-control" id="cpasswd" name="cpasswd">
                </div>
              </div>
                            
               <div class="form-group">
                <label for="realname" class="col-xs-4 control-label pr_0">真实姓名：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="realname" name="realname">
                </div>
              </div>
              
              <div class="form-group">
                <label for="telephone" class="col-xs-4 control-label pr_0">固定电话：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="telephone" name="telephone">
                </div>
              </div>
              
              <div class="form-group">
                <label for="telephone" class="col-xs-4 control-label pr_0">移动电话：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="cellphone" name="cellphone">
                </div>
              </div>
              
             <div class="form-group">
                <label for="email" class="col-xs-4 control-label pr_0">邮箱：</label>
                <div class="col-xs-5">
                <input type="text" class="form-control" id="email" name="email">
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
	
	$("#passwd").val("");
	$("#username").val("");
	$("#userform").validate({
		rules: {
			username: {
				required: true,
				minlength:1,
				remote: {
                    url: "${ctx}/user/checkname.do",  
                    type: "get",   
                    dataType: "json",      
                    data: {                    
                    	username: function () {
                            return $("#username").val();
                        } ,
                        userid: function () {
                            return $("#userid").val();
                        }
                    }
				},
				maxlength:20
			},
			realname:{
				rangelength: [0,20]
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
			passwd :{
				required: true,
				rangelength: [4,16],
			},
			cpasswd :{
				required: true,
				equalTo:'#passwd',
				rangelength: [4,16],
			}
		},
		messages: {
			username: {
				required: "请输入用户名",
				remote:"用户名已经存在",
				rangelength: "用户名长度为1-20"
			},
			realname:{
				required: "请输入真实姓名",
				rangelength: "用户名长度为1-20"		
			},
			email:"Email格式必须合法",
			cellphone:{
				required: "请输入移动电话",
				digits:"电话必须为数字",
				rangelength: "电话长度不能超过11位"
			},
			telephone:{
				digits:"电话必须为数字",
				rangelength: "电话长度不能超过12位"
			},
			cpasswd :{
				required: "请输入确认密码",
				equalTo:"确认密码必须和原始密码一致",
				rangelength: "密码长度为4-16"	
			},
			passwd :{
				required: "请输入密码",
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

function adduser(){
	if($("#userform").valid()){
		var roles =getSelectedPermisions();
		if(""==roles){
			 $.messager.alert("系统提示", "请选择角色");
			return;
		}
		var organizationvar = $("#Organization").val();
		if(""==organizationvar || null==organizationvar){
			 $.messager.alert("系统提示", "请选择组织机构");
			 return;
		}
		
		$.ajax({ url: "${ctx}/user/add.do", 
 			data:{
 				 "Organization": $("#Organization").val(),
				  "username": $("#username").val(),
				  "passwd": $("#passwd").val(),
				  "realname": $("#realname").val(),
				  "telephone": $("#telephone").val(),
				  "cellphone": $("#cellphone").val(),
				  "email": $("#email").val(),
				  "areaCode": $("#areaCode").val(),
				  "roles": roles 
 			},
 			success: function(msg){
 				 if("1"==msg)
 					 $.messager.alert("系统提示","对不起，你所赋予的角色已超过自身的角色，请联系管理员！");
 				  else  
 					window.location.href="${ctx}/user/list.do?page=${pageNo}";
 	      }});
	}
}

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

</script>
</body>
</html>