package org.beangle.wechat.corp.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.beangle.wechat.core.model.WechatCorpApp;
import org.beangle.wechat.core.model.WechatCorpUser;
import org.beangle.wechat.core.model.WechatImage;
import org.beangle.wechat.core.model.WechatKey;
import org.beangle.wechat.core.service.Hdfw;
import org.beangle.wechat.core.service.impl.BaseResponseImpl;
import org.beangle.wechat.core.util.MapPositionUtil;
import org.beangle.wechat.core.util.MessageUtil;
import org.beangle.wechat.core.util.SessionUtil;
import org.beangle.wechat.core.util.WechatUtil;
import org.beangle.wechat.core.util.aes.WXBizMsgCrypt;
import org.beangle.wechat.corp.service.WechatCorpResponseService;
import org.beangle.wechat.json.model.TextMessage;
import org.springframework.beans.BeansException;

public class WechatCorpResponseServiceImpl extends BaseResponseImpl implements WechatCorpResponseService {

	@Override
	public String responseMsg(WechatCorpApp wechatCorpApp, HttpServletRequest request, String signature, String timestamp, String nonce) {
		try {
			WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(wechatCorpApp.getToken(), wechatCorpApp.getEncodingAESKey(), wechatCorpApp.getWechatAccount().getAppid());
			String postParams = wxcpt.DecryptMsg(signature, timestamp, nonce, IOUtils.toString(request.getInputStream()));
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(postParams);
			return wxcpt.EncryptMsg(builderResponse(wechatCorpApp, requestMap), timestamp, nonce);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected String buildSubscribe(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName) {
		return updateWechatUser(wechatAccountOrWechatCorpApp, requestMap, fromUserName, toUserName);
	}
	
	@Override
	protected String updateWechatUser(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName) {
		WechatCorpApp wechatCorpApp = (WechatCorpApp) wechatAccountOrWechatCorpApp;
		WechatCorpUser wechatCorpUser = wechatUserService.saveOrUpdateWechatCorpUser(wechatCorpApp.getWechatAccount(), wechatAccountService.getCorpAppAccessToken(wechatCorpApp.getId()), fromUserName);
		if(wechatCorpUser == null){
			TextMessage textMessage = getDefaultResponse(fromUserName, toUserName);
			textMessage.setContent("获取用户信息失败");
			return MessageUtil.textMessageToXml(textMessage);
		}
		return "success";
	}
	
	@Override
	protected String buildUnsubscribe(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName) {
		/*WechatCorpApp wechatCorpApp = (WechatCorpApp) wechatAccountOrWechatCorpApp;
		WechatCorpUser wechatUser = wechatUserService.getWechatCorpUser(wechatCorpApp, fromUserName);
		if(wechatUser != null){
			entityDao.remove(wechatUser);
		}*/
		return "success";
	}
	
	@Override
	protected String buildLocation(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName) {
		WechatCorpApp wechatCorpApp = (WechatCorpApp) wechatAccountOrWechatCorpApp;
		Double latitude = Double.parseDouble(requestMap.get("Latitude"));
		Double longitude = Double.parseDouble(requestMap.get("Longitude"));
		String baiduMapInfo = MapPositionUtil.getBaiduMapInfo(latitude, longitude);
		SessionUtil.put(fromUserName + WechatUtil.BAIDU_MAP_POSITION, baiduMapInfo);
		SessionUtil.put(fromUserName + WechatUtil.BAIDU_MAP_POSITION + "_latitude", latitude);
		SessionUtil.put(fromUserName + WechatUtil.BAIDU_MAP_POSITION + "_longitude", longitude);
		WechatCorpUser wechatUser = wechatUserService.getWechatCorpUser(wechatCorpApp.getWechatAccount(), fromUserName);
		if(wechatUser != null){
			wechatUser.setNowLatitude(latitude);
			wechatUser.setNowLongitude(longitude);
			System.out.println("保存前：" + latitude + "," + longitude);
			wechatUser.setNowPlace(MapPositionUtil.getBaiduAddressInfo(latitude, longitude));
			wechatUser.setPositionTime(new Date());
			entityDao.saveOrUpdate(wechatUser);
			System.out.println("保存后：" + wechatUser.getNowLatitude() + "," + wechatUser.getNowLongitude());
		}
		return "success";
	}

	@Override
	protected String buildImage(Object wechatAccountOrWechatCorpApp, Map<String, String> requestMap, String fromUserName, String toUserName) {
		WechatCorpApp wechatCorpApp = (WechatCorpApp) wechatAccountOrWechatCorpApp;
		TextMessage textMessage = getDefaultResponse(fromUserName, toUserName);
		WechatCorpUser wechatCorpUser = wechatUserService.getWechatCorpUser(wechatCorpApp.getWechatAccount(), fromUserName);
		if(wechatCorpUser != null){
			String mediaId = requestMap.get("MediaId");
			String filepath = builderImagePath();
			File file = wechatSourceService.downLoadSourceOfCorp(wechatAccountService.getCorpAppAccessToken(wechatCorpApp.getId()), mediaId, filepath, mediaId + ".jpg");
			if(file != null){
				String picMd5Sum = buildFileMd5(file);
				WechatImage wechatImage = wechatImageService.getWechatImage(null, wechatCorpUser, picMd5Sum);
				if(wechatImage != null){
					wechatImage.setWechatCorpUser(wechatCorpUser);
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
							return hdfw.onSuccessOfCorp(wechatCorpApp, requestMap);
						} catch (BeansException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}else{
						return "success";
					}
				}else{
					return "success";
				}
			}else{
				textMessage.setContent("图片获取失败！请重新发送");
			}
		}else{
			textMessage.setContent("获取用户信息失败");
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
		WechatCorpApp wechatCorpApp = (WechatCorpApp) wechatAccountOrWechatCorpApp;
		return hdfw.onSuccessOfCorp(wechatCorpApp, requestMap);
	}

	/**
	 * 遍历关键字管理中是否存在用户输入的关键字信息
	 */
	@Override
	protected WechatKey findWechatKeyResponse(Object wechatAccountOrWechatCorpApp, String content) {
		WechatCorpApp wechatCorpApp = (WechatCorpApp) wechatAccountOrWechatCorpApp;
		List<WechatKey> wechatKeys = entityDao.get(WechatKey.class, new String[]{"keyWord", "wechatCorpApp"}, new Object[]{content, wechatCorpApp});
		return wechatKeys != null && wechatKeys.size() == 1 ? wechatKeys.get(0) : null;
	}

}
