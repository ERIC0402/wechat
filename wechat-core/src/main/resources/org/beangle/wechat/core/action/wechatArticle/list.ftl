[#ftl]
[@b.head/]
<script type="text/javascript" src="${base}/static/scripts/comm/jquery.blockUI.js"/>
[@b.toolbar title="微信图文素材管理"]
	[#if (Parameters['wechatCorpApp.wechatAccount.id'])?? && Parameters['wechatCorpApp.wechatAccount.id']?length > 0]
		bar.addItem("${b.text("action.back")}", action.goTo2("${b.url('/corp/wechat-corp-app')}?wechatAccount.id=${Parameters['wechatCorpApp.wechatAccount.id']!}"), "ico_backward");
	[#else]
		bar.addItem("${b.text("action.back")}", action.goTo2("${b.url('/app/wechat-app-account')}"), "ico_backward");
	[/#if]
[/@]
[@b.grid  items=wechatArticles var="wechatArticle"]
	[@b.gridbar]
		bar.addItem("${b.text("action.new")}",action.add());
		bar.addItem("${b.text("action.edit")}",action.edit());
		bar.addItem("${b.text("action.delete")}",action.remove());
		bar.addItem("上传临时图文素材",action.openCurrent("uploadTempWechatServer","wechatAccountID","${(wechatAccountID)!}",true,true,null, false));
		bar.addItem("上传永久图文素材",action.openCurrent("uploadWechatServer","wechatAccountID","${(wechatAccountID)!}",true,true,null, false));
		bar.addItem("发送预览",action.openCurrent("sendPreview","wechatAccountID","${(wechatAccountID)!}",true,true,null, false));
		bar.addItem("群发",action.openCurrent("sendAll","wechatAccountID","${(wechatAccountID)!}",true,true,null, false));
		bar.addItem("阅读量查询",action.openCurrent("readNumber","wechatAccountID","${(wechatAccountID)!}",true,true,null, false));
	[/@]
	[@b.row]
		[@b.boxcol/]
		[@b.col width="33%" property="wechatArticleItem.title" title="标题" align="center"/]
		[@b.col width="33%" property="wechatArticleItem.author" title="作者" align="center"/]
		[@b.col width="33%" property="mediaId" title="状态" align="center"]
			[#if wechatArticle.mediaId??]
				<span style="color:green">上传成功</span>
			[#else]
				<span style="color:red">未上传</span>
			[/#if]
		[/@]
	[/@]
[/@]
[@b.foot/]
