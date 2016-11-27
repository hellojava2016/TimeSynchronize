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
<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/moment-with-langs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/bootstrap/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js" ></script>
<script type="text/javascript" src="${ctx}/js/ztree/jstree.min.js" ></script>
 <jsp:include page="../commons/bankajax.jsp"></jsp:include> 
<STYLE type="text/css">
input.error { border: 1px dotted red; }   
label.error {   
    color: red;display: none;   

}  
.showline { 
 border-style:  solid ;
 border-color:black;
border-width: 1px;
}

</STYLE>
</head>
<body>
<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
</c:if>
<div class="container"> 
  <jsp:include page="../commons/header.jsp"></jsp:include>
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-system.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> &gt; <a>系统管理</a> &gt; <a>查看公告</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">查看公告</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="roleform" role="form" action="${ctx}/message/add.do"  method="post">   
            
            <div class="form-group">
                <label for="description" class="col-xs-4 control-label pr_0">发布机构：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="username" name="username" value="${bmr.message.sendUser.name}" readonly>
                </div>
              </div> 
              
              <div class="form-group">
                <label for="description" class="col-xs-4 control-label pr_0">紧急重要度：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="criticalStr" name="criticalStr" value="${bmr.message.criticalStr}" readonly>
                </div>
              </div> 
                         
                <div class="form-group">
                <label for="roleName" class="col-xs-4 control-label pr_0">标题：</label>
                <div class="col-xs-5">
                  <input type="text" class="form-control" id="title" name="title" value="${bmr.message.title}" readonly>
                </div>
              </div>
                 
              
              <div class="form-group">
                <label for="description" class="col-xs-4 control-label pr_0">内容：</label>
                <div class="col-xs-5">
                 <textarea class="form-control" id="dmessage" name="dmessage" readonly  rows="10">${bmr.message.message}</textarea>
                </div>
              </div>              
            
                <div class="form-group">
                <label   class="col-xs-4 control-label pr_0"></label>
                <div class="col-xs-5">
                        <button type="button" class="btn btn-default mr_20" onclick="window.location.href='${ctx}/rmessage/list.do';">返回</button>
                </div>
              </div> 
            </form>
          </div>
        </div>
      </div>
    </div>
    <div class="clearfix"></div>
  </div>
</div>
<jsp:include page="../commons/footer.jsp"></jsp:include>
 
</body>
</html>