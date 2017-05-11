package org.beangle.wechat.corp.action;

import org.beangle.wechat.core.action.BaseWechatAction;
import org.beangle.wechat.core.model.WechatCorpApp;

public class WechatCorpAppAction extends BaseWechatAction {

	@Override
	protected String getEntityName() {
		return WechatCorpApp.class.getName();
	}
	
}
