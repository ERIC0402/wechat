package org.beangle.wechat.core.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.beangle.model.persist.impl.BaseServiceImpl;
import org.beangle.wechat.core.service.WechatJoinService;
import org.beangle.wechat.core.util.aes.AesException;
import org.beangle.wechat.core.util.aes.WXBizMsgCrypt;

public class WechatJoinServiceImpl extends BaseServiceImpl implements WechatJoinService {

	public boolean checkSignature(String token,String signature, String timestamp, String nonce) {
		if(StringUtils.isNotBlank(token) && StringUtils.isNotBlank(token) && StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(nonce)){
			String[] arr = new String[] { token, timestamp, nonce };
			// 将token、timestamp、nonce三个参数进行字典序排序
			Arrays.sort(arr);
			StringBuilder content = new StringBuilder();
			for (int i = 0; i < arr.length; i++) {
				content.append(arr[i]);
			}
			MessageDigest md = null;
			String tmpStr = null;

			try {
				md = MessageDigest.getInstance("SHA-1");
				// 将三个参数字符串拼接成一个字符串进行sha1加密
				byte[] digest = md.digest(content.toString().getBytes());
				tmpStr = byteToStr(digest);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			content = null;
			// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
			return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
		}
		return false;
	}

	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;
	}
	
	public String getEchostr(String token, String encodeingAESKey, String corpid, String timestamp, String nonce, String echostr, String signature) throws AesException {
		return new WXBizMsgCrypt(token, encodeingAESKey, corpid).VerifyURL(signature, timestamp, nonce, echostr);
	}

}
