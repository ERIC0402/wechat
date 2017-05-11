package org.beangle.wechat.timetask.service;

import org.beangle.wechat.timetask.model.TimeTask;
import org.quartz.CronTrigger;

/**
 * 定时器接口
 * 
 * 作者：王政
 * 创建时间：2015年9月6日 上午11:55:07
 */
public interface TimeTaskService {
	
	/**
	 * 构建定时器任务
	 * @param timeTask
	 * @return 
	 * @throws ClassNotFoundException 
	 */
	public boolean buildTimeTask(TimeTask timeTask);
	
	/**
	 * 获取触发器
	 * @param triggerName
	 * @return
	 */
	public CronTrigger getTrigger(String triggerName);
	
	/**
	 * 恢复或暂停触发器
	 * 
	 * @param timeTask
	 * @param start
	 * @return
	 */
	public boolean startTriggerOrStopTrigger(TimeTask timeTask, boolean isStart);
	
	/**
	 * 更新调度任务表达式
	 * 
	 * @param timeTask
	 * @return
	 */
	public boolean updateCronExpression(TimeTask timeTask);
	
	/**
	 * 删除定时器任务
	 * 
	 * @param timeTask
	 * @return
	 */
	public boolean removeTimeTask(TimeTask timeTask);

}
