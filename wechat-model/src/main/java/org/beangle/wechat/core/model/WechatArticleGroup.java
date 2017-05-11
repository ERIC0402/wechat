package org.beangle.wechat.core.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.beangle.commons.collection.CollectUtils;
import org.beangle.model.pojo.LongIdObject;

/**
 * 微信多图文素材
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月10日  下午13:22:22
 */
@Entity(name="org.beangle.wechat.core.model.WechatArticleGroup")
public class WechatArticleGroup extends LongIdObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 媒体文件唯一标识
	 */
	@Column(length=64)
	private String mediaId;
	
	/**
	 * 图文内容
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "wechatArticleGroup")
	private List<WechatArticleItem> wehcatArticleItems = CollectUtils.newArrayList();
	
	/**
	 * 微信公众号
	 */
	private WechatAccount wechatAccount;
	
	/**
	 * 是否上传成功
	 */
	private boolean success;
	

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public WechatAccount getWechatAccount() {
		return wechatAccount;
	}

	public void setWechatAccount(WechatAccount wechatAccount) {
		this.wechatAccount = wechatAccount;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<WechatArticleItem> getWehcatArticleItems() {
		return wehcatArticleItems;
	}

	public void setWehcatArticleItems(List<WechatArticleItem> wehcatArticleItems) {
		this.wehcatArticleItems = wehcatArticleItems;
	}

}
