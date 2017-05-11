package org.beangle.wechat.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.beangle.model.pojo.LongIdObject;

/**
 * 
 * 微信授权用户详细信息
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年5月15日  上午11:51:51
 */
@Entity(name="org.beangle.wechat.core.model.WechatUser")
public class WechatUser extends LongIdObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	 */
	@Column(length = 100)
	private String openid;
	
	/**
	 * 用户昵称 
	 */
	private String nickname;
	
	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知 
	 */
	private String sex;
	
	/**
	 * 用户个人资料填写的省份 
	 */
	private String province;
	
	/**
	 * 普通用户个人资料填写的城市 
	 */
	private String city;
	
	/**
	 * 国家，如中国为CN 
	 */
	private String country;
	
	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空 
	 */
	private String headimgurl;
	
	/**
	 * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom） 
	 */
	private String privilege;

	/**
	 * Web安全Token
	 */
	@Column(length = 200)
	private String webAccessToken;
	
	/**
	 * Web安全Token到期时间
	 */
	private Date webAccessTokenTime;
	
	/**
	 * Web刷新Token
	 */
	@Column(length = 200)
	private String webRefreshToken;
	
	/**
	 * 用户授权的作用域，使用逗号（,）分隔 
	 */
	@Column(length = 50)
	private String scope;
	
	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
	 */
	@Column(length = 100)
	private String unionid;
	
	/**
	 * 纬度（GPS坐标）
	 */
	private Double nowLatitude;
	
	/**
	 * 经度（GPS坐标）
	 */
	private Double nowLongitude;
	
	/**
	 * 地方
	 */
	@Column(length = 100)
	private String nowPlace;
	
	/**
	 * 位置时间
	 */
	private Date positionTime;
	
	/**
	 * 微信公众号
	 */
	private WechatAccount wechatAccount;
	
	public String getWebAccessToken() {
		return webAccessToken;
	}

	public void setWebAccessToken(String webAccessToken) {
		this.webAccessToken = webAccessToken;
	}

	public Date getWebAccessTokenTime() {
		return webAccessTokenTime;
	}

	public void setWebAccessTokenTime(Date webAccessTokenTime) {
		this.webAccessTokenTime = webAccessTokenTime;
	}

	public String getWebRefreshToken() {
		return webRefreshToken;
	}

	public void setWebRefreshToken(String webRefreshToken) {
		this.webRefreshToken = webRefreshToken;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public Double getNowLatitude() {
		return nowLatitude;
	}

	public void setNowLatitude(Double nowLatitude) {
		this.nowLatitude = nowLatitude;
	}

	public Double getNowLongitude() {
		return nowLongitude;
	}

	public void setNowLongitude(Double nowLongitude) {
		this.nowLongitude = nowLongitude;
	}

	public String getNowPlace() {
		return nowPlace;
	}

	public void setNowPlace(String nowPlace) {
		this.nowPlace = nowPlace;
	}

	public WechatAccount getWechatAccount() {
		return wechatAccount;
	}

	public void setWechatAccount(WechatAccount wechatAccount) {
		this.wechatAccount = wechatAccount;
	}

	public Date getPositionTime() {
		return positionTime;
	}

	public void setPositionTime(Date positionTime) {
		this.positionTime = positionTime;
	}

	
}
