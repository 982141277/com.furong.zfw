package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.GooutBrush_Adapter;
import com.meiyin.erp.adapter.Spinner_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Gooutbrush_Entity;
import com.meiyin.erp.entity.Spinner_Entity;
import com.meiyin.erp.ui.XListView;
import com.meiyin.erp.util.DateUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.LogUtil;
import com.meiyin.erp.util.ToastUtil;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GoOutBrushActivity extends Activity {

	protected XListView gout_brush_listview;
	protected SharedPreferences sp;
	protected Context content;
	protected ArrayList<Gooutbrush_Entity> listbrush;
	protected GooutBrush_Adapter adapter;
//	private GeoCoder mSearch;
	private String Address;
	private String longitude;
	private String latitude;
	private String addr="";
	private String type_id="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		SDKInitializer.initialize(getApplicationContext());
		LocationManager im = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		setContentView(R.layout.goout_brush_main);
		content = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);

		initHeader();
		initView();
		if (!im.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			ToastManager.getInstance(content).showToast("请开启手机GPS定位系统！");
			Intent intent = new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 0);
		}
//		getLocation();
//		// 初始化搜索模块，注册事件监听
//		mSearch = GeoCoder.newInstance();
//		mSearch.setOnGetGeoCodeResultListener(new Getcode());
	}
//	private void getLocation() {
//		// 定位初始化
//		LocationClient mLocClient = new LocationClient(this);
//		mLocClient.registerLocationListener(new BDLocationListener() {
//
//			@Override
//			public void onReceiveLocation(BDLocation location) {
//				// TODO Auto-generated method stub
//				if (location == null) {
//					ToastManager.getInstance(content).showToast("定位失败！！！");
//					return;
//				}
//				double dlongitude = location.getLongitude();
//				double dlatitude = location.getLatitude();
//
//				longitude = String.valueOf(dlongitude);
//				latitude = String.valueOf(dlatitude);
//				addr = location.getAddrStr();
//				LatLng ptCenter = new LatLng(dlatitude, dlongitude);
//					// 反Geo搜索
//					mSearch.reverseGeoCode(new ReverseGeoCodeOption()
//							.location(ptCenter));
//
//			}
//		} );
//
//		LocationClientOption option = new LocationClientOption();
//		option.setIsNeedAddress(true);
//		option.setOpenGps(true);// 打开gps
//		option.setCoorType("bd09ll"); // 设置坐标类型
//		option.setScanSpan(10000); // 定位时间间隔
//		mLocClient.setLocOption(option);
//		mLocClient.start();
//	}
//	public class Getcode implements OnGetGeoCoderResultListener {
//
//		@Override
//		public void onGetGeoCodeResult(GeoCodeResult arg0) {
//			// TODO Auto-generated method stub
//
//		}
//		@Override
//		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//			// TODO Auto-generated method stub
//			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//				Toast.makeText(GoOutBrushActivity.this, "抱歉，未能找到结果！",
//						Toast.LENGTH_LONG).show();
//				return;
//			}
//			Address = result.getAddress();
//			Toast.makeText(GoOutBrushActivity.this, result.getAddress(),
//					Toast.LENGTH_LONG).show();
//		}
//	}
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
		headtitletext.setText("打卡记录");
	}

	/*
	 * 初始化View
	 */
	private void initView() {
		// TODO Auto-generated method stub
		final Button goout_brush_button = (Button) findViewById(R.id.goout_brush_button);
		if(sp.getString(SPConstant.MY_GOOUTBRUSH, "").equals("2")){
		Spinner people_list_sp = (Spinner) findViewById(R.id.people_list_sp);
		people_list_sp.setVisibility(ViewGroup.VISIBLE);
		final ArrayList<Spinner_Entity> spn = new ArrayList<Spinner_Entity>();
		spn.add(new Spinner_Entity("请选择项目人员",""));
		String array=sp.getString(SPConstant.MY_GOOUTBRUSHLIST,"");
		try {
			JSONArray arrays = new JSONArray(array);
			for (int i = 0; i < arrays.length(); i++) {
				spn.add(new Spinner_Entity(arrays.getJSONObject(i).getString("oname"),arrays.getJSONObject(i).getString("user_code")));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Spinner_Adapter spinnerAdapter = new Spinner_Adapter(content);
		spinnerAdapter.setList(spn);
		people_list_sp.setAdapter(spinnerAdapter);
		people_list_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				type_id = spn.get(arg2).getType_id();
				if(!type_id.equals("")){
					goout_brush_button.setVisibility(ViewGroup.GONE);
				}else{
					goout_brush_button.setVisibility(ViewGroup.VISIBLE);
				}
				brushlist(0,type_id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		}
		final Handler mHandler = new Handler();
		final Builder builder = new AlertDialog.Builder(this);// 初始化审批dialog
		listbrush = new ArrayList<Gooutbrush_Entity>();
		gout_brush_listview = (XListView) findViewById(R.id.gout_brush_listview);
		gout_brush_listview.setPullLoadEnable(true);
		gout_brush_listview.setXListViewListener(new XListView.IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						brushlist(0,type_id);
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
						if (listbrush.size() < 20) {
							load = 0;
						} else {
							load = listbrush.size();
						}
						brushlist(load,type_id);
					}
				}, 10);
			}
		});
		gout_brush_listview.setRefreshTime(sp.getString(SPConstant.REFRESHTIME,
				""));
		adapter = new GooutBrush_Adapter(content);
		adapter.setList(listbrush);
		gout_brush_listview.setAdapter(adapter);
		brushlist(0,type_id);

		goout_brush_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(null==Address){
					Address=addr;
				}
				LinearLayout dialog_approval = (LinearLayout) LayoutInflater
						.from(content).inflate(R.layout.dialog_goout_detail, null);
				TextView dialog_type_title = (TextView) dialog_approval
						.findViewById(R.id.dialog_type_title);// 完成按钮
				Button dialog_goout_complete_button = (Button) dialog_approval
						.findViewById(R.id.dialog_goout_complete_button);// 完成按钮
				final EditText complete_goout_app_view = (EditText) dialog_approval
						.findViewById(R.id.complete_goout_app_view);// 出差详情
				dialog_type_title.setText("地址："+Address);
				dialog_goout_complete_button.setText("打卡");
				complete_goout_app_view.setHint("请给出打卡详情！");
				builder.setView(dialog_approval);
				final AlertDialog dialog_app = builder.create();// 初始化审批dialog
				dialog_app.show();
				dialog_app.getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
				dialog_app.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				dialog_goout_complete_button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						final String describes = complete_goout_app_view.getText().toString();
						if (describes.trim().equals("")) {
							ToastUtil.showToast(content, "请先填写详情！");
							return;
						}
						if(null==Address||Address.equals("")){
							ToastUtil.showToast(content, "打卡地址不能为空！");
							return;
						}
						 Dialog_Http_Util dialog_util = new Dialog_Http_Util();
						 AsyncHttpClientUtil async = new AsyncHttpClientUtil();
						 String key = sp.getString(SPConstant.MY_TOKEN, "");
						 RequestParams param = new RequestParams();
						 param.setContentEncoding("utf_8");
						 param.put("key", key);// key
						 param.put("brushaddress", Address);// 刷卡地址
						 param.put("longitude", longitude);// 经度
						 param.put("latitude", latitude);// 纬度
						 param.put("describes", describes);// 描述
						 param.put("brushtype", "0");// 打卡类型

						 Dialog progressDialog =
						 dialog_util.showWaitingDialog(GoOutBrushActivity.this,
						 "正在刷卡", false);
						 async.post(APIURL.GOOUT_BRUSH, content, param, new
						 JsonHandler(
						 GoOutBrushActivity.this, progressDialog));
						 dialog_app.dismiss();
					}
				});


			}
		});
	}

	public void brushlist(int pager,String userid) {
		Dialog_Http_Util dialog_util = new Dialog_Http_Util();
		AsyncHttpClientUtil async = new AsyncHttpClientUtil();
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		RequestParams param = new RequestParams();
		param.setContentEncoding("utf_8");
		param.put("key", key);// key
		param.put("userid", userid);// userid
		param.put("pager", pager);// pager
		Dialog progressDialog = dialog_util.showWaitingDialog(
				GoOutBrushActivity.this, "正在刷新", false);
		async.post(APIURL.GOOUT_BRUSH_LIST, content, param,
				new JsonHandlerlist(GoOutBrushActivity.this, progressDialog,pager));
	}

	public void stopload() {
		String RefreshTime = DateUtil.convertLongToDate(System
				.currentTimeMillis());
		gout_brush_listview.stopRefresh();
		gout_brush_listview.stopLoadMore();
		gout_brush_listview.setRefreshTime(RefreshTime);
		sp.edit().putString(SPConstant.REFRESHTIME, RefreshTime).commit();
	}

	// 项目外勤列表
	private class JsonHandlerlist extends MJsonHttpHandler {
		private Dialog progressDialog;
		private int pager;

		protected JsonHandlerlist(Context context, Dialog progressDialog,int pager) {
			super(context, progressDialog);
			this.pager=pager;
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
				if (pager ==0) {
					listbrush.clear();
				}
				stopload();
				String message = response.getString("message");
				if (message.equals("success")) {
					JSONArray array = response.getJSONArray("result");
					ArrayList<Gooutbrush_Entity> list = new Gson().fromJson(
							array.toString(),
							new TypeToken<List<Gooutbrush_Entity>>() {
							}.getType());

					for (Gooutbrush_Entity item : list) {
						String brushtime = item.getBrushtime();
						boolean isenglish = brushtime.contains("M");
						if(isenglish){
							String brushtimes=DateUtil.getEnglishTime(brushtime);
							item.setBrushtime(brushtimes);
						}
						listbrush.add(item);
					}
					adapter.setList(listbrush);
					adapter.notifyDataSetChanged();
					ToastManager.getInstance(context)
							.showToastcenter("刷新成功！！！");
				} else if(message.equals("nodata")){
					adapter.setList(listbrush);
					adapter.notifyDataSetChanged();
					ToastManager.getInstance(context)
							.showToastcenter("无打卡记录！！！");
				} else{
					ToastManager.getInstance(context)
					.showToastcenter("加载失败！！！");
		}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	// 项目外勤打卡
	private class JsonHandler extends MJsonHttpHandler {
		private Dialog progressDialog;

		protected JsonHandler(Context context, Dialog progressDialog) {
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
							.showToastcenter("刷卡成功！！！");
					brushlist(0,type_id);
				} else {
					ToastManager.getInstance(context)
							.showToastcenter("刷卡失败！！！");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
