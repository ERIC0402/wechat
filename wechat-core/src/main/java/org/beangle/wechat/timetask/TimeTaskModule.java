package org.beangle.wechat.timetask;

import org.beangle.spring.bind.AbstractBindModule;
import org.beangle.spring.bind.Scope;
import org.beangle.wechat.timetask.action.TimeTaskAction;
import org.beangle.wechat.timetask.service.impl.SchedulerServiceImpl;
import org.beangle.wechat.timetask.service.impl.TimeTaskServiceImpl;

/**
 * 定时器绑定
 * 作者：王政
 * 创建时间：2015年9月6日 下午12:53:18
 */
public class TimeTaskModule extends AbstractBindModule {

	@Override
	protected void doBinding() {
		bind(TimeTaskAction.class).in(Scope.PROTOTYPE);
		bind(TimeTaskServiceImpl.class, SchedulerServiceImpl.class).shortName();
	}

}
