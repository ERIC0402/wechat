package org.beangle.wechat.core.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.beangle.commons.collection.CollectUtils;
import org.beangle.model.pojo.LongIdObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 企业用户信息
 * 
 * @作者 王政
 * @创建时间 2015年8月3日 上午11:10:38
 */
@Entity(name = "org.beangle.wechat.core.model.WechatCorpUser")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"userid", "wechat_Account_id"})})
public class WechatCorpUser extends LongIdObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 成员UserID。对应管理端的帐号，企业内必须唯一。长度为1~64个字节
	 */
	@Column(length = 64)
	private String userid;
	
	/**
	 * 成员名称。长度为1~64个字节
	 */
	@Column(length = 64)
	private String name;

	/**
	 * 成员所属部门id列表。注意，每个部门的直属成员上限为1000个
	 */
	@ManyToMany
	@JoinTable(name = "PTW_WHT_USER_DEPARTMENTS")
	private List<WechatCorpDepartment> departments = CollectUtils.newArrayList();

	/**
	 * 职位信息。长度为0~64个字节
	 */
	@Column(length = 64)
	private String position;

	/**
	 * 手机号码。企业内必须唯一，mobile/weixinid/email三者不能同时为空
	 */
	@Column(length = 18)
	private String mobile;

	/**
	 * 性别。1表示男性，2表示女性
	 */
	@Column(length = 1)
	private String gender;

	/**
	 * 邮箱。长度为0~64个字节。企业内必须唯一
	 */
	@Column(length = 64)
	private String email;

	/**
	 * 微信号。企业内必须唯一。（注意：是微信号，不是微信的名字）
	 */
	@Column(length = 64)
	private String weixinid;

	/**
	 * 头像url。注：如果要获取小图将url最后的"/0"改成"/64"即可
	 */
	@Column(length = 200)
	private String avatar;

	/**
	 * 关注状态: 1=已关注，2=已冻结，4=未关注
	 */
	@Column(length = 1)
	private String status;

	/**
	 * 扩展属性。扩展属性需要在WEB管理端创建后才生效，否则忽略未知属性的赋值
	 */
	@Column(length = 100)
	private String extattr;

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
	 * 微信企业应用
	 */
	private WechatAccount wechatAccount;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWeixinid() {
		return weixinid;
	}

	public void setWeixinid(String weixinid) {
		this.weixinid = weixinid;
	}

	public String getExtattr() {
		return extattr;
	}

	public void setExtattr(String extattr) {
		this.extattr = extattr;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
	public String getDepartment() {
		StringBuilder sb = new StringBuilder();
		for(WechatCorpDepartment department : departments) {
			sb.append(department.getName());
			sb.append("、");
		}
		if(sb.length() > 0) {
			return sb.deleteCharAt(sb.length() - 1).toString();
		}else {
			return "";
		}
	}

	public List<WechatCorpDepartment> getDepartments() {
		return departments;
	}

	public void setDepartments(List<WechatCorpDepartment> departments) {
		this.departments = departments;
	}

	public Date getPositionTime() {
		return positionTime;
	}

	public void setPositionTime(Date positionTime) {
		this.positionTime = positionTime;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getExtattrMap() {
		Map<String, String> map = CollectUtils.newHashMap();
		if(StringUtils.isNotBlank(extattr)) {
			JSONObject jsonObjectExattr = JSONObject.fromObject(extattr);
			if(jsonObjectExattr != null && jsonObjectExattr.has("attrs")) {
				JSONArray jsonAttrs = jsonObjectExattr.getJSONArray("attrs");
				for(Iterator<JSONObject> itor = jsonAttrs.iterator(); itor.hasNext();) {
					JSONObject jsonObject = itor.next();
					map.put(jsonObject.getString("name"), jsonObject.getString("value"));
				}
			}
		}
		return map;
	}
	
	public String getJobNumber() {
		return getExtattrMap().get("工号");
	}

}
