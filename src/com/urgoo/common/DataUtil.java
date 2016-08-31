package com.urgoo.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtil {

	public static String getSubmit() {
		Calendar c = Calendar.getInstance();
		// int hour = c.get(Calendar.HOUR);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		// hour = hour + 12;
		int minute = c.get(Calendar.MINUTE);
		String str = "";
		if (hour < 11) {
			str = "上午" + String.valueOf(hour) + ":" + String.valueOf(minute);
		} else if (hour >= 11 && hour < 13) {
			str = "中午" + String.valueOf(hour) + ":" + String.valueOf(minute);
		} else if (hour >= 13 && hour < 18) {
			str = "下午" + String.valueOf(hour) + ":" + String.valueOf(minute);
		} else if (hour >= 18 && hour <= 24) {
			str = "晚上" + String.valueOf(hour) + ":" + String.valueOf(minute);
		}

		return str;
	}

	public static String getReplyTime(String strTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = sdf.parse(strTime);
			int mouth = date.getMonth();
			int day = date.getDate();
			int hour = date.getHours();
			int minute = date.getMinutes();
			String str = (mouth + 1) + "月" + day + "日";
			if (hour < 11) {
				str += "上午" + String.valueOf(hour) + ":"
						+ String.valueOf(minute);
			} else if (hour >= 11 && hour < 13) {
				str += "中午" + String.valueOf(hour) + ":"
						+ String.valueOf(minute);
			} else if (hour >= 13 && hour < 18) {
				str += "下午" + String.valueOf(hour) + ":"
						+ String.valueOf(minute);
			} else if (hour >= 18 && hour <= 24) {
				str += "晚上" + String.valueOf(hour) + ":"
						+ String.valueOf(minute);
			}
			return str;

		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return "";
	}


	/**
	 *
	 * @param
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	 * @author fy.zhang
	 */
	public static String formatDuring(int mss) {
		long days = mss / (60 * 60 * 24);
		long hours = (mss % (60 * 60 * 24)) / (60 * 60);
		long minutes = (mss % (60 * 60)) / 60;
		long seconds = mss % 60;
		if (days > 0) {
			return days + "天";
		} else if (hours > 0){
			return hours + "小时" + minutes + "分钟"
					+ seconds + "秒";
		} else if (minutes > 0) {
			return minutes + "分钟" + seconds + "秒 ";
		} else {
			return seconds + "秒";
		}
	}


	/**
	 *
	 * @param
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	 * @author fy.zhang
	 */
	public static String formatDuring2(int mss) {
		long days = mss / (60 * 60 * 24);
		long hours = (mss % (60 * 60 * 24)) / (60 * 60);
		long minutes = (mss % (60 * 60)) / 60;
		long seconds = mss % 60;
		if (days > 0) {
			return days + "天";
		} else if (hours > 0){
			return hours + "小时" ;
		} else if (minutes > 0) {
			return minutes + "分钟";
		} else {
			return seconds + "秒";
		}
	}


	/**
	 *
	 * @param
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	 * @author fy.zhang
	 */
	public static String formatDuring3(int mss) {

		int hours = mss / 3600;
		int minutes = (mss - hours * 3600) / 60;

		String str = "";

		if (hours > 0) {
			str = hours + "小时";
		} else if (minutes > 0) {
			str+= minutes + "分钟";
		}

		return str;
	}

}
