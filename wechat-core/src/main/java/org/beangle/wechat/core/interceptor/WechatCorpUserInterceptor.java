package org.beangle.wechat.core.interceptor;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.beangle.wechat.core.model.WechatCorpApp;
import org.beangle.wechat.core.model.WechatCorpUser;
import org.beangle.wechat.core.util.WechatUtil;

import com.opensymphony.xwork2.ActionInvocation;

import net.sf.json.JSONObject;

public class WechatCorpUserInterceptor extends BaseWechatInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		String wechatCorpAppID = get(WechatUtil.WEB_CORP_APP_AUTHORITY_TOKEN);
		WechatCorpApp wechatCorpApp = (WechatCorpApp) session.getAttribute(WechatUtil.WEB_CORP_APP_TOKEN);
		WechatCorpUser wechatCorpUser = (WechatCorpUser) session.getAttribute(WechatUtil.WEB_CORP_USER_TOKEN);
		if(wechatCorpUser == null && wechatCorpApp == null && StringUtils.isNotBlank(wechatCorpAppID) && !"null".equals(wechatCorpAppID)){
			wechatCorpApp = wechatAccountService.getActiveCorpApp(Long.parseLong(wechatCorpAppID));
			if(wechatCorpApp != null){
				String accessToken = wechatAccountService.getCorpAppAccessToken(wechatCorpApp);
				wechatCorpUser = getWechatCorpUser(wechatCorpApp, getUserID(), get("code"));
				if(wechatCorpUser != null){
					wechatCorpUser = wechatUserService.saveOrUpdateWechatCorpUser(wechatCorpApp.getWechatAccount(), accessToken, wechatCorpUser.getUserid());
					if(wechatCorpUser != null){
						Cookie cookie = new Cookie(WechatUtil.WEB_USER_ID, wechatCorpUser.getUserid());
						cookie.setMaxAge(1*60*60*24*7);
						response.addCookie(cookie);
						session.setAttribute(WechatUtil.WEB_CORP_APP_TOKEN, wechatCorpApp);
						session.setAttribute(WechatUtil.WEB_CORP_USER_TOKEN, wechatCorpUser);
					}
				}
				
			}
		}
		put("wechatCorpUser", wechatCorpUser);
		put("wechatCorpApp", wechatCorpApp);
		return invocation.invoke();
	}
	
	public String getUserID(){
		Cookie[] cookies = ServletActionContext.getRequest().getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies){
				if(WechatUtil.WEB_USER_ID.equals(cookie.getName())){
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	private WechatCorpUser getWechatCorpUser(WechatCorpApp wechatCorpApp, String cookieUserID, String code) throws IOException {
		WechatCorpUser wechatCorpUser = null;
		String appid = wechatCorpApp.getWechatAccount().getAppid();
		if(StringUtils.isNotBlank(code)){
			JSONObject jsonObject = wechatUserService.getUserId(wechatCorpApp.getAccessToken(), code);
			if(jsonObject != null){
				String userId = jsonObject.getString("UserId");
				return builderWechatUser(wechatUserService.getWechatCorpUser(wechatCorpApp.getWechatAccount(), userId), userId);
			}
		}else{
			if(StringUtils.isNotBlank(cookieUserID)){
				wechatCorpUser = wechatUserService.getWechatCorpUser(wechatCorpApp.getWechatAccount(), cookieUserID);
			}
			if(wechatCorpUser == null){
				//需修改网页授权获取用户基本信息域名信息 
				sendOAuth2Page(appid, URLEncoder.encode(getRequestUrl(), "utf-8"));
				return null;
			}
		}
		return wechatCorpUser;
	}

	private WechatCorpUser builderWechatUser(WechatCorpUser wechatCorpUser, String userId) {
		if(wechatCorpUser == null){
			wechatCorpUser = new WechatCorpUser();
		}
		wechatCorpUser.setUserid(userId);
		return wechatCorpUser;
	}
	
}
