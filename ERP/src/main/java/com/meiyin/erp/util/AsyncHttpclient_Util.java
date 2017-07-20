package com.meiyin.erp.util;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.meiyin.erp.json.JsonHttpHandles;

/**
 * @author liuyuting
 * @version 2015年9月30日 上午09:34:52
 */
public class AsyncHttpclient_Util {

	/**
	 * POST请求
	 */
	public static void post(String url,Context context,RequestParams params,JsonHttpHandles jsonhttphandles) {
		AsyncHttpClient client = new AsyncHttpClient();
		if (client != null) {
			try {
				client.post(url,params,jsonhttphandles);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 文件下载
	 */
	public RequestHandle download(String url,Context context,FileAsyncHttpResponseHandler FileHttpHandler) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestHandle requestHandle = null;
		if (client != null) {
			try {
				requestHandle = client.get(url,null,FileHttpHandler);
			} catch (Exception e) {
			}
		}
		return requestHandle;
	}
	/**
	 * POST请text
	 */
	public static void postText(String url,Context context,RequestParams params,TextHttpResponseHandler textHttpResponseHandler) {
		AsyncHttpClient client = new AsyncHttpClient();
		if (client != null) {
			try {
				client.post(url,params,textHttpResponseHandler);
			} catch (Exception e) {
			}
		}
	}
}
