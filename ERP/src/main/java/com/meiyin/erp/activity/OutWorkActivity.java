package com.meiyin.erp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.OutWork_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.OutWork_Entity;
import com.meiyin.erp.ui.XListView;
import com.meiyin.erp.util.AsyncHttpclient_Util;
import com.meiyin.erp.util.DateUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 外勤列表
 */
public class OutWorkActivity extends Activity implements XListView.IXListViewListener,OnItemClickListener{
	
		private SharedPreferences sp;
		private Context content;
		private Handler mHandler;
		private XListView outwork_listview;
		private OutWork_Adapter adapter;
		private ArrayList<OutWork_Entity> outworklist;
		private int load=1;
		private Dialog_Http_Util dhu;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.outwork_main);
			content = getApplicationContext();
			sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
					Context.MODE_PRIVATE);
			initHeader();
			dhu = new Dialog_Http_Util();
			outworklist = new ArrayList<OutWork_Entity>();
			adapter = new OutWork_Adapter(content);
			outwork_listview = (XListView) findViewById(R.id.outwork_listview);
			outwork_listview.setOnItemClickListener(this);
			outwork_listview.setPullLoadEnable(true);
			outwork_listview.setXListViewListener(this);
			outwork_listview.setRefreshTime(sp.getString(SPConstant.REFRESHTIME, ""));
			mHandler = new Handler();
			adapter.setList(outworklist);
			outwork_listview.setAdapter(adapter);
			http(0,false);

		}
		/*
		 * 初始化标题UI
		 */
		private void initHeader() {
			ImageView headtitleimageView = (ImageView) findViewById(R.id.headtitleimageView);
			headtitleimageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			TextView headtitletext = (TextView) findViewById(R.id.headtitletext);
			headtitletext.setText("外勤系统");
		}
		public void http(int page,Boolean tos) {
			dhu.showWaitingDialog(OutWorkActivity.this, "正在加载..", true);
			RequestParams params = new RequestParams();
			String key = sp.getString(SPConstant.MY_TOKEN, "");
			params.put("key", key);//key
			params.put("page",page);//page分页
			params.put("size",10);//page分页
			AsyncHttpclient_Util.postText(APIURL.GETOUTWORK, content, params, new outworkTextHandler(tos));

		}
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			if(arg2>0){
				if(outworklist.size()>0){
					String _id = outworklist.get(arg2-1).get_id();
					String outtype = outworklist.get(arg2-1).getOuttype();
					intent.setClass(this, OutWorkDetailsActivity.class);
					intent.putExtra("id", _id);
					intent.putExtra("outtype", outtype);
					startActivity(intent);
				}
			}
		}
		private void onLoad() {
			String RefreshTime= DateUtil
			.convertLongToDate(System
					.currentTimeMillis());
			outwork_listview.stopRefresh();
			outwork_listview.stopLoadMore();
			outwork_listview.setRefreshTime(RefreshTime);
			sp.edit().putString(SPConstant.REFRESHTIME, RefreshTime).commit();
		}
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					outworklist.clear();
					load=1;
					http(0,true);
				}
			},1);
		}
		@Override
		protected void onRestart() {
		// TODO Auto-generated method stub
			if(OutWorkDetailsActivity.Refresh){
			outworklist.clear();
			load=1;
			http(0,false);
			}
			super.onRestart();
		}
		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					http(load,false);
					load++;
				}
			}, 1);
		}
		private class outworkTextHandler extends TextHttpResponseHandler {
			private Boolean tos;
			private outworkTextHandler(Boolean tos){
				this.tos=tos;
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				// TODO Auto-generated method stub
				
				JSONObject jsonobject;
				try {
					if(responseString.contains("errorMsg")){
						jsonobject = new JSONObject(responseString);
						ToastManager.getInstance(content).showToast(jsonobject.getString("errorMsg"));						
					}else{
						Log.e("lyt", responseString);
						ArrayList<OutWork_Entity> list = new Gson().fromJson(responseString,
								new TypeToken<List<OutWork_Entity>>() {
								}.getType());
						if(list.size()<9){
							ToastManager.getInstance(content).showToast("已加载全部！");
						}
						for (OutWork_Entity outWork_Entity : list) {
							outworklist.add(outWork_Entity);
						}
						adapter.setList(outworklist);
						adapter.notifyDataSetChanged();
						if(tos){
							ToastManager.getInstance(content).showToast("刷新成功！");
						}
					}
		
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				onLoad();
				dhu.dismissWaitingDialog();
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				dhu.dismissWaitingDialog();
				Log.e("xx", statusCode+responseString+throwable.toString());
				if(statusCode==0&&throwable.toString().contains("HttpHostConnect")){
					ToastManager.getInstance(content).showToast("网络连接失败,请检查网络设置！");
				}else if(statusCode==200){
					ToastManager.getInstance(content).showToast("账号异常请联系技术支持部！");
				}else if(statusCode==0&&throwable.toString().contains("Timeout")){
					ToastManager.getInstance(content).showToast("服务连接超时！");
				}
				onLoad();
			}
		}
}
