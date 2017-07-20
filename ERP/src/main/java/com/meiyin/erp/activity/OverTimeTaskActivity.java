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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.SimpleTreeAdapter;
import com.meiyin.erp.app.FileBean;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.bean.Node;
import com.meiyin.erp.bean.TreeListViewAdapter;
import com.meiyin.erp.datepicker.DatePick;
import com.meiyin.erp.entity.OverTimeTask_Entity;
import com.meiyin.erp.entity.Spinner_Entity;
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
import java.util.List;
/**
 * @param新增加班任务单
 * @Time 2016-5-13
 */
public class OverTimeTaskActivity extends Activity {
	private Context context;
	private SharedPreferences sp;
	private TextView overtimetask_start_time, overtimetask_end_time;
	private Dialog_Http_Util dialog_util;
	private AsyncHttpClientUtil mAsync;
	private EditText overtimetask_maincontent;
	private ListView mTree;
	private TextView overtimetask_people;
	private TreeListViewAdapter<FileBean> mAdapter;
	private List<FileBean> mDatas = new ArrayList<FileBean>();;
	private AlertDialog dialog_leixin;
	private RelativeLayout thisink;
	private ArrayList<OverTimeTask_Entity> mlist, overlist;
	private ArrayList<Spinner_Entity> spn;
	private String type="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.overtimetask_main);
		context = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		/*
		 * 初始化
		 */
		initHeader();
		initData();
	}

	/*
	 * 初始化标题UI
	 */
	private void initHeader() {
		ImageView headtitleimageView = (ImageView) findViewById(R.id.headtitleimageView);
		headtitleimageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TextView headtitletext = (TextView) findViewById(R.id.headtitletext);
		headtitletext.setText("加班任务单");
	}

	/*
	 * 初始化data
	 */
	private void initData() {
		// TODO Auto-generated method stub
		dialog_util = new Dialog_Http_Util();
		mAsync = new AsyncHttpClientUtil();
		/*
		 * 初始化所有人员
		 */
		String communication = sp.getString(SPConstant.COMMUNICATION, "");
		if (communication.equals("")) {
			Dialog_Http_Util dialog_util = new Dialog_Http_Util();
			AsyncHttpClientUtil mAsync = new AsyncHttpClientUtil();
			/*
			 * 初始化所有人员
			 */
			String key = sp.getString(SPConstant.MY_TOKEN, "");
			RequestParams param = new RequestParams();
			param.setContentEncoding("utf-8");
			param.put("key", key);
			Dialog progressDialog = dialog_util.showWaitingDialog(
					OverTimeTaskActivity.this, "正在刷新", true);
			mAsync.post(APIURL.COMPEOPLE, context, param, new OverTimeTaskJsonHandler(
					OverTimeTaskActivity.this, progressDialog));
		} else {
			frist(communication);
		}

		// 初始化弹窗
		dialog_leixin = new AlertDialog.Builder(this).create();
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		final AlertDialog dialog1 = new AlertDialog.Builder(this).create();
		overlist = new ArrayList<OverTimeTask_Entity>();
		overtimetask_start_time = (TextView) findViewById(R.id.overtimetask_start_time);
		overtimetask_end_time = (TextView) findViewById(R.id.overtimetask_end_time);
		overtimetask_start_time.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout date_dialog = (RelativeLayout) LayoutInflater
						.from(context).inflate(R.layout.date_dialog, null);
				date_dialog.addView(new DatePick(OverTimeTaskActivity.this,
						overtimetask_start_time).getDataPick());
				dialog.show();
				dialog.getWindow().setContentView(date_dialog);
				dialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						if (overtimetask_start_time.getText().toString()
								.equals("")) {
							overtimetask_start_time.setText(DateUtil
									.getCurrentTimeStr());
						}
					}
				});

			}
		});
		overtimetask_end_time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout date_dialog = (RelativeLayout) LayoutInflater
						.from(context).inflate(R.layout.date_dialog, null);
				date_dialog.addView(new DatePick(OverTimeTaskActivity.this,
						overtimetask_end_time).getDataPick());
				dialog1.show();
				dialog1.getWindow().setContentView(date_dialog);
				dialog1.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						if (overtimetask_end_time.getText().toString()
								.equals("")) {
							overtimetask_end_time.setText(DateUtil
									.getCurrentTimeStr());
						}
					}
				});
			}
		});
		overtimetask_people = (TextView) findViewById(R.id.overtimetask_people);
		ImageView overtime_delete_icon = (ImageView) findViewById(R.id.overtime_delete_icon);
		overtime_delete_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				overtimetask_people.setText("");
				overlist.clear();
			}
		});
		overtimetask_people.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				thisink = (RelativeLayout) LayoutInflater.from(context)
						.inflate(R.layout.leixing_main, null);
				mTree = (ListView) thisink.findViewById(R.id.id_tree);
				dialog_leixin.show();
				dialog_leixin.getWindow().setContentView(thisink);
				try {
					mAdapter = new SimpleTreeAdapter<FileBean>(mTree,
							OverTimeTaskActivity.this, mDatas, 0);
					mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
						@Override
						public void onClick(Node node, int position) {
							if (node.isLeaf()) {
								for (OverTimeTask_Entity fileEntity : mlist) {
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
				StringBuffer sb = new StringBuffer();
				for (OverTimeTask_Entity header : overlist) {
					sb.append(header.getName());
					sb.append("、");
				}
				overtimetask_people.setText(sb.toString());
			}
		});
		/*
		 * 获取要求加班人员
		 */
		String array = getIntent().getExtras().getString("fenlist");
		LinearLayout overtimetask_linear = (LinearLayout) findViewById(R.id.overtimetask_linear);
		View overtime_view = (View) findViewById(R.id.overtime_view);
		if (array.equals("")) {
			overtimetask_linear.setVisibility(ViewGroup.GONE);
			overtime_view.setVisibility(ViewGroup.GONE);
		} else {
			overtimetask_linear.setVisibility(ViewGroup.GONE);
			overtime_view.setVisibility(ViewGroup.GONE);
//			Spinner overtimespn = (Spinner) findViewById(R.id.overtimespn);
//			spn = new ArrayList<Spinner_Entity>();
//			spn = new Gson().fromJson(array,
//					new TypeToken<List<Spinner_Entity>>() {
//					}.getType());
//			Spinner_Adapter spinnerAdapter = new Spinner_Adapter(context);
//			spinnerAdapter.setList(spn);
//			overtimespn.setAdapter(spinnerAdapter);
//			overtimespn.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//				@Override
//				public void onItemSelected(AdapterView<?> arg0, View arg1,
//						int arg2, long arg3) {
//					// TODO Auto-generated method stub
//					type = spn.get(arg2).getType_id();
//				}
//
//				@Override
//				public void onNothingSelected(AdapterView<?> arg0) {
//					// TODO Auto-generated method stub
//
//				}
//			});
		}
		// 提交加班任务申请单
		overtimetask_maincontent = (EditText) findViewById(R.id.overtimetask_maincontent);
		Button overtime_submit = (Button) findViewById(R.id.overtime_submit);
		overtime_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (overlist.size() < 1) {
					ToastManager.getInstance(context)
							.showToastcenter("加班人员不能为空！");
					return;
				}
				final String stime = overtimetask_start_time.getText()
						.toString();
				final String etime = overtimetask_end_time.getText().toString();
				final String maincontent = overtimetask_maincontent.getText()
						.toString();

				if (stime.equals("") || etime.equals("")
						|| maincontent.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("必填项不能为空！");
					dialog.dismiss();
					return;
				}
				long starts = DateUtil.getLongFromStr(stime);
				long ends = DateUtil.getLongFromStr(etime);

				long time1 = ends-starts;
				long times1 = time1 / 1000 / 3600;
				if (times1 < 0)  {
					ToastManager.getInstance(context)
							.showToastcenter("结束时间错误，结束时间和开始时间混乱！");
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

						StringBuffer sb = new StringBuffer();
						for (OverTimeTask_Entity isEntity : overlist) {
							String accompanyId = isEntity.getUser_code();
							sb.append(accompanyId);
							sb.append(",");
						}
						RequestParams param = new RequestParams();
						param.setContentEncoding("utf-8");
						param.put("key", key);
						param.put("StartTime", stime);// 要求加班开始时间
						param.put("EndTime", etime);// 预计加班结束时间;
						param.put("detailAuth", type);// 加班任务审批人
						param.put("accompanyId",
								sb.substring(0, sb.length() - 1));// 要求加班人员
						param.put("mainContent", maincontent);// 加班事由
						param.put("orgId", "");
						param.put("stateMent",
								getIntent().getStringExtra("stateMent"));
						LogUtil.e("lyt", param.toString());
						Dialog progressDialog = dialog_util.showWaitingDialog(
								OverTimeTaskActivity.this, "正在刷新", false);
						mAsync.post(APIURL.CHECK.ADDNEWOVERTIMETASK, context,
								param, new JsonHandler(
										OverTimeTaskActivity.this,
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
	private void frist(String MIL) {
		mlist = new Gson().fromJson(MIL,
				new TypeToken<List<OverTimeTask_Entity>>() {
				}.getType());
		mDatas.clear();
		// 添加人员数据到Data
		ArrayList<Integer> s = new ArrayList<Integer>();
		for (OverTimeTask_Entity overTimeTask_Entity : mlist) {
			if(!overTimeTask_Entity.getName().equals("开福区")&&!overTimeTask_Entity.getName().equals("张主任")){

				mDatas.add(new FileBean(overTimeTask_Entity.getId(),
						overTimeTask_Entity.getPid(),
						overTimeTask_Entity.getName(), false));
			
			}
		}
	}
	// 加班任务单提交回调类
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
					System.exit(0);
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
					OverTimeTaskActivity.this.finish();
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

	// 人员信息
	private class OverTimeTaskJsonHandler extends MJsonHttpHandler {

		private Dialog progressDialog;

		protected OverTimeTaskJsonHandler(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			progressDialog.dismiss();
			if (!response.isNull("errorMsg")) {
				try {
					ToastManager.getInstance(context).showToast(
							response.getString("errorMsg"));
					stopService(new Intent()
							.setAction("com.meiyin.services.Meiyinservice"));
					startActivity(new Intent().setClass(context, Login.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					System.exit(0);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			try {
				JSONArray mli = response.getJSONArray("mlist");
				mlist = new Gson().fromJson(mli.toString(),
						new TypeToken<List<OverTimeTask_Entity>>() {
						}.getType());
				mDatas.clear();
				// 添加人员数据到Data
				for (OverTimeTask_Entity overTimeTask_Entity : mlist) {
					mDatas.add(new FileBean(overTimeTask_Entity.getId(),
							overTimeTask_Entity.getPid(), overTimeTask_Entity
									.getName(), false));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
