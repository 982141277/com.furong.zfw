package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.Requisition_Swipe_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.datepicker.DatePick;
import com.meiyin.erp.entity.Requisition_Swipe_Entity;
import com.meiyin.erp.ui.SwipeListView;
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

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @param新增请购单
 * @Time 2016-5-25
 */
public class RequisitionActivity extends Activity {
	private TextView need_time_text;
	private TextView requisition_username;
	private Context context;
	private Dialog_Http_Util dialog_util;
	private AsyncHttpClientUtil async;
	private SharedPreferences sp;
	private JSONArray array;
	private ArrayList<Requisition_Swipe_Entity> list;
	private Requisition_Swipe_Adapter adapter;
	private AlertDialog dialog;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.requisition_main);
		context = getApplicationContext();
		activity=this;
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		/*
		 * 初始化
		 */
		initHeader();
		initView();
		initDate();

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
	 * 初始化VIEW
	 */
	private void initView() {
		// TODO Auto-generated method stub
		dialog = new AlertDialog.Builder(this).create();
		SwipeListView requisition_swipelistview = (SwipeListView) findViewById(R.id.requisition_swipelistview);
		// 获取list头部
		View head = LayoutInflater.from(context).inflate(
				R.layout.requisiton_list_head, null);
		requisition_username = (TextView) head
				.findViewById(R.id.requisition_username);
		final EditText requisition_project_name = (EditText) head
				.findViewById(R.id.requisitions_project_names);// 请购项目名称
		final EditText article_resume = (EditText) head
				.findViewById(R.id.article_resume);// 请购物品简述
		final EditText reason_requisition_edit = (EditText) head
				.findViewById(R.id.reason_requisition_edit);// 请购物品原因
		need_time_text = (TextView) head
				.findViewById(R.id.need_time_text);// 需用时间
		need_time_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeLayout date_dialog = (RelativeLayout) LayoutInflater
						.from(context).inflate(R.layout.date_dialog, null);
				date_dialog.addView(new DatePick(RequisitionActivity.this,need_time_text).getDataPick());
				dialog.show();
				dialog.getWindow().setContentView(date_dialog);
				need_time_text.setText(DateUtil.getCurrentTimeStr());
			}
		});

		requisition_swipelistview.addHeaderView(head);
		// 物品列表插入数据
		list = new ArrayList<Requisition_Swipe_Entity>();
		adapter = new Requisition_Swipe_Adapter(this,
				requisition_swipelistview.getRightViewWidth());
		adapter.setList(list);
		requisition_swipelistview.setAdapter(adapter);
		adapter.setOnRightItemClickListener(new Requisition_Swipe_Adapter.onRightItemClickListener() {

			@Override
			public void onRightItemClick(View v, int position) {
				// TODO Auto-generated method stub
				Requisition_Swipe_Entity mlist = list.get(position);
				Intent intent = new Intent();
				intent.setClass(context, AddNewArticleActivity.class);
				intent.putExtra("array", array.toString());
				Bundle bl = new Bundle();
				bl.putSerializable("mlist", (Serializable) mlist);
				intent.putExtras(bl);
				intent.putExtra("position", position);
				startActivityForResult(intent, 1);
			}
		});
		adapter.setOnLeftItemClickListener(new Requisition_Swipe_Adapter.onLeftItemClickListener() {

			@Override
			public void onLeftItemClick(View v, int position) {
				// TODO Auto-generated method stub
				list.remove(position);
				adapter.setList(list);
				adapter.notifyDataSetChanged();
				ToastManager.getInstance(context)
				.showToastcenter("物品成功删除！");
			}
		});

		Button new_requisition = (Button) findViewById(R.id.new_requisition);
		new_requisition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != array) {
					Intent intent = new Intent();
					intent.setClass(context, AddNewArticleActivity.class);
					intent.putExtra("array", array.toString());
					intent.putExtra("tag", true);
					startActivityForResult(intent, 1);
				}
			}
		});
		Button new_requisition_submit = (Button) findViewById(R.id.new_requisition_submit);
		new_requisition_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (list.size() < 1) {
					ToastManager.getInstance(context)
							.showToastcenter("物品不能为空！");
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
						String reason = reason_requisition_edit.getText()
								.toString();// 物品用途原因
						String compendium = article_resume.getText().toString();// 物品简述
						String projectName = requisition_project_name.getText()
								.toString();// 项目名称
						String needsDate = need_time_text.getText().toString();// 需用时间
						if (reason.equals("") || compendium.equals("")
								|| needsDate.equals("")) {
							ToastManager.getInstance(context)
									.showToastcenter("必填项不能为空！");
							dialog.dismiss();
							return;
						}

						RequestParams param = new RequestParams();
						param.setContentEncoding("utf-8");
						param.put("key", key);
						param.put("CheckTheApplicationForm", "");// ...
						param.put("showAddApply", "");// ...
						param.put("reason", reason);// 请购原因用途
						param.put("compendium", compendium);// 请购物品简述
						param.put("projectName", projectName);// 项目名称
						param.put("needsDate", needsDate);// 需用时间

						param.put("NUM", list.size());// 物品数量

						for (int i = 0; i < list.size(); i++) {
							int s = i + 1;
							param.put("orgId" + s, list.get(i).getOrgId());// 物品名称
							param.put("accompanyId" + s, list.get(i)
									.getAccompanyId());// 物品类别
							param.put("accompanyId2" + s, list.get(i)
									.getAccompanyId2());// 物品品牌
							param.put("prod_Model" + s, list.get(i)
									.getProd_Model());// 物品型号
							param.put("need_num" + s, list.get(i).getNeed_num());// 物品数量
							param.put("prod_unit" + s, list.get(i)
									.getProd_unit());// 物品单位
							param.put("unit_price" + s, list.get(i)
									.getUnit_price());// 物品单价
							param.put("url" + s, list.get(i).getUrl());// 网店参考地址
							param.put("remark" + s, list.get(i).getRemark());// 物品备注
						}
						Dialog progressDialog = dialog_util.showWaitingDialog(
								RequisitionActivity.this, "正在刷新", false);
						async.post(APIURL.CHECK.ADD_REQUISITION, context,
								param, new ReJsonHandler(
										RequisitionActivity.this,
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

	/*
	 * 初始化DATE
	 */
	private void initDate() {
		// TODO Auto-generated method stub
		dialog_util = new Dialog_Http_Util();
		async = new AsyncHttpClientUtil();
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		RequestParams param = new RequestParams();
		param.setContentEncoding("utf-8");
		param.put("key", key);
		Dialog progressDialog = dialog_util.showWaitingDialog(this, "正在刷新",
				false);
		async.post(APIURL.CHECK.ADDARTICLE, context, param, new JsonHandler(
				this, progressDialog));
	}

	// 请购申请单提交回调类
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
				String name = response.getString("userName");
				requisition_username.setText(name);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				array = response.getJSONArray("equipTypeList");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	// 请购申请单提交回调类
	private class ReJsonHandler extends MJsonHttpHandler {
		private Dialog progressDialog;

		protected ReJsonHandler(Context context, Dialog progressDialog) {
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
							.showToastcenter("请购单领用成功！");
					RequisitionActivity.this.finish();
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 1:
			Bundle bundle = data.getExtras();
			if (null != bundle) {
				Requisition_Swipe_Entity mlist = (Requisition_Swipe_Entity) bundle
						.getSerializable("mlist");
				if (bundle.getInt("position", -1) != -1) {
					list.set(bundle.getInt("position", -1), mlist);
					adapter.setList(list);
				} else {
					list.add(mlist);
					adapter.setList(list);
				}
				adapter.notifyDataSetChanged();
			}
			break;

		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(ev.getAction()==MotionEvent.ACTION_DOWN){
			View v = getCurrentFocus();
			if(isShouldHideInput(v,ev)){
				hideSoftInput(v.getWindowToken());
			}
			
		}
		
		
		return super.dispatchTouchEvent(ev);
	}
	 private void hideSoftInput(IBinder windowToken) {
		// TODO Auto-generated method stub
		 if(windowToken!=null){
			 InputMethodManager im=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			 im.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
		 }
		
	}

	private boolean isShouldHideInput(View v,MotionEvent event){
		 
		 if(v!=null&&(v instanceof EditText)){
			 int [] l={0,0};
			 v.getLocationInWindow(l);
			 int left=l[0],top=l[1], bottom=top+v.getHeight(),right=left+v.getWidth();
			 if(event.getX()>left&&event.getX()<right&&event.getY()>top&&event.getY()<bottom){
				 return false;
				 
			 }else{
				 return true;
			 }
		 }
		 
		return false;
		
	}

}
