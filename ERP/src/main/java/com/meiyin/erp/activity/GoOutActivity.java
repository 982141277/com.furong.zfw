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
import com.meiyin.erp.entity.SellClientInfoEntity;
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
 * @author 新增出差申请单
 * @Time 2016-6-17
 */
public class GoOutActivity extends Activity {

	protected Context context;
	protected SharedPreferences sp;
	protected ArrayList<Spinner_Entity> spn1;
	protected String vehicletype;
	protected ArrayList<Spinner_Entity> ListType;
	protected TextView goout_clientName;
	protected String FID;
	protected String name;
	AsyncHttpClientUtil async;
	private Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.goout_main);
		context = getApplicationContext();
		activity=this;
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		/*
		 * 初始化
		 */
		initHeader();
		initView();
//		initdata();
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
		headtitletext.setText("出差申请单");
	}

	/*
	 * 初始化View
	 */
	private void initView() {
		// TODO Auto-generated method stub
		async = new AsyncHttpClientUtil();
		final Dialog_Http_Util dialog_util = new Dialog_Http_Util();
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		Spinner goout_Type = (Spinner) findViewById(R.id.goout_Type);// 出差类型
		final LinearLayout clientname_linear = (LinearLayout) findViewById(R.id.clientname_linear);// 所去客户

		ListType = new ArrayList<Spinner_Entity>();
		boolean sell = getIntent().getBooleanExtra("sell", false);
		if (!sell) {
			ListType.add(new Spinner_Entity("外出办事", "2"));
		} else {
			ListType.add(new Spinner_Entity("客户拜访", "1"));
			ListType.add(new Spinner_Entity("外出办事", "2"));
		}
		Spinner_Adapter spinnerAdapter1 = new Spinner_Adapter(context);
		spinnerAdapter1.setList(ListType);
		goout_Type.setAdapter(spinnerAdapter1);
		goout_Type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				name = ListType.get(arg2).getType_name();
				if (name.equals("客户拜访")) {
					clientname_linear.setVisibility(ViewGroup.VISIBLE);

				} else if (name.equals("外出办事")) {
					clientname_linear.setVisibility(ViewGroup.GONE);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		goout_clientName = (TextView) findViewById(R.id.goout_clientName);// 所去客户
		goout_clientName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setClass(context, ClientListActivity.class);
				intent.putExtra("result", SPConstant.TAGGOOUTACTIVITY);
				startActivityForResult(intent, SPConstant.TAGGOOUTACTIVITY);
			}
		});
		final EditText goout_mainContent = (EditText) findViewById(R.id.goout_mainContent);// 出差事由
		final TextView goout_toTime = (TextView) findViewById(R.id.goout_toTime);// 出发时间
		final TextView goout_backTime = (TextView) findViewById(R.id.goout_backTime);// 返回时间
		final TextView goout_days = (TextView) findViewById(R.id.goout_days);// 出差天数
		goout_toTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout date_dialog = (RelativeLayout) LayoutInflater
						.from(context).inflate(R.layout.date_dialog, null);
				date_dialog.addView(new DatePick(GoOutActivity.this,
						goout_toTime).getDataPick());
				dialog.show();
				dialog.getWindow().setContentView(date_dialog);
				goout_toTime.setText(DateUtil.getCurrentTimeStr());
				dialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						String goutback = goout_backTime.getText().toString();
						if (!goutback.equals("")) {
							long starts = DateUtil.getLongFromStr(goout_toTime
									.getText().toString());
							long ends = DateUtil.getLongFromStr(goutback);
							long time1 = ends - starts;
							long times1 = time1 / 1000 / 3600;
							if (times1 < 0) {
								ToastManager
										.getInstance(context).showToastcenter(
												"结束时间错误，结束时间必须晚于开始时间！");
							} else if (times1 < 24) {
								goout_days.setText(times1 + "小时");
							} else if (times1 > 24 && times1 < 168) {
								if (times1 % 24 == 0) {
									goout_days.setText(times1 / 24 + "天");
								} else {
									goout_days.setText(times1 / 24 + "天"
											+ times1 % 24 + "小时");
								}

							} else if (times1 > 168) {
								ToastManager
										.getInstance(context).showToastcenter(
												"外出时间不能超过7天，超过七天请另填申请单！");

							}
						}
					}
				});
			}
		});
		goout_backTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout date_dialog = (RelativeLayout) LayoutInflater
						.from(context).inflate(R.layout.date_dialog, null);
				date_dialog.addView(new DatePick(GoOutActivity.this,
						goout_backTime).getDataPick());
				dialog.show();
				dialog.getWindow().setContentView(date_dialog);
				goout_backTime.setText(DateUtil.getCurrentTimeStr());
				dialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						long starts = DateUtil.getLongFromStr(goout_toTime
								.getText().toString());
						long ends = DateUtil.getLongFromStr(goout_backTime
								.getText().toString());
						long time1 = ends - starts;
						long times1 = time1 / 1000 / 3600;
						if (times1 < 0) {
							ToastManager.getInstance(context)
									.showToastcenter("结束时间错误，结束时间必须晚于开始时间！");
						} else if (times1 < 24) {
							goout_days.setText(times1 + "小时");
						} else if (times1 > 24 && times1 < 168) {
							goout_days.setText(times1 / 24 + "天" + times1 % 24
									+ "小时");
						} else if (times1 > 168) {
							ToastManager.getInstance(context)
									.showToastcenter("外出时间不能超过7天，超过七天请另填申请单！");

						}
					}
				});

			}
		});

		Spinner goout_vehicle = (Spinner) findViewById(R.id.goout_vehicle);// 交通工具
		spn1 = new ArrayList<Spinner_Entity>();
		String vehicleList = getIntent().getExtras().getString("vehicleList");
		try {
			JSONArray arry = new JSONArray(vehicleList);
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
		Spinner_Adapter spinnerAdapter2 = new Spinner_Adapter(context);
		spinnerAdapter2.setList(spn1);
		goout_vehicle.setAdapter(spinnerAdapter2);
		goout_vehicle.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				vehicletype = spn1.get(arg2).getType_name();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		final EditText goout_predictLoan = (EditText) findViewById(R.id.goout_predictLoan);// 出差预借支
		final EditText goout_planTtask = (EditText) findViewById(R.id.goout_planTtask);// 计划任务
		// 提交出差申请单
		Button submit_gooutmemu = (Button) findViewById(R.id.submit_gooutmemu);
		submit_gooutmemu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				final String toTime = goout_toTime.getText().toString();
				final String backTime = goout_backTime.getText().toString();
				final String mainContent = goout_mainContent.getText()
						.toString();// 出差事由
				final String days = goout_days.getText().toString();// 出差天数
				final String predictLoan = goout_predictLoan.getText()
						.toString();// 出差预借支
				final String planTtask = goout_planTtask.getText().toString();// 计划任务
				if (toTime.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("出发时间不能为空！");
					dialog.dismiss();
					return;
				} else if (backTime.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("归来时间不能为空！");
					dialog.dismiss();
					return;
				} else if (mainContent.trim().equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("出差事由不能为空！");
					dialog.dismiss();
					return;
				} else if (days.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("出差天数不能为空！");
					dialog.dismiss();
					return;
				} else if (planTtask.trim().equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("计划任务不能为空！");
					dialog.dismiss();
					return;
				}
				long starts = DateUtil.getLongFromStr(toTime);
				long ends = DateUtil.getLongFromStr(backTime);
				long time1 = ends - starts;
				long times1 = time1 / 1000 / 3600;
				if (times1 < 0) {
					ToastManager.getInstance(context)
							.showToastcenter("结束时间错误，结束时间必须晚于开始时间！");
					dialog.dismiss();
					return;
				}
				if (planTtask.length() < 21) {
					ToastManager.getInstance(context)
							.showToastcenter("计划任务不能少于20个字！");
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
						param.put("orgId", "");// orgid
						param.put("currentTime", DateUtil.getCurrentTimeStr());// 新建时间
						param.put("backTime", backTime);// 返回时间
						param.put("clientFID", FID);// 客户资料
						param.put("days", days);// 出差天数
						param.put("planTtask", planTtask);// 计划任务
						param.put("Type", name);// 出差类型
						param.put("clientName", "");// 客户名称
						param.put("vehicle", vehicletype);// 交通工具
						param.put("userid", "");// userID
						param.put("user_id", "");// user_id
						param.put("predictLoan", predictLoan);// 出差预借支
						param.put("mainContent", mainContent);// 出差事由
						param.put("toTime", toTime);// 开始时间
						param.put("stateMent",
								getIntent().getStringExtra("stateMent"));// 申请单类型
						Dialog progressDialog = dialog_util.showWaitingDialog(
								GoOutActivity.this, "正在刷新", false);
						async.post(APIURL.CHECK.ADDNEWOVERTIMETASK, context,
								param, new JsonHandler(GoOutActivity.this,
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

	// 出差申请单提交回调类
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
					GoOutActivity.this.finish();
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case SPConstant.TAGGOOUTACTIVITY:
			Bundle bundle = data.getExtras();
			if (null != bundle) {
				@SuppressWarnings("unchecked")
				ArrayList<SellClientInfoEntity> mlist = (ArrayList<SellClientInfoEntity>) bundle
						.getSerializable("mlist");
				StringBuffer sb = new StringBuffer();
				StringBuffer sbs = new StringBuffer();
				for (SellClientInfoEntity sellClientInfoEntity : mlist) {
					sb.append(sellClientInfoEntity.getsClientName());
					sb.append("、");

					sbs.append(sellClientInfoEntity.getfID() + "||||"
							+ sellClientInfoEntity.getsClientName() + "||||"
							+ sellClientInfoEntity.getsClientWorkAddr());
					sbs.append("、");

				}
				int end = sbs.toString().lastIndexOf("、");
				FID = sbs.toString().substring(0, end);
				int ends = sb.toString().lastIndexOf("、");
				goout_clientName.setText(sb.toString().substring(0, ends));
			}
			break;

		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
