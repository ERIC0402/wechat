package org.beangle.wechat.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.beangle.wechat.core.model.WechatKey;
import org.beangle.wechat.core.model.WechatNewsItem;
import org.beangle.wechat.core.model.WechatTextNewsTemplate;
import org.beangle.wechat.core.service.Hdfw;
import org.beangle.wechat.core.util.MessageUtil;
import org.beangle.wechat.core.util.SessionUtil;
import org.beangle.wechat.core.util.WechatUtil;
import org.beangle.wechat.json.model.NewsMessage;
import org.beangle.wechat.json.model.NewsMessage.NewsMessageItem;
import org.beangle.wechat.json.model.TextMessage;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;

public abstract class BaseResponseImpl extends BaseWechatImpl implements ApplicationContextAware {
	
	protected String builderResponse(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap) {
		// 发送方帐号（open_id或者企业号UserID）
		String fromUserName = requestMap.get("FromUserName");
		// 公众帐号（开发者微信号或者企业号CorpID）
		String toUserName = requestMap.get("ToUserName");
		// 消息类型
		String msgType = requestMap.get("MsgType");
		return responseMsg(wechatAccountOrWechatCorpApp, requestMap, fromUserName, toUserName, msgType);
	}
	
	private String responseMsg(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName, String msgType) {
		updateWechatUser(wechatAccountOrWechatCorpApp, requestMap, fromUserName, toUserName);
		if (MessageUtil.REQ_MESSAGE_TYPE_IMAGE.equals(msgType)) {// 【微信触发类型】图片消息
			return buildImage(wechatAccountOrWechatCorpApp, requestMap, fromUserName, toUserName);
		}else if (MessageUtil.REQ_MESSAGE_TYPE_LINK.equals(msgType)) {// 【微信触发类型】链接消息
			return buildLink(wechatAccountOrWechatCorpApp, requestMap, fromUserName, toUserName);
		}else if (MessageUtil.REQ_MESSAGE_TYPE_VOICE.equals(msgType)) {// 【微信触发类型】音频消息
			return buildVoice(wechatAccountOrWechatCorpApp, requestMap, fromUserName, toUserName);
		}else if (MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgType)) {
			return findEvent(wechatAccountOrWechatCorpApp, requestMap, fromUserName, toUserName, requestMap.get("Event"));
		}else{
			return respMsgByEventKey(wechatAccountOrWechatCorpApp, requestMap, fromUserName, toUserName, requestMap.get("Content"));
		}
	}
	
	/**
	 * 微信推送事件
	 * @param wechatAccount
	 * @param textMessage
	 * @param requestMap
	 * @param fromUserName
	 * @param toUserName
	 * @param eventType
	 * @return
	 */
	public String findEvent(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName, String eventType) {
		// 订阅
		if (MessageUtil.EVENT_TYPE_SUBSCRIBE.equals(eventType)) {
			return buildSubscribe(wechatAccountOrWechatCorpApp, requestMap, fromUserName, toUserName);
		}else if (MessageUtil.EVENT_TYPE_UNSUBSCRIBE.equals(eventType)) {// 取消订阅
			return buildUnsubscribe(wechatAccountOrWechatCorpApp, requestMap, fromUserName, toUserName);
		}else if (MessageUtil.EVENT_TYPE_LOCATION.equals(eventType)) {// 地理位置消息
			return buildLocation(wechatAccountOrWechatCorpApp, requestMap, fromUserName, toUserName);
		}else if (StringUtils.isNotBlank(requestMap.get("EventKey"))) {// 菜单点击事件或者扫码推事件（提示框）
			return respMsgByEventKey(wechatAccountOrWechatCorpApp, requestMap, fromUserName, toUserName, requestMap.get("EventKey"));
		}else if (MessageUtil.EVENT_TYPE_VIEW.equals(eventType)) {// 菜单链接事件
			return MessageUtil.textMessageToXml(getDefaultResponse(fromUserName, toUserName, "您发送的是链接事件！"));
		}
		return "success";
	}

	/**
	 * 响应关键字
	 * @param wechatAccount
	 * @param requestMap
	 * @param fromUserName
	 * @param toUserName
	 * @param key
	 * @return
	 */
	protected String respMsgByEventKey(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName, String key) {
		WechatTextNewsTemplate wechatTextNewsTemplate = getWechatTextNewsTemplate(wechatAccountOrWechatCorpApp, key);
		if(wechatTextNewsTemplate != null){
			if(MessageUtil.RESP_MESSAGE_TYPE_TEXT.equals(wechatTextNewsTemplate.getMsgType())){
				return doTextResponse(fromUserName, toUserName, wechatTextNewsTemplate);
			}else if(MessageUtil.RESP_MESSAGE_TYPE_NEWS.equals(wechatTextNewsTemplate.getMsgType())){
				return doNewsResponse(fromUserName, toUserName, wechatTextNewsTemplate);
			}else if(MessageUtil.RESP_MESSAGE_TYPE_SESSON.equals(wechatTextNewsTemplate.getMsgType())){
				return doSessionResponse(fromUserName, toUserName, wechatTextNewsTemplate);
			}else if(MessageUtil.RESP_MESSAGE_TYPE_ACTION.equals(wechatTextNewsTemplate.getMsgType())){
				String actionName = wechatTextNewsTemplate.getActionName();
				if(StringUtils.isNotBlank(actionName)){
					try {
						Hdfw hdfw = (Hdfw) applicationContext.getBean(Class.forName(actionName));
						return executeHdfw(wechatAccountOrWechatCorpApp, requestMap, hdfw);
					} catch (BeansException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return "success";
	}
	
	private WechatTextNewsTemplate getWechatTextNewsTemplate(Object wechatAccountOrWechatCorpApp, String key) {
		WechatKey wechatKeyResponse = findWechatKeyResponse(wechatAccountOrWechatCorpApp, key);
		if(wechatKeyResponse != null){
			return wechatKeyResponse.getWechatTextNewsTemplate();
		}
		return null;
	}
	
	protected String doTextResponse(String fromUserName, String toUserName, WechatTextNewsTemplate wechatTextNewsTemplate) {
		TextMessage textMessage = wechatService.getDefaultTextMessage(fromUserName, toUserName);
		textMessage.setContent(wechatTextNewsTemplate.getContent());
		return MessageUtil.textMessageToXml(textMessage);
	}
	
	private String doSessionResponse(String fromUserName, String toUserName, WechatTextNewsTemplate wechatTextNewsTemplate) {
		TextMessage textMessage = wechatService.getDefaultTextMessage(fromUserName, toUserName);
		String sessionKey = wechatTextNewsTemplate.getContent();
		if(StringUtils.isNotBlank(sessionKey)){
			Object sessionValue = SessionUtil.get(fromUserName + sessionKey);
			if(sessionValue != null){
				textMessage.setContent(sessionValue.toString());
			}else{
				textMessage.setContent("未获取数据");
			}
		}
		return MessageUtil.textMessageToXml(textMessage);
	}
	
	public String doNewsResponse(String fromUserName, String toUserName, WechatTextNewsTemplate wechatTextNewsTemplate) {
		List<NewsMessageItem> articleList = new ArrayList<NewsMessageItem>();
		Collections.sort(wechatTextNewsTemplate.getNewsItems());
		for (WechatNewsItem wechatNewsItem : wechatTextNewsTemplate.getNewsItems()) {
			NewsMessageItem article = new NewsMessageItem();
			article.setTitle(wechatNewsItem.getTitle());
			if(StringUtils.isNotBlank(wechatNewsItem.getImagePath())){
				String imagePath = getSystemParameters(String.class, WechatUtil.LOCALHOST_ADDRESS) + "/common/download.action?file=%s";
				article.setPicUrl(String.format(imagePath, wechatNewsItem.getImagePath()));
			}
			article.setUrl(wechatNewsItem.getUrl());
			article.setDescription(wechatNewsItem.getDescription());
			articleList.add(article);
		}
		NewsMessage news = new NewsMessage();
		news.setCreateTime(new Date().getTime());
		news.setFromUserName(toUserName);
		news.setToUserName(fromUserName);
		news.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		news.setArticleCount(articleList.size());
		news.setArticles(articleList);
		System.out.println(MessageUtil.newsMessageToXml(news));
		return MessageUtil.newsMessageToXml(news);
	}
	
	protected TextMessage getDefaultResponse(String fromUserName, String toUserName) {
		TextMessage textMessage = wechatService.getDefaultTextMessage(fromUserName, toUserName);
		textMessage.setContent(getMainMenu());
		return textMessage;
	}
	
	protected TextMessage getDefaultResponse(String fromUserName, String toUserName, String content) {
		TextMessage textMessage = wechatService.getDefaultTextMessage(fromUserName, toUserName);
		textMessage.setContent(content);
		return textMessage;
	}
	
	protected String buildFileMd5(File file) {
		String picMd5Sum = null;
		try {
			picMd5Sum = DigestUtils.md5Hex(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return picMd5Sum;
	}
	
	protected String builderImagePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("user.dir").replaceAll("\\\\", "/"));
		sb.append("/wechat/attendance/");
		return sb.toString();
	}
	
	/**
	 * 欢迎语
	 */
	protected abstract String getMainMenu();
	
	/**
	 * 更新微信用户信息
	 * @param wechatAccountOrWechatCorpApp
	 * @param requestMap
	 * @param fromUserName
	 * @param toUserName
	 * @return
	 */
	protected abstract String updateWechatUser(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName);
	
	/**
	 * 遍历关键字管理中是否存在用户输入的关键字信息
	 */
	protected abstract WechatKey findWechatKeyResponse(Object wechatAccountOrWechatCorpApp, String content);
	
	/**
	 * 构建位置事件
	 * @param wechatAccountOrWechatCorpApp
	 * @param requestMap
	 * @param fromUserName
	 * @param toUserName
	 * @return
	 */
	protected abstract String buildLocation(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName);

	/**
	 * 构建取消关注事件
	 * @param wechatAccountOrWechatCorpApp
	 * @param requestMap
	 * @param fromUserName
	 * @param toUserName
	 * @return
	 */
	protected abstract String buildUnsubscribe(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName);

	/**
	 * 构建关注事件
	 * @param wechatAccountOrWechatCorpApp
	 * @param requestMap
	 * @param fromUserName
	 * @param toUserName
	 * @return
	 */
	protected abstract String buildSubscribe(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName);

	/**
	 * 构建图片事件
	 * @param wechatAccountOrWechatCorpApp
	 * @param requestMap
	 * @param fromUserName
	 * @param toUserName
	 * @return
	 */
	protected abstract String buildImage(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName);
	
	/**
	 * 构建链接事件
	 * @param wechatAccountOrWechatCorpApp
	 * @param requestMap
	 * @param fromUserName
	 * @param toUserName
	 * @return
	 */
	protected abstract String buildLink(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName);

	/**
	 * 构建音频事件
	 * @param wechatAccountOrWechatCorpApp
	 * @param requestMap
	 * @param fromUserName
	 * @param toUserName
	 * @return
	 */
	protected abstract String buildVoice(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName);

	/**
	 * 执行回调事件
	 * @param wechatAccountOrWechatCorpApp
	 * @param requestMap
	 * @param hdfw
	 * @return
	 */
	protected abstract String executeHdfw(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, Hdfw hdfw);
	
}
