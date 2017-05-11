package org.beangle.wechat.timetask.service.impl;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.beangle.model.persist.impl.BaseServiceImpl;
import org.beangle.model.util.EntityUtils;
import org.beangle.wechat.timetask.model.TimeTask;
import org.beangle.wechat.timetask.service.SchedulerService;
import org.beangle.wechat.timetask.service.TimeTaskService;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class TimeTaskServiceImpl extends BaseServiceImpl implements ApplicationContextAware, TimeTaskService {

	private SchedulerService scheduleService;

	private ApplicationContext applicationContext;

	public boolean buildTimeTask(TimeTask timeTask) {
		String beanName = timeTask.getTargetClass();
		if (StringUtils.isNotBlank(beanName)) {
			try {
				String shortName = EntityUtils.getCommandName(beanName);
				Class<?> targetClass = Class.forName(timeTask.getTargetClass());
				if (isTypeMatch(beanName, targetClass)) {
					return scheduleService.buildTrigger(timeTask.getTriggerName(), timeTask.getJobName(), applicationContext.getBean(beanName, targetClass), timeTask.getTargetMethod(), timeTask.getCronExpression());
				} else if (isTypeMatch(shortName, targetClass)) {
					return scheduleService.buildTrigger(timeTask.getTriggerName(), timeTask.getJobName(), applicationContext.getBean(shortName, targetClass), timeTask.getTargetMethod(), timeTask.getCronExpression());
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean isTypeMatch(String name, Class<?> targetType) {
		try {
			return applicationContext.isTypeMatch(name, targetType);
		} catch (Exception ee) {
			return false;
		}
	}

	@Override
	public CronTrigger getTrigger(String triggerName) {
		return (CronTrigger) scheduleService.getTrigger(triggerName, Scheduler.DEFAULT_GROUP);
	}

	@Override
	public boolean startTriggerOrStopTrigger(TimeTask timeTask, boolean isStart) {
		try {
			CronTrigger trigger = getTrigger(timeTask.getTriggerName());
			if (trigger != null && updateCronExpression(timeTask)) {
				if (isStart) {
					scheduleService.resumeTrigger(trigger);
					logger.info("trigger the start successfully!!");
				} else {
					scheduleService.pauseTrigger(trigger);
					logger.info("trigger the pause successfully!!");
				}
				return true;
			}
		} catch (SchedulerException e) {
			logger.error("Fail to reschedule. " + e);
		}
		return false;
	}

	@Override
	public boolean updateCronExpression(TimeTask timeTask) {
		String cronExpression = timeTask.getCronExpression();
		try {
			CronTrigger trigger = getTrigger(timeTask.getTriggerName());
			if (trigger == null) {
				return false;
			}
			if (StringUtils.equals(trigger.getCronExpression(), cronExpression)) {
				logger.info("cronExpression is same with the running Schedule , no need to update.");
				return true;
			}
			trigger.setCronExpression(cronExpression);
			scheduleService.rescheduleJob(trigger);
			logger.info("Update the cronExpression successfully!!");
			return true;
		} catch (ParseException e) {
			logger.error("The new cronExpression - " + cronExpression + " not conform to the standard. " + e);
			return false;
		} catch (SchedulerException e) {
			logger.error("Fail to reschedule. " + e);
			return false;
		}
	}

	@Override
	public boolean removeTimeTask(TimeTask timeTask) {
		if (StringUtils.isNotBlank(timeTask.getTriggerName()) && StringUtils.isNotBlank(timeTask.getTargetClass()) && StringUtils.isNotBlank(timeTask.getTargetMethod())) {
			if (getTrigger(timeTask.getTriggerName()) == null) {
				return true;
			}
			return scheduleService.removeTrigger(timeTask.getJobName(), timeTask.getTriggerName());
		}
		return false;
	}

	public void setSchedulerService(SchedulerService schedulerService) {
		this.scheduleService = schedulerService;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
