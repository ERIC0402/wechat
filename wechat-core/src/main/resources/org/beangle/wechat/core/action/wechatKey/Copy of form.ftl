[#ftl]
[@b.head/]
[@b.toolbar title="微信关键字管理" entityId=wechatKey.id!0]bar.addBack();[/@]
[@b.form action="!save" title="微信关键字管理" theme="list"]
	[@b.textfield label="关键字" name="wechatKey.keyWord" value="${wechatKey.keyWord!}" style="width:200px;" maxlength="50" required="true" /]
	[@b.radios id="type" label="消息类型" name="msgType" value=(wechatKey.wechatTextNewsTemplate.msgType)!'text' items={'text':'文本','news':'图文','sessionKey':'session关键字', 'action':'控制器'} style="width:200px;" required="true" /]
	[@b.field label="文本模板" required="true"]
		<select id="textTemplate" name="wechatTextTemplate.id" style="width:200px">
			<option value="" >请选择...</option>
			[#list wechatTextTemplates as wechatTextTemplate]
				<option [#if (wechatKey.wechatTextNewsTemplate)?? && wechatTextTemplate==wechatKey.wechatTextNewsTemplate]selected[/#if] value="${(wechatTextTemplate.id)!}">${(wechatTextTemplate.templateName)!}</option>
			[/#list]
		</select>
	[/@]
	[@b.field label="session关键字模板" required="true"]
		<select id="sessionKey" name="wechatSessionTemplate.id" style="width:200px">
			<option value="" >请选择...</option>
			[#list wechatSessionTemplates as wechatTextTemplate]
				<option [#if (wechatKey.wechatTextNewsTemplate)?? && wechatTextTemplate==wechatKey.wechatTextNewsTemplate]selected[/#if] value="${(wechatTextTemplate.id)!}">${(wechatTextTemplate.templateName)!}</option>
			[/#list]
		</select>
	[/@]
	[@b.field label="控制器" required="true"]
		<select id="actionName" name="wechatActionTemplate.id" style="width:200px">
			<option value="" >请选择...</option>
			[#list wechatActionTemplates as wechatTextTemplate]
				<option [#if (wechatKey.wechatTextNewsTemplate)?? && wechatTextTemplate==wechatKey.wechatTextNewsTemplate]selected[/#if] value="${(wechatTextTemplate.id)!}">${(wechatTextTemplate.templateName)!}</option>
			[/#list]
		</select>
	[/@]
	[@b.field label="图文模板" required="true"]
		<select id="newsTemplate" name="wechatNewsTemplate.id" style="width:200px">
			<option value=""　>请选择...</option>
			[#list wechatNewsTemplates as wechatNewsTemplate]
				<option [#if (wechatKey.wechatTextNewsTemplate)?? && wechatNewsTemplate==wechatKey.wechatTextNewsTemplate]selected[/#if] value="${(wechatNewsTemplate.id)!}">${(wechatNewsTemplate.templateName)!}</option>
			[/#list]
		</select>
	[/@]
	[@b.formfoot]
		[@b.reset/]&nbsp;&nbsp;
		[@b.submit value="action.submit" onsubmit="check"/]
		[@b.redirectParams/]
		<input type="hidden" name="wechatKey.wechatAccount.id" value="${Parameters['wechatAccount.id']!}" />
		<input type="hidden" name="wechatKey.wechatCorpApp.id" value="${Parameters['wechatCorpApp.id']!}" />
		<input type="hidden" name="wechatKey.id" value="${wechatKey.id!}" />
	[/@]
[/@]
[@b.foot/]
<script>
	if($("input[name='msgType']:checked").val() == 'text'){
		$("#newsTemplate").parent().parent().css("display", "none");
		$("#sessionKey").parent().parent().css("display", "none");
		$("#actionName").parent().parent().css("display", "none");
	}else if($("input[name='msgType']:checked").val() == 'news'){
		$("#textTemplate").parent().parent().css("display", "none");
		$("#sessionKey").parent().parent().css("display", "none");
		$("#actionName").parent().parent().css("display", "none");
	}else if($("input[name='msgType']:checked").val() == 'sessionKey'){
		$("#newsTemplate").parent().parent().css("display", "none");
		$("#textTemplate").parent().parent().css("display", "none");
		$("#actionName").parent().parent().css("display", "none");
	}else if($("input[name='msgType']:checked").val() == 'action'){
		$("#newsTemplate").parent().parent().css("display", "none");
		$("#textTemplate").parent().parent().css("display", "none");
		$("#sessionKey").parent().parent().css("display", "none");
	}
	$name("msgType").change(function(){
		if($(this).val() == 'text'){
			$("#textTemplate").parent().parent().removeAttr("style");
			$("#newsTemplate").parent().parent().css("display", "none");
			$("#sessionKey").parent().parent().css("display", "none");
			$("#actionName").parent().parent().css("display", "none");
			$("#newsTemplate").val('');
			$("#sessionKey").val('');
			$("#actionName").val('');
		}else if($(this).val() == 'news'){
			$("#newsTemplate").parent().parent().removeAttr("style");
			$("#textTemplate").parent().parent().css("display", "none");
			$("#sessionKey").parent().parent().css("display", "none");
			$("#actionName").parent().parent().css("display", "none");
			$("#textTemplate").val('');
			$("#sessionKey").val('');
			$("#actionName").val('');
		}else if($(this).val() == 'sessionKey'){
			$("#sessionKey").parent().parent().removeAttr("style");
			$("#textTemplate").parent().parent().css("display", "none");
			$("#newsTemplate").parent().parent().css("display", "none");
			$("#actionName").parent().parent().css("display", "none");
			$("#textTemplate").val('');
			$("#newsTemplate").val('');
			$("#actionName").val('');
		}else if($(this).val() == 'action'){
			$("#actionName").parent().parent().removeAttr("style");
			$("#textTemplate").parent().parent().css("display", "none");
			$("#newsTemplate").parent().parent().css("display", "none");
			$("#sessionKey").parent().parent().css("display", "none");
			$("#textTemplate").val('');
			$("#newsTemplate").val('');
			$("#sessionKey").val('');
		}
	});
	
	function check(){
		if($("input[name='msgType']:checked").val() == 'text'){
			if(!$("#textTemplate option:selected").val()){
				alert("请选择文本模板");
				return false;
			}
		}else if($("input[name='msgType']:checked").val() == 'sessionKey'){
			if(!$("#sessionKey option:selected").val()){
				alert("请选择session关键字模板");
				return false;
			}
		}else if($("input[name='msgType']:checked").val() == 'action'){
			if(!$("#actionName option:selected").val()){
				alert("请选择控制器关键字模板");
				return false;
			}
		}else{
			if(!$("#newsTemplate option:selected").val()){
				alert("请选择图文模板");
				return false;
			}
		}
	}
</script>