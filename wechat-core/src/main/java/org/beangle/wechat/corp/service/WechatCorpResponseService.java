package org.beangle.wechat.corp.service;

import javax.servlet.http.HttpServletRequest;

import org.beangle.wechat.core.model.WechatCorpApp;

/**
 * 微信企业号响应
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  上午11:48:51
 */
public interface WechatCorpResponseService {
	
	public String responseMsg(WechatCorpApp wechatCorpApp, HttpServletRequest request, String signature, String timestamp, String nonce);

}
