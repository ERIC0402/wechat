[#ftl]
[@b.head/]
[@b.toolbar title="微信关键字管理"]
	[#if (Parameters['wechatCorpApp.wechatAccount.id'])?? && Parameters['wechatCorpApp.wechatAccount.id']?length > 0]
		bar.addItem("${b.text("action.back")}", action.goTo2("${b.url('/corp/wechat-corp-app')}?wechatAccount.id=${Parameters['wechatCorpApp.wechatAccount.id']!}"), "ico_backward");
	[#else]
		bar.addItem("${b.text("action.back")}", action.goTo2("${b.url('/app/wechat-app-account')}"), "ico_backward");
	[/#if]
[/@]
[@b.grid  items=wechatKeys var="wechatKey"]
	[@b.gridbar]
		bar.addItem("${b.text("action.new")}",action.add());
		bar.addItem("${b.text("action.edit")}",action.edit());
		bar.addItem("${b.text("action.delete")}",action.remove());
		//bar.addItem("消息模板配置",action.targetMethod("${(b.url('wechat-template'))!}", "wechat", "", "", true));
	[/@]
	[@b.row]
		[@b.boxcol/]
		[@b.col width="50%" property="keyWord" title="关键字" align="center"/]
		[@b.col width="50%" property="wechatTextNewsTemplate.msgType" title="模板类型" align="center"]
			[#if (wechatKey.wechatTextNewsTemplate.msgType)?? && 'text' == wechatKey.wechatTextNewsTemplate.msgType]
				文本
			[#elseif (wechatKey.wechatTextNewsTemplate.msgType)?? && 'news' == wechatKey.wechatTextNewsTemplate.msgType]
				图文
			[#elseif (wechatKey.wechatTextNewsTemplate.msgType)?? && 'sessionKey' == wechatKey.wechatTextNewsTemplate.msgType]
				session关键字
			[#elseif (wechatKey.wechatTextNewsTemplate.msgType)?? && 'action' == wechatKey.wechatTextNewsTemplate.msgType]
				控制器
			[/#if] 
		[/@]
	[/@]
[/@]
[@b.foot/]
