package org.beangle.wechat.app.service;

import javax.servlet.http.HttpServletRequest;

import org.beangle.wechat.core.model.WechatAccount;

/**
 * 微信服务号和订阅号响应
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  上午11:48:51
 */
public interface WechatAppResponseService {
	
	public String responseMsg(WechatAccount wechatAccount, HttpServletRequest request);

}
