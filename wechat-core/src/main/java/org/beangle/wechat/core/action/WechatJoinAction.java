package org.beangle.wechat.core.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.beangle.wechat.core.model.WechatAccount;
import org.beangle.wechat.core.model.WechatCorpApp;
import org.beangle.wechat.core.util.aes.AesException;

/**
 * 微信接入
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  上午10:50:13
 */
public class WechatJoinAction extends BaseWechatAction {
	
	@Override
	protected String getEntityName() {
		return WechatAccount.class.getName();
	}
	
	public void wechatJoin() throws IOException, AesException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = getResponse();
		response.setContentType("text/html; charset=utf-8");
		WechatAccount wechatAccount = wechatAccountService.getActiveAccountByWechatAccountID(getLong("wechatAccountID"));
		WechatCorpApp wechatCorpApp = wechatAccountService.getActiveCorpApp(getLong("wechatCorpAppID"));
		if("GET".equalsIgnoreCase(request.getMethod())){
			String timestamp = get("timestamp");
			String nonce = get("nonce");
			String echostr = get("echostr");
			String signature = get("signature");
			if(StringUtils.isBlank(signature)){
				signature = get("msg_signature");
				if(wechatCorpApp != null){
					response.getWriter().print(wechatJoinService.getEchostr(wechatCorpApp.getToken(), wechatCorpApp.getEncodingAESKey(), wechatCorpApp.getWechatAccount().getAppid(), timestamp, nonce, echostr, signature));
				}
			}else{
				System.out.println("signature:" + signature + "timestamp:" + timestamp + "nonce:" + nonce + "echostr:" + echostr);
				if (wechatAccount != null && wechatJoinService.checkSignature(wechatAccount.getToken(), signature, timestamp, nonce)) {
					response.getWriter().print(echostr);
				}
			}
		}else{
			String message = null;
			String signature = get("msg_signature");
			if(StringUtils.isNotBlank(signature)){
				String timestamp = get("timestamp");
				String nonce = get("nonce");
				if(wechatCorpApp != null){
					message = wechatCorpResponseService.responseMsg(wechatCorpApp, request, signature, timestamp, nonce);
				}
			}else{
				if(wechatAccount != null){
					message = wechatAppResponseService.responseMsg(wechatAccount, request);
				}
			}
			response.getWriter().print(message);
		}
	}

}
