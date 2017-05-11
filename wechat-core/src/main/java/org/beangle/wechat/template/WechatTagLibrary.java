/* Copyright c 2005-2012.
 * Licensed under GNU  LESSER General Public License, Version 3.
 * http://www.gnu.org/licenses
 */
package org.beangle.wechat.template;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.beangle.struts2.view.BeangleTagLibrary;
import org.beangle.struts2.view.TagModel;
import org.beangle.struts2.view.component.Foot;
import org.beangle.struts2.view.component.Head;
import org.beangle.struts2.view.component.Messages;
import org.beangle.struts2.view.template.Theme;
import org.beangle.wechat.core.model.WechatAccount;
import org.beangle.wechat.core.model.WechatCorpApp;
import org.beangle.wechat.core.util.WechatUtil;

import com.opensymphony.xwork2.util.ValueStack;

public class WechatTagLibrary extends BeangleTagLibrary {

	public static final String DEFAULT_THEME = "wechat";

	public WechatTagLibrary() {
		super();
	}

	public WechatTagLibrary(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		super(stack, req, res);
		theme = new Theme(DEFAULT_THEME);
	}

	public Object getModels(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new WechatTagLibrary(stack, req, res);
	}

	public TagModel getHead() {
		return get(Head.class);
	}

	public TagModel getFoot() {
		return get(Foot.class);
	}

	public TagModel getMessages() {
		return get(Messages.class);
	}
	
	public String decode(String value) throws UnsupportedEncodingException {
		if (StringUtils.isNotBlank(value)) {
			return URLDecoder.decode(value, "UTF-8");
		} else {
			return null;
		}
	}

	public String encode(String value) throws UnsupportedEncodingException {
		if (StringUtils.isNotBlank(value)) {
			return URLEncoder.encode(value, "UTF-8");
		} else {
			return null;
		}
	}

	public String basePath() {
		String serverName = req.getHeader("x-forwarded-host");
		if (StringUtils.isBlank(serverName)) {
			serverName = req.getHeader("host");
		}
		return req.getScheme() + "://" + serverName + req.getContextPath();
	}
	
	public String requestUrl() {
		return basePath() + req.getRequestURI().substring(req.getContextPath().length()) + "?" + req.getQueryString();
	}
	
	public String wechatRequestParameters(WechatAccount wechatAccount) {
		return WechatUtil.WEB_AUTHORITY_TOKEN + "=" + wechatAccount.getId() + "&" + WechatUtil.WEB_JS_API + "=" + wechatAccount.getId();
	}
	
	public String wechatRequestParameters(WechatCorpApp wechatCorpApp) {
		return WechatUtil.WEB_CORP_APP_AUTHORITY_TOKEN + "=" + wechatCorpApp.getId() + "&" + WechatUtil.WEB_CORP_JS_API + "=" + wechatCorpApp.getId();
	}

}
