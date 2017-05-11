package org.beangle.wechat.core.service.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.beangle.commons.collection.CollectUtils;
import org.beangle.model.query.builder.OqlBuilder;
import org.beangle.wechat.core.model.WechatAccount;
import org.beangle.wechat.core.model.WechatCorpApp;
import org.beangle.wechat.core.model.WechatCorpDepartment;
import org.beangle.wechat.core.model.WechatCorpUser;
import org.beangle.wechat.core.model.WechatUser;
import org.beangle.wechat.core.service.WechatUserService;
import org.beangle.wechat.core.util.WechatUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WechatUserServiceImpl extends BaseWechatImpl implements WechatUserService {
	
	@Override
	public JSONObject getUserInfo(String webAccessToken, String openID, String lang) {
		String requestUrl = String.format(WechatUtil.WEB_USER_INFO_URL, webAccessToken, openID, lang);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
		if(jsonObject != null && !jsonObject.containsKey("errcode")){
			return jsonObject;
		}
		return null;
	}
	
	public JSONObject getUserInfoUnionID(String accessToken, String openID, String lang) {
		String requestUrl = String.format(WechatUtil.WEB_USER_INFO_UNIONID_URL, accessToken, openID, lang);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
		if(jsonObject != null && !jsonObject.containsKey("errcode")){
			return jsonObject;
		}
		return null;
	}
	
	public JSONObject getWechatCorpUser(String accessToken, String userID) {
		String requestUrl = String.format(WechatUtil.CORP_USER_INFO_URL, accessToken, userID);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
		if(jsonObject != null && jsonObject.containsValue("ok")){
			return jsonObject;
		}
		return null;
	}
	
	public JSONObject getWechatCorpDepartment(String accessToken, String departmentId) {
		String requestUrl = String.format(WechatUtil.CORP_DEPARTMENT_LIST_URL, accessToken, departmentId);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
		if(jsonObject != null && jsonObject.containsValue("ok")){
			return jsonObject;
		}
		return null;
	}
	
	@Override
	public WechatUser saveOrUpdateWechatUser(WechatAccount wechatAccount, String accessToken, String openID, String lang) {
		WechatUser wechatUser = getWechatUser(wechatAccount, openID);
		if(wechatUser == null){
			wechatUser = new WechatUser();
		}
		JSONObject userJsonObject = getUserInfoUnionID(accessToken, openID, lang);
		if(userJsonObject != null){
			saveWechatUser(wechatAccount, wechatUser, userJsonObject);
			return wechatUser;
		}else{
			return null;
		}
	}

	@Override
	public WechatUser saveWechatUser(WechatAccount wechatAccount, WechatUser wechatUser, JSONObject userJsonObject) {
		wechatUser.setNickname(userJsonObject.getString("nickname"));
		wechatUser.setSex(userJsonObject.getString("sex"));
		wechatUser.setProvince(userJsonObject.getString("province"));
		wechatUser.setCity(userJsonObject.getString("city"));
		wechatUser.setCountry(userJsonObject.getString("country"));
		wechatUser.setHeadimgurl(userJsonObject.getString("headimgurl"));
		wechatUser.setOpenid(userJsonObject.getString("openid"));
		if(userJsonObject.has("privilege")){
			wechatUser.setPrivilege(userJsonObject.getString("privilege"));
		}
		wechatUser.setWechatAccount(wechatAccount);
		entityDao.saveOrUpdate(wechatUser);
		return wechatUser;
	}
	
	@Override
	public WechatUser getWechatUser(WechatAccount wechatAccount, String openID) {
		List<WechatUser> wechatUsers = entityDao.get(WechatUser.class, new String[]{"openid", "wechatAccount"}, new Object[]{openID, wechatAccount});
		if(wechatUsers != null && wechatUsers.size() > 0){
			return wechatUsers.get(0);
		}
		return null;
	}

	@Override
	public synchronized WechatCorpUser saveOrUpdateWechatCorpUser(WechatAccount wechatAccount, String accessToken, String userID) {
		JSONObject userJsonObject = getWechatCorpUser(accessToken, userID);
		if(userJsonObject != null){
			WechatCorpUser wechatCorpUser = getWechatCorpUserBySave(wechatAccount, userID);
			wechatCorpUser.setUserid(userJsonObject.getString("userid"));
			wechatCorpUser.setName(userJsonObject.getString("name"));
			wechatCorpUser.getDepartments().clear();
			wechatCorpUser.getDepartments().addAll(buildWechatCorpDepeartment(wechatAccount, accessToken, userJsonObject.getJSONArray("department")));
			if(userJsonObject.has("position")){
				wechatCorpUser.setPosition(userJsonObject.getString("position"));
			}
			if(userJsonObject.has("mobile")){
				wechatCorpUser.setMobile(userJsonObject.getString("mobile"));
			}
			if(userJsonObject.has("gender")){
				wechatCorpUser.setGender(userJsonObject.getString("gender"));
			}
			if(userJsonObject.has("email")){
				wechatCorpUser.setEmail(userJsonObject.getString("email"));
			}
			if(userJsonObject.has("weixinid")){
				wechatCorpUser.setWeixinid(userJsonObject.getString("weixinid"));
			}
			if(userJsonObject.has("avatar")) {
				wechatCorpUser.setAvatar(userJsonObject.getString("avatar"));
			}
			wechatCorpUser.setStatus(userJsonObject.getString("status"));
			wechatCorpUser.setExtattr(userJsonObject.getString("extattr"));
			wechatCorpUser.setWechatAccount(wechatAccount);
			entityDao.saveOrUpdate(wechatCorpUser);
			return wechatCorpUser;
		}else{
			return null;
		}
	}

	private List<WechatCorpDepartment> buildWechatCorpDepeartment(WechatAccount wechatAccount, String accessToken, JSONArray jsonArray) {
		List<WechatCorpDepartment> wechatCorpDepartments = CollectUtils.newArrayList();
		for(@SuppressWarnings("unchecked")
		Iterator<Integer> departmentItor = jsonArray.iterator(); departmentItor.hasNext();){
			JSONObject jsonObject = getWechatCorpDepartment(accessToken, departmentItor.next().toString());
			if(jsonObject != null && jsonObject.containsValue("ok")) {
				JSONObject departmentJsonObject = jsonObject.getJSONArray("department").getJSONObject(0);
				String departmentId = departmentJsonObject.getString("id");
				WechatCorpDepartment wechatCorpDepartment = getWechatDepartment(wechatAccount, departmentId);
				wechatCorpDepartment.setDepartmentId(departmentId);
				wechatCorpDepartment.setName(departmentJsonObject.getString("name"));
				wechatCorpDepartment.setParentId(departmentJsonObject.getString("parentid"));
				wechatCorpDepartment.setOrders(departmentJsonObject.getString("order"));
				wechatCorpDepartment.setWechatAccount(wechatAccount);
				entityDao.saveOrUpdate(wechatCorpDepartment);
				wechatCorpDepartments.add(wechatCorpDepartment);
			}
		}
		return wechatCorpDepartments;
	}

	private WechatCorpDepartment getWechatDepartment(WechatAccount wechatAccount, String departmentId) {
		WechatCorpDepartment wechatCorpDepartment = new WechatCorpDepartment();
		List<WechatCorpDepartment> departments = entityDao.get(WechatCorpDepartment.class, new String[]{"wechatAccount", "departmentId"}, new Object[]{wechatAccount, departmentId});
		if(departments != null && departments.size() > 0) {
			wechatCorpDepartment = departments.get(0);
		}
		return wechatCorpDepartment;
	}
	
	private synchronized WechatCorpUser getWechatCorpUserBySave(WechatAccount wechatAccount, String userID) {
		List<WechatCorpUser> wechatUsers = entityDao.get(WechatCorpUser.class, new String[]{"userid", "wechatAccount"}, new Object[]{userID, wechatAccount});
		if(wechatUsers != null && wechatUsers.size() > 0){
			return wechatUsers.get(0);
		}else {
			return new WechatCorpUser();
		}
	}
	
	@Override
	public WechatCorpUser getWechatCorpUser(WechatAccount wechatAccount, String userID) {
		List<WechatCorpUser> wechatUsers = entityDao.get(WechatCorpUser.class, new String[]{"userid", "wechatAccount"}, new Object[]{userID, wechatAccount});
		if(wechatUsers != null && wechatUsers.size() > 0){
			return wechatUsers.get(0);
		}
		return null;
	}
	
	@Override
	public List<WechatCorpUser> findWechatCorpUser(WechatAccount wechatAccount) {
		List<WechatCorpUser> wechatUsers = entityDao.get(WechatCorpUser.class, new String[]{"wechatAccount"}, new Object[]{wechatAccount});
		return wechatUsers;
	}
	
	@Override
	public List<WechatUser> findWechatUser(WechatAccount wechatAccount) {
		List<WechatUser> wechatUsers = entityDao.get(WechatUser.class, new String[]{"wechatAccount"}, new Object[]{wechatAccount});
		return wechatUsers;
	}
	
	@Override
	public Map<String, List<WechatCorpUser>> findDepartmentWechatCorpUser(WechatAccount wechatAccount) {
		return groupWechatCorpUserByDepartment(findWechatCorpUser(wechatAccount));
	}
	
	@Override
	public JSONObject getUserId(String accessToken, String code) {
		String requestUrl = String.format(WechatUtil.WEB_CORP_USER_INFO_URL, accessToken, code);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
		if(jsonObject != null && jsonObject.containsKey("UserId")){
			return jsonObject;
		}
		return null;
	}

	@Override
	public String getTagidByName(String accessToken, String name) {
		String requestUrl = String.format(WechatUtil.WECHAT_CORP_TAG_LIST, accessToken);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
		if(jsonObject != null && jsonObject.containsKey("taglist")){
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> itor = jsonObject.getJSONArray("taglist").iterator();
			while(itor.hasNext()){
				JSONObject json = itor.next();
				if(json.containsValue(name)){
					return json.getString("tagid");
				}
			}
		}
		return null;
	}

	@Override
	public Set<WechatCorpUser> findWechatCorpUserByTagid(WechatAccount wechatAccount, String accessToken, String tagid) {
		String requestUrl = String.format(WechatUtil.WECHAT_CORP_TAG_USER, accessToken, tagid);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "GET", null);
		Set<WechatCorpUser> wechatCorpUsers = CollectUtils.newHashSet();
		if(jsonObject != null){
			if(jsonObject.containsKey("userlist")){
				@SuppressWarnings("unchecked")
				Iterator<JSONObject> itor = jsonObject.getJSONArray("userlist").iterator();
				while(itor.hasNext()){
					JSONObject json = itor.next();
					WechatCorpUser wechatCorpUser = getWechatCorpUser(wechatAccount, json.getString("userid"));
					if(wechatCorpUser != null){
						wechatCorpUsers.add(wechatCorpUser);
					}
				}
			}
			if(jsonObject.containsKey("partylist")){
				@SuppressWarnings("unchecked")
				Iterator<Integer> itor = jsonObject.getJSONArray("partylist").iterator();
				while(itor.hasNext()){
					Integer departmentId = itor.next();
					WechatCorpDepartment wechatCorpDepartment = getWechatDepartment(wechatAccount, departmentId.toString());
					List<WechatCorpUser> wechatCorpDepartmentUsers = findWechatCorpUserOfDepartmentName(wechatAccount, wechatCorpDepartment.getName());
					if(wechatCorpDepartmentUsers != null){
						wechatCorpUsers.addAll(wechatCorpDepartmentUsers);
					}
				}
			}
		}
		return wechatCorpUsers;
	}
	
	public Set<WechatCorpUser> findWechatCorpUserByTagidAndDepartment(WechatAccount wechatAccount, String accessToken, String tagid, List<WechatCorpDepartment> wechatCorpDepartments) {
		if(StringUtils.isNotBlank(accessToken) && StringUtils.isNotBlank(tagid)){
			Set<WechatCorpUser> wechatCorpUsers = findWechatCorpUserByTagid(wechatAccount, accessToken, tagid);
			Map<String, List<WechatCorpUser>> map = groupWechatCorpUserByDepartment(wechatCorpUsers);
			Set<WechatCorpUser> users = CollectUtils.newHashSet();
			for(WechatCorpDepartment department : wechatCorpDepartments) {
				users.addAll(map.get(department.getName()));
			}
			return users;
		}
		return null;
	}
	
	public Set<WechatCorpUser> findWechatCorpUserByTag(WechatCorpApp wechatCorpApp, WechatCorpUser wechatCorpUser, String tag) {
		String accessToken = wechatAccountService.getCorpAppAccessToken(wechatCorpApp);
		String tagid = wechatUserService.getTagidByName(accessToken, tag);
		if(StringUtils.isNotBlank(tagid)){
			return findWechatCorpUserByTagid(wechatCorpUser.getWechatAccount(), accessToken, tagid);
		}
		return null;
	}
	
	@Override
	public List<WechatCorpUser> findWechatCorpUserOfDepartmentName(WechatAccount wechatAccount, String departmentName) {
		OqlBuilder<WechatCorpUser> builder = OqlBuilder.from(WechatCorpUser.class, "wechatCorpUser");
		builder.join("wechatCorpUser.departments", "department");
		builder.where("department.name = :departmentName", departmentName);
		builder.where("wechatCorpUser.wechatAccount = :wechatAccount", wechatAccount);
		return entityDao.search(builder);
	}

	@Override
	public Map<String, List<WechatCorpUser>> findWechatCorpUserOfDepartment(WechatAccount wechatAccount, List<WechatCorpDepartment> wechatCorpDepartments) {
		Map<String, List<WechatCorpUser>> departmentWechatCorpUserMap = findDepartmentWechatCorpUser(wechatAccount);
		Map<String, List<WechatCorpUser>> map = CollectUtils.newHashMap();
		for(WechatCorpDepartment wechatCorpDepartment : wechatCorpDepartments) {
			map.put(wechatCorpDepartment.getName(), departmentWechatCorpUserMap.get(wechatCorpDepartment.getName()));
		}
		return map;
	}

	private Map<String, List<WechatCorpUser>> groupWechatCorpUserByDepartment(Collection<WechatCorpUser> wechatCorpUsers) {
		Map<String, List<WechatCorpUser>> map = CollectUtils.newHashMap();
		if(wechatCorpUsers != null && wechatCorpUsers.size() > 0) {
			for(WechatCorpUser wechatCorpUser : wechatCorpUsers) {
				List<WechatCorpDepartment> departments = wechatCorpUser.getDepartments();
				if(departments != null) {
					for(WechatCorpDepartment department : departments) {
						List<WechatCorpUser> corpUsers = map.get(department.getName());
						if(corpUsers == null) {
							corpUsers = CollectUtils.newArrayList();
						}
						corpUsers.add(wechatCorpUser);
						map.put(department.getName(), corpUsers);
					}
				}
			}
		}
		return map;
	}
	
}
