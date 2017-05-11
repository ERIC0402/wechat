package org.beangle.wechat.timetask.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.beangle.model.persist.impl.BaseServiceImpl;
import org.beangle.wechat.timetask.service.SchedulerService;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

public class SchedulerServiceImpl extends BaseServiceImpl implements SchedulerService {

	private Scheduler scheduler;

	public void start() throws SchedulerException {
		scheduler.start();
	}

	public void pauseTrigger(CronTrigger trigger) throws SchedulerException {
		scheduler.pauseTrigger(trigger.getName(), trigger.getGroup());
	}

	public void resumeTrigger(CronTrigger trigger) throws SchedulerException {
		scheduler.resumeTrigger(trigger.getName(), trigger.getGroup());
	}

	public void rescheduleJob(CronTrigger trigger) throws SchedulerException {
		scheduler.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
	}

	public boolean buildTrigger(String triggerName, String jobName, Object bean, String targetMethod, String cronExpression) {
		try {
			MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
			jobDetail.setName(jobName);
			jobDetail.setTargetObject(bean);
			jobDetail.setTargetMethod(targetMethod);
			jobDetail.setConcurrent(false);
			jobDetail.afterPropertiesSet();

			CronTriggerBean cronTrigger = new CronTriggerBean();
			cronTrigger.setName(triggerName);
			cronTrigger.setGroup(Scheduler.DEFAULT_GROUP);
			cronTrigger.setCronExpression(cronExpression);
			cronTrigger.afterPropertiesSet();
			// 往调度器添加触发器任务
			scheduler.scheduleJob((JobDetail) jobDetail.getObject(), cronTrigger);
			return true;
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return false;
	}

	public Trigger getTrigger(String triggerName, String groupName) {
		Trigger trigger = null;
		if (StringUtils.isBlank(groupName)) {
			logger.warn("Schedule Job Group is empty!");
			return null;
		}
		if (StringUtils.isBlank(triggerName)) {
			logger.warn("Schedule trigger Name is empty!");
			return null;
		}
		try {
			trigger = scheduler.getTrigger(triggerName, groupName);
		} catch (SchedulerException e) {
			logger.warn("Fail to get the trigger (triggerName: " + triggerName + ", groupName : " + groupName + ")");
			return null;
		}
		if (trigger == null) {
			logger.warn("Can not found the trigger of triggerName: " + triggerName + ", groupName : " + groupName);
		}
		return trigger;
	}

	public boolean removeTrigger(String jobName, String triggerName) {
		try {
			scheduler.pauseTrigger(triggerName, Scheduler.DEFAULT_GROUP);// 停止触发器
			// 移除触发器  删除任务
			if(scheduler.unscheduleJob(triggerName, Scheduler.DEFAULT_GROUP) && scheduler.deleteJob(jobName, Scheduler.DEFAULT_GROUP)){
				return true;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

}
