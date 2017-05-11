package org.beangle.wechat.core.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.beangle.commons.collection.CollectUtils;
import org.beangle.model.pojo.LongIdObject;

/**
 * 微信文本消息模板
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月10日  下午13:22:22
 */
@Entity(name="org.beangle.wechat.core.model.WechatTextNewsTemplate")
@Table(name="PTW_WECHATMSGTEMPLATE")
public class WechatTextNewsTemplate extends LongIdObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 模板名称
	 */
	private String templateName;
	
	/**
	 * 消息类型（text、news、sessionKey、action）
	 */
	private String msgType;
	
	/**
	 * 内容（针对text）
	 */
	private String content;
	
	/**
	 * session关键字（针对sessionKey）
	 */
	private String sessionKey;
	
	/**
	 *	控制器类名（针对action）
	 */
	private String actionName;
	
	/**
	 * 图文列表（针对news）
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy="wechatTextNewsTemplate")
	private List<WechatNewsItem> newsItems = CollectUtils.newArrayList();
	
	/**
	 * 微信公众号
	 */
	private WechatAccount wechatAccount;
	
	/**
	 * 微信企业应用
	 */
	private WechatCorpApp wechatCorpApp;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public List<WechatNewsItem> getNewsItems() {
		return newsItems;
	}

	public void setNewsItems(List<WechatNewsItem> newsItems) {
		this.newsItems = newsItems;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
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
}
