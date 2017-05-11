package org.beangle.wechat.app.action;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.beangle.model.Entity;
import org.beangle.model.query.QueryBuilder;
import org.beangle.model.query.builder.OqlBuilder;
import org.beangle.website.system.model.DictData;
import org.beangle.wechat.core.action.BaseWechatAction;
import org.beangle.wechat.core.model.WechatAccount;
import org.beangle.wechat.util.DictDataUtil;
import org.beangle.wechat.util.DictTypeUtil;

/**
 * 微信服务订阅号管理
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  上午10:50:13
 */
public class WechatAppAccountAction extends BaseWechatAction {
	
	@Override
	protected String getEntityName() {
		return WechatAccount.class.getName();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected QueryBuilder<?> getQueryBuilder() {
		OqlBuilder<WechatAccount> builder = (OqlBuilder<WechatAccount>) super.getQueryBuilder();
		builder.where("wechatAccount.publicType.code != :qyh", DictDataUtil.WECHAT_ACCOUNT_TYPE_QYH);
		return builder;
	}
	
	@Override
	protected void editSetting(Entity<?> entity) {
		List<DictData> publicTypes = findDictData(DictTypeUtil.WECHAT_ACCOUNT_TYPE);
		for(Iterator<DictData> itor = publicTypes.iterator(); itor.hasNext();){
			DictData publicType = itor.next();
			if(DictDataUtil.WECHAT_ACCOUNT_TYPE_QYH.equals(publicType.getCode())){
				itor.remove();
			}
		}
		put("publicTypes", publicTypes);
	}
	
	public String lookUrl(){
		String wechatAccountID = get("wechatAccountID");
		if(StringUtils.isNotBlank(wechatAccountID)){
			put("url", getBasePath() + "/core/wechat-join!wechatJoin.action?wechatAccountID=" + wechatAccountID);
		}else{
			String wechatCorpAppID = get("wechatCorpAppID");
			if(StringUtils.isNotBlank(wechatCorpAppID)){
				put("url", getBasePath() + "/core/wechat-join!wechatJoin.action?wechatCorpAppID=" + wechatCorpAppID);
			}
		}
		return forward();
	}

}
