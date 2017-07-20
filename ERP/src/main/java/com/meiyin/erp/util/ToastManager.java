package com.meiyin.erp.util;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 
 * @author zhaojing
 * @version 2015年4月22日 上午09:34:52
 * 
 *          Toast管理类，解决toast队列必须显示完后才会关闭的弊端
 */
public class ToastManager {

	private Toast toast;

	public static ToastManager INSTANCE;

	private Context mContext;

	public static ToastManager getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new ToastManager(context);
		}
		return INSTANCE;
	}

	private ToastManager(Context context) {
		this.mContext = context;
	}

	public void showToast(String msg, int time) {

		if (Looper.myLooper() != Looper.getMainLooper()) {
			return;
		}

		if (toast == null) {
			toast = Toast.makeText(mContext, msg, time);
		} else {
			toast.setText(msg);
			toast.setDuration(time);
		}
		toast.show();
	}

	public void showToast(String msg) {

		if (Looper.myLooper() != Looper.getMainLooper()) {
			return;
		}

		if (toast == null) {
			toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.show();
	}
	
	public void showimgToast(String msg) {

		if (Looper.myLooper() != Looper.getMainLooper()) {
			return;
		}
		if (toast == null) {
			toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 40);
		} else {
			toast.setText(msg);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 40);			
//			LinearLayout toastview=(LinearLayout) toast.getView();
//			LinearLayout submit_dialog = (LinearLayout) LayoutInflater
//					.from(mContext).inflate(R.layout.toast_item, null);
//			toastview.addView(submit_dialog, 0);
		}
		toast.show();
	}
}
