package org.beangle.wechat.json.model;

import java.util.Map;

import org.beangle.commons.collection.CollectUtils;

public class TemplateMessage {
	
	/**
	 * 公众帐号下模板消息ID 
	 */
	private String template_id;
	
	/**
	 * 用户OpenId
	 */
	private String touser;
	
	/**
	 * 链接
	 */
	private String url;
	
	/**
	 * 顶部颜色
	 */
	private String topcolor;
	
	/**
	 * 模板数据
	 */
	private Map<String, TemplateMessageData> data = CollectUtils.newHashMap();

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTopcolor() {
		return topcolor;
	}

	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}

	public Map<String, TemplateMessageData> getData() {
		return data;
	}

	public void setData(Map<String, TemplateMessageData> data) {
		this.data = data;
	}
	
	public static class TemplateMessageData {

		private String value;
		private String color;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

	}

}
