[#ftl]
[@b.head/]
[@b.toolbar title="公众号管理" entityId=wechatAccount.id!0]bar.addBack();[/@]
[@b.form action="!save" title="公众号管理" theme="list"]
	[@b.textfield label="公众微信名" name="wechatAccount.wechatName" value="${wechatAccount.wechatName!}" style="width:200px;" maxlength="50" required="true"/]
	[#if publicType??]
		[@b.textfield label="公众号corpid" name="wechatAccount.appid" value="${wechatAccount.appid!}" style="width:200px;" maxlength="150" required="true"/]
		<input type="hidden" name="wechatAccount.wechatNumber" value="${wechatAccount.wechatNumber!}"/>
		<input type="hidden" name="wechatAccount.publicType.id" value="${publicType.id!}" />
	[#else]
		[@b.field id="wechatNumber" label="公众微信号" required="true" style="width:200px;"]
			<input name="wechatAccount.wechatNumber" value="${wechatAccount.wechatNumber!}" style="width:200px;" maxlength="50" />
		[/@]
		[@b.select id="publicType" label="公众号类型" name="wechatAccount.publicType.id" value=(wechatAccount.publicType.id)! style="width:200px;" items=publicTypes required="true" option="id,name"/]
		[@b.textfield label="公众号token" name="wechatAccount.token" value="${wechatAccount.token!}" style="width:200px;" maxlength="50" required="true"/]
		[@b.textfield label="公众号appid" name="wechatAccount.appid" value="${wechatAccount.appid!}" style="width:200px;" maxlength="150" required="true"/]
		[@b.textfield label="公众号secret" name="wechatAccount.secret" value="${wechatAccount.secret!}" style="width:200px;" maxlength="150" required="true"/]
	[/#if]
	[@b.radios label="状态" name="wechatAccount.status" value=(wechatAccount.status)!1 items={'1':'有效','0':'无效'} style="width:200px;" required="true"/]
	[@b.formfoot]
		[@b.reset/]&nbsp;&nbsp;
		[@b.submit value="action.submit" onsubmit="check"/]
		[@b.redirectParams/]
		<input type="hidden" name="wechatAccount.id" value="${wechatAccount.id!}" />
	[/@]
[/@]
[@b.foot/]
<script>
	function check(){
		[#if publicType??]
			$name("wechatAccount.wechatNumber").val($name("wechatAccount.appid").val());
		[/#if]
	}
</script>