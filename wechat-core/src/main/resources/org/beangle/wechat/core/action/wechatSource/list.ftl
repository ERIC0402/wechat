[#ftl]
[@b.head/]
<script type="text/javascript" src="${base}/static/scripts/comm/jquery.blockUI.js"/>
[@b.toolbar title="微信素材管理"]
	bar.addItem("${b.text("action.back")}", action.goTo2("${b.url('wechat-account')}"), "ico_backward");
[/@]
[@b.grid  items=wechatSources var="wechatSource"]
	[@b.gridbar]
		bar.addItem("${b.text("action.new")}", action.method('edit', '', 'wechatAccountID=${(wechatAccountID)!}'), "ico_add");
		bar.addItem("${b.text("action.edit")}",action.single('edit', '', 'wechatAccountID=${(wechatAccountID)!}'));
		bar.addItem("${b.text("action.delete")}",action.remove());
		bar.addItem("上传永久素材",action.openCurrent("uploadWechatServer","wechatAccountID","${(wechatAccountID)!}",true,true,null, false));
	[/@]
	[@b.row]
		[@b.boxcol/]
		[@b.col width="50%" property="mediaId" title="文件名" align="center"/]
		[@b.col width="50%" property="type.name" title="文件类型" align="center"/]
	[/@]
[/@]
[@b.foot/]
