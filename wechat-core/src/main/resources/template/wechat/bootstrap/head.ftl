[#ftl]
[#if !(request.getHeader('x-requested-with')??) && !Parameters['x-requested-with']??]
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, maximum-scale=1.0, initial-scale=1.0 user-scalable=no">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>${tag.title!}</title>
    <!-- Bootstrap -->

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    	<script type="text/javascript">window.contextPath = "${base!}";var base = "${base!}", JSESSIONID = "${(Session['id'])!}";var tophref = location.href;</script>
	    [#--<script type="text/javascript" src="${base}/static/scripts/comm/jquery-1.10.2.min.js"></script>--]
		<script type="text/javascript" src="${base}/static/scripts/jquery-1.10.2/jquery.min.js"></script>
		<link href="${base}/static/scripts/bootstrap-3.3.5/css/bootstrap.min.css" rel="stylesheet">
	    <script src="${base}/static/scripts/bootstrap-3.3.5/js/bootstrap.min.js"></script>
	    <script src="${base}/static/scripts/bootbox/bootbox.js"></script>
	    <script>
	    	$('.dropdown-toggle').dropdown();
	    </script>
	    <script type="text/javascript" src="${base}/static/scripts/wechat/beangle/view/js/wechat.js"></script>
	    <script type="application/javascript" src="${base}/static/scripts/fastclick/fastclick.js"></script>
	    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <link href="${base}/static/scripts/font-awesome-4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <script type="text/javascript" src="${base}/struts/js/plugins/jquery.subscribe.js"></script>
	<script type="text/javascript" src="${base}/static/scripts/comm/jquery.struts2-3.1.0.min.js"></script>
  </head>
  <body [#if tag.parameters['bodyClass']??]class="${(tag.parameters['bodyClass'])!}" id="headbody"[/#if]>
  [/#if]
  <link rel="stylesheet" href="${base}/static/scripts/bootstrapvalidator-0.4.5/css/bootstrapValidator.min.css"/>
<script type="text/javascript" src="${base}/static/scripts/bootstrapvalidator-0.4.5/js/bootstrapValidator.min.js"></script>
  ${tag.body}
  [@w.messages theme="wechat/bootstrap" /]