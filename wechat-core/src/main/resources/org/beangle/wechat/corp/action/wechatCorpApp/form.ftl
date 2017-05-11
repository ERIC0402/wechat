[#ftl]
[@b.head/]
[@b.toolbar title="公众号管理" entityId=wechatCorpApp.id!0]bar.addBack();[/@]
[@b.form action="!save" title="公众号管理" theme="list"]
	[@b.textfield label="应用名" name="wechatCorpApp.wechatCorpAppName" value="${wechatCorpApp.wechatCorpAppName!}" style="width:200px;" maxlength="50" required="true"/]
	[@b.textfield label="token" name="wechatCorpApp.token" value="${wechatCorpApp.token!}" style="width:200px;" maxlength="50" required="true"/]
	[@b.textfield label="agentId" name="wechatCorpApp.agentid" value="${wechatCorpApp.agentid!}" style="width:200px;" maxlength="150" required="true"/]
	[@b.textfield label="secret" name="wechatCorpApp.secret" value="${wechatCorpApp.secret!}" style="width:200px;" maxlength="150" required="true"/]
	[@b.textfield label="encodingAESKey" name="wechatCorpApp.encodingAESKey" value="${wechatCorpApp.encodingAESKey!}" style="width:200px;" maxlength="150" required="true"/]
	[@b.radios label="状态" name="wechatCorpApp.status" value=(wechatCorpApp.status)!1 items={'1':'启用','0':'禁用'} style="width:200px;" required="true"/]
	[@b.formfoot]
		[@b.reset/]&nbsp;&nbsp;
		[@b.submit value="action.submit"/]
		[@b.redirectParams/]
		<input type="hidden" name="wechatCorpApp.id" value="${wechatCorpApp.id!}" />
		<input type="hidden" name="wechatCorpApp.wechatAccount.id" value="${wechatCorpApp.wechatAccount.id!}" />
	[/@]
[/@]
[@b.foot/]