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
	Date.prototype.format = function(format){ 
		var o = { 
		"M+" : this.getMonth()+1, //month 
		"d+" : this.getDate(), //day 
		"h+" : this.getHours(), //hour 
		"m+" : this.getMinutes(), //minute 
		"s+" : this.getSeconds(), //second 
		"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
		"S" : this.getMilliseconds() //millisecond 
		} 

		if(/(y+)/.test(format)) { 
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
		} 

		for(var k in o) { 
		if(new RegExp("("+ k +")").test(format)) { 
		format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		} 
		} 
		return format; 
	};
	$("form label.require").each(function(){
        $(this).append('<font color="red">*</font>'); //然后将它追加到文档中  
    });
	
	$.validator.addMethod("dayucurrent",function(value,element){
	    var reg = new RegExp('-','g');
	    var currentdate= new Date().format("yyyy-MM-dd");
	    var assigntime = value.replace(reg,'/'); 
	    var deadlinetime = currentdate.replace(reg,'/');
	    assigntime = new Date(parseInt(Date.parse(assigntime),10));
	    deadlinetime = new Date(parseInt(Date.parse(deadlinetime),10));
	    if(assigntime<deadlinetime){
	        return false;
	    }else{
	        return true;
	    }
	},"<font color='#E47068'>开始日期必须大于等于当前日期</font>");
	
	$.validator.addMethod("compareDate",function(value,element){
	    var assigntime = $("#starttime").val();
	    var deadlinetime = $("#endtime").val();
	    var reg = new RegExp('-','g');
	    assigntime = assigntime.replace(reg,'/');//正则替换
	    deadlinetime = deadlinetime.replace(reg,'/');
	    assigntime = new Date(parseInt(Date.parse(assigntime),10));
	    deadlinetime = new Date(parseInt(Date.parse(deadlinetime),10));
	    if(assigntime>deadlinetime){
	        return false;
	    }else{
	        return true;
	    }
	},"<font color='#E47068'>结束日期必须大于等于开始日期</font>");
	$('.input-append').datetimepicker({
        format: 'YYYY-MM-DD',
        language: 'zh-CN',
        pickDate: true,
        pickTime: false,
        inputMask: false
      });	
	$("#reportform").validate({
			rules: {
				reportindex:{
					required: true ,
					digits:true
				}  ,
				reportdate : {
					required : true,
					date:true,
				},
				starttime: {
					required: true,
					dayucurrent:true,
					date:true
				},
				endtime: {
					required: true,
					compareDate:true,
					date:true
				},
				reportreason: {
					required: true,
					rangelength:[1,255]  
				}
			},
			messages: {
				reportindex:"请输入合法的期数",
				reportdate: {required:"请输入补报指标时间"},
				starttime: {required:"请输入补报开始时间",dayucurrent:"开始时间必须大于等于当前时间"},
				endtime: {required:"请输入补报结束时间",compareDate:"结束时间必须大于开始时间"},
				reportreason: "请输入补报原因,补报原因在1-255字以内"
			}
		});
$('#jstree2').jstree({'plugins':["wholerow","checkbox"], 'core' : {
	'data' :${orgs}
}});

$('#jstree2').on('changed.jstree', function (e, data) {
    var i, j;
    r=[];
    for(i = 0, j = data.selected.length; i < j; i++) {
    	var tid=data.instance.get_node(data.selected[i]).id;
    		if(10000!=tid)
                 r.push(tid);
    }
    var orgs=getSelectedPermisions();
    $("#orgs").val(orgs);
  }).jstree(); 
  
$('#jstree3').jstree({'plugins':["wholerow","checkbox"], 'core' : {
	'data' :${rcs}
}});

$('#jstree3').on('changed.jstree', function (e, data) {
    var i, j;
    r=[];
    for(i = 0, j = data.selected.length; i < j; i++) {
    	var tid=data.instance.get_node(data.selected[i]).id;
    		if(10000!=tid)
                 r.push(tid);
    }
    var rcs=getSelectedPermisions();
    $("#rcs").val(rcs);
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
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>系统配置</a> > <a>补报配置</a> > <a>添加补报</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">补报设置</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="reportform" role="form" action="<%=request.getContextPath()%>/reportadditional/add.do?type=cbrc&pageNo=${pageNo}" method="post">
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">风险指标：</label>                
                <div class="col-xs-5">
                    <div class="showline">
                     <div id="jstree3"></div>
                   </div>
                </div>                
              </div>
              <input id="orgs" type="hidden" name="orgs" value=""/>
              <input id="rcs" type="hidden" name="rcs" value=""/>
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">金融机构：</label>
                 <div class="col-xs-5">
                    <div class="showline">
                     <div id="jstree2"></div>
                   </div>
                </div>
              </div>              
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">补报指标日期：</label>
                <div class="col-xs-4">
                  <div id="submit_time" class="input-append date wid_60">
                    <input type="text" class="pick_timeshow" name="reportdate">
                    </input>
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">补报开始时间：</label>
                <div class="col-xs-5">
                  <div id="submit_time" class="input-append date wid_60">
                    <input type="text" class="pick_timeshow" name="starttime" id="starttime">
                    </input>
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">补报结束时间：</label>
                <div class="col-xs-5">
                  <div id="submit_time" class="input-append date wid_60">
                    <input type="text" class="pick_timeshow" name="endtime" id="endtime">
                    </input>
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">补报原因：</label>
                <div class="col-xs-5">
                  <textarea class="textarea" cols="40" rows="7" name="reportreason"></textarea>
                </div>
              </div>
              <div class="form-group">
                <div class="col-xs-offset-5 col-xs-10">
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
  <!-- #BeginLibraryItem "/Library/footer.lbi" -->
  <jsp:include page="../commons/footer.jsp"></jsp:include>
</body>
</html>