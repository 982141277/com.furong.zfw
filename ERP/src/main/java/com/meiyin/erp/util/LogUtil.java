package com.meiyin.erp.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 这个是日志打印工具类，在项目正式发布时，将isPrint设置为false则所有的日志不会打印在控制台
 * 
 * @author Veronica
 * 
 */
public class LogUtil {
	// TODO ***********************SDK发布时请将此变量设置为私有的 **********************************
	public final static boolean isPrint = true;
	// 增加一个test属性用于防止测试代码因疏忽导致没有关闭
	public final static boolean test = isPrint;
	// TODO ***********************SDK发布时请将上面变量设置为私有的 **********************************
	public static void i(String tag, String message) {
		if (isPrint) {
			if (tag != null && message != null && !"".equals(tag.trim())
					&& !"".equals(message.trim())) {
				android.util.Log.i(tag, message);
			}
		}
	}
    
	public static void d(String tag, String message) {
		if (isPrint) {
			if (tag != null && message != null && !"".equals(tag.trim())
					&& !"".equals(message.trim())) {
				android.util.Log.d(tag, message);
			}
		}
	}

	public static void e(String tag, String message) {
		if (isPrint) {
			if (tag != null && message != null && !"".equals(tag.trim())
					&& !"".equals(message.trim())) {
				android.util.Log.e(tag, message);
			}
		}
	}

	public static void w(String tag, String message) {
		if (isPrint) {
			if (tag != null && message != null && !"".equals(tag.trim())
					&& !"".equals(message.trim())) {
				android.util.Log.w(tag, message);
			}
		}
	}

	public static void e(Exception e) {
		if (isPrint) {
			if (e != null) {
				e.printStackTrace();
			}
		}
	}

	public static void showToast(Context context, String content) {
		if (isPrint) {
			if (context != null && content != null)
				Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
		}
	}

}
