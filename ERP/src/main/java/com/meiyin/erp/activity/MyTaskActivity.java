package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.MymTaskAdapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.MymTaskEntity;
import com.meiyin.erp.util.AndroidUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.LogUtil;
import com.meiyin.erp.util.ToastUtil;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @param我的日志
 * 
 * @Time 2016-6-29
 */
public class MyTaskActivity extends Activity {

	private Context context;
	private SharedPreferences sp;
	private Builder builder;
	private ArrayList<MymTaskEntity> list;
	private AsyncHttpClientUtil async;
	private Dialog_Http_Util dialog_util;
	private Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytask_activity_main);
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
				Intent intent = new Intent();
				MyTaskActivity.this.setResult(1, intent);
				MyTaskActivity.this.finish();
			}
		});
		TextView headtitletext = (TextView) findViewById(R.id.headtitletext);
		headtitletext.setText("我的日志");
	}

	private void initView() {
		// TODO Auto-generated method stub
		async = new AsyncHttpClientUtil();
		dialog_util = new Dialog_Http_Util();
		builder = new Builder(this);// 初始化审批dialog
		ListView mytasks_list = (ListView) findViewById(R.id.mytasks_list);
		MymTaskAdapter adapter = new MymTaskAdapter(this, getIntent()
				.getStringExtra("Stafforg"));
		list = new Gson().fromJson(getIntent().getStringExtra("List")
				.toString(), new TypeToken<List<MymTaskEntity>>() {
		}.getType());
		adapter.setList(list);
		mytasks_list.setAdapter(adapter);
		adapter.setOnRightItemClickListener(new MymTaskAdapter.onRightItemClickListener() {

			@Override
			public void onRightItemClick(View v, final int position) {
				// TODO Auto-generated method stub
				if (list.get(position).getType().equals("dreport")) {
					Intent intent = new Intent();
					intent.putExtra("Work_date",
							getIntent().getStringExtra("Work_date"));
					intent.putExtra("name", list.get(position).getName());
					intent.putExtra("content", list.get(position).getNotes());
					intent.putExtra("reportId", list.get(position).getReportId());
					intent.putExtra("result", 1);
					intent.setClass(MyTaskActivity.this,
							TaskWriteActivity.class);
					startActivityForResult(intent,1);
					return;
				}
				LinearLayout dialog_write_tasklinear = (LinearLayout) LayoutInflater
						.from(context).inflate(R.layout.dialog_write_mytask,
								null);
				builder.setView(dialog_write_tasklinear);
				final AlertDialog dialog_app = builder.create();// 初始化审批dialog
				dialog_app.show();
				dialog_app.getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
				dialog_app.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				Button dialog_write_task_subt = (Button) dialog_write_tasklinear
						.findViewById(R.id.dialog_write_task_subt);
				final TextView dialog_task_title = (TextView) dialog_write_tasklinear
						.findViewById(R.id.dialog_task_title);
				dialog_task_title.setText(list.get(position).getName());
				final EditText dialog_mytask_content = (EditText) dialog_write_tasklinear
						.findViewById(R.id.dialog_mytask_content);// 日志内容
				if (((TextView) v).getText().equals("汇报情况")) {
					dialog_mytask_content.setText("");
				} else {
					dialog_mytask_content.setText(((TextView) v).getText());
				}
				dialog_write_task_subt
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								String title = dialog_task_title.getText()
										.toString();
								String content = dialog_mytask_content
										.getText().toString();
								String key = sp.getString(SPConstant.MY_TOKEN,
										"");
								String date = getIntent().getStringExtra(
										"Work_date");
								if (content.trim().equals("")) {
									ToastManager.getInstance(context)
											.showToastcenter("汇报内容不能为空！");
									return;
								}
								RequestParams param = new RequestParams();
								param.put("key", key);
								param.put("taskId", list.get(position)
										.getTask_id());
								param.put("workDate", date);
								param.put("type", "report");
								param.put("reportId", "");
								param.put("reportName", title);
								param.put("reportNotes", content);
								Dialog progressDialog = dialog_util
										.showWaitingDialog(MyTaskActivity.this,
												"正在刷新", false);
								async.post(
										APIURL.TASKREPORT,
										context,
										param,
										new JsonHandler(context, progressDialog));
							}
						});
			}
		});
	}

	// 加班审批回调类
	private class JsonHandler extends MJsonHttpHandler {
		Dialog progressDialog;

		protected JsonHandler(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			String ss = response.toString();
			LogUtil.e("lyt", ss);
			progressDialog.dismiss();
			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					Intent intent = new Intent();
					activity.setResult(1, intent);
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
				if (response.getString("message").equals("success")) {
					ToastUtil.showToast(context, "汇报成功！");
					Intent intent = new Intent();
					intent.putExtra("refresh", true);
					MyTaskActivity.this.setResult(1, intent);
					MyTaskActivity.this.finish();
				} else {
					ToastUtil.showToast(context, "汇报失败！");

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean back = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			MyTaskActivity.this.setResult(1, intent);
			MyTaskActivity.this.finish();
			back = false;
		}
		return back;

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 1:
			Intent intent = new Intent();
			intent.putExtra("refresh", true);
			MyTaskActivity.this.setResult(1, intent);
			MyTaskActivity.this.finish();
			break;
		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
