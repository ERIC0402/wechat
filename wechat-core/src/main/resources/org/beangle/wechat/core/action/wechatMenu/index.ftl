[#ftl]
[@b.head/]
	[@b.form action="!search" title="ui.searchForm" target="wechatMenulist" theme="search"]
		[@b.textfields names="wechatButton.name;按钮名"/]
		<input type="hidden" name="wechatButton.wechatAccount.id" value="${Parameters['wechatAccount.id']!}" />
		<input type="hidden" name="wechatButton.wechatCorpApp.id" value="${Parameters['wechatCorpApp.id']!}" />
		<input type="hidden" name="wechatCorpApp.wechatAccount.id" value="${Parameters['wechatCorpApp.wechatAccount.id']!}" />
	[/@]
	[@b.div href="!search?wechatButton.wechatAccount.id=${Parameters['wechatAccount.id']!}&wechatButton.wechatCorpApp.id=${Parameters['wechatCorpApp.id']!}&wechatCorpApp.wechatAccount.id=${Parameters['wechatCorpApp.wechatAccount.id']!}" id="wechatMenulist" class="dataList"/]
[@b.foot/]
