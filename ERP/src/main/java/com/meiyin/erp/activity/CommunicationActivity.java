package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.SimpleTreeAdapter;
import com.meiyin.erp.app.FileBean;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.bean.Node;
import com.meiyin.erp.bean.TreeListViewAdapter.OnTreeNodeClickListener;
import com.meiyin.erp.entity.OverTimeTask_Entity;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @param公司通讯
 * @Time 2016-7-6
 */
public class CommunicationActivity extends Activity {
	private Context context;
	private SharedPreferences sp;
	private Dialog_Http_Util dialog_util;
	private AsyncHttpClientUtil mAsync;
	private ArrayList<OverTimeTask_Entity> mlist;
	private List<FileBean> mDatas = new ArrayList<FileBean>();;
	private SimpleTreeAdapter<FileBean> mAdapter;;
	private ListView comlist;
	private Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.communication_main);
		context = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		/*
		 * 初始化
		 */
		initHeader();
		initView();

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
		headtitletext.setText("公司通讯录");
		ImageView com_img = (ImageView) findViewById(R.id.com_img);
		com_img.setVisibility(ViewGroup.VISIBLE);
		com_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sp.edit().putString(SPConstant.COMMUNICATION,"").commit();
				initView();
			}
		});
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		comlist = (ListView) findViewById(R.id.communication_list);
		builder = new AlertDialog.Builder(this);// 初始化审批dialog
		String communication = sp.getString(SPConstant.COMMUNICATION, "");
		if (communication.equals("")) {
			dialog_util = new Dialog_Http_Util();
			mAsync = new AsyncHttpClientUtil();
			/*
			 * 初始化所有人员
			 */
			String key = sp.getString(SPConstant.MY_TOKEN, "");
			RequestParams param = new RequestParams();
			param.setContentEncoding("utf-8");
			param.put("key", key);
			Dialog progressDialog = dialog_util.showWaitingDialog(
					CommunicationActivity.this, "正在刷新", true);
			mAsync.post(APIURL.COMPEOPLE, context, param, new JsonHandler(
					CommunicationActivity.this, progressDialog));
		} else {
			frist(communication);
		}
	}

	// 人员信息
	private class JsonHandler extends MJsonHttpHandler {

		private Dialog progressDialog;

		protected JsonHandler(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
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
					ToastManager.getInstance(context).showToast(
							response.getString("errorMsg"));
					stopService(new Intent()
							.setAction("com.meiyin.services.Meiyinservice"));
					startActivity(new Intent().setClass(context, Login.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					System.exit(0);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			try {
				JSONArray mli = response.getJSONArray("mlist");
				sp.edit().putString(SPConstant.COMMUNICATION, mli.toString())
						.commit();
				frist(mli.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void frist(String MIL) {
		mlist = new Gson().fromJson(MIL,
				new TypeToken<List<OverTimeTask_Entity>>() {
				}.getType());
		mDatas.clear();
		// 添加人员数据到Data
		for (OverTimeTask_Entity overTimeTask_Entity : mlist) {
			mDatas.add(new FileBean(overTimeTask_Entity.getId(),
					overTimeTask_Entity.getPid(),
					overTimeTask_Entity.getName(), false));
		}
		try {

			mAdapter = new SimpleTreeAdapter<FileBean>(comlist,
					CommunicationActivity.this, mDatas, 1);

			mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {

				@Override
				public void onClick(Node node, int position) {
					if (node.isLeaf()) {
						for (final OverTimeTask_Entity fileEntity : mlist) {
							if (fileEntity.getId() == node.getId()) {
								if (null == fileEntity.getUser_code()) {
									Toast.makeText(getApplicationContext(),
											"请选择公司职员！", Toast.LENGTH_SHORT)
											.show();
								} else {
									final AlertDialog dialog_app = builder
											.create();
									LinearLayout shenpi = (LinearLayout) LayoutInflater
											.from(context).inflate(
													R.layout.submit_dialog,
													null);
									TextView heads = (TextView) shenpi
											.findViewById(R.id.heads);
									Button button_t = (Button) shenpi
											.findViewById(R.id.button_t);
									Button button_f = (Button) shenpi
											.findViewById(R.id.button_f);
									heads.setText("您确定要拨打" + node.getName()
											+ "的号码吗？");
									dialog_app.show();
									dialog_app.getWindow().setContentView(
											shenpi);
									button_t.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											String phoneString;
											if (fileEntity.getPhone_num()
													.equals("")) {
												phoneString = fileEntity
														.getTelephone();
											} else {
												phoneString = fileEntity
														.getPhone_num();
											}
											if (phoneString.equals("")) {
												ToastManager
														.getInstance(context)
														.showToastcenter(
																"该成员号码为空！");
												return;
											}

											Intent intent = new Intent();
											intent.setAction(intent.ACTION_CALL);
											intent.setData(Uri.parse("tel:"
													+ phoneString));
											startActivity(intent);
											dialog_app.dismiss();
										}
									});

									button_f.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated method stub
											dialog_app.dismiss();
										}
									});

								}
							}
						}

					}
				}

			});
			comlist.setAdapter(mAdapter);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
