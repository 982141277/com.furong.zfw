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
import android.util.Log;
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
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.Memu_History_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Memu_History;
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

import java.util.ArrayList;
import java.util.List;


/**
 * @param费用申请单详情
 * @Time 2016-6-14
 */
public class ExpenseDetailsActivity extends Activity {

	private Context context;
	private ViewHolder holder;
	private ArrayList<Memu_History> memu;
	private AlertDialog dialog;
	private LinearLayout my_approve_bt;
	private ImageView expense_typeimg;
	private int app_atate = 3;// 审批(批准、否决)选项
	private String userid, state, descId, staffCode;// 审批所需参数
	private String expenseType = "";// 费用申请类型;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.expensedetails_main);
		context = getApplicationContext();
		SharedPreferences sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);

		/*
		 * 初始化
		 */
		initHeader();
		initFools();
		initData(sp);
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
		headtitletext.setText("费用申请单");
	}
	/*
	 * 初始化尾部UI
	 */
	private void initFools() {
		// 审批历史、审核
		LinearLayout my_approve_history_bt = (LinearLayout) findViewById(R.id.my_approve_history_bt);
		my_approve_history_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (memu.size() < 1) {
					ToastManager.getInstance(context).showToast("网络连接失败！");
					return;
				}
				LinearLayout list_dialog = (LinearLayout) LayoutInflater.from(
						context).inflate(R.layout.list_dialog, null);
				Memu_History_Adapter adapter = new Memu_History_Adapter(context);
				ListView history_list = (ListView) list_dialog
						.findViewById(R.id.history_list);
				adapter.setList(memu);
				history_list.setAdapter(adapter);
				dialog.show();
				dialog.getWindow().setContentView(list_dialog);
			}
		});
		my_approve_bt = (LinearLayout) findViewById(R.id.my_approve_bt);

	}
	/*
	 * 初始化数据
	 */
	private void initData(SharedPreferences sp) {
		// TODO Auto-generated method stub
		AsyncHttpClientUtil async = new AsyncHttpClientUtil();
		Dialog_Http_Util dialog_util = new Dialog_Http_Util();
		memu = new ArrayList<Memu_History>();// 审批历史数据
		dialog = new AlertDialog.Builder(this).create();// 审批历史dialog
		Builder builder = new AlertDialog.Builder(this);// 初始化审批dialog
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		expense_typeimg = (ImageView) findViewById(R.id.expense_typeimg);
		/*
		 * 查询详情所需要参数
		 */
		String applyNameState = getIntent().getStringExtra("applyNameState");
		String applyId = getIntent().getStringExtra("applyId");
		String procInstId = getIntent().getStringExtra("procInstId");
		String descId = getIntent().getStringExtra("descId");
		String TaskId = getIntent().getStringExtra("TaskId");
		String name = getIntent().getStringExtra("out");
		RequestParams param = new RequestParams();
		if (name.equals("申请表单")) {
			param.put("key", key);
			param.put("applyNameState", applyNameState);
			param.put("applyId", applyId);
			param.put("procInstId", procInstId);
			my_approve_bt.setVisibility(ViewGroup.GONE);
		} else if (name.equals("待审批事项")) {
			param.put("taskId", TaskId);
			param.put("key", key);
			param.put("applyNameState", applyNameState);
			param.put("applyId", applyId);
			param.put("procInstId", procInstId);
			param.put("isFresh", true);
			param.put("descId", descId);
			my_approve_bt.setVisibility(ViewGroup.VISIBLE);
			// 待审批审批接口
			approval(builder, async, TaskId, key, applyNameState, applyId,
					procInstId, dialog_util);
		} else if (name.equals("已审批事项")) {
			param.put("key", key);
			param.put("applyNameState", applyNameState);
			param.put("applyId", applyId);
			param.put("procInstId", procInstId);
			param.put("taskId", TaskId);
			param.put("isFresh", true);
			my_approve_bt.setVisibility(ViewGroup.GONE);
		}
		Dialog progressDialog = dialog_util.showWaitingDialog(this, "正在刷新",
				false);
		async.post(APIURL.CHECK.MEMU_SQAPI, context, param, new JsonHandler(
				context, progressDialog));
	}

	public static class ViewHolder {
		private TextView expense_applicant;// 申请人
		private TextView expense_type;// 申请费用类型
		private TextView expense_typename;// 申请费用名称
		private TextView expense_money;// /申请金额
		private TextView expense_cause;// 申请原因
		private TextView expense_type_names;// 申请费用类型名称
		private LinearLayout expense_linear;// 申请费用类型名称父布局
		private View expense_imgs;// 下划线
	}

	/*
	 * 初始化VIEW
	 */
	private void initView() {
		// TODO Auto-generated method stub
		holder = new ViewHolder();
		// 请假详情
		holder.expense_applicant = (TextView) findViewById(R.id.expense_applicant);// 申请人
		holder.expense_type = (TextView) findViewById(R.id.expense_type);// 申请费用类型
		holder.expense_typename = (TextView) findViewById(R.id.expense_typename);// 申请费用名称
		holder.expense_money = (TextView) findViewById(R.id.expense_money);// 申请金额
		holder.expense_cause = (TextView) findViewById(R.id.expense_cause);// 申请原因
		holder.expense_type_names = (TextView) findViewById(R.id.expense_type_names);// 申请费用类型名称
		holder.expense_linear = (LinearLayout) findViewById(R.id.expense_linear);// 申请费用类型名称父布局
		holder.expense_imgs = (View) findViewById(R.id.expense_imgs);// 下划线
	}

	// 费用申请单详情回调类
	private class JsonHandler extends MJsonHttpHandler {
		private Dialog progressDialog;

		protected JsonHandler(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			// TODO Auto-generated constructor stub
			this.progressDialog = progressDialog;
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			progressDialog.dismiss();
			String ss = response.toString();
			Log.e("lyt", ss);
			if (!response.isNull("errorMsg")) {
				try {
					ToastManager.getInstance(context).showToast(
							response.getString("errorMsg"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			JSONArray applyHistory = null;// 历史审批
			String createUserName = "";// 申请人
			String dept = "";// 所属部门
			String applyMount = "";// 申请金额
			String phoneNum = "";// 电话号码
			String applyReason = "";// 申请原因
			String plateNum = "";// 车牌号码

			try {
				createUserName = response.getString("createUserName");
				dept = response.getString("dept");
				applyMount = response.getString("applyMount");
				phoneNum = response.getString("phoneNum");
				applyReason = response.getString("applyReason");
				expenseType = response.getString("expenseType");
				plateNum = response.getString("plateNum");
				applyHistory = (JSONArray) response.get("applyHistory");
				state = response.getString("state");
				userid = response.getString("userid");
				descId = response.getString("descId");
				staffCode = response.getString("staffCode");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			holder.expense_applicant.setText(createUserName + "(" + dept + ")");
			if (expenseType.equals("话费充值")) {
				holder.expense_type.setText("话费充值");
				holder.expense_type_names.setText("电话号码：");
				holder.expense_typename.setText(phoneNum);
			} else if (expenseType.equals("油卡充值")) {
				holder.expense_type.setText("油卡充值");
				holder.expense_type_names.setText("车牌号码：");
				holder.expense_typename.setText(plateNum);
			} else if (expenseType.equals("车辆保养")) {
				holder.expense_type.setText("车辆保养");
				holder.expense_type_names.setText("车牌号码：");
				holder.expense_typename.setText(plateNum);
			} else if (expenseType.equals("其他")) {
				holder.expense_type.setText("其他");
				holder.expense_linear.setVisibility(ViewGroup.GONE);
				holder.expense_imgs.setVisibility(ViewGroup.GONE);
			}

			holder.expense_money.setText(applyMount + "元");
			holder.expense_cause.setText(applyReason);
			// 审批历史
			memu = new Gson().fromJson(applyHistory.toString(),
					new TypeToken<List<Memu_History>>() {
					}.getType());// 审批历史
			if (null != state && state.equals("2")) {
				String App_state = memu.get(0).getApp_state();
				if (App_state.equals("4")) {
					expense_typeimg.setImageResource(R.mipmap.veto);
					expense_typeimg.setVisibility(ViewGroup.VISIBLE);
					// 否决
				} else {
					if (!App_state.equals("6")) {
						expense_typeimg.setImageResource(R.mipmap.complete);
						expense_typeimg.setVisibility(ViewGroup.VISIBLE);
					}
				}

			}
		}

	}

	/*
	 * 审批接口
	 * 
	 * @参数
	 */
	private void approval(final Builder builder,
			final AsyncHttpClientUtil async, final String TaskId,
			final String key, final String applyNameState,
			final String applyId, final String procInstId,
			final Dialog_Http_Util dialog_util) {

		my_approve_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 审核dialog
				LinearLayout dialog_approval = (LinearLayout) LayoutInflater
						.from(context).inflate(R.layout.dialog_approval, null);
				builder.setView(dialog_approval);
				final AlertDialog dialog_app = builder.create();// 初始化审批dialog
				dialog_app.show();
				dialog_app.getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
				dialog_app.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				Button approval = (Button) dialog_approval
						.findViewById(R.id.dialog_approval);// 审批按钮
				final EditText app_view = (EditText) dialog_approval
						.findViewById(R.id.app_view);// 审批意见
				RadioGroup dialog_radio_group = (RadioGroup) dialog_approval
						.findViewById(R.id.dialog_radio_group);// (批准、否决)选项
				final TextView dialog_star = (TextView) dialog_approval
						.findViewById(R.id.dialog_star);// 是否必填
				/*
				 * 审批(批准、否决)选项
				 */
				dialog_radio_group
						.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(RadioGroup arg0,
									int arg1) {
								// TODO Auto-generated method stub
								if (arg0.getId() == R.id.dialog_radio_group) {
									switch (arg1) {
									case R.id.rgs_1:
										dialog_star
												.setVisibility(ViewGroup.GONE);
										app_atate = 3;
										break;
									case R.id.rgs_2:
										dialog_star
												.setVisibility(ViewGroup.VISIBLE);
										app_atate = 4;
										break;
									default:
										break;
									}
								}
							}
						});
				/*
				 * 审批事件
				 */
				approval.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						final String appview = app_view.getText().toString();
						if (app_atate == 4 && appview.equals("")) {
							ToastUtil.showToast(context, "请先填写审批意见！");
							return;
						}
						LinearLayout shenpi = (LinearLayout) LayoutInflater
								.from(context).inflate(R.layout.submit_dialog,
										null);
						TextView heads = (TextView) shenpi
								.findViewById(R.id.heads);
						Button button_t = (Button) shenpi
								.findViewById(R.id.button_t);
						Button button_f = (Button) shenpi
								.findViewById(R.id.button_f);
						heads.setText("您确定要提交审核吗？");
						dialog_app.show();
						dialog_app.getWindow().setContentView(shenpi);
						button_t.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// 提交审批
								RequestParams param = new RequestParams();
								param.put("app", app_atate);// 审批选型
								param.put("taskId", TaskId);// taskid
								param.put("key", key);
								param.put("applyNameState", applyNameState);// 类型code
								param.put("applyId", applyId);// 申请单id
								param.put("id", applyId);// 申请单id
								param.put("proc_inst_id", procInstId);// ..
								param.put("userid", userid);// id
								param.put("state", state);// 审批流程
								param.put("app_state", app_atate);// 审批选型
								param.put("app_view", appview);// 审批意见
								param.put("expenseType", expenseType);//
								if (!descId.equals("deptGenManager")
										|| !descId.equals("GenManager")
										|| !descId.equals("genManager")) {
									param.put("staffCode", staffCode);
									param.put("descId", descId);
									param.put("createUser", userid);
								}
								Dialog progressDialog = dialog_util
										.showWaitingDialog(
												ExpenseDetailsActivity.this,
												"正在审批", false);
								async.post(APIURL.CHECK.EXPENSEPROVE, context,
										param, new JsonHandlerApproval(
												ExpenseDetailsActivity.this,
												progressDialog));
								dialog_app.dismiss();
							}
						});

						button_f.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								dialog_app.dismiss();
							}
						});
					}
				});
			}
		});
	}

	// 请假审批回调类
	private class JsonHandlerApproval extends MJsonHttpHandler {
		Dialog progressDialog;

		protected JsonHandlerApproval(Context context, Dialog progressDialog) {
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
					System.exit(0);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			try {
				if (response.getString("message").equals("success")) {
					ToastUtil.showToast(context, "审批成功！");
					Intent intent = new Intent();
					intent.setAction(SPConstant.APPROVALACTION);
					sendBroadcast(intent);
					my_approve_bt.setVisibility(ViewGroup.GONE);
					ExpenseDetailsActivity.this.finish();
				} else {
					ToastUtil.showToast(context, "审批失败！");

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
