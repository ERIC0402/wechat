$(".ui-li-divider").live('click', function() {
	var $group = $(this);
	$("#select-restaurants").children("optgroup").each(function() {
		if ($group.text() == $(this).attr("label")) {
			var selected = true;
			if ($(this).attr("select")) {
				selected = false;
				$(this).removeAttr("select");
			} else {
				$(this).attr("select", true);
			}
			$(this).children().each(function() {
				$(this).attr("selected", selected)
			});
			$("#select-restaurants").selectmenu("refresh")
		}
	});
});