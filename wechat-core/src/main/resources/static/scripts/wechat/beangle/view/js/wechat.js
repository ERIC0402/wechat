(function(window) {
	if (wechat)
		return;
	var wechat = function() {
		return true;
	};
	wechat.extend = function(map) {
		for (attr in map) {
			var attrs = attr.split("."), obj = wechat, i;
			for (i = 0; i < attrs.length - 1; i++) {
				obj[attrs[i]] = obj[attrs[i]] || {};
				obj = obj[attrs[i]];
			}
			obj[attrs[attrs.length - 1]] = map[attr];
		}
	}
	window.wechat = wechat;
	window.w = wechat;

	wechat.alert=function(msg){
		if("undefined" != typeof bootbox) {
			bootbox.alert(msg);
		}else {
			alert(msg);
		}
	}
	wechat.confirm=function(confirmMsg,fn){
		if(null!=confirmMsg && ''!=confirmMsg){
			if("undefined" != typeof bootbox) {
				bootbox.confirm(confirmMsg, function(result) {
					if(fn) {
						if(result){
							fn();
						}
					}
				});
			}else {
				if(confirm(confirmMsg)) {
					fn();
				}
			}
		}else{
			fn();
		}
	}
	
	wechat.extend({
		go : function(target, url, callback) {
			if(target && url) {
				$(target).html('<div style="position: fixed; top: 50%; width: 100%; text-align: center; z-index: 99;"><img src="' + base + '/static/scripts/ueditor/themes/default/images/loading.gif" style="margin: 0 auto;"/></div>');
				$(target).load(url, function(){
					if(callback) {
						callback();
					}
				});
			}
		}
	});
	
	wechat.extend({
		form : {
			submit : function(myForm, action, target, onsubmit, callback, isJson) {
				if($(".subloading").size() > 0 && $(".subloading").css("display") != "none") {
					return;
				}
				wechat.form.loading('body');
				var rs;
				if(typeof myForm == 'string') {
					myForm = $(myForm);
					if(myForm && !(action && target)) {
						myForm = $(myForm)[0];
					}
				}
				if(!action) {
					action = myForm.action;
				}
				if(!target) {
					target = myForm.target;
				}
				if (myForm.onsubmit) {
					rs = null;
					try {
						rs = myForm.onsubmit();
					} catch (e) {
						alert(e);
						return;
					}
					if (rs == false) {
						return;
					}
				}
				if(isJson) {
					$.ajax({
						url : action,
						type : 'post',
						dataType: 'json',
						data: $(myForm).serialize(),
						success : function(data) {
							wechat.form.closeLoading();
							if(callback) {
								callback(data);
							}
						}
					});
				}else {
					$.ajax({
						url : action,
						type : 'post',
						data: $(myForm).serialize(),
						success : function(html) {
							wechat.form.closeLoading();
							$(target).html(html);
							if(callback) {
								callback();
							}
						}
					});
				}
			},
			loading : function(target) {
				if($(".subloading").size() < 1) {
					$(target).append('<div class="subloading loadTag" style="position: fixed; top: 40%; width: 100%; text-align: center; z-index: 99;"><img src="' + base + '/static/scripts/loading/images/loading.gif" style="margin: 0 auto;padding: 22px;background: #000;opacity: 0.7;border-radius: 5px;"/></div>');
				}else {
					$(".subloading").show();
				}
			},
			closeLoading : function() {
				$(".subloading").hide();
			}
		}
	});
	
	wechat.extend({
		mobile : {
			load : function(url, page, nextPage, isBack, callback) {
				if($(".loading").size() > 0 && $(".loading").css("display") != "none") {
					return;
				}
				wechat.mobile.loading('body');
				
				if(!nextPage) {
					// 随机创建下一个加载页面
					var nextPage = "dynamicPage" + new Date().getTime();
					$(page).after("<div id='" + nextPage + "' class='dynamicPage dynamicPageCustom'></div>");
					nextPage = "#" + nextPage;
				}
				$(nextPage).hide();
				
				// 将页面标识传给下一个页面
				if(url.indexOf("?") < 0) {
					url += "?";
				}
				url += "&lastPage=" + page.substr(1) + "&" + "currentPage=" + nextPage.substr(1);
				// 加载下一个页面
				$(nextPage).load(url, function(){
					 setTimeout(function(){
						 if(callback) {
							 callback();
						 }
						 wechat.mobile.animate(page, nextPage, isBack);
						 wechat.mobile.closeLoading();
					 }, 100);
				});
			},
			animate : function(page, nextPage, isBack, callback) {
				// 页面点击事件失效
				$("body").css("pointer-events", "none");
				
				jQuery.struts2_jquery.requireCss("/static/scripts/slider/css/slider.css", base);
				if(!isBack) {
					// 记录当前页的滚动条高度
					var $scrollInput = $(page + " #scrollHeight");
					if($scrollInput.size() < 1) {
						$(page).append("<input type='hidden' id='scrollHeight' />");
						$scrollInput = $(page + " #scrollHeight");
					}
					$scrollInput.val(document.body.scrollTop);
					
					$(nextPage).show();
					$(nextPage).addClass("animatestart sliderightin");
			        setTimeout(function(){ //动画结束时重置class
			        	$(page).hide();
		        		document.body.scrollTop = 0;
			        	$(nextPage).removeClass('animatestart sliderightin');
			        	$("body").css("pointer-events", "");
			        }, 400);
				}else {
					$(nextPage).show();
					$(page).addClass("animatestart sliderightout");
					
					// 返回时在下一页设置滚动条的高度等于之前记录的高度
	        		document.body.scrollTop = $(nextPage + " #scrollHeight").val();
			        setTimeout(function(){ //动画结束时重置class
			        	if(page.indexOf("dynamicPage") < 0) {
			        		$(page).hide();
			        		$(page).empty();
			        	}else {
			        		$(page).remove();
			        	}
			        	$(".loadTag").hide();
			        	$(page).removeClass('animatestart sliderightout');
			        	$("body").css("pointer-events", "");
			        }, 400);
				}
				if(callback) {
					callback();
				}
			},
			show : function(page, nextPage, isBack) {
				jQuery.struts2_jquery.requireCss("/static/scripts/slider/css/slider.css", base);
				if(!isBack) {
					var $scrollInput = $(page + " #scrollHeight");
					if($scrollInput.size() < 1) {
						$(page).append("<input type='hidden' id='scrollHeight' />");
						$scrollInput = $(page + " #scrollHeight");
					}
					$scrollInput.val(document.body.scrollTop);
					
					$(nextPage).show();
					$(nextPage).addClass("animatestart sliderightin");
					
			        setTimeout(function(){ //动画结束时重置class
			        	$(page).css({'display' : 'none'}); 
		        		document.body.scrollTop = 0;
				        $(nextPage).removeClass('animatestart sliderightin');
			        }, 300);
				}else {
					$(nextPage).show();
					$(page).addClass("animatestart sliderightout");
					document.body.scrollTop = $(nextPage + " #scrollHeight").val();
			        setTimeout(function(){ //动画结束时重置class
				        $(page).css({'display' : 'none'});
				        $(page).removeClass('animatestart sliderightout');
			        }, 300);
				}
			},
			loading : function(target) {
				if($(".loading").size() < 1) {
					$(target).append('<div class="loading loadTag" style="position: fixed; top: 40%; width: 100%; text-align: center;"><img src="' + base + '/static/scripts/loading/images/loading.gif" style="margin: 0 auto;padding: 22px;background: #000;opacity: 0.7;border-radius: 5px;"/></div>');
				}else {
					$(".loading").show();
				}
			},
			closeLoading : function() {
				$(".loading").hide();
			},
			lookImg : function(ele) {
				if(ele && "100%" != ele.style.width) {
					ele.style.width = "100%";
					ele.style.height = "100%";
					ele.style.position = "fixed";
					ele.style.top = 0;
					ele.style.left = 0;
					ele.style.zIndex=999999;
					ele.style.margin = 0;
				}else {
					ele.style.removeProperty("width");
					ele.style.removeProperty("height");
					ele.style.removeProperty("position");
					ele.style.removeProperty("top");
					ele.style.removeProperty("left");
					ele.style.removeProperty("z-index");
					ele.style.removeProperty("margin");
				}
			}
		}
	});
})(window);

$name = function(name) {
	return $("[name='" + name + "']");
}