package org.beangle.wechat.core.service;

import java.io.File;

import net.sf.json.JSONObject;

import org.beangle.wechat.core.model.WechatArticle;


/**
 * 微信素材
 * 
 * @作者 王政
 * @公司 上海彦致信息技术有限公司
 * @创建时间 2015年2月6日  上午11:48:51
 */
public interface WechatSourceService {
	
	/**
	 * 上传临时素材
	 * @param accessToken
	 * @param type
	 * @param filename
	 * @param filePath
	 * @return
	 */
	public JSONObject uploadTempSource(String accessToken, String type, String filename, String filePath);
	
	/**
	 * 上传临时图文素材
	 * @param accessToken
	 * @param type
	 * @param filename
	 * @param filePath
	 * @return
	 */
	public JSONObject uploadTempNews(String accessToken, WechatArticle wechatArticle);
	
	/**
	 * 下载多媒体文件（不包含视频）
	 * @param accessToken
	 * @param mediaId
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public File downLoadSource(String accessToken, String mediaId, String filePath, String fileName);
	
	/**
	 * 下载多媒体文件（不包含视频）
	 * @param accessToken
	 * @param mediaId
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public File downLoadSourceOfCorp(String accessToken, String mediaId, String filePath, String fileName);
	
	/**
	 * 上传永久素材
	 * @param accessToken
	 * @param type
	 * @param filename
	 * @param filePath
	 * @return
	 */
	public JSONObject uploadSource(String accessToken, String type, String filename, String filePath);
	
	/**
	 * 删除永久素材
	 * @param accessToken
	 * @param mediaId
	 * @return
	 */
	public boolean delSource(String accessToken, String mediaId);
	
	/**
	 * 上传永久图文素材
	 * @param accessToken
	 * @param type
	 * @param filename
	 * @param filePath
	 * @return
	 */
	public JSONObject uploadArticle(String accessToken, WechatArticle wechatArticle);
	
	/**
	 * 获取永久素材
	 * @param accessToken
	 * @param type
	 * @param filename
	 * @param filePath
	 * @return
	 */
	public JSONObject findSource(String accessToken, String mediaId);
	
	/**
	 * 发送预览
	 * @param openId
	 * @param accessToken
	 * @param wechatNumber
	 * @param mediaId
	 * @param msgType
	 * @return
	 */
	public JSONObject sendPreview(String openId, String accessToken, String wechatNumber, String mediaId, String msgType);
	
	/**
	 * 群发
	 * @param accessToken
	 * @param wechatNumber
	 * @param mediaId
	 * @param msgType
	 * @return
	 */
	public JSONObject sendAll(String accessToken, String mediaId, String msgType);
	
	/**
	 * 查询消息发送状态
	 * @param accessToken
	 * @param msgId
	 * @return
	 */
	public JSONObject searchSendStatus(String accessToken, String msgId);
	
	/**
	 * 查询阅读量
	 * @param accessToken
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public JSONObject searchReadNumber(String accessToken, String beginDate, String endDate);
	
	/**
	 * 获取文章阅读量和点赞数
	 * @param uin
	 * @param urls
	 * @return
	 */
	public JSONObject getReadNumberBy51Tool(String uin, String urls);
	
}
