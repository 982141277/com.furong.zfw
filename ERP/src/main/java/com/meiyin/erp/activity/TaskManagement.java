package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.MyTaskAdapter;
import com.meiyin.erp.adapter.MyTaskListAdapter;
import com.meiyin.erp.adapter.SimpleTreeAdapter;
import com.meiyin.erp.adapter.Spinner_Adapter;
import com.meiyin.erp.app.FileBean;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.bean.Node;
import com.meiyin.erp.bean.TreeListViewAdapter;
import com.meiyin.erp.entity.MyTaskEntity;
import com.meiyin.erp.entity.MyTaskListEntity;
import com.meiyin.erp.entity.OverTimeTask_Entity;
import com.meiyin.erp.entity.Spinner_Entity;
import com.meiyin.erp.ui.XListView;
import com.meiyin.erp.util.AndroidUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务日志管理页
 */
public class TaskManagement extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {
	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	private Context mContext;
	private TextView /*text1, */text2, text3;
	private View /*taskview_one*/ taskview_two, taskview_three;
//	private EditText task_name, task_context;
	private SharedPreferences sp;
	private Dialog_Http_Util dhu;
	private TextView task_name2;
	private XListView task_listview2;
	private XListView task_listview3;
	private Handler mHandler;
	private ArrayList<MyTaskEntity> tasklist;
	private MyTaskAdapter adapter;
	private MyTaskListAdapter taskadapter;
//	private TextView task_write_time;
//	private String reportId = "";
	private ArrayList<MyTaskListEntity> mytasklist;
	private AlertDialog dialog_list;
	private List<FileBean> mDatas = new ArrayList<FileBean>();
	private ArrayList<OverTimeTask_Entity> lists;
	String yearse = "";// 年
	String month = "";// 月
	private int load=1;
	String typenameString="1";
	int day=0;
	String staffcode="";
	private ImageView add_newtask_img;
	private TextView headtitletext;
	private int isboss;
	private JSONArray mlist = null;;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taskmanagement);
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		activity=this;
		mContext = getApplicationContext();
		dhu = new Dialog_Http_Util();
		initHeader();
		// 初始化头部
		initview();
		// 初始化viewpager页面
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
		headtitletext = (TextView) findViewById(R.id.headtitletext);
		headtitletext.setText("日志管理");
		add_newtask_img = (ImageView) findViewById(R.id.add_newtask_img);
		add_newtask_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("mlist", mlist.toString());
				intent.putExtra("result", 13);
				intent.setClass(TaskManagement.this,NewAddTaskActivity.class);
				startActivityForResult(intent, 13);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
	/*	case R.id.text1:
			vp.setCurrentItem(0);
			onPageSelected(0);

			break;*/
		case R.id.text2:
			vp.setCurrentItem(0);
			onPageSelected(0);
			break;
		case R.id.text3:
			vp.setCurrentItem(1);
			onPageSelected(1);
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

	@SuppressLint("CutPasteId")
	private void initViews() {
		views = new ArrayList<View>();
		// 初始化引导图片列表
//		LinearLayout task_text_one = (LinearLayout) LayoutInflater.from(
//				mContext).inflate(R.layout.task_text_one, null);
		LinearLayout task_text_two = (LinearLayout) LayoutInflater.from(
				mContext).inflate(R.layout.task_text_two, null);
		LinearLayout task_text_three = (LinearLayout) LayoutInflater.from(
				mContext).inflate(R.layout.task_text_three, null);
//		task_context = (EditText) task_text_one.findViewById(R.id.task_context);
//		task_name = (EditText) task_text_one.findViewById(R.id.task_name);
//		task_write_time = (TextView) task_text_one
//				.findViewById(R.id.task_write_time);
//		Button conserve = (Button) task_text_one.findViewById(R.id.conserve);
		task_name2 = (TextView) task_text_two.findViewById(R.id.task_name2);// 我的日志标题
		TextView all_task_name = (TextView) task_text_two.findViewById(R.id.all_task_name);// 全部日志
		
		dialog_list = new AlertDialog.Builder(this).create();
		all_task_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				RelativeLayout thisink = (RelativeLayout) LayoutInflater.from(
						mContext).inflate(R.layout.leixing_main, null);
				ListView mTree = (ListView) thisink.findViewById(R.id.id_tree);
				try {
					SimpleTreeAdapter<FileBean> mAdapter = new SimpleTreeAdapter<FileBean>(mTree,
							TaskManagement.this, mDatas, 0);
					mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
						@Override
						public void onClick(Node node, int position) {
							if (node.isLeaf()) {
									for (OverTimeTask_Entity overtask : lists) {
										if(overtask.getId()== node.getId()){
											String name=node.getName();
											String texts = "<font color=\"#ED1C24\">"+name+"</font>" + yearse
											+ "年" + month + "月工作日志";
											task_name2.setText(Html.fromHtml(texts));
											if (null == overtask.getUser_code()) {

											} else {
												staffcode=overtask.getUser_code();
												HTTP(overtask.getUser_code(),"");
											}
											
										}
									}
								
								dialog_list.dismiss();
							}
						}

					});

					mTree.setAdapter(mAdapter);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				if(mDatas.size()>0){
				dialog_list.show();
				dialog_list.getWindow().setContentView(thisink);
				}
			}
		});
		task_listview2 = (XListView) task_text_two
				.findViewById(R.id.task_listview2);// 我的日志
		Button last_work_button = (Button) task_text_two
				.findViewById(R.id.last_work_button);//上一周
		Button next_work_button = (Button) task_text_two
				.findViewById(R.id.next_work_button);//下一周
		last_work_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				day=day-1;
				HTTP(staffcode,String.valueOf(day));
			}
		});
		next_work_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				day=day+1;
				HTTP(staffcode,String.valueOf(day));
			}
		});
		
		mHandler = new Handler();
		task_listview2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				if (arg2 > 0) {
					if (tasklist.size() > 0) {
						String List = tasklist.get(arg2 - 1).getDtoList();
						String Stafforg = tasklist.get(arg2 - 1).getStafforg();
						String Work_date = tasklist.get(arg2 - 1)
								.getWork_date();
						intent.setClass(TaskManagement.this,
								MyTaskActivity.class);
						intent.putExtra("List", List);
						intent.putExtra("Stafforg", Stafforg);
						intent.putExtra("Work_date", Work_date);
						startActivityForResult(intent, 1);
					}
				}
			}
		});
		task_listview2.setPullLoadEnable(false);
		task_listview2.setXListViewListener(new XListView.IXListViewListener() {

			@Override
			public void onRefresh() {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						HTTP("","");
					}
				}, 100);
			}

			@Override
			public void onLoadMore() {

			}
		});
		task_listview2.setRefreshTime(sp.getString(SPConstant.REFRESHTIME, ""));
		adapter = new MyTaskAdapter(mContext);
		tasklist = new ArrayList<MyTaskEntity>();
		adapter.setList(tasklist);
		task_listview2.setAdapter(adapter);
		adapter.setOnRightItemClickListener(new MyTaskAdapter.onRightItemClickListener() {

			@Override
			public void onRightItemClick(View v, int position) {
				// TODO Auto-generated method stub
//				vp.setCurrentItem(0);
				Intent intent = new Intent();

					if (tasklist.size() > 0) {
						String Work_date = tasklist.get(position).getWork_date();
						intent.setClass(TaskManagement.this,
								TaskWriteActivity.class);
						intent.putExtra("reportId", "");
						intent.putExtra("Work_date", Work_date);
						intent.putExtra("result", 12);
						startActivityForResult(intent, 12);
					
				}
//				task_write_time.setText(tasklist.get(position).getWork_date());
//				reportId = "";
			}
		});
		HTTP("","");
		userlisttask();
//		conserve.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				String name = task_name.getText().toString();
//				String context = task_context.getText().toString();
//				if (name.equals("") || context.equals("")) {
//					ToastManager.getInstance(mContext)
//							.showToast("日志标题或内容不能为空！");
//				} else {
//					dhu.showWaitingDialog(TaskManagement.this, "正在提交", false);
//					RequestParams params = new RequestParams();
//					String key = sp.getString(SPConstant.MY_TOKEN, "");
//					params.put("key", key);
//					params.put("workDate", task_write_time.getText().toString());
//					params.put("reportId", reportId);
//					params.put("reportName", name);
//					params.put("reportNotes", context);
//					AsyncHttp(APIURL.TASKWRITEAPI, params);
//				}
//			}
//		});
		final ArrayList<Spinner_Entity> spinner_Entities = new ArrayList<Spinner_Entity>();
		Spinner task_text_spn3 = (Spinner) task_text_three.findViewById(R.id.task_text_spn3);

		spinner_Entities.add(new Spinner_Entity("未完成的任务", "1"));
		spinner_Entities.add(new Spinner_Entity("如期完成的任务", "2"));
		spinner_Entities.add(new Spinner_Entity("超期完成的任务", "3"));
		spinner_Entities.add(new Spinner_Entity("应知晓的任务", "4"));
		Spinner_Adapter spinnerAdapter = new Spinner_Adapter(mContext);
		spinnerAdapter.setList(spinner_Entities);
		task_text_spn3.setAdapter(spinnerAdapter);
//		task_text_spn3.setOnClickListener(new)
		task_text_spn3.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				typenameString=spinner_Entities.get(arg2).getType_id();
				mytasklist.clear();
				load = 1;
				mytasklist(0,typenameString);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
	
			}
		});
		task_listview3 = (XListView) task_text_three
				.findViewById(R.id.task_listview3);// 我的任务
		task_listview3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				if (arg2 > 0) {
					if (mytasklist.size() > 0) {

						String Creat_username = mytasklist.get(arg2 - 1)
								.getCreat_username();
						String Task_name = mytasklist.get(arg2 - 1)
								.getTask_name();
						String Task_target = mytasklist.get(arg2 - 1)
								.getTask_target();
						String Source = mytasklist.get(arg2 - 1).getSource();
						String Operatorname = mytasklist.get(arg2 - 1)
								.getOperatorname();
						String Knowername = mytasklist.get(arg2 - 1)
								.getKnowername();
						String Task_notes = mytasklist.get(arg2 - 1)
								.getTask_notes();
						String Start_time = mytasklist.get(arg2 - 1)
								.getStart_time();
						String End_time = mytasklist.get(arg2 - 1)
								.getEnd_time();
						String Task_type = mytasklist.get(arg2 - 1)
								.getTask_type();
						String Task_status = mytasklist.get(arg2 - 1)
								.getTask_status();
						String Task_id = mytasklist.get(arg2 - 1)
								.getTask_id();
						intent.setClass(TaskManagement.this,
								MyTaskListDetailsActivity.class);
						intent.putExtra("Creat_username", Creat_username);
						intent.putExtra("Task_name", Task_name);
						intent.putExtra("Task_target", Task_target);
						intent.putExtra("Source", Source);
						intent.putExtra("Operatorname", Operatorname);
						intent.putExtra("Knowername", Knowername);
						intent.putExtra("Task_notes", Task_notes);
						intent.putExtra("Start_time", Start_time);
						intent.putExtra("End_time", End_time);
						intent.putExtra("Task_type", Task_type);
						intent.putExtra("Task_status", Task_status);
						intent.putExtra("Task_id", Task_id);
						startActivityForResult(intent, 11);
					}
				}
			}
		});
	
		task_listview3.setPullLoadEnable(true);
		task_listview3.setXListViewListener(new XListView.IXListViewListener() {

			@Override
			public void onRefresh() {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mytasklist.clear();
						 load = 1;
						mytasklist(0,typenameString);
					}
				}, 1);
			}

			@Override
			public void onLoadMore() {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mytasklist(load*10,typenameString);
						load++;
					}
				}, 1);
			}
		});
		
		
		task_listview3.setRefreshTime(sp.getString(SPConstant.REFRESHTIME, ""));
		taskadapter = new MyTaskListAdapter(mContext);
		mytasklist = new ArrayList<MyTaskListEntity>();
		taskadapter.setList(mytasklist);
		task_listview3.setAdapter(taskadapter);

//		views.add(task_text_one);
		views.add(task_text_two);
		views.add(task_text_three);

		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter();
		vp = (ViewPager) findViewById(R.id.viewpagerss);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}

	private void HTTP(String code,String weeks) {
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		AsyncHttpClientUtil Async = new AsyncHttpClientUtil();
		Dialog_Http_Util dialog_util = new Dialog_Http_Util();
		RequestParams param = new RequestParams();
		param.put("key", key);
		if(!code.equals("")){
		param.put("staffCode", code);
		}
		if(!weeks.equals("")){
			param.put("day", weeks);
			if(!month.equals("")&&!yearse.equals("")){
			param.put("yearse", yearse);
			param.put("month", month);}
		}
		Dialog progressDialog = dialog_util.showWaitingDialog(
				TaskManagement.this, "正在加载", false);
		Async.post(APIURL.TASKDETAILSAPI, mContext, param,
				new JsonHandlerTaskDetails(code,TaskManagement.this, progressDialog));

	}

	private void userlisttask() {
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		AsyncHttpClientUtil Async = new AsyncHttpClientUtil();
		Dialog_Http_Util dialog_util = new Dialog_Http_Util();
		RequestParams param = new RequestParams();
		param.put("key", key);
		Dialog progressDialog = dialog_util.showWaitingDialog(
				TaskManagement.this, "正在加载", false);
		Async.post(APIURL.USERLISTFORALLTASK, mContext, param,
				new JsonHandleruserlist(TaskManagement.this, progressDialog));

	}

	private class JsonHandleruserlist extends MJsonHttpHandler {
		Dialog progressDialog;

		protected JsonHandleruserlist(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// task_listview2.stopRefresh();
			progressDialog.dismiss();
			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
				mlist = response.getJSONArray("mlist");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lists = new Gson().fromJson(mlist.toString(),
					new TypeToken<List<OverTimeTask_Entity>>() {
					}.getType());
			mDatas.clear();
			//添加事件类型数据到Data
			for (int a = 0; a < lists.size(); a++) {
				mDatas.add(new FileBean(Integer.valueOf(lists.get(a).getId()),Integer.valueOf(lists.get(a).getPid()), lists.get(a).getName(),false));

			}
			Log.e("lyt", response.toString());
		}

	}

	private void mytasklist(int start,String typenameString) {
		String key = sp.getString(SPConstant.MY_TOKEN, "");
		AsyncHttpClientUtil Async = new AsyncHttpClientUtil();
		Dialog_Http_Util dialog_util = new Dialog_Http_Util();
		RequestParams param = new RequestParams();
		param.put("key", key);
		if(typenameString.equals("1")){
			param.put("relationType", 1);
			param.put("taskStatus", 0);
			param.put("belong", "yes");
		}else if(typenameString.equals("2")){
			param.put("relationType", 1);
			param.put("taskStatus", 1);
			param.put("intime", "yes");
			param.put("belong", "yes");
		}else if(typenameString.equals("3")){
			param.put("relationType", 1);
			param.put("taskStatus", 1);
			param.put("intime", "no");
			param.put("belong", "yes");
		}else if(typenameString.equals("4")){
			param.put("relationType",2);
		}

		param.put("start", start);
		Dialog progressDialog = dialog_util.showWaitingDialog(
				TaskManagement.this, "正在加载", false);
		Async.post(APIURL.MYTASKLIST, mContext, param,
				new JsonHandlermyTasklist(TaskManagement.this, progressDialog));

	}
		
	private class JsonHandlermyTasklist extends MJsonHttpHandler {
		Dialog progressDialog;

		protected JsonHandlermyTasklist(Context context, Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			// task_listview2.stopRefresh();
			progressDialog.dismiss();
			task_listview3.stopRefresh();
			task_listview3.stopLoadMore();

			JSONArray resultList = null;
			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
				resultList = response.getJSONArray("resultList");
				isboss = response.getInt("isboss");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (resultList != null) {
				for (int i = 0; i < resultList.length(); i++) {
					try {
						String task_target = resultList.getJSONObject(i)
								.getString("task_target");
						String creat_time = resultList.getJSONObject(i)
								.getString("creat_time");
						String task_name = resultList.getJSONObject(i)
								.getString("task_name");
						String task_id = resultList.getJSONObject(i).getString(
								"task_id");
						String task_status = resultList.getJSONObject(i)
								.getString("task_status");
						String task_type = resultList.getJSONObject(i)
								.getString("task_type");
						String creat_username = resultList.getJSONObject(i)
								.getString("creat_username");
						boolean oper = resultList.getJSONObject(i).isNull(
								"operator");
						String operator = "";
						String operatorname = "";
						if (!oper) {
							operator = resultList.getJSONObject(i).getString(
									"operator");
							JSONArray operatorarray = (JSONArray) resultList
									.getJSONObject(i).get("operator");
							for (int s = 0; s < operatorarray.length(); s++) {
								JSONObject operobj = operatorarray
										.getJSONObject(s);
								operatorname = operatorname
										+ (String) operobj.get("staff_name")
										+ "、";
							}
							operatorname = operatorname.substring(0,
									operatorname.length() - 1);
						} else {
							operator = "";
						}

						String task_notes = resultList.getJSONObject(i)
								.getString("task_notes");
						String source = resultList.getJSONObject(i).getString(
								"source");
						String end_time = resultList.getJSONObject(i)
								.getString("end_time");
						String start_time = resultList.getJSONObject(i)
								.getString("start_time");
						boolean know = resultList.getJSONObject(i).isNull(
								"knower");
						String knower = "";
						String Knowername = "";
						if (!know) {
							knower = resultList.getJSONObject(i).getString(
									"knower");
							JSONArray knowerarray = (JSONArray) resultList
									.getJSONObject(i).get("knower");
							for (int j = 0; j < knowerarray.length(); j++) {
								JSONObject Knobj = knowerarray.getJSONObject(j);
								Knowername = Knowername
										+ (String) Knobj.get("staff_name")
										+ "、";
							}
							if (Knowername.length() > 0) {
								Knowername = Knowername.substring(0,
										Knowername.length() - 1);
							}
						} else {
							knower = "";
						}
						String creat_user = resultList.getJSONObject(i)
								.getString("creat_user");
						mytasklist.add(new MyTaskListEntity(task_target,
								creat_time, task_name, task_id, task_status,
								task_type, creat_username, operator,
								task_notes, source, end_time, start_time,
								knower, creat_user, operatorname, Knowername));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				taskadapter.setList(mytasklist);
				taskadapter.notifyDataSetChanged();
			}
		}
	}

//	private void AsyncHttp(String string, RequestParams params) {
//		AsyncHttpclient_Util.post(string, mContext, params, new JsonHandler());
//	}

//	private class JsonHandler extends JsonHttpHandles {
//		@SuppressLint("NewApi")
//		@Override
//		public void onSuccess(int statusCode, Header[] headers,
//				JSONObject response) {
//			dhu.dismissWaitingDialog();
//			String msg = null;
//			try {
//				msg = response.getString("message");
//				if (msg.equals("success")) {
//					ToastUtil.showToast(mContext, "填写成功！");
//					task_name.setText("");
//					task_context.setText("");
//					reportId = "";
//					HTTP("");
//				} else {
//					ToastUtil.showToast(mContext, "填写失败！");
//
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//
//				e.printStackTrace();
//			}
//
//		}
//
//		@Override
//		public void onFailure(int statusCode, Header[] headers,
//				Throwable throwable, JSONObject errorResponse) {
//			// TODO Auto-generated method stub
//			dhu.dismissWaitingDialog();
//			ToastUtil.showToast(mContext, "网络连接失败！");
//			super.onFailure(statusCode, headers, throwable, errorResponse);
//		}
//
//		@Override
//		public void onFailure(int statusCode, Header[] headers,
//				String responseString, Throwable throwable) {
//			// TODO Auto-generated method stub
//			dhu.dismissWaitingDialog();
//			ToastUtil.showToast(mContext, "网络连接失败！");
//			super.onFailure(statusCode, headers, responseString, throwable);
//		}
//	}

	private void initview() {
//		taskview_one = findViewById(R.id.taskview_one);
		taskview_two = findViewById(R.id.taskview_two);
		taskview_three = findViewById(R.id.taskview_three);

//		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		text3 = (TextView) findViewById(R.id.text3);
//		text1.setOnClickListener(this);
		text2.setOnClickListener(this);
		text3.setOnClickListener(this);

	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1) {
			return;
		} else if (position == 0) {
//			text1.setTextColor(getResources().getColor(R.color._red));
			text2.setTextColor(getResources().getColor(R.color._red));
			text3.setTextColor(getResources().getColor(R.color.text_gray));
//			taskview_one.setBackgroundColor(getResources().getColor(
//					R.color._red));
			taskview_two.setBackgroundColor(getResources().getColor(
					R.color._red));
			taskview_three.setBackgroundColor(getResources().getColor(
					R.color.white));
			add_newtask_img.setVisibility(ViewGroup.GONE);
			headtitletext.setText("日志管理");
		} else if (position == 1) {
//			text1.setTextColor(getResources().getColor(R.color.text_gray));
			text2.setTextColor(getResources().getColor(R.color.text_gray));
			text3.setTextColor(getResources().getColor(R.color._red));
//			taskview_one.setBackgroundColor(getResources().getColor(
//					R.color.white));
			taskview_two.setBackgroundColor(getResources().getColor(
					R.color.white));
			taskview_three.setBackgroundColor(getResources().getColor(
					R.color._red));
			if(isboss==1){
			add_newtask_img.setVisibility(ViewGroup.VISIBLE);}
			headtitletext.setText("任务管理");
//		} else if (position == 2) {
//			text1.setTextColor(getResources().getColor(R.color.text_gray));
//			text2.setTextColor(getResources().getColor(R.color.text_gray));
//			text3.setTextColor(getResources().getColor(R.color._red));
//			taskview_one.setBackgroundColor(getResources().getColor(
//					R.color.white));
//			taskview_two.setBackgroundColor(getResources().getColor(
//					R.color.white));
//			taskview_three.setBackgroundColor(getResources().getColor(
//					R.color._red));
		}

	}

	private class JsonHandlerTaskDetails extends MJsonHttpHandler {
		Dialog progressDialog;
		String code;
		
		protected JsonHandlerTaskDetails(String code,Context context, Dialog progressDialog) {
			super(context, progressDialog);
			this.progressDialog = progressDialog;
			this.code=code;
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("NewApi")
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			Log.e("lyt", response.toString());
			task_listview2.stopRefresh();
			progressDialog.dismiss();

			JSONArray reportlist = null;// 日志
			String dayt = "";
			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
				reportlist = response.getJSONArray("reportlist");
				yearse = response.getString("yearse");
				month = response.getString("month");
				dayt = response.getString("dayt");
				int  days = (Integer) response.get("day");
				if(day<1){
					day=days;
				}
				tasklist.clear();
				for (int i = 0; i < reportlist.length(); i++) {
					JSONObject reportjsonobj = reportlist.getJSONObject(i);
					String checkS = "";
					if (reportjsonobj.isNull("checkS")) {
						checkS = "";
					} else {
						checkS = reportjsonobj.getString("checkS");
					}
					String stafforg = "";
					if (reportjsonobj.isNull("stafforg")) {
						stafforg = "";
					} else {
						if(code.equals("")){
						stafforg = reportjsonobj.getString("stafforg");
						}
					}
					String staffDayReport = "";
					if (reportjsonobj.isNull("staffDayReport")) {
						staffDayReport = "";
					} else {
						staffDayReport = reportjsonobj
								.getString("staffDayReport");
					}
					String weekd = reportjsonobj.getString("weekd");
					String work_date = reportjsonobj.getString("work_date");
					JSONObject infos = reportjsonobj.getJSONObject("infos");
					JSONArray list = reportjsonobj.getJSONArray("list");
					String ischeck="1";
					if(list.length()<1){
						ischeck="1";
					}else{
						for (int s = 0; s < list.length(); s++) {
							JSONObject check = list.getJSONObject(s);
							if(check.isNull("status")){
								break;
							}else{
								String status = check.getString("status");
								if(!status.equals("0")){
									ischeck="2";
								}
							}

						}
					}
					
					tasklist.add(new MyTaskEntity(checkS, weekd, list
							.toString(), work_date, infos.toString(), stafforg,
							staffDayReport, ischeck));
				}
				adapter.setList(tasklist);
				adapter.notifyDataSetChanged();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(code.equals("")){
			task_name2.setText(sp.getString(SPConstant.MY_NAME, "") + yearse
					+ "年" + month + "月工作日志");
			}
			if (month.length() < 2) {
				month = "0" + month;
			}
			if (dayt.length() < 2) {
				dayt = "0" + dayt;
			}
//			task_write_time.setText(yearse + "-" + month + "-" + dayt);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 1:
			Bundle bundle = data.getExtras();
			if (null != bundle) {
				vp.setCurrentItem(0);
//				reportId = bundle.getString("id");
//				task_write_time.setText(bundle.getString("time"));
//				task_name.setText(bundle.getString("name"));
//				task_context.setText(bundle.getString("content"));
				if (bundle.getBoolean("refresh", false)) {
					HTTP("","");
				}
			}
			break;
		case 11://完成任务
			Bundle bundle11 = data.getExtras();
			if (null != bundle11) {
				if (bundle11.getBoolean("bool", false)) {
					mytasklist.clear();
					load = 1;
					mytasklist(0,typenameString);
				}
			}
			break;
		case 12://写日志
			Bundle bundle2 = data.getExtras();
			if (null != bundle2) {
				if (bundle2.getBoolean("bool", false)) {
					HTTP("","");
				}
			}
			
			break;
		case 13://新增任务
			Bundle bundle1 = data.getExtras();
			if (null != bundle1) {
				if (bundle1.getBoolean("bool", false)) {
					mytasklist.clear();
					load = 1;
					mytasklist(0,typenameString);
				}
			}

			
			break;
		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
