package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.GreenDao.MeiyinnewsDao;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.Memulist_Adapter;
import com.meiyin.erp.adapter.Spinner_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.MyApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Meiyinnews;
import com.meiyin.erp.entity.Memulist_Entity;
import com.meiyin.erp.entity.Spinner_Entity;
import com.meiyin.erp.json.JsonHttpHandles;
import com.meiyin.erp.ui.XListView;
import com.meiyin.erp.util.AndroidUtil;
import com.meiyin.erp.util.AsyncHttpclient_Util;
import com.meiyin.erp.util.DateUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.LogUtil;
import com.meiyin.erp.util.ToastUtil;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 申请单列表页面
 */
public class Memulist extends Activity implements XListView.IXListViewListener, OnItemClickListener {

	private Context content;
	private XListView memu_listview;
	private Memulist_Adapter adapter;
	private ArrayList<Memulist_Entity> memu;

	private MeiyinnewsDao meiyin;
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;
	private SharedPreferences sp;
	private int load = 1;
	private int leixin;
	private String name;
	private Dialog_Http_Util dhu;
	private MemulistReceiver Receiver;
	ArrayList<Spinner_Entity> spn;
	private int applyName=0;
	private String names ;
	private Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memulist);
		content = getApplicationContext();
		activity=this;
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		names = sp.getString(SPConstant.USERNAME, "");
		initHeader();
		// 注册接收广播
		Receiver = new MemulistReceiver();
		IntentFilter Filters = new IntentFilter();
		Filters.addAction("meiyinsp");
		registerReceiver(Receiver, Filters);

		adapter = new Memulist_Adapter(content, Memulist.this);
		memu = new ArrayList<Memulist_Entity>();
		activity();
		meiyin = MyApplication.getInstance().getDaoSession().getMeiyinnewsDao();

		Intent intent = new Intent();
		intent.setAction("meiyin");
		intent.putExtra("sys", true);
		Memulist.this.sendBroadcast(intent);

		memu_listview = (XListView) findViewById(R.id.memu_listview);
		memu_listview.setOnItemClickListener(this);
		memu_listview.setPullLoadEnable(true);
		memu_listview.setXListViewListener(this);
		memu_listview.setRefreshTime(sp.getString(SPConstant.REFRESHTIME, ""));
		mHandler = new Handler();
		adapter.setList(memu);
		memu_listview.setAdapter(adapter);
		// MED = new Memulist_EntityDaoManager();
		// MED.insertList(memu);
		// list = MED.queryAll();

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
		name = getIntent().getStringExtra("name");
		headtitletext.setText(name);
	}
	/**
	 * 页面参数分类
	 */
	private void activity() {
		dhu = new Dialog_Http_Util();
		dhu.showWaitingDialog(Memulist.this, "正在加载", true);
		if (name.equals("申请表单")) {
			leixin = 0;
			spn = new ArrayList<Spinner_Entity>();
			spn.add(new Spinner_Entity("请选择申请单", "0"));
			spn.add(new Spinner_Entity("外出申请单", "2"));
			spn.add(new Spinner_Entity("请购申请单", "5"));
			spn.add(new Spinner_Entity("员工转正申请单", "6"));
			spn.add(new Spinner_Entity("公章证明申请单", "7"));
			spn.add(new Spinner_Entity("设备报修申请单", "8"));
			spn.add(new Spinner_Entity("加班任务单", "9"));
			spn.add(new Spinner_Entity("请假申请单", "10"));
			spn.add(new Spinner_Entity("离职申请单", "11"));
			spn.add(new Spinner_Entity("费用申请单", "13"));
			
			Spinner memu_spn1 = (Spinner) findViewById(R.id.memu_spn1);
			if(names.equals("wenhui")||names.equals("wangshun")||names.equals("tanghuiling")||names.equals("xuliang")||names.equals("chenqing")||names.equals("chenxianhua")){
				memu_spn1.setVisibility(ViewGroup.VISIBLE);		
			}else{
				memu_spn1.setVisibility(ViewGroup.GONE);
			}

			Spinner_Adapter spinnerAdapter1 = new Spinner_Adapter(content);
			spinnerAdapter1.setList(spn);
			memu_spn1.setAdapter(spinnerAdapter1);
			memu_spn1.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					applyName = Integer.parseInt(spn.get(arg2).getType_id());

					dhu.showWaitingDialog(Memulist.this, "正在加载", true);
					memu.clear();
					load = 1;
					AsyncHttp(0, leixin);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});
			AsyncHttp(0, leixin);			
		} else if (name.equals("待审批事项")) {
			leixin = 1;
			AsyncHttp(0, leixin);
		} else if (name.equals("已审批事项")) {
			leixin = 2;
			AsyncHttp(0, leixin);
		}
	}


	private void onLoad() {
		String RefreshTime = DateUtil.convertLongToDate(System
				.currentTimeMillis());
		memu_listview.stopRefresh();
		memu_listview.stopLoadMore();
		memu_listview.setRefreshTime(RefreshTime);
		sp.edit().putString(SPConstant.REFRESHTIME, RefreshTime).commit();
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				memu.clear();
				load = 1;
				// geneItems();
				// mAdapter.notifyDataSetChanged();
				AsyncHttp(0, leixin);
			}
		}, 100);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				AsyncHttp(load, leixin);
				load++;
			}
		}, 100);
	}

	public void AsyncHttp(int paga, int state) {
		String string = APIURL.CHECK.MEMUAPI;
		RequestParams params = new RequestParams();
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		params.put("key", key);
		params.put("page", paga);
		params.put("state", state);
		if(state==0){
		if(applyName!=0){
		params.put("applyName", applyName);
		params.put("menuid", "01090608");
		params.put("menu_value", "SYSQBD");
		if(names.equals("wenhui")||names.equals("wangshun")||names.equals("tanghuiling")||names.equals("xuliang")||names.equals("chenqing")||names.equals("chenxianhua")){
			params.put("accompanyId", "all");			
		}		
		}

//		orgId=all
		}
		AsyncHttpclient_Util.post(string, content, params, new JsonHandler(state));
	}

	private class JsonHandler extends JsonHttpHandles {
		int state;
		private JsonHandler(int state){
			this.state=state;
		}
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			 LogUtil.e("lyt", response.toString());
			dhu.dismissWaitingDialog();
			String msg=null;
			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
				msg = response.getString("message");
				
				if (msg.equals("success")) {
					JSONArray array = (JSONArray) response.get("result");
					ArrayList<Memulist_Entity> list = new Gson().fromJson(
							array.toString(),
							new TypeToken<List<Memulist_Entity>>() {
							}.getType());
					for (Memulist_Entity item : list) {
						memu.add(item);
					}
					adapter.setList(memu);
					adapter.notifyDataSetChanged();
					onLoad();
				} else if (msg.equals("nodata")) {
					String RefreshTime = DateUtil
							.convertLongToDate(System.currentTimeMillis());
					memu_listview.stopRefresh();
					memu_listview.Loadall();
					memu_listview.setRefreshTime(RefreshTime);
					sp.edit().putString(SPConstant.REFRESHTIME, RefreshTime)
							.commit();
					adapter.setList(memu);
					adapter.notifyDataSetChanged();
					ToastUtil.showToast(content, "已加载完毕！");
					// memu_listview.setVisibility(ViewGroup.GONE);
				} else {
					onLoad();
					ToastUtil.showToast(content, msg);
				}
				if(state==1){
					showmemu(memu);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				onLoad();
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
			LogUtil.e("lyt", statusCode +responseString+ throwable.toString());
			ToastManager.getInstance(content).showToast("网络连接失败！");
			super.onFailure(statusCode, headers, responseString, throwable);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if (arg2 > 0) {
			if (memu.size() > 0) {
				String Type = memu.get(arg2 - 1).getType();
				String ApplyNameState = memu.get(arg2 - 1).getApplyNameState();
				String ApplyId = memu.get(arg2 - 1).getApplyId();
				String ProcInstId = memu.get(arg2 - 1).getProcInstId();
				String descId = memu.get(arg2 - 1).getDescId();
				String TaskId = memu.get(arg2 - 1).getTaskId();
				if (name.equals("待审批事项")) {
					showMessage(ApplyId);
				}
				if (ApplyNameState.equals("2")) {// 外出申请单
					intent.setClass(this, Out_Memu_Sq.class);
				} else if (ApplyNameState.equals("3")) {// 出差申请单
					intent.setClass(this, GoOutDetailsActivity.class);
				} else if (ApplyNameState.equals("5")) {// 请购申请单
					intent.setClass(this, RequisitionDetailActivity.class);
				} else if (ApplyNameState.equals("6")) {// 员工转正申请单
					intent.setClass(this, TrainingDetailsActivity.class);
				} else if (ApplyNameState.equals("7")) {// 公司公章(证照)申请单
					intent.setClass(this, CompanyChopDetailsActivity.class);
				} else if (ApplyNameState.equals("8")) {// 故障设备报修申请单
					intent.setClass(this, DeviceRepairDetailsActivity.class);
				} else if (ApplyNameState.equals("9")) {// 加班任务单
					intent.setClass(this, OverTimeTaskDetailsActivity.class);
				} else if (ApplyNameState.equals("10")) {// 请假申请单
					intent.setClass(this, LeaveDetailsActivity.class);
				} else if (ApplyNameState.equals("11")) {// 离职申请单
					intent.setClass(this, DimissionDetailsActivity.class);
				} else if (ApplyNameState.equals("13")) {// 费用申请单
					intent.setClass(this, ExpenseDetailsActivity.class);
				} else {
					return;
				}
				intent.putExtra("out", name);
				intent.putExtra("applyNameState", ApplyNameState);
				intent.putExtra("applyId", ApplyId);
				intent.putExtra("procInstId", ProcInstId);
				intent.putExtra("descId", descId);
				intent.putExtra("TaskId", TaskId);
				startActivity(intent);
			}
		}
	}
	/*
	 * 待审批列表中是否包括此消息
	 */
	@SuppressLint("InlinedApi")
	public void showmemu(ArrayList<Memulist_Entity> list) {
		SharedPreferences sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
		List<Meiyinnews> si = meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(
				SPConstant.USERNAME, "")),MeiyinnewsDao.Properties.Type.eq("办公管理系统"),MeiyinnewsDao.Properties.Biaoshi.eq("0")).list();
		if (si.size() > 0) {
			for (int i = 0; i < si.size(); i++) {
				String idstring = si.get(i).getIdString();
				if (null == idstring || idstring.equals("")) {
					break;
				}
				boolean zt=false;
				if(list.size()>0){
				for (Memulist_Entity item : list) {
					if (idstring.equals(item.getApplyId())) {
						zt=true;
					}
				}
				}
				if(!zt){
				si.get(i).setBiaoshi("1");
				meiyin.update(si.get(i));
				Intent intent = new Intent();
				intent.setAction("meiyin");
				intent.putExtra("sys", false);
				Memulist.this.sendBroadcast(intent);
				}
			}
		}
	}

	@SuppressLint("InlinedApi")
	public void showMessage(String ApplyId) {
		SharedPreferences sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
		List<Meiyinnews> si = meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(
				SPConstant.USERNAME, "")),MeiyinnewsDao.Properties.Type.eq("办公管理系统"),MeiyinnewsDao.Properties.Biaoshi.eq("0")).list();
		if (si.size() > 0) {
			for (int i = 0; i < si.size(); i++) {
				String idstring = si.get(i).getIdString();
				if (null == idstring || idstring.equals("")) {
					break;
				}
				if (idstring.equals(ApplyId)) {
					si.get(i).setBiaoshi("1");
					meiyin.update(si.get(i));
					Intent intent = new Intent();
					intent.setAction("meiyin");
					intent.putExtra("sys", false);
					Memulist.this.sendBroadcast(intent);

				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(Receiver);
		super.onDestroy();
	}

	public class MemulistReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			dhu.showWaitingDialog(Memulist.this, "正在刷新", false);
			onRefresh();

		}

	}
}
