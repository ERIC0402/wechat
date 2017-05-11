package org.beangle.wechat.core.action;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.beangle.commons.collection.CollectUtils;
import org.beangle.model.Entity;
import org.beangle.website.common.action.FileAction;
import org.beangle.website.common.util.ReturnValue;
import org.beangle.wechat.app.service.WechatAppResponseService;
import org.beangle.wechat.core.model.WechatAccount;
import org.beangle.wechat.core.model.WechatCorpApp;
import org.beangle.wechat.core.model.WechatCorpUser;
import org.beangle.wechat.core.model.WechatUser;
import org.beangle.wechat.core.service.WechatAccountService;
import org.beangle.wechat.core.service.WechatImageService;
import org.beangle.wechat.core.service.WechatJoinService;
import org.beangle.wechat.core.service.WechatMessageService;
import org.beangle.wechat.core.service.WechatService;
import org.beangle.wechat.core.service.WechatSourceService;
import org.beangle.wechat.core.service.WechatUserService;
import org.beangle.wechat.core.util.JsSDKSign;
import org.beangle.wechat.core.util.WechatUtil;
import org.beangle.wechat.corp.service.WechatCorpResponseService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 微信父类
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月7日 上午10:51:51
 */
public class BaseWechatAction extends FileAction {
	
	public static final String MESSAGE_JSON_KEY = "message";
	
	public static final String IS_SUCCESS_JSON_KEY = "isSuccess";

	public static final String JOBNUMBER = "工号";

	protected WechatAccountService wechatAccountService;

	protected WechatService wechatService;

	protected WechatJoinService wechatJoinService;

	protected WechatAppResponseService wechatAppResponseService;

	protected WechatCorpResponseService wechatCorpResponseService;

	protected WechatUserService wechatUserService;

	protected WechatSourceService wechatSourceService;

	protected WechatImageService wechatImageService;

	protected WechatMessageService wechatMessageService;

	public String getBasePath() {
		HttpServletRequest request = getRequest();
		String serverName = request.getHeader("x-forwarded-host");
		if (StringUtils.isBlank(serverName)) {
			serverName = request.getHeader("host");
		}
		return request.getScheme() + "://" + serverName + request.getContextPath();
	}

	public String getRequestUrl() {
		HttpServletRequest request = getRequest();
		String requestUrl = getBasePath() + request.getRequestURI().substring(request.getContextPath().length());
		if (StringUtils.isNotBlank(request.getQueryString())) {
			requestUrl += "?" + request.getQueryString();
		}
		if(getBool("isReferer")) {
			requestUrl = request.getHeader("referer");
		}
		return requestUrl;
	}
	
	public String getRequestUri() {
		HttpServletRequest request = getRequest();
		String requestUri = request.getRequestURI();
		if (StringUtils.isNotBlank(request.getQueryString())) {
			requestUri += "?" + request.getQueryString();
		}
		return requestUri;
	}
	
	public String getWechatRequestParameters(WechatUser wechatUser) {
		return WechatUtil.WEB_AUTHORITY_TOKEN + "=" + wechatUser.getId() + "&" + WechatUtil.WEB_JS_API + "=" + wechatUser.getId();
	}
	
	public String getWechatRequestParameters(WechatCorpUser wechatCorpUser) {
		return WechatUtil.WEB_CORP_APP_AUTHORITY_TOKEN + "=" + wechatCorpUser.getId() + "&" + WechatUtil.WEB_CORP_JS_API + "=" + wechatCorpUser.getId();
	}
	
	public void wechatSave() throws IOException {
		JSONObject jsonObject = new JSONObject();
		boolean isSuccess = false;
		String message = null;
		try{
			wechatSaveBuild(populateEntity(), jsonObject);
			if(jsonObject.containsKey(IS_SUCCESS_JSON_KEY)) {
				isSuccess = jsonObject.getBoolean(IS_SUCCESS_JSON_KEY);
			}else {
				if(!jsonObject.containsKey(MESSAGE_JSON_KEY) || StringUtils.isBlank(jsonObject.getString(MESSAGE_JSON_KEY))) {
					isSuccess = true;
				}
			}
			message = jsonObject.getString(MESSAGE_JSON_KEY);
		}catch(Exception ee) {
			isSuccess = false;
			message = ee.toString();
		}finally {
			jsonObject.put(MESSAGE_JSON_KEY, message);
			jsonObject.put(IS_SUCCESS_JSON_KEY, isSuccess);
			writeResponseMessage(jsonObject.toString());
		}
	}
	
	public JSONObject wechatSaveBuild(Entity<?> entity, JSONObject jsonObject) throws Exception {
		saveOrUpdate(entity);
		return jsonObject;
	}
	
	public void wechatRemove() throws IOException {
		boolean isSuccess = false;
		Long entityId = getEntityId(getShortName());
		Collection<?> entities = null;
		if (null == entityId) {
			entities = getModels(getEntityName(), getEntityIds(getShortName()));
		} else {
			Entity<?> entity = getModel(getEntityName(), entityId);
			entities = Collections.singletonList(entity);
		}
		JSONObject resultJson = new JSONObject();
		wechatRemoveBuild(entities, resultJson);
		if(resultJson != null && (resultJson.containsKey(IS_SUCCESS_JSON_KEY) || !resultJson.containsKey(MESSAGE_JSON_KEY) || StringUtils.isBlank(resultJson.getString(MESSAGE_JSON_KEY)))) {
			isSuccess = true;
		}
		resultJson.put(IS_SUCCESS_JSON_KEY, isSuccess);
		writeResponseMessage(resultJson.toString());
	}
	
	public JSONObject wechatRemoveBuild(Collection<?> entities, JSONObject jsonObject) {
		try{
			remove(entities);
		}catch(Exception ee) {
			jsonObject.put(MESSAGE_JSON_KEY, ee.toString());
		}
		return jsonObject;
	}
	
	protected void putJsApiConfigs() {
		Long wechatAccountID = getLong(WechatUtil.WEB_JS_API);
		if (wechatAccountID != null) {
			WechatAccount wechatAccount = wechatAccountService.getActiveAccountByWechatAccountID(wechatAccountID);
			if (wechatAccount != null) {
				String jsapiTicket = wechatAccountService.getJsapiTicket(wechatAccountID, wechatAccountService.getAccessToken(wechatAccountID));
				put("config", JsSDKSign.sign(wechatAccount.getAppid(), jsapiTicket, getRequestUrl()));
			}
		} else {
			Long wechatCorpAppID = getLong(WechatUtil.WEB_CORP_JS_API);
			if (wechatCorpAppID != null) {
				WechatCorpApp wechatCorpApp = wechatAccountService.getActiveCorpApp(wechatCorpAppID);
				if (wechatCorpApp != null) {
					String jsapiTicket = wechatAccountService.getCorpAppJsapiTicket(wechatCorpAppID, wechatAccountService.getCorpAppAccessToken(wechatCorpAppID));
					put("config", JsSDKSign.corpAppSign(wechatCorpApp.getWechatAccount().getAppid(), jsapiTicket, getRequestUrl()));
				}
			} else {
				String appid = get("appid");
				String appsecret = get("appsecret");
				putJsApiConfigs(appid, appsecret);
			}
		}
	}
	
	/**
	 * 上传文件返回值
	 * 
	 * @throws IOException
	 */
	public void uploadFiles() throws Exception {
		writeResponseMessage(JSONArray.fromObject(saveFiles()).toString());
	}
	
	protected List<ReturnValue> saveFiles() throws Exception {
		List<ReturnValue> values = CollectUtils.newArrayList();
		String pagePara = "fileData";
		File[] files = getAll(pagePara, File.class);
		String[] fileNames = getAll(pagePara + "FileName", String.class);
		if (files == null || fileNames == null) {
			return values;
		}

		// 保存文件
		String folder = get("folder");
		if (StringUtils.isEmpty(folder)) {
			folder = "files/";
		}
		if (folder.startsWith("/")) {
			folder = folder.substring(1);
		}
		if (!folder.endsWith("/")) {
			folder = folder + "/";
		}
		Boolean isSaveLocal = getBoolean("saveLocal");
		String savePath = ServletActionContext.getServletContext().getRealPath("/");
		if(isSaveLocal != null && isSaveLocal) {
			savePath = getBaseDir();
		}
		for(int i = 0; i < fileNames.length; i++) {
			String fileName = fileNames[i];
			File file = files[i];
			ReturnValue value = new ReturnValue();
			// StringBuilder sb = new StringBuilder("device/" + folder);
			StringBuilder sb = new StringBuilder(TEMP_DIR + folder);
			sb.append(UUID.randomUUID().toString());
			sb.append(fileName.substring(fileName.lastIndexOf(".")).toLowerCase());

			File newFile = new File(savePath + sb.toString());
			if (!newFile.getParentFile().exists()) {
				newFile.getParentFile().mkdirs();
			}

			//renameTo在Linux系统下不能移动文件
//			file.renameTo(newFile);
			FileUtils.moveFile(file, newFile);
			value.setFileName(fileName);
			value.setFilePath(sb.toString());
			value.setFileSize(newFile.length());
			values.add(value);
		}
		return values;
	}

	private void putJsApiConfigs(String appid, String secret) {
		if(StringUtils.isNotBlank(appid) && StringUtils.isNotBlank(secret)) {
			String jsapiTicket = wechatAccountService.getJsapiTicketStr(wechatAccountService.getAccessTokenStr(appid, secret));
			put("config", JsSDKSign.sign(appid, jsapiTicket, getRequestUrl()));
		}
	}

	public void setWechatJoinService(WechatJoinService wechatJoinService) {
		this.wechatJoinService = wechatJoinService;
	}

	public void setWechatSourceService(WechatSourceService wechatSourceService) {
		this.wechatSourceService = wechatSourceService;
	}

	public void setWechatUserService(WechatUserService wechatUserService) {
		this.wechatUserService = wechatUserService;
	}

	public void setWechatAccountService(WechatAccountService wechatAccountService) {
		this.wechatAccountService = wechatAccountService;
	}

	public void setWechatService(WechatService wechatService) {
		this.wechatService = wechatService;
	}

	public void setWechatImageService(WechatImageService wechatImageService) {
		this.wechatImageService = wechatImageService;
	}

	public void setWechatAppResponseService(WechatAppResponseService wechatAppResponseService) {
		this.wechatAppResponseService = wechatAppResponseService;
	}

	public void setWechatCorpResponseService(WechatCorpResponseService wechatCorpResponseService) {
		this.wechatCorpResponseService = wechatCorpResponseService;
	}

	public void setWechatMessageService(WechatMessageService wechatMessageService) {
		this.wechatMessageService = wechatMessageService;
	}

}
