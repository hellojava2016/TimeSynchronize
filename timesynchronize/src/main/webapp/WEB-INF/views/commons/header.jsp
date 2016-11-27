<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="../commons/bankajax.jsp" %>
<script src="${ctx}/js/bootstrap/jquery.bootstrap.min.js" type="text/javascript" ></script>
<style>
#gundongdiv{
float: left;padding:30px 20px 10px 0;
color:#1E90FF;
font-weight:bold;
font-size: 22px;
}
</style>
<div class="header navbar navbar-fixed-top" id="header">
<c:set var="permisions" value="${permisions}" scope="session" /> 
    <div class="logo"></div>
    <div class="sl_menu">
      <ul>
        <li class="menu_li1"><a href="${ctx}/report/systemavailablerate/list.do">数据采集</a></li>        
        <c:if test="${fn:contains(permisions, 'risk:analyse')}"><li class="menu_li2"><a href="${ctx}/report/systemavailablerate/report_pre.do">风险分析</a></li></c:if>
        <li class="menu_li5"><a href="${ctx}/user/list.do">系统配置</a>        
        </li>
      </ul>
    </div>    
      <div id="gundongdiv"><img src="${ctx}/images/mail_3.png" rel="tooltip" data-placement="bottom" width="40px" height="30px" onclick="redirectmessage();" title="您有新的消息" style="margin-top: -17px;cursor: pointer;"/><span id="messagebody"></span></div> 
    <div id="out"><span>welcome, ${user.username }</span><a title="注销" rel="tooltip" data-placement="left" id="logoutid" onclick="logout()"></a></div>
  </div>
  <script type="text/javascript">
var message = '${message}';
$(function(){
	$("#header").css({"width":window.screen.width});
	var cH = $("body").height();
	var dH = $(document).height();
	var rH = $("#right").height();
	var wH = $(window).height();

	if(cH < dH){
		$("#menu_c").css("height",wH -110);
		$(".show").css("min-height",wH -180);
	}else{
		$("#menu_c").css("height",rH + 60);
		$("#right").css("marginBottom",50);
	}
	$("#menu_c").click(function(){
		if($("#leftMDiv").is(":hidden")){
			$("#leftMDiv").show();
			$("#left").removeClass("wid_8p")
			$("#right").removeClass("wid_99")
			$(".show_c").removeClass("hidden_c");
		}else{
			$("#leftMDiv").hide();
			$("#left").addClass("wid_8p");
			$("#right").addClass("wid_99");
			$(".show_c").addClass("hidden_c");
		}
	});
	$(".left_menu ul li a").click(function(){
		$(this).next(".li_close").toggleClass("li_open");
		$(this).parent().siblings().children(".shu_ul").css("display","none");
		$(this).parent().toggleClass("active");
		$(this).parent().siblings().removeClass("active");
		$(this).parent().siblings().children(".li_close").removeClass("li_open");
		$(this).parent().children(".shu_ul").toggle(200);
	});
	
	$.messager.model = {
			ok : {
				text : "确定",
				classed : 'btn-primary'
			},
			cancel : {
				text : "取消",
				classed : 'btn-error'
			}
		};
	
	$("a[rel=tooltip]").tooltip();
	$("img[rel=tooltip]").tooltip();
	if(""==message || 'null'==message){
		
	}else{
		  $.messager.alert("系统提示",message);
	}
	try{
		hasMewMessage();
	}catch(e){}
	$("#menu_c").css({"min-height":document.body.scrollHeight-80});
	
});

function redirectmessage(){
	window.location.href="${ctx}/rmessage/list.do";
}
function hasMewMessage(){
	
	$.ajax({ url: "${ctx}/rmessage/mewmessage.do",  success: function(msg){
		if(""==msg){
			 $("#gundongdiv").hide();
			 $("#messagebody").empty();
		 }else{
			 $("#gundongdiv").show();
			// $("#messagebody").empty().html("<font size='1' color='red'>新消息</font>");
		 }

      }});
}

function logout(){ 
	$.messager.model = { 
	        ok:{ text: "确定", classed: 'btn-success' },
	        cancel: { text: "取消", classed: 'btn-primary' }
	      };
	$.messager.confirm("系统提示", "您确定注销用户？", function() { 
		window.location.href="${ctx}/relogin.do";
// 	    	    ajaxOptions.url = "${ctx}/user/logout.do";
// 	    	    ajaxOptions.success = function(msg){
// 	    			window.location.href="${ctx}/relogin.do";
// 	    	    };
// 	    		simpleAjax(ajaxOptions);
 });
}
</script>