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
import com.meiyin.erp.adapter.OverTimeTaskPeople_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Memu_History;
import com.meiyin.erp.entity.OverTimeTaskPeople_Entity;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @param加班任务单详细
 * @Time 2016-7-1
 */
public class OverTimeTaskDetailsActivity extends Activity {
	private Context context;
	private ViewHolder holder;
	private ArrayList<Memu_History> memu;
	private AlertDialog dialog;
	private LinearLayout my_approve_bt;
	private ImageView overtime_typeimg;
	private int app_atate = 3;// 审批(批准、否决)选项;
	private String userid, state;// 审批所需参数
	private String descId = "";;
	private String accompanyId = "";// 要求加班人员
	private OverTimeTaskPeople_Adapter OTadapter ;
	private ArrayList<OverTimeTaskPeople_Entity>mlist;
	private int appstate = 3;// 审批(批准、否决)选项;
	private int mstate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.overtimetaskdetail_main);
		context = getApplicationContext();
		SharedPreferences sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);

		/*
		 * 初始化
		 */
		initFools();
		initData(sp);
		initHeader();
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
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		Builder builder = new AlertDialog.Builder(this);// 初始化审批dialog
		overtime_typeimg = (ImageView) findViewById(R.id.overtime_typeimg);
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
			param.put("page", "showApplyTaskList");
			param.put("descId", descId);
			my_approve_bt.setVisibility(ViewGroup.VISIBLE);
			// 待审批审批接口
			approval(builder, async, TaskId, key, applyNameState, applyId,
					procInstId, dialog_util, descId);
		} else if (name.equals("已审批事项")) {
			param.put("key", key);
			param.put("applyNameState", applyNameState);
			param.put("applyId", applyId);
			param.put("procInstId", procInstId);
			param.put("taskId", TaskId);
			param.put("isFresh", true);
			my_approve_bt.setVisibility(ViewGroup.GONE);
		}
		Log.e("lyt", param.toString());
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
		headtitletext.setText("加班任务单");
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
		// 加班任务详情
		holder.overtask_detail_applicant = (TextView) findViewById(R.id.overtask_detail_applicant);// 填表人
		holder.overtask_detail_newtime = (TextView) findViewById(R.id.overtask_detail_newtime);// 填表时间
		holder.overtask_detail_starttime = (TextView) findViewById(R.id.overtask_detail_starttime);// 加班开始时间
		holder.overtask_detail_endtime = (TextView) findViewById(R.id.overtask_detail_endtime);// 加班结束时间
		holder.overtask_detail_peoples = (TextView) findViewById(R.id.overtask_detail_peoples);// 要求加班人员
		holder.overtask_detail_cause = (TextView) findViewById(R.id.overtask_detail_cause);// 加班事由
		holder.linear_overtimetaskdetail = (LinearLayout) findViewById(R.id.linear_overtimetaskdetail);// 加班详情
		holder.image_overtime_taskdetail = (ImageView) findViewById(R.id.image_overtime_taskdetail);// 加班详情线
		holder.overtimetask_details_edit = (EditText) findViewById(R.id.overtimetask_details_edit);// 填写加班详情

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
			final Dialog_Http_Util dialog_util, final String descId) {
		if (descId.equals("usertask4")||descId.equals("usertask5")) {
			RequestParams param = new RequestParams();
			param.put("key", key);
			param.put("taskId", TaskId);
			param.put("applyId", applyId);
			param.put("proc_inst_id", procInstId);
			param.put("descId", descId);
			if(descId.equals("usertask4")){
				param.put("auth", "authOtDetail");	
			}else if(descId.equals("usertask5")){
				param.put("auth", "verifyOtDetail");
			}
			Dialog progressDialog = dialog_util.showWaitingDialog(this, "正在刷新",
					false);
			async.post(APIURL.CHECK.SHOWAUTHDETAIL, context, param,
					new JsonHandlerDetail(context, progressDialog));
		}
		my_approve_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (descId.equals("usertask3")) {
					if (holder.overtimetask_details_edit.getText().toString()
							.trim().equals("")) {
						ToastManager.getInstance(context)
								.showToastcenter("详情不能为空！");
						return;
					}
					final AlertDialog dialog_app = builder.create();// 初始化审批dialog
					LinearLayout shenpi = (LinearLayout) LayoutInflater.from(
							context).inflate(R.layout.submit_dialog, null);
					TextView heads = (TextView) shenpi.findViewById(R.id.heads);
					Button button_t = (Button) shenpi
							.findViewById(R.id.button_t);
					Button button_f = (Button) shenpi
							.findViewById(R.id.button_f);
					heads.setText("您确定要提交详情吗？");
					dialog_app.show();
					dialog_app.getWindow().setContentView(shenpi);
					button_t.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// 提交详情
							RequestParams param = new RequestParams();
							param.put("taskId", TaskId);
							param.put("key", key);
							param.put("applyNameState", applyNameState);
							param.put("id", applyId);
							param.put("proc_inst_id", procInstId);
							param.put("userid", userid);
							param.put("state", state);
							param.put("descId", descId);
							param.put("stateMent", "otApply");
							param.put("authContent",
									holder.overtimetask_details_edit.getText()
											.toString());// 提交详情
							Dialog progressDialog = dialog_util
									.showWaitingDialog(
											OverTimeTaskDetailsActivity.this,
											"正在提交", false);
							async.post(APIURL.CHECK.ADDDETAILS, context, param,
									new JsonHandlerApproval(
											OverTimeTaskDetailsActivity.this,
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
				} else if (descId.equals("usertask4")) {// 审批详情

					LinearLayout overtimetaskdetail_xz = (LinearLayout) LayoutInflater
							.from(context).inflate(
									R.layout.overtimetaskdetail_xz, null);
					builder.setView(overtimetaskdetail_xz);
					final AlertDialog dialog_app = builder.create();// 初始化审批dialog
					dialog_app.show();
					dialog_app.getWindow().clearFlags(
							WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
					dialog_app
							.getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					ListView movertimetaskdetail_list = (ListView) overtimetaskdetail_xz
							.findViewById(R.id.movertimetaskdetail_list);//
					OTadapter = new OverTimeTaskPeople_Adapter(context,"1");
					if(null==mlist.get(mlist.size()-1)){
						mlist.remove(mlist.size()-1);	
					}
					OTadapter.setList(mlist);
					movertimetaskdetail_list.setAdapter(OTadapter);
					OTadapter.setOnRightItemClickListener(new OverTimeTaskPeople_Adapter.onRightItemClickListener() {
						
						@Override
						public void onRightItemClick(View v, final int position) {
							// TODO Auto-generated method stub
							LinearLayout overtimetaskdetail_p = (LinearLayout) LayoutInflater
									.from(context).inflate(
											R.layout.overtimetaskdetail_p, null);
							TextView overtimetaskdetail_accompanyId = (TextView) overtimetaskdetail_p
									.findViewById(R.id.overtimetaskdetail_accompanyId);// 加班人
							overtimetaskdetail_accompanyId.setText(mlist.get(position).getUsername());
							TextView overtimetaskdetail_ = (TextView) overtimetaskdetail_p
									.findViewById(R.id.overtimetaskdetail_);// 加班详情
							overtimetaskdetail_.setText(mlist.get(position).getMainContent());
							builder.setView(overtimetaskdetail_p);
							final AlertDialog dialog_app = builder.create();// 初始化审批dialog
							dialog_app.show();
							dialog_app.getWindow().clearFlags(
									WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
							dialog_app
									.getWindow()
									.setSoftInputMode(
											WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
							
							Button dialog_approval = (Button) overtimetaskdetail_p
									.findViewById(R.id.dialog_approval);// 审批按钮
							final EditText overtask_detail_edits = (EditText) overtimetaskdetail_p
									.findViewById(R.id.overtask_detail_edits);// 审批意见
							RadioGroup overtime__dialog_radio_group = (RadioGroup) overtimetaskdetail_p
									.findViewById(R.id.overtime__dialog_radio_group);// (批准、否决)选项
							/*
							 * 加班时间(按考勤、无考勤)选项
							 */
							overtime__dialog_radio_group
									.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
										@Override
										public void onCheckedChanged(RadioGroup arg0,
												int arg1) {
											// TODO Auto-generated method stub
											if (arg0.getId() == R.id.overtime__dialog_radio_group) {
												switch (arg1) {
												case R.id.overtime__rgs_1:
													appstate = 3;
													break;
												case R.id.overtime__rgs_2:
													appstate = 4;
													break;
												case R.id.overtime__rgs_3:
													appstate = 2;
													break;
												default:
													break;
												}
											}
										}
									});
							dialog_approval.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									String detail_edits=overtask_detail_edits.getText().toString();
									if(detail_edits.trim().equals("")){
										ToastManager.getInstance(context).showToastcenter("效果评估不能为空！");
										return;
									}
									RequestParams param = new RequestParams();
									param.put("taskId", TaskId);
									param.put("applyId", applyId);
									param.put("key", key);
									param.put("applyNameState", applyNameState);
									param.put("id", mlist.get(position).getId());
									param.put("proc_inst_id", procInstId);
									param.put("userids", mlist.get(position).getUserid());
									param.put("state", appstate);
									param.put("authOtDetail", "authOtDetail");
									param.put("descId", descId);
									param.put("mainCause",detail_edits);// 审批意见
									param.put("app_view", "审批"+mlist.get(position).getUsername()+"加班情况:"+detail_edits);// 审批意见
									Log.e("lyt", param.toString());
									Dialog progressDialog = dialog_util
											.showWaitingDialog(
													OverTimeTaskDetailsActivity.this,
													"正在审批", false);
									async.post(
											APIURL.CHECK.ADDDETAILS,
											context,
											param,
											new JsonHandlerApproval(
													OverTimeTaskDetailsActivity.this,
													progressDialog));
									dialog_app.dismiss();
								}
							});
						}
					});
					
					
				} else if (descId.equals("usertask5")) {// 行政人员审核
					LinearLayout overtimetaskdetail_xz = (LinearLayout) LayoutInflater
							.from(context).inflate(
									R.layout.overtimetaskdetail_xz, null);
					builder.setView(overtimetaskdetail_xz);
					final AlertDialog dialog_app = builder.create();// 初始化审批dialog
					dialog_app.show();
					dialog_app.getWindow().clearFlags(
							WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
					dialog_app
							.getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					ListView movertimetaskdetail_list = (ListView) overtimetaskdetail_xz
							.findViewById(R.id.movertimetaskdetail_list);//
					OTadapter = new OverTimeTaskPeople_Adapter(context,"2");
					if(null==mlist.get(mlist.size()-1)){
						mlist.remove(mlist.size()-1);	
					}
					OTadapter.setList(mlist);
					movertimetaskdetail_list.setAdapter(OTadapter);
					OTadapter.setOnRightItemClickListener(new OverTimeTaskPeople_Adapter.onRightItemClickListener() {
						
						@Override
						public void onRightItemClick(View v, final int position) {
					// 核实dialog
					LinearLayout overtimetaskdetails_hs = (LinearLayout) LayoutInflater
							.from(context).inflate(
									R.layout.overtimetaskdetails_hs, null);
					builder.setView(overtimetaskdetails_hs);
					final AlertDialog dialog_app = builder.create();// 初始化审批dialog
					dialog_app.show();
					dialog_app.getWindow().clearFlags(
							WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
					dialog_app
							.getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

					TextView overtimes_accompanyId = (TextView) overtimetaskdetails_hs
							.findViewById(R.id.overtimes_accompanyId);// 加班人
					overtimes_accompanyId.setText(accompanyId);
					Button approval = (Button) overtimetaskdetails_hs
							.findViewById(R.id.dialog_approval);// 审批按钮
					final EditText app_view = (EditText) overtimetaskdetails_hs
							.findViewById(R.id.app_view);// 审批意见
					RadioGroup overtimetask_dialog_radio_group = (RadioGroup) overtimetaskdetails_hs
							.findViewById(R.id.overtimetask_dialog_radio_group);// (批准、否决)选项
					final LinearLayout linear_startovertimes = (LinearLayout) overtimetaskdetails_hs
							.findViewById(R.id.linear_startovertimes);
					final LinearLayout linear_endovertimes = (LinearLayout) overtimetaskdetails_hs
							.findViewById(R.id.linear_endovertimes);

					/*
					 * 加班时间(按考勤、无考勤)选项
					 */
					overtimetask_dialog_radio_group
							.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(RadioGroup arg0,
										int arg1) {
									// TODO Auto-generated method stub
									if (arg0.getId() == R.id.overtimetask_dialog_radio_group) {
										switch (arg1) {
										case R.id.overtimetask_rgs_1:
											mstate = 3;
											linear_startovertimes
													.setVisibility(ViewGroup.GONE);
											linear_endovertimes
													.setVisibility(ViewGroup.GONE);
											break;
										case R.id.overtimetask_rgs_2:
											mstate = 57;
											linear_startovertimes
													.setVisibility(ViewGroup.VISIBLE);
											linear_endovertimes
													.setVisibility(ViewGroup.VISIBLE);
											break;
										default:
											break;
										}
									}
								}
							});
					/*
					 * 审核事件
					 */
					approval.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							final String appview = app_view.getText()
									.toString();
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
									ToastManager.getInstance(context).showToastcenter("此功能暂不能使用");


//{taskId=405574, applyId=1201, proc_inst_id=405515, descId=usertask5, verifyOtDetail=verifyOtDetail, state=57, duTime=, userids=228, id=1515, mainCause=和你一
//
//起碎，沉睡的人们毫无知觉, applyNameState=9, end_time=2016-07-08 23:23, start_time=2016-07-08 18:23, app_view=核实刘宇庭加班时长:}

//									RequestParams param = new RequestParams();
//									param.put("taskId", TaskId);
//									param.put("applyId", applyId);
//									param.put("key", key);
//									param.put("applyNameState", applyNameState);
////									param.put("id", );
//									param.put("proc_inst_id", procInstId);
//									param.put("verifyOtDetail", "verifyOtDetail");
////									param.put("userids", "");
//									param.put("state", mstate);
//									param.put("duTime", "");
//									param.put("authApp", app_atate);
//									param.put("descId", descId);
//									param.put("mainCause", appview);
//									param.put("start_time", "2016-07-08 18:23");
//									param.put("end_time", "2016-07-08 23:23");
//									param.put("app_view", "核实刘宇庭加班时长:");
//									Dialog progressDialog = dialog_util
//											.showWaitingDialog(
//													OverTimeTaskDetailsActivity.this,
//													"正在审批", false);
//									async.post(
//											APIURL.CHECK.ADDDETAILS,
//											context,
//											param,
//											new JsonHandlerApproval(
//													OverTimeTaskDetailsActivity.this,
//													progressDialog));
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
									param.put("taskId", TaskId);
									param.put("key", key);
									param.put("applyNameState", applyNameState);
									param.put("id", applyId);
									param.put("proc_inst_id", procInstId);
									param.put("userid", userid);
									param.put("state", state);
									param.put("authApp", app_atate);
									param.put("descId", descId);
									param.put("authContent", appview);// 审批意见
									Log.e("lyt", param.toString());
									Dialog progressDialog = dialog_util
											.showWaitingDialog(
													OverTimeTaskDetailsActivity.this,
													"正在审批", false);
									async.post(
											APIURL.CHECK.ADDNEWOVERPROVE,
											context,
											param,
											new JsonHandlerApproval(
													OverTimeTaskDetailsActivity.this,
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

	// 加班任务单详情回调类
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
			String createUserName = "";// 填表人
			String dept = "";// 所属部门
			String currentTime = "";// 填表时间
			String start_time = "";// 加班开始时间
			String EndTime = "";// 加班结束时间
			String mainContent = "";// 加班事由
			try {
				createUserName = response.getString("createUserName");
				dept = response.getString("dept");
				currentTime = response.getString("currentTime");
				start_time = response.getString("start_time");
				EndTime = response.getString("EndTime");
				accompanyId = response.getString("accompanyId");
				mainContent = response.getString("mainContent");
				applyHistory = (JSONArray) response.get("applyHistory");
				state = response.getString("state");
				userid = response.getString("userid");// userId
				descId = response.getString("descId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			holder.overtask_detail_applicant.setText(createUserName + "("
//					+ dept + ")");
			holder.overtask_detail_applicant.setText(createUserName);
			holder.overtask_detail_newtime.setText(currentTime);
			holder.overtask_detail_starttime.setText(start_time);
			String mEndtime = DateUtil.getEnglishTime(EndTime);
			holder.overtask_detail_endtime.setText(mEndtime);
			holder.overtask_detail_peoples.setText(accompanyId);
			holder.overtask_detail_cause.setText(mainContent);
			// 审批历史
			memu = new Gson().fromJson(applyHistory.toString(),
					new TypeToken<List<Memu_History>>() {
					}.getType());// 审批历史
			if (null != state && state.equals("2")) {
				String App_state = memu.get(0).getApp_state();
				if (App_state.equals("4")) {
					overtime_typeimg.setImageResource(R.mipmap.veto);
					overtime_typeimg.setVisibility(ViewGroup.VISIBLE);
					// 否决
				} else {
					if (!App_state.equals("6")) {
						overtime_typeimg.setImageResource(R.mipmap.complete);
						overtime_typeimg.setVisibility(ViewGroup.VISIBLE);
					}
				}

			}
			if (descId.equals("deptGenManager") || descId.equals("GenManager")
					|| descId.equals("deptManager")
					|| descId.equals("genManager")) {
			} else if (descId.equals("usertask3")) {
				holder.image_overtime_taskdetail
						.setVisibility(ViewGroup.VISIBLE);
				holder.linear_overtimetaskdetail
						.setVisibility(ViewGroup.VISIBLE);
			} else if (descId.equals("usertask4")) {

			} else if (descId.equals("usertask5")
					|| descId.equals("serverStaff2")
					|| descId.equals("serverStaff")) {

			}
		}

	}

	// 加班审批回调类
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
					OverTimeTaskDetailsActivity.this.finish();
				} else {
					ToastUtil.showToast(context, "审批失败！");

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// 加班任务单详情页面
	private class JsonHandlerDetail extends MJsonHttpHandler {
		Dialog progressDialog;

		protected JsonHandlerDetail(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			String ss = response.toString();
			Log.e("lyt",ss.toString());
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
			JSONArray otDetailList = null;//

			try {
				otDetailList = response.getJSONArray("otDetailList");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mlist = new Gson().fromJson(otDetailList.toString(),
					new TypeToken<List<OverTimeTaskPeople_Entity>>() {
					}.getType());

		}
	}

	public static class ViewHolder {
		private TextView overtask_detail_applicant;// 填表人
		private TextView overtask_detail_newtime;// 填表时间
		private TextView overtask_detail_starttime;// 加班开始时间
		private TextView overtask_detail_endtime;// 加班结束时间
		private TextView overtask_detail_peoples;// 要求加班人员
		private TextView overtask_detail_cause;// 请假原因
		private LinearLayout linear_overtimetaskdetail;// 填写详情
		private ImageView image_overtime_taskdetail;
		private EditText overtimetask_details_edit;
	}
}
