package com.yoyling.tools;

import java.io.UnsupportedEncodingException;

/**
 * @author RawChen
 * @date 2021/9/5 23:15
 */
public class UrlUtil {
	private final static String ENCODE = "UTF-8";

	/**
	 * URL 解码
	 * @param str
	 * @return
	 */
	public static String getURLDecoderString(String str) {
		String result = "";
		if (null == str) {
			return "";
		}
		try {
			result = java.net.URLDecoder.decode(str, ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * URL 转码
	 * @param str
	 * @return
	 */
	public static String getURLEncoderString(String str) {
		String result = "";
		if (null == str) {
			return "";
		}
		try {
			result = java.net.URLEncoder.encode(str, ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		String str = "测试1";
		System.out.println(getURLEncoderString(str));
		System.out.println(getURLDecoderString("%E6%B5%8B%E8%AF%951"));
		System.out.println(getURLDecoderString("%E6%88%91%E7%9A%84%E5%A4%A9%E7%A9%BA - %E5%8D%97%E5%BE%81%E5%8C%97%E6%88%98.mp3"));

	}

}
