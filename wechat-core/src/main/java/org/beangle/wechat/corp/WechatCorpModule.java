package org.beangle.wechat.corp;

import org.beangle.spring.bind.AbstractBindModule;
import org.beangle.spring.bind.Scope;
import org.beangle.wechat.corp.action.WechatCorpAccountAction;
import org.beangle.wechat.corp.action.WechatCorpAppAction;
import org.beangle.wechat.corp.service.impl.WechatCorpResponseServiceImpl;

/**
 * 微信Module
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  下午2:30:59
 */
public class WechatCorpModule extends AbstractBindModule {

	@Override
	protected void doBinding() {
		bind(WechatCorpAccountAction.class, WechatCorpAppAction.class).in(Scope.PROTOTYPE);
		bind(WechatCorpResponseServiceImpl.class).shortName();
	}

}
