package org.beangle.wechat.timetask.listener;

import java.util.List;

import org.beangle.model.persist.EntityDao;
import org.beangle.wechat.timetask.model.TimeTask;
import org.beangle.wechat.timetask.service.SchedulerService;
import org.beangle.wechat.timetask.service.TimeTaskService;

/**
 * 初始化定时器任务
 * 
 * 作者：王政
 * 创建时间：2015年9月6日 下午2:18:21
 */
public class InitTimeTaskListener{
	
	private TimeTaskService timeTaskService;
	
	private SchedulerService scheduleService;
	
	private EntityDao entityDao;

	public void init() {
		try {
			List<TimeTask> timeTasks = entityDao.get(TimeTask.class, "status", true);
			if(timeTasks != null && timeTasks.size() > 0){
				for (TimeTask timeTask : timeTasks) {
					timeTask.setStatus((timeTaskService.buildTimeTask(timeTask)));
				}
				scheduleService.start();
				entityDao.saveOrUpdate(timeTasks);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTimeTaskService(TimeTaskService timeTaskService) {
		this.timeTaskService = timeTaskService;
	}

	public void setScheduleService(SchedulerService scheduleService) {
		this.scheduleService = scheduleService;
	}

	public void setEntityDao(EntityDao entityDao) {
		this.entityDao = entityDao;
	}
}
