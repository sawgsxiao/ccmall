$(function(){
		/*
		var alist=$("#sidebar a[href!='#']");
		console.log(alist);
		for(var a in alist){
			//if(alist[a].href!="#"){
				console.log(alist[a].href);
				//alist[a].target="rightiframe";
				alist[a].click = function(){
					//$("#tabbox ul").append("<li id='"+alist[a].parent().attr("class")+"'>"+alist[a].text()+"<a href='javascript:;'  >关闭</a></li>");
					alert(1);
					//$("#iframeview").append("<iframe id=\"iframe"+a+"\" name=\""+alist[a].parent().attr("class")+"\" align=\"right\" ></iframe>");
					//alist[a].target=alist[a].parent().attr("class");
					//$("#iframeview").find("iframe[id='iframe"+a+"']").hide();
				};
				
			//}
			
		}
*/
		$("#sidebar a[href!='#']").bind("click", function(){
			var pId = $(this).parent().attr("id");
			var result = true;
			if($("#tabbox ul a[xys='"+pId+"']").length==0){
				$("#tabbox ul").append("<li style='cursor: pointer;' id='li"+pId+"'>"+$(this).text()+"<a xys='"+pId+"' onclick='removeLi(this);'>关闭</a></li>");
				$("#iframeview").append("<iframe id='iframe"+pId+"' name='"+pId+"' align='right'></iframe>");
			}else{
				result = false;
			}
			$(this).attr("target",pId);
			var iframe=$("#iframeview").find("iframe[id='iframe"+pId+"']");
			iframeInit(iframe);
			$("#iframeview").find("iframe[id!='iframe"+pId+"']").hide();
			$("#iframeview").find("iframe[id='iframe"+pId+"']").show();
			$("#tabbox ul li[id!='li"+pId+"']").addClass("vistited");
			$("#tabbox ul li[id='li"+pId+"']").removeClass("vistited");
			return result;
		});
		
		
		function removeLi(act){
			var node=$(act).parent().prev();
			//console.log($(node).length);
			if($(node).length!=1){
				node=$(act).parent().next();
			}
			var tab=$(node).find("a").attr("xys");
			//console.log(node);
			$("#iframe"+$(act).attr("xys")).remove();
			$(act).parent().remove();
			$(node).removeClass("vistited");
			$(node).addClass("hover");
			$("#tabbox li[id!='li"+tab+"']").removeClass("hover");
			$("#tabbox li[id!='li"+tab+"']").addClass("vistited");
			$("#iframeview iframe[id!='iframe"+tab+"']").hide();
			$("#iframeview iframe[id='iframe"+tab+"']").show();
		}

		$("#sidebar button").click(function(){
			var result = true;
			if($("#tabbox ul a[xys='topbtn']").length==0){
				$("#tabbox ul").append("<li style='cursor: pointer;' id='litopbtn'>快捷栏<a xys='topbtn' onclick='removeLi(this);'>关闭</a></li>");
				$("#iframeview").append("<iframe id='iframetopbtn' name='topbtn' align='right'></iframe>");
				$("#iframetopbtn").attr("src",$(this).attr("onclick"));
				var iframe=$("#iframeview").find("iframe[id='iframetopbtn']");
				iframeInit(iframe);
				$("#iframeview").find("iframe[id!='iframetopbtn']").hide();
				$("#iframeview").find("iframe[id='iframetopbtn']").show();
				$("#tabbox ul li[id!='litopbtn']").addClass("vistited");
				$("#tabbox ul li[id='litopbtn']").removeClass("vistited");
			}if($("#tabbox ul a[xys='topbtn']").length==1){
				$("#iframetopbtn").attr("src",$(this).attr("onclick"));
				var iframe=$("#iframeview").find("iframe[id='iframetopbtn']");
				iframeInit(iframe);
				$("#iframeview").find("iframe[id!='iframetopbtn']").hide();
				$("#iframeview").find("iframe[id='iframetopbtn']").show();
				$("#tabbox ul li[id!='litopbtn']").addClass("vistited");
				$("#tabbox ul li[id='litopbtn']").removeClass("vistited");
			}else{
				result = false;
			}
			
			
			return result;
		});
		
		$(".user-menu").find("a").click(function(){
			var result = true;
			if($("#tabbox ul a[xys='topbtn']").length==0){
				$("#tabbox ul").append("<li style='cursor: pointer;' id='litopbtn'>快捷栏<a xys='topbtn' onclick='removeLi(this);'>关闭</a></li>");
				$("#iframeview").append("<iframe id='iframetopbtn' name='topbtn' align='right'></iframe>");
				$("#iframetopbtn").attr("src",$(this).attr("onclick"));
				var iframe=$("#iframeview").find("iframe[id='iframetopbtn']");
				iframeInit(iframe);
				$("#iframeview").find("iframe[id!='iframetopbtn']").hide();
				$("#iframeview").find("iframe[id='iframetopbtn']").show();
				$("#tabbox ul li[id!='litopbtn']").addClass("vistited");
				$("#tabbox ul li[id='litopbtn']").removeClass("vistited");
			}if($("#tabbox ul a[xys='topbtn']").length==1){
				$("#iframetopbtn").attr("src",$(this).attr("onclick"));
				var iframe=$("#iframeview").find("iframe[id='iframetopbtn']");
				iframeInit(iframe);
				$("#iframeview").find("iframe[id!='iframetopbtn']").hide();
				$("#iframeview").find("iframe[id='iframetopbtn']").show();
				$("#tabbox ul li[id!='litopbtn']").addClass("vistited");
				$("#tabbox ul li[id='litopbtn']").removeClass("vistited");
			}else{
				result = false;
			}
			
			return result;
		});
	});

	