package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.Xbj_List_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Xjb_List_Entity;
import com.meiyin.erp.ui.XListView;
import com.meiyin.erp.util.DateUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.LogUtil;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class XjbActivity extends Activity {

	private Context context;
	private Dialog_Http_Util dialog_util;
	private AsyncHttpClientUtil async;
	private SharedPreferences sp;
	private ArrayList<Xjb_List_Entity> listxbj;
	private Xbj_List_Adapter adapter;
	private XListView xjb_listview;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xjb_main);
		context = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		/*
		 * 初始化
		 */
		initHeader();
		initView();

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
		headtitletext.setText("巡检列表");
	}

	/*
	 * 初始化View
	 */
	private void initView() {
		// TODO Auto-generated method stub
		final Handler mHandler = new Handler();
		listxbj = new ArrayList<Xjb_List_Entity>();
		xjb_listview = (XListView) findViewById(R.id.xjb_listview);
		xjb_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (listxbj.size() > 0) {
					Intent intent = new Intent();
					intent.putExtra("em_id", listxbj.get(arg2 - 1).getEm_id());
					intent.putExtra("Exam_man_name", listxbj.get(arg2 - 1).getExam_man_name());
					intent.putExtra("result", SPConstant.TAGXJBACTIVITY);
					intent.setClass(context, XjbDetailsActivity.class);
					startActivityForResult(intent,  SPConstant.TAGXJBACTIVITY);
				}
			}
		});

		xjb_listview.setPullLoadEnable(true);
		xjb_listview.setXListViewListener(new XListView.IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						xjb_post(0);
					}
				}, 10);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						int load;
						if (listxbj.size() < 20) {
							load = 0;
						} else {
							load = listxbj.size();
						}
						xjb_post(load);
					}
				}, 10);
			}
		});
		xjb_listview.setRefreshTime(sp.getString(SPConstant.REFRESHTIME, ""));
		adapter = new Xbj_List_Adapter(context);
		adapter.setList(listxbj);
		xjb_listview.setAdapter(adapter);
		xjb();
		xjb_post(0);

	}
	
	public void stopload() {
		String RefreshTime = DateUtil.convertLongToDate(System
				.currentTimeMillis());
		xjb_listview.stopRefresh();
		xjb_listview.stopLoadMore();
		xjb_listview.setRefreshTime(RefreshTime);
		sp.edit().putString(SPConstant.REFRESHTIME, RefreshTime).commit();
	}
	public void xjb() {
		dialog_util = new Dialog_Http_Util();
		async = new AsyncHttpClientUtil();
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		RequestParams param = new RequestParams();
		param.setContentEncoding("utf-8");
		param.put("key", key);
		Dialog progressDialog = dialog_util.showWaitingDialog(XjbActivity.this,
				"正在刷新", false);
		async.post(APIURL.ITSM.IT_DOREFRESHDATAAPP, context, param, new JsonHandlers(
				XjbActivity.this, progressDialog));
	}

	public void xjb_post(int pager) {
		dialog_util = new Dialog_Http_Util();
		async = new AsyncHttpClientUtil();
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		RequestParams param = new RequestParams();
		param.setContentEncoding("utf-8");
		param.put("key", key);
		param.put("pager", pager);
		Dialog progressDialog = dialog_util.showWaitingDialog(XjbActivity.this,
				"正在刷新", false);
		async.post(APIURL.ITSM.IT_EXAMLIST, context, param, new JsonHandler(
				XjbActivity.this, progressDialog,pager));
	}

	// 巡检表列表
	private class JsonHandler extends MJsonHttpHandler {
		private Dialog progressDialog;
		private int pager;

		protected JsonHandler(Context context, Dialog progressDialog,int pager) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
			this.pager=pager;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			LogUtil.e("lyt", response.toString());
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
			try {
				if (pager ==0) {
					listxbj.clear();
				}
				stopload();
				String message = response.getString("message");
				if (message.equals("success")) {
					JSONArray array = response.getJSONArray("dlist");
					ArrayList<Xjb_List_Entity> list = new Gson().fromJson(
							array.toString(),
							new TypeToken<List<Xjb_List_Entity>>() {
							}.getType());

					for (Xjb_List_Entity item : list) {
						listxbj.add(item);
					}
					adapter.setList(listxbj);
					adapter.notifyDataSetChanged();
				} else {
					ToastManager.getInstance(context)
							.showToastcenter("加载失败！！！");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	// 巡检表列表
		private class JsonHandlers extends MJsonHttpHandler {
			private Dialog progressDialog;

			protected JsonHandlers(Context context, Dialog progressDialog) {
				super(context, progressDialog);
				this.progressDialog = progressDialog;

			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				LogUtil.e("lyt", response.toString());
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
				try {
					String message = response.getString("message");
					if (message.equals("success")) {
						ToastManager.getInstance(context)
						.showToastcenter("更新成功！！！");
					} else {
						ToastManager.getInstance(context)
								.showToastcenter("更新失败！！！");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case SPConstant.TAGXJBACTIVITY:
			Bundle bundle = data.getExtras();
			if (null != bundle) {
				if (bundle.getBoolean("refresh", true)) {
					xjb_post(0);
				}
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
