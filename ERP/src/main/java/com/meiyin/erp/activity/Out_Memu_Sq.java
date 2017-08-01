package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
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
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.Memu_History_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.ClientInfoList;
import com.meiyin.erp.entity.Memu_History;
import com.meiyin.erp.json.JsonHttpHandles;
import com.meiyin.erp.util.AsyncHttpclient_Util;
import com.meiyin.erp.util.DateUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.ToastUtil;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 外出详情页
 */
public class Out_Memu_Sq extends Activity implements OnCheckedChangeListener {

	private Context context;
	private Builder builder;

	private AlertDialog dialog, dialogs;
	private Dialog_Http_Util dhu;
	private AlertDialog adb;
	private SharedPreferences sp;
	private TextView memusq_shenqingren, memusq_waichushiyou, memusq_time,
			memusq_yujijiaotongfei;
	private ArrayList<Memu_History> memu;
	private String key;

	private TextView shijijiaotongfei_s, shibaojiaotongfei_s,
			waichuxiangqing_s, textyujikehu;// 审核后显示的实际交通费
	private EditText edit_t1, edit_t2, edit_t3;
	private LinearLayout my_approve_bt, shijijiaotongfei, shibaojiaotongfei,
			waichuxiangqing, tianxie1, tianxie2, tianxie3, yujibaifang,
			shijibaifang;// 审核后显示的layout
	private View sqview1, sqview2, sqview3, sqview4, sqview5, sqview6,
			yujibaifang1, shijibaifang1;// 审核后显示的view虚线
	private String TaskId, applyNameState, applyId, procInstId, descId, isCar;
	private String userid, state;
	private int app_atate = 3;
	private TextView hongdian;
	private String appview;
	private EditText heshi_app_view, shuoming;
	private String realityFare1;
	private ImageView yiwancheng_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.out_memusq);
		context = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		memu = new ArrayList<Memu_History>();
		builder = new Builder(Out_Memu_Sq.this);
		adb = new Builder(Out_Memu_Sq.this).create();
		initHeader();
		initFools();
		view();
		// 我也没弄懂这些参数干啥的、、、
		key = sp.getString(SPConstant.MY_TOKEN, "");
		applyNameState = getIntent().getStringExtra("applyNameState");
		applyId = getIntent().getStringExtra("applyId");
		procInstId = getIntent().getStringExtra("procInstId");
		descId = getIntent().getStringExtra("descId");
		TaskId = getIntent().getStringExtra("TaskId");
		// 申请单分类、、
		String name = getIntent().getStringExtra("out");
		if (name.equals("申请表单")) {
			RequestParams params = new RequestParams();
			params.put("key", key);
			params.put("applyNameState", applyNameState);
			params.put("applyId", applyId);
			params.put("procInstId", procInstId);
			my_approve_bt.setVisibility(ViewGroup.GONE);
			AsyncHttp("正在刷新", APIURL.CHECK.MEMU_SQAPI, params);

		} else if (name.equals("待审批事项")) {
			desk();
			if (descId.equals("userSelfInnerTime")
					|| descId.equals("userSelfOutTime")) {
				tianxie1.setVisibility(ViewGroup.VISIBLE);
				tianxie2.setVisibility(ViewGroup.VISIBLE);
				tianxie3.setVisibility(ViewGroup.VISIBLE);
				sqview4.setVisibility(ViewGroup.VISIBLE);
				sqview5.setVisibility(ViewGroup.VISIBLE);
				sqview6.setVisibility(ViewGroup.VISIBLE);

			}
			RequestParams param = new RequestParams();
			param.put("taskId", TaskId);
			param.put("key", key);
			param.put("applyNameState", applyNameState);
			param.put("applyId", applyId);
			param.put("procInstId", procInstId);
			param.put("isFresh", true);
			param.put("descId", descId);
			AsyncHttp("正在刷新", APIURL.CHECK.MEMU_SQAPI, param);
		} else if (name.equals("已审批事项")) {

			RequestParams paramss = new RequestParams();
			paramss.put("key", key);
			paramss.put("applyNameState", applyNameState);
			paramss.put("applyId", applyId);
			paramss.put("procInstId", procInstId);
			paramss.put("taskId", TaskId);
			paramss.put("isFresh", true);
			my_approve_bt.setVisibility(ViewGroup.GONE);
			AsyncHttp("正在刷新", APIURL.CHECK.MEMU_SQAPI, paramss);
		}

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
		headtitletext.setText("外出申请单");
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
				LinearLayout list_dialog = (LinearLayout) LayoutInflater.from(
						context).inflate(R.layout.list_dialog, null);
				Memu_History_Adapter adapter = new Memu_History_Adapter(context);
				ListView history_list = (ListView) list_dialog
						.findViewById(R.id.history_list);
				adapter.setList(memu);
				history_list.setAdapter(adapter);
				adb.show();
				adb.getWindow().setContentView(list_dialog);
			}
		});
		my_approve_bt = (LinearLayout) findViewById(R.id.my_approve_bt);

	}
	protected void view() {

		yiwancheng_img = (ImageView) findViewById(R.id.yiwancheng_img);
		// 外出申请单基本信息
		memusq_shenqingren = (TextView) findViewById(R.id.memusq_shenqingren);
		memusq_waichushiyou = (TextView) findViewById(R.id.memusq_waichushiyou);
		memusq_time = (TextView) findViewById(R.id.memusq_time);
		memusq_yujijiaotongfei = (TextView) findViewById(R.id.memusq_yujijiaotongfei);
		// 外出申请单交通费
		shijijiaotongfei = (LinearLayout) findViewById(R.id.shijijiaotongfei);
		shijijiaotongfei_s = (TextView) findViewById(R.id.shijijiaotongfei_s);
		sqview1 = (View) findViewById(R.id.sqview1);

		shibaojiaotongfei = (LinearLayout) findViewById(R.id.shibaojiaotongfei);
		shibaojiaotongfei_s = (TextView) findViewById(R.id.shibaojiaotongfei_s);
		sqview2 = (View) findViewById(R.id.sqview2);
		// 外出申请单外出详情
		waichuxiangqing = (LinearLayout) findViewById(R.id.waichuxiangqing);
		waichuxiangqing_s = (TextView) findViewById(R.id.waichuxiangqing_s);
		sqview3 = (View) findViewById(R.id.sqview3);
		// 填写实际交通费、交通路线、外出详情
		tianxie1 = (LinearLayout) findViewById(R.id.tianxie1);
		edit_t1 = (EditText) findViewById(R.id.edit_t1);
		sqview4 = (View) findViewById(R.id.sqview4);

		tianxie2 = (LinearLayout) findViewById(R.id.tianxie2);
		edit_t2 = (EditText) findViewById(R.id.edit_t2);
		sqview5 = (View) findViewById(R.id.sqview5);

		tianxie3 = (LinearLayout) findViewById(R.id.tianxie3);
		edit_t3 = (EditText) findViewById(R.id.edit_t3);
		sqview6 = (View) findViewById(R.id.sqview6);

		yujibaifang = (LinearLayout) findViewById(R.id.yujibaifang);
		shijibaifang = (LinearLayout) findViewById(R.id.shijibaifang);
		textyujikehu = (TextView) findViewById(R.id.textyujikehu);

		yujibaifang1 = (View) findViewById(R.id.yujibaifang1);
		shijibaifang1 = (View) findViewById(R.id.shijibaifang1);

	}

	// 异步请求
	public void AsyncHttp(String name, String url, RequestParams params) {
		dhu = new Dialog_Http_Util();
		dhu.showWaitingDialog(Out_Memu_Sq.this, name, false);
		AsyncHttpclient_Util.post(url, context, params, new JsonHandler());
	}

	// 外出申请单详情回调类
	private class JsonHandler extends JsonHttpHandles {
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			String ss = response.toString();
			Log.e("lyt", ss);
			try {
				// 待审批/审核数据展示
				if (descId.equals("deptManager")
						|| descId.equals("deptGenManager")) {
					isnulljson(response);
					String createUserName = response.getString("createUserName");// 申请人名字						
					String dept= response.getString("dept");// 申请人所属部门					
					String accompanyId = response.getString("accompanyId");// 随行人员	
					String currentTime = response.getString("currentTime");// 提交申请单事件
					String mainContent = response.getString("mainContent");// 外出事由
					String outTime = response.getString("outTime");// 外出时间	
					String backTime = response.getString("backTime");// 回来时间	
					String startLocation = response.getString("startLocation");// 起始地点
					String toLocation = response.getString("toLocation");// 外出地点
					userid = response.getString("userid");// userId
					applyId = response.getString("applyId");// applyId
					state = response.getString("state");// state
					isCar = response.getString("isCar");// 是否乘车
					String gotonRoute = response.getString("gotonRoute");// 乘车原因
					String vehicle = response.getString("vehicle");// 乘坐工具
					String predictFare = response.getString("predictFare");// 外出费用
					boolean clientInfoList = response.isNull("clientInfoList");// 客户拜访数据
					// 是否存在客户拜访数据是否为空
					if (!clientInfoList) {
						JSONArray list = response
								.getJSONArray("clientInfoList");
						ArrayList<ClientInfoList> lists = new Gson().fromJson(
								list.toString(),
								new TypeToken<List<ClientInfoList>>() {
								}.getType());
						if (lists.size() > 0) {
							String client_name = lists.get(0).getClient_name();
							if (!client_name.equals("")) {
								textyujikehu.setText(client_name);
								yujibaifang.setVisibility(ViewGroup.VISIBLE);
								yujibaifang1.setVisibility(ViewGroup.VISIBLE);
								shijibaifang.setVisibility(ViewGroup.VISIBLE);
								shijibaifang1.setVisibility(ViewGroup.VISIBLE);
							}
						}
					}
					JSONArray applyHistory = (JSONArray) response
							.get("applyHistory");
					memu = new Gson().fromJson(applyHistory.toString(),
							new TypeToken<List<Memu_History>>() {
							}.getType());
					// 判断是否存在随行人员
					String mCurrentTime = DateUtil.getEnglishTime(currentTime);
					if (accompanyId.equals("")) {
						memusq_shenqingren.setText(createUserName + "(" + dept
								+ ")," + accompanyId + "\n(" + mCurrentTime
								+ "提交申请" + ")");
					} else {
						memusq_shenqingren.setText(createUserName + "(" + dept
								+ ")," + accompanyId + "随行" + "\n(" + mCurrentTime
								+ "提交申请" + ")");
					}
					memusq_waichushiyou.setText(mainContent);
					String mOutTime = DateUtil.getEnglishTime(outTime);
					String mBackTime = DateUtil.getEnglishTime(backTime);
					memusq_time.setText(mOutTime + "~" + mBackTime + "("
							+ startLocation + "到" + toLocation+")");
					// 判断是否存在交通费
					if (predictFare.equals("") || vehicle.equals("")
							|| gotonRoute.equals("")) {
						memusq_yujijiaotongfei.setText("无");
					} else {
						memusq_yujijiaotongfei.setText(predictFare + "元" + "乘坐"
								+ vehicle + "(" + gotonRoute + ")");
					}

				} else if (descId.equals("userSelfInnerTime")
						|| descId.equals("userSelfOutTime")) {
					// 待审批/填写外出详情，数据展示
					isnulljson(response);
					String createUserName = response
							.getString("createUserName");// 申请人名字
					String dept = response.getString("dept");// 申请人所属部门
					String accompanyId = response.getString("accompanyId");// 随行人员
					String currentTime = response.getString("currentTime");// 提交申请单事件
					String mainContent = response.getString("mainContent");// 外出事由
					String outTime = response.getString("outTime");// 外出时间
					String backTime = response.getString("backTime");// 回来时间
					String startLocation = response.getString("startLocation");// 起始地点
					String toLocation = response.getString("toLocation");// 外出地点
					userid = response.getString("userid");// userId
					applyId = response.getString("applyId");// applyId
					state = response.getString("state");// state
					isCar = response.getString("isCar");// 是否乘车

					String gotonRoute = response.getString("gotonRoute");// 乘车原因
					String vehicle = response.getString("vehicle");// 乘坐工具
					String predictFare = response.getString("predictFare");// 外出费用
					boolean clientInfoList = response.isNull("clientInfoList");// 客户拜访数据
					// 是否存在客户拜访数据是否为空
					if (!clientInfoList) {
						JSONArray list = response
								.getJSONArray("clientInfoList");
						ArrayList<ClientInfoList> lists = new Gson().fromJson(
								list.toString(),
								new TypeToken<List<ClientInfoList>>() {
								}.getType());
						if (lists.size() > 0) {
							String client_name = lists.get(0).getClient_name();
							if (!client_name.equals("")) {
								textyujikehu.setText(client_name);
								yujibaifang.setVisibility(ViewGroup.VISIBLE);
								yujibaifang1.setVisibility(ViewGroup.VISIBLE);
								shijibaifang.setVisibility(ViewGroup.VISIBLE);
								shijibaifang1.setVisibility(ViewGroup.VISIBLE);
								ToastUtil.showLongToast(context,
										"手机端暂不能填写客户拜访详情,请在网页端填写客户拜访详情！！！");
								my_approve_bt.setVisibility(ViewGroup.GONE);
							}
						}
					}
					JSONArray applyHistory = (JSONArray) response
							.get("applyHistory");
					memu = new Gson().fromJson(applyHistory.toString(),
							new TypeToken<List<Memu_History>>() {
							}.getType());
					// 判断是否存在随行人员
					String mCurrentTime = DateUtil.getEnglishTime(currentTime);
					if (accompanyId.equals("")) {
						memusq_shenqingren.setText(createUserName + "(" + dept
								+ ")," + accompanyId + "\n(" + mCurrentTime
								+ "提交申请" + ")");
					} else {
						memusq_shenqingren.setText(createUserName + "(" + dept
								+ ")," + accompanyId + "随行" + "\n(" + mCurrentTime
								+ "提交申请" + ")");
					}
					memusq_waichushiyou.setText(mainContent);
					String mOutTime = DateUtil.getEnglishTime(outTime);
					String mBackTime = DateUtil.getEnglishTime(backTime);
					memusq_time.setText(mOutTime + "~" + mBackTime + "("
							+ startLocation + "到" + toLocation+")");
					// 判断是否存在交通费
					if (predictFare.equals("") || vehicle.equals("")
							|| gotonRoute.equals("")) {
						memusq_yujijiaotongfei.setText("无");
					} else {
						memusq_yujijiaotongfei.setText(predictFare + "元" + "乘坐"
								+ vehicle + "(" + gotonRoute + ")");
					}
					edit_t1.setText(predictFare);
					edit_t2.setText(gotonRoute);

				} else if (descId.equals("serverStaff")) {
					// 待审批/核实交通费，填写核实交通费，数据展示
					isnulljson(response);
					String createUserName = response
							.getString("createUserName");// 申请人名字
					String dept = response.getString("dept");// 申请人所属部门
					String accompanyId = response.getString("accompanyId");// 随行人员
					String currentTime = response.getString("currentTime");// 提交申请单事件
					String mainContent = response.getString("mainContent");// 外出事由
					String outTime = response.getString("outTime");// 外出时间
					String backTime = response.getString("backTime");// 回来时间
					String startLocation = response.getString("startLocation");// 起始地点
					String toLocation = response.getString("toLocation");// 外出地点
					userid = response.getString("userid");// userId
					applyId = response.getString("applyId");// applyId
					state = response.getString("state");// state
					isCar = response.getString("isCar");// 是否乘车

					String gotonRoute = response.getString("gotonRoute");// 乘车原因
					String vehicle = response.getString("vehicle");// 乘坐工具
					String predictFare = response.getString("predictFare");// 外出费用

					String realityRoute1, detail1;
					boolean realityFare = response.isNull("realityFare");// 实际乘车费用
					boolean realityRoute = response.isNull("realityRoute");// 实际乘车路线
					boolean detail = response.isNull("detail");// 外出详情
					boolean clientInfoList = response.isNull("clientInfoList");// 客户拜访数据

					if (realityFare || realityRoute || detail) {
						realityFare1 = "";
						realityRoute1 = "";
						detail1 = "";
					} else {
						realityFare1 = response.getString("realityFare");
						realityRoute1 = response.getString("realityRoute");
						detail1 = response.get("detail").toString();
					}
					waichuxiangqing_s.setText(Html.fromHtml(detail1));
					// 是否存在客户拜访数据是否为空
					if (!clientInfoList) {
						JSONArray list = response
								.getJSONArray("clientInfoList");
						ArrayList<ClientInfoList> lists = new Gson().fromJson(
								list.toString(),
								new TypeToken<List<ClientInfoList>>() {
								}.getType());
						if (lists.size() > 0) {
							String client_name = lists.get(0).getClient_name();
							waichuxiangqing_s.setText(Html.fromHtml(lists.get(0).getDetail()));
							if (!client_name.equals("")) {
								textyujikehu.setText(client_name);
								yujibaifang.setVisibility(ViewGroup.VISIBLE);
								yujibaifang1.setVisibility(ViewGroup.VISIBLE);
								shijibaifang.setVisibility(ViewGroup.VISIBLE);
								shijibaifang1.setVisibility(ViewGroup.VISIBLE);
							}
						}
					}

					JSONArray applyHistory = (JSONArray) response
							.get("applyHistory");
					memu = new Gson().fromJson(applyHistory.toString(),
							new TypeToken<List<Memu_History>>() {
							}.getType());
					// 判断是否存在随行人员
					String mCurrentTime = DateUtil.getEnglishTime(currentTime);
					if (accompanyId.equals("")) {
						memusq_shenqingren.setText(createUserName + "(" + dept
								+ ")," + accompanyId + "\n(" + mCurrentTime
								+ "提交申请" + ")");
					} else {
						memusq_shenqingren.setText(createUserName + "(" + dept
								+ ")," + accompanyId + "随行" + "\n(" + mCurrentTime
								+ "提交申请" + ")");
					}
					memusq_waichushiyou.setText(mainContent);
					String mOutTime = DateUtil.getEnglishTime(outTime);
					String mBackTime = DateUtil.getEnglishTime(backTime);
					memusq_time.setText(mOutTime + "~" + mBackTime + "("
							+ startLocation + "到" + toLocation+")");

					// 判断是否存在交通费
					if (predictFare.equals("") || vehicle.equals("")
							|| gotonRoute.equals("")) {
						memusq_yujijiaotongfei.setText("无");
					} else {
						memusq_yujijiaotongfei.setText(predictFare + "元" + "乘坐"
								+ vehicle + "(" + gotonRoute + ")");
					}
					shijijiaotongfei_s.setText(realityFare1 + "元" + "乘坐"
							+ vehicle + "(" + realityRoute1 + ")");

				} else {
					// 申请单列表/已审批/所有数据展示
					isnulljson(response);
					String accompanyId = response.getString("accompanyId");// 随行人员
					String currentTime = response.getString("currentTime");// 提交申请单事件
					String mainContent = response.getString("mainContent");// 外出事由
					String outTime = response.getString("outTime");// 外出时间
					String backTime = response.getString("backTime");// 回来时间
					String startLocation = response.getString("startLocation");// 起始地点
					String toLocation = response.getString("toLocation");// 外出地点
					userid = response.getString("userid");// userId
					applyId = response.getString("applyId");// applyId
					state = response.getString("state");// state
					isCar = response.getString("isCar");// 是否乘车

					String gotonRoute = response.getString("gotonRoute");// 乘车原因
					String vehicle = response.getString("vehicle");// 乘坐工具
					String predictFare = response.getString("predictFare");// 外出费用
					String reality, route, details;
					boolean realityFare = response.isNull("realityFare");// 实际乘车费用
					boolean realityRoute = response.isNull("realityRoute");// 实际乘车路线
					boolean detail = response.isNull("detail");// 外出详情
					boolean realityApplyFare = response
							.isNull("realityApplyFare");// 实报交通费
					boolean clientInfoList = response.isNull("clientInfoList");// 客户拜访数据

					if (realityFare || realityRoute || detail) {
						reality = "";
						route = "";
						details = "";
					} else {
						reality = response.getString("realityFare");
						route = response.getString("realityRoute");
						details = response.get("detail").toString();
						waichuxiangqing_s.setText(Html.fromHtml(details));
						shijijiaotongfei_s.setText(reality + "元" + "乘坐"
								+ vehicle + "(" + route + ")");
						shijijiaotongfei.setVisibility(ViewGroup.VISIBLE);
						sqview1.setVisibility(ViewGroup.VISIBLE);
						waichuxiangqing.setVisibility(ViewGroup.VISIBLE);
						sqview3.setVisibility(ViewGroup.VISIBLE);

					}
					if (realityApplyFare) {
						shibaojiaotongfei.setVisibility(ViewGroup.GONE);
						sqview2.setVisibility(ViewGroup.GONE);
					} else {
						String realityapplyfare = response
								.getString("realityApplyFare");
						shibaojiaotongfei_s.setText(realityapplyfare);
						shibaojiaotongfei.setVisibility(ViewGroup.VISIBLE);
						sqview2.setVisibility(ViewGroup.VISIBLE);
					}
					// 是否存在客户拜访数据是否为空
					if (!clientInfoList) {
						JSONArray list = response
								.getJSONArray("clientInfoList");
						ArrayList<ClientInfoList> lists = new Gson().fromJson(
								list.toString(),
								new TypeToken<List<ClientInfoList>>() {
								}.getType());
						if (lists.size() > 0) {
							String client_name = lists.get(0).getClient_name();
							if(null!=lists.get(0).getDetail()){
								waichuxiangqing_s.setText(Html.fromHtml(lists.get(0).getDetail()));	
							}
							if (!client_name.equals("")) {
								textyujikehu.setText(client_name);
								yujibaifang.setVisibility(ViewGroup.VISIBLE);
								yujibaifang1.setVisibility(ViewGroup.VISIBLE);
								shijibaifang.setVisibility(ViewGroup.VISIBLE);
								shijibaifang1.setVisibility(ViewGroup.VISIBLE);
							}
						}
					}

					JSONArray applyHistory = (JSONArray) response
							.get("applyHistory");
					memu = new Gson().fromJson(applyHistory.toString(),
							new TypeToken<List<Memu_History>>() {
							}.getType());

					String user = "";
					String dept = "";
					try {
						if (response.getString("createUserName").equals("")) {
							user = memu.get(memu.size() - 1).getAppUser();
							dept = memu.get(memu.size() - 1).getDept();

						} else {
							user = response.getString("createUserName");
							dept = response.getString("dept");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 随行人员判断是否为空
					String mCurrentTime = DateUtil.getEnglishTime(currentTime);
					if (accompanyId.equals("")) {
						memusq_shenqingren.setText(user + "(" + dept + "),"
								+ accompanyId + "\n(" + mCurrentTime + "提交申请"
								+ ")");
					} else {
						memusq_shenqingren.setText(user + "(" + dept + "),"
								+ accompanyId + "随行" + "\n(" + mCurrentTime
								+ "提交申请" + ")");
					}
					memusq_waichushiyou.setText(mainContent);
					String mOutTime = DateUtil.getEnglishTime(outTime);
					String mBackTime = DateUtil.getEnglishTime(backTime);
					memusq_time.setText(mOutTime + "~" + mBackTime + "("
							+ startLocation + "到" + toLocation+")");

					// 判断是否存在交通费
					if (predictFare.equals("") || vehicle.equals("")
							|| gotonRoute.equals("")) {
						memusq_yujijiaotongfei.setText("无");
					} else {
						memusq_yujijiaotongfei.setText(predictFare + "元" + "乘坐"
								+ vehicle + "(" + gotonRoute + ")");
					}
					if (state.equals("2")) {
						String App_state = memu.get(0).getApp_state();
						if (App_state.equals("4")) {
							yiwancheng_img.setImageResource(R.mipmap.veto);
							yiwancheng_img.setVisibility(ViewGroup.VISIBLE);
							// 否决
						} else {

							if (App_state.equals("6") && isCar.equals("1")) {
								// 不报销
							} else {
								yiwancheng_img
										.setImageResource(R.mipmap.complete);
								yiwancheng_img.setVisibility(ViewGroup.VISIBLE);
							}
						}

					}

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dhu.dismissWaitingDialog();

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			dhu.dismissWaitingDialog();
			if (statusCode == 0
					&& throwable.toString().contains("HttpHostConnect")) {
				ToastManager.getInstance(context).showToast("网络连接失败,请检查网络设置！");
			} else if (statusCode == 200) {
				ToastManager.getInstance(context).showToast("账号异常请联系技术支持部！");
			} else if (statusCode == 0
					&& throwable.toString().contains("Timeout")) {
				ToastManager.getInstance(context).showToast("服务连接超时！");
			}
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			Log.e("lyt", statusCode + throwable.toString() + responseString);
			ToastUtil.showToast(context, "网络连接失败！");
			dhu.dismissWaitingDialog();
			super.onFailure(statusCode, headers, responseString, throwable);
		}
	}
	public JSONObject isnulljson(JSONObject response){
		try {
		if(response.isNull("createUserName")){
			response.put("createUserName", "");
			
		}else if(response.isNull("dept")){
			response.put("dept", "");

		}else if(response.isNull("accompanyId")){
			response.put("accompanyId", "");

		}else if(response.isNull("currentTime")){
			response.put("currentTime", "");

		}else if(response.isNull("mainContent")){
			response.put("mainContent", "");

		}else if(response.isNull("outTime")){
			response.put("outTime", "");

		}else if(response.isNull("backTime")){
			response.put("backTime", "");

		}else if(response.isNull("startLocation")){
			response.put("startLocation", "");

		}else if(response.isNull("toLocation")){
			response.put("toLocation", "");

		}else if(response.isNull("userid")){
			response.put("userid", "");

		}else if(response.isNull("applyId")){
			response.put("applyId", "");

		}else if(response.isNull("state")){
			response.put("state", "");

		}else if(response.isNull("isCar")){
			response.put("isCar", "");

		}else if(response.isNull("gotonRoute")){
			response.put("gotonRoute", "");

		}else if(response.isNull("vehicle")){
			response.put("vehicle", "");

		}else if(response.isNull("predictFare")){
			response.put("predictFare", "");

		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	// 外出申请单审批、填写详情、核实交通费的回调类
	private class JsonHandlers extends JsonHttpHandles {
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			String ss = response.toString();
			Log.e("lyt", ss);
			try {
				if (response.getString("message").equals("success")) {
					ToastUtil.showToast(context, "审批成功！");
					Intent intent = new Intent();
					intent.setAction("meiyinsp");
					Out_Memu_Sq.this.sendBroadcast(intent);
					Out_Memu_Sq.this.finish();
				} else {
					ToastUtil.showToast(context, "审批失败！");
					Out_Memu_Sq.this.finish();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dhu.dismissWaitingDialog();

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			ToastUtil.showToast(context, "网络连接失败！");
			// Log.e("lyt", errorResponse.toString());
			dhu.dismissWaitingDialog();
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			// Log.e("lyt", responseString.toString());
			ToastUtil.showToast(context, "网络连接失败！");
			dhu.dismissWaitingDialog();
			super.onFailure(statusCode, headers, responseString, throwable);
		}
	}


	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.dialog_radio_group) {
			switch (arg1) {
			case R.id.rgs_1:
				hongdian.setVisibility(ViewGroup.GONE);
				app_atate = 3;
				break;
			case R.id.rgs_2:
				hongdian.setVisibility(ViewGroup.VISIBLE);
				app_atate = 4;
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 待审核的审批流程操作事件
	 */
	private void desk() {
		TextView my_approve_text = (TextView) findViewById(R.id.my_approve_text);
		if (descId.equals("deptManager") || descId.equals("deptGenManager")||descId.equals("upManager")) {
			my_approve_text.setText("审核");
			my_approve_bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					LinearLayout shenhe = (LinearLayout) LayoutInflater.from(
							context).inflate(R.layout.dialog_approval, null);

					builder.setView(shenhe);
					dialog = builder.create();

					dialog.show();
					dialog.getWindow().clearFlags(
							WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
					dialog.getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					// InputMethodManager
					// imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
					// imm.showSoftInput(shenhe, 0);
					// imm.toggleSoftInput(0,
					// InputMethodManager.HIDE_NOT_ALWAYS);
					Button shenpi = (Button) shenhe
							.findViewById(R.id.dialog_approval);
					final EditText app_view = (EditText) shenhe
							.findViewById(R.id.app_view);
					RadioGroup rgsss = (RadioGroup) shenhe
							.findViewById(R.id.dialog_radio_group);
					hongdian = (TextView) shenhe.findViewById(R.id.dialog_star);

					rgsss.setOnCheckedChangeListener(Out_Memu_Sq.this);
					//
					shenpi.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							appview = app_view.getText().toString();
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
							dialog.show();
							dialog.getWindow().setContentView(shenpi);
							button_t.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
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
									dhu = new Dialog_Http_Util();
									dhu.showWaitingDialog(Out_Memu_Sq.this,
											"正在审批", false);
									AsyncHttpclient_Util.post(
											APIURL.CHECK.MEMU_SPAPI, context,
											param, new JsonHandlers());
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
			});
		} else if (descId.equals("userSelfInnerTime")
				|| descId.equals("userSelfOutTime")) {
			my_approve_text.setText("填写完毕");
			my_approve_bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					LinearLayout shenpi = (LinearLayout) LayoutInflater.from(
							context).inflate(R.layout.submit_dialog, null);
					TextView heads = (TextView) shenpi.findViewById(R.id.heads);
					Button button_t = (Button) shenpi
							.findViewById(R.id.button_t);
					Button button_f = (Button) shenpi
							.findViewById(R.id.button_f);
					heads.setText("您确定要提交详情吗？");
					adb.show();
					adb.getWindow().setContentView(shenpi);
					button_t.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {

							String ti1 = edit_t1.getText().toString();
							String ti2 = edit_t2.getText().toString();
							String ti3 = edit_t3.getText().toString();
							if (ti1.equals("") || ti2.equals("")
									|| ti3.equals("")) {
								ToastUtil.showToast(context, "外出详情不能为空！");
								return;
							}
							// TODO Auto-generated method stub
							RequestParams param = new RequestParams();
							param.put("taskId", TaskId);
							param.put("key", key);
							param.put("applyNameState", applyNameState);
							param.put("id", applyId);
							param.put("proc_inst_id", procInstId);
							param.put("userid", userid);
							param.put("state", state);
							param.put("isCar", isCar);
							param.put("realityFare", ti1);
							param.put("realityRoute", ti2);
							param.put("detail", ti3);
							param.put("descId", descId);
							dhu = new Dialog_Http_Util();
							dhu.showWaitingDialog(Out_Memu_Sq.this, "正在提交",
									false);
							AsyncHttpclient_Util.post(APIURL.CHECK.MEMU_XQAPI,
									context, param, new JsonHandlers());
							adb.dismiss();
						}
					});

					button_f.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							adb.dismiss();
						}
					});
				}
			});
		} else if (descId.equals("serverStaff")) {
			my_approve_text.setText("核实情况");
			shijijiaotongfei.setVisibility(ViewGroup.VISIBLE);
			sqview1.setVisibility(ViewGroup.VISIBLE);
			waichuxiangqing.setVisibility(ViewGroup.VISIBLE);
			sqview3.setVisibility(ViewGroup.VISIBLE);

			my_approve_bt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					LinearLayout heshifeiyong_item = (LinearLayout) LayoutInflater
							.from(context).inflate(R.layout.heshifeiyong_item,
									null);

					builder.setView(heshifeiyong_item);
					dialogs = builder.create();

					dialogs.show();
					dialogs.getWindow().clearFlags(
							WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
					dialogs.getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					Button heshifeiyong_true = (Button) heshifeiyong_item
							.findViewById(R.id.heshifeiyong_true);
					heshi_app_view = (EditText) heshifeiyong_item
							.findViewById(R.id.heshi_app_view);
					heshi_app_view.setText(realityFare1);
					shuoming = (EditText) heshifeiyong_item
							.findViewById(R.id.shuoming);

					heshifeiyong_true.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							LinearLayout shenpi = (LinearLayout) LayoutInflater
									.from(context).inflate(
											R.layout.submit_dialog, null);
							TextView heads = (TextView) shenpi
									.findViewById(R.id.heads);
							Button button_t = (Button) shenpi
									.findViewById(R.id.button_t);
							Button button_f = (Button) shenpi
									.findViewById(R.id.button_f);
							heads.setText("您确定要提交数据吗？");
							adb.show();
							adb.getWindow().setContentView(shenpi);
							button_t.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {

									String shuo = shuoming.getText().toString();
									String heshifeiyong = heshi_app_view
											.getText().toString();
									if (heshifeiyong.equals("")) {
										ToastUtil.showToast(context,
												"实报交通费不能为空！");
										return;
									}
									// TODO Auto-generated method stub
									RequestParams param = new RequestParams();
									param.put("taskId", TaskId);
									param.put("key", key);
									param.put("applyNameState", applyNameState);
									param.put("id", applyId);
									param.put("proc_inst_id", procInstId);
									param.put("userid", userid);
									param.put("state", state);
									param.put("realityApplyFare", heshifeiyong);
									param.put("app_view", shuo);

									dhu = new Dialog_Http_Util();
									dhu.showWaitingDialog(Out_Memu_Sq.this,
											"正在提交", false);
									AsyncHttpclient_Util.post(
											APIURL.CHECK.MEMU_HSAPI, context,
											param, new JsonHandlers());
									adb.dismiss();
								}
							});

							button_f.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									adb.dismiss();
								}
							});
						}
					});

				}
			});

		}
	}
}
