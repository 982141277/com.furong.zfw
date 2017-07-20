package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.IT_Management_Adapter;
import com.meiyin.erp.adapter.Spinner_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.IT_Management_Event_Entity;
import com.meiyin.erp.entity.Spinner_Entity;
import com.meiyin.erp.json.JsonHttpHandles;
import com.meiyin.erp.ui.XListView;
import com.meiyin.erp.util.AsyncHttpclient_Util;
import com.meiyin.erp.util.DateUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.ToastManager;
import com.meiyin.erp.util.ToastUtil;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * IT运维管理事件列表页
 */
public class IT_Management_Event_List extends Activity implements OnItemClickListener, XListView.IXListViewListener {
	private Context context;
	private XListView XListView1;
	private ArrayList<IT_Management_Event_Entity> list;
	private IT_Management_Adapter adapter;
	private Handler mHandler;
	private SharedPreferences sp;
	private Dialog_Http_Util dialog;
	private int load = 1;
	private ArrayList<Spinner_Entity> spn;
	private Spinner_Adapter spinnerAdapter1;
	private String type_id="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.it_management_event_list);
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		mHandler = new Handler();
		context = getApplicationContext();
		initHeader();
		findview();
		dialog = new Dialog_Http_Util();
		httplist(sp.getString(SPConstant.MY_TOKEN, ""), 0);
		alldept(sp.getString(SPConstant.MY_TOKEN, ""));
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
		headtitletext.setText("事件处理");
		}
	public void findview() {
		spn = new ArrayList<Spinner_Entity>();
		spn.add(new Spinner_Entity("请选择运维组",""));
		Spinner itsm_dept_sp = (Spinner) findViewById(R.id.itsm_dept_sp);
		spinnerAdapter1 = new Spinner_Adapter(context);
		spinnerAdapter1.setList(spn);
		itsm_dept_sp.setAdapter(spinnerAdapter1);
		itsm_dept_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				type_id = spn.get(arg2).getType_id();
				list.clear();
				load = 1;
				httplist(sp.getString(SPConstant.MY_TOKEN, ""), 0);
				dialog.showWaitingDialog(IT_Management_Event_List.this, "正在加载", true);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		XListView1 = (XListView) findViewById(R.id.XListView1);
		XListView1.setOnItemClickListener(this);
		XListView1.setPullLoadEnable(true);
		XListView1.setXListViewListener(this);
		XListView1.setRefreshTime(sp.getString(SPConstant.REFRESHTIME, ""));

		list = new ArrayList<IT_Management_Event_Entity>();

		adapter = new IT_Management_Adapter(context,
				IT_Management_Event_List.this);
		adapter.setList(list);
		XListView1.setAdapter(adapter);

	}

	private void httplist(String key, int page) {
		dialog.showWaitingDialog(IT_Management_Event_List.this, "正在加载", false);
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("key", key);
		if(!type_id.equals("")){
		params.put("search_area",type_id);
		}
		String string = APIURL.ITSM.IT_EVENT_LIST;
		AsyncHttpclient_Util.post(string, context, params, new JsonHandler());
	}
	private void alldept(String key) {
		AsyncHttpClientUtil async = new AsyncHttpClientUtil();
		RequestParams params = new RequestParams();
		params.put("key", key);
		String string = APIURL.ITSM.IT_EVENT_DEPT;
		async.post(string, context, params, new JsonHandlerDept(IT_Management_Event_List.this,
				null));
	}

	private class JsonHandler extends JsonHttpHandles {
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			try {
				JSONArray array = (JSONArray) response.get("data");
				ArrayList<IT_Management_Event_Entity> lists = new Gson()
						.fromJson(
								array.toString(),
								new TypeToken<List<IT_Management_Event_Entity>>() {
								}.getType());
				for (IT_Management_Event_Entity item : lists) {
					list.add(item);
				}
				adapter.setList(list);
				adapter.notifyDataSetChanged();
				onLoad();
				dialog.dismissWaitingDialog();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			if (statusCode == 0
					&& throwable.toString().contains("HttpHostConnect")) {
				ToastManager.getInstance(context).showimgToast(
						"网络连接失败,请检查网络设置！");
			} else if (statusCode == 200) {
				ToastManager.getInstance(context).showimgToast("账号异常请联系软件研发部！");
			} else if (statusCode == 0
					&& throwable.toString().contains("Timeout")) {
				ToastManager.getInstance(context).showimgToast("服务连接超时！");
			}
			dialog.dismissWaitingDialog();
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			ToastUtil.showToast(context, "网络连接失败！");
			dialog.dismissWaitingDialog();
			super.onFailure(statusCode, headers, responseString, throwable);
		}
	}
	private class JsonHandlerDept extends MJsonHttpHandler {
		
		protected JsonHandlerDept(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			// TODO Auto-generated constructor stub
		}
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			try {
				JSONArray array = (JSONArray) response.get("dept");
				for (int i = 0; i < array.length(); i++) {
					spn.add(new Spinner_Entity(array.getJSONObject(i).getString("oname"),array.getJSONObject(i).getString("oid")));
				}
				spinnerAdapter1.setList(spn);
				spinnerAdapter1.notifyDataSetChanged();
				dialog.dismissWaitingDialog();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if (arg2 > 0) {
			if (list.size() > 0) {
				String event_no = list.get(arg2 - 1).getEvent_no();
				intent.setClass(this, IT_Management_Sq.class);
				intent.putExtra("event_no", event_no);
				startActivity(intent);

			}
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				list.clear();
				load = 1;
				httplist(sp.getString(SPConstant.MY_TOKEN, ""), 0);
			}
		}, 10);
	}

	private void onLoad() {
		String RefreshTime = DateUtil.convertLongToDate(System
				.currentTimeMillis());
		XListView1.stopRefresh();
		XListView1.stopLoadMore();
		XListView1.setRefreshTime(RefreshTime);
		sp.edit().putString(SPConstant.REFRESHTIME, RefreshTime).commit();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				httplist(sp.getString(SPConstant.MY_TOKEN, ""), load);
				load++;
			}
		}, 10);
	}
}
