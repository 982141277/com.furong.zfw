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
 * @param公司公章(证照)申请单详情
 * @Time 2016-6-14
 */
public class CompanyChopDetailsActivity extends Activity{

	private Context context;
	private ViewHolder holder;
	private ArrayList<Memu_History> memu;
	private AlertDialog dialog;
	private LinearLayout company_approval;
	private ImageView company_typeimg;
	private String userid, state;// 审批所需参数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.company_details_main);
		context = getApplicationContext();
		SharedPreferences sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);

		/*
		 * 初始化
		 */
		initHeader();
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
		headtitletext.setText("公司公章(证照)申请单");
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
		company_approval = (LinearLayout) findViewById(R.id.company_approval);
		company_typeimg = (ImageView) findViewById(R.id.company_typeimg);
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
			company_approval.setVisibility(ViewGroup.GONE);
		} else if (name.equals("待审批事项")) {
			param.put("taskId", TaskId);
			param.put("key", key);
			param.put("applyNameState", applyNameState);
			param.put("applyId", applyId);
			param.put("procInstId", procInstId);
			param.put("isFresh", true);
			param.put("descId", descId);
			company_approval.setVisibility(ViewGroup.GONE);
			// 待审批审批接口
//			approval(builder, async, TaskId, key, applyNameState, applyId,
//					procInstId,dialog_util);
		} else if (name.equals("已审批事项")) {
			param.put("key", key);
			param.put("applyNameState", applyNameState);
			param.put("applyId", applyId);
			param.put("procInstId", procInstId);
			param.put("taskId", TaskId);
			param.put("isFresh", true);
			company_approval.setVisibility(ViewGroup.GONE);
		}
		Dialog progressDialog = dialog_util.showWaitingDialog(this, "正在刷新",
				false);
		async.post(APIURL.CHECK.MEMU_SQAPI, context, param, new JsonHandler(
				context, progressDialog));
	}
	public static class ViewHolder {
		private TextView company_detail_applicant;// 填表人
		private TextView company_detail_dept;// 所属部门
		private TextView company_detail_form_time;//填表时间
		private TextView company_detail_type;///印章类型
		private TextView company_detail_content;// 盖章事由

	}
	/*
	 * 初始化VIEW
	 */
	private void initView() {
		// TODO Auto-generated method stub
		holder = new ViewHolder();
		// 审批历史
		LinearLayout history_detail = (LinearLayout) findViewById(R.id.company_history_detail);
		//请假详情
		holder.company_detail_applicant = (TextView)findViewById(R.id.company_detail_applicant);// 填表人
		holder.company_detail_dept = (TextView)findViewById(R.id.company_detail_dept);// 所属部门
		holder.company_detail_form_time = (TextView)findViewById(R.id.company_detail_form_time);//填表时间
		holder.company_detail_type = (TextView)findViewById(R.id.company_detail_type);//印章类型
		holder.company_detail_content = (TextView)findViewById(R.id.company_detail_content);// 盖章事由
		
		history_detail.setOnClickListener(new OnClickListener() {

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
	}
	//
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
			String cachet_type = "";// 盖章类型
			String currentTime = "";// 填表时间
			String cachet_reason = "";// 盖章事由
			try {
				createUserName = response.getString("createUserName");
				dept = response.getString("dept");
				cachet_type = response.getString("cachet_type");
				currentTime = response.getString("currentTime");
				cachet_reason = response.getString("cachet_reason");
				applyHistory = (JSONArray) response.get("applyHistory");
				state = response.getString("state");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			holder.company_detail_applicant.setText(createUserName);
			holder.company_detail_dept.setText(dept);
			holder.company_detail_form_time.setText(currentTime);
			holder.company_detail_type.setText(cachet_type);
			holder.company_detail_content.setText(Html.fromHtml(cachet_reason));
			//审批历史
			memu = new Gson().fromJson(applyHistory.toString(),
					new TypeToken<List<Memu_History>>() {
					}.getType());// 审批历史
			if (null!=state&&state.equals("2")) {
				String App_state = memu.get(0).getApp_state();
				if (App_state.equals("4")) {
					company_typeimg.setImageResource(R.mipmap.veto);
					company_typeimg.setVisibility(ViewGroup.VISIBLE);
					// 否决
				} else {
					if (!App_state.equals("6")) {
						company_typeimg.setImageResource(R.mipmap.complete);
						company_typeimg.setVisibility(ViewGroup.VISIBLE);
					}
				}

			}
		}

	}
}
