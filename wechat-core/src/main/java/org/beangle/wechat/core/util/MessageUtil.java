package org.beangle.wechat.core.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.beangle.wechat.json.model.MusicMessage;
import org.beangle.wechat.json.model.NewsMessage;
import org.beangle.wechat.json.model.NewsMessage.NewsMessageItem;
import org.beangle.wechat.json.model.TextMessage;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class MessageUtil {
	/**
     * 返回消息类型：文本
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    /**
     * 返回消息类型：音乐
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    /**
     * 返回消息类型：图文
     */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";
    
    /**
     * 返回消息类型：session
     */
    public static final String RESP_MESSAGE_TYPE_SESSON = "sessionKey";
    
    /**
     * 返回消息类型：控制器
     */
    public static final String RESP_MESSAGE_TYPE_ACTION = "action";

    /**
     * 请求消息类型：文本
     */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";
    
    /**
     * 请求消息类型：扩展
     */
    public static final String REQ_MESSAGE_TYPE_EXPAND = "expand";

    /**
     * 请求消息类型：图片
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    /**
     * 请求消息类型：链接
     */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /**
     * 请求消息类型：音频
     */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

    /**
     * 请求消息类型：推送
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型：subscribe(订阅)
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
    
    /**
     * 事件类型：点击事件
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";
    
    /**
     * 事件类型：链接事件
     */
    public static final String EVENT_TYPE_VIEW = "VIEW";
    
    /**
     * 事件类型：扫码推事件
     */
    public static final String EVENT_TYPE_SCANCODE_PUSH = "scancode_push";
    
    /**
     * 事件类型：扫码推事件且弹出“消息接收中”提示框
     */
    public static final String EVENT_TYPE_SCANCODE_WAITMSG = "scancode_waitmsg";
    
    /**
     * 事件类型：弹出系统拍照发图
     */
    public static final String EVENT_TYPE_PIC_SYSPHOTO = "pic_sysphoto";
    
    /**
     * 事件类型：弹出拍照或者相册发图
     */
    public static final String EVENT_TYPE_PIC_PHOTO_OR_ALBUM = "pic_photo_or_album";
    
    /**
     * 事件类型：弹出微信相册发图器
     */
    public static final String EVENT_TYPE_PIC_WEIXIN = "pic_weixin";
    
    /**
     * 事件类型：地理位置事件
     */
    public static final String EVENT_TYPE_LOCATION = "LOCATION";
    
    
    /**
     * 解析微信发来的请求（XML）
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        parseXml(map, elementList);

        // 释放资源
        inputStream.close();
        inputStream = null;

        return map;
    }
    
    /**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(String msg) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = new ByteArrayInputStream(msg.getBytes("UTF-8"));
		
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		parseXml(map, elementList);

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}


	@SuppressWarnings("unchecked")
	private static void parseXml(Map<String, String> map, List<Element> elementList) {
		// 遍历所有子节点
        for (Element e : elementList){
        	List<Element> childrenElementList = e.elements();
        	if(childrenElementList != null && childrenElementList.size() > 0){
        		parseXml(map, childrenElementList);
        	}else{
        		map.put(e.getName(), e.getText());
        	}
        }
	}

    /**
     * 文本消息对象转换成xml
     * 
     * @param textMessage 文本消息对象
     * @return xml
     */
    public static String textMessageToXml(TextMessage textMessage) {
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }

    /**
     * 音乐消息对象转换成xml
     * 
     * @param musicMessage 音乐消息对象
     * @return xml
     */
    public static String musicMessageToXml(MusicMessage musicMessage) {
        xstream.alias("xml", musicMessage.getClass());
        return xstream.toXML(musicMessage);
    }

    /**
     * 图文消息对象转换成xml
     * 
     * @param newsMessage 图文消息对象
     * @return xml
     */
    public static String newsMessageToXml(NewsMessage newsMessage) {
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new NewsMessageItem().getClass());
        return xstream.toXML(newsMessage);
    }
    
    /**
     * 扩展xstream，使其支持CDATA块
     * 
     * @date 2013-05-19
     */
    private static XStream xstream = new XStream(new XppDriver() {
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                @SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                            writer.write("<![CDATA[");
                            writer.write(text);
                            writer.write("]]>");
                    } else {
                            writer.write(text);
                    }
                }
            };
        }
    });
    
}
