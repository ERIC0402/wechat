package org.beangle.wechat.core.service;

import org.beangle.wechat.core.model.WechatAccount;
import org.beangle.wechat.core.model.WechatCorpApp;

import net.sf.json.JSONObject;

/**
 * 微信公众号
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  上午11:48:51
 */
public interface WechatAccountService {
	
	/**
	 * 获取激活的账号
	 */
	public WechatAccount getActiveAccountByWechatNumber(String wechatNumber);
	
	/**
	 * 获取激活的账号
	 */
	public WechatAccount getActiveAccountByWechatAccountID(Long wechatAccountID);
	
	/**
	 * 获取激活的企业的应用
	 * 
	 * @param wechatCorpAppID
	 * @return
	 */
	public WechatCorpApp getActiveCorpApp(Long wechatCorpAppID);
	
	/**
	 * 获取有效Token
	 * 
	 * @param wechatAccountID
	 * @return
	 */
	public String getAccessToken(Long wechatAccountID);
	
	/**
	 * 获取有效Token
	 * 
	 * @param wechatNumber
	 * @return
	 */
	public String getAccessToken(String wechatNumber);
	
	/**
	 * 获取有效Token
	 * 
	 * @param appid
	 * @param appsecret
	 * @return
	 */
	public String getAccessTokenStr(String appid, String appsecret);
	
	/**
	 * 获取企业应用有效Token
	 * 
	 * @param wechatCorpApp
	 * @return
	 */
	public String getCorpAppAccessToken(WechatCorpApp wechatCorpApp);
	
	/**
	 * 获取企业应用有效Token
	 * 
	 * @param wechatCorpAppID
	 * @return
	 */
	public String getCorpAppAccessToken(Long wechatCorpAppID);
	
	/**
	 * 获取JsAPI票据
	 * 
	 * @param wechatAccountID
	 * @param accessToken
	 * @return
	 */
	public String getJsapiTicket(Long wechatAccountID, String accessToken);
	
	/**
	 * 获取JsAPI票据
	 * 
	 * @param accessToken
	 * @return
	 */
	public String getJsapiTicketStr(String accessToken);
	
	/**
	 * 获取企业应用JsAPI票据
	 * 
	 * @param wechatCorpAppID
	 * @param accessToken
	 * @return
	 */
	public String getCorpAppJsapiTicket(Long wechatCorpAppID, String accessToken);
	
	/**
	 * 获取网页授权URL地址
	 * 
	 * @param appid
	 * @param redirect_uri
	 * @param scope
	 * @return
	 */
	public String getWebAuthorityUrl(String appid, String redirect_uri, String scope);
	
	/**
	 * 获取网页AccessToken
	 * 
	 * @param appid
	 * @param appsecret
	 * @param code
	 * @return
	 */
	public JSONObject getWebAccessTokenByCode(String appid, String appsecret, String code);
	
	/**
	 * 获取刷新后的网页AccessToken
	 * 
	 * @param appid
	 * @param webRefreshToken
	 * @return
	 */
	public JSONObject getWebAccessTokenByRefreshToken(String appid, String webRefreshToken);
	
	/**
	 * 判断网页AccessToken是否有效
	 * 
	 * @param openid
	 * @param webAccessToken
	 * @return
	 */
	public boolean isWebAccessTokenEffective(String openid, String webAccessToken);
	
	/**
	 * 判断是否企业号
	 * 
	 * @param wechatAccount
	 * @return
	 */
	public boolean isCorp(WechatAccount wechatAccount);

}
