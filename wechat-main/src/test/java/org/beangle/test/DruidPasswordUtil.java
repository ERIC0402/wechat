package org.beangle.test;

import com.alibaba.druid.filter.config.ConfigTools;

/**
 * 数据库用户密码加密工具
 * 
 * @创建日期：2015年2月11日 下午1:41:07
 */
public class DruidPasswordUtil {
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		String password = "weixin";
		String decryptText = "Biyu5YzU+6sxDRbmWEa3B2uUcImzDo0BuXjTlL505+/pTb+/0Oqd3ou1R6J8+9Fy3CYrM18nBDqf6wAaPgUGOg==";
		
		ConfigTools configTools = new ConfigTools();
		try {
			System.out.println(configTools.encrypt(password));
			System.out.println(configTools.decrypt(decryptText));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
