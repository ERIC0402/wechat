package org.beangle.wechat.core.action;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.beangle.model.Entity;
import org.beangle.model.query.QueryBuilder;
import org.beangle.model.query.builder.OqlBuilder;
import org.beangle.struts2.convention.route.Action;
import org.beangle.wechat.core.model.WechatSource;
import org.beangle.wechat.util.DictTypeUtil;

/**
 * 微信素材
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  上午10:50:13
 */
public class WechatSourceAction extends BaseWechatAction {
	
	@Override
	protected String getEntityName() {
		return WechatSource.class.getName();
	}
	
	@Override
	protected void indexSetting() {
		put("wechatAccountID", getEntityId("wechatAccount"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected QueryBuilder<?> getQueryBuilder() {
		Long wechatAccountID = getLong("wechatAccountID");
		OqlBuilder<WechatSource> builder = (OqlBuilder<WechatSource>) super.getQueryBuilder();
		if(wechatAccountID != null){
			put("wechatAccountID", wechatAccountID);
			builder.where("wechatAccount.id = :wechatAccountID", wechatAccountID);
		}else{
			builder.where("1 = -1");
		}
		return builder;
	}
	
	@Override
	protected void editSetting(Entity<?> entity) {
		put("wechatAccountID", getLong("wechatAccountID"));
		put("types", getDictDataByDictTypeCode(DictTypeUtil.DICT_TYPE_WECHAT_FILETYPE));
	}
	
	/**
	 * 上传永久素材
	 * @return
	 */
	public String uploadWechatServer() {
		List<WechatSource> wechatSources = entityDao.get(WechatSource.class, getEntityIds());
		StringBuilder sb = new StringBuilder();
		if(wechatSources != null && wechatSources.size() > 0){
			String accessToken = wechatAccountService.getAccessToken(wechatSources.get(0).getWechatAccount().getId());
			for(WechatSource wechatSource : wechatSources){
				String mediaId = wechatSource.getMediaId();
				boolean isUpload = true;
				if(StringUtils.isNotBlank(mediaId)){
					isUpload = wechatSourceService.delSource(accessToken, wechatSource.getMediaId());
				}
				if(isUpload){
					JSONObject jsonObject = wechatSourceService.uploadSource(accessToken, wechatSource.getType().getCode(), wechatSource.getFileName(), getBaseDir() + wechatSource.getFilePath());
					if(jsonObject != null){
						wechatSource.setMediaId(jsonObject.getString("media_id"));
						wechatSource.setUrl(jsonObject.getString("url"));
					}else{
						sb.append(wechatSource.getFileName());
						sb.append(",");
					}
				}else{
					sb.append(wechatSource.getFileName());
					sb.append(",");
				}
			}
			entityDao.saveOrUpdate(wechatSources);
		}
		String message = sb.toString();
		if(sb.length() > 0){
			message = StringUtils.substringBeforeLast(message, ",") + "上传失败";
		}else{
			message = "上传成功！";
		}
		return redirect(new Action(this.getClass(), "search", "wechatAccountID=" + getLong("wechatAccountID")), message);
	}
	
	@Override
	protected String saveAndForward(Entity<?> entity) {
		WechatSource wechatSource = (WechatSource) entity;
		if(StringUtils.isNotBlank(wechatSource.getFilePath())){
			wechatSource.setFilePath(moveAndRemoveAnnex(wechatSource.getFilePath(), null));
		}
		return super.saveAndForward(wechatSource);
	}
	
}
