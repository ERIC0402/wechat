package org.beangle.wechat.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.beangle.model.pojo.LongIdObject;

/**
 * 图片
 * 
 * @作者 王政
 * @创建时间 2015年7月28日 上午10:15:28
 */
@Entity(name="org.beangle.wechat.core.model.WechatImage")
public class WechatImage extends LongIdObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 消息创建时间 （整型）
	 */
	@Column(length = 20)
	private String createTime;
	
	/**
	 * 图片链接
	 */
	@Column(length = 200)
	private String picUrl;
	
	/**
	 * 图片消息媒体id
	 */
	@Column(length = 100)
	private String mediaId;
	
	/**
	 * 消息id（64位整型）
	 */
	@Column(length = 80)
	private String msgId;
	
	/**
	 * 文件Md5
	 */
	@Column(length = 50)
	private String picMd5Sum;
	
	/**
	 * 路径
	 */
	@Column(length = 200)
	private String filePath;
	
	/**
	 * 图片回调类全名称
	 */
	@Column(length = 100)
	private String clazzName;
	
	/**
	 * 微信用户
	 */
	private WechatUser wechatUser;
	
	/**
	 * 微信企业用户
	 */
	private WechatCorpUser wechatCorpUser;
	
	public WechatUser getWechatUser() {
		return wechatUser;
	}

	public void setWechatUser(WechatUser wechatUser) {
		this.wechatUser = wechatUser;
	}

	public WechatCorpUser getWechatCorpUser() {
		return wechatCorpUser;
	}

	public void setWechatCorpUser(WechatCorpUser wechatCorpUser) {
		this.wechatCorpUser = wechatCorpUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getPicMd5Sum() {
		return picMd5Sum;
	}

	public void setPicMd5Sum(String picMd5Sum) {
		this.picMd5Sum = picMd5Sum;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

}
