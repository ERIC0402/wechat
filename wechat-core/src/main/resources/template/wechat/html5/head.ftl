[#ftl]
<!DOCTYPE html>
<html>
  <head>
    <title>${tag.title!}</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, maximum-scale=1.0, initial-scale=1.0 user-scalable=no">
    <script type="text/javascript">window.contextPath = "${base!}";var base = "${base!}", JSESSIONID = "${(Session['id'])!}";var tophref = location.href;</script>
    [#--<script type="text/javascript" src="${base}/static/scripts/comm/jquery-1.10.2.min.js"></script>--]
	<script type="text/javascript" src="${base}/static/scripts/jquery-1.10.2/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/struts/js/plugins/jquery.subscribe.js"></script>
    <script type="text/javascript" src="${base}/static/scripts/comm/jquery.struts2-3.1.0.min.js"></script>
    <script type="text/javascript" src="${base}/static/scripts/wechat/beangle/view/js/wechat.js"></script>
    <script type="application/javascript" src="${base}/static/scripts/fastclick/fastclick.js"></script>
    ${tag.body}
  </head>
  <body>
