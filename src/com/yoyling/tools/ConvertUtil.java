package com.yoyling.tools;

import java.text.DecimalFormat;

/**
 * @author RawChen
 * @date 2021/9/6 10:15
 */
public class ConvertUtil {
	/**
	 * byte转KB、MB、GB
	 * @param size
	 * @return
	 */
	public static String converFileSize(long size) {
		StringBuffer bytes = new StringBuffer();
		DecimalFormat format = new DecimalFormat("###.00");
		if (size >= 1024 * 1024 * 1024) {
			double i = (size / (1024.0 * 1024.0 * 1024.0));
			bytes.append(format.format(i)).append(" GB");
		} else if (size >= 1024 * 1024) {
			double i = (size / (1024.0 * 1024.0));
			bytes.append(format.format(i)).append(" MB");
		} else if (size >= 1024) {
			double i = (size / (1024.0));
			bytes.append(format.format(i)).append(" KB");
		} else if (size < 1024) {
			if (size <= 0) {
				bytes.append("0B");
			} else {
				bytes.append((int) size).append("B");
			}
		}
		return bytes.toString();
	}

	/**
	 * 毫秒时间格式化为时分秒
	 * @param millis
	 * @return
	 */
	public static String millisToStringShort(long millis) {
		StringBuilder strBuilder = new StringBuilder();
		long temp = millis;
		long hper = 60 * 60 * 1000;
		long mper = 60 * 1000;
		long sper = 1000;
		if (temp / hper > 0) {
			strBuilder.append(temp / hper).append(" 时");
		}
		temp = temp % hper;

		if (temp / mper > 0) {
			strBuilder.append(temp / mper).append(" 分");
		}
		temp = temp % mper;
		if (temp / sper > 0) {
			strBuilder.append(temp / sper).append(" 秒");
		}
		return strBuilder.toString();
	}
}
