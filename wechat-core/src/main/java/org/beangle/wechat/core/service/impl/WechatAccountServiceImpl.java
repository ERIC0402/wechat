package org.beangle.wechat.core.service.impl;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.beangle.model.persist.impl.BaseServiceImpl;
import org.beangle.wechat.core.model.WechatAccount;
import org.beangle.wechat.core.model.WechatCorpApp;
import org.beangle.wechat.core.service.WechatAccountService;
import org.beangle.wechat.core.util.WechatUtil;
import org.beangle.wechat.util.DictDataUtil;

public class WechatAccountServiceImpl extends BaseServiceImpl implements WechatAccountService {
	
	@Override
	public WechatAccount getActiveAccountByWechatNumber(String wechatNumber){
		List<WechatAccount> wechatAccounts = entityDao.get(WechatAccount.class, new String[]{"wechatNumber", "status"}, wechatNumber, true);
		return wechatAccounts!=null && wechatAccounts.size() == 1 ? wechatAccounts.get(0) : null;
	}
	
	@Override
	public WechatAccount getActiveAccountByWechatAccountID(Long wechatAccountID) {
		if(wechatAccountID != null){
			List<WechatAccount> wechatAccounts = entityDao.get(WechatAccount.class, new String[]{"id", "status"}, wechatAccountID, true);
			return wechatAccounts!=null && wechatAccounts.size() == 1 ? wechatAccounts.get(0) : null;
		}else{
			return null;
		}
	}
	
	@Override
	public WechatCorpApp getActiveCorpApp(Long wechatCorpAppID) {
		List<WechatCorpApp> wechatCorpApps = entityDao.get(WechatCorpApp.class, new String[]{"id", "status"}, wechatCorpAppID, true);
		return wechatCorpApps!=null && wechatCorpApps.size() == 1 ? wechatCorpApps.get(0) : null;
	}
	
	@Override
	public String getAccessToken(Long wechatAccountID) {
		WechatAccount wechatAccount = getActiveAccountByWechatAccountID(wechatAccountID);
		if(wechatAccount != null){
			return builderAccessToken(wechatAccount);
		}
		return null;
	}
	
	@Override
	public String getCorpAppAccessToken(Long wechatCorpAppID) {
		WechatCorpApp wechatCorpApp = getActiveCorpApp(wechatCorpAppID);
		if(wechatCorpApp != null){
			return builderAccessToken(wechatCorpApp);
		}
		return null;
	}
	
	@Override
	public String getCorpAppAccessToken(WechatCorpApp wechatCorpApp) {
		if(wechatCorpApp != null){
			return builderAccessToken(wechatCorpApp);
		}
		return null;
	}
	
	@Override
	public String getAccessToken(String wechatNumber) {
		WechatAccount wechatAccount = getActiveAccountByWechatNumber(wechatNumber);
		if(wechatAccount != null){
			return builderAccessToken(wechatAccount);
		}
		return null;
	}

	private String builderAccessToken(WechatAccount wechatAccount) {
		// 判断有效时间 
		Date now = new Date();
		Date accessTokenTime = wechatAccount.getAccessTokenTime();
		if (accessTokenTime == null || (now.getTime() > accessTokenTime.getTime())) {
			JSONObject jsonObject = null;
			// 重置token
			jsonObject = getAccessToken(wechatAccount);
			wechatAccount.setAccessToken(jsonObject.getString("access_token"));
			// 重置token到期时间
			String expires_in  = jsonObject.getString("expires_in");
			wechatAccount.setAccessTokenTime(new Date(now.getTime() + (Long.parseLong(expires_in)) * 1000));
			entityDao.saveOrUpdate(wechatAccount);
		}
		return wechatAccount.getAccessToken();
	}
	
	private String builderAccessToken(WechatCorpApp wechatCorpApp) {
		// 判断有效时间 
		Date now = new Date();
		Date accessTokenTime = wechatCorpApp.getAccessTokenTime();
		if (accessTokenTime == null || (now.getTime() > accessTokenTime.getTime())) {
			JSONObject jsonObject = null;
			// 重置token
			jsonObject = getAccessTokenOfCorp(wechatCorpApp);
			wechatCorpApp.setAccessToken(jsonObject.getString("access_token"));
			// 重置token到期时间
			String expires_in  = jsonObject.getString("expires_in");
			wechatCorpApp.setAccessTokenTime(new Date(now.getTime() + (Long.parseLong(expires_in)) * 1000));
			entityDao.saveOrUpdate(wechatCorpApp);
		}
		return wechatCorpApp.getAccessToken();
	}
	
	private JSONObject getAccessToken(WechatAccount wechatAccount) {
		return getAccessToken(wechatAccount.getAppid(), wechatAccount.getSecret());
	}
	
	private JSONObject getAccessTokenOfCorp(WechatCorpApp wechatCorpApp) {
		return getAccessTokenOfCorp(wechatCorpApp.getWechatAccount().getAppid(), wechatCorpApp.getSecret());
	}
	
	public JSONObject getAccessToken(String appid, String secret){
		String requestUrl = String.format(WechatUtil.ACCESS_TOKEN_URL, appid, secret);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
		if (null != jsonObject && !jsonObject.containsKey("errmsg")) {
			return jsonObject;
		}
		return null;
	}
	
	public JSONObject getAccessTokenOfCorp(String corpid, String secret){
		String requestUrl = String.format(WechatUtil.ACCESS_TOKEN_CORP_URL, corpid, secret);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
		if (null != jsonObject && !jsonObject.containsKey("errmsg")) {
			return jsonObject;
		}
		return null;
	}
	
	public String getAccessTokenStr(String appid, String appsecret){
		JSONObject jsonObject = getAccessToken(appid, appsecret);
		if (null != jsonObject) {
			return jsonObject.getString("access_token");
		}
		return null;
	}
	
	@Override
	public String getJsapiTicket(Long wechatAccountID, String accessToken) {
		if(StringUtils.isNotBlank(accessToken)){
			WechatAccount wechatAccount = getActiveAccountByWechatAccountID(wechatAccountID);
			if(wechatAccount != null){
				// 判断有效时间 
				Date now = new Date();
				Date jsapiTicketTime = wechatAccount.getJsapiTicketTime();
				if (jsapiTicketTime == null || (now.getTime() > jsapiTicketTime.getTime())) {
					// 重置token
					JSONObject jsonObject = getJsapiTicket(accessToken);
					if(jsonObject != null){
						wechatAccount.setJsapiTicket(jsonObject.getString("ticket"));
						// 重置token到期时间
						String expires_in  = jsonObject.getString("expires_in");
						wechatAccount.setJsapiTicketTime(new Date(now.getTime() + (Long.parseLong(expires_in)) * 1000));
						entityDao.saveOrUpdate(wechatAccount);
					}
				}
				return wechatAccount.getJsapiTicket();
			}
		}
		return null;
	}
	
	private JSONObject getJsapiTicket(String accessToken) {
		String requestUrl = String.format(WechatUtil.JSAPI_TICKET, accessToken);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
		if (null != jsonObject && jsonObject.containsKey("ticket")) {
			return jsonObject;
		}
		return null;
	}
	
	@Override
	public String getCorpAppJsapiTicket(Long wechatCorpAppID, String accessToken) {
		if(StringUtils.isNotBlank(accessToken)){
			WechatCorpApp wechatCorpApp = getActiveCorpApp(wechatCorpAppID);
			if(wechatCorpApp != null){
				// 判断有效时间 
				Date now = new Date();
				Date jsapiTicketTime = wechatCorpApp.getJsapiTicketTime();
				if (jsapiTicketTime == null || (now.getTime() > jsapiTicketTime.getTime())) {
					// 重置token
					JSONObject jsonObject = getCorpAppJsapiTicket(accessToken);
					if(jsonObject != null){
						wechatCorpApp.setJsapiTicket(jsonObject.getString("ticket"));
						// 重置token到期时间
						String expires_in  = jsonObject.getString("expires_in");
						wechatCorpApp.setJsapiTicketTime(new Date(now.getTime() + (Long.parseLong(expires_in)) * 1000));
						entityDao.saveOrUpdate(wechatCorpApp);
					}
				}
				return wechatCorpApp.getJsapiTicket();
			}
		}
		return null;
	}
	
	private JSONObject getCorpAppJsapiTicket(String accessToken) {
		String requestUrl = String.format(WechatUtil.JSAPI_CORP_APP_TICKET, accessToken);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
		if (null != jsonObject && jsonObject.containsKey("ticket")) {
			return jsonObject;
		}
		return null;
	}
	
	public String getJsapiTicketStr(String accessToken) {
		JSONObject jsonObject = getJsapiTicket(accessToken);
		if (null != jsonObject) {
			return jsonObject.getString("ticket");
		}
		return null;
	}
	
	@Override
	public String getWebAuthorityUrl(String appid, String redirect_uri, String scope){
		if(StringUtils.isNotBlank(appid) && StringUtils.isNotBlank(redirect_uri) && StringUtils.isNotBlank(scope)){
			return String.format(WechatUtil.WEB_AUTHORITY_URL, appid, redirect_uri, scope);
		}
		return null;
	}
	
	@Override
	public JSONObject getWebAccessTokenByCode(String appid, String appsecret, String code) {
		if(StringUtils.isNotBlank(appid) && StringUtils.isNotBlank(appsecret) && StringUtils.isNotBlank(code)){
			String requestUrl = String.format(WechatUtil.WEB_ACCESS_TOKEN_URL, appid, appsecret, code);
			JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
			if (null != jsonObject && !jsonObject.containsKey("errcode")) {
				return jsonObject;
			}
		}
		return null;
	}
	
	@Override
	public JSONObject getWebAccessTokenByRefreshToken(String appid, String webRefreshToken) {
		String requestUrl = String.format(WechatUtil.REFRESH_WEB_ACCESS_TOKEN_URL, appid, webRefreshToken);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
		if (null != jsonObject && !jsonObject.containsKey("errcode")) {
			return jsonObject;
		}
		return null;
	}
	
	@Override
	public boolean isWebAccessTokenEffective(String openid, String webAccessToken) {
		String requestUrl = String.format(WechatUtil.CHECK_WEB_ACCESS_TOKEN_URL, webAccessToken, openid);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
		if (null != jsonObject && jsonObject.containsValue("ok")) {
			return true;
		}
		return false;
	}
	
	public boolean isCorp(WechatAccount wechatAccount){
		return DictDataUtil.WECHAT_ACCOUNT_TYPE_QYH.equals(wechatAccount.getPublicType().getCode());
	}

	
}
