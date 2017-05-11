[#ftl]
[@b.head/]
	[@b.form action="!search" title="ui.searchForm" target="wechatTemplatelist" theme="search"]
		[@b.textfields names="wechatTemplate.templateName;模板名称"/]
		<input type="hidden" name="wechatTextNewsTemplate.wechatAccount.id" value="${wechatAccountID!}" />
	[/@]
	[@b.div href="!search?wechatTextNewsTemplate.wechatAccount.id=${(wechatAccountID)!}" id="wechatTemplatelist" class="dataList"/]
[@b.foot/]
