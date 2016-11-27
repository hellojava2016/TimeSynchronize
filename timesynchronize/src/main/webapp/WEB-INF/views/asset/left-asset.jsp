<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<div class="left_menu" id="leftMDiv">
        <ul>
          <li class="con_0"></li>
          <li><a href="${ctx}/asset/host/list.do">主机</a></li>
          <li><a href="${ctx}/asset/os/list.do">操作系统</a></li>
          <li><a href="${ctx}/asset/db/list.do">数据库</a></li>
          <li><a href="${ctx}/asset/middlewareinfo/list.do">中间件</a></li>
          <li><a href="${ctx}/asset/store/list.do">存储设备</a></li>
          <li><a href="${ctx}/asset/networkequipmentinfo/list.do">网络设备</a></li>
          <li><a href="${ctx}/asset/computerroominfo/list.do">机房</a></li>
          <li><a href="${ctx}/asset/upsinfo/list.do">机房UPS</a></li>
          <li><a href="${ctx}/asset/precisionacinfo/list.do">精密空调</a></li>
          <li><a href="${ctx}/asset/appsystem/list.do">应用系统</a></li>
          <li class="con_0"></li>
        </ul>
      </div>
      <div class="menu_c" id="menu_c"><a class="show_c"></a></div>