package org.beangle.wechat.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import net.sf.json.JSONObject;

public class MapPositionUtil {

	private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

	private static final double pi = 3.14159265358979324;
	private static final double a = 6378245.0;
	private static final double ee = 0.00669342162296594323;

	private static double EARTH_RADIUS = 6378.137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 通过(GPS)经纬度获取距离(单位：米)
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000d) / 10000d;
		s = s * 1000;
		return s;
	}

	public static String getBaiduMapInfo(Double latitude, Double longitude) {
		Point point = MapPositionUtil.wgs_bd_encrypt(latitude, longitude);
		JSONObject json = getBaiDuMapInfo(point.getLat(), point.getLng());
		StringBuilder sb = new StringBuilder("当前位置：");
		if (json != null && json.get("status").equals(0)) {
			sb.append(json.getJSONObject("result").get("formatted_address").toString());
			sb.append("\n纬度：" + point.getLat() + "\n经度：" + point.getLng());
		} else {
			sb.append("未获取到位置");
		}
		return sb.toString();
	}

	private static JSONObject getBaiDuMapInfo(double latitude, double longitude) {
		String requestUrl = String.format(WechatUtil.BAIDU_MAP, latitude, longitude);
		URL url;
		try {
			url = new URL(requestUrl);
			URLConnection connection = url.openConnection();
			/**
			 * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
			 * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
			 */
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
			// remember to clean up
			out.flush();
			out.close();
			// 一旦发送成功，用以下方法就可以得到服务器的回应：
			String res;
			InputStream l_urlStream = connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(l_urlStream, "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			while ((res = in.readLine()) != null) {
				buffer.append(res.trim());
			}
			return JSONObject.fromObject(buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getBaiduMapProvince(Double latitude, Double longitude) {
		Point point = MapPositionUtil.wgs_bd_encrypt(latitude, longitude);
		JSONObject json = getBaiDuMapInfo(point.getLat(), point.getLng());
		if (json != null && json.get("status").equals(0)) {
			return json.getJSONObject("result").getJSONObject("addressComponent").get("province").toString();
		} else {
			return null;
		}
	}

	public static String getBaiduAddressInfo(Double latitude, Double longitude) {
		Point point = MapPositionUtil.wgs_bd_encrypt(latitude, longitude);
		JSONObject json = getBaiDuMapInfo(point.getLat(), point.getLng());
		System.out.println(json);
		if (json != null && json.get("status").equals(0)) {
			return json.getJSONObject("result").get("formatted_address").toString();
		} else {
			return null;
		}
	}

	public static String getBaiduStreetInfo(Double latitude, Double longitude) {
		if (latitude != null && longitude != null) {
			Point point = MapPositionUtil.wgs_bd_encrypt(latitude, longitude);
			JSONObject json = getBaiDuMapInfo(point.getLat(), point.getLng());
			if (json != null && json.get("status").equals(0)) {
				return json.getJSONObject("result").getJSONObject("addressComponent").get("street").toString() + json.getJSONObject("result").getJSONObject("addressComponent").get("street_number").toString();
			}
		}
		return null;
	}

	public static Point wgs_bd_encrypt(double wgs_lat, double wgs_lon) {
		Point point = wgs_gcj_encrypts(wgs_lat, wgs_lon);
		return google_bd_encrypt(point.getLat(), point.getLng());
	}

	/**
	 * gg_lat 纬度
	 * gg_lon 经度
	 * GCJ-02转换BD-09
	 * Google地图经纬度转百度地图经纬度
	 * */
	public static Point google_bd_encrypt(double gg_lat, double gg_lon) {
		Point point = new Point();
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		point.setLat(bd_lat);
		point.setLng(bd_lon);
		return point;
	}

	/**
	 * wgLat 纬度
	 * wgLon 经度
	 * BD-09转换GCJ-02
	 * 百度转google
	 * */
	public static Point bd_google_encrypt(double bd_lat, double bd_lon) {
		Point point = new Point();
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		point.setLat(gg_lat);
		point.setLng(gg_lon);
		return point;
	}

	/**
	 * wgLat 纬度
	 * wgLon 经度
	 * WGS-84 到 GCJ-02 的转换（即 GPS 加偏）
	 * */
	public static Point wgs_gcj_encrypts(double wgLat, double wgLon) {
		Point point = new Point();
		if (outOfChina(wgLat, wgLon)) {
			point.setLat(wgLat);
			point.setLng(wgLon);
			return point;
		}
		double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
		double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
		double radLat = wgLat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double lat = wgLat + dLat;
		double lon = wgLon + dLon;
		point.setLat(lat);
		point.setLng(lon);
		return point;
	}

	public static void transform(double wgLat, double wgLon, double[] latlng) {
		if (outOfChina(wgLat, wgLon)) {
			latlng[0] = wgLat;
			latlng[1] = wgLon;
			return;
		}
		double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
		double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
		double radLat = wgLat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		latlng[0] = wgLat + dLat;
		latlng[1] = wgLon + dLon;
	}

	private static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	private static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
		return ret;
	}

	public static class Point {
		private double lat;// 纬度
		private double lng;// 经度

		public Point() {
		}

		public Point(double lng, double lat) {
			this.lng = lng;
			this.lat = lat;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Point) {
				Point bmapPoint = (Point) obj;
				return (bmapPoint.getLng() == lng && bmapPoint.getLat() == lat) ? true : false;
			} else {
				return false;
			}
		}

		public String getLngAndLat() {
			return lng + "," + lat;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

		@Override
		public String toString() {
			return "Point [lat=" + lat + ", lng=" + lng + "]";
		}

	}

}
