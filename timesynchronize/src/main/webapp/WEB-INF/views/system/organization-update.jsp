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
            <form class="form-horizontal" id="add" action="<%=request.getContextPath()%>/organization/update.do" method="post">
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">区域：</label>
                <div class="col-xs-5">  
                  <input type="text" class="form-control" id="areacity"  name="areacity" value="${organization.areaCity.name}" readonly > 
                </div>                
              </div>
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">机构类型：</label>
                <div class="col-xs-5">                 
                   <select name="category" id="category"  class="form-control">
                     <c:if test="${organization.category==1}">
                       <option value=1 <c:if test="${organization.category==1}">selected</c:if>>监管机构 </option>
                     </c:if>
                     <c:if test="${organization.category!=1}">
                       <option value="2" <c:if test="${category==2}"> selected</c:if>>政策性及国家开发银行</option>
                   <option value="3" <c:if test="${organization.category==3}"> selected</c:if>>大型商业银行</option>
                   <option value="4" <c:if test="${organization.category==4}"> selected</c:if>>股份制商业银行</option>
                   <option value="5" <c:if test="${organization.category==5}"> selected</c:if>>城市商业银行</option>
                   <option value="6" <c:if test="${organization.category==6}"> selected</c:if>>农村商业银行</option>
                   <option value="7" <c:if test="${organization.category==7}"> selected</c:if>>农村合作银行</option>
                   <option value="8" <c:if test="${organization.category==8}"> selected</c:if>>农村信用社</option>
                   <option value="9" <c:if test="${organization.category==9}"> selected</c:if>>邮政储蓄银行</option>
                   <option value="10" <c:if test="${organization.category==10}"> selected</c:if>>金融资产管理公司</option>
                   <option value="11" <c:if test="${organization.category==11}"> selected</c:if>>外资法人金融机构</option>
                   <option value="12" <c:if test="${organization.category==12}"> selected</c:if>>中德住房储蓄银行</option>
                   <option value="13" <c:if test="${organization.category==13}"> selected</c:if>>信托公司</option>
                   <option value="14" <c:if test="${organization.category==14}"> selected</c:if>>企业集团财务公司</option>
                   <option value="15" <c:if test="${organization.category==15}"> selected</c:if>>金融租赁公司</option>
                   <option value="16" <c:if test="${organization.category==16}"> selected</c:if>>货币经纪公司</option>
                   <option value="17" <c:if test="${organization.category==17}"> selected</c:if>>汽车金融公司</option>
                   <option value="18" <c:if test="${organization.category==18}"> selected</c:if>>消费金融公司</option>
                   <option value="19" <c:if test="${organization.category==19}"> selected</c:if>>村镇银行</option>
                   <option value="20" <c:if test="${organization.category==20}"> selected</c:if>>贷款公司</option>
                   <option value="21" <c:if test="${organization.category==21}"> selected</c:if>>农村资金互助社</option>
                     </c:if>
                   </select>             
                </div>
              </div>
              
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">机构名称：</label>
                <div class="col-xs-5">  
                  <input type="text" class="form-control" id="name"  name="name" value="${organization.name}" > 
                </div>
              </div>
              
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">机构代码：</label>
                <div class="col-xs-5">  
                  <input type="text" class="form-control" id="orgNo"  name="orgNo" value="${organization.orgNo}" > 
                </div>
              </div>
              
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">机构地址：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="address" name="address" value="${organization.address}" >
                </div>
              </div>
              
               <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0 require">资产规模：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="moneyCount" name="moneyCount" value="${organization.moneyCount}" >
                </div>
              </div>
              <c:if test="${organization.category!=1}">
              <div class="form-group"  id="controldiv">
                <label for="input" class="col-xs-4 control-label pr_0 require">是否纳入指标监控：</label>
                <div class="col-xs-5 row">
                  <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="radio" name="canControl" id="optionsRadios1" value="false" <c:choose><c:when test="${organization.canControl}"></c:when><c:otherwise>checked</c:otherwise></c:choose>>否
                    </label> 
                 </div>
                 <div class="col-xs-2 radio pr_0 pl_0">
                    <label>
                     <input type="radio" name="canControl" id="optionsRadios2" value="true" <c:if test="${organization.canControl}">checked</c:if>>是
                    </label> 
                  </div>
                </div>
              </div>
              </c:if>
              
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0">联系人员：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="contactsName" name="contactsName" value="${organization.contactsName}" >                            
                </div>
              </div>

             <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0">联系邮箱：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="contactsMail" name="contactsMail" value="${organization.contactsMail}" >                            
                </div>
              </div>
              
               <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0">联系电话：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="contactsCellphone" name="contactsCellphone" value="${organization.contactsCellphone}" >                            
                </div>
              </div>

              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0">联系座机：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="contactsPhone" name="contactsPhone" value="${organization.contactsPhone}" >                            
                </div>
              </div>
              
              <div class="form-group">
                <label for="input" class="col-xs-4 control-label pr_0">机构Key：</label>
                <div class="col-xs-5">           
                  <input type="text" class="form-control" id="secretKey" readonly="true" name="secretKey" value="${organization.secretKey}" >                            
                </div>
              </div>
              
                 <input type="hidden" name="orgId" value="${organization.orgId}">       
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
jQuery.validator.addMethod("isTel", function(value,element) {   
    var length = value.length;   
    var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;   
    var tel = /^(\d{3,4}-?)?\d{7,9}$/g;       
    return this.optional(element) || tel.test(value) || (length==11 && mobile.test(value));   
}, "请正确填写您的联系方式"); 
var  r=[];
$(function(){	
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
				rangelength: [1,9223372036854775807]
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
			moneyCount:"请输入资产规模",
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
});
});
</script>
</body>
</html>