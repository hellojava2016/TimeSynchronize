<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
<title>商业银行</title>
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
<script type="text/javascript" src="${ctx}/js/bootstrap/moment-with-langs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/bootstrap-datetimepicker.min.js"></script>
<script src="${ctx}/js/select/jquery.cityselect.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js" ></script>
	
</head>
<body>
<div class="container"> 
  <!--header start --><!-- #BeginLibraryItem "/Library/top.lbi" -->
   <jsp:include page="../commons/header.jsp"></jsp:include>
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-system.jsp"></jsp:include> 
    
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span>&gt; <a>系统配置</a> &gt; <a>机构设置</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">机构设置</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="add" action="<%=request.getContextPath()%>/organization/add.do" method="post">
              <div class="form-group">
                <label   class="col-xs-4 control-label pr_0 require">机构区域：</label>        
                  <div class="col-xs-5">
                  <select name="area"  class="form-control" id="area">
                    <option value=''>---请选择区域---</option>
                      <c:forEach items="${areas}" var="areat">
                      <option value=${areat.areeCode}>${areat.name}</option>
		            </c:forEach>
		           </select>
                  </div>                
              </div>
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">机构类型：</label>
                <div class="col-xs-5">                 
                   <select name="category" id="category"   class="form-control">
                     <option value=''>---请选择组织机构---</option>
                   <option value="1">监管机构 </option>
                   <option value="2">政策性及国家开发银行</option>
                   <option value="3">大型商业银行</option>
                   <option value="4">股份制商业银行</option>
                   <option value="5">城市商业银行</option>
                   <option value="6">农村商业银行</option>
                   <option value="7">农村合作银行</option>
                   <option value="8">农村信用社</option>
                   <option value="9">邮政储蓄银行</option>
                   <option value="10">金融资产管理公司</option>
                   <option value="11">外资法人金融机构</option>
                   <option value="12">中德住房储蓄银行</option>
                   <option value="13">信托公司</option>
                   <option value="14">企业集团财务公司</option>
                   <option value="15">金融租赁公司</option>
                   <option value="16">货币经纪公司</option>
                   <option value="17">汽车金融公司</option>
                   <option value="18">消费金融公司</option>
                   <option value="19">村镇银行</option>
                   <option value="20">贷款公司</option>
                   <option value="21">农村资金互助社</option>
                   </select>             
                </div>
              </div>
              
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">机构名称：</label>
                <div class="col-xs-5">  
                  <input type="text" class="form-control" id="name"  name="name" > 
                </div>
              </div>
              
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">机构代码：</label>
                <div class="col-xs-5">  
                  <input type="text" class="form-control" id="orgNo"  name="orgNo" > 
                </div>
              </div>
              
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">机构地址：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="address" name="address" >
                </div>
              </div>
              
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">资产规模：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="moneyCount" name="moneyCount" >
                </div>
              </div>
              
              <div class="form-group" id="controldiv">
                <label for="input" class="col-xs-4 control-label pr_0 require">是否纳入指标监控：</label>
                <div class="col-xs-5 row">
                  <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="radio" name="canControl" id="optionsRadios1" value="false">否
                    </label> 
                 </div>
                 <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="radio" name="canControl" id="optionsRadios2" value="true" checked>是
                    </label> 
                  </div>
                </div>
              </div>
              
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0">联系人员：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="contactsName" name="contactsName" >                            
                </div>
              </div>

             <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0">联系邮箱：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="contactsMail" name="contactsMail" >                            
                </div>
              </div>
              
               <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0">联系电话：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="contactsCellphone" name="contactsCellphone" >                            
                </div>
              </div>

              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0">联系座机：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="contactsPhone" name="contactsPhone" >                            
                </div>
              </div>
                        
              <div class="form-group">
                <div class="col-xs-offset-5 col-xs-10">
                  <button type="submit" class="btn btn-primary mr_20" >确定</button>
                  <button type="button" class="btn btn-default mr_20" onclick="window.location.href=document.referrer;">取消</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
    <div class="clearfix"></div>
  </div><!-- #BeginLibraryItem "/Library/footer.lbi" -->
  <jsp:include page="../commons/footer.jsp"></jsp:include><!-- #EndLibraryItem --></div>
<!-- #BeginLibraryItem "/Library/script.lbi" -->
<script type="text/javascript">
var  r=[];
jQuery.validator.addMethod("isTel", function(value,element) {   
    var length = value.length;   
    var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;   
    var tel = /^(\d{3,4}-?)?\d{7,9}$/g;       
    return this.optional(element) || tel.test(value) || (length==11 && mobile.test(value));   
}, "请正确填写您的联系方式"); 
$(function(){
	$("form label.require").each(function(){
        $(this).append('<font color="red">*</font>'); //然后将它追加到文档中  
    });	
	$("#add").validate({
		rules: {
			name: {
				required: true,				
				rangelength: [1,30]
			},
			area:{
				required: true
			},
			category:{
				required: true
			},
			orgNo: {
				required: true,				
				rangelength: [1,30]
			},
			moneyCount: {
				required: true,	
				digits: true,	
				rangelength: [1,1000000000000000000]
			},
			address:{
				required: true,	
				rangelength: [0,30]
			},
			contactsName :{	
				rangelength: [0,20],
			},
			contactsMail:{
				email:true,
				rangelength: [0,30]				
			} ,
			contactsCellphone:{
				isTel:true		
			} ,
			contactsPhone:{	
				isTel:true				
			},
			prov:{
				required: true
			} 		
		},
		messages: {
			name: {
				required: "请输入机构名称",
				remote:"机构名已经存在",
				rangelength: "机构名长度为1-30"
				},
		    orgNo: {
					required: "请输入机构代码",
					rangelength: "机构代码长度为1-30"
					},
			area:"请选择区域",
			category:"请选择组织机构",		
			address:{
				required: "请输入地址",
				rangelength: "联系人地址长度为0-30"		
			},
			moneyCount:"请输入资产规模，范围1-1000000000000000000",
			contactsName :{
				required: "请输入联系人姓名",
				rangelength: "联系人名称长度为1-20"	
			},
			contactsMail:{
				required: "请输入联系人邮箱",
				email: "请输入合法的邮箱",
				rangelength: "邮箱长度为0-30"	
			} ,
			contactsCellphone:{
				required: "请输入联系人手机",
				isTel: "联系电话输入不合法"	
			} ,
			contactsPhone:{
				required: "请输入联系人电话",
				isTel: "联系电话输入不合法"	
			},
			prov: {
				required: "请选择区域"
				}
		}
	});
	var result = $("#category").val();
		if(result==1){
			$("#controldiv").hide();
		}else{
			$("#controldiv").show();
		}
	$("#category").change(function(){
		var result = $("#category").val();
		if(result==1){
			$("#controldiv").hide();
		}else{
			$("#controldiv").show();
		}
	});
});
</script>
</body>
</html>