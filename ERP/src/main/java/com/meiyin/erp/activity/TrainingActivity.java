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
import com.meiyin.erp.util.LogUtil;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * @param新增员工转正申请单
 * @Time 2016-6-22
 */
public class TrainingActivity extends Activity {

	private Context context;
	private Dialog_Http_Util dialog_util;
	private AsyncHttpClientUtil async;
	private SharedPreferences sp;
	private EditText training_evaluation;
	private TextView training_name, enter_training_time;
	private Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training_main);
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
		headtitletext.setText("转正申请单");
	}

	/*
	 * 初始化View
	 */
	private void initView() {
		// TODO Auto-generated method stub
		dialog_util = new Dialog_Http_Util();
		async = new AsyncHttpClientUtil();
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		training_name = (TextView) findViewById(R.id.training_name);// 姓名
		training_name.setText(sp.getString(SPConstant.MY_NAME, ""));
		enter_training_time = (TextView) findViewById(R.id.enter_training_time);// 入职时间
		enter_training_time.setText(getIntent().getStringExtra("enter_date"));
		training_evaluation = (EditText) findViewById(R.id.training_evaluation);// 自我评价

		// 提交 转正申请单
		Button training_submit = (Button) findViewById(R.id.training_submit);
		training_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String entertime = enter_training_time.getText()
						.toString();// 入职时间
				final String evaluation = training_evaluation.getText()
						.toString();// 自我评价

				if (evaluation.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("请填写自我评价！");
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
						param.put("role_code", getIntent().getStringExtra("role_code"));
						param.put("self_assess", evaluation);// 自我评价
						param.put("enter_date", entertime);// 入职时间
						param.put("currentTime", DateUtil
								.convertLongToDate(System.currentTimeMillis()));// 创建时间
						param.put("stateMent",
								getIntent().getStringExtra("stateMent"));
						Dialog progressDialog = dialog_util.showWaitingDialog(
								TrainingActivity.this, "正在刷新", false);
						async.post(APIURL.CHECK.ADDNEWOVERTIMETASK, context,
								param, new JsonHandler(TrainingActivity.this,
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

	// 新增转正申请单
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
					TrainingActivity.this.finish();
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
