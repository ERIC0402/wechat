package org.beangle.wechat.core.service.impl;

import java.util.List;

import org.beangle.model.query.builder.OqlBuilder;
import org.beangle.wechat.core.model.WechatCorpUser;
import org.beangle.wechat.core.model.WechatImage;
import org.beangle.wechat.core.model.WechatUser;
import org.beangle.wechat.core.service.WechatImageService;

public class WechatImageServiceImpl extends BaseWechatImpl implements WechatImageService {

	@Override
	public WechatImage getWechatImage(WechatUser wechatUser, WechatCorpUser wechatCorpUser, String picMd5Sum) {
		OqlBuilder<WechatImage> query =OqlBuilder.from(WechatImage.class, "wechatImage");
		if(wechatUser != null){
			query.where("wechatImage.wechatUser = :wechatUser", wechatUser);
		}else if(wechatCorpUser != null){
			query.where("wechatImage.wechatCorpUser = :wechatCorpUser", wechatCorpUser);
		}else{
			query.where("wechatImage.id = -1");
		}
		query.where("wechatImage.picMd5Sum = :picMd5Sum", picMd5Sum);
		List<WechatImage> wechatImages = entityDao.search(query);
		if(wechatImages != null && wechatImages.size() > 0){
			return wechatImages.get(0);
		}
		return null;
	}

}
