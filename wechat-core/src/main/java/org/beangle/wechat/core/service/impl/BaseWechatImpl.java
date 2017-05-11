package org.beangle.wechat.core.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.beangle.commons.collection.CollectUtils;
import org.beangle.commons.property.PropertyConfig;
import org.beangle.commons.property.PropertyConfigFactory;
import org.beangle.model.persist.impl.BaseServiceImpl;
import org.beangle.website.system.model.DictData;
import org.beangle.website.system.service.DictDataService;
import org.beangle.wechat.app.service.WechatAppResponseService;
import org.beangle.wechat.core.service.WechatAccountService;
import org.beangle.wechat.core.service.WechatImageService;
import org.beangle.wechat.core.service.WechatJoinService;
import org.beangle.wechat.core.service.WechatMessageService;
import org.beangle.wechat.core.service.WechatService;
import org.beangle.wechat.core.service.WechatSourceService;
import org.beangle.wechat.core.service.WechatUserService;
import org.beangle.wechat.core.util.HttpConnectionManager;
import org.beangle.wechat.core.util.MessageUtil;
import org.beangle.wechat.core.util.WechatUtil;
import org.beangle.wechat.corp.service.WechatCorpResponseService;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class BaseWechatImpl extends BaseServiceImpl implements ApplicationContextAware {

	protected DictDataService dictDataService;

	protected WechatAccountService wechatAccountService;

	protected WechatService wechatService;

	protected WechatJoinService wechatJoinService;

	protected WechatAppResponseService wechatAppResponseService;

	protected WechatCorpResponseService wechatCorpResponseService;

	protected WechatUserService wechatUserService;

	protected WechatSourceService wechatSourceService;

	protected WechatImageService wechatImageService;

	protected WechatMessageService wechatMessageService;

	protected ApplicationContext applicationContext;

	// 以下为获取系统参数
	protected PropertyConfigFactory configFactory;

	public PropertyConfig getSystemConfig() {
		return configFactory.getConfig();
	}

	/**
	 * 获取系统参数
	 * 
	 * @param clazz
	 *            参数类型
	 * @param name
	 *            参数名称
	 * @return 参数
	 */
	protected <T> T getSystemParameters(Class<T> clazz, String name) {
		return getSystemConfig().get(clazz, name);
	}

	public String getBasePath() {
		return getSystemParameters(String.class, WechatUtil.LOCALHOST_ADDRESS);
	}

	/**
	 * 根据字典数据编码获得一个数据字典
	 * 
	 * @param code
	 * @return
	 */
	protected DictData getDictData(String code) {
		return dictDataService.getDictData(code);
	}

	public Object[] invokeWs(String url, String method, Object... params) {
		try {
			JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
			Client client = factory.createClient(url);
			return client.invoke(method, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Element getRoot(String xml) {
		Element root = null;
		try {
			InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			// 读取输入流
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			// 得到xml根元素
			root = document.getRootElement();
			// 释放资源
			inputStream.close();
			inputStream = null;
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return root;
	}

	@SuppressWarnings("unchecked")
	public String sendSoapBackStr(String url, String soapStr) {
		// 调用请求STA
		CloseableHttpClient httpClient = HttpConnectionManager.getHttpClient();
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		String result = null;
		try {

			StringEntity entiy = new StringEntity(soapStr, "UTF-8");
			httpPost.setEntity(entiy);
			httpPost.setHeader("Content-Type", "text/xml; charset=UTF-8");
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == statusCode) {
				soapStr = EntityUtils.toString(response.getEntity());
				// 调用请求END
				SOAPMessage msg = formatSoapString(soapStr);
				SOAPBody body = msg.getSOAPBody();
				result = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><data>" + StringUtils.substringAfter(buildUBody(body.getChildElements(), null), "?>") + "</data>";
			}else {
				httpPost.abort();
			}
		} catch (Exception e) {
			httpPost.abort();
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			httpPost.releaseConnection();
		}
		return result;
	}

	public Map<String, String> sendSoap(String url, String soapStr) {
		Map<String, String> map = CollectUtils.newHashMap();
		// 调用请求STA
		try {
			try {
				String xml = sendSoapBackStr(url, soapStr);
				if (StringUtils.indexOf(xml, "<br>") != -1) {
					xml = StringUtils.replace(xml, "<br>", "\n");
				}
				map = MessageUtil.parseXml(xml);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 把soap字符串格式化为SOAPMessage
	 * 
	 * @param soapString
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private static SOAPMessage formatSoapString(String soapString) {
		MessageFactory msgFactory;
		try {
			msgFactory = MessageFactory.newInstance();
			SOAPMessage reqMsg = msgFactory.createMessage(new MimeHeaders(), new ByteArrayInputStream(soapString.getBytes("UTF-8")));
			reqMsg.saveChanges();
			return reqMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static String buildUBody(Iterator<SOAPElement> iterator, String side) {
		StringBuilder sb = new StringBuilder();
		while (iterator.hasNext()) {
			SOAPElement element = (SOAPElement) iterator.next();
			if (null == element.getValue() && element.getChildElements().hasNext()) {
				return buildUBody(element.getChildElements(), side + "-----");
			} else {
				sb.append(element.getValue());
			}
		}
		return sb.toString();
	}

	public void setWechatUserService(WechatUserService wechatUserService) {
		this.wechatUserService = wechatUserService;
	}

	public void setWechatAccountService(WechatAccountService wechatAccountService) {
		this.wechatAccountService = wechatAccountService;
	}

	public void setWechatSourceService(WechatSourceService wechatSourceService) {
		this.wechatSourceService = wechatSourceService;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setWechatService(WechatService wechatService) {
		this.wechatService = wechatService;
	}

	public void setWechatImageService(WechatImageService wechatImageService) {
		this.wechatImageService = wechatImageService;
	}

	public void setWechatJoinService(WechatJoinService wechatJoinService) {
		this.wechatJoinService = wechatJoinService;
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

	public void setDictDataService(DictDataService dictDataService) {
		this.dictDataService = dictDataService;
	}

	public void setConfigFactory(PropertyConfigFactory configFactory) {
		this.configFactory = configFactory;
	}

}
