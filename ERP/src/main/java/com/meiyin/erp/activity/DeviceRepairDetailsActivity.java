package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.Memu_History_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Memu_History;
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
 * @param设备报修申请单详情
 * @Time 2016-6-14
 */
public class DeviceRepairDetailsActivity extends Activity {

	private Context context;
	private ViewHolder holder;
	private ArrayList<Memu_History> memu;
	private AlertDialog dialog;
	private LinearLayout device_repair_approval;
	private ImageView device_repair_typeimg;
	private String userid, state;// 审批所需参数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.device_repair_details_main);
		context = getApplicationContext();
		SharedPreferences sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);

		/*
		 * 初始化
		 */
		initHeader();
		initData(sp);
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
		headtitletext.setText("设备报修申请单");
	}

	/*
	 * 初始化数据
	 */
	private void initData(SharedPreferences sp) {
		// TODO Auto-generated method stub
		AsyncHttpClientUtil async = new AsyncHttpClientUtil();
		Dialog_Http_Util dialog_util = new Dialog_Http_Util();
		memu = new ArrayList<Memu_History>();// 审批历史数据
		dialog = new AlertDialog.Builder(this).create();// 审批历史dialog
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		device_repair_approval = (LinearLayout) findViewById(R.id.device_repair_approval);
		device_repair_typeimg = (ImageView) findViewById(R.id.device_repair_typeimg);
		/*
		 * 查询详情所需要参数
		 */
		String applyNameState = getIntent().getStringExtra("applyNameState");
		String applyId = getIntent().getStringExtra("applyId");
		String procInstId = getIntent().getStringExtra("procInstId");
		String descId = getIntent().getStringExtra("descId");
		String TaskId = getIntent().getStringExtra("TaskId");
		String name = getIntent().getStringExtra("out");
		RequestParams param = new RequestParams();
		if (name.equals("申请表单")) {
			param.put("key", key);
			param.put("applyNameState", applyNameState);
			param.put("applyId", applyId);
			param.put("procInstId", procInstId);
			device_repair_approval.setVisibility(ViewGroup.GONE);
		} else if (name.equals("待审批事项")) {
			param.put("taskId", TaskId);
			param.put("key", key);
			param.put("applyNameState", applyNameState);
			param.put("applyId", applyId);
			param.put("procInstId", procInstId);
			param.put("isFresh", true);
			param.put("descId", descId);
			device_repair_approval.setVisibility(ViewGroup.GONE);
			// 待审批审批接口
			// approval(builder, async, TaskId, key, applyNameState, applyId,
			// procInstId,dialog_util);
		} else if (name.equals("已审批事项")) {
			param.put("key", key);
			param.put("applyNameState", applyNameState);
			param.put("applyId", applyId);
			param.put("procInstId", procInstId);
			param.put("taskId", TaskId);
			param.put("isFresh", true);
			device_repair_approval.setVisibility(ViewGroup.GONE);
		}
		Dialog progressDialog = dialog_util.showWaitingDialog(this, "正在刷新",
				false);
		async.post(APIURL.CHECK.MEMU_SQAPI, context, param, new JsonHandler(
				context, progressDialog));
	}

	public static class ViewHolder {
		private TextView repair_details_section;// 报修部门
		private TextView repair_details_time;// 报修时间
		private TextView device_details_name;// 设备名称
		private TextView repair_details_cost;// /预计费用
		private TextView repair_details_brand_model;// 品牌型号
		private TextView repair_details_serial_number;// 品牌型号
		private TextView repair_details_describe;// 故障现象描述及故障原因分析
		private TextView repair_details_replace_device;// 需维修或更换的部件

	}

	/*
	 * 初始化VIEW
	 */
	private void initView() {
		// TODO Auto-generated method stub
		holder = new ViewHolder();
		// 审批历史
		LinearLayout history_detail = (LinearLayout) findViewById(R.id.device_repair_history_detail);
		// 请假详情
		holder.repair_details_section = (TextView) findViewById(R.id.repair_details_section);// 报修部门
		holder.repair_details_time = (TextView) findViewById(R.id.repair_details_time);// 报修时间
		holder.device_details_name = (TextView) findViewById(R.id.device_details_name);// 设备名称
		holder.repair_details_cost = (TextView) findViewById(R.id.repair_details_cost);// 预计费用
		holder.repair_details_brand_model = (TextView) findViewById(R.id.repair_details_brand_model);// 品牌型号
		holder.repair_details_serial_number = (TextView) findViewById(R.id.repair_details_serial_number);// 序列号
		holder.repair_details_describe = (TextView) findViewById(R.id.repair_details_describe);// 故障现象描述及故障原因分析
		holder.repair_details_replace_device = (TextView) findViewById(R.id.repair_details_replace_device);// 需维修或更换的部件

		history_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (memu.size() < 1) {
					ToastManager.getInstance(context).showToast("网络连接失败！");
					return;
				}
				LinearLayout list_dialog = (LinearLayout) LayoutInflater.from(
						context).inflate(R.layout.list_dialog, null);
				Memu_History_Adapter adapter = new Memu_History_Adapter(context);
				ListView history_list = (ListView) list_dialog
						.findViewById(R.id.history_list);
				adapter.setList(memu);
				history_list.setAdapter(adapter);
				dialog.show();
				dialog.getWindow().setContentView(list_dialog);
			}
		});
	}

	// 员工转正申请单详情
	private class JsonHandler extends MJsonHttpHandler {
		private Dialog progressDialog;

		protected JsonHandler(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			// TODO Auto-generated constructor stub
			this.progressDialog = progressDialog;
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			progressDialog.dismiss();
			String ss = response.toString();
			Log.e("lyt", ss);
			if (!response.isNull("errorMsg")) {
				try {
					ToastManager.getInstance(context).showToast(
							response.getString("errorMsg"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			JSONArray applyHistory = null;// 历史审批
			String applyFixDept = "";// 报修部门
			String applyFixDate = "";// 报修时间
			String currentTime = "";// 创建时间
			String equipName = "";// 设备名称
			String predictFee = "";// 预计费用
			String Baradmodelnum = "";// 品牌型号
			String serialNumber = "";// 序列号
			String describes = "";// 故障现象描述及故障原因分析
			String replacePart = "";// 需维修或更换的部件
			try {
				applyFixDept = response.getString("applyFixDept");
				applyFixDate = response.getString("applyFixDate");
				currentTime = response.getString("currentTime");
				equipName = response.getString("equipName");
				predictFee = response.getString("predictFee");
				Baradmodelnum = response.getString("Baradmodelnum");
				serialNumber = response.getString("serialNumber");
				describes = response.getString("describes");
				replacePart = response.getString("replacePart");
				applyHistory = (JSONArray) response.get("applyHistory");
				state = response.getString("state");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			holder.repair_details_section.setText(applyFixDept);
			holder.repair_details_time.setText(currentTime);
			holder.device_details_name.setText(equipName);
			holder.repair_details_cost.setText(predictFee);
			holder.repair_details_brand_model.setText(Baradmodelnum);
			holder.repair_details_serial_number.setText(serialNumber);
			holder.repair_details_describe.setText(describes);
			holder.repair_details_replace_device.setText(replacePart);
			// 审批历史
			memu = new Gson().fromJson(applyHistory.toString(),
					new TypeToken<List<Memu_History>>() {
					}.getType());// 审批历史
			if (null != state && state.equals("2")) {
				String App_state = memu.get(0).getApp_state();
				if (App_state.equals("4")) {
					device_repair_typeimg.setImageResource(R.mipmap.veto);
					device_repair_typeimg.setVisibility(ViewGroup.VISIBLE);
					// 否决
				} else {
					if (!App_state.equals("6")) {
						device_repair_typeimg
								.setImageResource(R.mipmap.complete);
						device_repair_typeimg.setVisibility(ViewGroup.VISIBLE);
					}
				}

			}
		}

	}
}
