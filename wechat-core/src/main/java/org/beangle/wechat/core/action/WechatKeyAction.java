package org.beangle.wechat.core.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.beangle.commons.collection.CollectUtils;
import org.beangle.model.Entity;
import org.beangle.model.query.QueryBuilder;
import org.beangle.model.query.builder.OqlBuilder;
import org.beangle.wechat.core.model.WechatKey;
import org.beangle.wechat.core.model.WechatNewsItem;
import org.beangle.wechat.core.model.WechatTextNewsTemplate;
import org.beangle.wechat.core.util.MessageUtil;

/**
 * 微信关键字
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  上午10:50:13
 */
public class WechatKeyAction extends BaseWechatAction {
	
	@Override
	protected String getEntityName() {
		return WechatKey.class.getName();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected QueryBuilder<?> getQueryBuilder() {
		OqlBuilder<WechatKey> builder = (OqlBuilder<WechatKey>) super.getQueryBuilder();
		Long wechatAccountID = getEntityId("wechatAccount");
		if(wechatAccountID != null){
			builder.where("wechatAccount.id = :wechatAccountID", wechatAccountID);
		}else{
			Long wechatCorpAppID = getEntityId("wechatCorpApp");
			if(wechatCorpAppID != null){
				builder.where("wechatCorpApp.id = :wechatCorpAppID", wechatCorpAppID);
			}else{
				builder.where("1 = -1");
			}
		}
		return builder;
	}
	
	@Override
	protected void editSetting(Entity<?> entity) {
		put("wechatTextTemplates",findWechatTextNewsTemplate(MessageUtil.RESP_MESSAGE_TYPE_TEXT, getEntityId("wechatAccount"), getEntityId("wechatCorpApp")));
		put("wechatNewsTemplates",findWechatTextNewsTemplate(MessageUtil.RESP_MESSAGE_TYPE_NEWS, getEntityId("wechatAccount"), getEntityId("wechatCorpApp")));
		put("wechatSessionTemplates",findWechatTextNewsTemplate(MessageUtil.RESP_MESSAGE_TYPE_SESSON, getEntityId("wechatAccount"), getEntityId("wechatCorpApp")));
		put("wechatActionTemplates",findWechatTextNewsTemplate(MessageUtil.RESP_MESSAGE_TYPE_ACTION, getEntityId("wechatAccount"), getEntityId("wechatCorpApp")));
	}
	
	@Override
	protected String saveAndForward(Entity<?> entity) {
		WechatKey wechatKey = (WechatKey) entity;
		wechatKey.setWechatTextNewsTemplate(saveTemplate(populateEntity(WechatTextNewsTemplate.class.getName(), "wechatTextNewsTemplate")));
		/*Long wechatTextTemplateID = getLong("wechatTextTemplate.id");
		if(wechatTextTemplateID != null){
			wechatKey.setWechatTextNewsTemplate(entityDao.get(WechatTextNewsTemplate.class, wechatTextTemplateID));
		}else{
			Long wechatNewsTemplateID = getLong("wechatNewsTemplate.id");
			if(wechatNewsTemplateID != null){
				wechatKey.setWechatTextNewsTemplate(entityDao.get(WechatTextNewsTemplate.class, wechatNewsTemplateID));
			}else{
				Long wechatSessionTemplateID = getLong("wechatSessionTemplate.id");
				if(wechatSessionTemplateID != null){
					wechatKey.setWechatTextNewsTemplate(entityDao.get(WechatTextNewsTemplate.class, wechatSessionTemplateID));
				}else{
					Long wechatActionTemplateID = getLong("wechatActionTemplate.id");
					if(wechatActionTemplateID != null){
						wechatKey.setWechatTextNewsTemplate(entityDao.get(WechatTextNewsTemplate.class, wechatActionTemplateID));
					}
				}
			}
		}
		entityDao.getTemplate().clear();*/
		return super.saveAndForward(wechatKey);
	}
	
	private WechatTextNewsTemplate saveTemplate(Entity<?> entity) {
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
				if(StringUtils.isNotBlank(wechatNewsItem.getTitle())){
					if(StringUtils.isNotBlank(wechatNewsItem.getImagePath())){
						wechatNewsItem.setImagePath(moveAndRemoveAnnex(wechatNewsItem.getImagePath(), null));
					}
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
		}
		return wechatTextNewsTemplate;
	}
	
	public List<WechatTextNewsTemplate> findWechatTextNewsTemplate(String msgType, Long wechatAccountID, Long wechatCorpAppID){
		if(wechatAccountID != null){
			return entityDao.get(WechatTextNewsTemplate.class, new String[]{"msgType", "wechatAccount.id"}, new Object[]{msgType, wechatAccountID});
		}
		if(wechatCorpAppID != null){
			return entityDao.get(WechatTextNewsTemplate.class, new String[]{"msgType", "wechatCorpApp.id"}, new Object[]{msgType, wechatCorpAppID});
		}
		return null;
	}
	
	public String addWechatNewsTemplate() {
		put("wechatNewsItem", populateEntity(WechatNewsItem.class, "wechatNewsItem"));
		put("nameBefore", get("nameBefore"));
		return forward();
	}
	
}
