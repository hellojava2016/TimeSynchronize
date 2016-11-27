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
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<script src="${ctx}/js/select/jquery.cityselect.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/moment-with-langs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js" ></script>
<script type="text/javascript" src="${ctx}/js/ztree/jstree.min.js" ></script>
<script type="text/javascript">
var  r=[];
$(function (){
	$("form label.require").each(function(){
        $(this).append('<font color="red">*</font>'); //然后将它追加到文档中  
    }); 
$("#sysavalableform").validate({
	rules: {
		uniqueVal: {
			required: true,
			minlength:1,
			remote: {
                url: "${ctx}/asset/networkequipmentinfo/checkname.do?id=${id}",  
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
		type: {
			required: true,
			minlength:1,
			maxlength:32
		},
		version: {
			required: true,
			minlength:1,
			maxlength:32
		},
		manufacturer: {
			required: true,
			minlength:1,
			maxlength:32
		},
		serialNumber: {
			required: true,
			minlength:1,
			maxlength:32
		},
		location: {
			required: true,
			minlength:1,
			maxlength:32
		},
		purpose: {
			required: true,
			minlength:1,
			maxlength:32
		},
		portCount: {
			required: true ,
			digits:true,
			range:[1,100000]
		},
		name: {
			required: true,
			minlength:1,
			maxlength:32
		 } 
	},
	messages: {
		uniqueVal: {
			required: "请输入编号",
			minlength:"编号长度最少1位",
			maxlength:"编号长度最长20位",
			remote:"编号不能重复"
		},
		type:"请输入网络设备类型，长度(1-32)" ,
		version:"请输入设备版本，长度(1-32)" ,
		serialNumber:"请输入设备序列号，长度(1-32)" ,
		manufacturer:"请输入设备生产厂商，长度(1-32)" ,
		purpose:"请输入设备用途，长度(1-32)" ,
		location:"请输入设备 位置，长度(1-32)" ,
		portCount:"请输入网络设备端口数量，1-100000" ,
		name:"请输入网络设备名称，长度(1-32)" 
	} 
});

$('#jstree2').jstree({'plugins':["wholerow","checkbox"], 'core' : {
	'data' :${apps}
}});

$('#jstree2').on('changed.jstree', function (e, data) {
    var i, j;
    r=[];
    for(i = 0, j = data.selected.length; i < j; i++) {
    	var tid=data.instance.get_node(data.selected[i]).id;
		if(10000!=tid)
             r.push(tid);
    }
    var apps=getSelectedPermisions();
    $("#apps").val(apps);
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
      <jsp:include page="left-asset.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>配置信息</a> > <a>网络设备</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">网络设备</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="sysavalableform" role="form"
            <c:choose> <c:when test="${id==0}">action="<%=request.getContextPath()%>/asset/networkequipmentinfo/add.do"</c:when>
            <c:otherwise>action="<%=request.getContextPath()%>/asset/networkequipmentinfo/update.do"</c:otherwise>
            </c:choose> method="post">
            <input type="hidden" name="id" value="${id}">
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">编号：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="uniqueVal" name="uniqueVal" <c:if test="${id!=0}"> readonly</c:if> value="${network.uniqueVal}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">序列号：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="serialNumber" name="serialNumber" value="${network.serialNumber}">                            
                </div>
              </div>
              <input id="apps" type="hidden" name="apps" value=""/>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备生产厂商：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="manufacturer" name="manufacturer" value="${network.manufacturer}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备类型：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="type" name="type" value="${network.type}">                            
                </div>
              </div>              
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备名称：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="name" name="name" value="${network.name}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备版本：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="version" name="version" value="${network.version}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备用途：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="purpose" name="purpose" value="${network.purpose}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备位置：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="location" name="location" value="${network.location}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">端口数量：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="portCount" name="portCount" value="${network.portCount}">                            
                </div>
              </div>
              
                <div class="form-group">
                <label  class="wid_200p control-label fl_left">支撑的应用系统：</label>
                <div class="col-xs-5">
                    <div class="showline">
                     <div id="jstree2"></div>
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
  <!-- #EndLibraryItem --> </div>
</body>
</html>