<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title>商业银行</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- Bootstrap -->
<link href="${ctx}/css/bootstrap/bootstrap.min.css" rel="stylesheet" media="screen">
<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/list.css"/>
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <link rel="stylesheet" type="text/css" href="${ctx}/css/ie_8.css"/>
<![endif]-->
<script type="text/javascript" src="${ctx}/js/jquery-1.11.1.min.js"></script>
<script src="${ctx}/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
<script src="${ctx}/js/twaver/twaver.js" type="text/javascript" ></script>
<script src="${ctx}/js/twaver/demo.js" type="text/javascript" ></script>
<script src="${ctx}/js/jquery.url-0.0.1.js" type="text/javascript" ></script>


</head>
<body>
<div class="container"> 
  <jsp:include page="../commons/header.jsp"></jsp:include>
  <div id="content" class="row mt_78">
    <div class="col-md-2" id="left"> 
      <jsp:include page="../system/left-system.jsp"></jsp:include> 
    </div>
    <div class="col-md-10" id="right">
      <div class="breadcrumb"><span>您当前所在的位置：</span> > <a href="${ctx}/topo/topo.do">TOPO管理</a> > <a href="${ctx}/chassis/chassisdemo.do">设备面板图</a></div>

    </div>
  </div>
  <div class="show_conment pad_5">
    <div id="chassis">
	
	</div>
	<div id="msgcount">
	</div>
	</div>
</div>
<jsp:include page="../commons/footer.jsp"></jsp:include>
<script type="text/javascript">
$(function (){
	var chassisDemo = new ChassisDemo();
	chassisDemo.init();

	var websocket = new WebSocket("ws://localhost:8080/timesynchronize/sendMessage?deviceName='ne=504103:/'");  
    
    websocket.onopen = function (evnt) {  
    };  
    websocket.onmessage = function (evnt) {
    	alert("接收告警:"+evnt.data);
        //$("#msgcount").html("(<font color='red'>"+evnt.data+"</font>)")  
        var alarm = eval('(' + evnt.data + ')'); 
        chassisDemo.onAlarm(alarm.sourceId,alarm.sourceId,alarm.alarmSeverity);
    };  
    websocket.onerror = function (evnt) {  
    };  
    websocket.onclose = function (evnt) {  
    } 

	
});	

ChassisDemo = function () {
	this.box = new twaver.ElementBox();
    this.network = new twaver.vector.Network(this.box);
    this.ports = new twaver.List();
    this.cards = new twaver.List();
};
twaver.Util.ext('ChassisDemo', Object, {
    init: function () {
        this.registImages();
        var toolbar = demo.Util.createNetworkToolbar(this.network);
        var borderBane = new twaver.controls.BorderPane(this.network,toolbar);
        var main = document.getElementById('chassis');

        demo.Util.appendChild(borderBane.getView(), main, 125, 0, 0, 285);

        demo.Util.addButton(toolbar, "Reload", 'refresh', function () { self.initBox(); });
        var movable = demo.Util.addCheckBox(toolbar, false, "Movable");

        this.initBox();
        this.network.setElementBox(this.box);

        this.network.setMovableFunction(function (element) {
            return movable.checked;
        });

        this.network.setVisibleFunction(function (element) {
            if (element.getClient("hidden")) {
                return false;
            }
            if (element.getParent() && element.getParent().getClient("hidden")) {
                return false;
            }
            return true;
        });
        var self = this;
       
    },
    onAlarm: function (alarmID, elementID, severityVal) {
    	
    	var aid = alarmID+demo.Util.randomInt(100);
        var alarm2 = new twaver.Alarm(aid, elementID);
        var severities = twaver.AlarmSeverity.severities;
        var severity=severities.get(severityVal);
        alarm2.setAlarmSeverity(severity);
        alarm2.setClient('raisedTime', new Date());
        var alarmBox = this.box.getAlarmBox();
        alarmBox.add(alarm2);
    },
    registImages: function () {
        this.registerImage("${ctx}/images/chassis/bolt.png");
        for (var i = 1; i <= 10; i++) {
            this.registerImage("${ctx}/images/chassis/chassis" + i + ".png");
        }
    },
    registerImage: function (url) {
        demo.Util.registerImage(url, this.network);
    },  
    tick: function () {
        for (var i = 0; i < this.ports.size(); i++) {
            this.ports.get(i).getAlarmState().clear();
        }
        for (i = 0; i < this.cards.size(); i++) {
            this.cards.get(i).setClient("hidden", false);
        }
        var node = this.ports.get(demo.Util.randomInt(this.ports.size()));
        node.getAlarmState().increaseNewAlarm(demo.Util.randomNonClearedSeverity());
        node = this.ports.get(demo.Util.randomInt(this.ports.size()));
        node.getAlarmState().increaseNewAlarm(demo.Util.randomNonClearedSeverity());

        node = this.cards.get(demo.Util.randomInt(this.cards.size()));
        node.setClient("hidden", true);

        if (this.light1.getStyle('vector.fill.color') == '#00FF00') {
            this.light1.setStyle('vector.fill.color', '#FFFF00');
        } else {
            this.light1.setStyle('vector.fill.color', '#00FF00');
        }

        if (this.light2.getStyle('vector.fill.color') == '#0000FF') {
            this.light2.setStyle('vector.fill.color', '#FF0000');
        } else {
            this.light2.setStyle('vector.fill.color', '#0000FF');
        }
    },
    initBox: function () {
        this.box.clear();      
        var first = new twaver.Follower("ne=504103:/");
        first.setStyle('outer.padding', 2);
        first.setStyle('select.color', '#000000');      
        first.setParent(null);
       
        first.setLocation(16, 11);
        first.setImage("chassis9");
        first.setStyle('outer.padding', 1);
        this.box.add(first);


        var cardContainer = this.createNode(first, 44, 27, null, 638, 269);
        cardContainer.setStyle('vector.fill.color', '#AAAAAA');
        cardContainer.setStyle('outer.padding', -1);
        cardContainer.setStyle('vector.deep', -4);

        this.cards.clear();
        for (var i = 0; i <= 12; i++) {
            this.cards.add(this.createCard(cardContainer, i));
        }

        card = this.createNode(cardContainer, 560, 27, null, 38, 268);
        this.createNode(card, 565, 223, "chassis4");
        this.createNode(card, 565, 251, "chassis4");
        card = this.createNode(card, 565, 46, null, 24, 163);
        card.setStyle('vector.fill.color', '#868686');
        this.createNode(card, 568, 52, "chassis3").setStyle('alarm.direction', 'left');
        this.createNode(card, 568, 166, "chassis3").setStyle('alarm.direction', 'left');
        this.createNode(card, 568, 147, "chassis3").setStyle('alarm.direction', 'left');
        this.createNode(card, 568, 128, "chassis3").setStyle('alarm.direction', 'left');
        this.createNode(card, 568, 109, "chassis3").setStyle('alarm.direction', 'left');
        this.createNode(card, 568, 90, "chassis3").setStyle('alarm.direction', 'left');
        this.createNode(card, 568, 71, "chassis3").setStyle('alarm.direction', 'left');
        this.createNode(card, 568, 185, "chassis3").setStyle('alarm.direction', 'left');

        card = this.createNode(cardContainer, 598, 27, null, 43, 268);
        this.light1 = this.createLight(card, 610, 225, '#00FF00');
        this.light2 = this.createLight(card, 610, 253, '#0000FF');
        card = this.createNode(card, 603, 46, null, 24, 163);
        card.setStyle('vector.fill.color', '#868686');
        this.createNode(card, 606, 52, "chassis3").setStyle('alarm.direction', 'right');
        this.createNode(card, 606, 166, "chassis3").setStyle('alarm.direction', 'right');
        this.createNode(card, 606, 147, "chassis3").setStyle('alarm.direction', 'right');
        this.createNode(card, 606, 128, "chassis3").setStyle('alarm.direction', 'right');
        this.createNode(card, 606, 109, "chassis3").setStyle('alarm.direction', 'right');
        this.createNode(card, 606, 90, "chassis3").setStyle('alarm.direction', 'right');
        this.createNode(card, 606, 71, "chassis3").setStyle('alarm.direction', 'right');
        this.createNode(card, 606, 185, "chassis3").setStyle('alarm.direction', 'right');

        var card = this.createNode(cardContainer, 641, 27, null, 43, 268);
        this.createNode(card, 653, 50, "chassis1");
        this.createNode(card, 653, 225, "chassis1");
        this.createNode(card, 653, 167, "chassis1");
        this.createNode(card, 653, 108, "chassis1");

        var secondShelf = this.createNode(null, 16, 319, "chassis10");
        var thirdShelf = this.createNode(null, 16, 447, "chassis2");
        this.createNode(thirdShelf, 363, 479, "chassis5");
        this.createNode(thirdShelf, 386, 479, "chassis5");
        this.createNode(thirdShelf, 409, 479, "chassis5");
        this.createNode(thirdShelf, 591, 479, "chassis5");
        this.createNode(thirdShelf, 614, 479, "chassis5");
        this.createNode(thirdShelf, 637, 479, "chassis5");
        this.createNode(thirdShelf, 461, 499, "chassis6");
        card = this.createNode(thirdShelf, 513, 447, null, 66, 120);
        this.createNode(card, 538, 526, "chassis7");
        this.createNode(card, 538, 489, "chassis7");
        this.createNode(card, 538, 450, "chassis7");

        var finder = new twaver.QuickFinder(this.box, "image");
        this.ports = new twaver.List(finder.find("chassis3"));
        finder.dispose();

        twaver.Util.moveElements(this.box.datas, 0, 40);
    },
    createCard: function (parent, index) {
        var node = this.createNode(parent, 44 + 43 * index, 27, null, 43, 268);
        this.createNode(node, node.getX() + 16, node.getY() + 10, "bolt");
        this.createNode(node, node.getX() + 16, node.getY() + node.getHeight() - 27, "bolt");
        return node;
    },
    createLight: function (parent, x, y, color) {
        var light = new twaver.Follower();
        light.setHost(parent);
        light.setStyle('body.type', 'vector');
        light.setStyle('vector.shape', 'circle');
        light.setStyle('vector.fill.color', color);
        light.setStyle('vector.gradient', 'radial.northeast');
        light.setSize(18, 18);
        light.setLocation(x, y);
        this.box.add(light);
        return light;
    },
    createNode: function (parent, x, y, image, w, h) {
        var node = new twaver.Follower();
        node.setStyle('outer.padding', 2);
        node.setStyle('select.color', '#000000');
        node.setStyle('alarm.position', 'center');
        node.setParent(parent);
        node.setHost(parent);
        node.setLocation(x, y);
        if (w) node.setWidth(w);
        if (h) node.setHeight(h);
        if (image != null) {
            node.setImage(image);
        } else {
            node.setStyle('body.type', 'vector');
            node.setStyle('vector.gradient', 'none');
            node.setStyle('vector.shape', 'rectangle');
            node.setStyle('vector.deep', 4);
        }
        this.box.add(node);
        return node;
    }
});

  
</script>
</body>
</html>