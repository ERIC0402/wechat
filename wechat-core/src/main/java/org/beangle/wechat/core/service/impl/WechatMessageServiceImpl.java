package org.beangle.wechat.core.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.beangle.wechat.core.model.WechatCorpApp;
import org.beangle.wechat.core.model.WechatNewsItem;
import org.beangle.wechat.core.model.WechatTextNewsTemplate;
import org.beangle.wechat.core.service.WechatMessageService;
import org.beangle.wechat.core.util.MessageUtil;
import org.beangle.wechat.core.util.WechatUtil;
import org.beangle.wechat.json.model.NewsMessage;
import org.beangle.wechat.json.model.NewsMessage.NewsMessageItem;
import org.beangle.wechat.json.model.TextCorpMessage;
import org.beangle.wechat.json.model.TextCorpMessage.CorpText;

import net.sf.json.JSONObject;

public class WechatMessageServiceImpl extends BaseWechatImpl implements WechatMessageService {
	
	@Override
	public boolean sendMessage(String accessToken, Object message) {
		String requestUrl = String.format(WechatUtil.SEND_CORP_MESSAGE_URL, accessToken);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "POST", JSONObject.fromObject(message).toString());
		if(jsonObject != null && jsonObject.containsValue("ok")){
			return true;
		}
		return false;
	}
	
	public boolean sendCorpTextMessage(String message, WechatCorpApp wechatCorpApp, String userid) {
		TextCorpMessage textCorpMessage = new TextCorpMessage();
		textCorpMessage.setTouser(userid);
		textCorpMessage.setMsgtype("text");
		textCorpMessage.setAgentid(wechatCorpApp.getAgentid());
		textCorpMessage.setText(new CorpText(message));
		if (wechatMessageService.sendMessage(wechatAccountService.getCorpAppAccessToken(wechatCorpApp), textCorpMessage)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String doNewsResponse(String fromUserName, String toUserName, WechatTextNewsTemplate wechatTextNewsTemplate) {
		List<NewsMessageItem> articleList = new ArrayList<NewsMessageItem>();
		Collections.sort(wechatTextNewsTemplate.getNewsItems());
		for (WechatNewsItem wechatNewsItem : wechatTextNewsTemplate.getNewsItems()) {
			NewsMessageItem article = new NewsMessageItem();
			article.setTitle(wechatNewsItem.getTitle());
			if(StringUtils.isNotBlank(wechatNewsItem.getImagePath())){
				article.setPicUrl(wechatNewsItem.getImagePath());
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

	public boolean sendTemplateMessage(String accessToken, Object message) {
		String requestUrl = String.format(WechatUtil.SEND_TEMPLATE_MESSAGE_URL, accessToken);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "POST", JSONObject.fromObject(message).toString());
		if(jsonObject != null && jsonObject.containsValue("ok")){
			return true;
		}
		return false;
	}
}
