package org.beangle.wechat.core.service;

import java.util.Map;

import org.beangle.wechat.core.model.WechatAccount;
import org.beangle.wechat.core.model.WechatCorpApp;

public interface Hdfw {

	/**
	 * 订阅号或服务号成功时调用的方法
	 */
	public String onSuccess(WechatAccount wechatAccount, Map<String, String> paramMap);
	
	/**
	 * 企业号成功时调用的方法
	 */
	public String onSuccessOfCorp(WechatCorpApp wechatCorpApp, Map<String, String> paramMap);
	
}
