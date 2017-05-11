package org.beangle.wechat.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.beangle.model.pojo.LongIdObject;

/**
 * 微信单图文素材
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月10日  下午13:22:22
 */
@Entity(name="org.beangle.wechat.core.model.WechatArticle")
public class WechatArticle extends LongIdObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 媒体文件唯一标识
	 */
	@Column(length=64)
	private String mediaId;
	
	/**
	 * 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
	 */
	@Column(length=200)
	private String digest;
	
	/**
	 * 文章
	 */
	private WechatArticleItem wechatArticleItem;
	
	/**
	 * 微信公众号
	 */
	private WechatAccount wechatAccount;
	
	/**
	 * 微信企业应用
	 */
	private WechatCorpApp wechatCorpApp;
	
	/**
	 * 是否临时素材
	 */
	private boolean temp;
	
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

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public WechatArticleItem getWechatArticleItem() {
		return wechatArticleItem;
	}

	public void setWechatArticleItem(WechatArticleItem wechatArticleItem) {
		this.wechatArticleItem = wechatArticleItem;
	}

	public boolean isTemp() {
		return temp;
	}

	public void setTemp(boolean temp) {
		this.temp = temp;
	}

	public WechatCorpApp getWechatCorpApp() {
		return wechatCorpApp;
	}

	public void setWechatCorpApp(WechatCorpApp wechatCorpApp) {
		this.wechatCorpApp = wechatCorpApp;
	}

}
