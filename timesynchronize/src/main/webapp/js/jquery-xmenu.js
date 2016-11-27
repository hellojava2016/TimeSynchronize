/*
 * X-Menu 下拉框多选组件
 * 依赖jquery.powerFloat.js 
 * 整合powerFloat
 * 浏览器支持 FF Chrome Opera IE9 IE8一下不兼容
 * @author yelingfeng
 * @email  yelingfeng521@gmail.com 
 */
 
(function($) {

	var defaults  = {
		width :580, //可选参数：inherit，数值(px)
		eventType: "click", //事件类型，其他可选参数有：click, focus
		dropmenu:".xmenu",//弹出层div
		hiddenID : "selectdeptidden",//隐藏域ID
		emptytext: "请选择金融机构"
	};
	
	$.fn.xMenu = function(options) {
		return $(this).each(function() {
		
			var owl = $.extend({}, defaults, options || {});
			//触发按钮
			var $this = $(this);
			//span
			var $span = $this.find("span");
			//浮动层主div
			var $dropmenu= $(owl.dropmenu);
			//触发隐藏域 存职位ID
			var $Hd = $("#"+owl.hiddenID);
			//主li
			var $mli = $("dl li",$dropmenu);
			//关闭按钮
			var $closebtn =$(".m-close", $dropmenu);			
			//已选在职位div
			var $selectinfo = $(".select-info",$dropmenu);
			//已选在职位ul
			var $selectUl = $("ul",$selectinfo);
			//确认按钮
			var $okbtn = $("a[name='menu-confirm']",$selectinfo);
			
			var $mdt = $("dt",$dropmenu);
			
			//绑定关闭
			$closebtn.click(function(){
				 $.powerFloat.hide();
			});
			//绑定保存
			$okbtn.click(function(){
				var $li =$selectUl.find("li");
				var name = "";
				var id = "";			
				$li.each(function(){
					_this = $(this);
					name += _this.text()+",";
					id +=  _this.attr("rel")+",";
				})
				name = name.substring(0,name.length-1);
				id = id.substring(0,id.length-1);
				
				if(name.length>20){
					$span.attr({"title":name});
					name =name.substring(0,20)+"...";
				}
				
				$span.text(name);
				$Hd.val(id);
				$.powerFloat.hide();
			});
			
			
			//绑定每一个职位
			$mli.click(function(){
				var $li = $(this);
				var val  =$li.text();
				var id  = $li.attr("rel");
				if($selectinfo.is(":hidden")){
					$selectinfo.show();
				}
				if($li.hasClass("current")){
					$selectUl.find("li[rel='"+id+"']").remove();					
					$li.removeClass("current");
				}else{					
					$("<li rel='"+id+"' class='current'>"+val+"</li>").appendTo($selectUl);
					$li.addClass("current");
				}				
				if($selectUl.children("li").length==0){
					$selectinfo.hide();
				}
			});
			
			$mdt.click(function(){
				var $mtt = $(this);
				var $mlit = $("ul li",$mtt.next());
				$mlit.each(function(){
					var $tlit =$(this);
					var val  =$tlit.text();
					var id  = $tlit.attr("rel");
					if($selectinfo.is(":hidden")){
						$selectinfo.show();
					}
					if($tlit.hasClass("current")){
						
					}else{					
						$("<li rel='"+id+"' class='current'>"+val+"</li>").appendTo($selectUl);
						$tlit.addClass("current");
					}				
					if($selectUl.children("li").length==0){
						$selectinfo.hide();
					} 
				});
			});
			
			
			
			$selectUl.on("click","li",function(){
				var $li = $(this);
				var id  = $li.attr("rel");
				$mli.filter("li[rel='"+id+"']").removeClass("current");	
				$li.remove();
				if($selectUl.children("li").length==0){
					$selectinfo.hide();
				}
			}); 
			
			
			//绑定power浮动层
			$this.powerFloat({
				width: owl.width,
				eventType: owl.eventType,
				offsets: {
					x: 0,
					y: -1 
				},
				target: $dropmenu, 	 		
				showCall: function(){ 				
					//标注已选职位
					setCurrentItem($Hd.val());					
					$this.addClass("menu-open");
				},
				hideCall:function(){
					$this.removeClass("menu-open");
					if($selectUl.children("li").length==0){
						$span.text(owl.emptytext);
						$Hd.val("");
					}
				}
			});
			//选中已选的职位
			var setCurrentItem = function (val){				
				if(val&&val!=""){
					$mli.removeClass("current");
					var array = val.split(",");
					$selectUl.empty();
					var name = "";
					for(var i=0;i<array.length;i++){
						var $cli = $mli.filter("li[rel='"+array[i]+"']");
						$cli.addClass("current");
						$("<li rel='"+array[i]+"' class='current'>"+$cli.text()+"</li>").appendTo($selectUl);
						name += $cli.text()+",";
					}	
					if(name.length>20){
						name =name.substring(0,20)+"...";
					}
					$span.text(name);
				}else{
					$selectinfo.hide();
				}
				
			}	
			setCurrentItem($Hd.val());	
		});
	};
	

	
	
})(jQuery);