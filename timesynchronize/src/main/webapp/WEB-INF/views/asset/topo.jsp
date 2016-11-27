<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page language="java" import="java.net.*" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Topology Management</title>
	<link rel="stylesheet" type="text/css" href="${ctx}/topo/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/topo/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/topo/css/demo.css">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <link rel="stylesheet" type="text/css" href="${ctx}/css/ie_8.css"/>
<![endif]-->
	<script type="text/javascript" src="${ctx}/topo/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/topo/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/topo/js/json2.js"></script>
	<script type="text/javascript" src="${ctx}/topo/js/uuid.js"></script>
	<script type="text/javascript" src="${ctx}/topo/js/kinetic-v3.10.4.js"></script>
	<script type="text/javascript" src="${ctx}/topo/js/kinetic_topology.js"></script>
	
	<script>
	 	 var jsonStr =  '${jsonStr}';
	 //	var jsonStr = '{"devices":[{"id":"0001","name":"dongpeng","devType":"OS","src":"../../topo/image/server.png","x":800,"y":100},{"id":"0002","name":"pengdong","devType":"HOST","src":"../../topo/image/PC.png","x":800,"y":100}],"lines":[{"srcDeviceId":"0001","dstDeviceId":"0002","stroke":"black","strokeWidth":3}]}';
		function saveJson()
    	{
    		jsonStr=topology.toJson();
    	}
		
    	function loadJson(){
    		topology.load(jsonStr);
    	}

		$(function(){
    		topology = new Kinetic.Topology({
	            container: "container",
			    width: $("#mainArea").width() - 5,
			    height: $("#mainArea").height() - 5,
			    backgroundImage:"${ctx}/topo/image/background.png",
			    popmenu:{
			     	container:'mm',
			     	data:[
				    	{id:'menu_9',name:'删除',onclick:function(evt,instance){topology.deleteCurrentObject();},filter:'desktop',iconCls:'icon-open'},
				    	{id:'menu_11',name:'刷新',onclick:function(evt,instance){topology.refresh();},filter:'desktop',iconCls:'icon-blank'}
				    ]
			    }

          });
          loadJson();
    	});
	</script>
</head>
<body class="easyui-layout">
	<div id="mainArea" region="center" title="">
		<div id = "container"></div>
		<div id = "mm" style="width:120px;"></div>
	</div>
</body>
</html>