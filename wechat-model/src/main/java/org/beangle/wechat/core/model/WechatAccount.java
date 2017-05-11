package org.beangle.wechat.core.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.beangle.commons.collection.CollectUtils;
import org.beangle.model.pojo.LongIdObject;
import org.beangle.website.system.model.DictData;

@Entity(name="org.beangle.wechat.core.model.WechatAccount")
public class WechatAccount extends LongIdObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 公众号token
	 */
	@Column(length=50)
	private String token;
	
	/**
	 * 公众微信号
	 */
	@Column(length=50)
	private String wechatNumber;
	
	/**
	 * 公众微信名
	 */
	@Column(length=50)
	private String wechatName;
	
	/**
	 * 公众类型
	 */
	private DictData publicType;
	
	/**
	 * 公众号appid/corpid
	 */
	@Column(length=150)
	private String appid;
	
	/**
	 * 公众号secret
	 */
	@Column(length=150)
	private String secret;
	
	/**
	 * 安全Token
	 */
	@Column(length = 200)
	private String accessToken;
	
	/**
	 * 安全Token到期时间
	 */
	private Date accessTokenTime;
	
	/**
	 * jsAPI票据
	 */
	@Column(length = 200)
	private String jsapiTicket;
	
	/**
	 * jsAPI票据到期时间
	 */
	private Date jsapiTicketTime;
	
	/**
	 * 微信企业应用
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "wechatAccount")
	private Set<WechatCorpApp> wechatCorpApps = CollectUtils.newHashSet();
	
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

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getAccessTokenTime() {
		return accessTokenTime;
	}

	public void setAccessTokenTime(Date accessTokenTime) {
		this.accessTokenTime = accessTokenTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getWechatNumber() {
		return wechatNumber;
	}

	public void setWechatNumber(String wechatNumber) {
		this.wechatNumber = wechatNumber;
	}

	public String getWechatName() {
		return wechatName;
	}

	public void setWechatName(String wechatName) {
		this.wechatName = wechatName;
	}
	
	public DictData getPublicType() {
		return publicType;
	}

	public void setPublicType(DictData publicType) {
		this.publicType = publicType;
	}
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getJsapiTicket() {
		return jsapiTicket;
	}

	public void setJsapiTicket(String jsapiTicket) {
		this.jsapiTicket = jsapiTicket;
	}

	public Date getJsapiTicketTime() {
		return jsapiTicketTime;
	}

	public void setJsapiTicketTime(Date jsapiTicketTime) {
		this.jsapiTicketTime = jsapiTicketTime;
	}

	public Set<WechatCorpApp> getWechatCorpApps() {
		return wechatCorpApps;
	}

	public void setWechatCorpApps(Set<WechatCorpApp> wechatCorpApps) {
		this.wechatCorpApps = wechatCorpApps;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
