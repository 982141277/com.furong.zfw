package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.Spinner_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Spinner_Entity;
import com.meiyin.erp.util.AndroidUtil;
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
 * @param新增费用申请单
 * @Time 2016-6-17
 */
public class ExpenseActivity extends Activity {


	private Context context;
	private Dialog_Http_Util dialog_util;
	private AsyncHttpClientUtil async;
	private SharedPreferences sp;
	private EditText expense_number, expense_mount, expense_new_cause,
			expense_new_reason;
	private ArrayList<Spinner_Entity> spn;
	private String type, name;
	private Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_expense_main);
		context = getApplicationContext();
		activity=this;
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
				finish();
			}
		});
		TextView headtitletext = (TextView) findViewById(R.id.headtitletext);
		headtitletext.setText("费用申请单");
	}

	/*
	 * 初始化View
	 */
	private void initView() {
		// TODO Auto-generated method stub
		dialog_util = new Dialog_Http_Util();
		async = new AsyncHttpClientUtil();
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		spn = new ArrayList<Spinner_Entity>();
		Spinner expense_spn = (Spinner) findViewById(R.id.expense_spn);// 类型
		final TextView expense_number_type = (TextView) findViewById(R.id.expense_number_type);// 。。
		final LinearLayout expense_number_linear = (LinearLayout) findViewById(R.id.expense_number_linear);// 。。
		final ImageView expense_imgs = (ImageView) findViewById(R.id.expense_imgs);// 。。
		String expenseTypeList =getIntent().getExtras().getString(
				"expenseTypeList");
		try {
			JSONArray arry = new JSONArray(expenseTypeList);
			for (int i = 0; i < arry.length(); i++) {
				String code = arry.getJSONObject(i).getString("code");
				String code_desc = arry.getJSONObject(i).getString("code_desc");
				spn.add(new Spinner_Entity(code_desc, code));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 类型
		Spinner_Adapter spinnerAdapter = new Spinner_Adapter(context);
		spinnerAdapter.setList(spn);
		expense_spn.setAdapter(spinnerAdapter);
		expense_spn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				type = spn.get(arg2).getType_id();
				name = spn.get(arg2).getType_name();
				if (name.equals("其他")) {
					expense_number_linear.setVisibility(ViewGroup.GONE);
					expense_imgs.setVisibility(ViewGroup.GONE);
					
				} else if (name.equals("话费充值")) {
					expense_number_type.setText("电话号码：");
					expense_number_linear.setVisibility(ViewGroup.VISIBLE);
					expense_imgs.setVisibility(ViewGroup.VISIBLE);
					
				} else {
					expense_number_type.setText("车牌号码：");
					expense_number_linear.setVisibility(ViewGroup.VISIBLE);
					expense_imgs.setVisibility(ViewGroup.VISIBLE);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		expense_number = (EditText) findViewById(R.id.expense_number);// 号码
		expense_mount = (EditText) findViewById(R.id.expense_mount);// 金额
		expense_new_cause = (EditText) findViewById(R.id.expense_new_cause);// 原因
		expense_new_reason = (EditText) findViewById(R.id.expense_new_reason);// 描述
		// 提交请假申请单
		Button expense_submit = (Button) findViewById(R.id.expense_submit);
		expense_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String number = expense_number.getText().toString();// 号码
				final String mount = expense_mount.getText().toString();// 金额
				final String cause = expense_new_cause.getText().toString();// 原因
				final String reason = expense_new_reason.getText().toString();// 描述
				if (mount.equals("") || cause.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("必填项不能为空！");
					dialog.dismiss();
					return;
				}
				if (name.equals("其他")) {
					if(reason.length()<1){
					ToastManager.getInstance(context)
							.showToastcenter("详细描述不能为空！");
					dialog.dismiss();
					return;
					}
				}
				if(cause.length()<10){
					ToastManager.getInstance(context)
					.showToastcenter("申请费用原因不能少于10个字！");
					dialog.dismiss();
					return;
				}
				if(reason.length()>0&&reason.length()<20){
					ToastManager.getInstance(context)
					.showToastcenter("详细描述不能少于20个字！");
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
						param.put("expenseType", type);// 请假类型
						param.put("applyDesc", cause);// 原因
						param.put("applyMount", mount);// 费用
						param.put("explanation", reason);// 详细描述
						String phoneNums = "";
						String plateNums = "";
						if (name.equals("其他")) {
							phoneNums = "";
							plateNums = "";
						}
						if (name.equals("话费充值")) {
							phoneNums = number;
							plateNums = "";
						} else {
							phoneNums = "";
							plateNums = number;
						}
						param.put("phoneNum", phoneNums);// 电话号码
						param.put("plateNum", plateNums);// 车牌号码
						param.put("stateMent",
								getIntent().getStringExtra("stateMent"));
						Dialog progressDialog = dialog_util.showWaitingDialog(
								ExpenseActivity.this, "正在刷新", false);
						async.post(APIURL.CHECK.ADDNEWEXPENSE, context, param,
								new JsonHandler(ExpenseActivity.this,
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

	// 请假申请单提交回调类
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
					ExpenseActivity.this.finish();
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
}
