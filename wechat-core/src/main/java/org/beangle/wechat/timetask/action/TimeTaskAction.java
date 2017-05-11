package org.beangle.wechat.timetask.action;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.beangle.model.Entity;
import org.beangle.website.common.action.BaseCommonAction;
import org.beangle.wechat.timetask.model.TimeTask;
import org.beangle.wechat.timetask.service.TimeTaskService;
import org.quartz.CronTrigger;

/**
 * 定时器管理 
 * 作者：王政 
 * 创建时间：2015年9月6日 下午2:59:58
 */
public class TimeTaskAction extends BaseCommonAction {

	private TimeTaskService timeTaskService;

	@Override
	protected String getEntityName() {
		return TimeTask.class.getName();
	}

	public String start() {
		boolean isStart = getBool("isStart");
		String message = "操作失败！";
		Long[] ids = getEntityIds();
		List<TimeTask> timeTasks = entityDao.get(TimeTask.class, ids);
		if (timeTasks != null && timeTasks.size() > 0) {
			message = "启用";
			if (!isStart) {
				message = "禁用";
			}
			int success = 0;
			for (TimeTask timeTask : timeTasks) {
				if (timeTaskService.getTrigger(timeTask.getTriggerName()) != null) {
					if (timeTaskService.startTriggerOrStopTrigger(timeTask, isStart)) {
						timeTask.setStatus(isStart);
						entityDao.saveOrUpdate(timeTask);
						success++;
					}
				} else {
					if (isStart) {
						if (timeTaskService.buildTimeTask(timeTask)) {
							timeTask.setStatus(isStart);
							entityDao.saveOrUpdate(timeTask);
							success++;
						}
					}
				}
			}
			if (success != timeTasks.size()) {
				if (success != 0 && timeTasks.size() > 1) {
					message = "部分" + message + "失败";
				} else {
					message += "失败";
				}
			} else {
				message += "成功";
			}
		}
		return redirect("search", message);
	}

	@Override
	protected String saveAndForward(Entity<?> entity) {
		TimeTask timeTask = (TimeTask) entity;
		TimeTask oldTimeTask = populateEntity(TimeTask.class, "oldTimeTask");
		String message = "info.save.success";
		if (!checkCronExpression(timeTask.getCronExpression())) {
			message = "时间表达式不正确";
			put("timeTask", timeTask);
			return forward("edit", message);
		}
		if (StringUtils.isNotBlank(oldTimeTask.getTriggerName()) && StringUtils.isNotBlank(oldTimeTask.getTargetClass()) && StringUtils.isNotBlank(oldTimeTask.getTargetMethod()) && !StringUtils.equals(oldTimeTask.getTriggerName(), timeTask.getTriggerName()) && !StringUtils.equals(oldTimeTask.getTargetClass(), timeTask.getTargetClass()) && !StringUtils.equals(oldTimeTask.getTargetMethod(), timeTask.getTargetMethod())) {
			if (!timeTaskService.removeTimeTask(oldTimeTask)) {
				return redirect("search", "操作失败！");
			}
		}
		if (timeTask.isStatus()) {
			if (timeTaskService.getTrigger(timeTask.getTriggerName()) != null) {
				if (!timeTaskService.startTriggerOrStopTrigger(timeTask, true)) {
					message = "启用失败！";
					timeTask.setStatus(false);
				}
			} else {
				if (!timeTaskService.buildTimeTask(timeTask)) {
					message = "启用失败！";
					timeTask.setStatus(false);
				}
			}
		} else {
			if (timeTaskService.getTrigger(timeTask.getTriggerName()) != null && !timeTaskService.startTriggerOrStopTrigger(timeTask, false)) {
				message = "禁用失败！";
				timeTask.setStatus(true);
			}
		}
		saveOrUpdate(timeTask);
		return redirect("search", message);
	}

	@Override
	protected String removeAndForward(Collection<?> entities) {
		@SuppressWarnings("unchecked")
		List<TimeTask> timeTasks = (List<TimeTask>) entities;
		String message = "info.remove.success";
		int success = 0;
		for (TimeTask timeTask : timeTasks) {
			if (timeTaskService.removeTimeTask(timeTask)) {
				success++;
			}
		}
		if (success != timeTasks.size()) {
			if (success != 0 && timeTasks.size() > 1) {
				message = "部分";
			}
			message += "删除失败";
		}
		try {
			remove(entities);
		} catch (Exception e) {
			logger.info("removeAndForwad failure", e);
			message = "info.delete.failure";
		}
		return redirect("search", message);
	}

	public boolean checkCronExpression(String cronExpression) {
		CronTrigger trigger = new CronTrigger();
		try {
			trigger.setCronExpression(cronExpression);
			return true;
		} catch (ParseException e) {
		}
		return false;
	}

	public void setTimeTaskService(TimeTaskService timeTaskService) {
		this.timeTaskService = timeTaskService;
	}

}
