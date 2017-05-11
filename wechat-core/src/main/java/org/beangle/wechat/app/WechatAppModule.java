package org.beangle.wechat.app;

import org.beangle.spring.bind.AbstractBindModule;
import org.beangle.spring.bind.Scope;
import org.beangle.wechat.app.action.WechatAppAccountAction;
import org.beangle.wechat.app.service.impl.WechatAppResponseServiceImpl;

/**
 * 微信Module
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  下午2:30:59
 */
public class WechatAppModule extends AbstractBindModule {

	@Override
	protected void doBinding() {
		bind(WechatAppAccountAction.class).in(Scope.PROTOTYPE);
		bind(WechatAppResponseServiceImpl.class).shortName();
	}

}
