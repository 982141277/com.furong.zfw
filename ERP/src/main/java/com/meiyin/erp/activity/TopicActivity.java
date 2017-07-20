package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.TopicList_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Topic_Entity;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @param公司公告列表
 * @Time 2016-5-24
 */
public class TopicActivity extends Activity{
	private Context context;
	private AlertDialog dialog;
	private TopicList_Adapter adapter;
	private ArrayList<Topic_Entity> memu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.topic_main);
		context = getApplicationContext();
		SharedPreferences sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		
		/*
		 * 初始化
		 */
		initView();
		initData(sp);
		initHeader();
	}

	/*
	 * 初始化Data
	 */
	private void initData(SharedPreferences sp) {
		// TODO Auto-generated method stub
		Dialog progressDialog =  new Dialog_Http_Util().showWaitingDialog(TopicActivity.this,
				 "正在刷新", false);
		RequestParams param = new RequestParams();
		param.setContentEncoding("utf-8");
		param.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
		param.put("pager", 0);//pager
		new AsyncHttpClientUtil().post(APIURL.CHECK.TOPICMEMU, context, param, new JsonHandlerInit(TopicActivity.this,progressDialog));
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
		headtitletext.setText("公司公告");
	}
	/*
	 * 初始化view
	 */
	private void initView() {
		// TODO Auto-generated method stub
		ListView topic_listview=(ListView) findViewById(R.id.topic_listview);
		topic_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				startActivity(new Intent().setClass(TopicActivity.this, TopicDetailsActivity.class).putExtra("topic_code", memu.get(arg2).getTopic_code()));
			}
		});
		adapter = new TopicList_Adapter(context);
		topic_listview.setAdapter(adapter);
	}
	
	// 公告list初始化
	private class JsonHandlerInit extends MJsonHttpHandler {
		private Dialog progressDialog;
		protected JsonHandlerInit(Context context,Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog=progressDialog;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			progressDialog.dismiss();
			if (!response.isNull("errorMsg")) {
				try {
					ToastManager.getInstance(context).showToast(
							response.getString("errorMsg"));
					stopService(new Intent()
							.setAction("com.meiyin.services.Meiyinservice"));
					startActivity(new Intent().setClass(context, Login.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					System.exit(0);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			JSONArray data = null;
			try {
				data = response.getJSONArray("data");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			memu = new Gson().fromJson(data.toString(),
					new TypeToken<List<Topic_Entity>>() {
					}.getType());//公告list

			adapter.setList(memu);
			adapter.notifyDataSetChanged();
		}

	}
}
