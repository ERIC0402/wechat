[#ftl]
[@b.head/]
	[@b.form action="!search" title="ui.searchForm" target="wechatKeylist" theme="search"]
		[@b.textfields names="wechatKey.keyWord;关键字"/]
		<input type="hidden" name="wechatAccount.id" value="${Parameters['wechatAccount.id']!}" />
		<input type="hidden" name="wechatCorpApp.id" value="${Parameters['wechatCorpApp.id']!}" />
		<input type="hidden" name="wechatCorpApp.wechatAccount.id" value="${Parameters['wechatCorpApp.wechatAccount.id']!}" />
	[/@]
	[@b.div href="!search?wechatAccount.id=${Parameters['wechatAccount.id']!}&wechatCorpApp.id=${Parameters['wechatCorpApp.id']!}&wechatCorpApp.wechatAccount.id=${Parameters['wechatCorpApp.wechatAccount.id']!}" id="wechatKeylist" class="dataList"/]
[@b.foot/]
