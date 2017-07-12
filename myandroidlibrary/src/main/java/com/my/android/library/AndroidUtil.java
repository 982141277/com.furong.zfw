package com.my.android.library;


import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2017/1/24 0024.
 *
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
        return android_id;
    }

    /**
     * 本方法判断自己些的一个Service是否已经运行
     *
     * @param context
     * @return
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
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
     * 获取屏幕的宽度
     */
    public int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    /**
     * 获取屏幕的高度
     */
    public int getScreenHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    /**
     * 获取状态栏的高度
     */
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    // android.view.WindowManager.LayoutParams la = new
    // WindowManager.LayoutParams();
    // la.x = getScreenWidth()/2;
    // la.y =
    // -getScreenHeight()/2+imageView_setting.getHeight()+getStatusBarHeight()+110;
    // LogUtil.e("lyt","y"+la.y+"-屏幕高度-"+getScreenHeight()/2+"-设置图片高度-："+imageView_setting.getHeight()+"-状态栏高度-:"+getStatusBarHeight());
    // dialogs.setCanceledOnTouchOutside(true);
    // dialogs.getWindow().setAttributes(la);
    // dialogs.show();
    // dialogs.getWindow().setContentView(setting_dialog, la);
}

