package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.json.JsonHttpHandles;
import com.meiyin.erp.util.AsyncHttpclient_Util;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.ToastUtil;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskWriteActivity extends Activity{
	private Context mContext ;
	private SharedPreferences sp;
	private EditText task_context,task_name ;
	private TextView task_write_time ;
	private String reportId="";
	private Dialog_Http_Util dhu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.taskwrite_main);
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		mContext = getApplicationContext();
		dhu = new Dialog_Http_Util();
		initHeader();
		// 初始化头部
		initview();

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
				int result=getIntent().getIntExtra("result", 12);
				intent.putExtra("bool", false);
				TaskWriteActivity.this.setResult(result, intent);
				TaskWriteActivity.this.finish();
			}
		});
		TextView headtitletext = (TextView) findViewById(R.id.headtitletext);
		headtitletext.setText("填写日志");
	}
	private void initview(){
	reportId=getIntent().getStringExtra("reportId");
	String Work_date=getIntent().getStringExtra("Work_date");
	String name=getIntent().getStringExtra("name");
	String content=getIntent().getStringExtra("content");
	
	task_context = (EditText)findViewById(R.id.task_context);
	task_name = (EditText)findViewById(R.id.task_name);
	task_write_time = (TextView)findViewById(R.id.task_write_time);
	task_context.setText(content);
	task_name.setText(name);
	task_write_time.setText(Work_date);
	Button conserve = (Button) findViewById(R.id.conserve);
	conserve.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String name = task_name.getText().toString();
			String context = task_context.getText().toString();
			if (name.equals("") || context.equals("")) {
				ToastManager.getInstance(mContext)
						.showToast("日志标题或内容不能为空！");
			} else {
				dhu.showWaitingDialog(TaskWriteActivity.this, "正在提交", false);
				RequestParams params = new RequestParams();
				String key = sp.getString(SPConstant.MY_TOKEN, "");
				params.put("key", key);
				params.put("workDate", task_write_time.getText().toString());
				params.put("reportId", reportId);
				params.put("reportName", name);
				params.put("reportNotes", context);
				AsyncHttp(APIURL.TASKWRITEAPI, params);
			}
		}
	});
	}
	private void AsyncHttp(String string, RequestParams params) {
		AsyncHttpclient_Util.post(string, mContext, params, new JsonHandler());
	}
	private class JsonHandler extends JsonHttpHandles {
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			dhu.dismissWaitingDialog();
			String msg = null;
			try {
				msg = response.getString("message");
				if (msg.equals("success")) {
					ToastUtil.showToast(mContext, "填写成功！");
					Intent intent = new Intent();
					int result=getIntent().getIntExtra("result", 12);
					intent.putExtra("bool", true);
					TaskWriteActivity.this.setResult(result, intent);
					TaskWriteActivity.this.finish();
//					task_name.setText("");
//					task_context.setText("");
//					reportId = "";
//					HTTP("");
				} else {
					ToastUtil.showToast(mContext, "填写失败！");

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
			ToastUtil.showToast(mContext, "网络连接失败！");
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			dhu.dismissWaitingDialog();
			ToastUtil.showToast(mContext, "网络连接失败！");
			super.onFailure(statusCode, headers, responseString, throwable);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean back = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			int result=getIntent().getIntExtra("result", 12);
			intent.putExtra("bool", false);
			TaskWriteActivity.this.setResult(result, intent);
			TaskWriteActivity.this.finish();
			back = false;
		}
		return back;

	}
}
