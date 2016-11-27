<%@ page contentType="text/html;charset=UTF-8"%>
<script type="text/javascript">
var ajaxOptions = {
		targetId : null,
		url:null,
		params : null,
		before : ajaxBefore,
		success : ajaxSuccess,
		callback: callback
};

function ajaxBefore() {
	$('body').append( "<div class='modal hide fade confirm-modal' id='ajaxloaddiv'> </div>" ).modal('show');
}

function ajaxError(XMLHttpRequest, error, thrownError) {
	
}

/**
 * 原始ajax请求成功后。默认执行方式，显示"操作成功"并刷新页面
 * @param responseText   后台返回文本
 * @return
 */
function ajaxSuccess(responseText,textStatus) {
	$('#ajaxloaddiv').modal('hide');
}
 
function callback(responseText, textStatus, XMLHttpRequest) {
	  if(textStatus=="error"){
    	 
      }
}

 if ( typeof window.jQuery != "undefined" ) {
	    jQuery.ajaxSettings.traditional = true;
 }



/**
 * 原始ajax提交。适用于删除元素等操作
 * @param ajaxOptions   ajax请求参数对象
 * @return
 */
function simpleAjax(ajaxOptions) {
	$.ajax( {
		type : "POST",
		url : ajaxOptions.url,
		dataType : "text",
		data : ajaxOptions.params,
		beforeSend : ajaxOptions.before,
		success : ajaxOptions.success,
		error : ajaxError
	});
}

 
/**
 * load页面
 * @param ajaxOptions  ajax请求参数对象
 * @return
 */
function loadAjax(ajaxOptions) {
	$("#" + ajaxOptions.targetId).load(ajaxOptions.url, ajaxOptions.params, ajaxOptions.callback);
}
 
</script>