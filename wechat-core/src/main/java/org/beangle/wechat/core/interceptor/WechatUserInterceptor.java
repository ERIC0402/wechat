package org.beangle.wechat.core.interceptor;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.beangle.wechat.core.model.WechatAccount;
import org.beangle.wechat.core.model.WechatUser;
import org.beangle.wechat.core.util.WechatUtil;

import com.opensymphony.xwork2.ActionInvocation;

import net.sf.json.JSONObject;

public class WechatUserInterceptor extends BaseWechatInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		String wechatAccountID = get(WechatUtil.WEB_AUTHORITY_TOKEN);
		if(StringUtils.isNotBlank(wechatAccountID)){
			WechatAccount wechatAccount = wechatAccountService.getActiveAccountByWechatAccountID(Long.parseLong(wechatAccountID));
			if(wechatAccount != null){
				WechatUser wechatUser = getWechatUser(wechatAccount, getOpenID(), new Date(), get("code"));
				if(wechatUser != null){
					JSONObject userJsonObject = wechatUserService.getUserInfo(wechatUser.getWebAccessToken(), wechatUser.getOpenid(), "zh_CN");
					if(userJsonObject != null){
						wechatUser = wechatUserService.saveWechatUser(wechatAccount, wechatUser, userJsonObject);
					}
					Cookie cookie = new Cookie(WechatUtil.WEB_OPEN_ID, wechatUser.getOpenid());
					cookie.setMaxAge(1*60*60*24*7);
					response.addCookie(cookie);
					put("wechatUser", wechatUser);
					put("wechatAccount", wechatAccount);
				}
			}
		}
		return invocation.invoke();
	}

	public String getOpenID(){
		Cookie[] cookies = ServletActionContext.getRequest().getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies){
				if(WechatUtil.WEB_OPEN_ID.equals(cookie.getName())){
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	private WechatUser getWechatUser(WechatAccount wechatAccount, String cookieOpenID, Date now, String code) throws IOException {
		String appid = wechatAccount.getAppid(), appsecret = wechatAccount.getSecret();
		WechatUser wechatUser = null;
		JSONObject jsonObject = null;
		if(StringUtils.isNotBlank(code)){
			jsonObject = wechatAccountService.getWebAccessTokenByCode(appid, appsecret, code);
			if(jsonObject != null){
				return builderWechatUser(wechatUserService.getWechatUser(wechatAccount, jsonObject.getString("openid")), now, jsonObject);
			}
		}else{
			if(StringUtils.isNotBlank(cookieOpenID)){
				wechatUser = wechatUserService.getWechatUser(wechatAccount, cookieOpenID);
				if(wechatUser != null){
					Date webAccessTokenTime = wechatUser.getWebAccessTokenTime();
					String webRefreshToken = wechatUser.getWebRefreshToken();
					if(StringUtils.isNotBlank(webRefreshToken) && webAccessTokenTime != null && now.getTime() >= webAccessTokenTime.getTime()){
						jsonObject = wechatAccountService.getWebAccessTokenByRefreshToken(appid, webRefreshToken);
						return builderWechatUser(wechatUser, now, jsonObject);
					}else{
						String webAccessToken = wechatUser.getWebAccessToken();
						String openid = wechatUser.getOpenid();
						if(wechatAccountService.isWebAccessTokenEffective(openid, webAccessToken)){
							return wechatUser;
						}
					}
				}
			}
			if(jsonObject == null){
				//需修改网页授权获取用户基本信息域名信息 
				sendOAuth2Page(appid, URLEncoder.encode(getRequestUrl(), "utf-8"));
				return null;
			}
		}
		return wechatUser;
	}

	private WechatUser builderWechatUser(WechatUser wechatUser, Date now, JSONObject jsonObject) {
		String webAccessToken = jsonObject.getString("access_token");
		String openID = jsonObject.getString("openid");
		if(wechatUser == null){
			wechatUser = new WechatUser();
		}
		wechatUser.setWebAccessToken(webAccessToken);
		wechatUser.setWebAccessTokenTime(new Date(now.getTime() + 1*1000*Long.parseLong(jsonObject.getString("expires_in"))));
		wechatUser.setWebRefreshToken(jsonObject.getString("refresh_token"));
		wechatUser.setOpenid(openID);
		wechatUser.setScope(jsonObject.getString("scope"));
		return wechatUser;
	}
	
}
