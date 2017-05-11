[#ftl]
[@b.head/]
[@b.grid  items=timeTasks var="timeTask"]
	[@b.gridbar]
		bar.addItem("${b.text("action.new")}",action.add());
		bar.addItem("${b.text("action.edit")}",action.edit());
		bar.addItem("${b.text("action.delete")}",action.remove());
		bar.addItem("启用", action.multi("start", "", "isStart=1"), '${b.theme.iconurl('actions/activate.png')}');
		bar.addItem("禁用", action.multi("start", "", "isStart=0"), '${b.theme.iconurl('actions/freeze.png')}');
	[/@]
	[@b.row]
		[@b.boxcol/]
		[@b.col width="20%" property="triggerName" title="触发器名称" align="center"/]
		[@b.col width="70%" property="describe" title="描述" align="center"/]
		[@b.col width="10%" property="status" title="状态" align="center"]
			[@c.enabled timeTask.status /]
		[/@]
	[/@]
[/@]
[@b.foot/]
