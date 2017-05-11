package org.beangle.wechat.core.service;

import org.beangle.wechat.core.util.aes.AesException;


/**
 * 微信接入
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  上午11:48:51
 */
public interface WechatJoinService {
	
	/**
	 * 检测对接信息
	 * @param token
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public boolean checkSignature(String token,String signature, String timestamp, String nonce);
	
	/**
	 * 获取解密出原文（企业）
	 * @param token
	 * @param encodeingAESKey
	 * @param corpid
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @param signature
	 * @return
	 * @throws AesException
	 */
	public String getEchostr(String token, String encodeingAESKey, String corpid, String timestamp, String nonce, String echostr, String signature) throws AesException;
	
}
