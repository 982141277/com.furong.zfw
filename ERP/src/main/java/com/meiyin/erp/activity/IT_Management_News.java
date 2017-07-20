package com.meiyin.erp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.meiyin.erp.R;
import com.meiyin.erp.adapter.SimpleTreeAdapter;
import com.meiyin.erp.app.FileBean;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.bean.Node;
import com.meiyin.erp.bean.TreeListViewAdapter;
import com.meiyin.erp.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * IT运维管理新增事件页
 */
public class IT_Management_News extends Activity implements OnClickListener ,OnItemSelectedListener{
	private Context context;
	private Spinner spinner, spinner_yxj, spinner_sf;
	private LinearLayout lianxidianhua, lianxishouji, bangongdizhi, suoshudanwei,
			lingdao, zhiwu;
	private TextView yonghu,think_text;
	private CheckBox checkBox1;
	private String shijian="",shijian1="",shijian2="";
	private EditText shijian_biaoti,bangongdizhi_input,yonghu_input;

	private List<FileBean> mDatas = new ArrayList<FileBean>();
	private ListView mTree;
	private TreeListViewAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_it_management);
		SharedPreferences sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		context = getApplicationContext();

		findview();
		getresources();
		initDatas();
		
		
		

	}

	

	private void findview() {
		lianxidianhua = (LinearLayout) findViewById(R.id.lianxidianhua);
		lianxishouji = (LinearLayout) findViewById(R.id.lianxishouji);
		bangongdizhi = (LinearLayout) findViewById(R.id.bangongdizhi);
		suoshudanwei = (LinearLayout) findViewById(R.id.suoshudanwei);
		lingdao = (LinearLayout) findViewById(R.id.lingdao);
		zhiwu = (LinearLayout) findViewById(R.id.zhiwu);

		shijian_biaoti = (EditText) findViewById(R.id.shijian_biaoti);
		bangongdizhi_input = (EditText) findViewById(R.id.bangongdizhi_input);
		yonghu_input = (EditText) findViewById(R.id.yonghu_input);
		
		yonghu = (TextView) findViewById(R.id.yonghu);
		think_text = (TextView) findViewById(R.id.think_text);
		checkBox1 = (CheckBox) findViewById(R.id.checkBox1);

		spinner = (Spinner) findViewById(R.id.spinner1);
		spinner_yxj = (Spinner) findViewById(R.id.spinner_yxj);
		spinner_sf = (Spinner) findViewById(R.id.spinner_sf);
		spinner.setOnItemSelectedListener(this);
		spinner_yxj.setOnItemSelectedListener(this);
		spinner_sf.setOnItemSelectedListener(this);
		
		Button baocun = (Button) findViewById(R.id.baocun);
		baocun.setOnClickListener(this);
		think_text.setOnClickListener(this);
		
		/*
		 * 3.24
		 */
		TextView area_text = (TextView) findViewById(R.id.area_text);
		EditText mobile_text = (EditText) findViewById(R.id.mobile_text);
		EditText phone_text = (EditText) findViewById(R.id.phone_text);
		EditText event_describe_text = (EditText) findViewById(R.id.event_describe_text);
		EditText position_text = (EditText) findViewById(R.id.position_text);
		
		
		}

	void getresources() {
		String[] mItems = getResources().getStringArray(R.array.spinner_sjly);
		String[] yxj = getResources().getStringArray(R.array.spinner_yxj);
		String[] sf = getResources().getStringArray(R.array.spinners_);
		ArrayAdapter<String> spinnerAdapter = array(mItems);
		ArrayAdapter<String> spinnerAdapters = array(yxj);
		ArrayAdapter<String> spinnerAdapterss = array(sf);
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
			heads.setText("您确定要提交此事件？");
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
					ToastUtil.showToast(context, "提交成功！");
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
		case R.id.think_text:
			dialog = new AlertDialog.Builder(this).create();
	        RelativeLayout thisink = (RelativeLayout) LayoutInflater.from(
	    					context).inflate(R.layout.leixing_main, null);		
	        mTree=(ListView) thisink.findViewById(R.id.id_tree);
			try
			{
				mAdapter = new SimpleTreeAdapter<FileBean>(mTree, this, mDatas, 10);
				
				mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
				{
					@Override
					public void onClick(Node node, int position)
					{
						if (node.isLeaf())
						{
							Toast.makeText(getApplicationContext(), node.getName(),
									Toast.LENGTH_SHORT).show();
						}
					}

				});
				
				mTree.setAdapter(mAdapter);
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			dialog.show();
			dialog.getWindow().setContentView(thisink);

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
			shijian = arg0.getItemAtPosition(arg2).toString();
			
			if (shijian.equals("巡检事件") || shijian.equals("报警事件")) {
				lianxidianhua.setVisibility(View.GONE);
				lianxishouji.setVisibility(View.GONE);
				bangongdizhi.setVisibility(View.GONE);
				suoshudanwei.setVisibility(View.GONE);
				lingdao.setVisibility(View.GONE);
				zhiwu.setVisibility(View.GONE);
				checkBox1.setVisibility(View.GONE);
				yonghu.setText("请求人");
				
			} else if (shijian.equals("服务请求")
					|| shijian.equals("--请选择事件来源--")) {
				lianxidianhua.setVisibility(View.VISIBLE);
				lianxishouji.setVisibility(View.VISIBLE);
				bangongdizhi.setVisibility(View.VISIBLE);
				suoshudanwei.setVisibility(View.VISIBLE);
				lingdao.setVisibility(View.VISIBLE);
				zhiwu.setVisibility(View.VISIBLE);
				checkBox1.setVisibility(View.VISIBLE);
				yonghu.setText("用户");
			} else if (shijian.equals("计划事件")) {
				lianxidianhua.setVisibility(View.GONE);
				lianxishouji.setVisibility(View.GONE);
				bangongdizhi.setVisibility(View.GONE);
				suoshudanwei.setVisibility(View.GONE);
				lingdao.setVisibility(View.GONE);
				zhiwu.setVisibility(View.GONE);
				checkBox1.setVisibility(View.GONE);
				yonghu.setText("请求人");
			}
			break;
		case R.id.spinner_yxj:
			shijian1 = arg0.getItemAtPosition(arg2).toString();
			break;
		case R.id.spinner_sf:
			shijian2 = arg0.getItemAtPosition(arg2).toString();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
		

	private void initDatas()
	{

//		// id , pid , label , 其他属性
//		mDatas.add(new FileBean(1, 0, "文件管理系统"));
//		mDatas.add(new FileBean(2, 1, "游戏"));
//		mDatas.add(new FileBean(3, 1, "文档"));
//		mDatas.add(new FileBean(4, 1, "程序"));
//		mDatas.add(new FileBean(5, 2, "war3"));
//		mDatas.add(new FileBean(6, 2, "刀塔传奇"));
//
//		mDatas.add(new FileBean(7, 4, "面向对象"));
//		mDatas.add(new FileBean(8, 4, "非面向对象"));
//
//		mDatas.add(new FileBean(9, 7, "C++"));
//		mDatas.add(new FileBean(10, 7, "JAVA"));
//		mDatas.add(new FileBean(11, 7, "Javascript"));
//		mDatas.add(new FileBean(12, 8, "C语言"));

	}
//    private class JsonHandler extends JsonHttpHandles {
//		@SuppressLint("NewApi")
//		@Override
//		public void onSuccess(int statusCode, Header[] headers,
//				JSONObject response) {
//			try {
//				JSONArray equiptype = response.getJSONArray("equiptype");
//				ArrayList<Equip_Type_Entity> mlist = new Gson().fromJson(equiptype.toString(),
//						new TypeToken<List<Equip_Type_Entity>>() {
//						}.getType());
//				for (int a = 0; a < mlist.size(); a++) {
//					if(mlist.get(a).getIs_leaf().equals("1")){
//					NodeResource n1 = new NodeResource(mlist.get(a).getParent_id(),mlist.get(a).getCode_id(), mlist.get(a).getCode_desc(), "dfs", R.drawable.wenjian);
//						list.add(n1);
//					}else if(mlist.get(a).getIs_leaf().equals("0")){
//						NodeResource n2 = new NodeResource(mlist.get(a).getParent_id(),mlist.get(a).getCode_id(), mlist.get(a).getCode_desc(), "dfs",R.drawable.bao);
//						list.add(n2);
//					}
//					
//					
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		@Override
//		public void onFailure(int statusCode, Header[] headers,
//				String responseString, Throwable throwable) {
//			// TODO Auto-generated method stub
//			ToastUtil.showToast(context, "网络连接失败,请检查网络设置！");
//			super.onFailure(statusCode, headers, responseString, throwable);
//		}
//
//		@Override
//		public void onFailure(int statusCode, Header[] headers,
//				Throwable throwable, JSONObject errorResponse) {
//			// TODO Auto-generated method stub
//			ToastUtil.showToast(context, "网络连接失败,请检查网络设置！");
//			super.onFailure(statusCode, headers, throwable, errorResponse);
//
//		}
//	
//	}
}
