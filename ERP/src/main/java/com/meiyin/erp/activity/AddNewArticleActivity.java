package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.Spinner_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Requisition_Swipe_Entity;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AddNewArticleActivity extends Activity {

	private Context context;
	private Dialog_Http_Util dialog_util;
	private AsyncHttpClientUtil async;
	private SharedPreferences sp;
	private ArrayList<Spinner_Entity> spn1, spn2, spn3, spn4, spn5;
	private Spinner_Adapter spinnerAdapter2;
	private Spinner_Adapter spinnerAdapter3;
	private Spinner_Adapter spinnerAdapter4;
	private Spinner_Adapter spinnerAdapter5;
	private String type0 = "", type1 = "", type2 = "", type3 = "";
	private String type_name = "", type_name1 = "", type_name2 = "",
			type_name3 = "";
	private String models;
	private EditText article_model;
	private EditText article_need_num, article_prod_unit, article_unit_price,
			article_url, article_remark;
	private int index1,index2,index22,index3,index4;
	private Requisition_Swipe_Entity mlists;
	private Spinner article_spn2,article_spn3,article_spn4,article_spn5;
	boolean tag;
	private Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addnewarticle_main);
		context = getApplicationContext();
		activity=this;
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		/*
		 * 初始化
		 */
		tag = !getIntent().getExtras().getBoolean("tag", false);
		if(tag){
			mlists = (Requisition_Swipe_Entity)getIntent().getExtras().getSerializable("mlist");
		}
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
				Intent intent = new Intent();
				AddNewArticleActivity.this.setResult(1, intent);
				AddNewArticleActivity.this.finish();
			}
		});
		TextView headtitletext = (TextView) findViewById(R.id.headtitletext);
		headtitletext.setText("新增物品");
		Button head_btn = (Button) findViewById(R.id.head_btn);
		head_btn.setVisibility(ViewGroup.VISIBLE);
		head_btn.setTextColor(getResources().getColor(R.color.white));
		head_btn.setText("保存");
		head_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String typeString2;// 物品类别
				String typenameString2;
				if (type1.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("请选择物品类别！");
					return;
				} else if (type3.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("请选择物品品牌！");
					return;
				}

				if (type2.equals("")) {// 物品品牌
					typeString2 = type1;
					typenameString2 = type_name1;
				} else {
					typeString2 = type2;
					typenameString2 = type_name2;
				}
				String modelmString = "";// 物品型号
				String model = article_model.getText().toString();
				if(null==models||null==model){
					ToastManager.getInstance(context)
					.showToastcenter("数据出现异常，请重新选择物品名称");
					return;
				}
				if (models.equals("") && model.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("请填写物品型号！");
					return;
				} else if (!models.equals("")) {
					modelmString = models;
				} else if (!model.equals("")) {
					modelmString = model;
				}
				String need_num = article_need_num.getText().toString();
				String prod_unit = article_prod_unit.getText().toString();
				String unit_price = article_unit_price.getText().toString();
				if (need_num.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("物品数量不能为空！");
					return;
				} else if (prod_unit.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("物品单位不能为空！");
					return;
				} else if (unit_price.equals("")) {
					ToastManager.getInstance(context)
							.showToastcenter("物品单价不能为空！");
					return;
				}

				String url = article_url.getText().toString();
				String remark = article_remark.getText().toString();

				Requisition_Swipe_Entity mlist = new Requisition_Swipe_Entity(
						type0, type_name, typeString2, typenameString2, type3,
						type_name3, modelmString, need_num, prod_unit,
						unit_price, url, remark, index1, index2, index22, index3, index4);
				
				Intent intent = new Intent();
				Bundle bl = new Bundle();
				bl.putSerializable("mlist", (Serializable) mlist);
				intent.putExtras(bl);
				if(tag){
					intent.putExtra("position",getIntent().getExtras().getInt("position"));
				}
				AddNewArticleActivity.this.setResult(1, intent);
				AddNewArticleActivity.this.finish();
			}

		});
	}

	/*
	 * 初始化VIEW
	 */
	private void initView() {
		// TODO Auto-generated method stub
		dialog_util = new Dialog_Http_Util();
		async = new AsyncHttpClientUtil();
		spn1 = new ArrayList<Spinner_Entity>();
		spn2 = new ArrayList<Spinner_Entity>();
		spn3 = new ArrayList<Spinner_Entity>();
		spn4 = new ArrayList<Spinner_Entity>();
		spn5 = new ArrayList<Spinner_Entity>();

		String array = getIntent().getExtras().getString("array");
		spn1 = new Gson().fromJson(array,
				new TypeToken<List<Spinner_Entity>>() {
				}.getType());
		// 物品名称
		Spinner article_spn1 = (Spinner) findViewById(R.id.article_spn1);
		Spinner_Adapter spinnerAdapter1 = new Spinner_Adapter(context);
		spinnerAdapter1.setList(spn1);
		if(tag){
			article_spn1.setSelection(mlists.getIndex1());
		}
		article_spn1.setAdapter(spinnerAdapter1);
		article_spn1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				index1=arg2;
				type0 = spn1.get(arg2).getType_id();
				type_name = spn1.get(arg2).getType_name();
				HttpRequest(1, APIURL.CHECK.ADDARTICLE1, spn1.get(arg2)
						.getType_id(), "");
				spn3.clear();
				spinnerAdapter3.setList(spn3);
				spinnerAdapter3.notifyDataSetChanged();
				spn4.clear();
				spinnerAdapter4.setList(spn4);
				spinnerAdapter4.notifyDataSetChanged();
				spn5.clear();
				spinnerAdapter5.setList(spn5);
				spinnerAdapter5.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		// 物品类别
		article_spn2 = (Spinner) findViewById(R.id.article_spn2);
		spinnerAdapter2 = new Spinner_Adapter(context);
		spinnerAdapter2.setList(spn2);
		article_spn2.setAdapter(spinnerAdapter2);
		article_spn2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2 != 0) {
					index2=arg2;
					type1 = spn2.get(arg2).getType_id();
					type_name1 = spn2.get(arg2).getType_name();
					HttpRequest1(APIURL.CHECK.ADDARTICLE1, spn2.get(arg2)
							.getType_id());
					spn4.clear();
					spinnerAdapter4.setList(spn4);
					spinnerAdapter4.notifyDataSetChanged();
					spn5.clear();
					spinnerAdapter5.setList(spn5);
					spinnerAdapter5.notifyDataSetChanged();
				} else {
					type1 = "";
					type_name1 = "";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		// 物品类别2
		article_spn3 = (Spinner) findViewById(R.id.article_spn3);
		spinnerAdapter3 = new Spinner_Adapter(context);
		spinnerAdapter3.setList(spn3);
		article_spn3.setAdapter(spinnerAdapter3);
		article_spn3.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2 != 0) {
					index22=arg2;
					type2 = spn3.get(arg2).getType_id();
					type_name2 = spn3.get(arg2).getType_name();
					HttpRequest(2, APIURL.CHECK.ADDARTICLE2, "", spn3.get(arg2)
							.getType_id());

					spn5.clear();
					spinnerAdapter5.setList(spn5);
					spinnerAdapter5.notifyDataSetChanged();
				} else {
					type2 = "";
					type_name2 = "";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// 物品品牌
		article_spn4 = (Spinner) findViewById(R.id.article_spn4);
		spinnerAdapter4 = new Spinner_Adapter(context);
		spinnerAdapter4.setList(spn4);
		article_spn4.setAdapter(spinnerAdapter4);
		article_spn4.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2 != 0) {
					index3=arg2;
					type3 = spn4.get(arg2).getType_id();
					type_name3 = spn4.get(arg2).getType_name();
					HttpRequest2(APIURL.CHECK.ADDARTICLE3, spn4.get(arg2)
							.getType_id());
				} else {
					type3 = "";
					type_name3 = "";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		// 物品型号
		article_spn5 = (Spinner) findViewById(R.id.article_spn5);
		article_model = (EditText) findViewById(R.id.article_model);

		spinnerAdapter5 = new Spinner_Adapter(context);
		spinnerAdapter5.setList(spn5);
		article_spn5.setAdapter(spinnerAdapter5);
		article_spn5.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 0) {
					article_model.setVisibility(View.VISIBLE);
					models = "";
				} else {
					index4=arg2;
					article_model.setVisibility(View.GONE);
					article_model.setText("");
					models = spn5.get(arg2).getType_name();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		article_need_num = (EditText) findViewById(R.id.article_need_num);// 物品数量
		article_prod_unit = (EditText) findViewById(R.id.article_prod_unit);// 物品单位
		article_unit_price = (EditText) findViewById(R.id.article_unit_price);// 物品单价
		article_url = (EditText) findViewById(R.id.article_url);// 网站参考地址
		article_remark = (EditText) findViewById(R.id.article_remark);// 备注
		if(tag){
		article_need_num.setText(mlists.getNeed_num());
		article_prod_unit.setText(mlists.getProd_unit());
		article_unit_price.setText(mlists.getUnit_price());
		article_url.setText(mlists.getUrl());
		article_remark.setText(mlists.getRemark());
		}
	}

	private void HttpRequest(int i, String api, String orgid, String accompanyId) {
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		RequestParams param = new RequestParams();
		param.setContentEncoding("utf-8");
		param.put("key", key);
		if (i == 1) {
			param.put("orgId", orgid);
		} else if (i == 2) {
			param.put("accompanyId", accompanyId);
		}
		Dialog progressDialog = dialog_util.showWaitingDialog(this, "正在刷新",
				false);
		async.post(api, context, param, new JsonHandler(this, progressDialog));
	}

	// 是否还有其他类别
	private void HttpRequest1(String api, String orgid) {
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		RequestParams param = new RequestParams();
		param.setContentEncoding("utf-8");
		param.put("key", key);
		param.put("orgId", orgid);
		Dialog progressDialog = dialog_util.showWaitingDialog(this, "正在刷新",
				false);
		async.post(api, context, param, new JsonHandler1(this, progressDialog,
				orgid));
	}

	// 物品型号
	private void HttpRequest2(String api, String brand_code) {
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		RequestParams param = new RequestParams();
		param.setContentEncoding("utf-8");
		param.put("key", key);
		param.put("orgId", type0);
		if (type2.equals("")) {
			param.put("accompanyId", type1);
		} else {
			param.put("accompanyId", type2);
		}
		param.put("accompanyId2", brand_code);

		Dialog progressDialog = dialog_util.showWaitingDialog(this, "正在刷新",
				false);
		async.post(api, context, param, new JsonHandler2(this, progressDialog));
	}

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
				if (!response.isNull("json")) {
					JSONArray json = response.getJSONArray("json");
					spn2 = new Gson().fromJson(json.toString(),
							new TypeToken<List<Spinner_Entity>>() {
							}.getType());
					spn2.add(new Spinner_Entity("请选择", "110"));
					Collections.reverse(spn2);
					spinnerAdapter2.setList(spn2);
					if(tag){
						article_spn2.setSelection(mlists.getIndex2());
					}
					spinnerAdapter2.notifyDataSetChanged();
				}
				if (!response.isNull("json2")) {
					JSONArray json2 = response.getJSONArray("json2");
					spn4 = new Gson().fromJson(json2.toString(),
							new TypeToken<List<Spinner_Entity>>() {
							}.getType());
					spn4.add(new Spinner_Entity("请选择", "110"));
					Collections.reverse(spn4);
					spinnerAdapter4.setList(spn4);
					if(tag){
						article_spn4.setSelection(mlists.getIndex3());
					}
					spinnerAdapter4.notifyDataSetChanged();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class JsonHandler1 extends MJsonHttpHandler {
		private Dialog progressDialog;
		private String orgid;

		protected JsonHandler1(Context context, Dialog progressDialog,
				String orgid) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
			this.orgid = orgid;
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
				if (!response.isNull("json")) {
					JSONArray json = response.getJSONArray("json");
					spn3 = new Gson().fromJson(json.toString(),
							new TypeToken<List<Spinner_Entity>>() {
							}.getType());
					spn3.add(new Spinner_Entity("请选择", "110"));
					Collections.reverse(spn3);
					spinnerAdapter3.setList(spn3);
					if(tag){
						article_spn3.setSelection(mlists.getIndex22());
					}
					spinnerAdapter3.notifyDataSetChanged();
				} else {
					spn3.clear();
					spinnerAdapter3.setList(spn3);
					spinnerAdapter3.notifyDataSetChanged();
					HttpRequest(2, APIURL.CHECK.ADDARTICLE2, "", orgid);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class JsonHandler2 extends MJsonHttpHandler {
		private Dialog progressDialog;

		protected JsonHandler2(Context context, Dialog progressDialog) {
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
				if (!response.isNull("json2")) {
					JSONArray json = response.getJSONArray("json2");
					spn5 = new Gson().fromJson(json.toString(),
							new TypeToken<List<Spinner_Entity>>() {
							}.getType());
					spn5.add(new Spinner_Entity("其他型号", "110"));
					Collections.reverse(spn5);
					spinnerAdapter5.setList(spn5);
					if(tag){
						article_spn5.setSelection(mlists.getIndex4());
					}
					spinnerAdapter5.notifyDataSetChanged();
				} else {

					spn5.clear();
					spinnerAdapter5.setList(spn5);
					spinnerAdapter5.notifyDataSetChanged();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean back = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			AddNewArticleActivity.this.setResult(1, intent);
			AddNewArticleActivity.this.finish();
			back = false;
		}
		return back;

	}
}
