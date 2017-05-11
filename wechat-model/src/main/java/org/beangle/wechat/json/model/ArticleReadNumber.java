package org.beangle.wechat.json.model;

import java.util.List;

import org.beangle.commons.collection.CollectUtils;

/**
 * 微信文章统计数据
 * 
 * @作者 王政
 * @创建时间 2015年7月17日 上午9:50:50
 */
public class ArticleReadNumber {
	
	/**
	 * 图文统计数据集合
	 */
	private List<ArticleReadNumberItem> list = CollectUtils.newArrayList();

	public List<ArticleReadNumberItem> getList() {
		return list;
	}

	public void setList(List<ArticleReadNumberItem> list) {
		this.list = list;
	}
	
	/**
	 * 单个图文统计数据
	 * 
	 * @作者 王政
	 * @创建时间 2015年7月17日 上午9:50:50
	 */
	public class ArticleReadNumberItem {
		
		/**
		 * 数据的日期，需在begin_date和end_date之间
		 */
		private String ref_date;
		
	    /**
	     * 图文页（点击群发图文卡片进入的页面）的阅读人数 
	     */
	    private String int_page_read_user;
	    
	    /**
	     * 图文消息id和消息次序索引组成， 例如12003_3
	     */
	    private String msgid;
	    
	    /**
	     * 图文页的阅读次数
	     */
	    private String int_page_read_count;
	    
	    /**
	     * 原文页（点击图文页“阅读原文”进入的页面）的阅读人数，无原文页时此处数据为0
	     */
	    private String ori_page_read_user;
	    
	    /**
	     * 图文页的阅读次数
	     */
	    private String ori_page_read_count;
	    
	    /**
	     * 分享的人数
	     */
	    private String share_user;
	    
	    /**
	     * 分享的次数
	     */
	    private String share_count;
	    
	    /**
	     * 收藏的人数
	     */
	    private String add_to_fav_user;
	    
	    /**
	     * 收藏的次数
	     */
	    private String add_to_fav_count;

		public String getRef_date() {
			return ref_date;
		}

		public void setRef_date(String ref_date) {
			this.ref_date = ref_date;
		}

		public String getInt_page_read_user() {
			return int_page_read_user;
		}

		public void setInt_page_read_user(String int_page_read_user) {
			this.int_page_read_user = int_page_read_user;
		}

		public String getInt_page_read_count() {
			return int_page_read_count;
		}

		public void setInt_page_read_count(String int_page_read_count) {
			this.int_page_read_count = int_page_read_count;
		}

		public String getOri_page_read_user() {
			return ori_page_read_user;
		}

		public void setOri_page_read_user(String ori_page_read_user) {
			this.ori_page_read_user = ori_page_read_user;
		}

		public String getOri_page_read_count() {
			return ori_page_read_count;
		}

		public void setOri_page_read_count(String ori_page_read_count) {
			this.ori_page_read_count = ori_page_read_count;
		}

		public String getShare_user() {
			return share_user;
		}

		public void setShare_user(String share_user) {
			this.share_user = share_user;
		}

		public String getShare_count() {
			return share_count;
		}

		public void setShare_count(String share_count) {
			this.share_count = share_count;
		}

		public String getAdd_to_fav_user() {
			return add_to_fav_user;
		}

		public void setAdd_to_fav_user(String add_to_fav_user) {
			this.add_to_fav_user = add_to_fav_user;
		}

		public String getAdd_to_fav_count() {
			return add_to_fav_count;
		}

		public void setAdd_to_fav_count(String add_to_fav_count) {
			this.add_to_fav_count = add_to_fav_count;
		}

		public String getMsgid() {
			return msgid;
		}

		public void setMsgid(String msgid) {
			this.msgid = msgid;
		}

	}

}
