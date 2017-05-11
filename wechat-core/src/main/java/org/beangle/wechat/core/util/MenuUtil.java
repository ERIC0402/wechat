package org.beangle.wechat.core.util;

import java.util.List;

import org.beangle.commons.collection.CollectUtils;

public class MenuUtil {

	/**
	 * 按钮
	 * 
	 * @作者 王政
	 * @公司 上海彦致信息技术有限公司
	 * @创建时间 2015年2月6日  下午3:41:16
	 */
	public static class Button {

		/**
		 * 按钮名称
		 */
		private String name;
		
		/**
		 * 触发或者链接（click、view）
		 */
		private String type;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}
	
	/**
	 * 触发按钮
	 * 
	 * @作者 王政
	 * @公司 上海彦致信息技术有限公司
	 * @创建时间 2015年2月6日 下午3:53:38
	 */
	public static class ClickButton extends Button {
		
		private String key;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}
	
	/**
	 * 复杂按钮（包含二级按钮）
	 * 
	 * @作者 王政
	 * @公司 上海彦致信息技术有限公司
	 * @创建时间 2015年2月15日  下午5:31:37
	 */
	public static class ComplexButton extends Button {
		
		private List<Button> sub_button = CollectUtils.newArrayList();

		public List<Button> getSub_button() {
			return sub_button;
		}

		public void setSub_button(List<Button> sub_button) {
			this.sub_button = sub_button;
		}

	}
	
	/**
	 * 菜单
	 * 
	 * @作者 王政
	 * @公司 上海彦致信息技术有限公司
	 * @创建时间 2015年2月6日  下午3:42:24
	 */
	public static class Menu {
		
		/**
		 * 按钮组
		 */
		private List<Button> button = CollectUtils.newArrayList();

		public List<Button> getButton() {
			return button;
		}

		public void setButton(List<Button> button) {
			this.button = button;
		}

	}
	
	/**
	 * 网页连接按钮
	 * 
	 * @作者 王政
	 * @公司 上海彦致信息技术有限公司
	 * @创建时间 2015年2月6日  下午3:51:59
	 */
	public static class UrlButton extends Button{
		
		private String url;
		 
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
}
