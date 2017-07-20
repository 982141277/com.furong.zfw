package com.meiyin.erp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiyin.erp.R;
import com.meiyin.erp.adapter.PersonalityAdapter;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Personalityentity;
import com.my.android.library.ToastManager;

import java.util.ArrayList;
/**
 * @description 背景装扮
 */
public class Personality extends Activity{
	private ArrayList<Personalityentity> list;
	private PersonalityAdapter per;
	private int bl = 0;
	private SharedPreferences sp;
	private Context context;
	private Button submit;
	private RelativeLayout my_head_relative;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personality);
		context = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		initHeader();
		findview();
		GridView gridview = (GridView) findViewById(R.id.gridview);
		per = new PersonalityAdapter(this);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				per.bl(arg2);
				per.notifyDataSetChanged();
				bl = arg2;
			}
		});
		list = new ArrayList<Personalityentity>();
		list.add(new Personalityentity(context.getResources().getDrawable(R.mipmap.my_red), "主题红色"));
		list.add(new Personalityentity(context.getResources().getDrawable(R.mipmap.biz_news_local_weather_bg_big), "主题蓝色"));
		list.add(new Personalityentity(context.getResources().getDrawable(R.mipmap.my_green), "主题绿色"));
		per.setList(list);
		gridview.setAdapter(per);
	}
	/*
	 * 初始化标题UI
	 */
	private void initHeader() {
		my_head_relative = (RelativeLayout) findViewById(R.id.my_head_relative);
		ImageView headtitleimageView = (ImageView) findViewById(R.id.headtitleimageView);
		headtitleimageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TextView headtitletext = (TextView) findViewById(R.id.headtitletext);
		headtitletext.setText("个性装扮");
	}

	private void findview() {
		submit = (Button) findViewById(R.id.shiyong);
		int a = sp.getInt(SPConstant.BACKGROUND, 0);
		bl=a;
		switch (a) {
		case 0:
			my_head_relative.setBackgroundColor(getResources().getColor(
					R.color.dark_red));
			submit.setBackgroundResource(R.drawable.button_login_red);
			break;
		case 1:
			my_head_relative.setBackgroundColor(getResources().getColor(
					R.color.blue1));
			submit.setBackgroundResource(R.drawable.button_login_blue);
			break;
		case 2:
			my_head_relative.setBackgroundColor(getResources().getColor(
					R.color.text_green));
			submit.setBackgroundResource(R.drawable.button_login_green);
			break;
		default:
			break;
		}
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction(SPConstant.MYBACKGOUNDACTION);
				intent.putExtra("bl", bl);
				sp.edit().putInt(SPConstant.BACKGROUND, bl).commit();
				Personality.this.sendBroadcast(intent);
				ToastManager.getInstance(context).showToastcenter("设置成功！");
				switch (bl) {
				case 0:
					my_head_relative.setBackgroundColor(getResources().getColor(
							R.color.dark_red));
					submit.setBackgroundResource(R.drawable.button_login_red);
					break;
				case 1:
					my_head_relative.setBackgroundColor(getResources().getColor(
							R.color.blue1));
					submit.setBackgroundResource(R.drawable.button_login_blue);
					break;
				case 2:
					my_head_relative.setBackgroundColor(getResources().getColor(
							R.color.text_green));
					submit.setBackgroundResource(R.drawable.button_login_green);
					break;
				default:
					break;
				}
			}
		});
	}

}
