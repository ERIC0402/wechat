(function() {
	window.jxls = {};
	window.qjui = {};
	window.dqname = {};
	window.Iaphael = {};
	window.yymap = new Array();
	var ra;
	var cir;
	var rqdx = {
		width : 950,
		height : 500
	}
	var hbcd = 2500;
	var hbkd = 1855;
	Iaphael.init = function(opts) {
		Raphael("raphaelDiv", hbcd, hbkd, function() {
					var r = this;
					r.image(base + "/static/fwpt/fuwu/images/yymap/map_03.jpg",
							0, 0, hbcd, hbkd);
					var over = function() {
						this.c = this.c || this.attr("fill");
						this.stop().animate({
									fill : "#b4eea9",
									"fill-opacity" : 0.4
								});
						this.attr("cursor", "pointer");
					}, out = function() {
						this.stop().animate({
									fill : this.c,
									"fill-opacity" : 0
								});

					};
					r.setStart();
					var hue = Math.random();
					for (var country in yymap) {
						var name = yymap[country].key;
		// 闭包传参
					(function(name) {
							var jxl = r.path(yymap[country].value).attr({
										stroke : "#b4eea9",
										fill : "#f0efeb",
										"fill-opacity" : 0,
										"stroke-opacity" : 0.25
									})
							jxl.click(function() {
										dqname = name;
										$name("dybtn").click();
									});
							jxls[name] = jxl;
						})(name);

					}
					var world = r.setFinish();
					world.hover(over, out);
					// world.animate({ stroke: "#b4eea9"}, 1000);
					ra = r;
				});

		$(function() {
					$("#raphaelDiv").draggable({
						drag : function(event, ui) {
							var pd = $("#raphaelParentDiv");
							var rd = $("#raphaelDiv");
							// $(".massage-tip").html(ui.position.top + ":"
							// + ui.position.left)
							if (ui.position.top > 0 || ui.position.left > 0) {
								return false;
							}
							if (ui.position.top < 500 - hbkd
									|| ui.position.left < 950 - hbcd) {
								return false;
							}
							return true;
						}
					});
				});

		$(function() {
					$("#raphaelDiv").css({
								left : -(hbcd - rqdx.width) / 2,
								top : -(hbkd - rqdx.height) / 2
							});
					$("#jxls li > input").click(function() {

								var left = $("#raphaelDiv").css("left");
								var top = $("#raphaelDiv").css("top");
								var jxlname = $(this).attr("name");
								var zxdx = hbcd / 2;
								var zxdy = hbkd / 2;
								for (var single in jxls) {
									if (single == jxlname) {
										var countryPath = jxls[jxlname];
										countryPath.attr({
													stroke : "#b4eea9",
													fill : "#22eed6",
													"fill-opacity" : 0.4,
													"stroke-opacity" : 0.25
												});
										var xy = countryPath.getBBox();
										var zxdleft = -((xy.x + xy.x2) / 2)
												+ 950 / 2;
										var zxdtop = -(xy.y + xy.y2) / 2 + 500
												/ 2;
										if (zxdleft < 950 - hbcd) {
											zxdleft = 950 - hbcd;
										}
										if (zxdleft > 0) {
											zxdleft = 0;
										}
										if (zxdtop < 500 - hbkd) {
											zxdtop = 500 - hbkd;
										}
										if (zxdtop > 0) {
											zxdtop = 0;
										}
										$("#raphaelDiv").animate({
													left : zxdleft,
													top : zxdtop
												}, "fast");
										var zxd = jxls[single].getBBox();
										cir != null && cir.remove();	
										cir = ra.circle(zxd.cx, zxd.cy, 6)
												.attr({
															fill : "red"
														});
									} else {
										jxls[single].attr({
													"fill-opacity" : 0
												});

										
									}

								}
							});
				})
	}
})();