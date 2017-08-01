package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.GreenDao.MeiyinnewsDao;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.AddMenu_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.MyApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.AddMenuEntity;
import com.meiyin.erp.util.AndroidUtil;
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

/**
 * 办公管理系统首页
 */
public class Home extends Activity implements OnClickListener {

	private Context context;
	private Dialog dialog;
	private ServiceReceivers Receiver;
	private IntentFilter Filters;
	private TextView hongdians;

	private RelativeLayout relativelayoutss;
	private Intent intent;
	private ArrayList<AddMenuEntity> mlist;
	private SharedPreferences sp;
	private LinearLayout new_dialog;
	private AddMenu_Adapter adapter;
	private String code;
	private MeiyinnewsDao meiyin;
	private Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);
		context = getApplicationContext();
		activity=this;
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		/*
		 * 初始化
		 */
		initHeader();
		initData();
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
		headtitletext.setText("办公管理系统");
	}
	/*
	 * 初始化数据
	 */
	private void initData() {
		// TODO Auto-generated method stub
		meiyin = MyApplication.getInstance().getDaoSession().getMeiyinnewsDao();
		intent = new Intent();
		registerReceiver();
	}

	/*
	 * 初始化UI
	 */
	private void initView() {
		// TODO Auto-generated method stub
		hongdians = (TextView) findViewById(R.id.hongdians);
		relativelayoutss = (RelativeLayout) findViewById(R.id.relativelayoutss);
		ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);
		ImageView waiqin_img = (ImageView) findViewById(R.id.waiqin_img);// 外勤
		ImageView topic_home = (ImageView) findViewById(R.id.topic_home);// 公告
		topic_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				startActivity(new Intent().setClass(Home.this,
//						TopicActivity.class));
				ToastManager.getInstance(context).showToast("正在研发");
			}
		});
		waiqin_img.setOnClickListener(this);
		imageView3.setOnClickListener(this);
		show();
		dialog = new AlertDialog.Builder(this).create();

		LinearLayout memulist = (LinearLayout) findViewById(R.id.memulist);
		LinearLayout daishenpishixiang = (LinearLayout) findViewById(R.id.daishenpishixiang);
		LinearLayout yishenpishixiang = (LinearLayout) findViewById(R.id.yishenpishixiang);
		LinearLayout out_memu = (LinearLayout) findViewById(R.id.out_memu);

		memulist.setOnClickListener(new l());
		daishenpishixiang.setOnClickListener(new l());
		yishenpishixiang.setOnClickListener(new l());
		out_memu.setOnClickListener(new l());
	}

	public void dialogcd() {
		mlist = new ArrayList<AddMenuEntity>();
		Dialog_Http_Util dialog_util = new Dialog_Http_Util();
		RequestParams param = new RequestParams();
		param.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
		AsyncHttpClientUtil async = new AsyncHttpClientUtil();
		Dialog progressDialog = dialog_util.showWaitingDialog(this, "正在刷新",
				false);
		async.post(APIURL.CHECK.ADDMEMUAPI, context, param, new JsonHandler(
				this, progressDialog));

		new_dialog = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.new_dialog, null);
		GridView Add_Gridview = (GridView) new_dialog
				.findViewById(R.id.Add_Gridview);
		adapter = new AddMenu_Adapter(context);
		adapter.setList(mlist);
		Add_Gridview.setAdapter(adapter);
		Add_Gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Dialog progressDialog = new Dialog_Http_Util()
						.showWaitingDialog(Home.this, "正在刷新", false);
				dialog.dismiss();
				if (mlist.size() > 0) {
					code = mlist.get(arg2).getCode();
					if (null != code) {
						RequestParams param = new RequestParams();
						param.setContentEncoding("utf-8");
						param.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
						param.put("apply", code);// code
						new AsyncHttpClientUtil().post(
								APIURL.CHECK.ADDMEMUINTI, context, param,
								new JsonHandlerInit(Home.this, progressDialog));
					} else {
						progressDialog.dismiss();
					}
				} else {
					progressDialog.dismiss();
				}
			}
		});

	}

	class l implements OnClickListener {
		@Override
		public void onClick(View arg0) {
//			switch (arg0.getId()) {
//			case R.id.memulist:
//				intent.putExtra("name", "申请表单");
//				intent.setClass(context, Memulist.class);
//				startActivity(intent);
//				break;
//			case R.id.daishenpishixiang:
//				intent.putExtra("name", "待审批事项");
//				intent.setClass(context, Memulist.class);
//				startActivity(intent);
//
//				break;
//			case R.id.yishenpishixiang:
//				intent.putExtra("name", "已审批事项");
//				intent.setClass(context, Memulist.class);
//				startActivity(intent);
//				break;
//			case R.id.out_memu:
//				dialogcd();
//				break;
//			default:
//				break;
//			}
		}

	}

	/*
	 * 注册广播接受者
	 */
	public void registerReceiver() {
		Receiver = new ServiceReceivers();
		Filters = new IntentFilter();
		Filters.addAction("meiyin");
		registerReceiver(Receiver, Filters);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(Receiver);
		super.onDestroy();
	}

	@SuppressLint("InlinedApi")
	public void show() {
		SharedPreferences sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
		Long si = meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Type.eq("办公管理系统"),MeiyinnewsDao.Properties.Biaoshi.eq("0")).count();
		if (si > 0) {
			relativelayoutss.setVisibility(ViewGroup.VISIBLE);
			hongdians.setText("" + si);
		} else {
			relativelayoutss.setVisibility(ViewGroup.GONE);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
//		switch (arg0.getId()) {
//		case R.id.imageView3:
//			intent.setClass(context, TaskManagement.class);
//			startActivity(intent);
//			break;
//		case R.id.waiqin_img:
//			startActivity(new Intent().setClass(context, OutWorkActivity.class));
//			break;
//		default:
//			break;
//		}
	}

	// 新增申请单列表详情回调类
	private class JsonHandler extends MJsonHttpHandler {
		private Dialog progressDialog;

		protected JsonHandler(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			LogUtil.e("lyt", response.toString());
			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
				if (response.getString("message").equals("success")) {
					JSONArray array = response.getJSONArray("result");
					for (int i = 0; i < array.length(); i++) {
						JSONArray marray = array.getJSONArray(i);
						ArrayList<AddMenuEntity> list = new Gson().fromJson(
								marray.toString(),
								new TypeToken<List<AddMenuEntity>>() {
								}.getType());// 新增申请单列表
						for (AddMenuEntity addMenuEntity : list) {
							if(null!=addMenuEntity.getCode()){
							if(addMenuEntity.getCode().equals("2")||addMenuEntity.getCode().equals("5")||addMenuEntity.getCode().equals("9")||addMenuEntity.getCode().equals("10")||addMenuEntity.getCode().equals("13")){
								mlist.add(addMenuEntity);								
							}}

						}

					}
					dialog.show();
					dialog.getWindow().setContentView(new_dialog);
					adapter.setList(mlist);
					adapter.notifyDataSetChanged();
				}
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			progressDialog.dismiss();
		}

	}

	// 新增申请单初始化
	private class JsonHandlerInit extends MJsonHttpHandler {
		private Dialog progressDialog;

		protected JsonHandlerInit(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			LogUtil.e("lyt", response.toString());
			progressDialog.dismiss();
			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
			}catch (JSONException E){
					E.printStackTrace();
			}
//				String stateMent = response.getString("stateMent");
//				if (code.equals("2")) {// 外出申请单
//					Boolean sell = response.getBoolean("sell");
//					Intent intent = new Intent();
//					intent.setClass(context, Out_Memu.class);
//					intent.putExtra("stateMent", stateMent);
//					intent.putExtra("sell", sell);
//					startActivity(intent);
//				} else if (code.equals("5")) {// 请购申请单
//					Intent intent = new Intent();
//					intent.setClass(context, RequisitionActivity.class);
//					intent.putExtra("stateMent", stateMent);
//					startActivity(intent);
//				} else if(code.equals("6")){//员工转正申请单
//					ToastManager.getInstance(context).showToast("正在研发");
////					String enter_date = response.getString("enter_date");
////					String role_code = response.getString("role_code");
////					Intent intent = new Intent();
////					intent.setClass(context, TrainingActivity.class);
////					intent.putExtra("stateMent", stateMent);
////					intent.putExtra("enter_date", enter_date);
////					intent.putExtra("role_code", role_code);
////					startActivity(intent);
//				}else if(code.equals("7")){	//公章证明申请单
//					ToastManager.getInstance(context).showToast("正在研发");
////					Intent intent = new Intent();
////					intent.setClass(context, CompanyChopActivity.class);
////					intent.putExtra("stateMent", stateMent);
////					startActivity(intent);
//				}else if(code.equals("8")){//设备报修申请单
//					ToastManager.getInstance(context).showToast("正在研发");
////					String defaultDept = response.getString("defaultDept");
////					Intent intent = new Intent();
////					intent.setClass(context, DeviceRepairActivity.class);
////					intent.putExtra("stateMent", stateMent);
////					intent.putExtra("defaultDept", defaultDept);
////					startActivity(intent);
//				} else if (code.equals("9")) {// 加班任务单
//					String fenList = "";
//					if (response.isNull("fenList")) {
//						fenList = "";
//					} else {
//						fenList = response.getJSONArray("fenList").toString();
//					}
//					Intent intent = new Intent();
//					intent.setClass(context, OverTimeTaskActivity.class);
//					intent.putExtra("stateMent", stateMent);
//					intent.putExtra("fenlist", fenList);
//					startActivity(intent);
//				} else if (code.equals("10")) {// 请假申请单
//					String codeList = response.getJSONArray("codeList")
//							.toString();
//					Intent intent = new Intent();
//					intent.setClass(context, LeaveActivity.class);
//					intent.putExtra("stateMent", stateMent);
//					intent.putExtra("codeList", codeList.toString());
//					startActivity(intent);
//				} else if(code.equals("11")){	//离职申请单
//					ToastManager.getInstance(context).showToast("正在研发");
////					String dept = response.getString("dept");
////					String enter_date = response.getString("enter_date");
////					String role_code = response.getString("role_code");
////					Intent intent = new Intent();
////					intent.setClass(context, DimissionActivity.class);
////					intent.putExtra("stateMent", stateMent);
////					intent.putExtra("enter_date", enter_date);
////					intent.putExtra("role_code", role_code);
////					intent.putExtra("dept", dept);
////					startActivity(intent);
//				} else if (code.equals("13")) {// 费用申请单
//					String expenseTypeList = response.getJSONArray(
//							"expenseTypeList").toString();
//					Intent intent = new Intent();
//					intent.setClass(context, ExpenseActivity.class);
//					intent.putExtra("stateMent", stateMent);
//					intent.putExtra("expenseTypeList", expenseTypeList.toString());
//					startActivity(intent);
//				}
//
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		}

	}

	public class ServiceReceivers extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			boolean sys = arg1.getBooleanExtra("sys", false);
			if (sys) {// 显示消息
				show();
			} else {// 取消消息
				show();
			}

		}

	}

}
