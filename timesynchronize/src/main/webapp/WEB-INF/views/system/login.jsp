<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录页</title>
<link rel="stylesheet" type="text/css" href="css/reset.css"/>
<link rel="stylesheet" type="text/css" href="css/login.css"/>
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <link rel="stylesheet" type="text/css" href="${ctx}/css/ie_8.css"/>
<![endif]-->
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#showerror").css("color","RED");
	$("input").bind({
		focus:(function(){
			if($(this).val()==this.defaultValue){
				$(this).val("");
				$(this).css("color","#666");
			}
			else{
				$(this).css("color","#CCC");
			}
		}),
		blur:(function(){
			if($(this).val()==""){
				$(this).val(this.defaultValue);
				$(this).css("color","#CCC");
			}
			else{
				$(this).css("color","#666");
			}
		})
	})
	 var username =$.cookie('username');
    var passwd= $.cookie('passwd');
    $("#username").val(username);
    $("#passwd").val(passwd);
    
    $(document).keypress(function(e)    
    	    {    
    	        switch(e.which)    
    	        {    
    	            case 13:login();     
    	              break;      
    	        }    
    	    });  
    
});

function login(){
	$.ajax({ url: "${ctx}/user/login.do",  
		data:{
			username:$("#username").val(),
			passwd:$("#passwd").val()
		},
		success: function(data){
           if("2"==data){
        		$("#showerror").empty().html("用户名或密码错误");
           }else if("3"==data){
        		$("#showerror").empty().html("请输入用户名密码");
           }else if("4"==data){
        		$("#showerror").empty().html("用户已经被禁用,请联系管理员");
           }else if("1"==data){
        	   saveCookie();
        	   window.location.href="${ctx}/report/systemavailablerate/report_pre.do";
           }else{
        	   saveCookie();
        	   window.location.href="${ctx}/report/systemavailablerate/list.do";
           }
           $("#loginid").val("登录");
           $("#loginid").addClass("dpfont");
          }});
}
function saveCookie(){
    if($("#rememberme").prop("checked"))
    {
      var username = $("#username").val();
      var passwd=  $("#passwd").val();
      $.cookie('username', username, { expires: 7 }); 
      $.cookie('passwd', passwd, { expires: 7 }); 
    }else{
    	 $.cookie('username', ''); 
         $.cookie('passwd', ''); 
    }
}
</script>
</head>
<body>
<div id="login">
  <div class="login_top"></div>
  <div class="login_main">
    <div class="main_r">
    <p align="center" id="showerror"></p>
      <form action="${ctx}/user/login.do" method="POST">
        <div class="ubd">
          <div class="imge_user">用户名：  </div>
          <input id="username" name="username" />
          <div style="clear:both"></div>
        </div>
        <div class="ubd">
          <div class="imge_password">密&nbsp;&nbsp;&nbsp;码：  </div>
          <input id="passwd" name="passwd" type="password"/>
          <div style="clear:both"></div>
        </div>
        <p class="frm"> <input id="rememberme" name="rememberme" type="checkbox" checked="true" /><span>记住登录密码</span> </p>
        <div class="deng">
          <input id="loginid" name="loginid" type="button" value="登录" class="login" onclick="login()"/>
        </div>
      </form>
    </div>
  </div>
</div>
</body>
</html>
