(function (){
	var GPY = window.GPY = {};
	GPY.init = function (opts){
		if(opts.field){
			var td = $(opts.field).find("td:eq(1)");
			td.append('<div class="imgsDiv"></div><input id="gpyBtn" type="button" value="使用高拍仪扫描纸质材料" />');
		}
		$("#gpyBtn").click(function (){
			$.colorbox({
				title:"上传纸材料",
				overlayClose:false,
				iframe : true,
				width : "620px",
				height : "80%",
				href : opts.href,
				onClosed:function (){
				}
			});
		});
	}
	GPY.addImgs = function(imgs){
		$.colorbox.close();
		var divs = $(".imgsDiv");
		for(var i in imgs){
			var filePath = imgs[i];
			var div = $('<div class="imgDiv"><img src="#" /><input class="delIpt" type="button" value="删除"/><input type="hidden" name="filePath" class="filePath"/></div>');
			div.appendTo(divs);
			div.find("img").attr("src", base + "/common/download.action?file="+filePath);
			div.find(".filePath").val(filePath);
			div.find(".delIpt").click(function (){
				$(this).parent().remove();
			});
		}
	}
})();