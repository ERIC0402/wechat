[#ftl]
[@b.head/]
	[@b.form action="!search" title="ui.searchForm" target="wechatSourcelist" theme="search"]
		[@b.textfields names="wechatSource.fileName;文件名"/]
		<input type="hidden" name="wechatAccountID" value="${wechatAccountID!}" />
	[/@]
	[@b.div href="!search?wechatAccountID=${(wechatAccountID)!}" id="wechatSourcelist" class="dataList"/]
[@b.foot/]
