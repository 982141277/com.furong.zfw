package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.util.AndroidUtil;
import com.meiyin.erp.util.DateUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @param设备报修申请单
 * @Time 2016-6-21
 */
public class DeviceRepairActivity extends Activity {

	private Context context;
	private Dialog_Http_Util dialog_util;
	private AsyncHttpClientUtil async;
	private SharedPreferences sp;
	private EditText device_repair_name, repair_brand_model,
			device_serial_numbers, device_repair_cost, device_repair_describe,
			repair_replace_device;
	private Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_repair_main);
		context = getApplicationContext();
		activity=this;
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
		headtitletext.setText("设备报修申请单");
	}

	/*
	 * 初始化View
	 */
	private void initView() {
		// TODO Auto-generated method stub
		dialog_util = new Dialog_Http_Util();
		async = new AsyncHttpClientUtil();
		final AlertDialog dialog = new AlertDialog.Builder(this).create();

		device_repair_name = (EditText) findViewById(R.id.device_repair_name);// 设备名称
		repair_brand_model = (EditText) findViewById(R.id.repair_brand_model);// 品牌型号
		device_serial_numbers = (EditText) findViewById(R.id.device_serial_numbers);// 序列号
		device_repair_cost = (EditText) findViewById(R.id.device_repair_cost);// 预计费用
		device_repair_describe = (EditText) findViewById(R.id.device_repair_describe);// 故障现象描述及故障原因分析
		repair_replace_device = (EditText) findViewById(R.id.repair_replace_device);// 需维修或更换的部件
		//设备报修申请单
		Button device_repair_submit = (Button) findViewById(R.id.device_repair_submit);
		device_repair_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String name = device_repair_name.getText().toString();// 名称
				final String model = repair_brand_model.getText().toString();// 型号
				final String numbers = device_serial_numbers.getText()
						.toString();// 序列号
				final String cost = device_repair_cost.getText().toString();// 预计费用
				final String describe = device_repair_describe.getText()
						.toString();// 故障现象描述及故障原因分析
				final String replace_device = repair_replace_device.getText()
						.toString();// 需维修或更换的部件
				if (name.equals("") || model.equals("") || numbers.equals("")
						|| cost.equals("") || describe.equals("")
						|| replace_device.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("必填项不能为空！");
					dialog.dismiss();
					return;
				}
				LinearLayout submit_dialog = (LinearLayout) LayoutInflater
						.from(context).inflate(R.layout.submit_dialog, null);
				Button button_t = (Button) submit_dialog
						.findViewById(R.id.button_t);
				Button button_f = (Button) submit_dialog
						.findViewById(R.id.button_f);
				dialog.show();
				dialog.getWindow().setContentView(submit_dialog);
				button_t.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						String key = sp.getString(SPConstant.MY_TOKEN, "");
						String device_times = DateUtil
								.convertLongToDate(System.currentTimeMillis());
						RequestParams param = new RequestParams();
						param.setContentEncoding("utf-8");
						param.put("key", key);
						param.put("currentTime", device_times);// 创建时间
						param.put("predictFee", cost);// 预计费用
						param.put("applyFixDept", getIntent().getStringExtra("defaultDept"));// 部门
						param.put("equipName", name);// 设备名称
						param.put("baradmodelnum", model);// 设备型号
						param.put("describe", describe);// 故障现象描述及故障原因分析
						param.put("applyFixDate", device_times);// 报修时间
						param.put("serialNumber", numbers);// 序列号
						param.put("replacePart", replace_device);// 需维修或更换的部件
						param.put("stateMent",
								getIntent().getStringExtra("stateMent"));
						Dialog progressDialog = dialog_util.showWaitingDialog(
								DeviceRepairActivity.this, "正在刷新", false);
						async.post(APIURL.CHECK.ADDNEWOVERTIMETASK, context,
								param, new JsonHandler(
										DeviceRepairActivity.this,
										progressDialog));
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
		});
	}

	//设备报修申请单
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
			progressDialog.dismiss();
			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
				String message = response.getString("message");
				if (message.equals("success")) {
					ToastManager.getInstance(context)
							.showToastcenter("提交成功！");
					DeviceRepairActivity.this.finish();
				} else {
					ToastManager.getInstance(context)
							.showToastcenter("提交失败！！！");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
