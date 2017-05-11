[#ftl]
[#import "/org/beangle/wechat/ueditor/ueditor.ftl" as ue /]
[@b.head]
	<script src="${base}/static/scripts/uploadify/upload.js"></script>
	<script src="${base}/static/scripts/uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script src="${base}/static/scripts/uploadify/swfobject.js"></script>
	<link href="${base}/static/scripts/uploadify/uploadify.css" rel="stylesheet" type="text/css"/>
[/@]
[@b.toolbar title="微信素材管理" entityId=wechatArticle.id!0]bar.addBack();[/@]
[@b.form action="!save" title="微信素材管理" theme="list"]
	[@b.textfield label="标题" name="wechatArticleItem.title" value="${(wechatArticle.wechatArticleItem.title)!}" style="width:300px;" maxlength="100" required="true"/]
	[@b.field label="封面图片" required="true"]
		<p><img id="albumImg" src="${(b.url('/common/download'))!}?file=${(wechatArticle.wechatArticleItem.wechatSource.filePath)!}" width="500px" height="281.25px" style="display:none" /></p>
		<input type="hidden" name="wechatSource.id" value="${(wechatArticle.wechatArticleItem.wechatSource.id)!}"/>
		<input type="hidden" name="wechatSource.wechatAccount.id" value="${(wechatAccountID)!}"/>
		<input type="hidden" name="wechatSource.filePath" value="${(wechatArticle.wechatArticleItem.wechatSource.filePath)!}"/>
		<input type="hidden" name="wechatSource.fileName" value="${(wechatArticle.wechatArticleItem.wechatSource.fileName)!}"/>
		<input id="pic_upload" name="picAdress" type="file" buttonText="上传图片" />
	[/@]
	[@b.textfield label="作者" name="wechatArticleItem.author" value="${(wechatArticle.wechatArticleItem.author)!}" style="width:300px;" maxlength="32"/]
	[@b.textarea label="图文摘要" name="wechatArticle.digest" value="${(wechatArticle.digest)!}" style="width:100%;" maxlength="200" required="true"/]
	[@b.field label="内容" required="true"]
		[@ue.ueditorJs/]
		[@ue.ueditor id="cmsTemplate" name="wechatArticleItem.content" value="${(wechatArticle.wechatArticleItem.content)!}" maxlength="20000" height="230" /]
	[/@]
	[@b.textfield label="原文地址" name="wechatArticleItem.sourceUrl" value="${(wechatArticle.wechatArticleItem.sourceUrl)!}" style="width:300px;" maxlength="100"/]
	[@b.formfoot]
		[@b.submit value="action.submit" onsubmit="check"/]
		[@b.redirectParams/]
		<input type="hidden" name="wechatArticle.wechatAccount.id" value="${Parameters['wechatAccount.id']!}" />
		<input type="hidden" name="wechatArticle.wechatCorpApp.id" value="${Parameters['wechatCorpApp.id']!}" />
		<input type="hidden" name="wechatArticle.id" value="${(wechatArticle.id)!}" />
	[/@]
[/@]
[@b.foot/]
<style>
	.table_style_2 tr td.td_title_1{
		width : 10%;
	}
</style>
<script>

	function check(){
		if(!$("input[name='wechatSource.filePath']").val()){
			alert("请上传文件");
			return false;
		}
		if(!uecmsTemplate.getContent()){
			alert("请填写内容");
			return false;
		}
	}
	
	$(function (){
        uploadimg({
            dir:"/wechat/uploadArticle",
            upBtn:"pic_upload",
            imgName:"wechatSource.filePath",
            fileName:"wechatSource.fileName",
            showImg:"albumImg"
        },"false");
        
    });
    
    [#if (wechatArticle.wechatArticleItem.wechatSource.filePath)??]
		$("#albumImg").show();
	[/#if]
	
</script>