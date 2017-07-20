package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.meiyin.erp.GreenDao.Topic_EntityDao;
import com.meiyin.erp.R;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.MyApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Topic_Entity;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.ToastManager;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 关于美音ERP
 */
public class About_Activity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {
	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	private Context mContext;
	private TextView text_one, text_two, text_three,text_four;
	private View view_one, view_two, view_three,view_four;
	private String webString="";
	private int text[] = { R.string.one_words, R.string.two_words,R.mipmap.zzjg};

	private Topic_Entity xTopic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about_main);
		mContext = getApplicationContext();
		SharedPreferences sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		/*
		 * 初始化标题UI
		 */
		initHeader();
		initview();
		// 初始化viewpager页面
		initData(sp);
		initViews();

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
			headtitletext.setText("关于美音");
			}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.text_ones:
			vp.setCurrentItem(0);
			onPageSelected(0);

			break;
		case R.id.text_twos:
			vp.setCurrentItem(1);
			onPageSelected(1);
			break;
		case R.id.text_threes:
			vp.setCurrentItem(2);
			onPageSelected(2);
			break;
		case R.id.text_fours:
			vp.setCurrentItem(3);
			onPageSelected(3);
			break;
		default:
			break;
		}

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

	private void initViews() {
		views = new ArrayList<View>();
		// 初始化引导图片列表
		for (int i = 0; i < text.length; i++) {
			if (i == text.length - 1) {
				ImageView imageview = new ImageView(this);
				imageview.setPadding(0, 10, 0, 0);
				imageview.setBackgroundResource(text[i]);
				views.add(imageview);
			} else {
				TextView texs = new TextView(this);
				texs.setPadding(30, 10, 30, 0);
				texs.setText(text[i]);
				texs.setTextSize(15);
				ScrollView SCROLL = new ScrollView(this);
				SCROLL.addView(texs);
				// view.setBackgroundResource(img[i]);
				views.add(SCROLL);
			}
		}
		
			WebView web =new WebView(this);
			web.setPadding(0, 10, 0, 0);
			WebSettings webset = web.getSettings();
			webset.setJavaScriptEnabled(true);//设置是否支持JavaScript
			webset.getCacheMode();//设置缓冲的模式
			webset.setSupportZoom(true);//设置是否支持变焦
			webset.setBuiltInZoomControls(true);//设置是否支持缩放
			webset.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);//设置布局方式
			webset.setUseWideViewPort(true);//设置将图片调整到适合webview的大小
			webset.setLoadWithOverviewMode(true);//缩放至屏幕大小
			webset.setDefaultTextEncodingName("UTF -8");//编码
			web.loadData(webString,"text/html;charset=UTF-8",null);
			web.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// TODO Auto-generated method stub
					view.loadData(webString,"text/html;charset=UTF-8",null);
					
					return true;
				}
			});
			views.add(web);	
		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter();
		vp = (ViewPager) findViewById(R.id.viewpagers);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}

	private void initview() {
		TextView meiyincode_text = (TextView) findViewById(R.id.meiyincode_text);
		meiyincode_text.setText("ERP "+getVersion());

		view_one = findViewById(R.id.view_one);
		view_two = findViewById(R.id.view_two);
		view_three = findViewById(R.id.view_three);
		view_four = findViewById(R.id.view_four);

		LinearLayout ll = (LinearLayout) findViewById(R.id.lls);
		text_one = (TextView) findViewById(R.id.text_ones);
		text_two = (TextView) findViewById(R.id.text_twos);
		text_three = (TextView) findViewById(R.id.text_threes);
		text_four = (TextView) findViewById(R.id.text_fours);
		
		text_one.setOnClickListener(this);
		text_two.setOnClickListener(this);
		text_three.setOnClickListener(this);
		text_four.setOnClickListener(this);

	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1) {
			return;
		} else if (position == 0) {
			text_one.setTextColor(getResources().getColor(R.color._red));
			text_two.setTextColor(getResources().getColor(R.color.text_gray));
			text_three.setTextColor(getResources().getColor(R.color.text_gray));
			text_four.setTextColor(getResources().getColor(R.color.text_gray));
			view_one.setBackgroundColor(getResources().getColor(
					R.color._red));
			view_two.setBackgroundColor(getResources().getColor(R.color.white));
			view_three.setBackgroundColor(getResources()
					.getColor(R.color.white));
			view_four.setBackgroundColor(getResources()
					.getColor(R.color.white));

		} else if (position == 1) {
			text_one.setTextColor(getResources().getColor(R.color.text_gray));
			text_two.setTextColor(getResources().getColor(R.color._red));
			text_three.setTextColor(getResources().getColor(R.color.text_gray));
			text_four.setTextColor(getResources().getColor(R.color.text_gray));
			view_one.setBackgroundColor(getResources().getColor(R.color.white));
			view_two.setBackgroundColor(getResources().getColor(
					R.color._red));
			view_three.setBackgroundColor(getResources()
					.getColor(R.color.white));
			view_four.setBackgroundColor(getResources()
					.getColor(R.color.white));
		} else if (position == 2) {
			text_one.setTextColor(getResources().getColor(R.color.text_gray));
			text_two.setTextColor(getResources().getColor(R.color.text_gray));
			text_three.setTextColor(getResources().getColor(R.color._red));
			text_four.setTextColor(getResources().getColor(R.color.text_gray));
			view_one.setBackgroundColor(getResources().getColor(R.color.white));
			view_two.setBackgroundColor(getResources().getColor(R.color.white));
			view_three.setBackgroundColor(getResources().getColor(
					R.color._red));
			view_four.setBackgroundColor(getResources()
					.getColor(R.color.white));
		} else if (position == 3) {
			text_one.setTextColor(getResources().getColor(R.color.text_gray));
			text_two.setTextColor(getResources().getColor(R.color.text_gray));
			text_three.setTextColor(getResources().getColor(R.color.text_gray));
			text_four.setTextColor(getResources().getColor(R.color._red));
			view_one.setBackgroundColor(getResources().getColor(R.color.white));
			view_two.setBackgroundColor(getResources().getColor(R.color.white));
			view_three.setBackgroundColor(getResources().getColor(
					R.color.white));
			view_four.setBackgroundColor(getResources()
					.getColor(R.color._red));
		}

	}

	public String getVersion() {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					mContext.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "没有找到版本号";
		}
	}
	private void initData(SharedPreferences sp) {
		// TODO Auto-generated method stub
//		337
		Topic_EntityDao topicDao = MyApplication.getInstance().getDaoSession().getTopic_EntityDao();
		xTopic = topicDao.loadByRowId(Long.valueOf(337));
		if(null!=xTopic){
		String xcontent = xTopic.getContent();
		if(null!=xcontent&&!xcontent.equals("")){
			webString=xcontent;
		}else{
		Dialog progressDialog =  new Dialog_Http_Util().showWaitingDialog(About_Activity.this,
				 "正在刷新", false);
		RequestParams param = new RequestParams();
		param.setContentEncoding("utf-8");
		param.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
		param.put("topic_code", 337);
		param.put("type", 1);

		new AsyncHttpClientUtil().post(APIURL.CHECK.TOPICSHOW, mContext, param, new JsonHandlerInit(mContext,progressDialog));
		}
		}
	}
	// 公告list初始化
	private class JsonHandlerInit extends MJsonHttpHandler {
		private Dialog progressDialog;
		protected JsonHandlerInit(Context context,Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog=progressDialog;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			progressDialog.dismiss();
			if (!response.isNull("errorMsg")) {
				try {
					ToastManager.getInstance(mContext).showToast(
							response.getString("errorMsg"));
					stopService(new Intent()
							.setAction("com.meiyin.services.Meiyinservice"));
					startActivity(new Intent().setClass(mContext, Login.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					System.exit(0);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}

			JSONObject data = null;
			String content="";
			try {
				data = response.getJSONObject("dto2");
				content = data.getString("content");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			webString=content;
			initViews();
		}

	}

}
