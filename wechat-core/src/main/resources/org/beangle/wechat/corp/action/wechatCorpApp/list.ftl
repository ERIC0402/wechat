[#ftl]
[@b.head/]
[@b.toolbar title="微信企业应用管理"]
	bar.addItem("${b.text("action.back")}", action.goTo2("${b.url('wechat-corp-account')}"), "ico_backward");
[/@]
[@b.grid  items=wechatCorpApps var="wechatCorpApp"]
	[@b.gridbar]
		bar.addItem("${b.text("action.new")}",action.add());
		bar.addItem("${b.text("action.edit")}",action.edit());
		bar.addItem("${b.text("action.delete")}",action.remove());
		bar.addItem("查看接入URI",action.openNewLayer("${(b.url('/app/wechat-app-account!lookUrl'))!}", "50%", "28.25%", "wechatCorpApp.id", false, "wechatCorpAppID"));
		bar.addItem("微信菜单管理",action.targetMethod('${(b.url('/core/wechat-menu'))!}', 'wechat', '', false));
		bar.addItem("微信关键字管理",action.targetMethod('${(b.url('/core/wechat-key'))!}', 'wechat', '', false));
		//bar.addItem("微信素材管理",action.targetMethod('${(b.url('/core/wechat-article'))!}', 'wechat', '', false));
	[/@]
	[@b.row]
		[@b.boxcol/]
		[@b.col width="25%" property="wechatCorpAppName" title="微信企业应用名" align="center"/]
		[@b.col width="25%" property="token" title="token" align="center"/]
		[@b.col width="25%" property="agentid" title="agentid" align="center"/]
		[@b.col width="25%" property="secret" title="secret" align="center"/]
		[@b.col width="25%" property="status" title="状态" align="center"]
			[@c.enabled wechatCorpApp.status /]
		[/@]
	[/@]
[/@]
[@b.foot/]
