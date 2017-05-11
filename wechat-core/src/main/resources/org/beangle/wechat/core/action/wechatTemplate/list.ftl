[#ftl]
[@b.head/]
[@b.toolbar title="微信消息模板管理"]
	bar.addItem("${b.text("action.back")}", action.targetMethod("${b.url('wechat-key?wechatAccount.id=${(wechatAccountID)!}')}", "wechat", "", "", true), "ico_backward");
[/@]
[@b.grid  items=wechatTextNewsTemplates var="wechatTextNewsTemplate"]
	[@b.gridbar]
		bar.addItem("${b.text("action.new")}",action.add());
		bar.addItem("${b.text("action.edit")}",action.edit());
		bar.addItem("${b.text("action.delete")}",action.remove());
	[/@]
	[@b.row]
		[@b.boxcol/]
		[@b.col width="50%" property="templateName" title="模板名称" align="center"/]
		[@b.col width="50%" property="msgType" title="模板类型" align="center"]
			[#if (wechatTextNewsTemplate.msgType)?? && wechatTextNewsTemplate.msgType == 'text']
				文本
			[#elseif (wechatTextNewsTemplate.msgType)?? && wechatTextNewsTemplate.msgType == 'news']
				图文
			[#elseif (wechatTextNewsTemplate.msgType)?? && wechatTextNewsTemplate.msgType == 'sessionKey']
				session关键字
			[#elseif (wechatTextNewsTemplate.msgType)?? && wechatTextNewsTemplate.msgType == 'action']
				控制器
			[/#if] 
		[/@]
	[/@]
[/@]
[@b.foot/]
