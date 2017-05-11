[#ftl]
[@b.head/]
[@b.toolbar title="微信菜单管理" entityId=wechatButton.id!0]bar.addBack();[/@]
[@b.form action="!save" title="微信菜单管理" theme="list"]
	[@b.textfield label="菜单名称" name="wechatButton.name" value="${wechatButton.name!}" style="width:200px;" maxlength="50" required="true"/]
	[@b.select id="type" label="菜单类型" name="wechatButton.event.id" value=(wechatButton.event.id)! style="width:200px;" items=events option="id,name"/]
	[@b.field label="菜单关键字"]
		<input id="menuKey" type="text" name="wechatButton.menuKey" value="${wechatButton.menuKey!}" style="width:200px" maxlength="50"/>
	[/@]
	[#assign paramstr = '' /]
	[#assign params = (wechatButton.url?split("?")[1]?split("&"))!]
	[#list params! as param]
		[#if param?index_of('WEB_AUTHORITY_TOKEN=') != -1 || param?index_of('WEB_CORP_APP_AUTHORITY_TOKEN=') != -1]
			[#assign isAutority = 1 /]
		[#elseif param?index_of('WEB_JS_API=') != -1 || param?index_of('WEB_CORP_JS_API=') != -1]
			[#assign isAccountID = 1 /]
		[#else]
			[#assign paramstr]${paramstr + "&" +  param}[/#assign]
		[/#if]
	[/#list]
	[#assign menu_url]
		[#if params?size > 0]
			[#if paramstr?length > 0]
				${wechatButton.url?substring(0, wechatButton.url?index_of('?') + 1) + paramstr?substring(1)!}
			[#else]
				${wechatButton.url?substring(0, wechatButton.url?index_of('?'))!}
			[/#if]
		[#else]
			${wechatButton.url!}
		[/#if]
	[/#assign]
	[@b.field label="菜单URL"]
		<input id="url" type="text" name="wechatButton.url" value="${menu_url?trim!}" style="width:200px" maxlength="150"/>
	[/@]
	[@b.radios label="获取用户信息" name="isAuthority" value=(isAutority)!0 items={'1':'是','0':'否'} style="width:200px;" required="true"/]
	[@b.radios label="获取JsApi" name="isAccountID" value=(isAccountID)!0 items={'1':'是','0':'否'} style="width:200px;" required="true"/]
	[@b.select label="上级菜单" name="wechatButton.parentButton.id" value=(wechatButton.parentButton.id)! style="width:200px;" items=wechatButtons/]
	[@b.textfield label="菜单顺序" name="wechatButton.orders" value="${wechatButton.orders!}" style="width:200px;" maxlength="1" required="true" check="match('integer').range(1,5)"/]
	[@b.radios label="状态" name="wechatButton.status" value=(wechatButton.status)!1 items={'1':'有效','0':'无效'} style="width:200px;" required="true"/]
	[@b.formfoot]
		[@b.reset/]&nbsp;&nbsp;
		[@b.submit value="action.submit" onsubmit="check" /]
		[@b.redirectParams/]
		<input type="hidden" name="wechatButton.wechatAccount.id" value="${(wechatButton.wechatAccount.id)!}" />
		<input type="hidden" name="wechatButton.wechatCorpApp.id" value="${(wechatButton.wechatCorpApp.id)!}" />
		<input type="hidden" name="wechatButton.id" value="${wechatButton.id!}" />
	[/@]
[/@]
[@b.foot/]
<script>
	
	var wechatAccountID = ${(wechatButton.wechatAccount.id)!'undefined'};
	var wechatCorpAppID = ${(wechatButton.wechatCorpApp.id)!'undefined'};
	function check(){
		if($("#type option:selected").text() == '网页链接类'){
			var url = $("#url").val();
			if($("input[name='isAuthority']:checked").val()=='1'){
				if(url.indexOf("?") > -1){
					url += "&"
				}else{
					url += "?"
				}
				if(wechatAccountID){
					url += "WEB_AUTHORITY_TOKEN=" + wechatAccountID;
				}else if(wechatCorpAppID){
					url += "WEB_CORP_APP_AUTHORITY_TOKEN=" + wechatCorpAppID;
				}
			}
			if($("input[name='isAccountID']:checked").val()=='1'){
				if(url.indexOf("?") > -1){
					url += "&"
				}else{
					url += "?"
				}
				if(wechatAccountID){
					url += "WEB_JS_API=" + wechatAccountID;
				}else if(wechatCorpAppID){
					url += "WEB_CORP_JS_API=" + wechatCorpAppID;
				}
			}
			$("#url").val(url);
		}
	}
	
	if($("#type option:selected").text() == '网页链接类'){
		$("#menuKey").parent().parent().css("display", "none");
	}else if( $("#type option:selected").text() == '消息触发类' ||
			$("#type option:selected").text() == '扫码推事件' ||
			$("#type option:selected").text() == '扫码推事件（提示框）' ||
			$("#type option:selected").text() == '系统拍照发图' ||
			$("#type option:selected").text() == '拍照或者相册发图' ||
			$("#type option:selected").text() == '微信相册发图'){
		$("#url").parent().parent().css("display", "none");
		$name("isAuthority").parent().parent().css("display", "none");
		$name("isAccountID").parent().parent().css("display", "none");
	}else{
		$("#menuKey").parent().parent().css("display", "none");
		$("#url").parent().parent().css("display", "none");
		$name("isAuthority").parent().parent().css("display", "none");
		$name("isAccountID").parent().parent().css("display", "none");
	}
	
	$("#type").change(function(){
		if( $(this).find("option:selected").text() == '消息触发类' ||
			$(this).find("option:selected").text() == '扫码推事件' ||
			$(this).find("option:selected").text() == '扫码推事件（提示框）' ||
			$(this).find("option:selected").text() == '系统拍照发图' ||
			$(this).find("option:selected").text() == '拍照或者相册发图' ||
			$(this).find("option:selected").text() == '微信相册发图'){
			$("#menuKey").parent().parent().removeAttr("style");
			$("#url").parent().parent().css("display", "none");
			$name("isAuthority").parent().parent().css("display", "none");
			$name("isAccountID").parent().parent().css("display", "none");
			$("#url").val('');
		}else if($(this).find("option:selected").text() == '网页链接类'){
			$("#url").parent().parent().removeAttr("style");
			$name("isAuthority").parent().parent().removeAttr("style");
			$name("isAccountID").parent().parent().removeAttr("style");
			$("#menuKey").parent().parent().css("display", "none");
			$("#menuKey").val('');
		}else{
			$("#menuKey").parent().parent().css("display", "none");
			$("#url").parent().parent().css("display", "none");
			$name("isAuthority").parent().parent().css("display", "none");
			$name("isAccountID").parent().parent().css("display", "none");
			$("#menuKey").val('');
			$("#url").val('');
		}
	});
</script>