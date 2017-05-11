package org.beangle.wechat.app.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.beangle.wechat.app.service.WechatAppResponseService;
import org.beangle.wechat.core.model.WechatAccount;
import org.beangle.wechat.core.model.WechatImage;
import org.beangle.wechat.core.model.WechatKey;
import org.beangle.wechat.core.model.WechatUser;
import org.beangle.wechat.core.service.Hdfw;
import org.beangle.wechat.core.service.impl.BaseResponseImpl;
import org.beangle.wechat.core.util.MapPositionUtil;
import org.beangle.wechat.core.util.MessageUtil;
import org.beangle.wechat.core.util.SessionUtil;
import org.beangle.wechat.core.util.WechatUtil;
import org.beangle.wechat.json.model.TextMessage;
import org.springframework.beans.BeansException;

public class WechatAppResponseServiceImpl extends BaseResponseImpl implements WechatAppResponseService {

	@Override
	public String responseMsg(WechatAccount wechatAccount, HttpServletRequest request) {
		String respMessage = null;
		try {
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			respMessage = builderResponse(wechatAccount, requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respMessage;
	}
	
	@Override
	protected String buildSubscribe(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName) {
		return updateWechatUser(wechatAccountOrWechatCorpApp, requestMap, fromUserName, toUserName);
	}
	
	@Override
	protected String updateWechatUser(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName) {
		WechatAccount wechatAccount = (WechatAccount) wechatAccountOrWechatCorpApp;
		WechatUser wechatUser = wechatUserService.saveOrUpdateWechatUser(wechatAccount, wechatAccountService.getAccessToken(toUserName), fromUserName, "zh_CN");
		if(wechatUser == null){
			TextMessage textMessage = getDefaultResponse(fromUserName, toUserName);
			textMessage.setContent("获取用户信息失败");
			return MessageUtil.textMessageToXml(textMessage);
		}
		return "success";
	}
	
	@Override
	protected String buildUnsubscribe(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName) {
		WechatAccount wechatAccount = (WechatAccount) wechatAccountOrWechatCorpApp;
		WechatUser wechatUser = wechatUserService.getWechatUser(wechatAccount, fromUserName);
		if(wechatUser != null){
			entityDao.remove(wechatUser);
		}
		return "success";
	}
	
	@Override
	protected String buildLocation(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName) {
		WechatAccount wechatAccount = (WechatAccount) wechatAccountOrWechatCorpApp;
		Double latitude = Double.parseDouble(requestMap.get("Latitude"));
		Double longitude = Double.parseDouble(requestMap.get("Longitude"));
		System.out.println(longitude + "," + latitude);
		String baiduMapInfo = MapPositionUtil.getBaiduMapInfo(latitude, longitude);
		SessionUtil.put(fromUserName + WechatUtil.BAIDU_MAP_POSITION, baiduMapInfo);
		SessionUtil.put(fromUserName + WechatUtil.BAIDU_MAP_POSITION + "_latitude", latitude);
		SessionUtil.put(fromUserName + WechatUtil.BAIDU_MAP_POSITION + "_longitude", longitude);
		WechatUser wechatUser = wechatUserService.getWechatUser(wechatAccount, fromUserName);
		if(wechatUser != null){
			wechatUser.setNowLatitude(latitude);
			wechatUser.setNowLongitude(longitude);
			wechatUser.setNowPlace(MapPositionUtil.getBaiduAddressInfo(latitude, longitude));
			entityDao.saveOrUpdate(wechatUser);
		}
		return "success";
	}

	@Override
	protected String buildImage(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName) {
		WechatAccount wechatAccount = (WechatAccount) wechatAccountOrWechatCorpApp;
		TextMessage textMessage = getDefaultResponse(fromUserName, toUserName);
		WechatUser wechatUser = wechatUserService.getWechatUser(wechatAccount, fromUserName);
		if(wechatUser != null){
			String mediaId = requestMap.get("MediaId");
			String filepath = builderImagePath();
			File file = wechatSourceService.downLoadSource(wechatAccountService.getAccessToken(toUserName), mediaId, filepath, mediaId + ".jpg");
			if(file != null){
				String picMd5Sum = buildFileMd5(file);
				WechatImage wechatImage = wechatImageService.getWechatImage(wechatUser, null, picMd5Sum);
				if(wechatImage != null){
					wechatImage.setWechatUser(wechatUser);
					wechatImage.setCreateTime(requestMap.get("CreateTime"));
					wechatImage.setMediaId(mediaId);
					wechatImage.setPicUrl(requestMap.get("PicUrl"));
					wechatImage.setMsgId(requestMap.get("MsgId"));
					wechatImage.setFilePath(filepath + mediaId + ".jpg");
					entityDao.saveOrUpdate(wechatImage);
					String className= wechatImage.getClazzName();
					if(StringUtils.isNotBlank(className)){
						try {
							requestMap.put("isImage", "isImage");
							requestMap.put("PicMd5Sum", picMd5Sum);
							Hdfw hdfw = (Hdfw) applicationContext.getBean(Class.forName(className));
							return hdfw.onSuccess(wechatAccount, requestMap);
						} catch (BeansException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}else{
						return "success";
					}
				}
			}else{
				textMessage.setContent("图片获取失败！请重新发送");
			}
		}else{
			textMessage.setContent("请关注本微信公众号");
		}
		return MessageUtil.textMessageToXml(textMessage);
	}
	
	@Override
	protected String buildLink(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName) {
		return MessageUtil.textMessageToXml(getDefaultResponse(fromUserName, toUserName, "您发送的是链接消息！"));
	}

	@Override
	protected String buildVoice(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName) {
		return MessageUtil.textMessageToXml(getDefaultResponse(fromUserName, toUserName, "您发送的是音频消息！"));
	}
	
	@Override
	protected String getMainMenu() {
		return "欢迎您关注";
	}

	@Override
	protected String executeHdfw(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, Hdfw hdfw) {
		WechatAccount wechatAccount = (WechatAccount) wechatAccountOrWechatCorpApp;
		return hdfw.onSuccess(wechatAccount, requestMap);
	}
	
	/**
	 * 遍历关键字管理中是否存在用户输入的关键字信息
	 */
	@Override
	protected WechatKey findWechatKeyResponse(Object wechatAccountOrWechatCorpApp, String content) {
		WechatAccount wechatAccount = (WechatAccount) wechatAccountOrWechatCorpApp;
		List<WechatKey> wechatKeys = entityDao.get(WechatKey.class, new String[]{"keyWord", "wechatAccount"}, new Object[]{content, wechatAccount});
		return wechatKeys != null && wechatKeys.size() == 1 ? wechatKeys.get(0) : null;
	}

}
