jQuery.extend(jQuery.validator.defaults, {
	focusInvalid: false,
	errorClass: "ValidError",
	validClass: "ValidCorrect",
	focusClass: "ValidFocus",
	errorElement: "div",
	onfocusin:onfocusinExtend,
	onfocusout:onfocusoutExtend,
	onkeyup:onfocusoutExtend,
	onclick:onclickExtend,
	// set this class to error-labels to indicate valid fields
	success: function(label) {
		// set &nbsp; as text for IE
		label.html("&nbsp;").addClass("ValidCorrect");
	}
});

function onclickExtend(element) {
	// click on selects, radiobuttons and checkboxes
	if ( element.name in this.submitted )
		this.element(element);
	// or option elements, check parent select in that case
	else if (element.parentNode.name in this.submitted)
		this.element(element.parentNode);
	hideAttention(this,element);
}

function onkeyupExtend(element) {
	if ( element.name in this.submitted || element == this.lastElement ) {
		this.element(element);
	}
	hideAttention(this,element);
}
 
function hideAttention(object,element){
	var name = object.idOrName(element);
	var label = $( object.settings.errorElement + "[class=ValidFocus]" , object.errorContext ).filter(function() {
		return $(this).attr('for') == name;
	});
	if ( label.length ) 
		label.hide();
}

function onfocusoutExtend(element){
	if ( !this.checkable(element) && (element.name in this.submitted || !this.optional(element)) ) {
		this.element(element);
	}
	hideAttention(this,element);
}

function onfocusinExtend(element){
	this.lastActive = element;
	// hide error label and remove error class on focus if enabled
		this.settings.unhighlight && this.settings.unhighlight.call( this, element, this.settings.errorClass, this.settings.validClass );
		this.addWrapper(this.errorsFor(element)).hide();
	
	var name = this.idOrName(element);
	
	var label = $( this.settings.errorElement + "[class=ValidFocus]" , this.errorContext ).filter(function() {
		return $(this).attr('for') == name;
	});

	if ( label.length ) {
		//如果规则做过add或remove操作后提示内容需要改变
		var rules = $(element).rules();
		for (var method in rules ) {
			if(method=='remote')
				continue;
			var rule = { method: method, parameters: rules[method] };
		}
		if(rule){
			var message = this.defaultMessage( element, rule.method ),
			theregex = /\$?\{(\d+)\}/g;
			if ( typeof message == "function" ) {
				message = message.call(this, rule.parameters, element);
			} else if (theregex.test(message)) {
				message = jQuery.format(message.replace(theregex, '{$1}'), rule.parameters);
			}
			//alert(message);
			if(label.html()!=message)
				label.html(message);
		}
		 label.show();
	} else {
		var rules = $(element).rules();
		for (var method in rules ) {
			if(method=='remote')
				continue;
			var rule = { method: method, parameters: rules[method] };
		}
		if(rule){
		var message = this.defaultMessage( element, rule.method ),
		theregex = /\$?\{(\d+)\}/g;
		if ( typeof message == "function" ) {
			message = message.call(this, rule.parameters, element);
		} else if (theregex.test(message)) {
			message = jQuery.format(message.replace(theregex, '{$1}'), rule.parameters);
		}
		// create label
		label = $("<" + this.settings.errorElement + "/>")
			.attr({"for":  this.idOrName(element), generated: true,"title":message})
			.addClass("ValidFocus")
			.html(message || "");
		if ( this.settings.wrapper ) {
			// make sure the element is visible, even in IE
			// actually showing the wrapped element is handled elsewhere
			label = label.hide().show().wrap("<" + this.settings.wrapper + "/>").parent();
		}
		if ( !this.labelContainer.append(label).length )
			this.settings.errorPlacement
				? this.settings.errorPlacement(label, $(element) )
				: label.insertAfter(element);
	}
	}

}

jQuery.validator.addMethod("ip", function(value, element) {    
	if(value=='255.255.255.255' || value=='0.0.0.0'){
		return false;
	}
  return this.optional(element) || value=='' || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));    
}); 
jQuery.validator.addMethod("ipv4orv6", function(value, element) {
	if(value=='255.255.255.255' || value=='0.0.0.0'){
		return false;
	}
  return this.optional(element) || value=='' || ((/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256))||(value.match(/:/g).length<=7&&/::/.test(value)?/^([\da-f]{1,4}(:|::)){1,6}[\da-f]{1,4}$/i.test(value):/^([\da-f]{1,4}:){7}[\da-f]{1,4}$/i.test(value)));    
}); 
//非组播IP value必须确定是一个IP地址
jQuery.validator.addMethod("notIgmpIp", function(value, element) {    
	return !isMulticastAddress(value);
}); 
//单播地址
jQuery.validator.addMethod("unicastAddress", function(value) {    
	  return isUnicastAddress(value);    
});
//不能为空的逻辑应该通过required属性限制
jQuery.validator.addMethod("subnetworkMask", function(value) {    
	return value=='' || validateMask(value);    
});
jQuery.validator.addMethod("subnetworkMaskRange", function(value,element,param) {    
	return this.optional(element) || validateSubnetworkMaskRange(value,$(param[0]).val(),$(param[1]).val());    
});
jQuery.validator.addMethod("sameNetworkSegment", function(value,element,param) {    
	return this.optional(element) || value=='' || validateSameNetworkSegment(value,$(param[0]).val(),$(param[1]).val());    
});
jQuery.validator.addMethod("mac", function(value, element) { 
	if(value=='00-00-00-00-00-00' || value=='ff-ff-ff-ff-ff-ff' || value=='FF-FF-FF-FF-FF-FF'){
		//全部为0、广播MAC地址不合法
		return false;
	}
	if(/^01-00-5e(-[0-9A-Fa-f]{2}){3}$/.test(value)||/^01-00-5E(-[0-9A-Fa-f]{2}){3}$/.test(value)){
		//组播MAC地址不合法
		return false;
	}
	return this.optional(element) || (/^([0-9A-Fa-f]{2})(-[0-9A-Fa-f]{2}){5}$/.test(value));    
}); 
jQuery.validator.addMethod("zyCode", function(value, element) { 
	return this.optional(element) || (/^([0-9A-Fa-f]{12})$/.test(value));    
}); 

jQuery.validator.addMethod("len", function(value, element,param) {    
  return this.optional(element) || this.getLength($.trim(value), element) == param;    
}); 
jQuery.validator.addMethod("evenRange",	function( value, element, param ) {
			return this.optional(element) || ( value >= param[0] && value <= param[1] && value%2==0);
});
jQuery.validator.addMethod("anyString",	function( value, element, param ) {
	return this.optional(element) || (/^(.*)$/.test(value));
});
jQuery.validator.addMethod("macAddr",	function( value, element, param ) {
	return this.optional(element) || (/^(.*)$/.test(value)); 
});
//mac地址中的字符只能是0-9 a-f A-F '-'
jQuery.validator.addMethod("macCharValidate",	function( value, element, param ) {
	return !(/[^a-fA-F0-9-]/g.test(value));
});
//ip地址中的字符只能是0-9 '.'
jQuery.validator.addMethod("ipCharValidate",	function( value, element, param ) {
	return !(/[^0-9\.]/g.test(value));
});
jQuery.validator.addMethod("ipSearch", function(value, element) {    
	return this.optional(element) || (/^(.*)$/.test(value));    
});
jQuery.validator.addMethod("moreThan", function(value, element,param) {  
	var curValue = parseInt(value);
	var targetValue = parseInt($(param).val());
	return curValue>targetValue;    
}); 
//终结点编号比较
jQuery.validator.addMethod("moreThanTerminalCode", function(value, element,param) {  
	var curValue = parseInt(value.replace(/^0+/, ''));
	var targetValue = parseInt($(param).val().replace(/^0+/, ''));
	return curValue>targetValue;    
}); 
//终结点编号比较
jQuery.validator.addMethod("digitsCustom", function(value, element,param) {  
	return this.optional(element) || /^[-]?[0-9]+$/.test(value);
}); 
jQuery.validator.addMethod("noLessThanGeneral", function(value, element,param) {  
	var curValue = parseInt(value);
	var targetValue = parseInt($(param).val());
	return curValue>targetValue || curValue == targetValue;    
}); 
//比较起始时间与结束时间
jQuery.validator.addMethod("moreThanTime", function(value, element,param) {  
	var curDate = new Date(value.replace(/\-/g, "\/"));
	var targetDate = new Date($(param).val().replace(/\-/g, "\/"));
	return curDate>targetDate;    
}); 
//比较两个IP地址大小
jQuery.validator.addMethod("moreThanIp", function(value, element,param) {  
	return compareIP(value,$(param).val())==1;   
}); 

jQuery.validator.addMethod("notEqual", function(value, element,param) {  
	return value != $("#"+param[0]).val();   
}); 

//比较两个IP地址大小,并限制IP范围
//param中的第一个参数为范围值，第二个参数为结束对象的Id
jQuery.validator.addMethod("moreThanIpRange", function(value, element,param) { 
	var ipRange = ipSpan($(param[1]).val(),value);
	return ipRange && ipRange>0 && ipRange <=1024;
}); 

jQuery.validator.addMethod("isMiddle", function(value, element,param) {
	var params = param.split(',');
	var startIp = $(params[0]).val();
	var endIp = $(params[1]).val();
	return  compareIP(value, startIp)!=-1&&compareIP(endIp ,value)!=-1;
});

jQuery.validator.addMethod("notMoreThan200", function(value, element,param) {   
	return value-$(param).val()<=200;    
}); 
jQuery.validator.addMethod("lessThan", function(value, element,param) {    
	return this.optional(element) || value<$(param).val();
}); 
//固定带宽<=保证带宽
jQuery.validator.addMethod("lessEqualPledgeBandwidth", function(value, element,param) {
	return value<=$(param).val();
}); 

//禁止输入特殊字符，允许输入汉字，字母、数字和下划线
jQuery.validator.addMethod("filtSpecialChar", function(value, element) {    
	return this.optional(element) || (/^[^~`!@#$%\^&\*\(\)\+\=\{\}\[\]'"\'\<\>\?\|\\]+$/.test(value));
});
//只能输入数字字母或其它文字
jQuery.validator.addMethod("legalChar", function(value, element) {    
	return this.optional(element) || (/^[\da-zA-Z\u00c0-\uFFFF]+$/.test(value));
}); 
//联系电话(手机/电话皆可)验证   
jQuery.validator.addMethod("phone", function(value,element) {   
	var mobile = /^(\d{11})$/; 
	var tel = /^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{5}|\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{5}|\d{4}|\d{3}|\d{2}|\d{1}))$/;   
	return this.optional(element) || (tel.test(value) || mobile.test(value)); 
});  
// 手机号码验证       
jQuery.validator.addMethod("mobile", function(value, element) {         
	var mobile = /^(\d{11})$/;   
	return this.optional(element) || mobile.test(value);   
});  
//电话号码验证      
jQuery.validator.addMethod("tel", function(value, element) {         
	var tel = /^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{5}|\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{5}|\d{4}|\d{3}|\d{2}|\d{1}))$/;   
	return this.optional(element) || tel.test(value);   
}); 
//验证用户名
jQuery.validator.addMethod("userName", function(value, element) {    
	return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);    
}); 

//验证onu数据业务vlan配置中trunk vlan
jQuery.validator.addMethod("vlanInputRule", function(value, element) { 
	 return value == '' ||  /^[1-9]\d{0,3}(,[1-9]\d{0,3}){0,63}$/.test(value);
}); 

//验证onu组播业务全局配置FastLeave能力
jQuery.validator.addMethod("fastLeaveAbility", function(value, element) { 
	 return this.optional(element) || /^[0-1]{4}$/.test(value);
}); 
//在此条件下无采集参数，请重新选择
jQuery.validator.addMethod("noRequiredPmParam", function(value, element) { 
	return $.trim(value).length>0;
}); 

//验证十六进制 改成两个字节
jQuery.validator.addMethod("customTypes", function(value, element) { 
	if(/^(0){1,4}$/.test(value)){//不能全是0
		return false;
	}
	 return this.optional(element) || /^([0-9A-Fa-f]){1,4}$/.test(value);
}); 

//验证onu数据业务vlan配置中trunk vlan 根据ctc2.1规范 最多配30个trunkVlan
jQuery.validator.addMethod("vlanInputOtherRule", function(value, element,param) { 
	var vlanArray = value.split(',');
	if(vlanArray.length>param){
		return false;
	}
	for(var prop in vlanArray){
		if(vlanArray[prop] > 4094){
			return false;
		}
	}
	if(repeatValInArray(vlanArray)){
		return false;
	}
	return true;
}); 

//trunk vlan和pvid不能相同
jQuery.validator.addMethod("eqlToDefaultVlanRule", function(value, element) { 
	var vlanArray = value.split(',');
	for(var prop in vlanArray){
		if($("#defaultVid").val() == vlanArray[prop]){
			return false;
		}
	}
	return true;
}); 
//验证onu组播业务端口配置中转换vlan
jQuery.validator.addMethod("vlanTransInput", function(value, element) { 
	if(this.optional(element)){
		return true;
	}
	if(/^[1-9]\d{0,3}-[1-9]\d{0,3}(,[1-9]\d{0,3}-[1-9]\d{0,3}){0,7}$/.test(value)){
		var vlanArray = value.split(',');
		if(vlanArray.length>8){
			return false;
		}
		//原始vlan
		var originalVlan = new Array();
		//目标vlan字符串集合
		var destinatVlan = '##';
		for(var prop in vlanArray){
			var vlanValueArray = vlanArray[prop].split('-');
			if(vlanValueArray[0] > 4094 || vlanValueArray[1]>4094){
				return false;
			}
			//原始vlan也不能重复
			originalVlan.push(vlanValueArray[0]);
			//不能产生首尾相接配置。如100-200,200-500(这里第二个200应改为300) 
			destinatVlan += vlanValueArray[1]+'##';
		}
		for(var k in originalVlan){
			if(destinatVlan.indexOf('##'+originalVlan[k]+'##')>-1){
				return false;
			}
		}
		if(repeatValInArray(originalVlan)){
			return false;
		}
		if(repeatValInArray(vlanArray)){
			return false;
		}
	}else{
		return false;
	}
	return true;
}); 
//vlan 输入 1,2,3-5形式
jQuery.validator.addMethod("vlanTest", function(value, element) { 
	if(this.optional(element)){
		return true;
	}
		
	if((/^[1-9]\d{0,3}(,[1-9]\d{0,3}-[1-9]\d{0,3}){0,63}(,[1-9]\d{0,3}){0,63}/.test(value))||(/^[1-9]\d{0,3}-[1-9]\d{0,3}(,[1-9]\d{0,3}-[1-9]\d{0,3}){0,63}(,[1-9]\d{0,3}){0,63}/.test(value))){
		var values = value.split(',');
		for(var i=0;i<values.length;i++){
			if(values[i].indexOf("-")>0){
				var range = values[i].split("-");
				if(parseInt(range[0])>parseInt(range[1])){
					alert("起始大于结束");
					return false;
				}
			}
		}
	}else{
		return false;
	}
	return true;
}); 
//输入的vlan是否在已经存在，界面校验
jQuery.validator.addMethod("testIn", function(value, element,param) { 
	var params = param.split(',');
	if(this.optional(element)){
		return true;
	}
		
	if((/^[1-9]\d{0,3}(,[1-9]\d{0,3}-[1-9]\d{0,3}){0,63}(,[1-9]\d{0,3}){0,63}/.test(value))||(/^[1-9]\d{0,3}-[1-9]\d{0,3}(,[1-9]\d{0,3}-[1-9]\d{0,3}){0,63}(,[1-9]\d{0,3}){0,63}/.test(value))){
		var values = value.split(',');
		for(var i=0;i<values.length;i++){
			if(values[i].indexOf("-")>0){
				var range = values[i].split("-");
				if(parseInt(range[0])>parseInt(range[1])){
					alert("起始大于结束");
					return false;
				}else{
					for(var k=parseInt(range[0]);k<=parseInt(range[1]);k++){
						for(var j=0;j<params.length;j++){
							if(params[j]==k){
								break;
							}
						}
						if(j==params.length){
							return false;
						}
					}
					return true;
				}
			}else{
				for(var j=0;j<params.length;j++){
					if(params[j]==values[i]){
						return true;
					}
				}
			}
		}
	}else{
		return false;
	}
	return false;
}); 
//验证输入格式为,匹配CVLAN或SVLAN时，范围1-4094，可以是单个值也可以是一个范围值
jQuery.validator.addMethod("vlanRange", function(value, element, param) { 
	var values = value.split('-');
	if(/^(\d+)\-(\d+)$/.test(value)) {
		return (parseInt(values[1])>parseInt(values[0]) && values[0]>=param[0] && values[0]<=param[1] &&  values[1]>=param[0] && values[1]<=param[1]) ; 
	} else {
		return (/^(\d+)$/.test(value) &&  value>=param[0] && value<=param[1]); 
	}
}); 
//验证输入格式为,匹配CVLAN或SVLAN时，匹配范围段只能使用一次。
jQuery.validator.addMethod("vlanAssociateRule", function(value, element, param) { 
	  if(/^(\d+)\-(\d+)$/.test($(param[0]).val()) && /^(\d+)\-(\d+)$/.test(value)) {
		  return false; 
	  } else {
		  return true; 
	  }
}); 
//验证输入格式为X-X，如5-9,1-8
jQuery.validator.addMethod("valueRange", function(value, element, param) { 
	  var values = value.split('-');
	  return (/^[1-9]\d{0,4}-[1-9]\d{0,4}$/.test(value) && parseInt(values[1])>parseInt(values[0]) && values[0]>=param[0] && values[0]<=param[1] &&  values[1]>=param[0] && values[1]<=param[1]);    
}); 

//验证ip地址范围为224.0.1.0～238.255.255.255
jQuery.validator.addMethod("ipRange", function(value, element) {    
	  return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (223<RegExp.$1 && RegExp.$1<239 && RegExp.$2<256 && 0<RegExp.$3 && RegExp.$3<256 && RegExp.$4<256));    
}); 
//批量新增黑白名单明细时使用混合验证方式。MAC地址和LOID必须且只能填写一项。此时是在jqgrid里面批量验证，表单元素的id符合1_id 2_id的形式
//如果id的值不符合1_id 2_id的形式，表示是在普通表单中进行验证 
jQuery.validator.addMethod("mixModeMacLoid", function(value, element,param) {
	var batch = /^\d+_(\w+)$/.test(element.id);
	var id = batch?element.id.substring(0,element.id.indexOf('_')+1):'';
	var startMac = $("#"+id+param[0]).val();
	var loid = $("#"+id+param[1]).val();
	return (startMac.length>0&&loid.length==0) || (startMac.length==0&&loid.length>0);
});
//验证Mac或者Loid至少填一项
jQuery.validator.addMethod("leastMacOrLoid", function(value, element,param) {
	var batch = /^\d+_(\w+)$/.test(element.id);
	var id = batch?element.id.substring(0,element.id.indexOf('_')+1):'';
	var startMac = $("#"+id+param[0]).val();
	var loid = $("#"+id+param[1]).val();
	return startMac.length>0 || loid.length>0;
});

//新增黑白名单明细时使用混合验证方式。填写填写mac之后，不能再填loid和pwd 。此时是在jqgrid里面批量验证，表单元素的id符合1_id 2_id的形式
//如果id的值不符合1_id 2_id的形式，表示是在普通表单中进行验证 
jQuery.validator.addMethod("mixModePwd", function(value, element,param) {
	var batch = /^\d+_(\w+)$/.test(element.id);
	var id = batch?element.id.substring(0,element.id.indexOf('_')+1):'';
	var startMac = $("#"+id+param[0]).val();
	return !(startMac.length>0&&value.length>0);
});
//MAC地址不能重复。
jQuery.validator.addMethod("noRepeatMac", function(value, element,param) { 
	return noRepeatInJqGrid(value,element);
});
//loid不能重复
jQuery.validator.addMethod("noRepeatLoid", function(value, element,param) { 
	return noRepeatInJqGrid(value,element);
});
//loid + password不能重复
jQuery.validator.addMethod("noRepeatLoidAndPassword", function(value, element,param) { 
	var values = new Array();
	var ids = $("#"+param).jqGrid('getDataIDs');  
    for(var i=0;i<ids.length;i++){
    	values.push($("#"+ids[i]+"_loid").val()+"-"+$("#"+ids[i]+"_password").val());
    }	
	return !repeatValInArray(values);
});
//不能重复
jQuery.validator.addMethod("noRepeat", function(value, element,param) { 
	var values = new Array();
	$.each($("input[name='"+element.name+"']"), function(){
		if($(this).val()!="")
			values.push($(this).val());
	});
	return !repeatValInArray(values);
});
//vlanRule
jQuery.validator.addMethod("vlanRule", function(value, element,param) { 
	var myValue=value;
	if(param[3]||param[3]=='true'){
		if(/^[1-9]\d{0,3}(,[1-9]\d{0,3}){0,63}$/.test(value)){
			var vlanArray = value.split(',');
			if(vlanArray.length>param[2]){
				return false;
			}
			for(var prop in vlanArray){
				if(vlanArray[prop] > param[1]){
					return false;
				}
			}
			if(repeatValInArray(vlanArray)){
				return false;
			}
			return true;
		}else{
			return false;
		}
	}else{
		if(value=''||value==null||value.length==0){
			return true;
		}else{
			if(/^[1-9]\d{0,3}(,[1-9]\d{0,3}){0,63}$/.test(myValue)){
				var vlanArray = myValue.split(',');
				if(vlanArray.length>param[2]){
					return false;
				}
				for(var prop in vlanArray){
					if(vlanArray[prop] > param[1]){
						return false;
					}
				}
				if(repeatValInArray(vlanArray)){
					return false;
				}
				return true;
			}else{
				return false;
			}
		}
	}
});

/**
 * 判断一个数组中的值是否有重复
 * paramArray 为要判断的数组
 * 如果有重复值返回true,否则返回false
 */
function repeatValInArray(paramArray){
	var uniqueValArray = [];
	 var temptyArray = [];
	 for(var prop in paramArray){
	     
	     var tempVal = paramArray[prop];
	     if(tempVal===temptyArray[prop]){
	         //防止对prototype进行比较
	         continue;
	     }
	     if(uniqueValArray[tempVal]!=1){
	         uniqueValArray[tempVal] = 1;
	     }else{
	         //lert('值重复:'+tempVal);
	         return true;
	     }
	 }
	 return false;
}

//ip地址大小比较(如果ipBegin大于ipEnd，返回1，等于返回0，小于返回-1)
function compareIP(ipBegin, ipEnd)  
{  
    var temp1;  
    var temp2;  
    temp1 = ipBegin.split(".");  
    temp2 = ipEnd.split(".");   
    for (var i = 0; i < 4; i++)  
    {  
        if (parseInt(temp1[i])>parseInt(temp2[i]))  
        {  
            return 1;  
        }  
        else if (parseInt(temp1[i])<parseInt(temp2[i]))  
        {  
            return -1;  
        }  
    }  
    return 0;     
}

//判断IP地址是否在指定的范围内
//大于等于起始IP，小于等于结束IP
function inNetworkSegment(ipAddress,beginIp,endIp){
	if((compareIP(ipAddress,beginIp)==1 || compareIP(ipAddress,beginIp)==0) && (compareIP(ipAddress,endIp)==-1 || compareIP(ipAddress,endIp) == 0)){
		return true;
	}else{
		return false;
	}
}
//是否是单播地址
function isUnicastAddress(ipAddress){
	return inNetworkSegment(ipAddress,IP_ADDRESS_UNICAST.beginIp,IP_ADDRESS_UNICAST.endIp);
}
//是否是组播地址
function isMulticastAddress(ipAddress){
	return inNetworkSegment(ipAddress,IP_ADDRESS_MULTICAST.beginIp,IP_ADDRESS_MULTICAST.endIp);
}
//是否是合法的子网掩码
function validateMask(MaskStr)
{
	/* 有效性校验 */
	var IPPattern = /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/;
	if(!IPPattern.test(MaskStr))return false;
	var ip_binary = ipBinaryString(MaskStr);
	if(ip_binary === false)return false;
	if(-1 != ip_binary.indexOf("01"))return false;
	return true;
}
//检验是否为合法IP
function validateIp(ip){
	var IPPattern = /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/;
	if(!IPPattern.test(ip))return false;
	/* 检查域值 */
	var IPArray = ip.split(".");
	var ip1 = parseInt(IPArray[0]);
	var ip2 = parseInt(IPArray[1]);
	var ip3 = parseInt(IPArray[2]);
	var ip4 = parseInt(IPArray[3]);
	if ( ip1<0 || ip1>255 || ip2<0 || ip2>255 || ip3<0 || ip3>255|| ip4<0 || ip4>255 )
	{
	   return false;
	}
	return true;
}


//获取ip的二进制字符串
function ipBinaryString(ip){
	if(!validateIp(ip)){
		return false;
	}
	/* 检查域值 */
	var IPArray = ip.split(".");
	var ip1 = parseInt(IPArray[0]);
	var ip2 = parseInt(IPArray[1]);
	var ip3 = parseInt(IPArray[2]);
	var ip4 = parseInt(IPArray[3]);
	if ( ip1<0 || ip1>255 || ip2<0 || ip2>255 || ip3<0 || ip3>255|| ip4<0 || ip4>255 )
	{
	   return false;
	}
	
	/* 检查二进制值是否合法 */
	//拼接二进制字符串
	var ip_binary = _checkIput_fomartIP(ip1) + _checkIput_fomartIP(ip2) + _checkIput_fomartIP(ip3) + _checkIput_fomartIP(ip4);
	return ip_binary;
}

function _checkIput_fomartIP(ip)
{
	return (ip+256).toString(2).substring(1); //格式化输出(补零)
}
//计算子网掩码的范围 传入的字符串必须是合法的子网掩码
function subnetworkMaskRange(subnetworkMask){
	var ipBinaryStr = ipBinaryString(subnetworkMask);
	if(ipBinaryStr===false)return false;
	var zeroCount = ipBinaryStr.match(/0/g).length;
	var oneString ='';
	for(var i=0;i<zeroCount;i++){
		oneString += '1';
	}
	return parseInt(oneString,2);
}
//判断结束IP到起始IP的范围
function ipSpan(beginIp,endIp){
	if(validateIp(beginIp) && validateIp(endIp)){
		var endIpValue = parseInt(ipBinaryString(endIp),2);
		var beginIpValue = parseInt(ipBinaryString(beginIp),2);
		return endIpValue - beginIpValue ;
	} 
	return false;
}
//判断一个子网掩码的范围是否大于给定的两个ip的范围
//endIp应该大于beginIp
function validateSubnetworkMaskRange(mask,beginIp,endIp){
	var maskRange = subnetworkMaskRange(mask);
	if(maskRange===false)return false;
	var ipSpanParam = ipSpan(beginIp,endIp);
	if(ipSpanParam===false)return false;
	if(ipSpanParam>-1 && maskRange>ipSpanParam){
		return true;
	}else{
		return false;
	}
}
//获得ip网段
function networkSegment(ip,mask){
	var ipBinaryStr = ipBinaryString(ip);
	if(ipBinaryStr===false)return false;
	var maskBinaryStr = ipBinaryString(mask);
	if(maskBinaryStr===false)return false;
	return parseInt(ipBinaryStr,2) & parseInt(maskBinaryStr,2);
}
//判断两个ip是否在同一网段
function validateSameNetworkSegment(ipX,ipY,mask){
	var segX = networkSegment(ipX,mask);
	if(segX===false)return false;
	var segY = networkSegment(ipY,mask);
	if(segY===false)return false;
	return segX==segY;
}
/**
 * 在用jqgrid批量添加或修改时，判断某个字段是否重复
 * @value 被验证input元素的值
 * @element 被验证的input元素本身
 */
function noRepeatInJqGrid(value,element){
	if(value==null||value==''){
		return true;
	}
	var suffix = element.id.substring(element.id.indexOf('_'),element.id.length);
	var result = true;
	$("input[id$="+suffix+"]").each(function(){
		if($(this).val()==value && this.id != element.id){
			result = false;
		}
	});
	return result;
}

var IP_ADDRESS_A = {beginIp:"10.0.0.0",endIp:"10.255.255.255"};//A类地址
var IP_ADDRESS_B = {beginIp:"172.16.0.0",endIp:"172.31.255.255"};//B类地址
var IP_ADDRESS_C = {beginIp:"192.168.0.0",endIp:"192.168.255.255"};//C类地址
var IP_ADDRESS_UNICAST = {beginIp:"0.0.0.0",endIp:"223.255.255.255"};//单播地址范围
var IP_ADDRESS_MULTICAST = {beginIp:"224.0.1.0",endIp:"238.255.255.255"};//组播地址范围
