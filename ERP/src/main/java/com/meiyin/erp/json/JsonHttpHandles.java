package com.meiyin.erp.json;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.meiyin.erp.application.MyApplication;
import com.meiyin.erp.util.ToastManager;

import org.apache.http.Header;
import org.json.JSONObject;


public class JsonHttpHandles extends JsonHttpResponseHandler{

		private Context context= MyApplication.getContext();
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			if(statusCode==0&&throwable.toString().contains("HttpHostConnect")){
				ToastManager.getInstance(context).showToast("网络连接失败,请检查网络设置！");
			}else if(statusCode==200){
				ToastManager.getInstance(context).showToast("账号异常请联系技术支持部！");
			}else if(statusCode==0&&throwable.toString().contains("Timeout")){
				ToastManager.getInstance(context).showToast("服务连接超时！");
			}
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}
}