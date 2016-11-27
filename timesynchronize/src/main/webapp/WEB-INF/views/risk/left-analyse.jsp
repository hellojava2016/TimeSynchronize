<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<div class="left_menu" id="leftMDiv">
        <ul>
          <li class="con_0"></li>
          <li><a href="${ctx}/report/systemavailablerate/report_pre.do">系统可用率(周报)</a></li>
          <li><a href="${ctx}/report/systemtransactionsuccessrate/report_pre.do">系统交易成功率(日报)</a></li>
          <li><a href="${ctx}/report/operationchangesuccessrate/report_pre.do">投产变更成功率(月报)</a></li>
          <li><a href="${ctx}/report/fakesiteattachmentrate/report_pre.do">假冒网站查封率(月报)</a></li>          
          <li><a href="${ctx}/report/outsideattackchangerate/report_pre.do">外部攻击变化率(周报)</a></li>         
          <li><a href="${ctx}/report/infotechnologyriskeventcount/report_pre.do">信息科技风险事件数量(周报)</a></li>          
          <li><a href="${ctx}/report/electronictransactionchangerate/report_pre.do">主要电子渠道交易变化率(日报)</a></li>
          <li><a href="${ctx}/report/electronicactiveuserchangerate/report_pre.do">主要电子渠道活跃用户变化率(月报)</a></li>
          <li class="con_0"></li>
          <!--<li><a href="${ctx}/report/report/report1.do">单机构单期指标同比环比监测</a></li> -->
          <li><a href="${ctx}/report/report/report2.do">单机构多期指标监测</a></li>
          <li><a href="${ctx}/report/report/report3_pre.do">多机构近期全部指标监测</a></li>
          <li><a href="${ctx}/report/report/report4_pre.do">多机构指标超阈值监测</a></li>
          <li class="con_0"></li>
        </ul>
      </div>
      <div class="menu_c" id="menu_c"><a class="show_c"></a></div>