package org.beangle.wechat.core.service;

import org.beangle.wechat.core.model.WechatCorpUser;
import org.beangle.wechat.core.model.WechatImage;
import org.beangle.wechat.core.model.WechatUser;

/**
 * 图片
 * 
 * @作者 王政
 * @创建时间 2015年8月4日 下午2:33:28
 */
public interface WechatImageService {

	/**
	 * 获取用户上传图片
	 * 
	 * @param wechatUser
	 * @param wechatCorpUser
	 * @param picMd5Sum
	 * @return
	 */
	public WechatImage getWechatImage(WechatUser wechatUser, WechatCorpUser wechatCorpUser, String picMd5Sum);
	
}
