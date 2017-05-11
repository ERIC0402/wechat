package org.beangle.wechat.core.service.impl;

import java.util.Date;

import org.beangle.wechat.core.service.WechatService;
import org.beangle.wechat.core.util.MessageUtil;
import org.beangle.wechat.json.model.TextMessage;

public class WechatServiceImpl extends BaseWechatImpl implements WechatService {
	
	@Override
	public TextMessage getDefaultTextMessage(String fromUserName, String toUserName) {
		TextMessage textMessage = new TextMessage();
		textMessage.setToUserName(fromUserName);
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		return textMessage;
	}

}
