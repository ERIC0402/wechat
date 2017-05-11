package org.beangle.wechat.core.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

import org.beangle.model.pojo.LongIdObject;

/**
 * 微信图文素材节点
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月10日  下午13:22:22
 */
@Entity(name="org.beangle.wechat.core.model.WechatArticleItem")
public class WechatArticleItem extends LongIdObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 微信多图文素材
	 */
	private WechatArticleGroup wechatArticleGroup;
	
	/**
	 * 标题
	 */
	@Column(length=100)
	private String title;
	
	/**
	 * 图文位置（要更新的文章在图文消息中的位置（多图文消息时，此字段才有意义），第一篇为0 ）
	 */
	private Integer articleIndex;
	
	/**
	 * 图文消息的封面图片素材
	 */
	private WechatSource wechatSource;

	/**
	 * 作者
	 */
	@Column(length=32)
	private String author;
	
	/**
	 * 是否显示封面
	 */
	private Integer showCovePic = 0;
	
	/**
	 * 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
	 */
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(columnDefinition="CLOB")
	private String content;
	
	/**
	 * 图文消息的原文地址，即点击“阅读原文”后的URL 
	 */
	@Column(length=200)
	private String sourceUrl;
	
	/**
	 * 文章url
	 *//*
	@Column(length = 200)
	private String url;*/
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public WechatSource getWechatSource() {
		return wechatSource;
	}

	public void setWechatSource(WechatSource wechatSource) {
		this.wechatSource = wechatSource;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public Integer getArticleIndex() {
		return articleIndex;
	}

	public void setArticleIndex(Integer articleIndex) {
		this.articleIndex = articleIndex;
	}

	public WechatArticleGroup getWechatArticleGroup() {
		return wechatArticleGroup;
	}

	public void setWechatArticleGroup(WechatArticleGroup wechatArticleGroup) {
		this.wechatArticleGroup = wechatArticleGroup;
	}

	public Integer getShowCovePic() {
		return showCovePic;
	}

	public void setShowCovePic(Integer showCovePic) {
		this.showCovePic = showCovePic;
	}

}
