package com.meiyin.erp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.adapter.AddShortCut_Adapter;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.MyPage_Entity;

import java.util.ArrayList;

public class AddShortCutActivity extends Activity{
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addshortcut_main);
		context = getApplicationContext();
		SharedPreferences sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);

		/*
		 * 初始化
		 */
		initHeader();
		initData();
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
		headtitletext.setText("添加快捷");
		TextView head_right_text = (TextView) findViewById(R.id.head_right_text);
		head_right_text.setText("确定");
		head_right_text.setVisibility(ViewGroup.VISIBLE);
		head_right_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	/*
	 * 初始化数据
	 */
	private void initData() {
		// TODO Auto-generated method stub
		ListView addshortcut_list = (ListView) findViewById(R.id.addshortcut_list);
		AddShortCut_Adapter adapter = new AddShortCut_Adapter(context);
		ArrayList<MyPage_Entity> addlist = new ArrayList<MyPage_Entity>();
//		addlist.add(new MyPage_Entity("待审批事项","0", false, null));
//		addlist.add(new MyPage_Entity("已审批事项","0", false));
//		addlist.add(new MyPage_Entity("新增申请单","0", false));
//		addlist.add(new MyPage_Entity("申请单列表","0", false));
//		addlist.add(new MyPage_Entity("日志","0", false));
//		addlist.add(new MyPage_Entity("外勤","0", false));
//		addlist.add(new MyPage_Entity("公告","0", false));
//		addlist.add(new MyPage_Entity("待处理事件","0", false));
		adapter.setList(addlist);
		addshortcut_list.setAdapter(adapter);
		
	}
}
