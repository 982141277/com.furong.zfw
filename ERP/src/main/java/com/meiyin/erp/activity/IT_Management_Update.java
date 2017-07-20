
package com.meiyin.erp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.IT_Group_Name_Entity;
import com.meiyin.erp.json.JsonHttpHandles;
import com.meiyin.erp.util.AsyncHttpclient_Util;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.ToastUtil;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * IT运维管理修改事件页
 */
public class IT_Management_Update extends Activity implements OnClickListener ,OnItemSelectedListener{
	private Context context;
	private Spinner spinner, spinner_yxj, spinner_sf,update_name_text;

	private TextView yonghu,think_text;
	private String shijian="",shijian1="",shijian2="",shijian3="";
	private EditText shijian_biaoti,bangongdizhi_input,yonghu_input;
	private EditText mobile_text,phone_text,event_describe_text,position_text;//手机、电话、事件描述、职务 
	private SharedPreferences sp;
	private String oid,ids,event_type_id;
	private int is_leader,event_level,event_source,index;
	private ArrayList<IT_Group_Name_Entity> lists;
	private Dialog_Http_Util dia;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.update_it_management);
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		context = getApplicationContext();
		dia=new Dialog_Http_Util();
		String string1=getIntent().getStringExtra("string1");
		String string2=getIntent().getStringExtra("string2");

		Log.e("lyt", string1);
		Log.e("lyt", string2);
		lists = new Gson().fromJson(string2.toString(),
				new TypeToken<List<IT_Group_Name_Entity>>() {
				}.getType());
		 ArrayList<String> ssStrings=new ArrayList<String>();
		for(int i=0;i<lists.size();i++){
			ssStrings.add(lists.get(i).getKey());
		}
		findview(string1,ssStrings);

	}

	private void findview(String string1,ArrayList<String> mit) {
		JSONObject obj = null;
		try {
			 obj = new JSONObject(string1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shijian_biaoti = (EditText) findViewById(R.id.shijian_biaoti);
		bangongdizhi_input = (EditText) findViewById(R.id.bangongdizhi_input);
		yonghu_input = (EditText) findViewById(R.id.yonghu_input);
		
		yonghu = (TextView) findViewById(R.id.yonghu);
		think_text = (TextView) findViewById(R.id.think_text);

		spinner = (Spinner) findViewById(R.id.spinner1);
		spinner_yxj = (Spinner) findViewById(R.id.spinner_yxj);
		spinner_sf = (Spinner) findViewById(R.id.spinner_sf);
		update_name_text = (Spinner) findViewById(R.id.update_name_text);
		update_name_text.setOnItemSelectedListener(this);
		spinner.setOnItemSelectedListener(this);
		spinner_yxj.setOnItemSelectedListener(this);
		spinner_sf.setOnItemSelectedListener(this);
		getresources(mit);
		Button baocun = (Button) findViewById(R.id.baocun);
		baocun.setOnClickListener(this);
		
		/*
		 * 3.24
		 */
		TextView area_text = (TextView) findViewById(R.id.area_text);//运维组
		mobile_text = (EditText) findViewById(R.id.mobile_text);
		phone_text = (EditText) findViewById(R.id.phone_text);
		event_describe_text = (EditText) findViewById(R.id.event_describe_text);
		position_text = (EditText) findViewById(R.id.position_text);
		
		try {
		bangongdizhi_input.setText(obj.getString("workspace"));// 办公地址
		think_text.setText(obj.getString("event_type"));// 事件类型
		ids=obj.getString("id");// 事件状态
		oid = obj.getString("oid");
		event_type_id=obj.getString("event_type_id");
		

		if(obj.getString("event_level").equals("紧急")){
			spinner_yxj.setSelection(1,true);	
		}else if(obj.getString("event_level").equals("高")){
			spinner_yxj.setSelection(2,true);
		}else if(obj.getString("event_level").equals("中")){
			spinner_yxj.setSelection(3,true);
		}else if(obj.getString("event_level").equals("低")){
			spinner_yxj.setSelection(4,true);
		}// 优先级
		shijian_biaoti.setText(obj.getString("title"));// 标题
		if(obj.getString("source").equals("服务请求")){
			spinner.setSelection(1,true);	
		}else if(obj.getString("source").equals("巡检事件")){
			spinner.setSelection(2,true);
		}else if(obj.getString("source").equals("报警事件")){
			spinner.setSelection(3,true);
		}else if(obj.getString("source").equals("计划事件")){
			spinner.setSelection(4,true);
		}// 事件来源
		
		event_describe_text.setText(obj.getString("event_describe"));// 事件描述
		phone_text.setText(obj.getString("phone"));// 联系手机
		mobile_text.setText(obj.getString("mobile"));// 联系电话
		
		boolean area = obj.isNull("area");
		if(area){
			area_text.setText("");
		}else{
			area_text.setText(obj.getString("area"));// 运维组
		}
		if (obj.isNull("recivern")) {
			yonghu_input.setText("");// 用户
		} else {
			yonghu_input.setText(obj.getString("recivern"));// 用户
		}
		if (obj.getString("Is_leader").equals("1")) {
			spinner_sf.setSelection(1,true);
		} else if (obj.getString("Is_leader").equals("0")) {
			spinner_sf.setSelection(0,true);
		}
		position_text.setText(obj.getString("position"));// 职务
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

	private void getresources(ArrayList<String> mit) {
		String[] mItems = getResources().getStringArray(R.array.spinner_sjly);
		String[] yxj = getResources().getStringArray(R.array.spinner_yxj);
		String[] sf = getResources().getStringArray(R.array.spinners_);
		
		ArrayAdapter<String> spinnerAdapter = array(mItems);
		ArrayAdapter<String> spinnerAdapters = array(yxj);
		ArrayAdapter<String> spinnerAdapterss = array(sf);
		
		ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, mit);
		spAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
		update_name_text.setAdapter(spAdapter);
		spinner.setAdapter(spinnerAdapter);
		spinner_yxj.setAdapter(spinnerAdapters);
		spinner_sf.setAdapter(spinnerAdapterss);
	}

	public ArrayAdapter<String> array(String[] s) {
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, s);
		spinnerAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
		return spinnerAdapter;
	}

	@Override
	public void onClick(View arg0) {
		final AlertDialog dialog;
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.baocun:
			if(shijian.equals("")||shijian.equals("--请选择事件来源--")){
				ToastUtil.showToast(context, "事件来源不能为空！");
			}else if(shijian1.equals("")||shijian1.equals("--请选择优先级--")){
				ToastUtil.showToast(context, "优先级不能为空！");
			}else if(shijian_biaoti.getText().toString().equals("")){
				ToastUtil.showToast(context, "事件标题不能为空！");
			}else if(yonghu_input.getText().toString().equals("")){
				ToastUtil.showToast(context, "用户不能为空！");
			}else if(bangongdizhi_input.getText().toString().equals("")){
				ToastUtil.showToast(context, "办公地址不能为空！");
			}else{
			dialog = new AlertDialog.Builder(this).create();
			LinearLayout submit_dialog = (LinearLayout) LayoutInflater.from(
					context).inflate(R.layout.submit_dialog, null);

			TextView heads = (TextView) submit_dialog.findViewById(R.id.heads);
			heads.setText("您确定把事件要分配给"+shijian3+"？");
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

					if(event_level==0||event_source==-1){
						return;
					}
					dia.showWaitingDialog(IT_Management_Update.this, "正在分配..", false);
					RequestParams params = new RequestParams();
					params.put("menu_id", "01020405");//主键id
					params.put("action_value", "FP");//事件状态
					params.put("event_no", getIntent().getStringExtra("event_no"));//事件编码
					params.put("menu_value", "SJCL");
					
					params.put("position", position_text.getText().toString());//职务
					params.put("state", ids);//事件状态
					params.put("event_id", getIntent().getStringExtra("id"));//主键Id
					params.put("phone", phone_text.getText().toString());//电话
					params.put("workspace", bangongdizhi_input.getText().toString());//办公地址 
					params.put("event_type",event_type_id);// 事件类型
					params.put("opType", "edit");
					params.put("event_level", event_level);//优先级
					
					params.put("title", shijian_biaoti.getText().toString());//事件标题
					params.put("is_leader", is_leader);//是否领导
					params.put("oid", oid);//所属运维项目组
					params.put("describe", event_describe_text.getText().toString());//事件描述
					params.put("asker", yonghu_input.getText().toString());//请求人
					params.put("mobile", mobile_text.getText().toString());//手机 
					params.put("event_source", event_source);//事件来源
					params.put("reciver", lists.get(index).getValue());//分配人
					params.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
					//不需要的参数
					params.put("helper", "");//帮助
					params.put("depart", "");//所属单位
					params.put("sendMsg", 0);//短信提醒
					
					String string = APIURL.ITSM.IT_EVENT_UPDATE;
					
					AsyncHttpclient_Util.post(string, context, params, new JsonHandler());
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
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.spinner1:
			event_source=arg2-1;
			shijian = arg0.getItemAtPosition(arg2).toString();
			if (shijian.equals("巡检事件") || shijian.equals("报警事件")) {
				yonghu.setText("请求人");
			} else if (shijian.equals("服务请求")
					|| shijian.equals("--请选择事件来源--")) {

				yonghu.setText("用户");
			} else if (shijian.equals("计划事件")) {

				yonghu.setText("请求人");
			}
			break;
		case R.id.spinner_yxj:
			event_level=arg2;
			shijian1 = arg0.getItemAtPosition(arg2).toString();
			break;
		case R.id.spinner_sf:
			is_leader=arg2;
			shijian2 = arg0.getItemAtPosition(arg2).toString();
			break;
		case R.id.update_name_text:
			index=arg2;
			shijian3 = arg0.getItemAtPosition(arg2).toString();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

    private class JsonHandler extends JsonHttpHandles {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt",response.toString());
			String msg = null;
			try {
				msg = response.getString("message");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dia.dismissWaitingDialog();
			ToastManager.getInstance(context).showToast(msg);
			IT_Management_Update.this.finish();
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			Log.e("lyt","@什么鬼："+statusCode+"2:"+headers+"3:"+throwable);
			if(statusCode==0&&throwable.toString().contains("HttpHostConnect")){
				ToastManager.getInstance(context).showToast("网络连接失败,请检查网络设置！");
			}else if(statusCode==200){
				ToastManager.getInstance(context).showToast("账号异常！！！请联系湖南美音网络技术有限公司！");
			}else if(statusCode==0&&throwable.toString().contains("Timeout")){
				ToastManager.getInstance(context).showToast("服务连接超时！");
			}
			dia.dismissWaitingDialog();
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			Log.e("lyt","@什么鬼："+statusCode+"2:"+headers+"3:"+throwable);
			ToastManager.getInstance(context).showToast("网络连接失败！");
			dia.dismissWaitingDialog();
			super.onFailure(statusCode, headers, responseString, throwable);
		}
	
	}
}
