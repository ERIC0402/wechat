package org.beangle.wechat.core.service.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang3.StringUtils;
import org.beangle.wechat.core.model.WechatArticle;
import org.beangle.wechat.core.service.WechatSourceService;
import org.beangle.wechat.core.util.WechatUtil;
import org.beangle.wechat.json.model.Article;
import org.beangle.wechat.json.model.Article.ArticleItem;

import net.sf.json.JSONObject;

public class WechatSourceServiceImpl extends BaseWechatImpl implements WechatSourceService {

	@Override
	public JSONObject uploadSource(String accessToken, String type, String filename, String filePath) {
		return sendSource(filename, filePath, String.format(WechatUtil.UPLOAD_SOURCE, accessToken, type.toLowerCase()));
	}

	private JSONObject sendSource(String filename, String filePath, String requestUrl) {
		try {	
			URL url = new URL(requestUrl);
			String result = null;
			File file = new File(filePath);
			if (!file.exists() || !file.isFile()) {
				System.out.println("上传的文件不存在");
				return null;
			}
			//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.10.198.59", 808));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false); // post方式不能使用缓存
			// 设置请求头信息
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			// 设置边界
			String BOUNDARY = "----------" + System.currentTimeMillis();
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			// 请求正文信息
			// 第一部分：
			StringBuilder sb = new StringBuilder();
			sb.append("--"); // 必须多两道线
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + filename + "\" \r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			byte[] head = sb.toString().getBytes("utf-8");
			// 获得输出流
			OutputStream out = new DataOutputStream(con.getOutputStream());
			// 输出表头
			out.write(head);
			// 文件正文部分
			// 把文件已流文件的方式 推入到url中
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			// 结尾部分
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			out.write(foot);
			out.flush();
			out.close();
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = null;
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
				JSONObject jsonObject = JSONObject.fromObject(result);
				if (jsonObject.has("media_id")) {
					return jsonObject;
				}
			}
		} catch (IOException e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		} finally {

		}
		return null;
	}

	@Override
	public boolean delSource(String accessToken, String mediaId) {
		// 拼装请求地址
		String requestUrl = String.format(WechatUtil.DEL_SOURCE, accessToken);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("media_id", mediaId);
		JSONObject json = WechatUtil.httpsRequest(requestUrl, "POST", jsonObject.toString());
		if(json != null && json.containsValue("ok")){
			return true;
		}
		return false;
	}

	@Override
	public JSONObject uploadArticle(String accessToken, WechatArticle wechatArticle) {
		return uploadArticle(wechatArticle, String.format(WechatUtil.UPLOAD_ARTICLE, accessToken));
	}

	private JSONObject uploadArticle(WechatArticle wechatArticle, String requestUrl) {
		Article article =  new Article();
		ArticleItem articleItem = new ArticleItem();
		articleItem.setTitle(wechatArticle.getWechatArticleItem().getTitle());
		articleItem.setThumb_media_id(wechatArticle.getWechatArticleItem().getWechatSource().getMediaId());
		articleItem.setAuthor(wechatArticle.getWechatArticleItem().getAuthor());
		articleItem.setDigest(wechatArticle.getDigest());
		articleItem.setShow_cover_pic(wechatArticle.getWechatArticleItem().getShowCovePic());
		articleItem.setContent(wechatArticle.getWechatArticleItem().getContent());
		articleItem.setContent_source_url(wechatArticle.getWechatArticleItem().getSourceUrl());
		article.getArticles().add(articleItem);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "POST", JSONObject.fromObject(article).toString());
		if(jsonObject != null && jsonObject.has("media_id")){
			return jsonObject;
		}
		return null;
	}
	
	@Override
	public JSONObject findSource(String accessToken, String mediaId) {
		// 拼装请求地址
		String requestUrl = String.format(WechatUtil.FIND_SOURCE, accessToken);
		JSONObject json = new JSONObject();
		json.put("media_id", mediaId);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "POST", json.toString());
		if(jsonObject != null && !jsonObject.has("errmsg")){
			return jsonObject;
		}
		return null;
	}

	@Override
	public JSONObject uploadTempSource(String accessToken, String type, String filename, String filePath) {
		return sendSource(filename, filePath, String.format(WechatUtil.UPLOAD_TEMP_SOURCE, accessToken, type.toLowerCase()));
	}

	@Override
	public JSONObject uploadTempNews(String accessToken, WechatArticle wechatArticle) {
		return uploadArticle(wechatArticle, String.format(WechatUtil.UPLOAD_TEMP_NEWS, accessToken));
	}
	
	public File downLoadSource(String accessToken, String mediaId, String filePath, String fileName) {
		// 拼装请求地址
		String requestUrl = String.format(WechatUtil.GET_TEMP_NEWS, accessToken, mediaId);
		File file = WechatUtil.downLoadFile(requestUrl, filePath, fileName);
		if(file != null){
			return file;
		}
		return null;
	}
	
	public File downLoadSourceOfCorp(String accessToken, String mediaId, String filePath, String fileName) {
		// 拼装请求地址
		String requestUrl = String.format(WechatUtil.GET_TEMP_NEWS_CORP, accessToken, mediaId);
		File file = WechatUtil.downLoadFile(requestUrl, filePath, fileName);
		if(file != null){
			return file;
		}
		return null;
	}
	
	@Override
	public JSONObject sendPreview(String openId, String accessToken, String wechatNumber, String mediaId, String msgType) {
		String requestUrl = String.format(WechatUtil.SEND_PREVIEW, accessToken);
		JSONObject json = new JSONObject();
		if(StringUtils.isBlank(openId)) {
			json.put("towxname", "WangZheng0402");
		}else {
			json.put("touser", openId);
		}
		if("mpnews".equals(msgType)){
			JSONObject mpnewsJson = new JSONObject();
			mpnewsJson.put("media_id", mediaId);
			json.put("mpnews", mpnewsJson);
		}
		json.put("msgtype", msgType);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "POST", json.toString());
		if(jsonObject != null && jsonObject.containsValue("preview success")){
			return jsonObject;
		}
		System.out.println(jsonObject);
		return null;
	}
	
	@Override
	public JSONObject sendAll(String accessToken, String mediaId, String msgType) {
		String requestUrl = String.format(WechatUtil.SEND_ALL, accessToken);
		JSONObject json = new JSONObject();
		JSONObject filterJson =  new JSONObject();
		filterJson.put("is_to_all", true);
		json.put("filter", filterJson);
		if("mpnews".equals(msgType)){
			JSONObject mpnewsJson = new JSONObject();
			mpnewsJson.put("media_id", mediaId);
			json.put("mpnews", mpnewsJson);
		}
		json.put("msgtype", msgType);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "POST", json.toString());
		if(jsonObject != null && jsonObject.containsValue("send job submission success")){
			return jsonObject;
		}
		return null;
	}
	
	@Override
	public JSONObject searchSendStatus(String accessToken, String msgId) {
		// 拼装请求地址
		String requestUrl = String.format(WechatUtil.SEARCH_SEND_STATUS, accessToken);
		JSONObject json = new JSONObject();
		json.put("msg_id", msgId);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "POST", json.toString());
		if(jsonObject != null && jsonObject.containsValue("SEND_SUCCESS")){
			return jsonObject;
		}
		return null;
	}

	@Override
	public JSONObject searchReadNumber(String accessToken, String beginDate, String endDate) {
		String requestUrl = String.format("https://api.weixin.qq.com/datacube/getuserreadhour?access_token=%s", accessToken);
		JSONObject json = new JSONObject();
		json.put("begin_date", beginDate);
		json.put("end_date", beginDate);
		JSONObject jsonObject = WechatUtil.httpsRequest(requestUrl, "POST", json.toString());
		if(jsonObject != null && jsonObject.containsKey("list")){
			return jsonObject;
		}
		return null;
	}

	@Override
	public JSONObject getReadNumberBy51Tool(String uin, String urls) {
		String requestUrl = String.format("http://51tools.info/wx/api.ashx?uin=MjI4NzQ2MTIxNA==");
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(requestUrl);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print("urls=" + urls);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += "/n" + line;
			}
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", result);
		return jsonObject;
	}

}
