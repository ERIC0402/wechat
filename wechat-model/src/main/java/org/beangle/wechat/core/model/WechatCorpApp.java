package org.beangle.wechat.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.beangle.model.pojo.LongIdObject;

@Entity(name="org.beangle.wechat.core.model.WechatCorpApp")
public class WechatCorpApp extends LongIdObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 公众号
	 */
	private WechatAccount wechatAccount;
	
	/**
	 * 企业应用的token
	 */
	@Column(length=50)
	private String token;
	
	/**
	 * 企业应用名称
	 */
	@Column(length=50)
	private String wechatCorpAppName;
	
	/**
	 * 企业应用的agentId
	 */
	@Column(length=150)
	private String agentid;
	
	/**
	 * 企业应用的appsecret
	 */
	@Column(length=150)
	private String secret;
	
	/**
	 * 开发者设置的EncodingAESKey
	 */
	@Column(length=150)
	private String encodingAESKey;
	
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
	 * 状态
	 */
	private boolean status;
	
	public String getEncodingAESKey() {
		return encodingAESKey;
	}

	public void setEncodingAESKey(String encodingAESKey) {
		this.encodingAESKey = encodingAESKey;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public WechatAccount getWechatAccount() {
		return wechatAccount;
	}

	public void setWechatAccount(WechatAccount wechatAccount) {
		this.wechatAccount = wechatAccount;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getWechatCorpAppName() {
		return wechatCorpAppName;
	}

	public void setWechatCorpAppName(String wechatCorpAppName) {
		this.wechatCorpAppName = wechatCorpAppName;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
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

}
