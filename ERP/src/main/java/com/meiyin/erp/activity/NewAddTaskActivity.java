package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.SimpleTreeAdapter;
import com.meiyin.erp.adapter.Spinner_Adapter;
import com.meiyin.erp.app.FileBean;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.BaseApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.bean.Node;
import com.meiyin.erp.bean.TreeListViewAdapter;
import com.meiyin.erp.datepicker.DatePick;
import com.meiyin.erp.entity.OverTimeTask_Entity;
import com.meiyin.erp.entity.Spinner_Entity;
import com.meiyin.erp.util.DateUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * @param新增任务
 * 
 * @Time 2016-11-9
 */
public class NewAddTaskActivity extends Activity{
	private SharedPreferences sp ;
	private Context context ;
	private String type;
	private AlertDialog dialog_leixin,dialog_leixin1;
	private List<FileBean> mDatas = new ArrayList<FileBean>();;
	private ArrayList<OverTimeTask_Entity> lists;
	private ArrayList<OverTimeTask_Entity> overlist,overlist1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_add_task_main_activity);
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
				Intent intent = new Intent();
				intent.putExtra("bool", false);
				int result=getIntent().getIntExtra("result", 13);
				NewAddTaskActivity.this.setResult(result, intent);
				NewAddTaskActivity.this.finish();
			}
		});
		TextView headtitletext = (TextView) findViewById(R.id.headtitletext);
		headtitletext.setText("新增任务");
		String mlist = getIntent().getStringExtra("mlist");
		lists = new Gson().fromJson(mlist,
				new TypeToken<List<OverTimeTask_Entity>>() {
				}.getType());
		//添加事件类型数据到Data
		for (int a = 0; a < lists.size(); a++) {
			mDatas.add(new FileBean(Integer.valueOf(lists.get(a).getId()),Integer.valueOf(lists.get(a).getPid()), lists.get(a).getName(),false));

		}
	}
	/*
	 * 初始化UI
	 */
	private void initView(){
		final LinearLayout new_add_task_linear = (LinearLayout) findViewById(R.id.new_add_task_linear);
		final View new_add_task_view = findViewById(R.id.new_add_task_view);
		Spinner newadd_task_spn = (Spinner) findViewById(R.id.newadd_task_spn);
		final ArrayList<Spinner_Entity> spnlist = new ArrayList<Spinner_Entity>();
		spnlist.add(new Spinner_Entity("自拟任务", "002"));
		spnlist.add(new Spinner_Entity("下达任务", "001"));
		// 任务类型
		Spinner_Adapter spinnerAdapter1 = new Spinner_Adapter(context);
		spinnerAdapter1.setList(spnlist);
		newadd_task_spn.setAdapter(spinnerAdapter1);
		newadd_task_spn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				type = spnlist.get(arg2).getType_id();
				if(type.equals("002")){
					new_add_task_linear.setVisibility(ViewGroup.GONE);
					new_add_task_view.setVisibility(ViewGroup.GONE);
				}else if(type.equals("001")){
					new_add_task_linear.setVisibility(ViewGroup.VISIBLE);
					new_add_task_view.setVisibility(ViewGroup.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		final EditText new_add_task_name = (EditText)findViewById(R.id.new_add_task_name);
		final EditText new_add_task_target = (EditText)findViewById(R.id.new_add_task_target);
		final EditText new_add_task_source = (EditText)findViewById(R.id.new_add_task_source);
		final EditText new_add_task_describe = (EditText)findViewById(R.id.new_add_task_describe);
		final TextView new_add_task_start_time = (TextView)findViewById(R.id.new_add_task_start_time);
		final TextView new_add_task_end_time = (TextView)findViewById(R.id.new_add_task_end_time);
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		final AlertDialog dialog1 = new AlertDialog.Builder(this).create();
		new_add_task_start_time.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout date_dialog = (RelativeLayout) LayoutInflater
						.from(context).inflate(R.layout.date_dialog, null);
				date_dialog.addView(new DatePick(NewAddTaskActivity.this,
						new_add_task_start_time).getDataPick());
				dialog.show();
				dialog.getWindow().setContentView(date_dialog);
				dialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						if (new_add_task_start_time.getText().toString()
								.equals("")) {
							new_add_task_start_time.setText(DateUtil
									.getCurrentTimeStr());
						}
					}
				});

			}
		});
		new_add_task_end_time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout date_dialog = (RelativeLayout) LayoutInflater
						.from(context).inflate(R.layout.date_dialog, null);
				date_dialog.addView(new DatePick(NewAddTaskActivity.this,
						new_add_task_end_time).getDataPick());
				dialog1.show();
				dialog1.getWindow().setContentView(date_dialog);
				dialog1.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						if (new_add_task_end_time.getText().toString()
								.equals("")) {
							new_add_task_end_time.setText(DateUtil
									.getCurrentTimeStr());
						}
					}
				});
			}
		});
		
		final TextView new_add_task_know = (TextView)findViewById(R.id.new_add_task_know);//任务知晓人
		ImageView new_add_task_know_delete = (ImageView)findViewById(R.id.new_add_task_know_delete);
		final TextView new_add_task_execute = (TextView)findViewById(R.id.new_add_task_execute);//任务执行人
		ImageView new_add_task_execute_delete = (ImageView)findViewById(R.id.new_add_task_execute_delete);
		dialog_leixin = new AlertDialog.Builder(this).create();
		dialog_leixin1 = new AlertDialog.Builder(this).create();
		overlist = new ArrayList<OverTimeTask_Entity>();//执行人
		overlist1 = new ArrayList<OverTimeTask_Entity>();//知晓人
		//执行人
		new_add_task_execute_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new_add_task_execute.setText("");
				overlist.clear();
			}
		});
		new_add_task_execute.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout thisink = (RelativeLayout) LayoutInflater.from(context)
						.inflate(R.layout.leixing_main, null);
				ListView mTree = (ListView) thisink.findViewById(R.id.id_tree);
				dialog_leixin.show();
				dialog_leixin.getWindow().setContentView(thisink);
				try {
					SimpleTreeAdapter<FileBean> mAdapter = new SimpleTreeAdapter<FileBean>(mTree,
							NewAddTaskActivity.this, mDatas, 0);
					mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
						@Override
						public void onClick(Node node, int position) {
							if (node.isLeaf()) {
								for (OverTimeTask_Entity fileEntity : lists) {
									if (fileEntity.getId() == node.getId()) {
										if (null == fileEntity.getUser_code()) {
											Toast.makeText(
													getApplicationContext(),
													"请选择公司职员！",
													Toast.LENGTH_SHORT).show();
										} else {
											for (OverTimeTask_Entity iterable_element : overlist) {
												if (iterable_element
														.getUser_code()
														.equals(fileEntity
																.getUser_code())) {
													overlist.remove(iterable_element);
												}
											}
											overlist.add(fileEntity);
											Toast.makeText(
													getApplicationContext(),
													node.getName(),
													Toast.LENGTH_SHORT).show();
											dialog_leixin.dismiss();
										}
									}
								}

							}
						}

					});

					mTree.setAdapter(mAdapter);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		});
		dialog_leixin.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
			if(overlist.size()>0){	
				StringBuffer sb = new StringBuffer();
				for (OverTimeTask_Entity header : overlist) {
					sb.append(header.getName());
					sb.append("、");
				}
				String taskknow=sb.toString();
				new_add_task_execute.setText(taskknow.substring(0, taskknow.length()-1));
			}else{
				new_add_task_know.setText("");	
			}
			}
		});
		//知晓人
		new_add_task_know_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new_add_task_know.setText("");
				overlist1.clear();
			}
		});
		new_add_task_know.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout thisink = (RelativeLayout) LayoutInflater.from(context)
						.inflate(R.layout.leixing_main, null);
				ListView mTree = (ListView) thisink.findViewById(R.id.id_tree);
				dialog_leixin1.show();
				dialog_leixin1.getWindow().setContentView(thisink);
				try {
					SimpleTreeAdapter<FileBean> mAdapter = new SimpleTreeAdapter<FileBean>(mTree,
							NewAddTaskActivity.this, mDatas, 0);
					mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
						@Override
						public void onClick(Node node, int position) {
							if (node.isLeaf()) {
								for (OverTimeTask_Entity fileEntity : lists) {
									if (fileEntity.getId() == node.getId()) {
										if (null == fileEntity.getUser_code()) {
											Toast.makeText(
													getApplicationContext(),
													"请选择公司职员！",
													Toast.LENGTH_SHORT).show();
										} else {
											for (OverTimeTask_Entity iterable_element : overlist1) {
												if (iterable_element
														.getUser_code()
														.equals(fileEntity
																.getUser_code())) {
													overlist1.remove(iterable_element);
												}
											}
											overlist1.add(fileEntity);
											Toast.makeText(
													getApplicationContext(),
													node.getName(),
													Toast.LENGTH_SHORT).show();
											dialog_leixin1.dismiss();
										}
									}
								}

							}
						}

					});

					mTree.setAdapter(mAdapter);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		});
		dialog_leixin1.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				StringBuffer sb = new StringBuffer();
				if(overlist1.size()>0){
				for (OverTimeTask_Entity header : overlist1) {
					sb.append(header.getName());
					sb.append("、");
				}
				String taskknow=sb.toString();
				new_add_task_know.setText(taskknow.substring(0, taskknow.length()-1));
				}else{
					new_add_task_know.setText("");	
				}
			}
		});
		Button new_add_submit_task = (Button)findViewById(R.id.new_add_submit_task);
		new_add_submit_task.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String taskName = new_add_task_name.getText().toString().trim();
				String taskTarget = new_add_task_target.getText().toString().trim();
				String source = new_add_task_source.getText().toString();
				String taskNotes = new_add_task_describe.getText().toString();
				String startTime=new_add_task_start_time.getText().toString();
				String endTime=new_add_task_end_time.getText().toString();
				if(taskName.equals("")){
					ToastManager.getInstance(context)
					.showToastcenter("任务名称不能为空！");
					return;
				}else if(startTime.equals("")){
					ToastManager.getInstance(context)
					.showToastcenter("请选择任务时间！");
					return;
				}else if(endTime.equals("")){
					ToastManager.getInstance(context)
					.showToastcenter("请选择任务时间！");
					return;
				}else if(taskNotes.equals("")){
					ToastManager.getInstance(context)
					.showToastcenter("任务描述不能为空！");
					return;
				}else if(overlist.size()<1&&type.equals("001")){
					ToastManager.getInstance(context)
					.showToastcenter("执行人员不能为空！");
					return;
				}
				StringBuffer sb = new StringBuffer();
				for (OverTimeTask_Entity header : overlist) {
					sb.append(header.getUser_code());
					sb.append(",");
				}
				StringBuffer sb1 = new StringBuffer();
				for (OverTimeTask_Entity header : overlist1) {
					sb1.append(header.getUser_code());
					sb1.append(",");
				}
				
				String key = sp.getString(SPConstant.MY_TOKEN, "");
				AsyncHttpClientUtil Async = new AsyncHttpClientUtil();
				Dialog_Http_Util dialog_util = new Dialog_Http_Util();
				RequestParams param = new RequestParams();
				param.put("key", key);
				param.put("taskName",taskName);//任务名称
				param.put("staffdo",new_add_task_execute);//执行人员名字
				param.put("accompanyId",sb.toString());//执行人员ID
				param.put("staffKnower_id",sb1.toString());//任务知晓人ID
				param.put("staffKnower",new_add_task_know);//任务知晓人
				param.put("taskNotes",taskNotes);//任务描述
				param.put("task_type2","请选择........");//任务名称二请选择
				param.put("task_type",type);//任务名称id
				param.put("startTime",startTime.substring(0, 10));//任务开始时间
				param.put("endTime",endTime.substring(0, 10));//任务结束时间
				param.put("taskTarget",taskTarget);//任务目标
				param.put("source",source);//任务资源
				param.put("problem_des","");//
				Dialog progressDialog = dialog_util.showWaitingDialog(
						NewAddTaskActivity.this, "正在保存", false);
				Log.e("lyt", param.toString());
				Async.post(APIURL.APPADDTASK, context, param,
						new JsonHandler(NewAddTaskActivity.this, progressDialog));
//				{staffdo=, staffKnower_id=5f0a1cc1c5e84cee923b3394e77cc3f5,,
//						taskNotes==%25E5%2587%25A0%25E4%25B8%25AAIEN%25E5%25AE%25B6%25E5%2585%25B3%25E9%2594%25AE%25E6%2598%25AF%25E7%259C%258B%25E6%2588%25BF%25E9%2597%25B4%25E5%25BC%2580%25E5%25A7%258B%25E7%259A%2584%25E8%25A7%2584%25E5%2588%2592%25E7%259A%2584%25E7%25A9%25BA%25E9%2597%25B4%25E6%258C%2589%25E6%2597%25B6%253Cbr/%253E%250A=1%25E5%259C%25A3%25E8%25AF%259E%25E8%258A%2582%25E7%2596%25AF%25E7%258B%2582%25E7%259A%2584%25E8%2580%2581%25E5%25B8%2588%25E6%2594%25BE%25E5%2581%2587%25E5%2595%258A%253Cbr/%253E%250A=2%25E3%2580%2581%25E7%259A%2584%25E5%2587%258F%25E8%2582%25A5%25E5%25BF%25AB%25E4%25B9%2590%25E7%259A%2584%25E9%25AA%2584%25E5%2582%25B2%25E6%2598%25AF%25E8%25A7%2582%25E7%259C%258B%25E4%25BA%2586%25E7%2594%25B5%25E8%25A7%2586%253Cbr/%253E,
//						task_type=002, task_type2=请选择........, 
//						endTime=2016-11-10, taskName=啊哈哦是东风科技, accompanyId=, startTime=2016-11-02, 
//						taskTarget=关节囧的关键, staffKnower=刘宇庭, source=设计费, problem_des=}
				//staffdo=刘宇庭, staffKnower_id=196858539ebc452a92ab9a13ffdfd25b,,
				//taskNotes=%25E5%2580%259A%25E5%25A4%25A9%25E4%25B8%2580%25E5%2587%25BA%25EF%25BC%258C%25E8%25B0%2581%25E4%25B8%258E%25E4%25BA%2589%25E9%2594%258B%25EF%25BC%2581%25EF%25BC%2581%25EF%25BC%2581%25EF%25BC%2581, task_type=001,
				//task_type2=请选择........, endTime=2016-11-11, taskName=呵呵呵呵,
				//accompanyId=5f0a1cc1c5e84cee923b3394e77cc3f5,, startTime=2016-11-1, 
				//taskTarget=所向披靡, staffKnower=周亮, source=倚天剑, problem_des=
			}
		});
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
				String message = response.getString("message");
				if (message.equals("success")) {
					ToastManager.getInstance(context)
							.showToastcenter("提交成功！");
					Intent intent = new Intent();
					int result=getIntent().getIntExtra("result", 13);
					intent.putExtra("bool", true);
					NewAddTaskActivity.this.setResult(result, intent);
					NewAddTaskActivity.this.finish();
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
			int result=getIntent().getIntExtra("result", 13);
			intent.putExtra("bool", false);
			NewAddTaskActivity.this.setResult(result, intent);
			NewAddTaskActivity.this.finish();
			back = false;
		}
		return back;

	}
}
