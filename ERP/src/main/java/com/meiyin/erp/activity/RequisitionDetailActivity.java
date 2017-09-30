package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.meiyin.erp.adapter.Purchase_List_Adapter;
import com.meiyin.erp.adapter.Purchase_mList_Adapter;
import com.meiyin.erp.adapter.RequisitionAdapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Memu_History;
import com.meiyin.erp.entity.Purchase_List_Entity;
import com.meiyin.erp.entity.Purchase_mList_Entity;
import com.meiyin.erp.entity.Requisition_Entity;
import com.meiyin.erp.util.AndroidUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
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
 * @param请购单详情
 * @Time 2016-5-12
 */
public class RequisitionDetailActivity extends Activity {

	private Context context;
	private ArrayList<Memu_History> memu;
	private AlertDialog dialog;

	private RequisitionAdapter adapter;
	private int app_atate = 3;// 审批(批准、否决)选项
	private String userid, state;// 审批所需参数
	private LinearLayout my_approve_bt;
	private ImageView requisition_typeimg;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.requisitiondetail_main);
		context = getApplicationContext();
		activity=this;
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

	@Override
	protected void onDestroy() {
		if(null!=activity){activity=null;}

		if (memu != null){
			memu.clear();
			memu = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	private void initData(SharedPreferences sp) {
		// TODO Auto-generated method stub
		Dialog_Http_Util dialog_util = new Dialog_Http_Util();
		AsyncHttpClientUtil async = new AsyncHttpClientUtil();
		memu = new ArrayList<Memu_History>();// 审批历史数据
		dialog = new Builder(activity).create();// 初始化审批历史dialog
		Builder builder = new Builder(activity);// 初始化审批dialog
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		requisition_typeimg = (ImageView) findViewById(R.id.requisition_typeimg);
		/*
		 * 查询详情所需要参数
		 */
		String applyNameState = getIntent().getStringExtra("applyNameState");
		String applyId = getIntent().getStringExtra("applyId");
		String procInstId = getIntent().getStringExtra("procInstId");
		String descId = getIntent().getStringExtra("descId");
		String TaskId = getIntent().getStringExtra("TaskId");
		String name = getIntent().getStringExtra("out");
		/*
		 * 提交详情参数
		 */
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
					procInstId, dialog_util,descId);
		} else if (name.equals("已审批事项")) {
			param.put("key", key);
			param.put("applyNameState", applyNameState);
			param.put("applyId", applyId);
			param.put("procInstId", procInstId);
			param.put("taskId", TaskId);
			param.put("isFresh", true);
			my_approve_bt.setVisibility(ViewGroup.GONE);
		}

		Dialog progressDialog = dialog_util.showWaitingDialog(activity, "正在刷新",
				false);
		async.post(APIURL.CHECK.MEMU_SQAPI, context, param, new JsonHandler(
				context, progressDialog));
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
			final Dialog_Http_Util dialog_util,final String descId) {

		my_approve_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//核价员checkMan、财务副总finaDeptGenManager、采购员buyer、
				if(descId.equals("checkMan")||descId.equals("finaDeptGenManager")){
					//获取采购单列表

					//assessor
					RequestParams param = new RequestParams();
					param.put("purApplyId", applyId);
					param.put("key", key);
					param.put("applyNameState", applyNameState);
					param.put("id", applyId);
					param.put("taskId", TaskId);
					param.put("descId", descId);
					param.put("proc_inst_id", procInstId);

					param.put("userid", userid);
					param.put("state", state);
					param.put("isFresh", true);
					Log.e("lyt","key="+key+"&purApplyId=" + applyId + "&id="
							+ applyId + "&applyNameState="
							+ applyNameState + "&proc_inst_id="
							+ procInstId + "&userid=" + userid
							+ "&state=" + state + "&taskId="
							+ TaskId + "&descId=" + descId+"&descId=" + descId+"&isFresh=" + true);
					Dialog progressDialog = dialog_util
							.showWaitingDialog(
									activity,
									"正在刷新", false);
					async.post(APIURL.CHECK.PURCHASEORDERSLIST, context,
							param, new JsonHandlerPurchase(descId,
									context,
									progressDialog));


				}else{
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
								param.put("taskId", TaskId);
								param.put("key", key);
								param.put("applyNameState", applyNameState);
								param.put("id", applyId);
								param.put("proc_inst_id", procInstId);
								param.put("userid", userid);
								param.put("state", state);
								param.put("app_state", app_atate);
								param.put("app_view", appview);
								Log.e("lyt", "taskId=" + TaskId + ",id="
										+ applyId + ",applyNameState="
										+ applyNameState + ",proc_inst_id="
										+ procInstId + ",userid=" + userid
										+ ",state=" + state + ",app_state="
										+ app_atate + ",app_view=" + appview);
								Dialog progressDialog = dialog_util
										.showWaitingDialog(
												activity,
												"正在审批", false);
								async.post(APIURL.CHECK.MEMU_SPAPI, context,
										param, new JsonHandlerApproval(
												context,
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
		headtitletext.setText("请购申请单");
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
	private TextView requisition_applicant;// 申请人
	private TextView requisition_project_name;// 项目名称
	private TextView requisition_article_introduction;// 物品介绍
	private TextView requisition_cause;// 物品用途
	private TextView requisition_need_time;// 需用时间
	private TextView requisition_total;// 总计
	private LinearLayout linear_project;// 项目布局
	private View img_project;// 项目布局线
	private void initView() {
		// TODO Auto-generated method stub
		// 请购详情
		ListView detail_listview = (ListView) findViewById(R.id.detail_listview);
		View requisition_head = LayoutInflater.from(context).inflate(
				R.layout.requisition_head, null);
		requisition_applicant = (TextView) requisition_head
				.findViewById(R.id.requisition_applicant);// 申请人
		requisition_project_name = (TextView) requisition_head
				.findViewById(R.id.requisition_project_name);// 项目名称
		requisition_article_introduction = (TextView) requisition_head
				.findViewById(R.id.requisition_article_introduction);// 物品介绍
		requisition_cause = (TextView) requisition_head
				.findViewById(R.id.requisition_cause);// 物品用途
		requisition_need_time = (TextView) requisition_head
				.findViewById(R.id.requisition_need_time);// 需用时间
		requisition_total = (TextView) requisition_head
				.findViewById(R.id.requisition_total);// 总计
		linear_project = (LinearLayout) requisition_head
				.findViewById(R.id.linear_project);// 项目布局
		img_project = (View) requisition_head
				.findViewById(R.id.img_project);// 项目布局线

		// 添加详情列表头部
		detail_listview.addHeaderView(requisition_head);
		adapter = new RequisitionAdapter(getApplicationContext());
		detail_listview.setAdapter(adapter);
		detail_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 > 0) {
					try {
						Uri uri = Uri.parse(adapter.getList().get(arg2 - 1)
								.getUrl());
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
					} catch (Exception e) {
						// TODO: handle exception
						ToastManager.getInstance(context)
								.showToastcenter("网址不正确！");
					}
				}
			}
		});

	}
	//核价员，财务副总审批
    private class JsonHandlerPurchase extends MJsonHttpHandler {
        Dialog progressDialog;
		String descId;
        protected JsonHandlerPurchase(String descId,Context context, Dialog progressDialog) {
            super(context, progressDialog);
            this.progressDialog = progressDialog;
			this.descId=descId;
            // TODO Auto-generated constructor stub
        }

        @SuppressLint("NewApi")
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
            String ss = response.toString();
			progressDialog.dismiss();
			Log.e("lyt", ss);
			if(descId.equals("checkMan")) {
				try {
					JSONObject taskdto = (JSONObject)response.get("TaskIdDto");
					final String TaskId=taskdto.getString("taskId");
					final String key=taskdto.getString("key");
					final String applyNameState=taskdto.getString("applyNameState");
					final String applyId=taskdto.getString("applyId");
					final String proc_inst_id=taskdto.getString("proc_inst_id");
					final String descId=taskdto.getString("descId");
//                    final String prod_id=taskdto.getString("prod_id");
					final String state=taskdto.getString("state");
					final String userid=taskdto.getString("userid");
					final String isFresh=taskdto.getString("isFresh");
					final String id=taskdto.getString("id");
					final String purApplyId=taskdto.getString("purApplyId");
					final String procInstId=taskdto.getString("procInstId");
					final String user_id=taskdto.getString("user_id");

					JSONArray dto2 = (JSONArray) response.get("dto2");

					final ArrayList<Purchase_List_Entity> memu1 = new Gson().fromJson(dto2.toString(),
							new TypeToken<List<Purchase_List_Entity>>() {
							}.getType());
					LinearLayout layout_purchase_dialog = (LinearLayout) LayoutInflater
							.from(context).inflate(R.layout.layout_purchase_dialog, null);
					Button purchase_approval = (Button) layout_purchase_dialog
							.findViewById(R.id.purchase_approval);
					Builder builder = new Builder(activity);//
					builder.setView(layout_purchase_dialog);
					final AlertDialog dialog_app = builder.create();// 初始化审批dialog
					dialog_app.show();
					dialog_app.getWindow().clearFlags(
							WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
					dialog_app.getWindow().setSoftInputMode(
							WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					ListView purchase_list = (ListView) layout_purchase_dialog
							.findViewById(R.id.purchase_list);
					Purchase_List_Adapter adapter = new Purchase_List_Adapter(context);
					adapter.setList(memu1);
					purchase_list.setAdapter(adapter);
                /*
				 * 审批事件
				 */
					purchase_approval.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// 提交审批
							RequestParams param = new RequestParams();
							param.put("taskId",TaskId);
							param.put("key", key);
							param.put("applyNameState", applyNameState);
							param.put("id", id);
							param.put("proc_inst_id", proc_inst_id);
							param.put("userid", userid);
							param.put("state", state);
							param.put("app_states", "0");
							param.put("app_state", "3");
							param.put("isFresh", isFresh);
							param.put("NUM", "");
							param.put("purApplyId", purApplyId);
							param.put("descId", descId);

							for(int i=0;i<memu1.size();i++){
								String prod_id=memu1.get(i).getProd_id();
								String radio=memu1.get(i).getRadios();

								param.put("buy"+prod_id, radio);
								param.put("radio"+prod_id, radio);
								param.put("supplier"+prod_id+"1", memu1.get(i).getSupplier1());//供应商
								param.put("supplier"+prod_id+"2", memu1.get(i).getSupplier2());//供应商
								param.put("supplier"+prod_id+"3", memu1.get(i).getSupplier3());//供应商
								param.put("s_bill"+prod_id+"1", memu1.get(i).getS_bill1());//发票
								param.put("s_bill"+prod_id+"2", memu1.get(i).getS_bill2());//发票
								param.put("s_bill"+prod_id+"3", memu1.get(i).getS_bill3());//发票
								param.put("typeOfPayment"+prod_id+"1", memu1.get(i).getTypeOfPayment1());//支付方式
								param.put("typeOfPayment"+prod_id+"2", memu1.get(i).getTypeOfPayment2());//支付方式
								param.put("typeOfPayment"+prod_id+"3", memu1.get(i).getTypeOfPayment3());//支付方式
								param.put("price"+prod_id+"1", memu1.get(i).getS_price1());//单价
								param.put("price"+prod_id+"2", memu1.get(i).getS_price2());//单价
								param.put("price"+prod_id+"3", memu1.get(i).getS_price3());//单价
								param.put("effective"+prod_id+"1", memu1.get(i).getS_limitTime1());//保质期
								param.put("effective"+prod_id+"2", memu1.get(i).getS_limitTime2());//保质期
								param.put("effective"+prod_id+"3", memu1.get(i).getS_limitTime3());//保质期
								param.put("remark"+prod_id+"1", memu1.get(i).getS_remark1());//备注
								param.put("remark"+prod_id+"2", memu1.get(i).getS_remark2());//备注
								param.put("remark"+prod_id+"3", memu1.get(i).getS_remark3());//备注
								param.put("apply_estimate"+prod_id+"1", memu1.get(i).getS_arrivalTime1());//到货时间
								param.put("apply_estimate"+prod_id+"2", memu1.get(i).getS_arrivalTime2());//到货时间
								param.put("apply_estimate"+prod_id+"3", memu1.get(i).getS_arrivalTime3());//到货时间
							}
//							LogUtil.e("lyt",param.toString());
							Dialog_Http_Util dialog_util = new Dialog_Http_Util();
							AsyncHttpClientUtil async = new AsyncHttpClientUtil();
							Dialog progressDialog = dialog_util
									.showWaitingDialog(
											activity,
											"正在审批", false);
							async.post(APIURL.CHECK.PURCHASEORDERSAPPROVALS, context,
									param, new JsonHandlerApproval(
											context,
											progressDialog));

						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}




			}else if(descId.equals("finaDeptGenManager")){
                try {
                    JSONArray json2 = (JSONArray) response.get("json2");
                    JSONObject taskdto = (JSONObject)response.get("TaskIdDto");
                    final String TaskId=taskdto.getString("taskId");
                    final String key=taskdto.getString("key");
                    final String applyNameState=taskdto.getString("applyNameState");
                    final String applyId=taskdto.getString("applyId");
                    final String proc_inst_id=taskdto.getString("proc_inst_id");
                    final String descId=taskdto.getString("descId");
//                    final String prod_id=taskdto.getString("prod_id");
                    final String state=taskdto.getString("state");
                    final String userid=taskdto.getString("userid");
                    final String isFresh=taskdto.getString("isFresh");
                    final String id=taskdto.getString("id");
                    final String purApplyId=taskdto.getString("purApplyId");
                    final String procInstId=taskdto.getString("procInstId");
                    final String user_id=taskdto.getString("user_id");
                    final ArrayList<Purchase_mList_Entity> memus = new Gson().fromJson(json2.toString(),
						new TypeToken<List<Purchase_mList_Entity>>() {
						}.getType());
				   if(memus.size()<1){
						return;
				   }
				LinearLayout layout_purchase_dialog = (LinearLayout) LayoutInflater
						.from(context).inflate(R.layout.layout_purchase_dialog, null);
                Button purchase_approval = (Button) layout_purchase_dialog
                        .findViewById(R.id.purchase_approval);
				Builder builder = new Builder(activity);//
				builder.setView(layout_purchase_dialog);
				final AlertDialog dialog_app = builder.create();// 初始化审批dialog
				dialog_app.show();
				dialog_app.getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
				dialog_app.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				ListView purchase_list = (ListView) layout_purchase_dialog
						.findViewById(R.id.purchase_list);
                Purchase_mList_Adapter adapter = new Purchase_mList_Adapter(context);
				adapter.setList(memus);
				purchase_list.setAdapter(adapter);
                /*
				 * 审批事件
				 */
                purchase_approval.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                // 提交审批
                                RequestParams param = new RequestParams();
                                param.put("taskId",TaskId);
                                param.put("key", key);
                                param.put("applyNameState", applyNameState);
                                param.put("id", id);
                                param.put("proc_inst_id", proc_inst_id);
                                param.put("userid", userid);
                                param.put("state", state);
                                param.put("app_states", "0");
								param.put("app_state", "3");
                                param.put("isFresh", isFresh);
                                param.put("NUM", "");
                                param.put("purApplyId", purApplyId);
								param.put("descId", descId);

                                for(int i=0;i<memus.size();i++){
									String prod_id=memus.get(i).getProd_id();
									param.put("T50"+i, memus.get(i).getProd_Model());//型号
                                    param.put("supplier"+prod_id, memus.get(i).getSupplier());//供应商
                                    param.put("s_bill"+prod_id, memus.get(i).getBill_type());//发票
                                    param.put("typeOfPayment"+prod_id, memus.get(i).getTypeOfPayment());//支付方式
                                    param.put("price"+prod_id, memus.get(i).getProd_unit());//单价
                                    param.put("effective"+prod_id, memus.get(i).getEffective());//保质期
                                    param.put("remark"+prod_id, memus.get(i).getRemark());//备注
                                    param.put("apply_estimate"+prod_id, memus.get(i).getApply_estimate());//到货时间
                                }

                                Dialog_Http_Util dialog_util = new Dialog_Http_Util();
                                AsyncHttpClientUtil async = new AsyncHttpClientUtil();
                                Dialog progressDialog = dialog_util
                                        .showWaitingDialog(activity,
                                                "正在审批", false);
                                async.post(APIURL.CHECK.PURCHASEORDERSAPPROVAL, context,
                                        param, new JsonHandlerApproval(context,
                                                progressDialog));

                            }
                        });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

			}

        }

    }
	// 请购单详情
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
			Log.e("lyt", response.toString());
			progressDialog.dismiss();

			JSONArray applyHistory = null;
			JSONArray queryForList = null;
			String createUserName = "";// 申请人
			String dept = "";// 所属部门
			String projectName = "";// 项目名称
			String compendium = "";// 物品介绍
			String reason = "";// 物品用途
			String needsDate = "";// 物品需要时间
			String zongjiaqian = "";// 总计
			String descId = "";
			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
				descId = response.getString("descId");
				createUserName = response.getString("createUserName");
				dept = response.getString("dept");
				if(!response.isNull("projectName")){
					projectName = response.getString("projectName");
				}
				compendium = response.getString("compendium");
				reason = response.getString("reason");
				needsDate = response.getString("needsDate");
				if (response.isNull("zongjiaqian")) {
					zongjiaqian = "";
				} else {
					zongjiaqian = response.getString("zongjiaqian");
				}
				applyHistory = (JSONArray) response.get("applyHistory");// 历史审批
				queryForList = (JSONArray) response.get("queryForList");// 物品明细
				// 审批所需参数
				userid = response.getString("userid");// userId
				state = response.getString("state");// state
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			requisition_applicant.setText(createUserName + "(" + dept
					+ ")");
			if (projectName.equals("")) {// 项目为空 隐藏这一项
				linear_project.setVisibility(ViewGroup.GONE);
				img_project.setVisibility(ViewGroup.GONE);
			}
			requisition_project_name.setText(projectName);
			requisition_article_introduction.setText(compendium);
			requisition_cause.setText(reason);
			requisition_need_time.setText(needsDate);
			requisition_total.setText("￥" + zongjiaqian);
			memu = new Gson().fromJson(applyHistory.toString(),
					new TypeToken<List<Memu_History>>() {
					}.getType());// 审批历史
			ArrayList<Requisition_Entity> list = new Gson().fromJson(
					queryForList.toString(),
					new TypeToken<List<Requisition_Entity>>() {
					}.getType());// 物品明细
			if (null != state && state.equals("2")) {
				String App_state = memu.get(0).getApp_state();
				if (App_state.equals("4")) {
					requisition_typeimg.setImageResource(R.mipmap.veto);
					requisition_typeimg.setVisibility(ViewGroup.VISIBLE);
					// 否决
				} else {
					if (!App_state.equals("6")) {
						requisition_typeimg
								.setImageResource(R.mipmap.complete);
						requisition_typeimg.setVisibility(ViewGroup.VISIBLE);
					}
				}

			}
			adapter.setList(list);
			adapter.notifyDataSetChanged();
//			checkMan、finaDeptGenManager、buyer、
			if (descId.equals("buyer")) {
				ToastManager.getInstance(context)
						.showToastcenter("采购人员请到ERP签收此单！");
				my_approve_bt.setVisibility(ViewGroup.GONE);
			} else if (descId.equals("deptGenManager")
					|| descId.equals("GenManager")) {
				my_approve_bt.setVisibility(ViewGroup.VISIBLE);
			}
		}

	}

	public  class ViewHolder {

	}

	// 请购单审批// 请购单财务副总审批//核价员审批
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
			Log.e("lyt", ss);
			progressDialog.dismiss();
			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
				if (response.getString("message").equals("success")) {
					ToastManager.getInstance(context).showToastcenter( "审批成功！");
					Intent intent = new Intent();
					intent.setAction(SPConstant.APPROVALACTION);
					sendBroadcast(intent);
					my_approve_bt.setVisibility(ViewGroup.GONE);
					activity.finish();
				} else {
					ToastManager.getInstance(context).showToastcenter( "审批失败！");

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}




}
