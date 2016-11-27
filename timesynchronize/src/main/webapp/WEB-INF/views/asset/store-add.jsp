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
<script type="text/javascript" src="${ctx}/js/jQuery-jcDate.js" ></script>
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
$("#storeform").validate({
	rules: {
		uniqueVal: {
			required: true,
			minlength:1,
			remote: {
                url: "${ctx}/asset/store/checkname.do?id=${id}",  
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
		version: {
			required: true,
			minlength:1,
			maxlength:32
		},
		capacityInfo: {
			required: true,
			minlength:1,
			maxlength:32
		},
		diskSpec: {
			required: true,
			minlength:1,
			maxlength:32
		},
		microcodeVersion: {
			required: true,
			minlength:1,
			maxlength:32
		},
		storageRaidMode: {
			required: true,
			minlength:1,
			maxlength:32
		},
		storageTapeMediaType: {
			required: true,
			minlength:1,
			maxlength:32
		},
		serviceTime: {
			required: true,
		},
		type: {
			required: true,
			minlength:1,
			maxlength:32
		},
		storageArraySize: {
			required: true,
			digits:true,
			range:[1,100000]
		},
		storageCacheSize: {
			required: true,
			digits:true,
			range:[1,100000]
		},
		storageTapeMediaCount: {
			required: true,
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
			required: "请输入存储设备编号",
			minlength:"编号长度最少1位",
			maxlength:"编号长度最长32位",
			remote:"编号不能重复"
		},
		capacityInfo:"请输入容量信息，长度(1-32)" ,
		diskSpec:"请输入磁盘规格，长度(1-32)" ,
		serialNumber:"请输入设备序列号，长度(1-32)",
		location:"请输入存储设备位置，长度(1-32)" ,
		manufacturer:"请输入存储设备制造商，长度(1-32)" ,
		version:"请输入设备版本，长度(1-32)",
		purpose:"请输入存储设备用于，长度(1-32)" ,
		serviceTime:"请输入存储设备时间" ,
		type:"请输入存储设备型号，长度(1-32)" ,
		microcodeVersion:"请输入存储微码版本" ,
		storageRaidMode:"请输入存储RAID方式" ,
		storageTapeMediaType:"请输入存储带介质类型" ,
		storageArraySize:"请输入存储配置容量（GB），1-100000" ,
		storageCacheSize:"请输入存储CACHE容量（GB），1-100000" ,
		storageTapeMediaCount:"请输入存储带介质数量，1-100000",
		name:"请输入存储设备名称，长度(1-32)" 
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
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a>配置信息</a> > <a>存储设备</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">存储设备</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="storeform" role="form" 
            <c:choose> <c:when test="${id==0}">action="<%=request.getContextPath()%>/asset/store/add.do"</c:when>
            <c:otherwise>action="<%=request.getContextPath()%>/asset/store/update.do"</c:otherwise>
            </c:choose> method="post">
            <input type="hidden" name="id" value="${id}">
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">编号：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="uniqueVal" name="uniqueVal" <c:if test="${id!=0}"> readonly</c:if> value="${store.uniqueVal}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">序列号：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="serialNumber" name="serialNumber" value="${store.serialNumber}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备生产产商：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="manufacturer" name="manufacturer" value="${store.manufacturer}">                            
                </div>             
              </div>
             <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备名称：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="name" name="name" value="${store.name}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备类型：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="type" name="type" value="${store.type}">                            
                </div>
              </div>
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备版本：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="version" name="version" value="${store.version}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备用途：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="purpose" name="purpose" value="${store.purpose}">                            
                </div>
              </div>
                <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">开始服务时间：</label>
                <div class="col-xs-5">
                  <div id="submit_time" class="input-append date wid_60">
                    <input id="serviceTime" type="text" class="pick_timeshow" name="serviceTime" value="<fmt:formatDate value="${store.serviceTime}" pattern="yyyy-MM-dd"/>">
                    <span class="add-on"> <i data-time-icon="icon-time" data-date-icon="icon-calendar"></i> </span> </div>
                </div>
              </div>       
              
                <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">设备位置：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="location" name="location" value="${store.location}">                            
                </div>
              </div>
                     
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">容量信息：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="capacityInfo" name="capacityInfo" value="${store.capacityInfo}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">存储配置容量：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="storageArraySize" name="storageArraySize" value="${store.storageArraySize}">                            
                </div>
              </div>
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">存储CACHE容量：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="storageCacheSize" name="storageCacheSize" value="${store.storageCacheSize}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">存储RAID方式：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="storageRaidMode" name="storageRaidMode" value="${store.storageRaidMode}">                            
                </div>
              </div>
              
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">磁盘规格：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="diskSpec" name="diskSpec" value="${store.diskSpec}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">存储微码版本：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="microcodeVersion" name="microcodeVersion" value="${store.microcodeVersion}">                            
                </div>
              </div>
               <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">存储带介质类型：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="storageTapeMediaType" name="storageTapeMediaType" value="${store.storageTapeMediaType}">                            
                </div>
              </div>
              <div class="form-group">
                <label for="input" class="wid_200p control-label fl_left require">存储带介质数量：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="storageTapeMediaCount" name="storageTapeMediaCount" value="${store.storageTapeMediaCount}">                            
                </div>
              </div>
              <input id="apps" type="hidden" name="apps" value=""/>
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
   </div>
</body>
</html>