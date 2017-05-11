[#ftl/]
[#if tag.id??]
[#if tag.hasActionMessages()]
	<div id="${tag.id}" data-role="popup" class="ui-content" data-transition="pop" data-position-to="window">
		<p>
         [#list tag.actionMessages as message]
			${message!}
		 [/#list]
		</p>
	 </div>
[/#if]
[#if tag.hasActionErrors()]
	<div id="${tag.id}" data-role="popup" class="ui-content" data-transition="pop" data-position-to="window">
		<p>
         [#list tag.actionErrors as message]
			${message!}
		 [/#list]
		</p>
	 </div>
[/#if]
<script>
	$(function(){
        setTimeout(function(){
        	$("#${tag.id}").popup();
            $("#${tag.id}").popup("open");
            setTimeout(function(){ 
            	$("#${tag.id}").popup("close"); 
        	},2000);   
        },500); 
	});
</script>
[/#if]