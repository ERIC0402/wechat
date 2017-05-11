[#ftl]
[@b.head/]
<script type="text/javascript" src="${base}/static/scripts/colorbox/jquery-colorbox-1.3.17.1.js"></script>
<link href="${base}/static/scripts/colorbox/colorbox.css" rel="stylesheet" type="text/css" />
[@b.toolbar title="微信关键字管理" entityId=wechatKey.id!0]bar.addBack();[/@]
<style>
	#newsTable{
		width:100%;
		word-break:break-all;
		border-collapse:collapse;
		text-align:center
	}
	#newsTable tr td{
		border:1px solid;
		border-color:#ccc;
		text-align:text-align;
	}
</style>
[@b.form action="!save" title="微信关键字管理" theme="list"]
	[@b.textfield label="关键字" name="wechatKey.keyWord" value="${wechatKey.keyWord!}" style="width:200px;" maxlength="50" required="true" /]
	[@b.radios id="type" label="消息类型" name="wechatTextNewsTemplate.msgType" value=(wechatKey.wechatTextNewsTemplate.msgType)!'text' items={'text':'文本','news':'图文','sessionKey':'session关键字', 'action':'控制器'} style="width:200px;" required="true" /]
	<input type="hidden" name="wechatTextNewsTemplate.id" value="${(wechatKey.wechatTextNewsTemplate.id)!}" />
	[@b.field label="文本" required="true"]
		<textarea id="content" title="内容" name="wechatTextNewsTemplate.content" rows="3" cols="50" style="width:500px;" maxlength="200">${(wechatKey.wechatTextNewsTemplate.content)!}</textarea>
	[/@]
	[@b.field label="session关键字" required="true"]
		<input id="sessionKey" name="wechatTextNewsTemplate.sessionKey" style="width:500px;" maxlength="200" value="${(wechatKey.wechatTextNewsTemplate.sessionKey)!}" />
	[/@]
	[@b.field label="控制器" required="true"]
		<input id="actionName" name="wechatTextNewsTemplate.actionName" style="width:500px;" maxlength="100" value="${(wechatKey.wechatTextNewsTemplate.actionName)!}" />
	[/@]
	[@b.field label="图文" required="true"]
		<table id="newsTable">
			<thead>
				<tr>
					<td width="16%">标题</td>
					<td width="16%">图片</td>
					<td width="16%">描述</td>
					<td width="16%">链接</td>
					<td width="16%">顺序</td>
					<td width="16%">操作</td>
				</tr>
			</thead>
			<tbody id="newsTbody">
				[#list (wechatKey.wechatTextNewsTemplate.newsItems?sort_by('orders'))! as newsItem]
					<tr>
						<td><input type="hidden" name="wechatNewsItem${(newsItem_index)!}.title" value="${(newsItem.title)!}" /><span>${(newsItem.title)!}</span></td>
						<td><input type="hidden" name="wechatNewsItem${(newsItem_index)!}.imagePath" value="${(newsItem.imagePath)!}" /><img src="${(b.url('/common/download'))!}?file=${(newsItem.imagePath)!}" width="160px" height="90px"></td>
						<td><input type="hidden" name="wechatNewsItem${(newsItem_index)!}.description" value="${(newsItem.description)!}" /><span>${(newsItem.description?replace('\n', '<br />'))!}</span</td>
						<td><input type="hidden" name="wechatNewsItem${(newsItem_index)!}.url" value="${(newsItem.url)!}" /><span>${(newsItem.url)!}</span</td>
						<td><input type="hidden" name="wechatNewsItem${(newsItem_index)!}.orders" value="${(newsItem.orders)!}" /><span>${(newsItem.orders)!}</span</td>
						<td><input type="hidden" name="wechatNewsItem${(newsItem_index)!}.id" value="${(newsItem.id)!}" /><input type="button" value="修改" onclick="editTr('${(newsItem.title)!}', '${(newsItem.imagePath)!}', '${w.encode(newsItem.description)!}', '${(newsItem.url)!}', '${(newsItem.orders)!}', '${(newsItem.id)!}', 'wechatNewsItem${(newsItem_index)!}')" /><input type="button" value="删除" onclick="removeTr(this)" /></td>
					</tr>
				[/#list]
			</tbody>
			<tfoot>
				<tr>
					<td colspan="6" ><input type="button" id="tj" value="添加" onclick="addTr()" style="width:128px" /></td>
				</tr>
			</tfoot>	
		</table>
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
	if($("input[name='wechatTextNewsTemplate.msgType']:checked").val() == 'text'){
		$("#newsTable").parent().parent().css("display", "none");
		$("#sessionKey").parent().parent().css("display", "none");
		$("#actionName").parent().parent().css("display", "none");
	}else if($("input[name='wechatTextNewsTemplate.msgType']:checked").val() == 'news'){
		$("#content").parent().parent().css("display", "none");
		$("#sessionKey").parent().parent().css("display", "none");
		$("#actionName").parent().parent().css("display", "none");
	}else if($("input[name='wechatTextNewsTemplate.msgType']:checked").val() == 'sessionKey'){
		$("#newsTable").parent().parent().css("display", "none");
		$("#content").parent().parent().css("display", "none");
		$("#actionName").parent().parent().css("display", "none");
	}else if($("input[name='wechatTextNewsTemplate.msgType']:checked").val() == 'action'){
		$("#newsTable").parent().parent().css("display", "none");
		$("#content").parent().parent().css("display", "none");
		$("#sessionKey").parent().parent().css("display", "none");
	}
	$name("wechatTextNewsTemplate.msgType").change(function(){
		if($(this).val() == 'text'){
			$("#content").parent().parent().removeAttr("style");
			$("#newsTable").parent().parent().css("display", "none");
			$("#sessionKey").parent().parent().css("display", "none");
			$("#actionName").parent().parent().css("display", "none");
			$("#newsTable").val('');
			$("#sessionKey").val('');
			$("#actionName").val('');
		}else if($(this).val() == 'news'){
			$("#newsTable").parent().parent().removeAttr("style");
			$("#content").parent().parent().css("display", "none");
			$("#sessionKey").parent().parent().css("display", "none");
			$("#actionName").parent().parent().css("display", "none");
			$("#content").val('');
			$("#sessionKey").val('');
			$("#actionName").val('');
		}else if($(this).val() == 'sessionKey'){
			$("#sessionKey").parent().parent().removeAttr("style");
			$("#content").parent().parent().css("display", "none");
			$("#newsTable").parent().parent().css("display", "none");
			$("#actionName").parent().parent().css("display", "none");
			$("#content").val('');
			$("#newsTable").val('');
			$("#actionName").val('');
		}else if($(this).val() == 'action'){
			$("#actionName").parent().parent().removeAttr("style");
			$("#content").parent().parent().css("display", "none");
			$("#newsTable").parent().parent().css("display", "none");
			$("#sessionKey").parent().parent().css("display", "none");
			$("#content").val('');
			$("#newsTable").val('');
			$("#sessionKey").val('');
		}
	});
	
	var trSize= ${(wechatKey.wechatTextNewsTemplate.newsItems?size)!}-1;
	
	function check(){
		if($("input[name='wechatTextNewsTemplate.msgType']:checked").val() == 'news'){
			if($("#newsTbody").children().size() < 1){
				alert("请添加图文消息模板");
				return false;
			}
			$("#newsTbody").append("<input type='hidden' name='trSize' value='" + trSize + "'>");
		}else if($("input[name='wechatTextNewsTemplate.msgType']:checked").val() == 'text'){
			if(!$("#content").val()){
				alert("请填写文本消息内容");
				return false;
			}
		}else if($("input[name='wechatTextNewsTemplate.msgType']:checked").val() == 'action'){
			if(!$("#actionName").val()){
				alert("请填写控制器类名");
				return false;
			}
		}else if($("input[name='wechatTextNewsTemplate.msgType']:checked").val() == 'sessionKey'){
			if(!$("#sessionKey").val()){
				alert("请填写session关键字");
				return false;
			}
		}
	}
	
	function addTr(){
		redirectBox();
	}
	
	function editTr(title, imagePath, description, url , orders, wechatNewsItemID, nameBefore){
		redirectBox("wechatNewsItem.title=" + title + "&wechatNewsItem.imagePath=" + imagePath + "&wechatNewsItem.description=" + description + "&wechatNewsItem.url=" + url + "&wechatNewsItem.orders=" + orders + "&wechatNewsItem.id=" + wechatNewsItemID + "&nameBefore=" + nameBefore);
	}
	
	function removeTr(btn){
		$(btn).parent().parent().remove();
	}
	
	function redirectBox(params){
		$.colorbox({
			iframe : true,
			width : "1000px",
			height : "562.5px",
			href : "${(b.url('!addWechatNewsTemplate'))!}?" + params
		});
	}
	
	function closeColorbox() {
		$.colorbox.close();
	}
	
	function editTrStr(title, imagePath, description, url, orders, wechatNewsItemID, nameBefore){
		if(nameBefore){
			if(title){
				$name(nameBefore + ".title").val(title);
				$name(nameBefore + ".title").next().text(title);
			}
			if(imagePath){
				$name(nameBefore + ".imagePath").val(imagePath);
				$name(nameBefore + ".imagePath").next().attr("src", "${(b.url('/common/download'))!}?file=" + imagePath);
			}
			if(description){
				$name(nameBefore + ".description").val(decodeURI(description));
				$name(nameBefore + ".description").next().html($("<span>" + decodeURI(description) + "</span>").text().replace(/\n/g, "<br />"));
			}
			if(url){
				$name(nameBefore + ".url").val(url);
				$name(nameBefore + ".url").next().text(url);
			}
			if(orders){
				$name(nameBefore + ".orders").val(orders);
				$name(nameBefore + ".orders").next().text(orders);
			}
			$name(nameBefore + ".orders").parent().next().children().next().eq(0).attr("onclick", "editTr('" + title + "', '" + imagePath + "', '" + description + "', '" + url + "', '" + orders + "', '" + wechatNewsItemID + "', '" + nameBefore + "')");
		}
		closeColorbox();
	}
	
	function addTrStr(title, imagePath, description, url, orders, wechatNewsItemID){
		trSize++;
		var addStr = "<tr>" +
						"<td><input type='hidden' name='wechatNewsItem" + trSize + ".title' value='" + title + "' /><span>" + title + "</span></td>" +
						"<td><input type='hidden' name='wechatNewsItem" + trSize + ".imagePath' value='" + imagePath + "' /><img src='${(b.url('/common/download'))!}?file=" + imagePath + "' width='160px' height='90px'></td>" +
						"<td><input type='hidden' name='wechatNewsItem" + trSize + ".description' value='" + decodeURI(description) + "' /><span>" + $("<span>" + decodeURI(description) + "</span>").text().replace(/\n/g, "<br />") + "<span></td>" +
						"<td><input type='hidden' name='wechatNewsItem" + trSize + ".url' value='" + url + "' /><span>" + url + "<span></td>" +
						"<td><input type='hidden' name='wechatNewsItem" + trSize + ".orders' value='" + orders + "' /><span>" + orders + "<span></td>" +
						"<td><input type='hidden' name='wechatNewsItem" + trSize + ".id' value='" + wechatNewsItemID + "' /><input type='button' value='修改' onclick='editTr(\"" + title + "\", \"" + imagePath + "\", \"" + description + "\", \"" + url + "\", \"" + orders + "\", \""+ wechatNewsItemID + "\", \"wechatNewsItem" + trSize + "\")' /><input type='button' value='删除' onclick='removeTr(this)' /></td>" +
					"</tr>";
		$("#newsTbody").append(addStr);
		closeColorbox();
	}
</script>