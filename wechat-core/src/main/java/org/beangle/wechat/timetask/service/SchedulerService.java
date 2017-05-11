package org.beangle.wechat.timetask.service;

import org.quartz.CronTrigger;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

/**
 * 调度器接口
 * 
 * 作者：王政
 * 创建时间：2015年9月6日 上午10:50:10
 */
public interface SchedulerService {
	
	/**
	 * 启动调度器
	 * @throws SchedulerException
	 */
	public void start() throws SchedulerException;
	
	/**
	 * 构建触发器
	 * 
	 * @param triggerName
	 * @param jobName
	 * @param bean
	 * @param targetMethod
	 * @param cronExpression
	 * @return
	 */
	public boolean buildTrigger(String triggerName, String jobName, Object bean, String targetMethod, String cronExpression);
	
	/**
	 * 删除触发器
	 * @param jobName
	 * @param triggerName
	 */
	public boolean removeTrigger(String jobName, String triggerName);
	
	/**
	 * 获取触发器
	 * 
	 * @param triggerName
	 * @param groupName
	 * @return
	 */
	public Trigger getTrigger(String triggerName, String groupName);
	
	/**
	 * 暂停触发器
	 * @param trigger
	 * @throws SchedulerException
	 */
	public void pauseTrigger(CronTrigger trigger) throws SchedulerException;

	/**
	 * 恢复触发器
	 * @param trigger
	 * @throws SchedulerException
	 */
	public void resumeTrigger(CronTrigger trigger) throws SchedulerException;
	
	/**
	 * 重新往调度器添加任务
	 * @param trigger
	 * @throws SchedulerException
	 */
	public void rescheduleJob(CronTrigger trigger) throws SchedulerException;
	
}
