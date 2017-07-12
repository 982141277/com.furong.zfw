package com.my.android.library;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/1/24 0024.
 *
 * @version 2015年4月22日 上午09:34:52
 * @Toast管理类，解决toast队列必须显示完后才会关闭的弊端
 */
public class ToastManager {

    private Toast toast;

    public static ToastManager INSTANCE;

    private Context mContext;

    /**
     * 初始化
     */
    public static ToastManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ToastManager(context);
        }
        return INSTANCE;
    }

    private ToastManager(Context context) {
        this.mContext = context;
    }

    /**
     * Toast消息时长
     */
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

    /**
     * Toast消息SHORT
     */
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

    /**
     * Toast消息屏幕中间位置
     */
    public void showToastcenter(String msg) {

        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);

        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);

        }
        toast.show();
    }

    /**
     * Toast消息屏幕顶部位置
     */
    public void showimgToasttop(String msg) {

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
        }
        toast.show();
    }
}
