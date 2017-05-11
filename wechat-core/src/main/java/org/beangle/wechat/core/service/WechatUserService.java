package org.beangle.wechat.core.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.beangle.wechat.core.model.WechatAccount;
import org.beangle.wechat.core.model.WechatCorpApp;
import org.beangle.wechat.core.model.WechatCorpDepartment;
import org.beangle.wechat.core.model.WechatCorpUser;
import org.beangle.wechat.core.model.WechatUser;

import net.sf.json.JSONObject;

/**
 * 
 * 微信用户信息接口
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年5月15日  上午11:51：51
 */
public interface WechatUserService {
	
	/**
	 * 获取用户信息
	 * @param webAccessToken
	 * @param openID
	 * @param lang
	 * @return
	 */
	public JSONObject getUserInfo(String webAccessToken, String openID, String lang);
	
	/**
	 * 获取微信企业用户信息
	 * @param accessToken
	 * @param userID
	 * @return
	 */
	public JSONObject getWechatCorpUser(String accessToken, String userID);
	
	/**
	 * 获取微信用户信息
	 * @param openID
	 * @return
	 */
	public WechatUser getWechatUser(WechatAccount wechatAccount, String openID);

	
	/**
	 * 获取微信用户信息（Union机制）
	 * @param accessToken
	 * @param openID
	 * @param lang
	 * @return
	 */
	public WechatUser saveOrUpdateWechatUser(WechatAccount wechatAccount, String accessToken, String openID, String lang);
	
	/**
	 * 获取微信企业用户信息
	 * @param accessToken
	 * @param userID
	 * @return
	 */
	public WechatCorpUser saveOrUpdateWechatCorpUser(WechatAccount wechatAccount, String accessToken, String userID);

	/**
	 * 获取微信企业用户信息
	 * @param wechatAccount
	 * @param userID
	 * @return
	 */
	public WechatCorpUser getWechatCorpUser(WechatAccount wechatAccount, String userID);
	
	/**
	 * 获取企业用户信息
	 * @param accessToken
	 * @param code
	 * @return UserId 成员UserID 或 OpenId 非企业成员的标识，对当前企业号唯一  
	 */
	public JSONObject getUserId(String accessToken, String code);

	/**
	 * 保存微信用户信息
	 * @param wechatAccount
	 * @param wechatUser
	 * @param userJsonObject
	 * @return 
	 */
	public WechatUser saveWechatUser(WechatAccount wechatAccount, WechatUser wechatUser, JSONObject userJsonObject);

	/**
	 * 获取所有企业用户
	 * @param wechatAccount
	 * @return
	 */
	public List<WechatCorpUser> findWechatCorpUser(WechatAccount wechatAccount);
	
	/**
	 * 获取所有企业用户（按部门划分）
	 * @param wechatAccount
	 * @return
	 */
	public Map<String, List<WechatCorpUser>> findDepartmentWechatCorpUser(WechatAccount wechatAccount);
	
	/**
	 * 获取指定部门名称的企业用户
	 * @param wechatAccount
	 * @return
	 */
	public List<WechatCorpUser> findWechatCorpUserOfDepartmentName(WechatAccount wechatAccount, String departmentName);
	
	/**
	 * 获取指定部门的企业用户
	 * @param wechatAccount
	 * @return
	 */
	public Map<String, List<WechatCorpUser>> findWechatCorpUserOfDepartment(WechatAccount wechatAccount, List<WechatCorpDepartment> wechatCorpDepartments);
	
	/**
	 * 获取标签id根据标签名
	 * 
	 * @param accessToken
	 * @param name
	 * @return
	 */
	public String getTagidByName(String accessToken, String name);
	
	/**
	 * 根据标签id获取微信企业成员
	 * 
	 * @param wechatAccount
	 * @param accessToken
	 * @param tagid
	 * @return
	 */
	public Set<WechatCorpUser> findWechatCorpUserByTagid(WechatAccount wechatAccount, String accessToken, String tagid);
	
	/**
	 * 根据标签id和部门获取微信企业成员
	 * 
	 * @param wechatAccount
	 * @param accessToken
	 * @param tagid
	 * @return
	 */
	public Set<WechatCorpUser> findWechatCorpUserByTagidAndDepartment(WechatAccount wechatAccount, String accessToken, String tagid, List<WechatCorpDepartment> wechatCorpDepartments);
	
	/**
	 * 根据标签名获取微信企业成员
	 * 
	 * @param wechatCorpApp
	 * @param wechatCorpUser
	 * @param tag
	 * @return
	 */
	public Set<WechatCorpUser> findWechatCorpUserByTag(WechatCorpApp wechatCorpApp, WechatCorpUser wechatCorpUser, String tag);

	/**
	 * 获得所有订阅号/服务号用户
	 * @param wechatAccount
	 * @return
	 */
	public List<WechatUser> findWechatUser(WechatAccount wechatAccount);
}
