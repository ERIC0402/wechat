package org.beangle.wechat.core.action;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.beangle.commons.collection.CollectUtils;
import org.beangle.commons.exception.MyException;
import org.beangle.model.Entity;
import org.beangle.model.query.QueryBuilder;
import org.beangle.model.query.builder.OqlBuilder;
import org.beangle.wechat.core.model.WechatAccount;
import org.beangle.wechat.core.model.WechatButton;
import org.beangle.wechat.core.model.WechatCorpApp;
import org.beangle.wechat.core.service.WechatAccountService;
import org.beangle.wechat.core.util.MenuUtil.Button;
import org.beangle.wechat.core.util.MenuUtil.ClickButton;
import org.beangle.wechat.core.util.MenuUtil.ComplexButton;
import org.beangle.wechat.core.util.MenuUtil.Menu;
import org.beangle.wechat.core.util.MenuUtil.UrlButton;
import org.beangle.wechat.core.util.WechatUtil;
import org.beangle.wechat.util.DictDataUtil;
import org.beangle.wechat.util.DictTypeUtil;

/**
 * 微信按钮管理
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  上午10:50:13
 */
public class WechatMenuAction extends BaseWechatAction {
	
	private WechatAccountService wechatAccountService;
	
	@Override
	protected String getEntityName() {
		return WechatButton.class.getName();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected QueryBuilder<?> getQueryBuilder() {
		OqlBuilder<WechatButton> builder = (OqlBuilder<WechatButton>) super.getQueryBuilder();
		buildWhere(builder);
		builder.orderBy(getOrderString("code"));
		return builder;
	}

	private void buildWhere(OqlBuilder<WechatButton> builder) {
		Long wechatAccountID = getEntityId("wechatButton.wechatAccount");
		if(wechatAccountID != null){
			builder.where("wechatAccount.id = :wechatAccountID", wechatAccountID);
		}else{
			Long wechatCorpAppID = getEntityId("wechatButton.wechatCorpApp");
			if(wechatCorpAppID != null){
				builder.where("wechatCorpApp.id = :wechatCorpAppID", wechatCorpAppID);
			}else{
				builder.where("1 = -1");
			}
		}
	}
	
	@Override
	protected void editSetting(Entity<?> entity) {
		WechatButton wechatButton = (WechatButton) entity;
		OqlBuilder<WechatButton> builder = OqlBuilder.from(WechatButton.class);
		if(wechatButton == null || wechatButton.getParentButton() == null){
			if(wechatButton != null && wechatButton.getId() != null){
				List<WechatButton> wechatButtons = entityDao.get(WechatButton.class, "parentButton.id", wechatButton.getId());
				if((wechatButtons != null && wechatButtons.size() > 0) || (wechatButton.getWechatAccount() == null && wechatButton.getWechatCorpApp() == null)){
					builder.where("1 = -1");
				}
				builder.where("id != :wechatButtonIDSelf", wechatButton.getId());
			}
			builder.where("parentButton is null");
		}else if(wechatButton.getParentButton() != null) {
			builder.where("id = :parentButtonID", wechatButton.getParentButton().getId());
		}else{
			builder.where("1 = -1");
		}
		buildWhere(builder);
		put("wechatButtons", entityDao.search(builder));
		put("events", getDictDataByDictTypeCode(DictTypeUtil.WECHAT_EVENT));
	}
	
	@Override
	protected String saveAndForward(Entity<?> entity) {
		try {
			WechatButton wechatButton = (WechatButton) entity;
			if(wechatButton.getParentButton() != null){
				wechatButton.setParentButton(entityDao.get(WechatButton.class, wechatButton.getParentButton().getId()));
			}
			buildOrders(wechatButton);
			entityDao.save(wechatButton);
			return redirect("search", "info.save.success");
		} catch (MyException e) {
			logger.error("saveAndForwad failure", e);
			return redirect("search", e.getMessage());
		} catch (Exception e) {
			logger.info("saveAndForwad failure", e);
			return redirect("search", "info.save.failure");
		}
	}

	private List<WechatButton> buildOrders(WechatButton wechatButton) {
		int index = wechatButton.getOrders();
		List<WechatButton> wechatButtons = findWechatButton(wechatButton, wechatButton.getParentButton(), index);
		List<WechatButton> wechatAllButtons = findWechatButton(wechatButton, wechatButton.getParentButton(), null);
		if(wechatButtons != null && wechatButtons.size() > 0){
			if(wechatAllButtons.size() >= index){
				wechatButton.setCode(index);
				index = wechatAllButtons.size() - wechatButtons.size();
				for(WechatButton button : wechatButtons){
					++index;
					button.setCode(index == wechatButton.getOrders() ? ++index : index);
				}
			}else{
				index = 0;
				for(WechatButton button : wechatAllButtons){
					button.setCode(++index);
				}
				wechatButton.setCode(++index);
			}
		}else{
			index = 0;
			for(WechatButton button : wechatAllButtons){
				button.setCode(++index);
			}
			wechatButton.setCode(++index);
		}
		return wechatButtons;
	}
	
	private List<WechatButton> findWechatButton(WechatButton wechatButton, WechatButton parentButton, Integer index) {
		OqlBuilder<WechatButton> builder = OqlBuilder.from(WechatButton.class, "wechatButton");
		if(wechatButton.getWechatAccount() != null){
			builder.where("wechatButton.wechatAccount = :wechatAccount", wechatButton.getWechatAccount());
		}
		if(wechatButton.getWechatCorpApp() != null){
			builder.where("wechatButton.wechatCorpApp = :wechatCorpApp", wechatButton.getWechatCorpApp());
		}
		if(index != null){
			builder.where("wechatButton.orders >= :index", index);
		}
		if(parentButton == null){
			builder.where("wechatButton.parentButton is null");
		}else{
			builder.where("wechatButton.parentButton = :parentButton", parentButton);
		}
		if(wechatButton.getId() != null){
			builder.where("wechatButton != :wechatButton", wechatButton);
		}
		builder.where("wechatButton.orders is not null");
		builder.orderBy("wechatButton.orders");
		return entityDao.search(builder);
	}
	
	public String subWechatMenu() {
		String message = "提交菜单信息失败！";
		Long wechatAccountID = getEntityId("wechatButton.wechatAccount");
		Long wechatCorpAppID = getEntityId("wechatButton.wechatCorpApp");
		WechatAccount wechatAccount = wechatAccountService.getActiveAccountByWechatAccountID(wechatAccountID);
		if(wechatAccount != null || wechatCorpAppID != null){
			List<WechatButton> wechatButtons = findWechatButton(wechatAccountID, wechatCorpAppID, null);
			Menu menu = new Menu();
			for (int a = 0; a < wechatButtons.size(); a++) {
				WechatButton wechatButton = wechatButtons.get(a);
				List<WechatButton> childList = findWechatButton(wechatAccountID, wechatCorpAppID, wechatButton);
				if (childList.size() == 0) {
					menu.getButton().addAll(builderButton(wechatButton));
				} else {
					List<Button> secondButtons = CollectUtils.newArrayList();
					ComplexButton complexButton = new ComplexButton();
					complexButton.setName(wechatButton.getName());
					for (WechatButton wechatButtonChild : childList) {
						secondButtons.addAll(builderButton(wechatButtonChild));
					}
					complexButton.getSub_button().addAll(secondButtons);
					menu.getButton().add(complexButton);
				}
			}
			try{
				JSONObject jsonMenu = JSONObject.fromObject(menu);
				String url = null;
				if(wechatCorpAppID != null){
					WechatCorpApp wechatCorpApp = wechatAccountService.getActiveCorpApp(wechatCorpAppID);
					url = String.format(WechatUtil.MENU_CREATE_CORP_URL, wechatAccountService.getCorpAppAccessToken(wechatCorpAppID), wechatCorpApp.getAgentid());
				}else{
					url = String.format(WechatUtil.MENU_CREATE_URL, wechatAccountService.getAccessToken(wechatAccountID));
				}
				JSONObject jsonObject= new JSONObject();
				jsonObject = WechatUtil.httpsRequest(url, "POST", jsonMenu.toString());
				if(jsonObject!=null){
					if (0 != jsonObject.getInt("errcode")) {
						message = "提交菜单信息数据失败！错误码为："+jsonObject.getInt("errcode")+"错误信息为："+jsonObject.getString("errmsg");
					}else{
						message = "提交服务器菜单成功！";
					}
				}else{
					message = "提交菜单信息数据失败！提交自定义菜单URL地址不正确";
				}
			}catch(Exception ee){
				message = ee.getMessage();
			}
		}
		return redirect("search", message);
	}

	public String activateMenu() {
		boolean isActivate = getBool("isActivate");
		String message = "激活成功！";
		if(!isActivate){
			message = "冻结成功！";
		}
		List<WechatButton> wechatButtons = entityDao.get(WechatButton.class, getEntityIds());
		for(WechatButton wechatButton : wechatButtons){
			wechatButton.setStatus(isActivate);
		}
		entityDao.saveOrUpdate(wechatButtons);
		return redirect("search", message);
	}
	
	public String findWechatServerMenu() {
		String message = "获取服务器菜单失败！";
		Long wechatAccountID = getEntityId("wechatButton.wechatAccount");
		Long wechatCorpAppID = getEntityId("wechatButton.wechatCorpApp");
		WechatAccount wechatAccount = wechatAccountService.getActiveAccountByWechatAccountID(wechatAccountID);
		WechatCorpApp wechatCorpApp = wechatAccountService.getActiveCorpApp(wechatCorpAppID);
		if(wechatAccount != null || wechatCorpApp != null){
			String url = null;
			if(wechatCorpApp != null){
				url = String.format(WechatUtil.MENU_SEARCH_CORP_URL, wechatAccountService.getCorpAppAccessToken(wechatCorpAppID), wechatCorpApp.getAgentid());
			}else{
				url = String.format(WechatUtil.MENU_SEARCH_URL, wechatAccountService.getAccessToken(wechatAccountID));
			}
			JSONObject jsonObject= WechatUtil.httpsRequest(url, "GET", null);
			if(jsonObject!=null && jsonObject.has("menu")){
				message = "获取服务器菜单成功！";
				setButtonStatus(wechatAccount, wechatCorpApp);
				builderServerButton(wechatAccount, wechatCorpApp, jsonObject.getJSONObject("menu").getJSONArray("button"), null);
			}else{
				if (0 != jsonObject.getInt("errcode")) {
					message = "获取菜单信息数据失败！错误码为："+jsonObject.getInt("errcode")+"错误信息为："+jsonObject.getString("errmsg");
				}else{
					message = "获取菜单信息数据失败！获取自定义菜单URL地址不正确";
				}
			}
		}
		return redirect("search", StringUtils.substringBeforeLast(message, ","));
	}

	private void builderServerButton(WechatAccount wechatAccount, WechatCorpApp wechatCorpApp, JSONArray jsonArray, WechatButton parentButton) {
		for(int i = 0; i < jsonArray.size(); i++){
			JSONObject json = jsonArray.getJSONObject(i);
			WechatButton wechatButton = new WechatButton();
			String name = json.getString("name");
			wechatButton.setParentButton(parentButton);
			wechatButton.setWechatAccount(wechatAccount);
			wechatButton.setWechatCorpApp(wechatCorpApp);
			wechatButton.setName(name);
			wechatButton.setStatus(true);
			//setSameButtonName(name);
			if(json.has("type")){
				String type = json.getString("type");
				if(type.equals("view")){
					wechatButton.setEvent(getDictData(DictDataUtil.WECHAT_EVENT_VIEW));
					wechatButton.setUrl(json.getString("url"));
					wechatButton.setCode(findWechatButton(wechatButton, wechatButton.getParentButton(), null).size() + 1);
				}else if(type.equals("click")){
					wechatButton.setEvent(getDictData(DictDataUtil.WECHAT_EVENT_CLICK));
					wechatButton.setMenuKey(json.getString("key"));
					wechatButton.setCode(findWechatButton(wechatButton, wechatButton.getParentButton(), null).size() + 1);
				}else if(type.equals("scancode_waitmsg")){
					wechatButton.setEvent(getDictData(DictDataUtil.WECHAT_EVENT_SCANCODE_WAITMSG));
					wechatButton.setMenuKey(json.getString("key"));
					wechatButton.setCode(findWechatButton(wechatButton, wechatButton.getParentButton(), null).size() + 1);
				}else if(type.equals("scancode_push")){
					wechatButton.setEvent(getDictData(DictDataUtil.WECHAT_EVENT_SCANCODE_PUSH));
					wechatButton.setMenuKey(json.getString("key"));
					wechatButton.setCode(findWechatButton(wechatButton, wechatButton.getParentButton(), null).size() + 1);
				}else if(type.equals("pic_sysphoto")){
					wechatButton.setEvent(getDictData(DictDataUtil.WECHAT_EVENT_PIC_SYSPHOTO));
					wechatButton.setMenuKey(json.getString("key"));
					wechatButton.setCode(findWechatButton(wechatButton, wechatButton.getParentButton(), null).size() + 1);
				}else if(type.equals("pic_photo_or_album")){
					wechatButton.setEvent(getDictData(DictDataUtil.WECHAT_EVENT_PIC_PHOTO_OR_ALBUM));
					wechatButton.setMenuKey(json.getString("key"));
					wechatButton.setCode(findWechatButton(wechatButton, wechatButton.getParentButton(), null).size() + 1);
				}else if(type.equals("pic_weixin")){
					wechatButton.setEvent(getDictData(DictDataUtil.WECHAT_EVENT_PIC_WEIXIN));
					wechatButton.setMenuKey(json.getString("key"));
					wechatButton.setCode(findWechatButton(wechatButton, wechatButton.getParentButton(), null).size() + 1);
				}
				entityDao.saveOrUpdate(wechatButton);
			}else{
				wechatButton.setCode(findWechatButton(wechatButton, wechatButton.getParentButton(), null).size() + 1);
				entityDao.saveOrUpdate(wechatButton);
				builderServerButton(wechatAccount, wechatCorpApp, json.getJSONArray("sub_button"), wechatButton);
			}
		}
	}

	public void setSameButtonName(String name) {
		List<WechatButton> wechatLocalButtons = entityDao.get(WechatButton.class, new String[]{"name", "status"}, new Object[]{name, true});
		if(wechatLocalButtons != null && wechatLocalButtons.size() > 0){
			WechatButton wechatLocalButton = wechatLocalButtons.get(0);
			if(wechatLocalButton.getParentButton() == null){
				List<WechatButton> wechatLocalChildrenButtons = entityDao.get(WechatButton.class, new String[]{"parentButton", "status"}, new Object[]{wechatLocalButton, true});
				for(WechatButton wechatLocalChildrenButton : wechatLocalChildrenButtons){
					wechatLocalChildrenButton.setStatus(false);
				}
				entityDao.saveOrUpdate(wechatLocalChildrenButtons);
			}
			wechatLocalButton.setStatus(false);
			entityDao.saveOrUpdate(wechatLocalButton);
		}
	}
	
	private void setButtonStatus(WechatAccount wechatAccount, WechatCorpApp wechatCorpApp) {
		List<WechatButton> wechatLocalButtons = null;
		if(wechatAccount != null){
			wechatLocalButtons = entityDao.get(WechatButton.class, new String[]{"wechatAccount", "status"}, new Object[]{wechatAccount, true});
		}else if(wechatCorpApp != null){
			wechatLocalButtons = entityDao.get(WechatButton.class, new String[]{"wechatCorpApp", "status"}, new Object[]{wechatCorpApp, true});
		}
		if(wechatLocalButtons != null && wechatLocalButtons.size() > 0){
			for(WechatButton wechatLocalButton : wechatLocalButtons){
				wechatLocalButton.setStatus(false);
			}
			entityDao.saveOrUpdate(wechatLocalButtons);
		}
	}

	private List<Button> builderButton(WechatButton wechatButton) {
		List<Button> buttons = CollectUtils.newArrayList();
		if(wechatButton.getEvent() != null){
			String event = wechatButton.getEvent().getEnName();
			if("view".equals(event)){
				UrlButton urlButton = new UrlButton();
				urlButton.setName(wechatButton.getName());
				urlButton.setType(event);
				urlButton.setUrl(wechatButton.getUrl());
				buttons.add(urlButton);
			}else if("click".equals(event) || "scancode_waitmsg".equals(event) || "scancode_push".equals(event) || "pic_sysphoto".equals(event) || "pic_photo_or_album".equals(event) || "pic_weixin".equals(event)){
				ClickButton clickButton = new ClickButton();
				clickButton.setKey(wechatButton.getMenuKey());
				clickButton.setName(wechatButton.getName());
				clickButton.setType(event);
				buttons.add(clickButton);
			}
		}
		return buttons;
	}

	private List<WechatButton> findWechatButton(Long wechatAccountID, Long wechatCorpAppID, WechatButton parentButton) {
		OqlBuilder<WechatButton> builder = OqlBuilder.from(WechatButton.class);
		if(wechatAccountID != null){
			builder.where("wechatAccount.id = :wechatAccountID", wechatAccountID);
		}else{
			if(wechatCorpAppID != null){
				builder.where("wechatCorpApp.id = :wechatCorpAppID", wechatCorpAppID);
			}else{
				builder.where("id = -1");
			}
		}
		if(parentButton == null){
			builder.where("parentButton is null");
		}else{
			builder.where("parentButton.id = :parentButtonID", parentButton.getId());
		}
		builder.where("status = true");
		builder.orderBy("orders");
		List<WechatButton> wechatButtons = entityDao.search(builder);
		return wechatButtons;
	}

	public void setWechatAccountService(WechatAccountService wechatAccountService) {
		this.wechatAccountService = wechatAccountService;
	}
	
}
