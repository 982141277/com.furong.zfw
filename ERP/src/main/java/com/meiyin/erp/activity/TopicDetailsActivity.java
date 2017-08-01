package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.meiyin.erp.GreenDao.Topic_EntityDao;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.TopicList_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.MyApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Topic_Entity;
import com.meiyin.erp.util.AndroidUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.Function;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @param公司公告详情
 * @Time 2016-5-24
 */
public class TopicDetailsActivity extends Activity{
	private Context context;
	private AlertDialog dialog;
	private TopicList_Adapter adapter;
	private SharedPreferences sp;
	private TextView topic_cotent;

	private Topic_Entity xTopic ;
	private String topic_code;
	private Topic_EntityDao topicDao;
	private Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_details_mian);
		context = getApplicationContext();
		activity=this;
		sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		topicDao= MyApplication.getInstance().getDaoSession().getTopic_EntityDao();
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
		headtitletext.setText("公告内容");
	}
	private void initData() {
		// TODO Auto-generated method stub
		topic_cotent=(TextView) findViewById(R.id.topic_cotent);
		topic_code = getIntent().getExtras().getString("topic_code");
		xTopic = topicDao.loadByRowId(Long.valueOf(topic_code));
		String xcontent = xTopic.getContent();
		if(null!=xcontent&&!xcontent.equals("")){
			if(xcontent.contains("<table")){
				topic_cotent.setVisibility(ViewGroup.GONE);
				websview(xcontent);
			}else{
				topic_cotent.setText(Html.fromHtml(xcontent, Function.imgGetter, null));
				topic_cotent.setVisibility(ViewGroup.VISIBLE);
			}	
		}else{
		Dialog progressDialog =  new Dialog_Http_Util().showWaitingDialog(TopicDetailsActivity.this,
				 "正在刷新", false);
		RequestParams param = new RequestParams();
		param.setContentEncoding("utf-8");
		param.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
		param.put("topic_code", topic_code);
		param.put("type", 1);

		new AsyncHttpClientUtil().post(APIURL.CHECK.TOPICSHOW, context, param, new JsonHandlerInit(context,progressDialog));
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

			JSONObject data = null;
			String content="";
			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
				data = response.getJSONObject("dto2");
				content = data.getString("content");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			xTopic.setContent(content);
			topicDao.update(xTopic);
			if(content.contains("<table")){
				topic_cotent.setVisibility(ViewGroup.GONE);
				websview(content);
			}else{
				topic_cotent.setText(Html.fromHtml(content, Function.imgGetter, null));	
				topic_cotent.setVisibility(ViewGroup.VISIBLE);
			}	
		}

	}
	@SuppressLint("SetJavaScriptEnabled")
	private void websview(final String html){
		WebView web = (WebView) findViewById(R.id.websview);
		final ProgressBar pb = (ProgressBar) findViewById(R.id.pb);
		WebSettings webset = web.getSettings();
		webset.setJavaScriptEnabled(true);//设置是否支持JavaScript
		webset.getCacheMode();//设置缓冲的模式
		webset.setSupportZoom(true);//设置是否支持变焦
		webset.setBuiltInZoomControls(true);//设置是否支持缩放
		webset.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);//设置布局方式
		webset.setUseWideViewPort(true);//设置将图片调整到适合webview的大小
		webset.setLoadWithOverviewMode(true);//缩放至屏幕大小
		webset.setDefaultTextEncodingName("UTF -8");//编码
		web.loadData(html,"text/html;charset=UTF-8",null);
		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadData(html,"text/html;charset=UTF-8",null);
				
				return true;
			}
		});
		web.setWebChromeClient(new WebChromeClient() {
			
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				pb.setProgress(newProgress);
				if (newProgress == 100) {
					pb.setProgress(0);
					pb.setVisibility(ViewGroup.GONE);
				} else {
					pb.setVisibility(ViewGroup.GONE);
				}
				super.onProgressChanged(view, newProgress);
			}
		});
	}

}
