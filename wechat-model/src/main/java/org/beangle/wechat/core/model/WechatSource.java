package org.beangle.wechat.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.beangle.model.pojo.LongIdObject;
import org.beangle.website.system.model.DictData;

/**
 * 微信素材
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月10日  下午13:22:22
 */
@Entity(name="org.beangle.wechat.core.model.WechatSource")
public class WechatSource extends LongIdObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 媒体文件唯一标识
	 */
	@Column(length=64)
	private String mediaId;
	
	/**
	 * 服务器此资源URL
	 */
	@Column(length = 200)
	private String url;
	
	/**
	 * 媒体文件类型 有图片（image）、语音（voice）、视频（video）和缩略图（thumb，主要用于视频与音乐格式的缩略图）
	 */
	private DictData type;
	
	/**
	 * 文件名
	 */
	@Column(length = 32)
	private String fileName;
	
	/**
	 * 文件路径
	 */
	@Column(length = 100)
	private String filePath;
	
	/**
	 * 微信公众号
	 */
	private WechatAccount wechatAccount;

	public WechatAccount getWechatAccount() {
		return wechatAccount;
	}

	public void setWechatAccount(WechatAccount wechatAccount) {
		this.wechatAccount = wechatAccount;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public DictData getType() {
		return type;
	}

	public void setType(DictData type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
