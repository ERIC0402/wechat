[#ftl]
[@b.head/]
[@b.grid  items=wechatAccounts var="wechatAccount"]
	[@b.gridbar]
		bar.addItem("${b.text("action.new")}",action.add());
		bar.addItem("${b.text("action.edit")}",action.edit());
		bar.addItem("${b.text("action.delete")}",action.remove());
		bar.addItem("微信应用管理",action.targetMethod('${(b.url('wechat-corp-app'))!}', 'wechat', '', false));
	[/@]
	[@b.row]
		[@b.boxcol/]
		[@b.col width="20%" property="wechatName" title="微信名" align="center"/]
		[@b.col width="20%" property="wechatNumber" title="微信号" align="center"/]
		[@b.col width="20%" property="appid" title="corpid" align="center"/]
		[@b.col width="20%" title="微信企业应用" align="center"]
			[#list wechatAccount.wechatCorpApps as wechatCorpApp]
				${(wechatCorpApp.wechatCorpAppName)!}
				[#if wechatCorpApp_has_next]
					<br />
				[/#if]
			[/#list]
		[/@]
		[@b.col width="20%" property="status" title="状态" align="center"]
			[@c.enabled wechatAccount.status /]
		[/@]
	[/@]
[/@]
[@b.foot/]
