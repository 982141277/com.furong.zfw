package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.ClientList_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.SellClientInfoEntity;
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
import java.util.List;

/**
 * @param客户列表
 * @Time 2016-5-21
 */
public class ClientListActivity extends Activity{

	
	private ClientList_Adapter adapter;
	private ArrayList<SellClientInfoEntity>list,mlist ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.clientlist_main);
		Context context = getApplicationContext();
		SharedPreferences sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		/*
		 * 初始化
		 */
		initHeader(context);
		initDate(context,sp);
		initView(context);
	}
	/*
	 * 初始化标题UI
	 */
	private void initHeader(final Context context) {
		ImageView headtitleimageView = (ImageView) findViewById(R.id.headtitleimageView);
		headtitleimageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				int result=getIntent().getIntExtra("result", 0);
				ClientListActivity.this.setResult(result, intent);
				ClientListActivity.this.finish();
			}
		});
		TextView headtitletext = (TextView) findViewById(R.id.headtitletext);
		headtitletext.setText("销售客户");
		Button head_btn = (Button) findViewById(R.id.head_btn);
		head_btn.setText("提交");
		head_btn.setTextColor(getResources().getColor(R.color.white));
		head_btn.setVisibility(ViewGroup.VISIBLE);
		head_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ArrayList<SellClientInfoEntity> Mlist = new ArrayList<SellClientInfoEntity>();
				ArrayList<SellClientInfoEntity> li = adapter.getList();
				if(null!=li&&li.size()>0){
					for (SellClientInfoEntity sellClientInfoEntity : li) {
						if(sellClientInfoEntity.getIsChecked()){
							Mlist.add(sellClientInfoEntity);
						}
					}
					if(Mlist.size()<1){
						ToastManager.getInstance(context).showToastcenter("请选择客户！");
						return;
					}
					Intent intent = new Intent();
					Bundle bl = new Bundle();
					bl.putSerializable("mlist", (Serializable) Mlist);
					intent.putExtras(bl);
					int result=getIntent().getIntExtra("result", 0);
					ClientListActivity.this.setResult(result, intent);
					ClientListActivity.this.finish();
				}else{
					ToastManager.getInstance(context).showToastcenter("请选择客户！");
					return;
				}

			}
		});
	}
	/*
	 * 初始化销售客户数据
	 */
	private void initDate(Context context,SharedPreferences sp) {
		// TODO Auto-generated method stub
		AsyncHttpClientUtil async = new AsyncHttpClientUtil();
		Dialog_Http_Util dialog_util = new Dialog_Http_Util();
		list=new ArrayList<SellClientInfoEntity>();
		mlist = new ArrayList<SellClientInfoEntity>();
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		
		RequestParams param = new RequestParams();
		param.setContentEncoding("utf-8");
		param.put("key", key);
		Dialog progressDialog = dialog_util.showWaitingDialog(this,
				 "正在刷新", false);
		async.post(APIURL.CHECK.ADDMEMUCLIENT, context, param, new JsonHandler(this,progressDialog));

	}
	/*
	 * 初始化VIEW
	 */
	private void initView(Context context) {
		// TODO Auto-generated method stub
		// 客户列表
		adapter=new ClientList_Adapter(context);
		ListView clientlistview = (ListView) findViewById(R.id.clientlistview);
		clientlistview.setAdapter(adapter);
		EditText client_edit_textview = (EditText) findViewById(R.id.client_edit_textview);
		client_edit_textview.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				mlist.clear();
				for (SellClientInfoEntity iterable : list) {
					if(iterable.getsClientName().contains(arg0)){
						mlist.add(iterable);
					}
				}
				adapter.setList(mlist);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});


	}
	// 外出申请单提交回调类
		private class JsonHandler extends MJsonHttpHandler {
			private Dialog progressDialog;
			private Context context;
			protected JsonHandler(Context context,Dialog progressDialog) {
				super(context, progressDialog);
				this.progressDialog=progressDialog;
				this.context=context;
				// TODO Auto-generated constructor stub
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
					int count=response.getInt("count");
					JSONArray clientList = (JSONArray) response.get("clientList");
					list = new Gson().fromJson(clientList.toString(),
							new TypeToken<List<SellClientInfoEntity>>() {
							}.getType());//客户资料
					adapter.setList(list);
					adapter.notifyDataSetChanged();
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
				int result=getIntent().getIntExtra("result", 0);
				ClientListActivity.this.setResult(result, intent);
				ClientListActivity.this.finish();
				back = false;
			}
			return back;

		}

}
