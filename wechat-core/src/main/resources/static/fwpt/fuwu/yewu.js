$(function (){
	var btns = $(".tghbtgDiv input");
	btns.click(function (){
		btns.each(function (){
			$(this).attr("class", $(this).attr("oclass")).attr("selected", false);
		});
		$(this).attr("class", $(this).attr("nclass")).attr("selected", true);
		$("#shjgIpt").val($(this).attr("val"));
//		$("#shjgSpan").html("您已选择了" + $(this).attr("val") + "！");
	});
});
