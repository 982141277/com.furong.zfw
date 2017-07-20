package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.IT_StayEvent_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.IT_StayEvent;
import com.meiyin.erp.json.JsonHttpHandles;
import com.meiyin.erp.util.AsyncHttpclient_Util;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.ToastManager;
import com.meiyin.erp.util.ToastUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IT_Management_Activity extends Activity implements OnClickListener{
	
	private Context context;
	Intent intent;
	private LinearLayout it_text_two;
	private ListView it_listview;
	private ArrayList<IT_StayEvent> list;
	private SharedPreferences sp;
	private IT_StayEvent_Adapter adapter;
//	private TextView it_headitem_text_cont;
	private Dialog_Http_Util dia;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.it_management_activity);
		context = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		dia = new Dialog_Http_Util();
		intent = new Intent();
		initHeader();
		findview();
		
		final AlertDialog dialog = new AlertDialog.Builder(this).create();		
		adapter = new IT_StayEvent_Adapter(context);
		list = new ArrayList<IT_StayEvent>();
		adapter.setOnRightItemClickListener(new IT_StayEvent_Adapter.onRightItemClickListener() {
			
			@Override
			public void onRightItemClick(View v, final int position) {
				// TODO Auto-generated method stub

				LinearLayout submit_dialog = (LinearLayout) LayoutInflater
						.from(context).inflate(R.layout.submit_dialog, null);
				TextView heads = (TextView) submit_dialog.findViewById(R.id.heads);
				Button button_t = (Button) submit_dialog.findViewById(R.id.button_t);
				Button button_f = (Button) submit_dialog.findViewById(R.id.button_f);
				if(list.get(position).getModetype().equals("1")){
				heads.setText("您确定受理标题是“"+list.get(position).getTitle()+"”的事件吗？");
				}else if(list.get(position).getModetype().equals("2")){
					heads.setText("您确定主动获取标题是"+list.get(position).getTitle()+"的事件吗？");
				}
				dialog.show();
				dialog.getWindow().setContentView(submit_dialog);
				button_t.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(list.get(position).getModetype().equals("1")){
							event_accept(list.get(position).getEvent_no(),list.get(position).getId(),list.get(position).getReciver(),"1");
						}else if(list.get(position).getModetype().equals("2")){
							event_accept(list.get(position).getEvent_no(),list.get(position).getId(),list.get(position).getReciver(),"2");							
						}
						Log.e("lyt", "position:"+position);
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
		adapter.setList(list);
		
		it_listview.setAdapter(adapter);
		it_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
//				if(arg2>0){
				Intent intent = new Intent();
					if (list.size() > 0) {
						String event_no = list.get(arg2).getEvent_no();
						intent.setClass(IT_Management_Activity.this, IT_Management_Sq.class);
						intent.putExtra("event_no", event_no);
						startActivity(intent);

//					}
					}
			}
		});
		httpstaylist(sp.getString(SPConstant.MY_TOKEN, ""),0);
		if(sp.getString(SPConstant.IT_MANAGEMENT_TYPE, "").equals("")){
			RequestParams params = new RequestParams();
			params.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
			String string = APIURL.ITSM.IT_EVENT_TYPE;
			AsyncHttpclient_Util.post(string, context, params, new JsonHandletype());
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
		headtitletext.setText("待处理事件");
		}
	public void findview(){
//		it_text_one=(LinearLayout) findViewById(R.id.it_text_one);

		it_listview=(ListView)findViewById(R.id.it_listview);
//		View head = LayoutInflater.from(context).inflate(R.layout.it_headitem, null);
//		TextView it_textview_one = (TextView)head.findViewById(R.id.it_textview_one);
//		it_textview_one.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				startActivity(new Intent().setClass(context, IT_Management_Event_List.class));
//
//			}
//		});	
//		it_headitem_text_cont = (TextView)head.findViewById(R.id.it_headitem_text_cont);
		
//		it_listview.addHeaderView(head);
//		it_text_two=(LinearLayout) findViewById(R.id.it_text_two);

//		View view = findViewById(R.id.it_text_three);
//		view.setOnClickListener(this);

//		it_text_two.setOnClickListener(this);

		
		
	}
	private void httpstaylist(String key, int page) {
		dia.showWaitingDialog(IT_Management_Activity.this, "正在加载...", false);
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("page", page);
		params.put("size", 1000);
		String string = APIURL.ITSM.IT_DELAY_PROCESS_LIST;
		AsyncHttpclient_Util.post(string, context, params, new JsonHandler());
	}
	//事件受理
	private void event_accept(String event_no,String id,String reciver,String type) {
		dia.showWaitingDialog(IT_Management_Activity.this, "正在处理...", false);
		RequestParams params = new RequestParams();
		String string =APIURL.ITSM.IT_EVENT_ACCEPT;
		if(type.equals("1")){
		params.put("action_value", "SLGD");
		params.put("reciver", reciver);
		string = APIURL.ITSM.IT_EVENT_ACCEPT;
		}else if(type.equals("2")){
		params.put("action_value", "ZDHQ");
		params.put("deal_type", "zdhq");
		string = APIURL.ITSM.IT_EVENT_EXECUTES;
		}
		params.put("opType", "edit");
		params.put("event_id", id);
		params.put("event_no", event_no);
		params.put("menu_value", "SJCL");
		params.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
		
		
		AsyncHttpclient_Util.post(string, context, params, new JsonHandlers());
	}
	private class JsonHandler extends JsonHttpHandles {
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			try {
				JSONArray array = (JSONArray) response.get("data");
				ArrayList<IT_StayEvent> lists = new Gson()
						.fromJson(
								array.toString(),
								new TypeToken<List<IT_StayEvent>>() {
								}.getType());
//				it_headitem_text_cont.setText(String.valueOf(lists.size()));
				list.clear();
				for (IT_StayEvent item : lists) {
					list.add(item);
				}
				adapter.setList(list);
				adapter.notifyDataSetChanged();
//				onLoad();
				dia.dismissWaitingDialog();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			if (statusCode == 0
					&& throwable.toString().contains("HttpHostConnect")) {
				ToastManager.getInstance(context).showimgToast(
						"网络连接失败,请检查网络设置！");
			} else if (statusCode == 200) {
				ToastManager.getInstance(context).showimgToast("账号异常请联系软件研发部！");
			} else if (statusCode == 0
					&& throwable.toString().contains("Timeout")) {
				ToastManager.getInstance(context).showimgToast("服务连接超时！");
			}
			dia.dismissWaitingDialog();
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			Log.e("lyt", "网络连接失败："+responseString+statusCode+throwable.toString());
			ToastUtil.showToast(context, "网络连接失败！");
			dia.dismissWaitingDialog();
			super.onFailure(statusCode, headers, responseString, throwable);
		}
	}
	private class JsonHandlers extends JsonHttpHandles {
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			try {
				 boolean success = response.getBoolean("success");
				 if(success){
					 ToastManager.getInstance(context).showimgToast("恭喜你,添加事件处理信息成功！");
				 }else {
					 ToastManager.getInstance(context).showimgToast(response.getString("message"));
				 }
				 httpstaylist(sp.getString(SPConstant.MY_TOKEN, ""),0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			dia.dismissWaitingDialog();
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			dia.dismissWaitingDialog();
			if (statusCode == 0
					&& throwable.toString().contains("HttpHostConnect")) {
				ToastManager.getInstance(context).showimgToast(
						"网络连接失败,请检查网络设置！");
			} else if (statusCode == 200) {
				ToastManager.getInstance(context).showimgToast("账号异常请联系软件研发部！");
			} else if (statusCode == 0
					&& throwable.toString().contains("Timeout")) {
				ToastManager.getInstance(context).showimgToast("服务连接超时！");
			}
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			ToastUtil.showToast(context, "网络连接失败！");
			dia.dismissWaitingDialog();
			super.onFailure(statusCode, headers, responseString, throwable);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
//		switch (arg0.getId()) {	
//		case R.id.it_text_two:
//			Intent intent = new Intent();
//			intent.setClass(context, IT_Management_StayList.class);
//			startActivity(intent);
//			break;
//		case R.id.it_text_three:
//			ToastUtil.showToast(context, "暂未开放！");
//			startActivity(new Intent().setClass(context, IT_Management_News.class));
//			break;			
//		default:
//			break;
//		}
	}
	private class JsonHandletype extends JsonHttpHandles {
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			try {
				JSONArray equiptype = response.getJSONArray("equiptype");
				sp.edit().putString(SPConstant.IT_MANAGEMENT_TYPE,equiptype.toString()).commit();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			ToastUtil.showToast(context, "网络连接失败,请检查网络设置！");
			super.onFailure(statusCode, headers, responseString, throwable);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			ToastUtil.showToast(context, "网络连接失败,请检查网络设置！");
			super.onFailure(statusCode, headers, throwable, errorResponse);

		}
	
	}
}
