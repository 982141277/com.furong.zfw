package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.meiyin.erp.util.FileUtil;
import com.meiyin.erp.util.LogUtil;
import com.meiyin.erp.util.ToastUtil;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;
import com.my.android.library.ToastManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @param请假单详细
 * @Time 2016-5-12
 */
public class LeaveDetailsActivity extends Activity {
	private Context context;
	private ViewHolder holder;
	private ArrayList<Memu_History> memu;
	private AlertDialog dialog;
	private LinearLayout my_approve_bt;
	private ImageView leave_typeimg;
	private int app_atate = 3;// 审批(批准、否决)选项
	private String userid, state, leave_type;// 审批所需参数
	private String leave_times = "";// 系统时间
	private ImageView material_leave_image;
	private String uristring;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.leave_main);
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
		leave_typeimg = (ImageView) findViewById(R.id.leave_typeimg);
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
	 * 初始化VIEW
	 */
	private void initView() {
		// TODO Auto-generated method stub
		holder = new ViewHolder();
		// 请假详情
		holder.leave_applicant = (TextView) findViewById(R.id.leave_applicant);// 申请人
		holder.leave_names = (TextView) findViewById(R.id.leave_names);// 请假代班人
		holder.leave_time = (TextView) findViewById(R.id.leave_time);// 请假时间
		holder.leave_need_time = (TextView) findViewById(R.id.leave_need_time);// 请假时长
		holder.leave_cause = (TextView) findViewById(R.id.leave_cause);// 请假原因
		holder.leave_types = (TextView) findViewById(R.id.leave_types);// 请假类型
		holder.leave_material = (TextView) findViewById(R.id.my_approve_text);// 提交材料

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
				if (holder.leave_material.getText().toString().equals("提交材料")) {
					ToastManager.getInstance(context)
					.showToastcenter("手机端暂不能提交证明材料，请网页端提交证明材料！");
					return;
//					LinearLayout dialog_approval = (LinearLayout) LayoutInflater
//							.from(context).inflate(
//									R.layout.dialog_leave_material, null);
//					builder.setView(dialog_approval);
//					final AlertDialog dialog_app = builder.create();// 初始化审批dialog
//					dialog_app.show();
//					dialog_app.getWindow().clearFlags(
//							WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//					dialog_app
//							.getWindow()
//							.setSoftInputMode(
//									WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//					material_leave_image = (ImageView) dialog_approval
//							.findViewById(R.id.material_leave_image);
//					Button button_material = (Button) dialog_approval
//							.findViewById(R.id.button_material);
//					Button dialog_leave_sbt = (Button) dialog_approval
//							.findViewById(R.id.dialog_leave_sbt);// 审批按钮
//					button_material.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View arg0) {
//							// TODO Auto-generated method stub
//							Intent intent = new Intent();
//							intent.setType("image/*");
//							/* 使用Intent.ACTION_GET_CONTENT这个Action */
//							intent.setAction(Intent.ACTION_GET_CONTENT);
//							/* 取得相片后返回本画面 */
//							startActivityForResult(intent, 1);
//
//						}
//					});
//					/*
//					 * 提交证明
//					 */
//					dialog_leave_sbt.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View arg0) {
//							// TODO Auto-generated method stub
//							LinearLayout shenpi = (LinearLayout) LayoutInflater
//									.from(context).inflate(
//											R.layout.submit_dialog, null);
//							TextView heads = (TextView) shenpi
//									.findViewById(R.id.heads);
//							Button button_t = (Button) shenpi
//									.findViewById(R.id.button_t);
//							Button button_f = (Button) shenpi
//									.findViewById(R.id.button_f);
//							heads.setText("您确定要提交此证明材料吗？");
//							dialog_app.show();
//							dialog_app.getWindow().setContentView(shenpi);
//							button_t.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View arg0) {
//									// 提交证明材料
//									RequestParams param = new RequestParams();
//									File files = new File(
//											FileUtil.APP_BUF_ROOT_DIR
//													+ "/material.JPEG");
//									try {
//										File file = new File(
//												FileUtil.GetFileRootPath(
//														getApplicationContext())
//														.toString()
//														+ "/" + files.getName());
//
//										if (!file.exists()) {
//											file.createNewFile();
//										}
//										BufferedOutputStream bufferedOutputStream;
//										bufferedOutputStream = new BufferedOutputStream(
//												new FileOutputStream(file));
//										BitmapFactory
//												.decodeFile(
//														FileUtil.APP_BUF_ROOT_DIR +"/material.JPEG")
//												.compress(
//														Bitmap.CompressFormat.JPEG,
//														100,
//														bufferedOutputStream);
//										bufferedOutputStream.flush();
//										bufferedOutputStream.close();
//										param.put("attachment", file);
//									} catch (IOException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									} catch (Exception e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//									param.put("app", 59);
//									param.put("taskId", TaskId);
//									param.put("key", key);
//									param.put("applyNameState", applyNameState);
//									param.put("applyId", applyId);
//									param.put("id", applyId);
//									param.put("proc_inst_id", procInstId);
//									param.put("userid", userid);
//									param.put("state", state);
//									param.put("app_state", app_atate);
//									param.put("descId", "userSelfInnerTime");
//									param.put("app_view", "材料说米了");
//
//									param.put("leaveType", leave_type);
//									param.put("leave_times", leave_times);
//									LogUtil.e("lyt", "app=" + app_atate
//											+ "taskId=" + TaskId + ",id="
//											+ applyId + ",applyNameState="
//											+ applyNameState + ",proc_inst_id="
//											+ procInstId + ",userid=" + userid
//											+ ",state=" + state + ",app_state="
//											+ app_atate + ",leaveType="
//											+ leave_type + ",leave_times="
//											+ leave_times);
//									Dialog progressDialog = dialog_util
//											.showWaitingDialog(
//													LeaveDetailsActivity.this,
//													"正在提交", false);
//									async.post(APIURL.CHECK.ADDLEAVEMATERAIL,
//											context, param,
//											new JsonHandlerApproval(
//													LeaveDetailsActivity.this,
//													progressDialog));
//									dialog_app.dismiss();
//								}
//							});
//
//							button_f.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View arg0) {
//									// TODO Auto-generated method stub
//									dialog_app.dismiss();
//								}
//							});
//						}
//					});
				} else {
					// 审核dialog
					LinearLayout dialog_approval = (LinearLayout) LayoutInflater
							.from(context).inflate(R.layout.dialog_approval,
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
							final String appview = app_view.getText()
									.toString();
							if (app_atate == 4 && appview.equals("")) {
								ToastUtil.showToast(context, "请先填写审批意见！");
								return;
							}
							LinearLayout shenpi = (LinearLayout) LayoutInflater
									.from(context).inflate(
											R.layout.submit_dialog, null);
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
									param.put("app", app_atate);
									param.put("taskId", TaskId);
									param.put("key", key);
									param.put("applyNameState", applyNameState);
									param.put("applyId", applyId);
									param.put("id", applyId);
									param.put("proc_inst_id", procInstId);
									param.put("userid", userid);
									param.put("state", state);
									param.put("app_state", app_atate);
									param.put("app_view", appview);
									param.put("leaveType", leave_type);
									param.put("leave_times", leave_times);
									param.put("reality_start_time", "");
									LogUtil.e("lyt", "app=" + app_atate
											+ "taskId=" + TaskId + ",id="
											+ applyId + ",applyNameState="
											+ applyNameState + ",proc_inst_id="
											+ procInstId + ",userid=" + userid
											+ ",state=" + state + ",app_state="
											+ app_atate + ",app_view="
											+ appview + ",leaveType="
											+ leave_type + ",leave_times="
											+ leave_times);
									Dialog progressDialog = dialog_util
											.showWaitingDialog(
													LeaveDetailsActivity.this,
													"正在审批", false);
									async.post(APIURL.CHECK.ADDLEAVEPROVE,
											context, param,
											new JsonHandlerApproval(
													LeaveDetailsActivity.this,
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
			}
		});
	}

	// 请假单详情回调类
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
			LogUtil.e("lyt", response.toString());
			progressDialog.dismiss();
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
			String replace_user = "";// 请假代班人
			String start_time = "";// 开始时间
			String end_time = "";// 结束时间
			String reality_leave_times = "";// 修正时长
			String leave_reason = "";// 请假原因
			String descId = "";
			try {
				replace_user = response.getString("replace_user");
				start_time = response.getString("start_time");
				end_time = response.getString("end_time");
				leave_times = response.getString("leave_times");
				reality_leave_times = response.getString("reality_leave_times");
				leave_reason = response.getString("leave_reason");
				state = response.getString("state");
				userid = response.getString("userid");// userId
				leave_type = response.getString("leave_type");// leave_type
				applyHistory = (JSONArray) response.get("applyHistory");
				descId = response.getString("descId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String typeString = "";
			if (leave_type.equals("1")) {
				typeString = "事假";
			} else if (leave_type.equals("2")) {
				typeString = "调休";
			} else if (leave_type.equals("3")) {
				typeString = "病假";
			} else if (leave_type.equals("4")) {
				typeString = "婚假";
			} else if (leave_type.equals("5")) {
				typeString = "产假";
			} else if (leave_type.equals("6")) {
				typeString = "陪产假";
			} else if (leave_type.equals("7")) {
				typeString = "其他";
			}
			holder.leave_types.setText(typeString);
			holder.leave_names.setText(replace_user);
			holder.leave_time.setText(start_time + "至\n" + end_time);
			holder.leave_need_time.setText("修正时长："
					+ Integer.parseInt(reality_leave_times) / 60 + "小时"
					+ Integer.parseInt(reality_leave_times) % 60 + "分"
					+ "\n系统时长：" + Integer.parseInt(leave_times) / 60 + "小时"
					+ Integer.parseInt(leave_times) % 60 + "分");
			holder.leave_cause.setText(leave_reason);
			memu = new Gson().fromJson(applyHistory.toString(),
					new TypeToken<List<Memu_History>>() {
					}.getType());// 审批历史
			try {
				if (response.isNull("createUserName")) {
					String user = memu.get(memu.size() - 1).getAppUser();
					dept = memu.get(memu.size() - 1).getDept();
					holder.leave_applicant.setText(user + "(" + dept + ")");
				} else {
					createUserName = response.getString("createUserName");
					holder.leave_applicant.setText(createUserName);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null != state && state.equals("2")) {
				String App_state = memu.get(0).getApp_state();
				if (App_state.equals("4")) {
					leave_typeimg.setImageResource(R.mipmap.veto);
					leave_typeimg.setVisibility(ViewGroup.VISIBLE);
					// 否决
				} else {
					if (!App_state.equals("6")) {
						leave_typeimg.setImageResource(R.mipmap.complete);
						leave_typeimg.setVisibility(ViewGroup.VISIBLE);
					}
				}

			}
			if (descId.equals("deptGenManager") || descId.equals("GenManager")
					|| descId.equals("deptManager")
					|| descId.equals("genManager")) {
			} else if (descId.equals("userSelfInnerTime")) {
				// {staffCode=cac862bde04a424baa5c895efedfe023, taskId=373182,
				// app=59, proc_inst_id=373153, leave_times=450,
				// descId=userSelfInnerTime, state=1, userid=132, leaveType=3,
				// apply_id=752, id=752, applyNameState=10,
				// createUser=000000132, app_view=哥哥哥哥哥哥哥哥哥}
				holder.leave_material.setText("提交材料");
			} else if (descId.equals("serverStaff2")
					|| descId.equals("serverStaff")) {
				my_approve_bt.setVisibility(ViewGroup.GONE);
				ToastManager.getInstance(context)
						.showToastcenter("暂不能使用！");
			}
		}

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
					LeaveDetailsActivity.this.finish();
				} else {
					ToastUtil.showToast(context, "审批失败！");

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static class ViewHolder {
		private TextView leave_applicant;// 申请人
		private TextView leave_names;// 请假代班人
		private TextView leave_time;// 请假时间
		private TextView leave_need_time;// 请假时长
		private TextView leave_cause;// 请假原因
		private TextView leave_types;// 请假类型
		private TextView leave_material;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			uristring = uri.toString();
			ContentResolver cr = context.getContentResolver();
			Bitmap bm = null;
			try {
				bm = BitmapFactory.decodeStream(cr.openInputStream(uri));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FileUtil.saveBitmap(bm, "material");
			ImageLoader(uri.toString(), material_leave_image);
		}
	}

	public void ImageLoader(String image, ImageView img) {

		ImageLoader.getInstance().displayImage(image, img,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						LogUtil.e("uri", "imageUriStart:" + imageUri);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						LogUtil.e("uri", "imageUriFailed:" + imageUri
								+ "--fails--" + failReason);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {

					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {

					}
				});
	}
}
