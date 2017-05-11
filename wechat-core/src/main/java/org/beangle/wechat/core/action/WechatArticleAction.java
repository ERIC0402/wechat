package org.beangle.wechat.core.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.beangle.commons.exception.MyException;
import org.beangle.model.Entity;
import org.beangle.model.query.QueryBuilder;
import org.beangle.model.query.builder.OqlBuilder;
import org.beangle.struts2.convention.route.Action;
import org.beangle.wechat.core.model.WechatArticle;
import org.beangle.wechat.core.model.WechatArticleItem;
import org.beangle.wechat.core.model.WechatSource;
import org.beangle.wechat.json.model.ArticleReadNumber;
import org.beangle.wechat.util.DictDataUtil;

import net.sf.json.JSONObject;

/**
 * 微信临时图文素材
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  上午10:50:13
 */
public class WechatArticleAction extends BaseWechatAction {
	
	@Override
	protected String getEntityName() {
		return WechatArticle.class.getName();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected QueryBuilder<?> getQueryBuilder() {
		Long wechatAccountID = getEntityId("wechatAccount");
		OqlBuilder<WechatArticle> builder = (OqlBuilder<WechatArticle>) super.getQueryBuilder();
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
	
	/**
	 * 上传临时素材
	 * @param wechatSource
	 * @return
	 */
	public boolean uploadTempSource(String accessToken, WechatSource wechatSource) {
		String type = wechatSource.getType().getCode();
		JSONObject jsonObject = wechatSourceService.uploadTempSource(accessToken, type, wechatSource.getFileName(), getBaseDir() + wechatSource.getFilePath());
		if(jsonObject != null){
			if ("thumb".equals(type)){
				wechatSource.setMediaId(jsonObject.getString("thumb_media_id"));
			}else{
				wechatSource.setMediaId(jsonObject.getString("media_id"));
			}
			return true;
		}else{
			wechatSource.setMediaId(null);
			return false;
		}
	}
	
	/**
	 * 上传临时图文素材
	 * @return
	 */
	public boolean uploadTempNews(String accessToken, WechatArticle wechatArticle) {
		JSONObject jsonObject = wechatSourceService.uploadTempNews(accessToken, wechatArticle);
		if(jsonObject != null){
			wechatArticle.setMediaId(jsonObject.getString("media_id"));
			return true;
		}else{
			wechatArticle.setMediaId(null);
			return false;
		}
	}
	
	/**
	 * 上传永久素材
	 * @param wechatSource
	 * @return
	 */
	public boolean uploadSource(String accessToken, WechatSource wechatSource, boolean isTemp) {
		String mediaId = wechatSource.getMediaId();
		boolean isUpload = true;
		if(!isTemp && StringUtils.isNotBlank(mediaId)){
			isUpload = wechatSourceService.delSource(accessToken, wechatSource.getMediaId());
		}
		if(isUpload){
			JSONObject jsonObject = wechatSourceService.uploadSource(accessToken, wechatSource.getType().getCode(), wechatSource.getFileName(), getBaseDir() + wechatSource.getFilePath());
			if(jsonObject != null){
				wechatSource.setMediaId(jsonObject.getString("media_id"));
				wechatSource.setUrl(jsonObject.getString("url"));
				return true;
			}else{
				wechatSource.setMediaId(null);
				wechatSource.setUrl(null);
				return false;
			}
		}else{
			wechatSource.setMediaId(null);
			wechatSource.setUrl(null);
			return false;
		}
	}
	
	/**
	 * 上传永久图文素材
	 * @return
	 */
	public boolean uploadNews(String accessToken, WechatArticle wechatArticle) {
		JSONObject jsonObject = wechatSourceService.uploadArticle(accessToken, wechatArticle);
		if(jsonObject != null){
			wechatArticle.setMediaId(jsonObject.getString("media_id"));
			return true;
		}else{
			wechatArticle.setMediaId(null);
			return false;
		}
	}
	
	/**
	 * 获取永久图文URL
	 * @param accessToken
	 * @param wechatArticle
	 * @return
	 */
	public String getArticaleUrl(String accessToken, WechatArticle wechatArticle) {
		JSONObject jsonObject = wechatSourceService.findSource(accessToken, wechatArticle.getMediaId());
		if(jsonObject != null){
			return jsonObject.getJSONArray("news_item").getJSONObject(0).getString("url");
		}
		return null;
	}
	
	/**
	 * 上传永久图文素材至服务器
	 * @return
	 */
	public String uploadWechatServer() {
		List<WechatArticle> wechatArticles = entityDao.get(WechatArticle.class, getEntityIds());
		StringBuilder sb = new StringBuilder();
		if(wechatArticles != null && wechatArticles.size() > 0){
			String accessToken = wechatAccountService.getAccessToken(wechatArticles.get(0).getWechatAccount().getId());
			for(WechatArticle wechatArticle : wechatArticles){
				boolean isUpload = uploadSource(accessToken, wechatArticle.getWechatArticleItem().getWechatSource(), wechatArticle.isTemp());
				if(!isUpload){
					sb.append(wechatArticle.getWechatArticleItem().getTitle() + "封面上传失败！");
					sb.append(",");
					wechatArticle.setMediaId(null);
				}else{
					isUpload = uploadNews(accessToken, wechatArticle);
					if(!isUpload){
						sb.append(wechatArticle.getWechatArticleItem().getTitle() + "永久图文素材上传失败！");
						sb.append(",");
					}
				}
			}
			entityDao.saveOrUpdate(wechatArticles);
		}
		String message = sb.toString();
		if(sb.length() > 0){
			message = StringUtils.substringBeforeLast(message, ",") + "上传失败";
		}else{
			message = "上传成功！";
		}
		return redirect(new Action(this.getClass(), "search", "wechatAccountID=" + getLong("wechatAccountID")), message);
	}
	
	/**
	 * 上传临时图文素材至服务器
	 * @return
	 */
	public String uploadTempWechatServer() {
		List<WechatArticle> wechatArticles = entityDao.get(WechatArticle.class, getEntityIds());
		StringBuilder sb = new StringBuilder();
		if(wechatArticles != null && wechatArticles.size() > 0){
			String accessToken = wechatAccountService.getAccessToken(wechatArticles.get(0).getWechatAccount().getId());
			for(WechatArticle wechatArticle : wechatArticles){
				boolean isUpload = false;
				isUpload = uploadTempSource(accessToken, wechatArticle.getWechatArticleItem().getWechatSource());
				if(!isUpload){
					sb.append(wechatArticle.getWechatArticleItem().getTitle() + "封面上传失败！");
					sb.append(",");
					wechatArticle.setMediaId(null);
				}else{
					isUpload = uploadTempNews(accessToken, wechatArticle);
					if(!isUpload){
						sb.append(wechatArticle.getWechatArticleItem().getTitle() + "图文素材上传失败！");
						sb.append(",");
					}
				}
			}
			entityDao.saveOrUpdate(wechatArticles);
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
		try {
			String message = "info.save.success";
			WechatArticle wechatArticle = (WechatArticle) entity;
			wechatArticle.setTemp(true);
			WechatSource wechatSource = populateEntity(WechatSource.class, "wechatSource");
			wechatSource.setFilePath(moveAndRemoveAnnex(wechatSource.getFilePath(), null));
			wechatSource.setType(getDictData(DictDataUtil.DICT_TYPE_WECHAT_FILETYPE_IMAGE));
			WechatArticleItem wechatArticleItem = populateEntity(WechatArticleItem.class, "wechatArticleItem");
			wechatArticleItem.setWechatSource(wechatSource);
			wechatArticle.setWechatArticleItem(wechatArticleItem);
			entityDao.saveOrUpdate(wechatSource, wechatArticleItem, wechatArticle);
//			String accessToken = wechatAccountService.getAccessToken(wechatArticle.getWechatAccount().getId());
//			String url = getArticaleUrl(accessToken, wechatArticle);
//			wechatSourceService.getReadNumberBy51Tool("MTk2MTk4ODUyMA==", url);
			return redirect("search", message);
		} catch (MyException ee) {
			logger.error("saveAndForwad failure", ee);
			return redirect("search", ee.getMessage());
		} catch (Exception ee) {
			logger.info("saveAndForwad failure", ee);
			return redirect("search", "info.save.failure");
		}
	}
	
	public String sendPreview(){
		List<WechatArticle> wechatArticles = entityDao.get(WechatArticle.class, getEntityIds());
		String message = "请勾选数据";
		if(wechatArticles != null && wechatArticles.size() > 0){
			StringBuilder sb = new StringBuilder();
			String accessToken = wechatAccountService.getAccessToken(wechatArticles.get(0).getWechatAccount().getId());
			for(WechatArticle wechatArticle : wechatArticles){
				JSONObject jsonObject = wechatSourceService.sendPreview("", accessToken, wechatArticle.getWechatAccount().getWechatNumber(), wechatArticle.getMediaId(), "mpnews");
				if(jsonObject == null){
					sb.append(wechatArticle.getWechatArticleItem().getTitle());
					sb.append(",");
				}
			}
			message = sb.toString();
			if(sb.length() > 0){
				message = StringUtils.substringBeforeLast(message, ",") + "发送失败";
			}else{
				message = "发送成功！";
			}
		}
		return redirect(new Action(this.getClass(), "search", "wechatAccountID=" + getLong("wechatAccountID")), message);
	}
	
	public String sendAll(){
		List<WechatArticle> wechatArticles = entityDao.get(WechatArticle.class, getEntityIds());
		String message = "请勾选数据";
		if(wechatArticles != null && wechatArticles.size() > 0){
			StringBuilder sb = new StringBuilder();
			String accessToken = wechatAccountService.getAccessToken(wechatArticles.get(0).getWechatAccount().getId());
			for(WechatArticle wechatArticle : wechatArticles){
				JSONObject jsonObject = wechatSourceService.sendAll(accessToken, wechatArticle.getMediaId(), "mpnews");
				if(jsonObject == null){
					sb.append(wechatArticle.getWechatArticleItem().getTitle());
					sb.append(",");
				}
			}
			message = sb.toString();
			if(sb.length() > 0){
				message = StringUtils.substringBeforeLast(message, ",") + "发送失败";
			}else{
				message = "发送成功！";
			}
		}
		return redirect(new Action(this.getClass(), "search", "wechatAccountID=" + getLong("wechatAccountID")), message);
	}
	
	public String readNumber() {
		List<WechatArticle> wechatArticles = entityDao.get(WechatArticle.class, getEntityIds());
		if(wechatArticles != null && wechatArticles.size() > 0){
			String accessToken = wechatAccountService.getAccessToken(wechatArticles.get(0).getWechatAccount().getId());
			String[] beginEndDates = findBeginEndDate();
			JSONObject jsonObject = wechatSourceService.searchReadNumber(accessToken, beginEndDates[0], beginEndDates[1]);
			if(jsonObject != null){
				System.out.println(jsonObject);
				ArticleReadNumber articleReadNumber = (ArticleReadNumber) JSONObject.toBean(jsonObject, ArticleReadNumber.class);
				System.out.println(articleReadNumber);
			}
		}
		return forward();
	}
	
	public String[] findBeginEndDate(){
		Calendar rightNow = Calendar.getInstance();
		Date now = new Date();
        rightNow.setTime(now);
        rightNow.add(Calendar.DAY_OF_YEAR,-1);
        Date endDate = rightNow.getTime();
        rightNow.add(Calendar.DAY_OF_YEAR,-2);
        Date beginDate = rightNow.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return new String[]{sdf.format(beginDate), sdf.format(endDate)};
	}
	
}
