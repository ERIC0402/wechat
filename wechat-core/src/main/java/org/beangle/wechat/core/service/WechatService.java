package org.beangle.wechat.core.service;

import org.beangle.wechat.json.model.TextMessage;

/**
 * 核心
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日 上午11:48:51
 */
public interface WechatService {

	public TextMessage getDefaultTextMessage(String fromUserName, String toUserName);

}
