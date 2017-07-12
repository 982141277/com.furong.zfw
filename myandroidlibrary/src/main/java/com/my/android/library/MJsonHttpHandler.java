package com.my.android.library;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by Administrator on 2017/1/24 0024.
 */

public class MJsonHttpHandler extends JsonHttpResponseHandler {

    protected Context context;
    protected Dialog progressDialog;
    protected String mString;
    protected int mi;

    protected MJsonHttpHandler(String mString, Context context, Dialog progressDialog) {
        this.mString = mString;
        this.context = context;
        this.progressDialog = progressDialog;
    }

    protected MJsonHttpHandler(int mi, Context context, Dialog progressDialog) {
        this.mi = mi;
        this.context = context;
        this.progressDialog = progressDialog;
    }

    protected MJsonHttpHandler(Context context, Dialog progressDialog) {
        this.context = context;
        this.progressDialog = progressDialog;
    }

    @Override
    public void onFailure(int statusCode, Header[] headers,
                          Throwable throwable, JSONObject errorResponse) {
        // TODO Auto-generated method stub
        dismissWaitingDialog();
        if (statusCode == 0 && throwable.toString().contains("HttpHostConnect")) {
            ToastManager.getInstance(context).showToast("网络连接失败,请检查网络设置！");
        } else if (statusCode == 200) {
            Log.e("lyt", "header：" + headers.toString() + "throw：" + throwable.toString());
            ToastManager.getInstance(context).showToast("JSON数据加载出错！");
        } else if (statusCode == 0 && throwable.toString().contains("Timeout")) {
            ToastManager.getInstance(context).showToast("服务连接超时！");
        }
        super.onFailure(statusCode, headers, throwable, errorResponse);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers,
                          String responseString, Throwable throwable) {
        // TODO Auto-generated method stub
        dismissWaitingDialog();
        ToastManager.getInstance(context).showToast("网络连接失败！");
        super.onFailure(statusCode, headers, responseString, throwable);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers,
                          Throwable throwable, JSONArray errorResponse) {
        // TODO Auto-generated method stub
        dismissWaitingDialog();
        ToastManager.getInstance(context).showToast("网络连接失败！");
        super.onFailure(statusCode, headers, throwable, errorResponse);
    }

    public void dismissWaitingDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}