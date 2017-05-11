package org.beangle.wechat.core.util;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sf.json.JSONObject;

/**
 * 公众平台通用接口工具类
 */
public class WechatUtil {
	
	/**
	 * 获取access_token的接口地址（GET） 限200（次/天）
	 */
	public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
	
	/**
	 * 获取企业access_token的接口地址
	 */
	public final static String ACCESS_TOKEN_CORP_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
	
    /**
     * 菜单创建（POST） 限100（次/天）
     */
    public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
    
    /**
     * 企业菜单创建（POST）
     */
    public final static String MENU_CREATE_CORP_URL = "https://qyapi.weixin.qq.com/cgi-bin/menu/create?access_token=%s&agentid=%s";
    
    /**
     * 企业菜单查询
     */
    public final static String MENU_SEARCH_CORP_URL = "https://qyapi.weixin.qq.com/cgi-bin/menu/get?access_token=%s&agentid=%s";
    
    /**
     * 菜单查询
     */
    public final static String MENU_SEARCH_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=%s";
   
    /**
     * 客服接口的地址
     */
    public final static String SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
    
    /**
     * 模板消息接口的地址
     */
    public final static String SEND_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    
    /**
     * 企业客服接口的地址
     */
    public final static String SEND_CORP_MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";
    
    /**
     * 网页授权的地址
     */
    public final static String WEB_AUTHORITY_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=STATE#wechat_redirect";
    
    /**
     * 获取网页access_token的接口地址
     */
    public final static String WEB_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    
    /**
     * 刷新网页access_token的接口地址
     */
    public final static String REFRESH_WEB_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
    
    /**
     * 获取检查网页access_token是否有效
     */
    public final static String CHECK_WEB_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s";
    
    /**
     * 获取用户信息地址
     */
    public final static String WEB_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=%s";
    
    /**
     * 获取用户信息地址（UNIONID机制）
     */
    public final static String WEB_USER_INFO_UNIONID_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=%s";
    
    /**
     * 获取企业用户信息地址
     */
    public final static String CORP_USER_INFO_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=%s&userid=%s";
    
    /**
     * 获取企业部门列表
     */
    public final static String CORP_DEPARTMENT_LIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=%s&id=%s";
    
    /**
     * 获取访问网页的企业用户信息
     */
    public final static String WEB_CORP_USER_INFO_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s";
    
    /**
	 * 弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息
	 */
	public final static String WEB_SNSAPI_USERINFO = "snsapi_userinfo";
	
	/**
	 * 不弹出授权页面，直接跳转，只能获取用户openid
	 */
	public final static String WEB_SNSAPI_BASE = "snsapi_base";
    
    /**
     * 获取用户信息参数标识
     */
    public final static String WEB_AUTHORITY_TOKEN = "WEB_AUTHORITY_TOKEN";
    
    /**
     * 获取企业应用的用户信息参数标识
     */
    public final static String WEB_CORP_APP_AUTHORITY_TOKEN = "WEB_CORP_APP_AUTHORITY_TOKEN";
    
    /**
     * 企业应用的用户标识
     */
    public final static String WEB_CORP_USER_TOKEN = "WEB_CORP_USER_TOKEN";
    
    /**
     * 企业应用的标识
     */
    public final static String WEB_CORP_APP_TOKEN = "WEB_CORP_APP_TOKEN";
    
    /**
     * 获取微信公众号信息参数标识
     */
    public final static String WEB_JS_API = "WEB_JS_API";
    
    /**
     * 获取企业应用的信息参数标识
     */
    public final static String WEB_CORP_JS_API = "WEB_CORP_JS_API";
    
    /**
     * 获取百度地图信息参数标识
     */
    public final static String BAIDU_MAP_POSITION = "BAIDU_MAP_POSITION";
    
    /**
     * 放入Cookie的openid标识
     */
    public final static String WEB_OPEN_ID = "openid";
    
    /**
     * 放入Cookie的userId标识
     */
    public final static String WEB_USER_ID = "userId";
    
    /**
     * 调用js-sdk的jsapi_ticket
     */
    public final static String JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
    
    /**
     * 企业应用调用js-sdk的jsapi_ticket
     */
    public final static String JSAPI_CORP_APP_TICKET = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=%s";
    
    /**
     * 本地主机的地址
     */
    public final static String LOCALHOST_ADDRESS = "localhost_address";
    
    /**
     * 百度地图
     */
    public final static String BAIDU_MAP = "http://api.map.baidu.com/geocoder/v2/?ak=ovaOqqOOTaPBWrmHY5GUpKSW&location=%s,%s&output=json&pois=1";
	
    /**
     * 上传永久素材
     */
    public final static String UPLOAD_SOURCE = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=%s&type=%s";
    
    /**
     * 删除永久素材
     */
    public final static String DEL_SOURCE = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=%s";
    
    /**
     * 上传永久图文素材
     */
    public final static String UPLOAD_ARTICLE = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=%s";
    
    /**
     * 获取永久图文素材
     */
    public final static String FIND_SOURCE = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=%s";
    
    /**
     * 上传临时素材
     */
    public final static String UPLOAD_TEMP_SOURCE = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s";
    
    /**
     * 上传临时图文素材
     */
    public final static String UPLOAD_TEMP_NEWS = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=%s";
    
    /**
     * 获取企业临时素材
     */
    public final static String GET_TEMP_NEWS_CORP = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
    
    /**
     * 获取临时素材
     */
    public final static String GET_TEMP_NEWS = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
    /**
     * 发送预览
     */
    public final static String SEND_PREVIEW = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=%s";
    
    /**
     * 群发
     */
    public final static String SEND_ALL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=%s";
    
    /**
     * 查询发送状态
     */
    public final static String SEARCH_SEND_STATUS = "https://api.weixin.qq.com/cgi-bin/message/mass/get?access_token=%s";
    
    /**
     * 获取图文统计数据
     */
    public final static String READ_NUMBER = "https://api.weixin.qq.com/datacube/getuserread?access_token=%s";
    
    /**
     * 获取图文统计每日群发数据
     */
    public final static String READ_NUMBER_DAY_MASS = "https://api.weixin.qq.com/datacube/getarticlesummary?access_token=%s";
    
    /**
     * 获取图文统计群发数据
     */
    public final static String READ_NUMBER_MASS = "https://api.weixin.qq.com/datacube/getarticletotal?access_token=%s";
    
    /**
     * 获取文章阅读数点赞数
     */
    public final static String READ_NUMBER_51TOOL = "http://51tools.info/wx/api.ashx?uin=MTk2MTk4ODUyMA==";
    
    /**
     * 获取微信企业号标签列表
     */
    public final static String WECHAT_CORP_TAG_LIST = "https://qyapi.weixin.qq.com/cgi-bin/tag/list?access_token=%s";
    
    /**
     * 获取微信企业号标签成员
     */
    public final static String WECHAT_CORP_TAG_USER = "https://qyapi.weixin.qq.com/cgi-bin/tag/get?access_token=%s&tagid=%s";
    
    /**
     * 发起https请求并获取结果
     * 
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        HttpsURLConnection httpsUrlConn = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        StringBuffer buffer = new StringBuffer();
        try {
                httpsUrlConn = builderHttpsUrlConnection(requestUrl, requestMethod);

                // 当有数据需要提交时
                if (null != outputStr) {
                        OutputStream outputStream = httpsUrlConn.getOutputStream();
                        // 注意编码格式，防止中文乱码
                        outputStream.write(outputStr.getBytes("UTF-8"));
                        outputStream.close();
                }

                // 将返回的输入流转换成字符串
                inputStream = httpsUrlConn.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                bufferedReader = new BufferedReader(inputStreamReader);

                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                        buffer.append(str);
                }
                bufferedReader.close();
                inputStreamReader.close();
                // 释放资源
                inputStream.close();
                inputStream = null;
                httpsUrlConn.disconnect();
                //jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ee) {
        	ee.printStackTrace();
        } catch (Exception ee) {
        	ee.printStackTrace();
        } finally {
        	if(bufferedReader != null) {
        		try {
					bufferedReader.close();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
        	}
        	if(inputStreamReader != null) {
        		try {
					inputStreamReader.close();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
        	}
            // 释放资源
        	if(inputStream != null) {
        		try {
					inputStream.close();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
        		inputStream = null;
        	}
        	if(httpsUrlConn != null) {
        		httpsUrlConn.disconnect();
        	}
        	if(buffer.length() > 0) {
        		jsonObject = JSONObject.fromObject(buffer.toString());
        	}
        }
        return jsonObject;
    }
    
    /**
     * 发起http请求并获取结果
     * 
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        HttpURLConnection httpUrlConn = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        StringBuffer buffer = new StringBuffer();
        try {
                httpUrlConn = builderHttpUrlConnection(requestUrl, requestMethod);

                // 当有数据需要提交时
                if (null != outputStr) {
                        OutputStream outputStream = httpUrlConn.getOutputStream();
                        // 注意编码格式，防止中文乱码
                        outputStream.write(outputStr.getBytes("UTF-8"));
                        outputStream.close();
                }

                // 将返回的输入流转换成字符串
                inputStream = httpUrlConn.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                bufferedReader = new BufferedReader(inputStreamReader);

                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                        buffer.append(str);
                }
                bufferedReader.close();
                inputStreamReader.close();
                // 释放资源
                inputStream.close();
                inputStream = null;
                httpUrlConn.disconnect();
                jsonObject = JSONObject.fromObject(buffer.toString());
                //jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ee) {
        	ee.printStackTrace();
        } catch (Exception ee) {
        	ee.printStackTrace();
        } finally {
        	if(bufferedReader != null) {
        		try {
					bufferedReader.close();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
        	}
        	if(inputStreamReader != null) {
        		try {
					inputStreamReader.close();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
        	}
            // 释放资源
        	if(inputStream != null) {
        		try {
					inputStream.close();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
        		inputStream = null;
        	}
        	if(httpUrlConn != null) {
        		httpUrlConn.disconnect();
        	}
            jsonObject = JSONObject.fromObject(buffer.toString());
        }
        return jsonObject;
    }
    
    /**
     * 下载
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @param filePath
     * @return
     */
    public static File downLoadFile(String requestUrl, String filePath, String fileName) {
        File file = null;
        
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");

            file = new File(filePath);
        	if(!file.exists() && !file.isDirectory()){
        		file.mkdirs();
        	}
        	
        	file = new File(filePath + fileName);
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = new byte[8096];
            int size = 0;
            while ((size = bis.read(buf)) != -1)
              fos.write(buf, 0, size);
            fos.close();
            bis.close();

            conn.disconnect();
            String info = String.format("下载媒体文件成功，filePath=" + filePath);
            System.out.println(info);
          } catch (Exception e) {
            filePath = null;
            String error = String.format("下载媒体文件失败：%s", e);
            System.out.println(error);
          }
          return file;

    }

	private static HttpsURLConnection builderHttpsUrlConnection(
			String requestUrl, String requestMethod)
			throws NoSuchAlgorithmException, NoSuchProviderException,
			KeyManagementException, MalformedURLException, IOException,
			ProtocolException {
		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, new java.security.SecureRandom());
		// 从上述SSLContext对象中得到SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();

		URL url = new URL(requestUrl);
		//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.10.198.59", 808));
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setSSLSocketFactory(ssf);
		
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 设置请求方式（GET/POST）
		httpUrlConn.setRequestMethod(requestMethod);
		if ("GET".equalsIgnoreCase(requestMethod))
		        httpUrlConn.connect();
		return httpUrlConn;
	}
	
	private static HttpURLConnection builderHttpUrlConnection(String requestUrl, String requestMethod) throws IOException {
		URL url = new URL(requestUrl);
		//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.10.198.59", 808));
		HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
		
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 设置请求方式（GET/POST）
		httpUrlConn.setRequestMethod(requestMethod);
		if ("GET".equalsIgnoreCase(requestMethod))
		        httpUrlConn.connect();
		return httpUrlConn;
	}
    
    /**
     * 证书信任管理器（用于https请求）
     * 
     * @作者 王政
     * @创建时间 2015年7月29日 下午3:59:20
     */
    public static class MyX509TrustManager implements X509TrustManager {

    	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    	}

    	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    	}

    	public X509Certificate[] getAcceptedIssuers() {
    		return null;
    	}
    }
    
}