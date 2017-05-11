package org.beangle.wechat.core.service;

import org.beangle.wechat.core.model.WechatCorpApp;
import org.beangle.wechat.core.model.WechatTextNewsTemplate;

/**
 * 消息
 * 
 * 作者：王政
 * 创建时间：2015年8月25日 上午9:36:45
 */
public interface WechatMessageService {
	
	/**
	 * 企业号发送消息
	 * @param accessToken
	 * @param message
	 * @return
	 */
	public boolean sendMessage(String accessToken, Object message);
	
	/**
	 * 发送企业号文本消息
	 * @param message
	 * @param agentid
	 * @param userid
	 */
	public boolean sendCorpTextMessage(String message, WechatCorpApp wechatCorpApp, String userid);
	
	/**
	 * 发送图文消息
	 * @param fromUserName
	 * @param toUserName
	 * @param wechatTextNewsTemplate
	 * @return
	 */
	public String doNewsResponse(String fromUserName, String toUserName, WechatTextNewsTemplate wechatTextNewsTemplate);
	
	/**
	 * 发送模板消息
	 * @param accessToken
	 * @param message
	 * @return
	 */
	public boolean sendTemplateMessage(String accessToken, Object message);
	
}
