package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.BaseApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.LogUtil;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @param新增公司公章
 *            (证照)申请单
 * @Time 2016-6-21
 */
public class CompanyChopActivity extends Activity {

	private Context context;
	private Dialog_Http_Util dialog_util;
	private AsyncHttpClientUtil async;
	private SharedPreferences sp;
	private String type = "公章";
	private EditText new_companychop_cause;
	private TextView companychop_name, new_companychop_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.companychop_main);
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
		headtitletext.setText("增加公司公章(证照)申请单");
	}

	/*
	 * 初始化View
	 */
	private void initView() {
		// TODO Auto-generated method stub
		dialog_util = new Dialog_Http_Util();
		async = new AsyncHttpClientUtil();
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		companychop_name = (TextView) findViewById(R.id.companychop_name);
		companychop_name.setText(sp.getString(SPConstant.MY_NAME, ""));
		new_companychop_time = (TextView) findViewById(R.id.new_companychop_time);
		new_companychop_time.setText(com.meiyin.erp.util.DateUtil
				.convertLongToDate(System.currentTimeMillis()));
		new_companychop_cause = (EditText) findViewById(R.id.new_companychop_cause);
		RadioGroup RadioGroup_ = (RadioGroup) findViewById(R.id.RadioGroup_companychop);
		RadioGroup_.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.RadioGroup_companychop1:
					type = "公章";
					break;
				case R.id.RadioGroup_companychop2:
					type = "执照";
					break;
				case R.id.RadioGroup_companychop3:
					type = "合同公章";
					break;
				case R.id.RadioGroup_companychop4:
					type = "其他";
					break;
				default:
					break;
				}
			}
		});
		// 提交 公司公章(证照)申请单
		Button companychop_submit = (Button) findViewById(R.id.companychop_submit);
		companychop_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String time = new_companychop_time.getText().toString();// 时间
				final String cause = new_companychop_cause.getText().toString();// 盖章事由

				if (cause.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("盖章事由不能为空！");
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

						RequestParams param = new RequestParams();
						param.setContentEncoding("utf-8");
						param.put("key", key);
						param.put("orgId", "");
						param.put("cachet_type", type);// 印章类型
						param.put("cachet_reason", cause);// 盖章事由
						param.put("currentTime", time);// 填表时间
						param.put("stateMent",
								getIntent().getStringExtra("stateMent"));
						Log.e("lyt", param.toString());
						Dialog progressDialog = dialog_util.showWaitingDialog(
								CompanyChopActivity.this, "正在刷新", false);
						async.post(APIURL.CHECK.ADDNEWOVERTIMETASK, context,
								param, new JsonHandler(
										CompanyChopActivity.this,
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

	// 新增公司公章(证照)申请单
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
			if (!response.isNull("errorMsg")) {
				try {
					ToastManager.getInstance(context).showToast(
							response.getString("errorMsg"));
					stopService(new Intent()
							.setAction("com.meiyin.services.Meiyinservice"));
					startActivity(new Intent().setClass(context, Login.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					BaseApplication.getInstance().AppExit();
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
							.showToastcenter("提交成功！");
					CompanyChopActivity.this.finish();
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
