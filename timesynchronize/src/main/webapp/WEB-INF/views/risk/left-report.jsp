<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<div class="left_menu" id="leftMDiv">
        <ul>
          <li class="con_0"></li>
          <li><a href="${ctx}/report/systemavailablerate/list.do">系统可用率(周报)</a></li>
          <li><a href="${ctx}/report/systemtransactionsuccessrate/list.do">系统交易成功率(日报)</a></li>
           <li><a href="${ctx}/report/operationchangesuccessrate/list.do">投产变更成功率(月报)</a></li>
          <li><a href="${ctx}/report/fakesiteattachmentrate/list.do">假冒网站查封率(月报)</a></li>         
          <li><a href="${ctx}/report/outsideattackchangerate/list.do">外部攻击变化率(周报)</a></li>          
          <li><a href="${ctx}/report/infotechnologyriskeventcount/list.do">信息科技风险事件数量(周报)</a></li>
          <li><a href="${ctx}/report/electronictransactionchangerate/list.do">主要电子渠道交易变化率(日报)</a></li>
          <li><a href="${ctx}/report/electronicactiveuserchangerate/list.do">主要电子渠道活跃用户变化率(月报)</a></li>          
          <li class="con_0"></li>
        </ul>
      </div>
      <div class="menu_c" id="menu_c"><a class="show_c"></a></div>