package org.beangle.wechat.core.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.beangle.commons.property.PropertyConfig;
import org.beangle.commons.property.PropertyConfigFactory;
import org.beangle.wechat.core.service.WechatAccountService;
import org.beangle.wechat.core.service.WechatUserService;
import org.beangle.wechat.core.util.WechatUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public abstract class BaseWechatInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected WechatAccountService wechatAccountService;

	protected WechatUserService wechatUserService;

	// 以下为获取系统参数
	protected PropertyConfigFactory configFactory;

	public PropertyConfig getSystemConfig() {
		return configFactory.getConfig();
	}

	/**
	 * 获取系统参数
	 * 
	 * @param clazz
	 *            参数类型
	 * @param name
	 *            参数名称
	 * @return 参数
	 */
	protected <T> T getSystemParameters(Class<T> clazz, String name) {
		return getSystemConfig().get(clazz, name);
	}

	public String getRequestUrl() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String serverName = request.getHeader("x-forwarded-host");
		if (StringUtils.isBlank(serverName)) {
			serverName = request.getHeader("host");
		}
		return request.getScheme() + "://" + serverName + request.getContextPath()  + request.getRequestURI().substring(request.getContextPath().length()) + "?" + request.getQueryString();
	}

	public String get(String name) {
		return ServletActionContext.getRequest().getParameter(name);
	}

	public void put(String key, Object value) {
		// 用于页面获取值
		ActionContext.getContext().getContextMap().put(key, value);
		// 用于本次请求方法中获取值
		ActionContext.getContext().getParameters().put(key, value);
	}

	public void sendOAuth2Page(String appid, String redirectUri) throws IOException {
		String webActhorityUrl = wechatAccountService.getWebAuthorityUrl(appid, redirectUri, WechatUtil.WEB_SNSAPI_USERINFO);
		ServletActionContext.getResponse().sendRedirect(webActhorityUrl);
	}

	public void setWechatAccountService(WechatAccountService wechatAccountService) {
		this.wechatAccountService = wechatAccountService;
	}

	public void setWechatUserService(WechatUserService wechatUserService) {
		this.wechatUserService = wechatUserService;
	}

	public void setConfigFactory(PropertyConfigFactory configFactory) {
		this.configFactory = configFactory;
	}

}
