package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.IT_History_Adapter;
import com.meiyin.erp.adapter.SimpleTreeAdapter;
import com.meiyin.erp.app.FileBean;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.bean.Node;
import com.meiyin.erp.bean.TreeListViewAdapter;
import com.meiyin.erp.entity.Equip_Type_Entity;
import com.meiyin.erp.entity.IT_EventSp_Entity;
import com.meiyin.erp.entity.IT_Event_SQ_Entity;
import com.meiyin.erp.json.JsonHttpHandles;
import com.meiyin.erp.util.AsyncHttpclient_Util;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.meiyin.erp.util.ToastManager;
import com.meiyin.erp.util.ToastUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * IT运维管理事件详情页
 */
public class IT_Management_Sq extends Activity implements OnClickListener {

	private Context context;
	private Button chongxinfenpei, shouli, sqchongxinfenpei;
	private Animation left, right;
	private View xiahua_view;

	LinearLayout huadongview;
	private ArrayList<View> views;
	private ViewPager xqviewpager;
	private ViewPagerAdapter vpAdapter;
	private SharedPreferences sp;
	private ArrayList<IT_Event_SQ_Entity> lists;
	private TextView sjly, sjlx, sjbt, ywz, yxj, yh, lxdh, bgdz, sjms, sfld,
			zw;
	private IT_History_Adapter adapter;
	private int arg = 0;
	private TextView minute, situation;
	private String id, reciver, pool, state0, source0;
	private ArrayAdapter<String> spinnerAdapter;
	private ArrayList<String> mItems;
	private Builder builder;
	private ArrayList<IT_EventSp_Entity> SPlist;
	private String deal_type, deal_content = "", failure_desc = "", spinners;

	private View views1, views2, views3;
	private LinearLayout IT_Event_linerlayout_3, IT_Eventtype_linerlayout_3,
			Linearlaout_type_3;
	private TextView autocompletetextview;

	private String solution_type = "", event_type2;
	private ArrayList<Equip_Type_Entity> mlist;
	private EditText guzhangleixin,chulixinxi;
	private String pageType;
	private Dialog_Http_Util dic;
	private String jsonString="",jsonString2="";

	private TextView super_title;//标题
	private List<FileBean> mDatas = new ArrayList<FileBean>();
	private ListView mTree;
	private TreeListViewAdapter mAdapter;
	private TextView textView2;
	private AlertDialog dialogss;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.it_management_sq);
		context = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		dic=new Dialog_Http_Util();
		builder = new AlertDialog.Builder(IT_Management_Sq.this);
		lists = new ArrayList<IT_Event_SQ_Entity>();

		String event_no = getIntent().getStringExtra("event_no");
		Log.e("lyt", "2:" + event_no);
		initHeader();
		findview();
		intiview();
		httpsq(event_no, sp.getString(SPConstant.MY_TOKEN, ""));
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
		textView2 = (TextView) findViewById(R.id.headtitletext);
		textView2.setText("详细情况");
		}
	private void httpsq(String event_no, String key) {
		// TODO Auto-generated method stub
		dic.showWaitingDialog(this, "正在刷新", true);
		RequestParams params = new RequestParams();
		params.put("event_no", event_no);
		params.put("key", key);
		String string = APIURL.ITSM.IT_EVENT_SQ;
		AsyncHttpclient_Util.post(string, context, params, new JsonHandler());
	}

	private class JsonHandler extends JsonHttpHandles {
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			try {
				JSONObject data = response.getJSONObject("data");
				 
				jsonString=	data.toString();
				JSONObject permit = data.getJSONObject("permit");// 权限处理
				permit.getBoolean("SCFJ");// 关联附件
				boolean CLGD = permit.getBoolean("CLGD");// 执行任务
				boolean SLGD = permit.getBoolean("SLGD");// 受理

				permit.getBoolean("TJXGZY");// 关联设备
				boolean ZXFP=permit.getBoolean("ZXFP");// 申请重新分配
				permit.getBoolean("FP");// 确认分配
				permit.getBoolean("ZDHQ");// 主动获取
				permit.getBoolean("TJPZ");// 批注
				permit.getBoolean("MYDFK");// 满意度调查
				if (SLGD) {
					id = data.getString("id");// id
					reciver = data.getString("reciver");// reciver
					shouli.setText("受理");
					shouli.setVisibility(ViewGroup.VISIBLE);
				} else if (CLGD) {
					id = data.getString("id");// id
					reciver = data.getString("reciver");// reciver
					pool = data.getString("pool");// pool
					state0 = data.getString("state0");// state0
					source0 = data.getString("source0");// source0
					shouli.setText("执行任务");
					shouli.setVisibility(ViewGroup.VISIBLE);
				} else {
					shouli.setVisibility(ViewGroup.GONE);
				}
				if(ZXFP){
//					sqchongxinfenpei.setVisibility(ViewGroup.VISIBLE);
					sqchongxinfenpei.setVisibility(ViewGroup.GONE);
					}else{
					sqchongxinfenpei.setVisibility(ViewGroup.GONE);
				}
				String workspace = data.getString("workspace");
				if(workspace.equals("")){
					LinearLayout Linear_view_bgdz = (LinearLayout)findViewById(R.id.Linear_view_bgdz);
					Linear_view_bgdz.setVisibility(ViewGroup.GONE);
				}
				bgdz.setText(workspace);// 办公地址
				sjlx.setText(data.getString("event_type"));// 事件类型
				data.getString("state");// 事件状态
				String event_level = data.getString("event_level");// 优先级
				yxj.setText(event_level);// 优先级
				if(event_level.equals("高")||event_level.equals("紧急")){
					yxj.setTextColor(getResources().getColor(R.color._red));
				}
				sjbt.setText(data.getString("title"));// 标题
				sjly.setText(data.getString("source"));// 事件来源
				sjms.setText(data.getString("event_describe"));// 事件描述
				if(data.getString("mobile").equals("")){
					LinearLayout Linear_lxdh = (LinearLayout)findViewById(R.id.Linear_lxdh);
					Linear_lxdh.setVisibility(ViewGroup.GONE);
				}
				lxdh.setText(data.getString("mobile"));// 联系电话
				boolean area = data.isNull("area");
				if(area){
					ywz.setText("");
				}else{
				ywz.setText(data.getString("area"));// 运维组
				}
				if (data.isNull("recivern")) {
					yh.setText("");// 用户
				} else {
					yh.setText(data.getString("recivern"));// 用户
				}
				if (data.getString("Is_leader").equals("1")) {
					sfld.setText("是");
				} else if (data.getString("Is_leader").equals("0")) {
					sfld.setText("否");
					LinearLayout Linear_view_zw=(LinearLayout)findViewById(R.id.Linear_view_zw);
					Linear_view_zw.setVisibility(ViewGroup.GONE);
				}
				zw.setText(data.getString("position"));// 职务

				JSONArray array = (JSONArray) data.get("process");
				lists = new Gson().fromJson(array.toString(),
						new TypeToken<List<IT_Event_SQ_Entity>>() {
						}.getType());

				adapter.setList(lists);
				adapter.notifyDataSetChanged();
				
				dic.dismissWaitingDialog();
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
				ToastManager.getInstance(context).showimgToast("服务连接超时！");
			}
			dic.dismissWaitingDialog();
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			ToastUtil.showToast(context, "网络连接失败！");
			 dic.dismissWaitingDialog();
			super.onFailure(statusCode, headers, responseString, throwable);
		}
	}

	private void findview() {

		left = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		right = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);

		xiahua_view = findViewById(R.id.xiahua_view);
		huadongview = (LinearLayout) findViewById(R.id.huadongview);

		minute = (TextView) findViewById(R.id.minute);// 详细情况
		minute.setOnClickListener(new click());
		situation = (TextView) findViewById(R.id.situation);// 处理情况
		situation.setOnClickListener(new click());

		xqviewpager = (ViewPager) findViewById(R.id.xqviewpager);
		xqviewpager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						// TODO Auto-generated method stub
						arg = arg0;
						xqviewpager.setCurrentItem(arg0);
						if (arg0 == 0) {
							huadongview.startAnimation(right);
							textView2.setText("详细情况");
							minute.setTextColor(getResources().getColor(
									R.color._red));
							situation.setTextColor(getResources().getColor(
									R.color.text_graya));
						} else {
							textView2.setText("处理情况");
							minute.setTextColor(getResources().getColor(
									R.color.text_graya));
							situation.setTextColor(getResources().getColor(
									R.color._red));
							huadongview.startAnimation(left);
						}
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub

					}
				});

		chongxinfenpei = (Button) findViewById(R.id.chongxinfenpei);
		shouli = (Button) findViewById(R.id.shouli);
		sqchongxinfenpei = (Button) findViewById(R.id.sqchongxinfenpei);
		chongxinfenpei.setOnClickListener(this);
		shouli.setOnClickListener(this);
		sqchongxinfenpei.setOnClickListener(this);

	}

	public void intiview() {

		views = new ArrayList<View>();
		View left = getLayoutInflater().inflate(R.layout.page_left_item, null);
		View right = getLayoutInflater()
				.inflate(R.layout.page_right_item, null);

		sjly = (TextView) left.findViewById(R.id.sjly);// 事件来源
		sjlx = (TextView) left.findViewById(R.id.sjlx);// 事件类型
		sjbt = (TextView) left.findViewById(R.id.sjbt);// 标题
		ywz = (TextView) left.findViewById(R.id.ywz);// 运维组
		yxj = (TextView) left.findViewById(R.id.yxj);// 优先级
		yh = (TextView) left.findViewById(R.id.yh);// 用户
		lxdh = (TextView) left.findViewById(R.id.lxdh);// 联系电话
		bgdz = (TextView) left.findViewById(R.id.bgdz);// 办公地址
		sjms = (TextView) left.findViewById(R.id.sjms);// 事件描述
		sfld = (TextView) left.findViewById(R.id.sfld);// 是否领导
		zw = (TextView) left.findViewById(R.id.zw);// 职务

		ListView listview = (ListView) right.findViewById(R.id.listview);
		adapter = new IT_History_Adapter(context);
		adapter.setList(lists);
		listview.setAdapter(adapter);

		views.add(left);
		views.add(right);
		vpAdapter = new ViewPagerAdapter();
		xqviewpager.setAdapter(vpAdapter);
	}

	public class ViewPagerAdapter extends PagerAdapter {

		public ViewPagerAdapter() {
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			return views.get(arg1);
		}

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
	public class click implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.situation:
				if (arg == 0) {
					minute.setTextColor(getResources().getColor(
							R.color._red));
					situation.setTextColor(getResources().getColor(
							R.color.text_graya));
					huadongview.startAnimation(left);
					xqviewpager.setCurrentItem(1);
					textView2.setText("处理情况");
				}
				break;
			case R.id.minute:
				if (arg == 1) {
					textView2.setText("详细情况");
					minute.setTextColor(getResources().getColor(R.color.text_graya));
					situation.setTextColor(getResources().getColor(
							R.color._red));
					huadongview.startAnimation(right);
					xqviewpager.setCurrentItem(0);
				}
				break;
			default:
				break;
			}
		}

	}

	// 事件受理
	private void event_accept() {
		RequestParams params = new RequestParams();
		params.put("action_value", "SLGD");
		params.put("opType", "edit");
		params.put("event_id", id);
		params.put("event_no", getIntent().getStringExtra("event_no"));
		params.put("menu_value", "SJCL");
		params.put("reciver", reciver);
		params.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
		Log.e("lyt", params.toString());
		String string = APIURL.ITSM.IT_EVENT_ACCEPT;
		AsyncHttpclient_Util.post(string, context, params, new JsonHandlers());
	}

	// 执行任务内容
	private void event_execute() {
		RequestParams params = new RequestParams();
		params.put("menu_id", "01020403");
		params.put("action_value", "CLGD");
		params.put("pool", pool);
		params.put("source", source0);
		params.put("state", state0);
		params.put("event_id", id);
		params.put("event_no", getIntent().getStringExtra("event_no"));
		params.put("menu_value", "SJCL");
		params.put("recivers", reciver);
		params.put("key", sp.getString(SPConstant.MY_TOKEN, ""));

		String string = APIURL.ITSM.IT_EVENT_EXECUTE;
		AsyncHttpclient_Util.post(string, context, params, new JsonHandlerss());
	}

	// 执行任务处理
	private void event_executes() {
		RequestParams params = new RequestParams();
		// params.put("menu_id", "01020405");
		// params.put("zssbj", zssbj);
		String string = APIURL.ITSM.IT_EVENT_EXECUTES;
		if (spinners.equals("执行任务") || spinners.equals("现场服务")
				|| spinners.equals("电话解决")) {
			params.put("deal_content", deal_content);// 执行任务
		} else if (spinners.equals("挂起任务")) {
			if (deal_content.equals("")) {
				ToastManager.getInstance(context).showimgToast("处理信息不能为空！");
				dic.dismissWaitingDialog();
				return;
			}
			params.put("deal_content", deal_content);
		} else if (spinners.equals("未解决") || spinners.equals("已解决")) {
			if (failure_desc.equals("")) {
				ToastManager.getInstance(context).showimgToast("故障现象不能为空！");
				return;
			}
			if(solution_type.equals("-1")){
				ToastManager.getInstance(context).showimgToast("请填写服务方式！");
				dic.dismissWaitingDialog();
				return;
			}
			if (deal_content.equals("")) {
				ToastManager.getInstance(context).showimgToast("处理信息不能为空！");
				return;
			}
			params.put("solution_type", solution_type);
			params.put("event_type2", event_type2);
			params.put("deal_content", deal_content);
			params.put("failure_desc", failure_desc);

		} else if (spinners.equals("升级至二线")) {
			if (solution_type.equals("-1")) {
				ToastManager.getInstance(context).showimgToast("请填写服务方式！");
				dic.dismissWaitingDialog();
				return;
			}
			if (deal_content.equals("")) {
				ToastManager.getInstance(context).showimgToast("处理信息不能为空！");
				dic.dismissWaitingDialog();
				return;
			}
			string = APIURL.ITSM.IT_EVENT_RISE_TWO;
			params.put("solution_type", solution_type);
			params.put("event_type2", event_type2);
			params.put("deal_content", deal_content);
		} else if (spinners.equals("--请选择--")){
			ToastManager.getInstance(context).showimgToast("请填写处理类型！");
			dic.dismissWaitingDialog();
			return;
		}

		params.put("deal_type", deal_type);
		params.put("opType", "edit");
		params.put("action_value", "CLGD");
		params.put("pool", pool);
		params.put("event_id", id);
		params.put("menu_value", "SJCL");
		params.put("event_no", getIntent().getStringExtra("event_no"));
		params.put("recivers", reciver);
		params.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
		
		AsyncHttpclient_Util
				.post(string, context, params, new JsonHandlersss());
	}

	@Override
	public void onClick(View arg0) {
		final AlertDialog dialog;
		final AlertDialog dialog_leixin;
		// TODO Auto-generated method stub
		dialog_leixin = new AlertDialog.Builder(this).create();
		dialog = new AlertDialog.Builder(this).create();
		LinearLayout submit_dialog = (LinearLayout) LayoutInflater
				.from(context).inflate(R.layout.submit_dialog, null);
		TextView heads = (TextView) submit_dialog.findViewById(R.id.heads);
		Button button_t = (Button) submit_dialog.findViewById(R.id.button_t);
		Button button_f = (Button) submit_dialog.findViewById(R.id.button_f);
		switch (arg0.getId()) {
		case R.id.chongxinfenpei:
			heads.setText("您必须先撤销该事件的上一次分配,才能进行下一次的分配！！！");
			dialog.show();
			dialog.getWindow().setContentView(submit_dialog);
			button_t.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ToastUtil.showToast(context, "修改事件信息处理成功！");
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
			break;
		case R.id.shouli:
			if (shouli.getText().equals("受理")) {
				heads.setText("您确定要受理编号为"
						+ getIntent().getStringExtra("event_no") + "的服务请求事件吗？");
				dialog.show();
				dialog.getWindow().setContentView(submit_dialog);
				button_t.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dic.showWaitingDialog(IT_Management_Sq.this, "正在受理", true);
						event_accept();
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
			} else if (shouli.getText().equals("执行任务")) {
				dic.showWaitingDialog(this, "正在刷新", true);
				event_execute();
				if (dialog != null) {
					dialog.dismiss();
				}
				LinearLayout sbo = (LinearLayout) LayoutInflater.from(context)
						.inflate(R.layout.sb_zhixingrenwu_dialog, null);
				chulixinxi = (EditText) sbo
						.findViewById(R.id.chulixinxi);
				guzhangleixin = (EditText) sbo.findViewById(R.id.guzhangleixin);
				super_title=(TextView) sbo
						.findViewById(R.id.super_title);
				
				IT_Event_linerlayout_3 = (LinearLayout) sbo
						.findViewById(R.id.IT_Event_linerlayout_3);
				IT_Eventtype_linerlayout_3 = (LinearLayout) sbo
						.findViewById(R.id.IT_Eventtype_linerlayout_3);
				Linearlaout_type_3 = (LinearLayout) sbo
						.findViewById(R.id.Linearlaout_type_3);
				views1 = sbo.findViewById(R.id.views1);
				views2 = sbo.findViewById(R.id.views2);
				views3 = sbo.findViewById(R.id.views3);
				//事件类型获取
				autocompletetextview = (TextView) sbo
						.findViewById(R.id.autocompletetextview);
				autocompletetextview.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
				        RelativeLayout thisink = (RelativeLayout) LayoutInflater.from(
				    					context).inflate(R.layout.leixing_main, null);		
				        mTree=(ListView) thisink.findViewById(R.id.id_tree);
						try
						{
							mAdapter = new SimpleTreeAdapter<FileBean>(mTree,IT_Management_Sq.this, mDatas, 0);
							mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
							{
								@Override
								public void onClick(Node node, int position)
								{
									if (node.isLeaf())
									{
										autocompletetextview.setText(node.getName());
										Toast.makeText(getApplicationContext(), node.getName(),
												Toast.LENGTH_SHORT).show();
										dialog_leixin.dismiss();
									}
								}

							});
							
							mTree.setAdapter(mAdapter);
						} catch (IllegalAccessException e)
						{
							e.printStackTrace();
						}
						dialog_leixin.show();
						dialog_leixin.getWindow().setContentView(thisink);
					}

				});
				builder.setView(sbo);
				dialogss = builder.create();
				String mitem[] = getResources().getStringArray(
						R.array.spinnersb_renwus);
				ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(
						context, android.R.layout.simple_spinner_item, mitem);
				spinnerAdapter1
						.setDropDownViewResource(R.layout.spinner_drop_down_item);
				Spinner fuwu_type = (Spinner) sbo.findViewById(R.id.fuwu_type);
				fuwu_type.setAdapter(spinnerAdapter1);
				fuwu_type
						.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								solution_type =""+(arg2);
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});
				mItems = new ArrayList<String>();
				spinnerAdapter = new ArrayAdapter<String>(context,
						android.R.layout.simple_spinner_item, mItems);
				spinnerAdapter
						.setDropDownViewResource(R.layout.spinner_drop_down_item);
				Spinner spinner_sbrenwu = (Spinner) sbo
						.findViewById(R.id.spinner_sbrenwu);
				spinner_sbrenwu
						.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								spinners = arg0.getItemAtPosition(arg2)
										.toString();
								if (spinners.equals("执行任务")) {
									deal_type = SPlist.get(arg2).getValue();
								} else if (spinners.equals("挂起任务")) {
									deal_type = SPlist.get(arg2).getValue();
								} else if (spinners.equals("现场服务")) {
									deal_type = SPlist.get(arg2).getValue();
								} else if (spinners.equals("电话解决")) {
									deal_type = SPlist.get(arg2).getValue();
								} else if (spinners.equals("已解决")) {
									Linearlaout_type_3.setVisibility(ViewGroup.VISIBLE);
									views2.setVisibility(ViewGroup.VISIBLE);
									deal_type = SPlist.get(arg2).getValue();
								} else if (spinners.equals("未解决")) {
									Linearlaout_type_3.setVisibility(ViewGroup.VISIBLE);
									views2.setVisibility(ViewGroup.VISIBLE);
									deal_type = SPlist.get(arg2).getValue();
								} else if (spinners.equals("升级至二线")) {
									deal_type = SPlist.get(arg2).getValue();
									Linearlaout_type_3.setVisibility(ViewGroup.GONE);
									views2.setVisibility(ViewGroup.GONE);
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});
				spinner_sbrenwu.setAdapter(spinnerAdapter);
				TextView button_queding_chuli = (TextView) sbo
						.findViewById(R.id.button_queding_chuli);
				button_queding_chuli.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(pageType.equals("3")){
						String au = autocompletetextview.getText().toString();
						Boolean is=false;
						for (int i = 0; i < mlist.size(); i++) {
							if (au.equals(mlist.get(i).getCode_desc())) {
								event_type2 = mlist.get(i).getCode_id();
								is=true;
							}
						}
						if(!is){
							ToastManager.getInstance(context).showimgToast("事件类型选择错误！");
							dic.dismissWaitingDialog();
							return;
						}
						failure_desc = guzhangleixin.getText().toString();
						}
						deal_content = chulixinxi.getText()
								.toString();
						dic.showWaitingDialog(IT_Management_Sq.this, "正在执行", true);
						event_executes();
						dialogss.dismiss();
					}
				});

			}
			break;
		case R.id.sqchongxinfenpei:
			RequestParams params = new RequestParams();
			params.put("menu_id", "01020405");
			params.put("action_value", "ZXFP");
			params.put("pool", pool);
			params.put("state", state0);
			params.put("event_id", id);
			params.put("event_no", getIntent().getStringExtra("event_no"));
			params.put("menu_value", "SJCL");
			params.put("recivers", reciver);
			params.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
			String string = APIURL.ITSM.IT_EVENT_APPLY_ASSIGN_ONE;
			AsyncHttpclient_Util.post(string, context, params, new JsonHttpResponseHandlersq(0));

			LinearLayout sb = (LinearLayout) LayoutInflater.from(context)
					.inflate(R.layout.sb_dialog, null);
			final EditText shenqingyuanyin = (EditText) sb
					.findViewById(R.id.shenqingyuanyin);
			Button button_queding = (Button) sb
					.findViewById(R.id.button_queding);

			builder.setView(sb);
			final AlertDialog dialog1 = builder.create();

			button_queding.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(shenqingyuanyin.getText().toString().trim().equals("")){
						ToastManager.getInstance(context).showimgToast(
								"申请原因不能为空！");
						return;
					}
					if(!jsonString2.equals("")){
					dic.showWaitingDialog(IT_Management_Sq.this, "正在申请..", false);
					RequestParams params = new RequestParams();
					params.put("menu_id", "01020405");
					params.put("deal_content", shenqingyuanyin.getText().toString());
					params.put("action_value", "ZXFP");
					params.put("pool", pool);
					params.put("opType", "edit");
					params.put("deal_type", "redo");
					params.put("event_id", id);
					params.put("event_no", getIntent().getStringExtra("event_no"));
					params.put("menu_value", "SJCL");
					params.put("recivers", reciver);
					params.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
					String string = APIURL.ITSM.IT_EVENT_APPLY_ASSIGN_TWO;
					AsyncHttpclient_Util.post(string, context, params, new JsonHttpResponseHandlersq(1));
					dialog1.dismiss();
					}else{
						ToastManager.getInstance(context).showimgToast(
								"网络连接失败！请重新填写原因！");
						dialog1.dismiss();
					}
				}
			});
			dialog1.show();
			dialog1.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
			dialog1.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			break;

		default:
			break;
		}
	}

	private class JsonHandlers extends JsonHttpHandles {
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			try {
				boolean success = response.getBoolean("success");
				httpsq(getIntent().getStringExtra("event_no"),
						sp.getString(SPConstant.MY_TOKEN, ""));
				if (success) {
					ToastManager.getInstance(context).showimgToast(
							"恭喜你,添加事件处理信息成功！");
				} else {
					ToastManager.getInstance(context).showimgToast(
							response.getString("message"));
				}
				dic.dismissWaitingDialog();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			if (statusCode == 0
					&& throwable.toString().contains("HttpHostConnect")) {
				ToastManager.getInstance(context).showimgToast(
						"网络连接失败,请检查网络设置！");
			} else if (statusCode == 200) {
				ToastManager.getInstance(context).showimgToast("账号异常请联系软件研发部！");
			} else if (statusCode == 0
					&& throwable.toString().contains("Timeout")) {
				ToastManager.getInstance(context).showimgToast("服务连接超时！");
			}
			dic.dismissWaitingDialog();
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			ToastUtil.showToast(context, "网络连接失败！");
			dic.dismissWaitingDialog();
			super.onFailure(statusCode, headers, responseString, throwable);
		}
	}

	private class JsonHandlerss extends JsonHttpHandles {
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			dic.dismissWaitingDialog();
			dialogss.show();
			dialogss.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
			dialogss.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			try {
				pageType = response.getString("pageType");
				if (pageType.equals("1")) {			//执行任务一部
					super_title.setText("处理信息：");
					chulixinxi.setHint("请输入处理信息！");
					JSONArray worktype = response.getJSONArray("worktype");
					mItems.clear();
					SPlist = new Gson().fromJson(worktype.toString(),
							new TypeToken<List<IT_EventSp_Entity>>() {
							}.getType());
					for (int a = 0; a < SPlist.size(); a++) {
						mItems.add(SPlist.get(a).getKey());
					}
					spinnerAdapter.notifyDataSetChanged();
				} else if (pageType.equals("2")) {			//执行任务二部
					super_title.setText("处理信息：");
					chulixinxi.setHint("请输入处理信息！");
					JSONArray worktype = response.getJSONArray("worktype");
					mItems.clear();
					SPlist = new Gson().fromJson(worktype.toString(),
							new TypeToken<List<IT_EventSp_Entity>>() {
							}.getType());
					for (int a = 0; a < SPlist.size(); a++) {
						mItems.add(SPlist.get(a).getKey());
					}
					spinnerAdapter.notifyDataSetChanged();
				} else if (pageType.equals("3")) {             //执行任务三部
					IT_Event_linerlayout_3.setVisibility(ViewGroup.VISIBLE);
					IT_Eventtype_linerlayout_3.setVisibility(ViewGroup.VISIBLE);
					Linearlaout_type_3.setVisibility(ViewGroup.VISIBLE);
					super_title.setText("解决方案：");
					chulixinxi.setHint("请输入解决方案！");
					views1.setVisibility(ViewGroup.VISIBLE);
					views2.setVisibility(ViewGroup.VISIBLE);
					views3.setVisibility(ViewGroup.VISIBLE);
					JSONArray worktype = response.getJSONArray("worktype");
					mItems.clear();
					SPlist = new Gson().fromJson(worktype.toString(),
							new TypeToken<List<IT_EventSp_Entity>>() {
							}.getType());

					for (int a = 0; a < SPlist.size(); a++) {
						mItems.add(SPlist.get(a).getKey());
					}
					spinnerAdapter.notifyDataSetChanged();

					//获取事件类型列表
					mlist = new Gson().fromJson(sp.getString(SPConstant.IT_MANAGEMENT_TYPE, ""),
							new TypeToken<List<Equip_Type_Entity>>() {
							}.getType());
					mDatas.clear();
					//添加事件类型数据到Data
					for (int a = 0; a < mlist.size(); a++) {
						mDatas.add(new FileBean(Integer.valueOf(mlist.get(a).getCode_id()),Integer.valueOf(mlist.get(a).getParent_id()), mlist.get(a).getCode_desc(),false));

					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			dic.dismissWaitingDialog();
			if (statusCode == 0
					&& throwable.toString().contains("HttpHostConnect")) {
				ToastManager.getInstance(context).showimgToast(
						"网络连接失败,请检查网络设置！");
			} else if (statusCode == 200) {
				ToastManager.getInstance(context).showimgToast("账号异常请联系软件研发部！");
			} else if (statusCode == 0
					&& throwable.toString().contains("Timeout")) {
				ToastManager.getInstance(context).showimgToast("服务连接超时！");
			}
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			dic.dismissWaitingDialog();
			ToastUtil.showToast(context, "网络连接失败！");
			super.onFailure(statusCode, headers, responseString, throwable);
		}
	}

	private class JsonHandlersss extends JsonHttpHandles {
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			ToastManager.getInstance(context).showimgToast("添加事件信息处理成功！");

			try {
				boolean success = response.getBoolean("success");
				if (success) {
					ToastManager.getInstance(context).showimgToast(
							"恭喜你,添加事件处理信息成功！");
					httpsq(getIntent().getStringExtra("event_no"),
							sp.getString(SPConstant.MY_TOKEN, ""));
				} else {
					ToastManager.getInstance(context).showimgToast(
							response.getString("message"));
				}
				dic.dismissWaitingDialog();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			if (statusCode == 0
					&& throwable.toString().contains("HttpHostConnect")) {
				ToastManager.getInstance(context).showimgToast(
						"网络连接失败,请检查网络设置！");
			} else if (statusCode == 200) {
				ToastManager.getInstance(context).showimgToast("账号异常请联系软件研发部！");
			} else if (statusCode == 0
					&& throwable.toString().contains("Timeout")) {
				ToastManager.getInstance(context).showimgToast("服务连接超时！");
			}
			dic.dismissWaitingDialog();
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			ToastUtil.showToast(context, "网络连接失败！");
			super.onFailure(statusCode, headers, responseString, throwable);
			dic.dismissWaitingDialog();
		}
	}
	private class JsonHttpResponseHandlersq extends JsonHttpHandles {
		private int index;
		private JsonHttpResponseHandlersq(int index){
			this.index=index;
		}
		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			if(index==0){
			JSONObject rCombomData;
			try {
				String string= response.getString("rCombomData");
				 rCombomData = new JSONObject(string);
				 JSONArray list = rCombomData.getJSONArray("list");
				 jsonString2 =list.toString();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}else if(index==1){
				if(!jsonString.equals("")){
				Intent intent=new Intent(IT_Management_Sq.this,IT_Management_Update.class);
				intent.putExtra("string1", jsonString);
				intent.putExtra("string2", jsonString2);
				intent.putExtra("id", id);
				intent.putExtra("event_no", getIntent().getStringExtra("event_no"));
				startActivity(intent);
				dic.dismissWaitingDialog();
				ToastManager.getInstance(context).showimgToast(
						"恭喜你,事件处理成功！");
				IT_Management_Sq.this.finish();
				}
				
			}
			
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			if (statusCode == 0
					&& throwable.toString().contains("HttpHostConnect")) {
				ToastManager.getInstance(context).showimgToast(
						"网络连接失败,请检查网络设置！");
			} else if (statusCode == 200) {
				ToastManager.getInstance(context).showimgToast("账号异常请联系软件研发部！");
			} else if (statusCode == 0
					&& throwable.toString().contains("Timeout")) {
				ToastManager.getInstance(context).showimgToast("服务连接超时！");
			}
			dic.dismissWaitingDialog();
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			ToastUtil.showToast(context, "网络连接失败！");
			super.onFailure(statusCode, headers, responseString, throwable);
			dic.dismissWaitingDialog();
		}
	}
	
}
