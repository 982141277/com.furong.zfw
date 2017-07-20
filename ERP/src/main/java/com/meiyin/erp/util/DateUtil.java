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

}
