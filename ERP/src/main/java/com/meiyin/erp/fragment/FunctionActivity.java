package com.meiyin.erp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiyin.erp.R;
/**
 * @date 2016/7/14
 * @description 功能介绍
 */
public class FunctionActivity extends Activity
{
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
				requestWindowFeature(Window.FEATURE_NO_TITLE);
				setContentView(R.layout.function_item);
				initHeader();
//				RelativeLayout function_relative=(RelativeLayout)findViewById(R.id.function_relative);
				
//				SharedPreferences sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
//						Context.MODE_PRIVATE);
//				int a=sp.getInt(SPConstant.BACKGROUND,0);
//				switch (a) {
//				case 0:
//					function_relative.setBackgroundColor(getResources().getColor(R.color.blue1));
//					break;
//				case 1:
//					function_relative.setBackgroundColor(getResources().getColor(R.color.dark_red));
//					break;
//				case 2:
//					function_relative.setBackgroundColor(getResources().getColor(R.color.text_green));
//					break;
//
//				default:
//					break;
//				}
			super.onCreate(savedInstanceState);
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
			headtitletext.setText("功能介绍");
		}
}
