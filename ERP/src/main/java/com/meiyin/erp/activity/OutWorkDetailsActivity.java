package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.GreenDao.ClientAddress_EntityDao;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.OutWorkDetails_Adapter;
import com.meiyin.erp.adapter.onAdapterItemClick;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.MyApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.ClientAddress_Entity;
import com.meiyin.erp.entity.OutWorkDetails_Entity;
import com.meiyin.erp.json.JsonHttpHandles;
import com.meiyin.erp.util.AsyncHttpclient_Util;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.LogUtil;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * @param外勤列表详情
 * @Time 2016-4-18
 */
public class OutWorkDetailsActivity extends Activity implements onAdapterItemClick {
	private Context content;
	private GeoCoder mSearch;
//	 定位相关
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private AlertDialog dialog;
	private TextView heads;
	private LinearLayout submit_dialog;
	private SharedPreferences sp;
	private ArrayList<OutWorkDetails_Entity> list;
	private OutWorkDetails_Adapter adapter;
	private ListView owd_listview;
	private Dialog_Http_Util dhu;
	public static Boolean Refresh = false;
	private String location = "";
	private String longitude = "";
	private String latitude = "";
	private ImageView map_img;
	LatLng lat;
	private ClientAddress_EntityDao ClientAddressDao;
	private TextView go_isbrush_text,out_isbrush_text,go_brush_buttn,out_brush_buttn;
	private String id ,id1;
	private TextView go_brushtime_text,go_brushaddress_text,out_brushtime_text,out_brushaddress_text;
	private List<ClientAddress_Entity> li;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		LocationManager im =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.outworkdetails_main);
		content = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		if(!im.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			ToastManager.getInstance(content).showToast(
					"请开启手机GPS定位系统！");
			Intent intent=new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 0);
		}
		initHeader();
		ClientAddressDao = MyApplication.getInstance().getDaoSession().getClientAddress_EntityDao();
		// 查询本地是否存储地址
		li = ClientAddressDao.queryBuilder().where(ClientAddress_EntityDao.Properties.Id.eq(getIntent().getStringExtra("id"))).list();


		dhu = new Dialog_Http_Util();
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(new Getcode());
		dialog = new AlertDialog.Builder(this).create();
		map_img = (ImageView) findViewById(R.id.map_img);
		map_img.setVisibility(ViewGroup.GONE);
		owd_listview = (ListView) findViewById(R.id.owd_listview);
		adapter = new OutWorkDetails_Adapter(content);
		list = new ArrayList<OutWorkDetails_Entity>();
		adapter.setList(list);
		owd_listview.setAdapter(adapter);
		adapter.setOnRightItemClickListener(this);
		adapter.setOnRightItemClickListener(new onAdapterItemClick() {
			@Override
			public void onRightItemClick(View v, final int position) {
				// TODO Auto-generated method stub
				dhu.showWaitingDialog(OutWorkDetailsActivity.this, "正在定位..", true);
				getLocation();// 定位
				final String OUTTYPE = getIntent().getStringExtra("outtype");
				lat = null;
				if (li.size() < 1) {
					if (OUTTYPE.equals("客户拜访")) {
						String shi=list.get(position).getShi();
						if(shi.equals("")){
							shi=list.get(position).getQu();
							}
						mSearch.geocode(new GeoCodeOption().city(shi).address(list.get(position)
														.getTargetaddress()));
					} else if (OUTTYPE.equals("外出办事")) {
						String nameString = "长沙";
						String address = list.get(position).getTargetaddress();
						if (address.contains("市")) {
							int s = address.indexOf("市");
							nameString = address.substring(s - 2, s);
						}
						LogUtil.e("lyt", nameString);
						mSearch.geocode(new GeoCodeOption().city(nameString)
								.address(list.get(position).getTargetaddress()));
					}
				} else {
					String shi=li.get(position).getShi();
					if(shi.equals("")){
						shi=li.get(position).getQu();
						}
					mSearch.geocode(new GeoCodeOption().city(shi).address(
							list.get(position).getTargetaddress()));

				}
				submit_dialog = (LinearLayout) LayoutInflater.from(content)
						.inflate(R.layout.submit_dialog, null);
				heads = (TextView) submit_dialog.findViewById(R.id.heads);
				Button button_t = (Button) submit_dialog
						.findViewById(R.id.button_t);
				Button button_f = (Button) submit_dialog
						.findViewById(R.id.button_f);
				button_t.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (location.equals("") || longitude.equals("")
								|| latitude.equals("")) {
							ToastManager.getInstance(content).showToast(
									"刷卡地址不能为空！");
							return;
						}
						if (lat == null) {
							ToastManager.getInstance(content).showToast(
									"地址不正确,定位失败！");
							dialog.dismiss();
							if(OUTTYPE.equals("客户拜访")){
							return;
							}
						}
						boolean scope = SpatialRelationUtil
								.isCircleContainsPoint(lat, 500,
										new LatLng(Double.valueOf(latitude),
												Double.valueOf(longitude)));
						if (OUTTYPE.equals("客户拜访")) {
							if (scope) {
								dhu.showWaitingDialog(
										OutWorkDetailsActivity.this, "正在加载..",
										false);
								String key = sp.getString(SPConstant.MY_TOKEN,
										"");
								RequestParams params = new RequestParams();
								params.setContentEncoding("utf_8");
								params.put("key", key);// key
								params.put("id", list.get(position).getId());// 主键id
								params.put("isbrush", 1);// 是否刷卡
								params.put("brushaddress", location);// 刷卡地址
								params.put("longitude", longitude);// 经度
								params.put("latitude", latitude);// 纬度
								AsyncHttpclient_Util
										.post(APIURL.UPDATEBRUSH, content,
												params, new BrushJsonHandler());
								dialog.dismiss();
							} else {
								heads.setText("不在指定位置范围内,是否继续刷卡？");
								dialog.show();
								dialog.getWindow().setContentView(submit_dialog);
//								ToastManager.getInstance(content).showToast(
//										"不在指定位置范围内,是否继续刷卡？");
								Button button_t = (Button) submit_dialog
										.findViewById(R.id.button_t);
								Button button_f = (Button) submit_dialog
										.findViewById(R.id.button_f);
								button_t.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View arg0) {
										// TODO Auto-generated method stub
										dhu.showWaitingDialog(
												OutWorkDetailsActivity.this, "正在加载..",
												false);
										String key = sp.getString(SPConstant.MY_TOKEN,
												"");
										RequestParams params = new RequestParams();
										params.setContentEncoding("utf_8");
										params.put("key", key);// key
										params.put("id", list.get(position).getId());// 主键id
										params.put("isbrush", 2);// 是否刷卡
										params.put("brushaddress", location);// 刷卡地址
										params.put("longitude", longitude);// 经度
										params.put("latitude", latitude);// 纬度
										AsyncHttpclient_Util
												.post(APIURL.UPDATEBRUSH, content,
														params, new BrushJsonHandler());
										dialog.dismiss();
									}
								});
								button_f.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View arg0) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
							}

						} else if (OUTTYPE.equals("外出办事")) {
							dhu.showWaitingDialog(OutWorkDetailsActivity.this,
									"正在加载..", false);
							String key = sp.getString(SPConstant.MY_TOKEN, "");
							RequestParams params = new RequestParams();
							params.setContentEncoding("utf_8");
							params.put("key", key);// key
							params.put("id", list.get(position).getId());// 主键id
							params.put("isbrush", 1);// 是否刷卡
							params.put("brushaddress", location);// 刷卡地址
							params.put("longitude", longitude);// 经度
							params.put("latitude", latitude);// 纬度
							AsyncHttpclient_Util.post(APIURL.UPDATEBRUSH,
									content, params, new BrushJsonHandler());
							dialog.dismiss();
						}

					}
				});
				button_f.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				if(!location.equals("")){

				}else{
					ToastManager.getInstance(content).showToast(
							"定位失败，请重新刷卡定位！");
				}
			}
		});
		map_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bl = new Bundle();
				bl.putSerializable("list", (Serializable) list);
				startActivity(new Intent().setClass(
						OutWorkDetailsActivity.this, Map_Activity.class)
						.putExtras(bl));
			}
		});
		httpoutwork(li.size());
		findView();
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
		headtitletext.setText("外勤刷卡");
	}
	private void findView() {
		// TODO Auto-generated method stub
		go_brush_buttn=(TextView) findViewById(R.id.go_brush_buttn);
		go_brush_buttn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Brush(id);
			}
		});
		go_brushtime_text=(TextView) findViewById(R.id.go_brushtime_text);
		go_isbrush_text=(TextView) findViewById(R.id.go_isbrush_text);
		go_brushaddress_text=(TextView) findViewById(R.id.go_brushaddress_text);


		out_brush_buttn=(TextView) findViewById(R.id.out_brush_buttn);
		out_brush_buttn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Brush(id1);
			}
		});
		out_brushtime_text=(TextView) findViewById(R.id.out_brushtime_text);
		out_isbrush_text=(TextView) findViewById(R.id.out_isbrush_text);
		out_brushaddress_text=(TextView) findViewById(R.id.out_brushaddress_text);
	}

	public void httpoutwork(int li) {
		dhu.showWaitingDialog(OutWorkDetailsActivity.this, "正在加载", false);
		RequestParams params = new RequestParams();
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		params.put("key", key);// key
		if (li < 1) {
			params.put("isneedclientInfo", 1);// 地址
		}
		params.put("_id", getIntent().getStringExtra("id"));//主键id
		AsyncHttpclient_Util.post(APIURL.OUTWORK_DETAIL, content, params,
				new outworkdetailsJsonHandler(li));
	}

	private class outworkdetailsJsonHandler extends JsonHttpHandles {
		private int dex;

		private outworkdetailsJsonHandler(int li) {
			this.dex = li;
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			LogUtil.e("lyt", response.toString());
			if(!response.isNull("loginStatus")){
				ToastManager.getInstance(content).showToast("登录密码已失效，请重新登录！");
				return;
			}
			try {
				JSONArray brush = response.getJSONArray("brushList");
				ArrayList<OutWorkDetails_Entity> lists = new Gson().fromJson(
						brush.toString(),
						new TypeToken<List<OutWorkDetails_Entity>>() {
						}.getType());
				    list.clear();
				for (OutWorkDetails_Entity outWork_Entity : lists) {
					list.add(outWork_Entity);
				}
				if (response.getString("outtype").equals("客户拜访")) {
					if (dex < 1) {
						for (OutWorkDetails_Entity outWork_Entity : lists) {
							ClientAddressDao.insert(new ClientAddress_Entity(
									null, response.getString("_id"),
									outWork_Entity.getSheng(), outWork_Entity
											.getShi(), outWork_Entity.getQu()));

						}
					}
				}
				adapter.setList(list);
				owd_listview.setAdapter(adapter);
				if(lists.size()>0){
				map_img.setVisibility(ViewGroup.VISIBLE);
				}
				JSONArray kaoqingbrushList = response.getJSONArray("kaoqingbrushList");
				for (int i = 0; i < kaoqingbrushList.length(); i++) {
				JSONObject kaoqing=(JSONObject) kaoqingbrushList.get(i);
				String brush_type = kaoqing.getString("brush_type");
				if(brush_type.equals("3")){
					id = kaoqing.getString("id");
					if(kaoqing.getString("isbrush").equals("0")){
						go_isbrush_text.setText("未刷卡");
						go_isbrush_text.setTextColor(getResources().getColor(R.color.dark_red));
						go_brush_buttn.setVisibility(ViewGroup.VISIBLE);
						}else if(kaoqing.getString("isbrush").equals("1")){
						go_isbrush_text.setText("已刷卡");
						go_brush_buttn.setVisibility(ViewGroup.GONE);
						go_brushaddress_text.setText(kaoqing.getString("brushaddress"));
						go_brushtime_text.setText(kaoqing.getString("brushtime"));
						go_isbrush_text.setTextColor(getResources().getColor(R.color.text_green));
						}
				}else if(brush_type.equals("4")){
					id1 = kaoqing.getString("id");
					if(kaoqing.getString("isbrush").equals("0")){
						out_isbrush_text.setText("未刷卡");
						out_isbrush_text.setTextColor(getResources().getColor(R.color.dark_red));
						out_brush_buttn.setVisibility(ViewGroup.VISIBLE);
						}else if(kaoqing.getString("isbrush").equals("1")){
						out_isbrush_text.setText("已刷卡");
						out_brush_buttn.setVisibility(ViewGroup.GONE);
						out_brushaddress_text.setText(kaoqing.getString("brushaddress"));
						out_brushtime_text.setText(kaoqing.getString("brushtime"));
						out_isbrush_text.setTextColor(getResources().getColor(R.color.text_green));
						}
				}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dhu.dismissWaitingDialog();

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			dhu.dismissWaitingDialog();
			if (statusCode == 0
					&& throwable.toString().contains("HttpHostConnect")) {
				ToastManager.getInstance(content).showToast("网络连接失败,请检查网络设置！");
			} else if (statusCode == 200) {
				ToastManager.getInstance(content).showToast("账号异常请联系技术支持部！");
			} else if (statusCode == 0
					&& throwable.toString().contains("Timeout")) {
				ToastManager.getInstance(content).showToast("服务连接超时！");
			}
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			dhu.dismissWaitingDialog();
			LogUtil.e("lyt", statusCode + throwable.toString());
			ToastManager.getInstance(content).showToast("网络连接失败！");
			super.onFailure(statusCode, headers, responseString, throwable);
		}
	}

	private class BrushJsonHandler extends JsonHttpHandles {
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				LogUtil.e("lyt", response.toString());
				int state = response.getInt("state");
				if (state == 0) {
					ToastManager.getInstance(content).showToast("刷卡失败！");
					dhu.dismissWaitingDialog();
				} else if (state == 1) {
					ToastManager.getInstance(content).showToast("刷卡成功！");
					httpoutwork(1);
					Refresh = true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			dhu.dismissWaitingDialog();
			if (statusCode == 0
					&& throwable.toString().contains("HttpHostConnect")) {
				ToastManager.getInstance(content).showToast("网络连接失败,请检查网络设置！");
			} else if (statusCode == 200) {
				ToastManager.getInstance(content).showToast("账号异常请联系技术支持部！");
			} else if (statusCode == 0
					&& throwable.toString().contains("Timeout")) {
				ToastManager.getInstance(content).showToast("服务连接超时！");
			}
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			dhu.dismissWaitingDialog();
			LogUtil.e("lyt", statusCode + throwable.toString());
			ToastManager.getInstance(content).showToast("网络连接失败！");
			super.onFailure(statusCode, headers, responseString, throwable);
		}
	}

	private void getLocation() {
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setIsNeedAddress(true);
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
//		option.setScanSpan(5000); // 定位时间间隔
		mLocClient.setLocOption(option);

		mLocClient.start();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			if (location == null) {
				ToastManager.getInstance(content).showToast("定位失败！！！");
				dhu.dismissWaitingDialog();
				return;
			}
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			sp.edit().putString(SPConstant.LATITUDE, String.valueOf(latitude))
					.commit();
			sp.edit()
					.putString(SPConstant.LONGITUDE, String.valueOf(longitude))
					.commit();
			sp.edit().putString(SPConstant.RADIUS, "" + 800).commit();
			OutWorkDetailsActivity.this.longitude = String.valueOf(longitude);
			OutWorkDetailsActivity.this.latitude = String.valueOf(latitude);
			String addr = location.getAddrStr();
			if (addr != null) {
				OutWorkDetailsActivity.this.location=addr;
				dhu.dismissWaitingDialog();
				sp.edit().putString(SPConstant.LOCATION, addr)
						.commit();
				heads.setText((Html.fromHtml("确定在</font><font color=\"#058b05\">"
						+ addr + "</font>刷卡吗？", imgGetter, null)));
				dialog.show();
				dialog.getWindow().setContentView(submit_dialog);
				LogUtil.e("xx", addr);
			} else {
			if (longitude > 0 && latitude > 0) {
				LogUtil.e("lyt", "经度："+longitude+"纬度："+latitude+"地理编码");
				LatLng ptCenter = new LatLng(latitude, longitude);
				// 反Geo搜索
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(ptCenter));
			} else {
				ToastManager.getInstance(content).showToast("定位失败！！！");
				dhu.dismissWaitingDialog();
			}
			}
			// 停止定位
			mLocClient.stop();
		}

		@Override
		public void onConnectHotSpotMessage(String s, int i) {

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	public class Getcode implements OnGetGeoCoderResultListener {

		@Override
		public void onGetGeoCodeResult(GeoCodeResult arg0) {
			// TODO Auto-generated method stub
			if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(OutWorkDetailsActivity.this,
						"抱歉，未能找到结果！请填写正确地址！！！", Toast.LENGTH_LONG).show();
				// 没有检索到结果
				dhu.dismissWaitingDialog();
			} else {
				// 获取地理编码结果
				lat = arg0.getLocation();

			}

		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			// TODO Auto-generated method stub
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(OutWorkDetailsActivity.this, "抱歉，未能找到结果！",
						Toast.LENGTH_LONG).show();
				dhu.dismissWaitingDialog();
				return;
			}
			dhu.dismissWaitingDialog();
			location = result.getAddress();
			sp.edit().putString(SPConstant.LOCATION, result.getAddress())
					.commit();
			heads.setText((Html.fromHtml("确定在</font><font color=\"#058b05\">"
					+ location + "</font>刷卡吗？", imgGetter, null)));
			dialog.show();
			dialog.getWindow().setContentView(submit_dialog);
			// Toast.makeText(OutWorkDetailsActivity.this, result.getAddress(),
			// Toast.LENGTH_LONG).show();

			String province = result.getAddressDetail().province;
			String city = result.getAddressDetail().city;
			if (province != null && city != null) {
				// Toast.makeText(OutWorkDetailsActivity.this,
				// city+province+result.getAddressDetail().street+result.getAddressDetail().streetNumber+result.getAddressDetail().district,
				// Toast.LENGTH_LONG).show();

			}
		}
	}
	//上下班打卡。。。
	private void Brush(final String id){
		LogUtil.e("lyt", id);
		submit_dialog = (LinearLayout) LayoutInflater.from(content)
				.inflate(R.layout.submit_dialog, null);
		heads = (TextView) submit_dialog.findViewById(R.id.heads);
		Button button_t = (Button) submit_dialog
				.findViewById(R.id.button_t);
		Button button_f = (Button) submit_dialog
				.findViewById(R.id.button_f);
		button_t.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (location.equals("") || longitude.equals("")
						|| latitude.equals("")) {
					ToastManager.getInstance(content).showToast(
							"刷卡地址不能为空！");
					return;
				}
					dhu.showWaitingDialog(OutWorkDetailsActivity.this,
							"正在加载..", false);
					String key = sp.getString(SPConstant.MY_TOKEN, "");
					RequestParams params = new RequestParams();
					params.setContentEncoding("utf_8");
					params.put("key", key);// key
					params.put("id", id);// 主键id
					params.put("isbrush", 1);// 是否刷卡
					params.put("brushaddress", location);// 刷卡地址
					params.put("longitude", longitude);// 经度
					params.put("latitude", latitude);// 纬度
					AsyncHttpclient_Util.post(APIURL.UPDATEBRUSH,
							content, params, new BrushJsonHandler());
					dialog.dismiss();
				}
		});
		button_f.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
//		getLocation();// 定位
	}
	ImageGetter imgGetter = new ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			// Log.i("zkr", source);
			int i = 0;
			try {
				Field field = R.drawable.class.getField(source);
				i = field.getInt(new R.drawable());
				drawable = content.getResources().getDrawable(i);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth() - 10,
						drawable.getIntrinsicHeight() - 10);
				// Log.d("zkr", i + "");
			} catch (Exception e) {
				// LogUtil.e("zkr", e.toString());
			}

			return drawable;
		}
	};

	@Override
	public void onRightItemClick(View v, final int position) {
		// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.brush_text:
			dhu.showWaitingDialog(OutWorkDetailsActivity.this, "正在定位..", true);
			getLocation();// 定位
			final String OUTTYPE = getIntent().getStringExtra("outtype");
			lat = null;
			if (li.size() < 1) {
				if (OUTTYPE.equals("客户拜访")) {
					String shi=list.get(position).getShi();
					if(shi.equals("")){
						shi=list.get(position).getQu();
						}
					mSearch.geocode(new GeoCodeOption().city(shi).address(list.get(position)
													.getTargetaddress()));
				} else if (OUTTYPE.equals("外出办事")) {
					String nameString = "长沙";
					String address = list.get(position).getTargetaddress();
					if (address.contains("市")) {
						int s = address.indexOf("市");
						nameString = address.substring(s - 2, s);
					}
					LogUtil.e("lyt", nameString);
					mSearch.geocode(new GeoCodeOption().city(nameString)
							.address(list.get(position).getTargetaddress()));
				}
			} else {
				String shi=li.get(position).getShi();
				if(shi.equals("")){
					shi=li.get(position).getQu();
					}
				mSearch.geocode(new GeoCodeOption().city(shi).address(
						list.get(position).getTargetaddress()));

			}
			submit_dialog = (LinearLayout) LayoutInflater.from(content)
					.inflate(R.layout.submit_dialog, null);
			heads = (TextView) submit_dialog.findViewById(R.id.heads);
			Button button_t = (Button) submit_dialog
					.findViewById(R.id.button_t);
			Button button_f = (Button) submit_dialog
					.findViewById(R.id.button_f);
			button_t.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (location.equals("") || longitude.equals("")
							|| latitude.equals("")) {
						ToastManager.getInstance(content).showToast(
								"刷卡地址不能为空！");
						return;
					}
					if (lat == null) {
						ToastManager.getInstance(content).showToast(
								"地址不正确,定位失败！");
						dialog.dismiss();
						if(OUTTYPE.equals("客户拜访")){
						return;
						}
					}
					boolean scope = SpatialRelationUtil
							.isCircleContainsPoint(lat, 800,
									new LatLng(Double.valueOf(latitude),
											Double.valueOf(longitude)));
					if (OUTTYPE.equals("客户拜访")) {
						if (scope) {
							dhu.showWaitingDialog(
									OutWorkDetailsActivity.this, "正在加载..",
									false);
							String key = sp.getString(SPConstant.MY_TOKEN,
									"");
							RequestParams params = new RequestParams();
							params.setContentEncoding("utf_8");
							params.put("key", key);// key
							params.put("id", list.get(position).getId());// 主键id
							params.put("isbrush", 1);// 是否刷卡
							params.put("brushaddress", location);// 刷卡地址
							params.put("longitude", longitude);// 经度
							params.put("latitude", latitude);// 纬度
							AsyncHttpclient_Util
									.post(APIURL.UPDATEBRUSH, content,
											params, new BrushJsonHandler());
							dialog.dismiss();
						} else {
							heads.setText("不在指定位置范围内,是否继续刷卡？");
							dialog.show();
							dialog.getWindow().setContentView(submit_dialog);
//							ToastManager.getInstance(content).showToast(
//									"不在指定位置范围内,是否继续刷卡？");
							Button button_t = (Button) submit_dialog
									.findViewById(R.id.button_t);
							Button button_f = (Button) submit_dialog
									.findViewById(R.id.button_f);
							button_t.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									dhu.showWaitingDialog(
											OutWorkDetailsActivity.this, "正在加载..",
											false);
									String key = sp.getString(SPConstant.MY_TOKEN,
											"");
									RequestParams params = new RequestParams();
									params.setContentEncoding("utf_8");
									params.put("key", key);// key
									params.put("id", list.get(position).getId());// 主键id
									params.put("isbrush", 2);// 是否刷卡
									params.put("brushaddress", location);// 刷卡地址
									params.put("longitude", longitude);// 经度
									params.put("latitude", latitude);// 纬度
									AsyncHttpclient_Util
											.post(APIURL.UPDATEBRUSH, content,
													params, new BrushJsonHandler());
									dialog.dismiss();
								}
							});
							button_f.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
						}

					} else if (OUTTYPE.equals("外出办事")) {
						dhu.showWaitingDialog(OutWorkDetailsActivity.this,
								"正在加载..", false);
						String key = sp.getString(SPConstant.MY_TOKEN, "");
						RequestParams params = new RequestParams();
						params.setContentEncoding("utf_8");
						params.put("key", key);// key
						params.put("id", list.get(position).getId());// 主键id
						params.put("isbrush", 1);// 是否刷卡
						params.put("brushaddress", location);// 刷卡地址
						params.put("longitude", longitude);// 经度
						params.put("latitude", latitude);// 纬度
						AsyncHttpclient_Util.post(APIURL.UPDATEBRUSH,
								content, params, new BrushJsonHandler());
						dialog.dismiss();
					}

				}
			});
			button_f.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			if(!location.equals("")){

			}else{
				ToastManager.getInstance(content).showToast(
						"定位失败，请重新刷卡定位！");
			}

			break;

		default:
			break;
		}
	}


}
