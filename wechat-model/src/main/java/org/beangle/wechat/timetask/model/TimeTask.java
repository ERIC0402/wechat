package org.beangle.wechat.timetask.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.beangle.model.pojo.LongIdObject;

@Entity(name = "org.beangle.wechat.timetask.model.TimeTask")
public class TimeTask extends LongIdObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 触发器名称
	 */
	@Column(length = 50)
	private String triggerName;

	/**
	 * 执行类
	 */
	@Column(length = 100)
	private String targetClass;

	/**
	 * 执行方法
	 */
	@Column(length = 50)
	private String targetMethod;

	/**
	 * 描述
	 */
	@Column(length = 100)
	private String describe;

	/**
	 * 时间表达式
	 */
	@Column(length = 50)
	private String cronExpression;

	/**
	 * 是否有效
	 */
	private boolean status;

	public String getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	public String getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getJobName() {
		return this.targetClass + "." + this.targetMethod;
	}

}
