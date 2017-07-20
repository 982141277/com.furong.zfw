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
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.BaseApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.util.DateUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.LogUtil;
import com.meiyin.erp.util.ToastUtil;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 任务详情
 */
public class MyTaskListDetailsActivity extends Activity {
	private SharedPreferences sp ;
	private Context context ;
	private LinearLayout my_approve_bt;
	private JSONArray taskreport;
	private boolean bools=false;
	private String Task_id;
	private String Task_status;
	private LinearLayout my_approve_report_bt;
	private String Task_name;
	private TextView mytask_details_summ;
	private LinearLayout mytask_details_summ_linear;
	private View mytask_details_summ_view;
	private String report_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mytasklist_details_activity_main);
		context = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		/*
		 * 初始化
		 */
		initHeader();
		initView();
		initFools();
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

				MyTaskListDetailsActivity.this.setResult(11, intent);
				MyTaskListDetailsActivity.this.finish();
			}
		});
		TextView headtitletext = (TextView) findViewById(R.id.headtitletext);
		headtitletext.setText("任务详情");
	}
	private void initView(){
		String Creat_username=getIntent().getStringExtra("Creat_username");
		Task_name=getIntent().getStringExtra("Task_name");
		String Task_target=getIntent().getStringExtra("Task_target");
		String Source=getIntent().getStringExtra("Source");
		String Operatorname=getIntent().getStringExtra("Operatorname");
		String Knowername=getIntent().getStringExtra("Knowername");
		String Task_notes=getIntent().getStringExtra("Task_notes");
		String Start_time=getIntent().getStringExtra("Start_time");
		String End_time=getIntent().getStringExtra("End_time");
		String Task_type=getIntent().getStringExtra("Task_type");
		Task_status=getIntent().getStringExtra("Task_status");
		Task_id=getIntent().getStringExtra("Task_id");
		
		TextView mytask_details_name = (TextView) findViewById(R.id.mytask_details_name);// 任务名称
		TextView mytask_details_target = (TextView) findViewById(R.id.mytask_details_target);// 任务目标
		TextView mytask_details_times = (TextView) findViewById(R.id.mytask_details_times);// 任务时间
		TextView mytask_details_creates = (TextView) findViewById(R.id.mytask_details_creates);// 任务下达人
		TextView mytask_details_execute = (TextView) findViewById(R.id.mytask_details_execute);// 任务执行人
		TextView mytask_details_know = (TextView) findViewById(R.id.mytask_details_know);// 任务知晓人
		TextView mytask_details_source = (TextView) findViewById(R.id.mytask_details_source);// 任务资源
		TextView mytask_details_describe = (TextView) findViewById(R.id.mytask_details_describe);// 任务描述
		TextView mytask_details_state = (TextView) findViewById(R.id.mytask_details_state);// 任务状态
		mytask_details_summ = (TextView) findViewById(R.id.mytask_details_summ);// 任务总结
		 mytask_details_summ_linear = (LinearLayout) findViewById(R.id.mytask_details_summ_linear);// 任务总结
		 mytask_details_summ_view =findViewById(R.id.mytask_details_summ_view);// 任务总结
		
		LinearLayout linear_task_target1 = (LinearLayout) findViewById(R.id.linear_task_target1);// 目标
		LinearLayout linear_task_know2 = (LinearLayout) findViewById(R.id.linear_task_know2);// 知晓
		LinearLayout linear_task_source3 = (LinearLayout) findViewById(R.id.linear_task_source3);// 资源
		
		View task_target_view1 =findViewById(R.id.task_target_view1);// 目标
		View task_know_view2 =findViewById(R.id.task_know_view2);// 知晓
		View task_source_view3 =findViewById(R.id.task_source_view3);// 资源
		
		
		
		String start = DateUtil.getEnglishTime(Start_time);
		String end = DateUtil.getEnglishTime(End_time);
		mytask_details_name.setText(Task_name);
		if(Task_target.equals("")){
			linear_task_target1.setVisibility(ViewGroup.GONE);
			task_target_view1.setVisibility(ViewGroup.GONE);
		}
		mytask_details_target.setText(Task_target);
		mytask_details_times.setText(start.substring(0, start.length()-8)+" 至 "+end.substring(0, end.length()-8));
		mytask_details_creates.setText(Creat_username);
		mytask_details_execute.setText(Operatorname);
		if(Knowername.equals("")){
			linear_task_know2.setVisibility(ViewGroup.GONE);
			task_know_view2.setVisibility(ViewGroup.GONE);
		}
		mytask_details_know.setText(Knowername);
		if(Source.equals("")){
			linear_task_source3.setVisibility(ViewGroup.GONE);
			task_source_view3.setVisibility(ViewGroup.GONE);
		}
		mytask_details_source.setText(Source);
		mytask_details_describe.setText(Html.fromHtml(Task_notes));
		if(Task_status.equals("0")){
			mytask_details_state.setText("未完成");
			mytask_details_state.setTextColor(getResources().getColor(R.color._red));
		}else if(Task_status.equals("1")){
			mytask_details_state.setText("已完成");
			mytask_details_state.setTextColor(getResources().getColor(R.color._green));	
		}
		http();


	}
	private void http(){
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		AsyncHttpClientUtil Async = new AsyncHttpClientUtil();
		Dialog_Http_Util dialog_util = new Dialog_Http_Util();
		RequestParams param = new RequestParams();
		param.put("key", key);
		param.put("task_id", Task_id);
		param.put("reportypeinfo", "userreport");
//		seachStaffCode=5f0a1cc1c5e84cee923b3394e77cc3f5
		Dialog progressDialog = dialog_util.showWaitingDialog(
				MyTaskListDetailsActivity.this, "正在加载", false);
		Async.post(APIURL.MYFINDOTASK, context, param,
				new JsonHandler(MyTaskListDetailsActivity.this, progressDialog));		
	}
	private void initFools() {
		TextView my_approve_text = (TextView) findViewById(R.id.my_approve_text);
		TextView my_appprove_history_text = (TextView) findViewById(R.id.my_appprove_history_text);
		my_approve_text.setText("完成任务");
		my_appprove_history_text.setText("汇报详情");
		// 汇报详情
		LinearLayout my_approve_history_bt = (LinearLayout) findViewById(R.id.my_approve_history_bt);
		my_approve_history_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (taskreport.toString().length()<3) {
				ToastManager.getInstance(context).showToast("无任务汇报情况！");
				return;
				}
					Intent intent = new Intent();
					intent.setClass(MyTaskListDetailsActivity.this,
							MyTaskReportDetailsActivity.class);
					intent.putExtra("taskreport",taskreport.toString());
					startActivity(intent);


			}
		});
		//完成情况
		final Builder builder = new AlertDialog.Builder(this);// 初始化审批dialog
		my_approve_bt = (LinearLayout) findViewById(R.id.my_approve_bt);
		if(Task_status.equals("1")){
			my_approve_bt.setVisibility(ViewGroup.GONE);
		}
		my_approve_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				LinearLayout dialog_approval = (LinearLayout) LayoutInflater
						.from(context).inflate(R.layout.dialog_complete_task,
								null);
				builder.setView(dialog_approval);
				final AlertDialog dialog_app = builder.create();// 初始化审批dialog
				dialog_app.show();
				dialog_app.getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
				dialog_app
						.getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				Button dialog_complete_button = (Button) dialog_approval
						.findViewById(R.id.dialog_complete_button);// 完成按钮
				final EditText complete_task_app_view = (EditText) dialog_approval
						.findViewById(R.id.complete_task_app_view);// 任务总结

				dialog_complete_button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// 完成任务
						String complete =complete_task_app_view.getText().toString().trim();
						if(complete.equals("")||complete.length()<1){
							ToastManager.getInstance(context).showToast("任务总结不能为空！");
							return;
						}
						String key = sp.getString(SPConstant.MY_TOKEN, "");
						AsyncHttpClientUtil Async = new AsyncHttpClientUtil();
						Dialog_Http_Util dialog_util = new Dialog_Http_Util();
						RequestParams param = new RequestParams();
						param.put("key", key);
						param.put("taskId",Task_id);
						param.put("type","report");
						param.put("reportNotes",complete);
						Dialog progressDialog = dialog_util.showWaitingDialog(
								MyTaskListDetailsActivity.this, "正在完成", false);
						Async.post(APIURL.APPFINISHTASK, context, param,
								new JsonHandlerFinishTask(MyTaskListDetailsActivity.this, progressDialog));
						dialog_app.dismiss();
					}
				});
				
			}
		});
//		汇报情况
		final AsyncHttpClientUtil async = new AsyncHttpClientUtil();
		final Dialog_Http_Util dialog_util = new Dialog_Http_Util();
		my_approve_report_bt = (LinearLayout) findViewById(R.id.my_approve_report_bt);
		my_approve_report_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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
				dialog_task_title.setText(Task_name);

				final EditText dialog_mytask_content = (EditText) dialog_write_tasklinear
						.findViewById(R.id.dialog_mytask_content);// 日志内容

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
								String date = DateUtil.getCurrentTimeStr();
								if (content.trim().equals("")) {
									ToastManager.getInstance(context)
											.showToastcenter("汇报内容不能为空！");
									return;
								}
								RequestParams param = new RequestParams();
								param.put("key", key);
								param.put("taskId", Task_id);
//								param.put("workDate", date);
								param.put("type", "report");
								param.put("reportId", report_id);
								param.put("reportName", title);
								param.put("reportNotes", content);
								Dialog progressDialog = dialog_util
										.showWaitingDialog(MyTaskListDetailsActivity.this,
												"正在刷新", false);
								async.post(
										APIURL.TASKREPORT,
										context,
										param,
										new JsonHandlerreport(context, progressDialog));
								dialog_app.dismiss();
							}
						});

			}
		});
		
	}
	
	// 加班审批回调类
	private class JsonHandlerreport extends MJsonHttpHandler {
		Dialog progressDialog;

		protected JsonHandlerreport(Context context, Dialog progressDialog) {
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
			if (!response.isNull("errorMsg")) {
				try {
					ToastManager.getInstance(context).showToast(
							response.getString("errorMsg"));
					stopService(new Intent()
							.setAction("com.meiyin.services.Meiyinservice"));
					startActivity(new Intent().setClass(context, Login.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					Intent intent = new Intent();
					MyTaskListDetailsActivity.this.setResult(1, intent);
					MyTaskListDetailsActivity.this.finish();
					BaseApplication.getInstance().AppExit();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			try {
				if (response.getString("message").equals("success")) {
					ToastUtil.showToast(context, "汇报成功！");
					http();
				} else {
					ToastUtil.showToast(context, "汇报失败！");

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
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
			Log.e("lyt", response.toString());
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
				String edittask=response.getString("edittask");
				String operator=response.getString("operator");
				if(operator.equals("1")&&Task_status.equals("0")){
					my_approve_report_bt.setVisibility(ViewGroup.VISIBLE);	
				}
				my_approve_bt.setVisibility(ViewGroup.VISIBLE);
				if(edittask.equals("1")&&Task_status.equals("0")){
					my_approve_bt.setVisibility(ViewGroup.VISIBLE);
					}else{
					my_approve_bt.setVisibility(ViewGroup.GONE);
				}
				JSONObject returndto=(JSONObject) response.get("returndto");
				JSONArray taskfinishReport = returndto.getJSONArray("taskfinishReport");
				String report_notes="";
				String report_time="";
				for (int i = 0; i < taskfinishReport.length(); i++) {
					JSONObject enty = taskfinishReport.getJSONObject(i);
					report_notes =report_notes+enty.getString("report_notes");
					report_time =report_time+ enty.getString("report_time");

				}
				if(taskfinishReport.length()>0&&Task_status.equals("1")){
					mytask_details_summ_linear.setVisibility(ViewGroup.VISIBLE);
					mytask_details_summ_view.setVisibility(ViewGroup.VISIBLE);					
					mytask_details_summ.setText(report_notes+"\n"+DateUtil.getEnglishTime(report_time));
				}


					taskreport = returndto.getJSONArray("taskReport");
					if(taskreport.toString().length()>2){
					JSONObject enty = taskreport.getJSONObject(0);
					report_id=enty.getString("report_id");
					}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	private class JsonHandlerFinishTask extends MJsonHttpHandler {
		Dialog progressDialog;
		
		protected JsonHandlerFinishTask(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
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
					Intent intent = new Intent();
					intent.putExtra("bool", true);
					MyTaskListDetailsActivity.this.setResult(11, intent);
					MyTaskListDetailsActivity.this.finish();
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean back = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra("bool", false);
			MyTaskListDetailsActivity.this.setResult(11, intent);
			MyTaskListDetailsActivity.this.finish();
			back = false;
		}
		return back;

	}
}
