package org.beangle.wechat.core.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.beangle.commons.collection.CollectUtils;
import org.beangle.model.pojo.LongIdObject;
import org.beangle.website.system.model.DictData;

/**
 * 微信按钮
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  下午3:56:58
 */
@Entity(name = "org.beangle.wechat.core.model.WechatButton")
public class WechatButton extends LongIdObject {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 微信公众号
	 */
	private WechatAccount wechatAccount;
	
	/**
	 * 微信企业应用
	 */
	private WechatCorpApp wechatCorpApp;
	
	/**
	 * 代码
	 */
	private String code;
	
	/**
	 * 按钮名
	 */
	@Column(length=50)
	private String name;
	
	/**
	 * 按钮事件
	 */
	private DictData event;
	
	/**
	 * 菜单标识（关键字，用来判断click事件）
	 */
	@Column(length=50)
	private String menuKey;
	
	/**
	 * 网页链接（如按钮类型为url，则此不能为空）
	 */
	@Column(length=250)
	private String url;
	
	/**
	 * 按钮顺序
	 */
	private Integer orders;
	
	/**
	 * 父级按钮
	 */
	private WechatButton parentButton;
	
	/**
	 * 子级别按钮
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentButton")
	private List<WechatButton> childrenButtons = CollectUtils.newArrayList();
	
	/**
	 * 状态
	 */
	private boolean status;
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMenuKey() {
		return menuKey;
	}

	public DictData getEvent() {
		return event;
	}

	public void setEvent(DictData event) {
		this.event = event;
	}

	public void setMenuKey(String menuKey) {
		this.menuKey = menuKey;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public WechatButton getParentButton() {
		return parentButton;
	}

	public void setParentButton(WechatButton parentButton) {
		this.parentButton = parentButton;
	}

	public WechatAccount getWechatAccount() {
		return wechatAccount;
	}

	public void setWechatAccount(WechatAccount wechatAccount) {
		this.wechatAccount = wechatAccount;
	}

	public WechatCorpApp getWechatCorpApp() {
		return wechatCorpApp;
	}

	public void setWechatCorpApp(WechatCorpApp wechatCorpApp) {
		this.wechatCorpApp = wechatCorpApp;
	}
	
	public void setCode(Integer orders){
		this.orders = orders;
		if(this.getParentButton() != null){
			this.setCode(this.getParentButton().getCode() + "." + orders);
		}else{
			this.setCode(orders.toString());
		}
		for(WechatButton children : this.getChildrenButtons()){
			children.setCode(children.getOrders());
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public List<WechatButton> getChildrenButtons() {
		return childrenButtons;
	}

	public void setChildrenButtons(List<WechatButton> childrenButtons) {
		this.childrenButtons = childrenButtons;
	}

}
