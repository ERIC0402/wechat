[#ftl]
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
  [#if tag.parameters['isZoom']?? && tag.parameters['isZoom'] == 1]
  	  <meta name="viewport" content="width=device-width, initial-scale=1" />
  [#else]
	  <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0" /> 
  [/#if]
	  <meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
  <link rel="shortcut icon" type="image/x-icon" href="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/images/icon/common/favicon22c41b.ico" /> 
  <meta name="apple-mobile-web-app-capable" content="yes" /> 
  <meta name="apple-mobile-web-app-status-bar-style" content="black" /> 
  <meta name="format-detection" content="telephone=no" /> 
  <title>${(tag.title)!'微信'}</title> 
  <script type="text/javascript" src="${base}/static/scripts/comm/jquery-latest.js"></script>
  <link rel="stylesheet" type="text/css" href="${base}/static/scripts/mobile/page.css" /> 
  <link rel="stylesheet" type="text/css" href="${base}/static/scripts/mobile/page_mp_article.css" /> 
 <script type="text/javascript">window.contextPath = "${base!}";var base = "${base!}", JSESSIONID = "${(Session['id'])!}";var tophref = location.href;</script>
 <script type="text/javascript" src="${base}/static/scripts/wechat/beangle/view/js/wechat.js"></script>
 ${tag.body}
 </head> 
 <body> 
 
  <div id="activity-detail" class="zh_CN mm_appmsg not_in_mm" ontouchstart="">
  <div id="js_article" class="rich_media"> 
   <div class="rich_media_inner"> 
