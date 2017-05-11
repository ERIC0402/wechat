package org.beangle.wechat.json.model;

import java.util.List;

import org.beangle.commons.collection.CollectUtils;
import org.beangle.wechat.json.model.NewsMessage.NewsMessageItem;

public class NewsCorpMessage {

	/**
	 * 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	 */
	private String touser;

	/**
	 * 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
	 */
	private String toparty;

	/**
	 * 标签ID列表，多个接收者用‘|’分隔。当touser为@all时忽略本参数
	 */
	private String totag;

	/**
	 * 消息类型，此时固定为：news
	 */
	private String msgtype = "news";

	/**
	 * 企业应用的id，整型。可在应用的设置页面查看
	 */
	private String agentid;

	/**
	 * 消息
	 */
	private CorpNews news;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getToparty() {
		return toparty;
	}

	public void setToparty(String toparty) {
		this.toparty = toparty;
	}

	public String getTotag() {
		return totag;
	}

	public void setTotag(String totag) {
		this.totag = totag;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public CorpNews getNews() {
		return news;
	}

	public void setNews(CorpNews news) {
		this.news = news;
	}

	/**
	 * 消息
	 * 
	 * 作者：王政 创建时间：2015年8月25日 下午5:12:27
	 */
	public static class CorpNews {

		/**
		 * 图文消息，一个图文消息支持1到10条图文
		 */
		private List<NewsMessageItem> articles = CollectUtils.newArrayList();

		public List<NewsMessageItem> getArticles() {
			return articles;
		}

		public void setArticles(List<NewsMessageItem> articles) {
			this.articles = articles;
		}

	}

}