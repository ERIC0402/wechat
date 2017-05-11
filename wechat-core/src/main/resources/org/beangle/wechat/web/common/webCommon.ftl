[#ftl]
[#macro jsApi config="" jsApiList=""]
[#if config != "" && jsApiList != ""]
<script type="text/javascript" src="${base}/static/scripts/wechat/jweixin/jweixin-1.1.0.js"></script>
<script>
	wx.config({
	    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	    appId: '${(config.appid)!}', // 必填，公众号的唯一标识
	    timestamp: ${(config.timestamp)!}, // 必填，生成签名的时间戳
	    nonceStr: '${(config.nonceStr)!}', // 必填，生成签名的随机串
	    signature: '${(config.signature)!}',// 必填，签名，见附录1
	    jsApiList: ${jsApiList!} // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	});
	
	wx.ready(function(){
		wx.checkJsApi({
		    jsApiList: ${jsApiList!}, // 需要检测的JS接口列表，所有JS接口列表见附录2,
		    fail: function(res) {
		      	alert(res.errMsg);
		    }
		});
		[#nested]
	});
	
	wx.error(function (res) {
	  alert(res.errMsg);
	});
</script>
[/#if]
[/#macro]
