package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.json.JsonHttpHandles;
import com.meiyin.erp.util.AndroidUtil;
import com.meiyin.erp.util.AsyncHttpclient_Util;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.LogUtil;
import com.meiyin.erp.util.ToastManager;
import com.meiyin.erp.util.ToastUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
	/**
	 * ERP登陆页
	 */
public class Login extends Activity implements OnKeyListener,TextWatcher{
	private Button login_button;
	private Context context;
	private Dialog_Http_Util dialog;
	private EditText name,password;
	private SharedPreferences sp;
	private Animation anc;
	@SuppressLint("NewApi")
	@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.login_);
	sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
	context = getApplicationContext();
	 anc = AnimationUtils.loadAnimation(this,R.anim.slide_top_in);
	
	findview();
	dialog = new Dialog_Http_Util();
	}
	public void login(String name,String psd){
		String model= AndroidUtil.getDeviceModel(context);
		String SystemVersion=AndroidUtil.getSystemVersion(context);
		String AppVersionName=AndroidUtil.getAppVersionName(context);
		 RequestParams params = new RequestParams();  
		 params.put("username", name);  
		 params.put("password", psd);  
		 params.put("login_model", model);  		 
		 params.put("login_system_version", SystemVersion);  		 
		 params.put("login_erp_version", AppVersionName);  		 
		 params.put("uf", "loc");
		 String string = APIURL.LOGINAPI;
		 AsyncHttpclient_Util.post(string, context,params,new JsonHandler(name));
	}
	public void findview(){
		LinearLayout donghua1=(LinearLayout)findViewById(R.id.donghua1);
		donghua1.startAnimation(anc);
		LinearLayout donghua2=(LinearLayout)findViewById(R.id.donghua2);
		donghua2.startAnimation(anc);
		login_button=(Button) findViewById(R.id.login_button);
		name=(EditText) findViewById(R.id.user_name);
		name.addTextChangedListener(this);

		password=(EditText) findViewById(R.id.password);
		password.setOnKeyListener(this);
		login_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
		// TODO Auto-generated method stub
					String names=name.getText().toString();
					String pass=password.getText().toString();	
					if(names.trim().equals("")){
						soft(name);
						ToastManager.getInstance(context).showimgToast("请输入帐号！");
//						name.setError("请输入用户名！");
//						password.setError("")
					}else if(pass.trim().equals("")){
						soft(password);
						ToastManager.getInstance(context).showimgToast("请输入密码！");
					}else{
						dialog.showWaitingDialog(Login.this, "正在登录...", false);
						login(names,pass);
					}
			}
		});
	}
	
	private class JsonHandler extends JsonHttpHandles {
		String usernameString;
		private JsonHandler(String usernameString){
			this.usernameString=usernameString;
		}
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				String ss = response.getString("loginStatus");
				if(ss.equals("1")){
					final String mString = response.getString("mlist");
					
					JSONArray array = (JSONArray) response.get("systems");
					String user = response.getString("USER_MARK");
					String key = response.getString("key");
					sp.edit().putString(SPConstant.MY_NAME,response.getString("name")).commit();
					sp.edit().putBoolean(SPConstant.MY_LOGIN_HOST,true).commit();
					sp.edit().putString(SPConstant.MY_LOGINID,user).commit();
					sp.edit().putString(SPConstant.MY_LOGININFO,array.toString()).commit();
					sp.edit().putString(SPConstant.MY_TOKEN,key).commit();
					sp.edit().putString(SPConstant.USERNAME,usernameString).commit();//用户名
					sp.edit().putString(SPConstant.MY_GOOUTBRUSH,mString).commit();//项目人员权限
					if(mString.equals("2")){
						JSONArray dListString = response.getJSONArray("dList");	
						sp.edit().putString(SPConstant.MY_GOOUTBRUSHLIST,dListString.toString()).commit();//项目人员权限	
					}
					dialog.dismissWaitingDialog();
					ToastUtil.showToast(context,"登录成功！");
					Intent intent = new Intent();
					intent.setClass(context, Main_Home.class);
					intent.putExtra("login",array.toString());
					intent.putExtra("name",response.getString("name"));
					intent.putExtra("gooutbrush", mString);
					startActivity(intent);
					Login.this.finish();
				}else if(ss.equals("0")){
					ToastUtil.showToast(context,response.getString("errorMsg"));
					dialog.dismissWaitingDialog();
				}
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
				ToastManager.getInstance(context).showimgToast("服务连接超时");
			}
			dialog.dismissWaitingDialog();
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			LogUtil.e("lyt", "测试："+responseString+statusCode+throwable.toString());
			ToastUtil.showToast(context,"网络连接失败！");
			dialog.dismissWaitingDialog();
			super.onFailure(statusCode, headers, responseString, throwable);
		}
}
	/**
	 * 键盘ENTER键的监听
	 */
	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		if(arg1==KeyEvent.KEYCODE_ENTER&&arg2.getAction()==KeyEvent.ACTION_DOWN){
			String names=name.getText().toString();
			String pass=password.getText().toString();	
			if(names.equals("")||pass.equals("")){
				ToastManager.getInstance(context).showToast("用户名或密码不能为空！");
			}else{
				dialog.showWaitingDialog(Login.this, "正在登录...", false);
				login(names,pass);
			}

		}
		return false;
	}
	public void soft(View tv){
		
		InputMethodManager im=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		im.showSoftInput(tv, 0);
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		String s = name.getText().toString().trim();
		Pattern p = Pattern.compile("[\u4E00-\u9FA5]");
		Matcher m = p.matcher(s);
		if(m.matches()){
		soft(name);
		name.setText("");
		ToastManager.getInstance(context).showimgToast("请输入正确的帐号！");
		}
	}
	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
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