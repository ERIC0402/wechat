package org.beangle.wechat.core;

import org.beangle.spring.bind.AbstractBindModule;
import org.beangle.spring.bind.Scope;
import org.beangle.website.common.action.DownloadAction;
import org.beangle.wechat.core.action.WechatArticleAction;
import org.beangle.wechat.core.action.WechatJoinAction;
import org.beangle.wechat.core.action.WechatKeyAction;
import org.beangle.wechat.core.action.WechatMenuAction;
import org.beangle.wechat.core.action.WechatSourceAction;
import org.beangle.wechat.core.action.WechatTemplateAction;
import org.beangle.wechat.core.interceptor.WechatCorpUserInterceptor;
import org.beangle.wechat.core.interceptor.WechatUserInterceptor;
import org.beangle.wechat.core.service.impl.WechatAccountServiceImpl;
import org.beangle.wechat.core.service.impl.WechatImageServiceImpl;
import org.beangle.wechat.core.service.impl.WechatJoinServiceImpl;
import org.beangle.wechat.core.service.impl.WechatMessageServiceImpl;
import org.beangle.wechat.core.service.impl.WechatServiceImpl;
import org.beangle.wechat.core.service.impl.WechatSourceServiceImpl;
import org.beangle.wechat.core.service.impl.WechatUserServiceImpl;

/**
 * 微信Module
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  下午2:30:59
 */
public class WechatModule extends AbstractBindModule {

	@Override
	protected void doBinding() {
		bind(WechatJoinAction.class, WechatMenuAction.class, WechatKeyAction.class, WechatTemplateAction.class, DownloadAction.class).in(Scope.PROTOTYPE);
		bind(WechatSourceAction.class, WechatArticleAction.class).in(Scope.PROTOTYPE);
		bind(WechatAccountServiceImpl.class, WechatJoinServiceImpl.class, WechatServiceImpl.class).shortName();
		bind(WechatUserServiceImpl.class, WechatSourceServiceImpl.class, WechatImageServiceImpl.class).shortName();
		bind(WechatMessageServiceImpl.class).shortName();
		bind(WechatUserInterceptor.class, WechatCorpUserInterceptor.class).in(Scope.PROTOTYPE);
	}

}
