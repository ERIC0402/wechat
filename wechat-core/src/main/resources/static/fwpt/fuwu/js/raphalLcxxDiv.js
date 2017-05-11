(function(){
window.jxls = {};
window.qjui = {};
window.dqname={};
window.IRaphael = {};
IRaphael.init = function (opts){
	var backgroundImg = opts.img;//base + "temp/lcxx/Img/0a23576b-155d-445a-9c10-fab1b5057adc.png"
	var hbkd=opts.hbkd;
	var hbgd=opts.hbgd;
	var transform=opts.transform;
	Raphael("raphaelLcxxDiv", hbkd, hbgd, function() {
	var r = this;
	var im=r.image(backgroundImg, 0, 0, hbkd,
			hbgd);
	im.attr("transform",transform);
	var over = function() {
		this.c = this.c || this.attr("fill");
		this.o=this.o||this.attr("fill-opacity");
		this.stop().animate({
					//fill : "#FFFF4A",
					"fill-opacity" : 0.5
				});
		this.attr("cursor","pointer");
		
	}, out = function() {
		this.stop().animate({
					//fill : this.c,
					"fill-opacity" : this.o
				});
	};
	r.setStart();
	var hue = Math.random();
	for (var country in yymap) {
		var name = yymap[country].key;
		var sfyzy=yymap[country].sfyzy;
		var jslb=yymap[country].jslb;
		var zws=yymap[country].zws;
		var jsmph=yymap[country].jsmph;
		var jsmc=yymap[country].jsmc;
		//闭包传参
	(function(name,sfyzy,jslb,zws,jsmph) {
			var jxl;
			
			if(sfyzy=="上课"){
				
				 jxl = r.path(yymap[country].value).attr({
					stroke : "#FFFF4A",
					fill : "#31a5ee",
					"fill-opacity" : 0.8,
					"stroke-opacity" : 0.25,
					title:"教室名称："+jsmc+" 教室状态："+sfyzy+" 座位数："+zws
				});

				var zxd=jxl.getBBox();
				name2= jsmph;
				var text = r.text(zxd.cx,zxd.cy,name2);
				text.insertBefore(jxl);
			}else if(sfyzy=="借用"){
				
				 jxl = r.path(yymap[country].value).attr({
					stroke : "#FFFF4A",
					fill : "#eee102",
					"fill-opacity" : 0.8,
					"stroke-opacity" : 0.25,
					title:"教室名称："+jsmc+" 教室状态："+sfyzy+" 座位数："+zws
				});
				var zxd=jxl.getBBox();
				name2= jsmph;
				var text = r.text(zxd.cx,zxd.cy,name2);
				text.insertBefore(jxl);
			}else if(sfyzy=="关闭"){
				
				 jxl = r.path(yymap[country].value).attr({
					stroke : "#FFFF4A",
					fill : "#ee791d",
					"fill-opacity" : 0.8,
					"stroke-opacity" : 0.25,
					title:"教室名称："+jsmc+" 教室状态："+sfyzy+" 座位数："+zws
				});
				var zxd=jxl.getBBox();
				name2= jsmph;
				var text = r.text(zxd.cx,zxd.cy,name2);
				text.insertBefore(jxl);
			}else {
				 jxl = r.path(yymap[country].value).attr({
					stroke : "#FFFF4A",
					fill : "#b4ee9a",
					"fill-opacity" : 0.8,
					"stroke-opacity" : 0.25,
					title:"教室名称："+jsmc+" 教室状态："+sfyzy+" 座位数："+zws

				});
				 var zxd=jxl.getBBox();
				 name2= jsmph;
				 var text = r.text(zxd.cx,zxd.cy,name2);
					text.insertBefore(jxl);
			}
			
			jxl.click(function() {
				
				
					dqname=name;
					zyzt=sfyzy;
					
						$name("dybtn").click();
					});
			jxls[name] = jxl;
		})(name,sfyzy,jslb,zws,jsmph);
	}
	
	var world = r.setFinish();
	world.hover(over, out);
	});
}
})();
