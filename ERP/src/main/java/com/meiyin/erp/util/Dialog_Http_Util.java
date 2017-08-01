package com.meiyin.erp.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.activity.ExpenseActivity;
import com.meiyin.erp.activity.GoOutActivity;
import com.meiyin.erp.activity.LeaveActivity;
import com.meiyin.erp.activity.Out_Memu;
import com.meiyin.erp.activity.OverTimeTaskActivity;
import com.meiyin.erp.activity.RequisitionActivity;
import com.meiyin.erp.adapter.AddMenu_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.AddMenuEntity;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyuting
 * @version 2015年9月15日 上午09:34:52
 */
public class Dialog_Http_Util {

	Dialog progressDialog;
	public Dialog showWaitingDialog(Context context, String msg, boolean canCancel) {
		dismissWaitingDialog();
		try {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.progress_loading, null);// 得到加载view
			// main.xml中的ImageView
			ImageView spaceshipImage = (ImageView) v.findViewById(R.id.iv_loading);
			TextView tipTextView = (TextView) v.findViewById(R.id.iv_text);// 提示文字
			// 加载动画
			Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.progress_loading);
			// 使用ImageView显示动画
			spaceshipImage.startAnimation(hyperspaceJumpAnimation);
			tipTextView.setText(msg);// 设置加载信息

			progressDialog = new Dialog(context, R.style.dialog_progress);// 创建自定义样式dialog


			progressDialog.setCancelable(false);// 不可以用"返回键"取消
			if (canCancel) {

				progressDialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							dismissWaitingDialog();
						}
						return false;
					}
				});
			} else {
				progressDialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
						}
						return false;
					}
				});
			}

			progressDialog.show();
			progressDialog.getWindow().setContentView(v);// 设置布局
		} catch (Exception e) {
		}
		return progressDialog;
	}

	public void dismissWaitingDialog() {

		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}



	ArrayList<AddMenuEntity> mlist;
	LinearLayout new_dialog ;
	AddMenu_Adapter adapter ;
	AlertDialog dialog;
	Context mcontext;
	String code;
	Activity mactivity;
	public void dialogcd(Context context,final SharedPreferences sp,Activity activity){
		this.mcontext=context;
		this.mactivity=activity;
		dialog = new AlertDialog.Builder(activity).create();
		mlist = new ArrayList<AddMenuEntity>();
		RequestParams param = new RequestParams();
		param.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
		AsyncHttpClientUtil async = new AsyncHttpClientUtil();
		Dialog progressDialog = showWaitingDialog(mactivity, "正在刷新",
				false);
		async.post(APIURL.CHECK.ADDMEMUAPI, context, param, new JsonHandler(
				mactivity, progressDialog));

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
				Dialog progressDialog =showWaitingDialog(mactivity, "正在刷新", false);
				dialog.dismiss();
				if (mlist.size() > 0) {
					code = mlist.get(arg2).getCode();
					if (null != code) {
						RequestParams param = new RequestParams();
						param.setContentEncoding("utf-8");
						param.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
						param.put("apply", code);// code
						new AsyncHttpClientUtil().post(
								APIURL.CHECK.ADDMEMUINTI, mcontext, param,
								new JsonHandlerInit(mactivity, progressDialog));
					} else {
						progressDialog.dismiss();
					}
				} else {
					progressDialog.dismiss();
				}
			}
		});

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
					AndroidUtil.LoginOut(mactivity,errorMsg);
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
							if(addMenuEntity.getCode().equals("2")||addMenuEntity.getCode().equals("3")||addMenuEntity.getCode().equals("5")||addMenuEntity.getCode().equals("9")||addMenuEntity.getCode().equals("10")||addMenuEntity.getCode().equals("13")){
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
					AndroidUtil.LoginOut(mactivity,errorMsg);
					return;
				}
				String stateMent = response.getString("stateMent");
				if (code.equals("2")) {// 外出申请单
					Boolean sell = response.getBoolean("sell");
					Intent intent = new Intent();
					intent.setClass(mcontext, Out_Memu.class);
					intent.putExtra("stateMent", stateMent);
					intent.putExtra("sell", sell);
					mactivity.startActivity(intent);
				} else if (code.equals("3")) {// 出差申请单
					Boolean sell = response.getBoolean("sell");
					String vehicleList = response.getJSONArray("vehicleList")
							.toString();
					Intent intent = new Intent();
					intent.setClass(mcontext, GoOutActivity.class);
					intent.putExtra("sell", sell);
					intent.putExtra("vehicleList", vehicleList);
					intent.putExtra("stateMent", stateMent);
					mactivity.startActivity(intent);
				} else if (code.equals("5")) {// 请购申请单
					Intent intent = new Intent();
					intent.setClass(mcontext, RequisitionActivity.class);
					intent.putExtra("stateMent", stateMent);
					mactivity.startActivity(intent);
				} else if(code.equals("6")){//员工转正申请单
					ToastManager.getInstance(mcontext).showToast("正在研发");
//					String enter_date = response.getString("enter_date");
//					String role_code = response.getString("role_code");
//					Intent intent = new Intent();
//					intent.setClass(context, TrainingActivity.class);
//					intent.putExtra("stateMent", stateMent);
//					intent.putExtra("enter_date", enter_date);
//					intent.putExtra("role_code", role_code);
//					startActivity(intent);
				}else if(code.equals("7")){	//公章证明申请单
					ToastManager.getInstance(mcontext).showToast("正在研发");
//					Intent intent = new Intent();
//					intent.setClass(context, CompanyChopActivity.class);
//					intent.putExtra("stateMent", stateMent);
//					startActivity(intent);
				}else if(code.equals("8")){//设备报修申请单
					ToastManager.getInstance(mcontext).showToast("正在研发");
//					String defaultDept = response.getString("defaultDept");
//					Intent intent = new Intent();
//					intent.setClass(context, DeviceRepairActivity.class);
//					intent.putExtra("stateMent", stateMent);
//					intent.putExtra("defaultDept", defaultDept);
//					startActivity(intent);
				} else if (code.equals("9")) {// 加班任务单
					String fenList = "";
					if (response.isNull("fenList")) {
						fenList = "";
					} else {
						fenList = response.getJSONArray("fenList").toString();
					}
					Intent intent = new Intent();
					intent.setClass(mcontext, OverTimeTaskActivity.class);
					intent.putExtra("stateMent", stateMent);
					intent.putExtra("fenlist", fenList);
					mactivity.startActivity(intent);
				} else if (code.equals("10")) {// 请假申请单
					String codeList = response.getJSONArray("codeList")
							.toString();
					Intent intent = new Intent();
					intent.setClass(mcontext, LeaveActivity.class);
					intent.putExtra("stateMent", stateMent);
					intent.putExtra("codeList", codeList.toString());
					mactivity.startActivity(intent);
				} else if(code.equals("11")){	//离职申请单
					ToastManager.getInstance(mcontext).showToast("正在研发");
//					String dept = response.getString("dept");
//					String enter_date = response.getString("enter_date");
//					String role_code = response.getString("role_code");
//					Intent intent = new Intent();
//					intent.setClass(context, DimissionActivity.class);
//					intent.putExtra("stateMent", stateMent);
//					intent.putExtra("enter_date", enter_date);
//					intent.putExtra("role_code", role_code);
//					intent.putExtra("dept", dept);
//					startActivity(intent);
				} else if (code.equals("13")) {// 费用申请单
					String expenseTypeList = response.getJSONArray(
							"expenseTypeList").toString();
					Intent intent = new Intent();
					intent.setClass(mcontext, ExpenseActivity.class);
					intent.putExtra("stateMent", stateMent);
					intent.putExtra("expenseTypeList", expenseTypeList.toString());
					mactivity.startActivity(intent);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
