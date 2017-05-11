package org.beangle.wechat.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.beangle.web.util.WebConfig;

public class WechatThreadManager {
	
	private static ExecutorService pool;
	
	private static Integer count = 100;
	
	public static final String WECHAT_POOL_SIZE = "wechat.pool.size";

	public static ExecutorService getPool() {
		if(pool == null) {
			String poolSize = WebConfig.get(WECHAT_POOL_SIZE);
			if(StringUtils.isNotBlank(poolSize)) {
				count = Integer.parseInt(poolSize);
			}
			pool = Executors.newFixedThreadPool(count);
		}
		return pool;
	}

}
