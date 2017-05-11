package org.beangle.wechat.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.beangle.model.pojo.LongIdObject;

/**
 * 微信图文列表
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月10日  下午5:19:20
 */
@Entity(name="org.beangle.wechat.core.model.WechatNewsItem")
public class WechatNewsItem extends LongIdObject implements Comparable<WechatNewsItem> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 微信图文模板
	 */
	private WechatTextNewsTemplate wechatTextNewsTemplate;

	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 图片地址
	 */
	private String imagePath;
	
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 排序
	 */
	@Column(nullable = false)
	private Integer orders = 0;
	
	/**
	 * 点击图文跳转URL
	 */
	private String url;

	public WechatTextNewsTemplate getWechatTextNewsTemplate() {
		return wechatTextNewsTemplate;
	}

	public void setWechatTextNewsTemplate(WechatTextNewsTemplate wechatTextNewsTemplate) {
		this.wechatTextNewsTemplate = wechatTextNewsTemplate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public int compareTo(WechatNewsItem wechatNewsItem) {
		int orders = this.orders;
		int another = wechatNewsItem.getOrders();
		if(orders > another){
			return 1;
		}else{
			return 0;
		}
	}
}
