[#ftl/]
[#if tag.id??]
[#if tag.hasActionMessages()]
	<div id="${tag.id}" class="alert alert-warning alert-dismissible fade in" role="alert"style="position: fixed; width: 100%; z-index: 99;">
      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
      <p style="word-wrap: break-word; margin: 0 auto; ">
         [#list tag.actionMessages as message]
			${message!}
		 [/#list]
	 </p>
    </div>
[/#if]
[#if tag.hasActionErrors()]
	 <div id="${tag.id}" class="alert alert-warning alert-dismissible fade in" role="alert"style="position: fixed; width: 100%; z-index: 99;">
      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
      <p style="word-wrap: break-word; margin: 0 auto; ">
         [#list tag.actionErrors as message]
			${message!}
		 [/#list]
	  </p>
     </div>
[/#if]
[#if tag.parameters['message']??]
	<div id="${tag.id}" class="alert alert-warning alert-dismissible fade in" role="alert"style="position: fixed; width: 100%; z-index: 99;">
      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
      <p style="word-wrap: break-word; margin: 0 auto; ">
         ${(tag.parameters['message'])!}
	 </p>
    </div>
[/#if]
<script>
	$(function(){
		setTimeout(function(){
			$(".alert").alert('close');
		}, 2000);
	});
</script>
[/#if]