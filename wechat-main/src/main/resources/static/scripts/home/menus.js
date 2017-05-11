$(function() {
	// 左边菜单展开效果
	function leftMenuExpandAnimate(){
		var menuUls = $(".menu_expand_ul");
		var lis = $(".menu_ul > li");
		menuUls.hide();
		lis.click(function() {
			var a = $(">a", this);
			if (a.hasClass("expand"))
				return;
			$(">a", lis).removeClass("expand").addClass("closed");
			a.addClass("expand").removeClass("closed");
			menuUls.slideUp();
			var ul = $(this).find("ul");
			ul.slideDown();
		});
		lis.first().click();
	}
	leftMenuExpandAnimate();
	// 左侧菜单收缩效果
	function leftMenuHideAnimate() {
		var navas = $(".nav_div ul li a");
		$('.left_menu_closed_ico').click(function() {
			$("#left_menu").animate({
				width : "10px",
				height : "38px"
			});
			$(".right_box").animate({
				width : "1181px"
			});
			$(this).hide();
			$('.left_menu_expand_ico').show();
		});
		$('.left_menu_expand_ico').click(function() {
			$("#left_menu").animate({
				width : "178px",
				height : "100%"
			});
			$(".right_box").animate({
				width : "1015px"
			});
			$(this).hide();
			$('.left_menu_closed_ico').show();
		});
	}
	leftMenuHideAnimate();
});
