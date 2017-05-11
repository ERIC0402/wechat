package org.beangle.wechat.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.beangle.model.pojo.LongIdObject;

@Entity(name = "org.beangle.wechat.core.model.WechatCorpDepartment")
@Table(name = "PTW_WHT_DEPARTMENT")
public class WechatCorpDepartment extends LongIdObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 部门id
	 */
	@Column(length = 10)
	private String departmentId;
	
	/**
	 * 部门名称
	 */
	@Column(length = 100)
	private String name;
	
	/**
	 * 父亲部门id 根部门为1
	 */
	@Column(length = 10)
	private String parentId;
	
	/**
	 * 在父部门中的次序值。order值小的排序靠前
	 */
	@Column(length = 10)
	private String orders;
	
	/**
	 * 微信公众号
	 */
	private WechatAccount wechatAccount;

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getOrders() {
		return orders;
	}

	public void setOrders(String orders) {
		this.orders = orders;
	}

	public WechatAccount getWechatAccount() {
		return wechatAccount;
	}

	public void setWechatAccount(WechatAccount wechatAccount) {
		this.wechatAccount = wechatAccount;
	}
	
}
