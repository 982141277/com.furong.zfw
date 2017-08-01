package com.meiyin.erp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	public static String convertLongToDate(long time){
		Date now = new Date(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String currentTime = dateFormat.format(now);
		return currentTime;
	}

	public static String convertLong00(long time) {
		Date now = new Date(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd 00:00:00");// 可以方便地修改日期格式
		String currentTime = dateFormat.format(now);
		return currentTime;
	}

	public static String convertLongToDateWithoutsecond(long time) {
		Date now = new Date(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");// 可以方便地修改日期格式
		String currentTime = dateFormat.format(now);
		return currentTime;
	}

	public static String getCurrentTimeStr() {
		return convertLongToDate(System.currentTimeMillis());
	}

	public static Date getDateFromStr(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");
		try {
			Date date = dateFormat.parse(time);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Long getLongFromStr(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		try {
			Date date = dateFormat.parse(time);
			long s = date.getTime();
			return s;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getHourTimeStr() {
		Date now = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");// 可以方便地修改日期格式
		String currentTime = dateFormat.format(now);
		return currentTime;
	}

	public static String getHourTimeStr(long time) {
		Date now = new Date(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");// 可以方便地修改日期格式
		String currentTime = dateFormat.format(now);
		return currentTime;
	}

	public static String getEnglishTime(String time) {
		SimpleDateFormat formatFrom = new SimpleDateFormat(
				"MMM dd,yyyy hh:mm:ss aa", Locale.ENGLISH);
		String currentTime = "";
		try {
			Date date = formatFrom.parse(time);
			SimpleDateFormat formatto = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			currentTime = formatto.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currentTime;
	}

	public static String gettimes(Long t,Long t1) {
		long time = t - t1;
		long times = time /1000/60;
		if (times < 1) {
			return "刚刚";
		} else if (times > 1 && times < 60) {
			return (int) times + "分钟前";
		} else if (times > 60) {
			times = times / 60;
			if (times > 24) {
				times = times / 24;
				if (times < 60) {
					if (times > 1 && times < 2) {
						return "昨天";
					} else if (times > 2 && times < 7) {
						return (int) times + "天前";
					} else if (times > 7 && times < 28) {
						return (int) times / 7 + "周前";
					} else if (times > 28 && times < 60) {
						return "1个月前";
					}
				} else if (times > 60 && times < 365) {
					times = times / 30;
					return (int) times + "个月前";
				} else if (times > 365) {
					times = times / 365;
					return (int) times + "年前";
				}
			} else {
				return (int) times + "小时前";
			}
		}
		return "";
	}

}
