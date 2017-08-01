package com.meiyin.erp.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.meiyin.erp.activity.Login;
import com.meiyin.erp.application.BaseApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.service.Meiyinservice;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author liuyuting
 * @version 2015年9月10日 上午09:34:52
 */
public class AndroidUtil {
	/**
	 * 获取androidId
	 * 
	 * @param context
	 * @return
	 */
	public static String getAndroidId(Context context) {
		String android_id = android.provider.Settings.System.getString(context.getContentResolver(), Secure.ANDROID_ID);
		Log.e("lyt", "android_id"+android_id);
		return android_id;
	}
	/**
	 *  本方法判断自己些的一个Service是否已经运行
	 * @param context
	 * @return
	 */
		public static boolean isServiceRunning(Context context,String serviceName) {
			ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(30);
			for (int i = 0; i < runningService.size(); i++) {
				if (runningService.get(i).service.getClassName().toString()
						.equals(serviceName)) {
					return true;
				}
			}
			return false;
		}
		/**
		 * 获取设备型号
		 * 
		 * @param context
		 * @return
		 */
		public static String getDeviceModel(Context context) {
			return android.os.Build.MODEL;
		}
		/**
		 * 获取系统版本号
		 * 
		 * @param context
		 * @return
		 */
		public static String getSystemVersion(Context context) {
			return android.os.Build.VERSION.RELEASE;
		}
		/**
		 * Role:Telecom service providers获取手机服务商信息 <BR>
		 * 需要加入权限<uses-permission
		 * android:name="android.permission.READ_PHONE_STATE"/> <BR>
		 * 
		 * @author
		 */
		public static String getProvidersName(Context context) {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String ProvidersName = null;
			// 返回唯一的用户ID;就是这张卡的编号神马的
			String IMSI = telephonyManager.getSubscriberId();
			// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
			System.out.println(IMSI);
			if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
				ProvidersName = "中国移动";
			} else if (IMSI.startsWith("46001")) {
				ProvidersName = "中国联通";
			} else if (IMSI.startsWith("46003")) {
				ProvidersName = "中国电信";
			}
			return ProvidersName;
		}
		/**
		 * 返回当前程序版本名
		 */
		public static String getAppVersionName(Context context) {
			String versionName = "";
			try {
				// ---get the package info---
				PackageManager pm = context.getPackageManager();
				PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
				versionName = pi.versionName;
				// versioncode = pi.versionCode;
				if (versionName == null || versionName.length() <= 0) {
					return "";
				}
			} catch (Exception e) {
				// Log.e("VersionInfo", "Exception", e);
			}
			return versionName;
		}
	/**
	 * 拨号方法
	 */
	public static void callPhone(Activity activity,String phoneString)
	{
		Intent intent = new Intent();
		intent.setAction(intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:"
				+ phoneString));
		activity.startActivity(intent);
	}
	/**
	 * 检查安卓6.0以上播打电话权限问题
	 * 申请权限
	 */

	public static void requestPermission(Activity activity, String phoneString)
	{
		//判断Android版本是否大于23
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);

			if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED)
			{
					ActivityCompat.requestPermissions(activity,
							new String[]{Manifest.permission.CALL_PHONE},
							SPConstant.TAGCALLPHONE);
			}
			else
			{
				callPhone(activity,phoneString);
			}
		}
		else
		{
			callPhone(activity,phoneString);
		}
	}
	public static void LoginOut(Activity activity,String message){
			com.my.android.library.ToastManager.getInstance(activity).showToast(message);
			activity.stopService(new Intent(activity, Meiyinservice.class));
			activity.startActivity(new Intent().setClass(activity, Login.class)
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			BaseApplication.getInstance().AppExit();
			System.exit(0);
	}
	public static Observable<Spanned> async(final String html){
		Observable<Spanned> sender = Observable.create(new Observable.OnSubscribe<Spanned>() {

			@Override
			public void call(Subscriber<? super Spanned> subscriber) {
				Spanned notes = Html.fromHtml(html, new Html.ImageGetter() {
					@Override
					public Drawable getDrawable(String source) {
						InputStream is = null;
						try {
							is = (InputStream) new URL(source).getContent();
							Drawable d = Drawable.createFromStream(is, "src");
							d.setBounds(0, 0, d.getIntrinsicWidth(),
									d.getIntrinsicHeight());
							is.close();
							return d;
						} catch (Exception e) {
							return null;
						}
					}
				}, null);
				subscriber.onNext(notes);
			}

		}).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
		.observeOn(AndroidSchedulers.mainThread()); // 指定 Subscriber 的回调发生在主线程

		return sender;
	}

}
