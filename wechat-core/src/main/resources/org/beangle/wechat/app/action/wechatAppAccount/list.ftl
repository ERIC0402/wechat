[#ftl]
[@b.head/]
[@b.grid  items=wechatAccounts var="wechatAccount"]
	[@b.gridbar]
		bar.addItem("${b.text("action.new")}",action.add());
		bar.addItem("${b.text("action.edit")}",action.edit());
		bar.addItem("${b.text("action.delete")}",action.remove());
		bar.addItem("查看接入URI",action.openNewLayer("${(b.url('!lookUrl'))!}", "50%", "28.25%", "wechatAccount.id", false, "wechatAccountID"));
		bar.addItem("微信菜单管理",action.targetMethod('${(b.url('/core/wechat-menu'))!}', 'wechat', '', false));
		bar.addItem("微信关键字管理",action.targetMethod('${(b.url('/core/wechat-key'))!}', 'wechat', '', false));
		bar.addItem("微信素材管理",action.targetMethod('${(b.url('/core/wechat-article'))!}', 'wechat', '', false));
		bar.addItem("微信文章管理",action.targetMethod('${(b.url('/article/wechat-article'))!}', 'wechat', '', false));
	[/@]
	[@b.row]
		[@b.boxcol/]
		[@b.col width="25%" property="wechatName" title="微信名" align="center"/]
		[@b.col width="25%" property="wechatNumber" title="微信号" align="center"/]
		[@b.col width="25%" property="publicType.name" title="公众号类型" align="center"/]
		[@b.col width="25%" property="token" title="token" align="center"/]
		[@b.col width="25%" property="appid" title="appid" align="center"/]
		[@b.col width="25%" property="secret" title="secret" align="center"/]
		[@b.col width="25%" property="status" title="状态" align="center"]
			[@c.enabled wechatAccount.status /]
		[/@]
	[/@]
[/@]
[@b.foot/]
