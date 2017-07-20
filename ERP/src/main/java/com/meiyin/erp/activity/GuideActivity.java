package com.meiyin.erp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meiyin.erp.R;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.util.DenstyUtil;

import java.util.ArrayList;
import java.util.List;


public class GuideActivity extends FragmentActivity implements OnPageChangeListener, OnClickListener {

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	private Context mContext;
	private Button mBtnLogin;

	// 底部小点图片
	private ImageView[] dots;
	private int img[] = { R.mipmap.index_one, R.mipmap.index_two, R.mipmap.index_three };
	// 记录当前选中位置
	private int currentIndex;
	private SharedPreferences sp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		setContentView(R.layout.activity_guide);
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		// userModel = new UserModel(mContext);
		// 初始化页面
		initViews();
		// 初始化底部小点
		initDots();
	}

	private String getManifestChannel() {
		String result = "";

		try {
			ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(),
					PackageManager.GET_META_DATA);
			if (appInfo != null)
				result = appInfo.metaData.getString("channel");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void initViews() {
		//mBtnRegister = (Button) findViewById(R.id.guide_btn_register);
		mBtnLogin = (Button) findViewById(R.id.guide_btn_login);
		mBtnLogin.setOnClickListener(this);

		views = new ArrayList<View>();
		// 初始化引导图片列表
		for (int i = 0; i < img.length; i++) {
			View view = new View(this);
			view.setBackgroundResource(img[i]);
			views.add(view);
		}

		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter();
		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[views.size()];
		int width = (int) DenstyUtil.convertDpToPixel(10, mContext);
		int margin = (int) DenstyUtil.convertDpToPixel(10, mContext);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
		params.setMargins(margin, margin, margin, margin);

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++) {
			dots[i] = new ImageView(mContext);
			dots[i].setLayoutParams(params);
			if (i == 0) {
				dots[i].setImageResource(R.drawable.circle_in_orange);
			} else {
				dots[i].setImageResource(R.drawable.circle_in_gray);
			}
			ll.addView(dots[i]);
		}

		currentIndex = 0;
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1 || currentIndex == position) {
			return;
		}
		dots[position].setImageResource(R.drawable.circle_in_orange);
		dots[currentIndex].setImageResource(R.drawable.circle_in_gray);

		currentIndex = position;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状态
		if (arg0 == dots.length - 1) {
			mBtnLogin.setVisibility(View.VISIBLE);
		} else {
			mBtnLogin.setVisibility(View.GONE);
		}
		setCurrentDot(arg0);

	}

	public class ViewPagerAdapter extends PagerAdapter {

		public ViewPagerAdapter() {
		}

		// 销毁arg1位置的界面
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		// 获得当前界面数
		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}

		// 初始化arg1位置的界面
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			return views.get(arg1);
		}

		// 判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.guide_btn_login:
			Intent intent = new Intent(this,Login.class);
			this.startActivity(intent);
			sp.edit().putBoolean(SPConstant.USERINFO, true).commit();
			GuideActivity.this.finish();
			break;

		default:
			break;
		}
	}



	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

}
