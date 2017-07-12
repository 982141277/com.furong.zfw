package com.jcd.psms.Util;

/**
 * Created by Administrator on 2017/5/25 0025.
 */
        import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;
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
}
