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
<style>
.dpfont{
font-size: 15px;
}
</style>
<div class="container"> 
  <jsp:include page="../commons/header.jsp" />
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="left-system.jsp" />
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> &gt;<a>系统管理</a> &gt; <a>监控指标模板下载</a></div>
      <div class="row show mb_10">
        <div class="span12">
          <h3 class="show_tit">监控指标模板下载</h3>
          <div class="show_conment">
            <form class="form-horizontal" id="userform" role="form" action="" method="post">
                            
              <div class="form-group">
                <div class="col-xs-12 alert alert-info dpfont">
                  <strong><a href="${ctx}/files/SystemAvailableRate.xls">系统可用率导入模板</a></strong>
                </div>
                <div class="col-xs-12 alert alert-info dpfont">
                  <strong><a href="${ctx}/files/FakeSiteAttachmentRate.xls">假冒网站查封率导入模板</a></strong>
                </div>
                 <div class="col-xs-12 alert alert-info dpfont">
                 <strong><a href="${ctx}/files/OperationChangeSuccessRate.xls">投产变更成功率导入模板</a></strong>
                </div>
                <div class="col-xs-12 alert alert-info dpfont">
                   <strong><a href="${ctx}/files/OutsideAttackChangeRate.xls">外部攻击变化率导入模板</a></strong>
                </div>
                <div class="col-xs-12 alert alert-info dpfont">
                <strong><a href="${ctx}/files/SystemTransactionSuccessRate.xls">系统交易成功率导入模板</a></strong>
                </div>
                <div class="col-xs-12 alert alert-info dpfont">
                  <strong><a href="${ctx}/files/InfoTechnologyRiskEventCount.xls">信息科技风险事件数量导入模板</a></strong>
                </div>
                <div class="col-xs-12 alert alert-info dpfont">
                  <strong><a href="${ctx}/files/ElectronicActiveUserChangeRate.xls">主要电子渠道活跃用户账户变化率导入模板</a></strong>
                </div>
                <div class="col-xs-12 alert alert-info dpfont">
                 <strong><a href="${ctx}/files/ElectronicTransactionChangeRate.xls">主要电子渠道交易变化率导入模板</a></strong>
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
</body>
</html>