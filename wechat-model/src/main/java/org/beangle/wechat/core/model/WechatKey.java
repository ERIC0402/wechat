package org.beangle.wechat.core.model;

import javax.persistence.Entity;

import org.beangle.model.pojo.LongIdObject;

/**
 * 微信关键字
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月10日  下午5:02:52
 */
@Entity(name="org.beangle.wechat.core.model.WechatKey")
public class WechatKey extends LongIdObject {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 关键字
	 */
	private String keyWord;
	
	/**
	 * 关联模板名称
	 */
	private WechatTextNewsTemplate wechatTextNewsTemplate;
	
	/**
	 * 微信公众号
	 */
	private WechatAccount wechatAccount;
	
	/**
	 * 微信企业应用
	 */
	private WechatCorpApp wechatCorpApp;

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public WechatTextNewsTemplate getWechatTextNewsTemplate() {
		return wechatTextNewsTemplate;
	}

	public void setWechatTextNewsTemplate(
			WechatTextNewsTemplate wechatTextNewsTemplate) {
		this.wechatTextNewsTemplate = wechatTextNewsTemplate;
	}

	public WechatAccount getWechatAccount() {
		return wechatAccount;
	}

	public void setWechatAccount(WechatAccount wechatAccount) {
		this.wechatAccount = wechatAccount;
	}

	public WechatCorpApp getWechatCorpApp() {
		return wechatCorpApp;
	}

	public void setWechatCorpApp(WechatCorpApp wechatCorpApp) {
		this.wechatCorpApp = wechatCorpApp;
	}

}
