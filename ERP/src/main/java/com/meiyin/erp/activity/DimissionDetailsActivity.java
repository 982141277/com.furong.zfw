package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
 * @param离职申请单详情
 * @Time 2016-6-14
 */
public class DimissionDetailsActivity extends Activity {
	private Context context;
	private ViewHolder holder;
	private ArrayList<Memu_History> mlist;
	private AlertDialog dialog;
	private LinearLayout dimissionapproval;
	private ImageView dimission_detail_typeimg;
	private String userid, state;// 审批所需参数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dimissiondetails_main);
		context = getApplicationContext();
		SharedPreferences sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);

		/*
		 * 初始化
		 */
		initHeader();
		initView();
		initData(sp);
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
		headtitletext.setText("离职申请单");
	}

	/*
	 * 初始化数据
	 */
	private void initData(SharedPreferences sp) {
		// TODO Auto-generated method stub
		AsyncHttpClientUtil async = new AsyncHttpClientUtil();
		Dialog_Http_Util dialog_util = new Dialog_Http_Util();
		mlist = new ArrayList<Memu_History>();// 审批历史数据
		dialog = new AlertDialog.Builder(this).create();// 审批历史dialog
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		dimissionapproval = (LinearLayout) findViewById(R.id.dimissionapproval);
		dimission_detail_typeimg = (ImageView) findViewById(R.id.dimission_detail_typeimg);
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
			dimissionapproval.setVisibility(ViewGroup.GONE);
		} else if (name.equals("待审批事项")) {
			param.put("taskId", TaskId);
			param.put("key", key);
			param.put("applyNameState", applyNameState);
			param.put("applyId", applyId);
			param.put("procInstId", procInstId);
			param.put("isFresh", true);
			param.put("descId", descId);
			dimissionapproval.setVisibility(ViewGroup.GONE);
			// 待审批审批接口
			// approval(builder, async, TaskId, key, applyNameState, applyId,
			// procInstId,dialog_util);
		} else if (name.equals("已审批事项")) {
			param.put("key", key);
			param.put("applyNameState", applyNameState);
			param.put("applyId", applyId);
			param.put("procInstId", procInstId);
			param.put("taskId", TaskId);
			param.put("isFresh", true);
			dimissionapproval.setVisibility(ViewGroup.GONE);
		}
		Dialog progressDialog = dialog_util.showWaitingDialog(this, "正在刷新",
				false);
		async.post(APIURL.CHECK.MEMU_SQAPI, context, param, new JsonHandler(
				context, progressDialog));
	}

	public static class ViewHolder {
		private TextView dimission_detail_applicant;// 申请人
		private TextView dimission_detail_time;// 离职申请日期
		private TextView dimissiondetail_entrytime;// 入职日期
		private TextView dimission_detail_wish_time;// /希望离职日期
		private TextView dimission_detail_report;// 自我评价

	}

	/*
	 * 初始化VIEW
	 */
	private void initView() {
		// TODO Auto-generated method stub
		holder = new ViewHolder();
		// 审批历史
		LinearLayout history_detail = (LinearLayout) findViewById(R.id.dimission_history_detail);
		// 请假详情
		holder.dimission_detail_applicant = (TextView) findViewById(R.id.dimission_detail_applicant);// 申请人
		holder.dimission_detail_time = (TextView) findViewById(R.id.dimission_detail_time);// 离职申请日期
		holder.dimissiondetail_entrytime = (TextView) findViewById(R.id.dimissiondetail_entrytime);// 入职日期
		holder.dimission_detail_wish_time = (TextView) findViewById(R.id.dimission_detail_wish_time);// 希望离职日期
		holder.dimission_detail_report = (TextView) findViewById(R.id.dimission_detail_report);// 离职报告

		history_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mlist.size() < 1) {
					ToastManager.getInstance(context).showToast("网络连接失败！");
					return;
				}
				LinearLayout list_dialog = (LinearLayout) LayoutInflater.from(
						context).inflate(R.layout.list_dialog, null);
				Memu_History_Adapter adapter = new Memu_History_Adapter(context);
				ListView history_list = (ListView) list_dialog
						.findViewById(R.id.history_list);
				adapter.setList(mlist);
				history_list.setAdapter(adapter);
				dialog.show();
				dialog.getWindow().setContentView(list_dialog);
			}
		});
	}

	// 员工离职申请单详情
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
			String enter_date = "";// 入职时间
			String currentTime = "";// 填表时间
			String realDimission_time = "";//
			String dimission_reason = "";// 离职报告
			try {

				realDimission_time = response.getString("realDimission_time");
				enter_date = response.getString("enter_date");
				currentTime = response.getString("currentTime");
				dimission_reason = response.getString("dimission_reason");
				applyHistory = (JSONArray) response
						.getJSONArray("applyHistory");
				state = response.getString("state");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;
			holder.dimission_detail_time.setText(realDimission_time);
			holder.dimissiondetail_entrytime.setText(enter_date);
			holder.dimission_detail_wish_time.setText(currentTime);
			holder.dimission_detail_report.setText(Html
					.fromHtml(dimission_reason));
			// 审批历史
			mlist = new Gson().fromJson(applyHistory.toString(),
					new TypeToken<List<Memu_History>>() {
					}.getType());// 审批历史
			try {
				if (response.isNull("createUserName")) {
					String user = mlist.get(mlist.size() - 1).getAppUser();
					String dept = mlist.get(mlist.size() - 1).getDept();
					holder.dimission_detail_applicant.setText(user + "(" + dept
							+ ")");
				} else {
					createUserName = response.getString("createUserName");
					holder.dimission_detail_applicant.setText(createUserName);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null != state && state.equals("2")) {
				if(mlist.size()>0){
				String App_state = mlist.get(0).getApp_state();
				if (App_state.equals("4")) {
					dimission_detail_typeimg.setImageResource(R.mipmap.veto);
					dimission_detail_typeimg.setVisibility(ViewGroup.VISIBLE);
					// 否决
				} else {
					if (!App_state.equals("6")) {
						dimission_detail_typeimg
								.setImageResource(R.mipmap.complete);
						dimission_detail_typeimg
								.setVisibility(ViewGroup.VISIBLE);
					}
				}

			}
			}
		}

	}

}
