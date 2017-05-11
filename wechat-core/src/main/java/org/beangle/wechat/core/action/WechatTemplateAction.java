package org.beangle.wechat.core.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.beangle.commons.collection.CollectUtils;
import org.beangle.model.Entity;
import org.beangle.model.query.QueryBuilder;
import org.beangle.model.query.builder.OqlBuilder;
import org.beangle.wechat.core.model.WechatNewsItem;
import org.beangle.wechat.core.model.WechatTextNewsTemplate;
import org.beangle.wechat.core.util.MessageUtil;

/**
 * 微信文本图文模板配置
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  上午10:50:13
 */
public class WechatTemplateAction extends BaseWechatAction {
	
	@Override
	protected String getEntityName() {
		return WechatTextNewsTemplate.class.getName();
	}
	
	@Override
	protected void indexSetting() {
		put("wechatAccountID", getLong("wechatAccountID"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected QueryBuilder<?> getQueryBuilder() {
		Long wechatAccountID = getLong("wechatTextNewsTemplate.wechatAccount.id");
		OqlBuilder<WechatTextNewsTemplate> builder = (OqlBuilder<WechatTextNewsTemplate>) super.getQueryBuilder();
		if(wechatAccountID != null){
			put("wechatAccountID", wechatAccountID);
			builder.where("wechatAccount.id = :wechatAccountID", wechatAccountID);
		}else{
			builder.where("1 = -1");
		}
		return builder;
	}
	
	public String addWechatNewsTemplate() {
		put("wechatNewsItem", populateEntity(WechatNewsItem.class, "wechatNewsItem"));
		put("nameBefore", get("nameBefore"));
		return forward();
	}
	
	@Override
	protected String saveAndForward(Entity<?> entity) {
		WechatTextNewsTemplate wechatTextNewsTemplate = (WechatTextNewsTemplate) entity;
		if(MessageUtil.RESP_MESSAGE_TYPE_NEWS.equals(wechatTextNewsTemplate.getMsgType())){
			wechatTextNewsTemplate.setContent(null);
			Map<Long, WechatNewsItem> removeMap = CollectUtils.newHashMap();
			if(wechatTextNewsTemplate.getNewsItems() != null && wechatTextNewsTemplate.getNewsItems().size() > 0){
				for(WechatNewsItem wechatNewsItem : wechatTextNewsTemplate.getNewsItems()){
					removeMap.put(wechatNewsItem.getId(), wechatNewsItem);
				}
			}
			int trSize = getInteger("trSize");
			List<WechatNewsItem> wechatNewsItems = CollectUtils.newArrayList();
			for(int i = 0; i <= trSize; i++){
				WechatNewsItem wechatNewsItem = populateEntity(WechatNewsItem.class, "wechatNewsItem"+i);
				if(StringUtils.isNotBlank(wechatNewsItem.getImagePath())){
					wechatNewsItem.setImagePath(moveAndRemoveAnnex(wechatNewsItem.getImagePath(), null));
					wechatNewsItem.setWechatTextNewsTemplate(wechatTextNewsTemplate);
					wechatNewsItems.add(wechatNewsItem);
					if(wechatNewsItem.getId() != null && removeMap.get(wechatNewsItem.getId()) != null){
						removeMap.remove(wechatNewsItem.getId());
					}
				}
			}
			wechatTextNewsTemplate.getNewsItems().clear();
			entityDao.remove(removeMap.values());
			entityDao.saveOrUpdate(wechatTextNewsTemplate, wechatNewsItems);
		}else if(MessageUtil.RESP_MESSAGE_TYPE_TEXT.equals(wechatTextNewsTemplate.getMsgType()) || MessageUtil.RESP_MESSAGE_TYPE_SESSON.equals(wechatTextNewsTemplate.getMsgType()) || MessageUtil.RESP_MESSAGE_TYPE_ACTION.equals(wechatTextNewsTemplate.getMsgType())){
			List<WechatNewsItem> wechatNewsItems = wechatTextNewsTemplate.getNewsItems();
			List<WechatNewsItem> removeWechatNewsItems = CollectUtils.newArrayList();
			for(WechatNewsItem wechatNewsItem : wechatNewsItems){
				wechatNewsItem.setWechatTextNewsTemplate(null);
				removeWechatNewsItems.add(wechatNewsItem);
			}
			wechatTextNewsTemplate.getNewsItems().removeAll(removeWechatNewsItems);
			entityDao.remove(removeWechatNewsItems);
			entityDao.saveOrUpdate(wechatTextNewsTemplate);
		}else{
			return redirect("search", "info.save.failure", "wechatTextNewsTemplate.wechatAccount.id=" + wechatTextNewsTemplate.getWechatAccount().getId());
		}
		return redirect("search", "info.save.success", "wechatTextNewsTemplate.wechatAccount.id=" + wechatTextNewsTemplate.getWechatAccount().getId());
	}
	
}
