[#ftl]
[@b.head/]
<script type="text/javascript">
	bg.ui.load("tabletree");
	defaultColumn=1;
</script>
[@b.toolbar title="微信菜单管理"]
	[#if (Parameters['wechatCorpApp.wechatAccount.id'])?? && Parameters['wechatCorpApp.wechatAccount.id']?length > 0]
		bar.addItem("${b.text("action.back")}", action.goTo2("${b.url('/corp/wechat-corp-app')}?wechatAccount.id=${Parameters['wechatCorpApp.wechatAccount.id']!}"), "ico_backward");
	[#else]
		bar.addItem("${b.text("action.back")}", action.goTo2("${b.url('/app/wechat-app-account')}"), "ico_backward");
	[/#if]
[/@]
[@b.grid  items=wechatButtons var="wechatButton" sortable="false"]
	[@b.gridbar]
		bar.addItem("${b.text("action.new")}",action.add());
		bar.addItem("${b.text("action.edit")}",action.edit());
		bar.addItem("${b.text("action.delete")}",action.remove());
		bar.addItem("${b.text("action.activate")}",action.multi("activateMenu", "", "isActivate=1"),'${b.theme.iconurl('actions/activate.png')}');
		bar.addItem("${b.text("action.freeze")}",action.multi("activateMenu", "", "isActivate=0"),'${b.theme.iconurl('actions/freeze.png')}');
		bar.addItem("获取微信服务器菜单",action.method("findWechatServerMenu", "获取将会设置现有菜单状态为禁用，确定获取到本地吗？"));
		bar.addItem("提交本地菜单",action.method("subWechatMenu", "确定提交菜单到微信吗？"));
	[/@]
	[@b.row]
		<tr id="${(wechatButton.code)!}">
		[@b.boxcol/]
		[@b.col width="20%" property="code" title="代码"]
			<div class="tier[#if !wechatButton.parentButton??]1[#else]2[/#if]" align="left">
				[#if wechatButton.childrenButtons?size == 0 ]
					<a href="#" class="doc"></a>
				[#else]
					<a href="#" class="folder_open" id="${(wechatButton.code)!}_folder" onclick="toggleRows(this)" >   </a>
				[/#if]
				${(wechatButton.code)!} ${(wechatButton.name)!}
			</div>
		[/@]
		[@b.col width="20%" property="name" title="按钮名称" align="center"/]
		[@b.col width="20%" property="event.name" title="按钮类型" align="center"/]
		[@b.col width="20%" property="parentButton.name" title="上级菜单" align="center"/]
		[@b.col width="20%" property="status" title="状态" align="center"]
			[@c.enabled wechatButton.status /]
		[/@]
		</tr>
	[/@]
[/@]
[@b.foot/]
