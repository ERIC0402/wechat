package org.beangle.wechat.corp.action;

import org.beangle.model.query.QueryBuilder;
import org.beangle.model.query.builder.OqlBuilder;
import org.beangle.wechat.core.action.BaseWechatAction;
import org.beangle.wechat.core.model.WechatAccount;
import org.beangle.wechat.util.DictDataUtil;

/**
 * 微信企业公众号管理
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  上午10:50:13
 */
public class WechatCorpAccountAction extends BaseWechatAction {
	
	@Override
	protected String getEntityName() {
		return WechatAccount.class.getName();
	}
	
	@Override
	public String index() throws Exception {
		return forward("/org/beangle/wechat/app/action/wechatAppAccount/index");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected QueryBuilder<?> getQueryBuilder() {
		OqlBuilder<WechatAccount> builder = (OqlBuilder<WechatAccount>) super.getQueryBuilder();
		builder.where("wechatAccount.publicType.code = :qyh", DictDataUtil.WECHAT_ACCOUNT_TYPE_QYH);
		return builder;
	}
	
	@Override
	public String edit() {
		put(getShortName(), getEntity());
		put("publicType", getDictData(DictDataUtil.WECHAT_ACCOUNT_TYPE_QYH));
		return forward("/org/beangle/wechat/app/action/wechatAppAccount/form");
	}
	
}
