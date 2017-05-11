package org.beangle.wechat.core.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

public class DownloadAction extends BaseWechatAction {

	public String online() {

		HttpServletRequest req = ServletActionContext.getRequest();
			// -----------------//
			// 此方法需要浏览器自己能够打开，ios可以但是微信andriod版内置浏览器不支持
			// -----------------//
			// 如果是苹果手机
			// 获得文件地址
			String fileUrl = ServletActionContext.getRequest().getParameter("url");
			try {
				URL oaUrl = new URL(fileUrl);
				HttpURLConnection httpConn = (HttpURLConnection) oaUrl.openConnection();
				InputStream in = httpConn.getInputStream();
				// 获取输出流
				HttpServletResponse response = ServletActionContext.getResponse();
				req.setCharacterEncoding("UTF-8");
				response.setContentType("text/html; charset=utf-8");
				String fileName = get("fileName");
				if(StringUtils.isBlank(fileName)) {
					fileName = StringUtils.substringAfterLast(fileUrl, "/");
				}
				if (fileName != null) {
					String contentType = "";
					if (fileName.endsWith(".flv") || fileName.endsWith(".swf")) {
						contentType = "application/x-shockwave-flash";
					} else {
						if(fileName.endsWith(".mp4")){
							contentType = "video/mp4";
						}else if(fileName.endsWith(".pdf")){
							contentType = "application/pdf";
						}else{
							String imgtypes = "jpg,jpeg,gif,png,bmp";
							String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
							if (imgtypes.indexOf(ext) >= 0) {
								contentType = "image/jpeg";
							} else {
								contentType = "application/zip";
							}
						}
					}
					response.setContentType(contentType);
				}
				response.setHeader("Content-Disposition", "attachment;filename=\"" +  new String(fileName.getBytes("utf-8"), "ISO-8859-1") + "\"");
				OutputStream out = response.getOutputStream();
				// 输出图片信息
				byte[] bytes = new byte[1024];
				int cnt = 0;
				while ((cnt = in.read(bytes, 0, bytes.length)) != -1) {
					out.write(bytes, 0, cnt);
				}
				out.flush();
				out.close();
				in.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;

	}

}
