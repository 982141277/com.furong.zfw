package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.MyApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.json.JsonHttpHandles;
import com.meiyin.erp.service.Meiyinservice;
import com.meiyin.erp.util.AndroidUtil;
import com.meiyin.erp.util.AsyncHttpclient_Util;
import com.meiyin.erp.util.ToastUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
/**
 * 首页
 */
public class MainActivity extends Activity {
	private Context context;
	private SharedPreferences sp;

	private Boolean MY_LOGIN_HOST = false;
	private String logininfo;
	private String name;
	private boolean USERINFO;
	private String gooutbrush;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		activity=this;
		context = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		USERINFO = sp.getBoolean(SPConstant.USERINFO, false);
		MY_LOGIN_HOST = sp.getBoolean(SPConstant.MY_LOGIN_HOST, false);
		logininfo = sp.getString(SPConstant.MY_LOGININFO, "");
		name = sp.getString(SPConstant.MY_NAME, "");
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		gooutbrush= sp.getString(SPConstant.MY_GOOUTBRUSH, "");
		if(USERINFO){
		if (MY_LOGIN_HOST) {
			// intent.setClass(context, Main_Home.class);
			// intent.putExtra("login", logininfo);
			// intent.putExtra("name",name);
			// Timer time = new Timer();
			// TimerTask timertask = new TimerTask() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// startActivity(intent);
			// MainActivity.this.finish();
			// }
			// };
			// time.schedule(timertask, 2000);
			login(key);
		} else {
			
			Timer time = new Timer();
			TimerTask timertask = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					startActivity(new Intent().setClass(context, Login.class));
					MainActivity.this.finish();
				}
			};
			time.schedule(timertask, 3000);
		}
		}else{
			// 首次进入
			Timer time = new Timer();
			TimerTask timertask = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					startActivity(new Intent().setClass(context, GuideActivity.class));
					MainActivity.this.finish();
				}
			};
			time.schedule(timertask, 3000);
		}

	}

	private void login(String key) {
		String model= AndroidUtil.getDeviceModel(context);
		String SystemVersion=AndroidUtil.getSystemVersion(context);
		String AppVersionName=AndroidUtil.getAppVersionName(context);
		RequestParams params = new RequestParams();
		params.put("key", key);
		params.put("login_model", model);  		 
		params.put("login_system_version", SystemVersion);  		 
		params.put("login_erp_version", AppVersionName);  	
		String string = APIURL.LOGINAPI;
		AsyncHttpclient_Util.post(string, context, params, new JsonHandler());
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }
	private class JsonHandler extends JsonHttpHandles {
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				String ss = response.getString("loginStatus");

				if (ss.equals("1")) {
					final String mString = response.getString("mlist");
					if(mString.equals("2")){
						JSONArray dListString = response.getJSONArray("dList");	
						sp.edit().putString(SPConstant.MY_GOOUTBRUSHLIST,dListString.toString()).commit();//项目人员权限	
					}
					Timer time = new Timer();
					TimerTask timertask = new TimerTask() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							sp.edit().putString(SPConstant.MY_GOOUTBRUSH,mString).commit();
							startActivity(new Intent().setClass(context, Main_Home.class)
									.putExtra("login", logininfo)
									.putExtra("name", name)
									.putExtra("gooutbrush", mString));
							MainActivity.this.finish();
						}
					};
					time.schedule(timertask, 2000);
				} else if (ss.equals("0")) {
					Log.e("lyt", response.toString());
					ToastUtil.showToast(context, "登陆密码失效,请重新登陆！");
					
					sp.edit().putString(SPConstant.MY_NAME,"").commit();
					sp.edit().putBoolean(SPConstant.MY_LOGIN_HOST,false).commit();
					sp.edit().putString(SPConstant.MY_LOGINID,"").commit();
					sp.edit().putString(SPConstant.MY_LOGININFO,"").commit();
					sp.edit().putString(SPConstant.MY_TOKEN,"").commit();
					
					MyApplication.getInstance().getDaoSession().getMeiyinnewsDao().deleteAll();
					stopService(new Intent(activity, Meiyinservice.class));
					Timer time = new Timer();
					TimerTask timertask = new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							startActivity(new Intent().setClass(context, Login.class));
							MainActivity.this.finish();
						}
					};
					time.schedule(timertask, 2000);
					
				}
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
			Timer time = new Timer();
			TimerTask timertask = new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					startActivity(new Intent().setClass(context, Main_Home.class)
							.putExtra("login", logininfo)
							.putExtra("name", name)
							.putExtra("gooutbrush", gooutbrush));
					MainActivity.this.finish();
				}
			};
			time.schedule(timertask, 2000);
			super.onFailure(statusCode, headers, responseString, throwable);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			ToastUtil.showToast(context, "网络连接失败,请检查网络设置！");
			Timer time = new Timer();
			TimerTask timertask = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					startActivity(new Intent().setClass(context, Main_Home.class)
							.putExtra("login", logininfo)
							.putExtra("name", name)
							.putExtra("gooutbrush", gooutbrush));
					MainActivity.this.finish();
				}
			};
			time.schedule(timertask, 2000);
			super.onFailure(statusCode, headers, throwable, errorResponse);

		}
	
	}
}
