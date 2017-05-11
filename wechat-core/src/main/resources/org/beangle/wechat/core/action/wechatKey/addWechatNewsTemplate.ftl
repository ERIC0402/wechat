[#ftl]
[@b.head/]
<script src="${base}/static/scripts/uploadify/upload.js"></script>
<script src="${base}/static/scripts/uploadify/jquery.uploadify.v2.1.4.js"></script>
<script src="${base}/static/scripts/uploadify/swfobject.js"></script>
<link href="${base}/static/scripts/uploadify/uploadify.css" rel="stylesheet" type="text/css"/>
[@b.form action="!save" title="微信关键字管理" theme="list"]
	[@b.textfield label="标题" name="wechatNewsItem.title" value="${(wechatNewsItem.title)!}" style="width:200px;" maxlength="50" required="true"/]
	[@b.textarea id="content" label="描述" name="wechatNewsItem.description" value="${w.decode(wechatNewsItem.description)!}" style="width:200px;" maxlength="200"/]
	[@b.textfield label="URL" name="wechatNewsItem.url" value="${(wechatNewsItem.url)!}" style="width:200px;" maxlength="100"/]
	[@b.textfield label="顺序" name="wechatNewsItem.orders" value="${(wechatNewsItem.orders)!}" style="width:200px;" maxlength="2" required="true" check="match('integer')"/]
	[@b.field label="图文消息模板"]
		<img id="albumImg" src="${(b.url('/common/download'))!}?file=${(wechatNewsItem.imagePath)!}" width="160px" height="90px" style="display:none" />
		<input type="hidden" name="wechatNewsItem.imagePath" value="${(wechatNewsItem.imagePath)!}"/>
		<input id="pic_upload" name="picAdress" type="file" buttonText="上传图片" />
	[/@]
	[@b.formfoot]
		[@b.reset/]&nbsp;&nbsp;
		<input type="button" value="提交" onclick="addOreditTr('${(nameBefore)!}')" class="ip_6">
	[/@]
[/@]	
[@b.foot/]
<script>
	
	function addOreditTr(nameBefore){
		if(!$name("wechatNewsItem.title").val()){
			alert("标题不能为空");
			return;
		}
		if(!$name("wechatNewsItem.orders").val()){
			alert("顺序不能为空");
			return;
		}
		var isSameOrders = false;
		$("input[name*='orders']", parent.document).each(function(){
			if($name("wechatNewsItem.orders").val() == $(this).val() && $name("wechatNewsItem.orders").val() != "${(wechatNewsItem.orders)!'aa'}"){
				isSameOrders = true;
			}
		})
		if(isSameOrders){
			alert("顺序重复！请更换其他顺序");
			return;
		}
		if(isNaN($name("wechatNewsItem.orders").val()) || parseInt($name("wechatNewsItem.orders").val()) <= 0){
			alert("顺序请输入正整数");
			return;
		}
		/**if(!$name("wechatNewsItem.imagePath").val()){
			alert("图片不能为空");
			return;
		}*/
		if(nameBefore){
			parent.editTrStr($name("wechatNewsItem.title").val(), $name("wechatNewsItem.imagePath").val(), encodeURI($name("wechatNewsItem.description").val()), $name("wechatNewsItem.url").val(), $name("wechatNewsItem.orders").val(), ${(wechatNewsItem.id)!'null'}, nameBefore);
		}else{
			parent.addTrStr($name("wechatNewsItem.title").val(), $name("wechatNewsItem.imagePath").val(), encodeURI($name("wechatNewsItem.description").val()), $name("wechatNewsItem.url").val(), $name("wechatNewsItem.orders").val(), ${(wechatNewsItem.id)!'null'});
		}
	}
	
	function editTr(){
		
	}
	
	$(function (){
        uploadimg({
            dir:"/wechat/newsTempateImage",
            upBtn:"pic_upload",
            imgNmae:"wechatNewsItem.imagePath",
            showImg:"albumImg"
        },"false");
        
    });
    [#if (wechatNewsItem.imagePath)??]
		$("#albumImg").show();
	[/#if]
</script>