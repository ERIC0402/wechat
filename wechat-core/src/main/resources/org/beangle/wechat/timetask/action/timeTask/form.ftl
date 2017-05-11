[#ftl]
[@b.head/]
[@b.toolbar title="触发器管理" entityId=timeTask.id!0]bar.addBack();[/@]
[@b.form action="!save" title="触发器管理" theme="list"]
	[@b.textfield label="触发器名称" name="timeTask.triggerName" value="${timeTask.triggerName!}" style="width:200px;" maxlength="50" required="true"/]
	[@b.textfield label="类" name="timeTask.targetClass" value="${timeTask.targetClass!}" style="width:200px;" maxlength="100" required="true"/]
	[@b.textfield label="方法" name="timeTask.targetMethod" value="${timeTask.targetMethod!}" style="width:200px;" maxlength="50" required="true"/]
	[@b.textarea label="描述" name="timeTask.describe" value="${timeTask.describe!}" style="width:200px;" maxlength="100" required="true"/]
	[@b.textfield label="时间表达式" name="timeTask.cronExpression" value="${timeTask.cronExpression!}" style="width:200px;" maxlength="50" required="true"/]
	[@b.radios label="状态" name="timeTask.status" value=(timeTask.status)!1 items={'1':'启用','0':'禁用'} style="width:200px;" required="true"/]
	[@b.formfoot]
		[@b.reset/]&nbsp;&nbsp;
		[@b.submit value="action.submit"/]
		[@b.redirectParams/]
		<input type="hidden" name="oldTimeTask.triggerName" value="${timeTask.triggerName!}" />
		<input type="hidden" name="oldTimeTask.targetClass" value="${timeTask.targetClass!}" />
		<input type="hidden" name="oldTimeTask.targetMethod" value="${timeTask.targetMethod!}" />
		<input type="hidden" name="timeTask.id" value="${timeTask.id!}" />
	[/@]
[/@]
[@b.foot/]