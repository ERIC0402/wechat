[#ftl]
[#--
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
  <meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
  <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" /> 
  <link rel="shortcut icon" type="image/x-icon" href="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/images/icon/common/favicon22c41b.ico" /> 
  <meta name="apple-mobile-web-app-capable" content="yes" /> 
  <meta name="apple-mobile-web-app-status-bar-style" content="black" /> 
  <meta name="format-detection" content="telephone=no" /> 
  <title>${(tag.title)!'微信'}</title> 
  <script type="text/javascript" src="${base}/static/scripts/comm/jquery-latest.js"></script>
  <link rel="stylesheet" type="text/css" href="${base}/static/scripts/mobile/page.css" /> 
  <link rel="stylesheet" type="text/css" href="${base}/static/scripts/mobile/page_mp_article.css" /> 
 </head> 
 <body id="activity-detail" class="zh_CN mm_appmsg not_in_mm" ontouchstart=""> 
  <div id="js_article" class="rich_media"> 
   <div class="rich_media_inner" style="text-align: center;"> 
--]
<!DOCTYPE html>
<html>
<head>
 [#if tag.parameters['isZoom']?? && tag.parameters['isZoom'] == 1]
 	<meta name="viewport" content="width=device-width, initial-scale=1" />
 [#else]
	<meta name="viewport" content="width=device-width, maximum-scale=1.0, initial-scale=1.0 user-scalable=no">
 [/#if]

<meta name="format-detection" content="telephone=no" />
<script type="text/javascript" src="${base}/static/scripts/comm/jquery-1.10.2.min.js"></script>
<script type="text/javascript">window.contextPath = "${base!}";var base = "${base!}", JSESSIONID = "${(Session['id'])!}";var tophref = location.href;</script>
<script type="text/javascript" src="${base}/static/scripts/wechat/beangle/view/js/wechat.js"></script>
<script>
	$(document).bind("mobileinit",function(){
		$.mobile.pageLoadErrorMessage = '页面加载错误！';
	});
</script>
[#if !tag.parameters['isHand']?? || tag.parameters['isHand'] != 1]
<link rel="stylesheet" href="${base}/static/scripts/jquery.mobile-1.4.5/jquery.mobile.flatui.css">
<script src="${base}/static/scripts/jquery.mobile-1.4.5/jquery.mobile-1.4.5.min.js"></script>
[/#if]
<title>${tag.title!}</title>
</head>
<body>