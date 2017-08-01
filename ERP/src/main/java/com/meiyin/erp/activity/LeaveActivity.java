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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.Spinner_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.datepicker.DatePick;
import com.meiyin.erp.entity.Spinner_Entity;
import com.meiyin.erp.util.AndroidUtil;
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

/**
 * @param新增请假申请单
 * @Time 2016-5-23
 */
public class LeaveActivity extends Activity {

	private TextView leave_start_time;
	private TextView leave_end_time;
	private Context context;
	private Dialog_Http_Util dialog_util;
	private AsyncHttpClientUtil async;
	private SharedPreferences sp;
	private ArrayList<Spinner_Entity> spn1;
	private String type = "1";
	private EditText leave_peoples, new_leave_cause, new_leave_hours,
			new_leave_min;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_leave_main);
		context = getApplicationContext();
		activity=this;
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		/*
		 * 初始化
		 */
		initHeader();
		initView();
		// initDate();

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
		headtitletext.setText("请假申请单");
	}

	/*
	 * 初始化View
	 */
	private void initView() {
		// TODO Auto-generated method stub
		dialog_util = new Dialog_Http_Util();
		async = new AsyncHttpClientUtil();
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		spn1 = new ArrayList<Spinner_Entity>();
		Spinner leave_spn1 = (Spinner) findViewById(R.id.leave_spn1);

		String codeList = getIntent().getExtras().getString("codeList");
		try {
			JSONArray arry = new JSONArray(codeList);
			for (int i = 0; i < arry.length(); i++) {
				String code = arry.getJSONObject(i).getString("code");
				String code_desc = arry.getJSONObject(i).getString("code_desc");
				spn1.add(new Spinner_Entity(code_desc, code));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 物品名称
		Spinner_Adapter spinnerAdapter1 = new Spinner_Adapter(context);
		spinnerAdapter1.setList(spn1);
		leave_spn1.setAdapter(spinnerAdapter1);
		leave_spn1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				type = spn1.get(arg2).getType_id();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		leave_start_time = (TextView) findViewById(R.id.leave_start_time);
		leave_end_time = (TextView) findViewById(R.id.leave_end_time);
		leave_start_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout date_dialog = (RelativeLayout) LayoutInflater
						.from(context).inflate(R.layout.date_dialog, null);
				date_dialog.addView(new DatePick(LeaveActivity.this,
						leave_start_time).getDataPick());
				dialog.show();
				dialog.getWindow().setContentView(date_dialog);
				leave_start_time.setText(DateUtil.getCurrentTimeStr());
			}
		});
		leave_end_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout date_dialog = (RelativeLayout) LayoutInflater
						.from(context).inflate(R.layout.date_dialog, null);
				date_dialog.addView(new DatePick(LeaveActivity.this,
						leave_end_time).getDataPick());
				dialog.show();
				dialog.getWindow().setContentView(date_dialog);
				leave_end_time.setText(DateUtil.getCurrentTimeStr());
			}
		});
		leave_peoples = (EditText) findViewById(R.id.leave_peoples);
		new_leave_cause = (EditText) findViewById(R.id.new_leave_cause);
		new_leave_hours = (EditText) findViewById(R.id.new_leave_hours);
		new_leave_min = (EditText) findViewById(R.id.new_leave_min);
		// 提交请假申请单
		Button leave_submit = (Button) findViewById(R.id.leave_submit);
		leave_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String start = leave_start_time.getText().toString();// 开始时间
				final String end = leave_end_time.getText().toString();// 结束时间
				final String cause = new_leave_cause.getText().toString();// 请假原因
				final String hour = new_leave_hours.getText().toString();// 小时
				final String min = new_leave_min.getText().toString();// 分钟
				if (start.equals("") || end.equals("") || cause.equals("")
						|| hour.equals("") || min.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("必填项不能为空！");
					dialog.dismiss();
					return;
				}
				long starts = DateUtil.getLongFromStr(start);
				long ends = DateUtil.getLongFromStr(end);
				long now = System.currentTimeMillis();
				long time = starts - now;
				long times = time / 1000 / 3600;
				if (times < 24)  {
					ToastManager.getInstance(context)
							.showToastcenter("开始时间错误，请假单必须提前一天填写！");
					dialog.dismiss();
					return;
				}
				long time1 = ends-starts;
				long times1 = time1 / 1000 / 3600;
				if (times1 < 0)  {
					ToastManager.getInstance(context)
							.showToastcenter("结束时间错误，结束时间和开始时间混乱！");
					dialog.dismiss();
					return;
				}
				if(cause.length()<21){
					ToastManager.getInstance(context)
					.showToastcenter("请假原因不能少于20个字！");
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
						String leave_peoplestring = leave_peoples.getText()
								.toString();// 请假期间代班人
						RequestParams param = new RequestParams();
						param.setContentEncoding("utf-8");
						param.put("key", key);
						param.put("start_time", start);// 开始时间
						param.put("end_time", end);// 结束时间;
						param.put("leave_type", type);// 请假类型
						param.put("leave_reason", cause);// 请假原因
						param.put("replace_user", leave_peoplestring);// 请假期间代班人

						param.put("leave_time_min", min);// 请假时长（分钟）
						param.put("leave_time_hour", hour);// 请假时长（小时）
						param.put("state", 0);// 流程
						param.put("stateDesc", 0);
						param.put("leave_times", "1440");
						param.put("stateMent",
								getIntent().getStringExtra("stateMent"));
						Log.e("lyt", param.toString());
						Dialog progressDialog = dialog_util.showWaitingDialog(
								LeaveActivity.this, "正在刷新", false);
						async.post(APIURL.CHECK.ADDMEMULEAVE, context, param,
								new JsonHandler(LeaveActivity.this,
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

	// 请假申请单提交回调类
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
					LeaveActivity.this.finish();
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
