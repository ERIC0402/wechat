[#ftl]
[@b.head]
	<script src="${base}/static/scripts/uploadify/upload.js"></script>
	<script src="${base}/static/scripts/uploadify/jquery.uploadify.v2.1.4.js"></script>
	<script src="${base}/static/scripts/uploadify/swfobject.js"></script>
	<link href="${base}/static/scripts/uploadify/uploadify.css" rel="stylesheet" type="text/css"/>
[/@]
[@b.toolbar title="微信素材管理" entityId=wechatSource.id!0]bar.addBack();[/@]
[@b.form action="!save" title="微信素材管理" theme="list"]
	[@b.select label="文件类型" name="wechatSource.type.id" value=(wechatSource.type.id)! style="width:200px;" items=types required="true" option="id,name"/]
	[@b.field label="素材" required="true"]
		<p><img id="albumImg" src="${(b.url('/common/download'))!}?file=${(wechatSource.filePath)!}" width="160px" height="90px" style="display:none" /></p>
		<input type="hidden" name="wechatSource.filePath" value="${(wechatSource.filePath)!}"/>
		<input type="hidden" name="wechatSource.fileName" value="${(wechatSource.fileName)!}"/>
		<input id="pic_upload" name="picAdress" type="file" buttonText="上传图片" />
	[/@]
	[@b.formfoot]
		[@b.submit value="action.submit" onsubmit="check"/]
		[@b.redirectParams/]
		<input type="hidden" name="wechatSource.wechatAccount.id" value="${wechatAccountID!}" />
		<input type="hidden" name="wechatSource.id" value="${wechatSource.id!}" />
	[/@]
[/@]
[@b.foot/]
<script>

	function check(){
		if(!$("input[name='wechatSource.filePath']").val()){
			alert("请上传文件");
			return false;
		}
	}
	
	$(function (){
        uploadimg({
            dir:"/wechat/uploadSource",
            upBtn:"pic_upload",
            imgName:"wechatSource.filePath",
            fileName:"wechatSource.fileName",
            showImg:"albumImg"
        },"false");
        
    });
    
    [#if (wechatSource.filePath)??]
		$("#albumImg").show();
	[/#if]
	
</script>