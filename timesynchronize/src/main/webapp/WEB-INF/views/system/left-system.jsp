<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<div class="left_menu" id="leftMDiv">
        <ul>
          <li class="con_0"></li>
          <c:set var="permisions" value="${permisions}" scope="session" /> 
		   <c:if test="${fn:contains(permisions, 'user:view')}"><li><a href="${ctx}/user/list.do">用户管理</a></li></c:if>
          <c:if test="${fn:contains(permisions, 'role:view')}"><li><a href="${ctx}/role/list.do">角色管理</a></li></c:if>
          <li><a href="${ctx}/reportadditional/list.do">补报配置</a></li>
          <c:if test="${fn:contains(permisions, 'system:riskcategory')}"><li><a href="${ctx}/riskcategory/list.do?riskCode=">监控指标配置</a></li></c:if>
          <c:if test="${fn:contains(permisions, 'org:view')}"><li><a href="${ctx}/organization/list.do">机构配置</a></li></c:if>
          <c:if test="${fn:contains(permisions, 'area:view')}"><li><a href="${ctx}/areacity/list.do">区域配置</a></li></c:if>
          <li><a href="${ctx}/operatelog/list.do">操作日志</a></li>
          <c:if test="${fn:contains(permisions, 'system:backupdb')}"><li><a href="${ctx}/backupdb/backup.do">数据备份</a></li></c:if> 
          <c:if test="${fn:contains(permisions, 'system:globalparameter')}"><li><a href="${ctx}/globalparameter/list.do">全局参数配置</a></li></c:if> 
          <c:if test="${fn:contains(permisions, 'system:warninginfo')}"><li><a href="${ctx}/warninginfo/list.do">风险预警</a></li></c:if> 
          <li><a href="${ctx}/template/list.do">监控指标模板下载</a></li>
          <c:if test="${fn:contains(permisions, 'message:manage')}"><li><a href="${ctx}/message/list.do">公告发布</a></li></c:if>
          <c:if test="${fn:contains(permisions, 'message:view')}"><li><a href="${ctx}/rmessage/list.do">我的公告</a></li></c:if>
          <li class="con_0"></li>
        </ul>
      </div>
      <div class="menu_c" id="menu_c"><a class="show_c"></a></div>