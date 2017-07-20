package com.meiyin.erp.util;
import android.content.Context;
import android.widget.Toast;
/**
 * @author liuyuting
 * @version 2015年9月10日 上午09:34:52
 */
public class ToastUtil {

	public static void showToast(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	public static void showLongToast(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
}
