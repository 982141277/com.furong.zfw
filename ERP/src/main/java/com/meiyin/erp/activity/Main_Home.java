package com.meiyin.erp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.meiyin.erp.R;


//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.app.Dialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
//import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
//import com.loopj.android.http.RequestParams;
//import com.meiyin.erp.GreenDao.MeiyinnewsDao;
//import com.meiyin.erp.GreenDao.Topic_EntityDao;
//import com.meiyin.erp.adapter.MyPage_Grid_Adapter;
//import com.meiyin.erp.adapter.Setting_Adapter;
//import com.meiyin.erp.adapter.SimpleTreeAdapter;
//import com.meiyin.erp.adapter.TopicList_Adapter;
//import com.meiyin.erp.app.FileBean;
//import com.meiyin.erp.application.APIURL;
//import com.meiyin.erp.application.MyApplication;
//import com.meiyin.erp.application.SPConstant;
//import com.meiyin.erp.bean.Node;
//import com.meiyin.erp.bean.TreeListViewAdapter;
//import com.meiyin.erp.entity.Get_Logins;
//import com.meiyin.erp.entity.Meiyinnews;
//import com.meiyin.erp.entity.MyPage_Entity;
//import com.meiyin.erp.entity.OverTimeTask_Entity;
//import com.meiyin.erp.entity.Setting_Entity;
//import com.meiyin.erp.entity.Topic_Entity;
//import com.meiyin.erp.fragment.LeftFragment;
//import com.meiyin.erp.fragment.Personality;
//import com.meiyin.erp.service.Meiyinservice;
//import com.meiyin.erp.service.Watchservice;
//import com.meiyin.erp.ui.CircleImageView;
//import com.meiyin.erp.ui.Image;
//import com.meiyin.erp.ui.XListView;
//import com.meiyin.erp.util.AndroidUtil;
//import com.meiyin.erp.util.Dialog_Http_Util;
//import com.meiyin.erp.util.LogUtil;
//import com.meiyin.erp.util.UpdateManager;
//import com.my.android.library.AsyncHttpClientUtil;
//import com.my.android.library.MJsonHttpHandler;
//import com.my.android.library.MyViewPagerAdapter;
//import com.my.android.library.ToastManager;
//
//import org.apache.http.Header;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//
///**
// * ERP首页
// */
//
public class Main_Home extends Activity {
//        implements
//		OnClickListener
//
//	private ServiceReceiver Receiver;
//	private Context context;
//	private Fragment mContent;
//	private AlertDialog dialog;
//	private ImageView imageView_setting;
//	private IntentFilter Filters;
//	private MeiyinnewsDao meiyin;
//	private RelativeLayout pluslistview_relative;
//	private View quanview;
//
//	private CircleImageView head_photo;
//	private SharedPreferences sp;
//	private List<Meiyinnews> meiyinlist;
//	private ViewPager vp;
//	// private ListView mlistview;
//	// private TextView mtextView;
//	// private Xxls_Adapter madapter;
//	// private LinearLayout message_linearout;
//	// private TextView message_cont;
	private View homes;
//	private ArrayList<MyPage_Entity> pagelist;
//	private List<Get_Logins> getlist;
//	private MyPage_Grid_Adapter PageAdapter;
//
//	private List<FileBean> mDatas = new ArrayList<FileBean>();;
//	private SimpleTreeAdapter<FileBean> mAdapter;;
//	private ListView myaddress_item_list;
//	private Builder builder;
//	private ArrayList<OverTimeTask_Entity> mlist;
//	private TopicList_Adapter topicAdapter;
//
//	private ArrayList<Topic_Entity> topiclist;
//	private Handler mHandler;
//	private XListView topic_Listview;
//	private ImageView reload_img;
//	private TextView topic_cont;
//	private LinearLayout topic_linearout;
//	public Topic_EntityDao topicDao;
//	private boolean it=false;
//	private TextView mysection;
//
//	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		homes = getLayoutInflater().inflate(R.layout.homes, null);
		setContentView(homes);
//		// CoverManager.getInstance().init(this);
//		Image.initImageLoader(this);
//		context = getApplicationContext();
//		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
//				Context.MODE_MULTI_PROCESS);
//		new UpdateManager(Main_Home.this).checkUpdateInfo(true);
//		dialog = new AlertDialog.Builder(this).create();
//		findview();
//		registerReceiver();
//		startservice();
//		initSlidingMenu(savedInstanceState);
//		// CoverManager.getInstance().setMaxDragDistance(150);
//		// CoverManager.getInstance().setExplosionTime(150);
//
	}
//
//	/*
//	 * 开启服务
//	 */
//	private void startservice() {
//		boolean isrun = AndroidUtil.isServiceRunning(context,
//				"com.meiyin.services.Meiyinservice");
//		if (isrun) {
//			LogUtil.e("lyt", "Meiyinservice正在运行中...");
//		} else {
//
//			startService(new Intent(this, Meiyinservice.class).putExtra("name",
//					sp.getString(SPConstant.MY_NAME, "")).putExtra("token",
//					sp.getString(SPConstant.MY_TOKEN, "")));
//			LogUtil.e("lyt", "Meiyinservice开启成功...");
//		}
//		boolean isruns = AndroidUtil.isServiceRunning(context,
//				"com.meiyin.services.Watchservice");
//		if (isruns) {
//			LogUtil.e("lyt", "Watchservice正在运行中...");
//		} else {
//			startService(new Intent(this, Watchservice.class));
//			LogUtil.e("lyt", "Watchservice开启成功...");
//		}
//	}
//
//	@Override
//	protected void onDestroy() {
//		unregisterReceiver(Receiver);
//		super.onDestroy();
//	}
//
//	public void findview() {
//		TextView myname = (TextView) homes.findViewById(R.id.myname);
//		mysection = (TextView) homes.findViewById(R.id.mysection);
//		reload_img = (ImageView) homes.findViewById(R.id.reload_img);
//		myname.setText(getIntent().getStringExtra("name"));
//		meiyin = MyApplication.getInstance().getDaoSession().getMeiyinnewsDao();
//		topicDao =  MyApplication.getInstance().getDaoSession().getTopic_EntityDao();
//		// message_linearout = (LinearLayout) homes
//		// .findViewById(R.id.message_linearout);
//		// message_cont = (TextView) homes.findViewById(R.id.message_cont);
//		// Long size = meiyin.querysize(sp.getString(SPConstant.USERNAME, ""));
//		// if (size > 0) {
//		// message_cont.setText(String.valueOf(size));
//		// } else {
//		// message_linearout.setVisibility(ViewGroup.GONE);
//		// }
//		topic_linearout = (LinearLayout) homes
//				.findViewById(R.id.topic_linearout);
//		topic_cont = (TextView) homes.findViewById(R.id.topic_cont);
//
//		ArrayList<View> view = new ArrayList<View>();
//		View item1 = getLayoutInflater().inflate(R.layout.my_page_item, null);
//		// View item2 = getLayoutInflater()
//		// .inflate(R.layout.my_message_item, null);
//		View item3 = getLayoutInflater()
//				.inflate(R.layout.my_address_item, null);
//		View item4 = getLayoutInflater().inflate(R.layout.my_topic_item, null);
//		View item5 = getLayoutInflater().inflate(R.layout.my_item, null);
//		view.add(item1);
//		// view.add(item2);
//		view.add(item3);
//		view.add(item4);
//		view.add(item5);
//		item1(item1);
//		// item2(item2);
//		item3(item3);
//		item4(item4);
//		item5(item5);
//		vp = (ViewPager) findViewById(R.id.home_viewpager);
//		MyViewPagerAdapter vpAdapter = new MyViewPagerAdapter(view);
//		vp.setAdapter(vpAdapter);
//
//		head_photo = (CircleImageView) homes.findViewById(R.id.homes_photo);
//		String s = sp.getString("imguri", "");
//		if (!s.equals("")) {
////			Image.ImageLoader(s.toString(), head_photo);
//			head_photo.setImageDrawable(getResources().getDrawable(R.drawable.loga));
//		}
//		head_photo.setImageDrawable(getResources().getDrawable(R.drawable.loga));
//		head_photo.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				// toggle();
//			}
//		});
//		imageView_setting = (ImageView) findViewById(R.id.imageView_setting);
//		imageView_setting.setOnClickListener(this);
//
//		pluslistview_relative = (RelativeLayout) findViewById(R.id.pluslistview_relative);
//		quanview = findViewById(R.id.quanview);
//		quanview.setOnClickListener(this);
//		/*
//		 * 首页
//		 */
//		LinearLayout page_linear = (LinearLayout) findViewById(R.id.page_linear);
//		final ImageView page_image = (ImageView) findViewById(R.id.page_image);
//		final TextView page_text = (TextView) findViewById(R.id.page_text);
//		// /*
//		// * 消息
//		// */
//		// RelativeLayout message_relative = (RelativeLayout)
//		// findViewById(R.id.message_relative);
//		// final ImageView message_image = (ImageView)
//		// findViewById(R.id.message_image);
//		// final TextView message_text = (TextView)
//		// findViewById(R.id.message_text);
//		/*
//		 * 通讯录
//		 */
//		LinearLayout address_linear = (LinearLayout) findViewById(R.id.address_linear);
//		final ImageView address_image = (ImageView) findViewById(R.id.address_image);
//		final TextView address_text = (TextView) findViewById(R.id.address_text);
//		/*
//		 * 公告
//		 */
//		RelativeLayout topic_relative = (RelativeLayout) findViewById(R.id.topic_relative);
//		final ImageView topic_image = (ImageView) findViewById(R.id.topic_image);
//		final TextView topic_text = (TextView) findViewById(R.id.topic_text);
//		/*
//		 * 我的
//		 */
//		LinearLayout my_linear = (LinearLayout) findViewById(R.id.my_linear);
//		final ImageView my_image = (ImageView) findViewById(R.id.my_image);
//		final TextView my_text = (TextView) findViewById(R.id.my_text);
//		// 首页
//		page_linear.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				setCurrent(0, page_image, page_text, address_image,
//						address_text, topic_image, topic_text, my_image,
//						my_text);
//			}
//		});
//		// // 消息
//		// message_relative.setOnClickListener(new OnClickListener() {
//		//
//		// @Override
//		// public void onClick(View arg0) {
//		// // TODO Auto-generated method stub
//		// setCurrent(1, page_image, page_text, message_image,
//		// message_text, address_image, address_text, topic_image,
//		// topic_text, my_image, my_text);
//		// }
//		//
//		// });
//		// 通讯录
//		address_linear.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				setCurrent(1, page_image, page_text, address_image,
//						address_text, topic_image, topic_text, my_image,
//						my_text);
//			}
//
//		});
//		// 公告
//		topic_relative.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				setCurrent(2, page_image, page_text, address_image,
//						address_text, topic_image, topic_text, my_image,
//						my_text);
//			}
//
//		});
//		// 我的
//		my_linear.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				setCurrent(3, page_image, page_text, address_image,
//						address_text, topic_image, topic_text, my_image,
//						my_text);
//			}
//		});
//		vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//			@Override
//			public void onPageSelected(int arg0) {
//				// TODO Auto-generated method stub
//				setCurrent(arg0, page_image, page_text, address_image,
//						address_text, topic_image, topic_text, my_image,
//						my_text);
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//
//	}
//
//	private void item1(View item1) {
//		GridView my_gridview = (GridView) item1.findViewById(R.id.my_gridview);
//		PageAdapter = new MyPage_Grid_Adapter(context);
//		pagelist = new ArrayList<MyPage_Entity>();
//		Long lons = meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Type.eq("办公管理系统"), MeiyinnewsDao.Properties.Biaoshi.eq("0")).count();
//		pagelist.add(new MyPage_Entity("待审批事项", String.valueOf(lons), true,getResources().getDrawable(R.drawable.mshenpi)));
//		pagelist.add(new MyPage_Entity("日志管理", "0", true,getResources().getDrawable(R.drawable.mrizhi)));
//		pagelist.add(new MyPage_Entity("外勤管理", "0", true,getResources().getDrawable(R.drawable.mwaiqin)));
//		pagelist.add(new MyPage_Entity("新增申请单", "0", true,getResources().getDrawable(R.drawable.newoutlogos)));
//		pagelist.add(new MyPage_Entity("所有申请单", "0", true,getResources().getDrawable(R.drawable.alloutlogos)));
//
//		getlist = new Gson().fromJson(getIntent().getStringExtra("login"),
//				new TypeToken<List<Get_Logins>>() {
//				}.getType());
//		for (Get_Logins item : getlist) {
//			if (item.getSystem_code().equals("92165969")) {
//				Long lonss = meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Type.eq("IT运维管理"), MeiyinnewsDao.Properties.Biaoshi.eq("0")).count();
//				pagelist.add(new MyPage_Entity("待处理事件", String.valueOf(lonss),
//						true,getResources().getDrawable(R.drawable.mshijian)));
//				pagelist.add(new MyPage_Entity("事件处理", "0", true,getResources().getDrawable(R.drawable.allitlogos)));
//				pagelist.add(new MyPage_Entity("巡检表", "0", true,getResources().getDrawable(R.drawable.xjbs)));
//
//				it = true;
//			}
//		}
//		if(getIntent().getStringExtra("gooutbrush").equals("1")||getIntent().getStringExtra("gooutbrush").equals("2")){
//		pagelist.add(new MyPage_Entity("项目考勤", "0", true,getResources().getDrawable(R.drawable.xjb)));}
//
//		if (pagelist.size() % 2 == 1) {
//			pagelist.add(new MyPage_Entity("", "0", true,null));
//		}
//		PageAdapter.setList(pagelist);
//		my_gridview.setAdapter(PageAdapter);
//		my_gridview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				if (pagelist.size() > 0) {
//					String xName = pagelist.get(arg2).getxName();
//					if (xName.equals("待审批事项")) {
//						Intent intent = new Intent();
//						intent.putExtra("name", "待审批事项");
//						intent.setClass(context, Memulist.class);
//						startActivity(intent);
//					} else if (xName.equals("待处理事件")) {
//							List<Meiyinnews> si = meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(
//									SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Type.eq("IT运维管理"),MeiyinnewsDao.Properties.Biaoshi.eq("0")).list();
//							if (si.size() > 0) {
//								for (int i = 0; i < si.size(); i++) {
//										si.get(i).setBiaoshi("1");
//										meiyin.update(si.get(i));
//										Intent intent = new Intent();
//										intent.setAction("meiyin");
//										intent.putExtra("sys", false);
//										Main_Home.this.sendBroadcast(intent);
//									}
//								}
//						Intent intent = new Intent();
//						intent.setClass(context, IT_Management_Activity.class);
//						startActivity(intent);
//					} else if (xName.equals("日志管理")) {
//						Intent intent = new Intent();
//						intent.setClass(context, TaskManagement.class);
//						startActivity(intent);
//					} else if (xName.equals("外勤管理")) {
//						startActivity(new Intent().setClass(context,
//								OutWorkActivity.class));
//					} else if (xName.equals("新增申请单")) {
//						new Dialog_Http_Util().dialogcd(context, sp, Main_Home.this);
//					} else if (xName.equals("所有申请单")) {
//						Intent intent = new Intent();
//						intent.putExtra("name", "申请表单");
//						intent.setClass(context, Memulist.class);
//						startActivity(intent);
//					} else if (xName.equals("事件处理")) {
//						startActivity(new Intent().setClass(context, IT_Management_Event_List.class));
//					} else if (xName.equals("巡检表")) {
//						startActivity(new Intent().setClass(context, XjbActivity.class));
//					} else if(xName.equals("项目考勤")){
//						startActivity(new Intent().setClass(context, GoOutBrushActivity.class));
//					}
//				}
//			}
//		});
////		LinearLayout mypage_addout_linear = (LinearLayout) item1
////				.findViewById(R.id.mypage_addout_linear);
////		mypage_addout_linear.setOnClickListener(new OnClickListener() {
////
////			@Override
////			public void onClick(View arg0) {
////				// TODO Auto-generated method stub
////				new Dialog_Http_Util().dialogcd(context, sp, Main_Home.this);
////			}
////		});
////		LinearLayout mypage_allout_linear = (LinearLayout) item1
////				.findViewById(R.id.mypage_allout_linear);
////		mypage_allout_linear.setOnClickListener(new OnClickListener() {
////
////			@Override
////			public void onClick(View arg0) {
////				// TODO Auto-generated method stub
////				Intent intent = new Intent();
////				intent.putExtra("name", "申请表单");
////				intent.setClass(context, Memulist.class);
////				startActivity(intent);
////			}
////		});
////		LinearLayout mypage_allit_linear = (LinearLayout) item1
////				.findViewById(R.id.mypage_allit_linear);
////		if(it){
////			mypage_allit_linear.setVisibility(ViewGroup.VISIBLE);
////		}else{
////			mypage_allit_linear.setVisibility(ViewGroup.GONE);
////		}
////		mypage_allit_linear.setOnClickListener(new OnClickListener() {
////
////			@Override
////			public void onClick(View arg0) {
////				// TODO Auto-generated method stub
////				startActivity(new Intent().setClass(context, IT_Management_Event_List.class));
////			}
////		});
//
//		// TextView add_shortcut_text = (TextView)
//		// item1.findViewById(R.id.add_shortcut_text);
//		// add_shortcut_text.setOnClickListener(new OnClickListener() {
//		//
//		// @Override
//		// public void onClick(View arg0) {
//		// // TODO Auto-generated method stub
//		// Intent intent = new Intent();
//		// intent.setClass(context, AddShortCutActivity.class);
//		// startActivity(intent);
//		// }
//		// });
//
//		Dialog_Http_Util dialog_util = new Dialog_Http_Util();
//		AsyncHttpClientUtil mAsync = new AsyncHttpClientUtil();
//		String key = sp.getString(SPConstant.MY_TOKEN, "");
//		RequestParams param = new RequestParams();
//		param.setContentEncoding("utf-8");
//		param.put("key", key);
//		Dialog progressDialog = dialog_util.showWaitingDialog(
//				Main_Home.this, "正在刷新", true);
//		mAsync.post(APIURL.CHECK.MEMUAPI_COUNT, context, param, new JsonHandlersize(
//				Main_Home.this, progressDialog));
//	}
//
//
////
//private class JsonHandlersize extends MJsonHttpHandler {
//	private Dialog progressDialog;
//
//	protected JsonHandlersize(Context context, Dialog progressDialog) {
//		super(context, progressDialog);
//		this.progressDialog = progressDialog;
//		// TODO Auto-generated constructor stub
//	}
//
//	@SuppressLint("NewApi")
//	@Override
//	public void onSuccess(int statusCode, Header[] headers,
//			JSONObject response) {
//		progressDialog.dismiss();
//		if (!response.isNull("errorMsg")) {
//			try {
//				ToastManager.getInstance(context).showToast(
//						response.getString("errorMsg"));
//				stopService(new Intent()
//						.setAction("com.meiyin.services.Meiyinservice"));
//				startActivity(new Intent().setClass(context, Login.class)
//						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//				System.exit(0);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return;
//		}
//
//		try {
//			int result = (Integer) response.get("result");
//			Log.e("lyt", "待审批数目："+result);
//			showmemu(result);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//}
//	/*
//	 * 待审批列表中是否包括此消息
//	 */
//	@SuppressLint("InlinedApi")
//	public void showmemu(int list) {
//		MeiyinnewsDao meiyin = MyApplication.getInstance().getDaoSession().getMeiyinnewsDao();
//		SharedPreferences sp = getSharedPreferences(
//				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
//		List<Meiyinnews> si = meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(
//				SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Type.eq("办公管理系统"), MeiyinnewsDao.Properties.Biaoshi.eq("0")).list();
//		if (si.size() > 0) {
//				if(list>si.size()){
//					for (MyPage_Entity iterable_element : pagelist) {
//						if (iterable_element.getxName().equals("待审批事项")) {
//							iterable_element
//									.setxMessage(String.valueOf(si.size()));
//						} else if (iterable_element.getxName().equals("待处理事件")) {
//							Long lonss = meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(
//									SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Type.eq("IT运维管理"), MeiyinnewsDao.Properties.Biaoshi.eq("0")).count();
//							iterable_element.setxMessage(String.valueOf(lonss));
//						}
//					}
//					PageAdapter.setList(pagelist);
//					PageAdapter.notifyDataSetChanged();
//				}else if(list<=si.size()){
//					for (MyPage_Entity iterable_element : pagelist) {
//						if (iterable_element.getxName().equals("待审批事项")) {
//							iterable_element
//									.setxMessage(String.valueOf(list));
//						} else if (iterable_element.getxName().equals("待处理事件")) {
//							Long lonss = meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(
//									SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Type.eq("IT运维管理"), MeiyinnewsDao.Properties.Biaoshi.eq("0")).count();
//							iterable_element.setxMessage(String.valueOf(lonss));
//						}
//					}
//					PageAdapter.setList(pagelist);
//					PageAdapter.notifyDataSetChanged();
//				}
//		}
//	}
//
//	// private void item2(View item2) {
//	// mlistview = (ListView) item2.findViewById(R.id.listviewlishi);
//	// mtextView = (TextView) item2.findViewById(R.id.tixing);
//	// madapter = new Xxls_Adapter(context);
//	// setRefresh();
//	// }
//	//公司通讯录
//	private void item3(View item3) {
//		myaddress_item_list = (ListView) item3
//				.findViewById(R.id.myaddress_item_list);
//		// madapter = new Xxls_Adapter(context);
//		builder = new AlertDialog.Builder(this);// 初始化审批dialog
//		String communication = sp.getString(SPConstant.COMMUNICATION, "");
//		if (communication.equals("")) {
//			Dialog_Http_Util dialog_util = new Dialog_Http_Util();
//			AsyncHttpClientUtil mAsync = new AsyncHttpClientUtil();
//			/*
//			 * 初始化所有人员
//			 */
//			String key = sp.getString(SPConstant.MY_TOKEN, "");
//			RequestParams param = new RequestParams();
//			param.setContentEncoding("utf-8");
//			param.put("key", key);
//			Dialog progressDialog = dialog_util.showWaitingDialog(
//					Main_Home.this, "正在刷新", true);
//			mAsync.post(APIURL.COMPEOPLE, context, param, new JsonHandler(
//					Main_Home.this, progressDialog));
//		} else {
//			frist(communication);
//		}
//		reload_img.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Dialog_Http_Util dialog_util = new Dialog_Http_Util();
//				AsyncHttpClientUtil mAsync = new AsyncHttpClientUtil();
//				String key = sp.getString(SPConstant.MY_TOKEN, "");
//				RequestParams param = new RequestParams();
//				param.setContentEncoding("utf-8");
//				param.put("key", key);
//				Dialog progressDialog = dialog_util.showWaitingDialog(
//						Main_Home.this, "正在刷新", true);
//				mAsync.post(APIURL.COMPEOPLE, context, param, new JsonHandler(
//						Main_Home.this, progressDialog));
//			}
//		});
//	}
//
//	// 人员信息
//	private class JsonHandler extends MJsonHttpHandler {
//
//		private Dialog progressDialog;
//
//		protected JsonHandler(Context context, Dialog progressDialog) {
//			super(context, progressDialog);
//			this.progressDialog = progressDialog;
//			// TODO Auto-generated constructor stub
//		}
//
//		@SuppressLint("NewApi")
//		@Override
//		public void onSuccess(int statusCode, Header[] headers,
//				JSONObject response) {
//			Log.e("lyt", response.toString());
//			progressDialog.dismiss();
//			if (!response.isNull("errorMsg")) {
//				try {
//					ToastManager.getInstance(context).showToast(
//							response.getString("errorMsg"));
//					stopService(new Intent()
//							.setAction("com.meiyin.services.Meiyinservice"));
//					startActivity(new Intent().setClass(context, Login.class)
//							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//					System.exit(0);
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
//			try {
//				JSONArray mli = response.getJSONArray("mlist");
//				sp.edit().putString(SPConstant.COMMUNICATION, mli.toString())
//						.commit();
//				frist(mli.toString());
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	}
//	//公告页
//	private void item4(View item4) {
//		topiclist = new ArrayList<Topic_Entity>();
//		topic_Listview = (XListView) item4.findViewById(R.id.topic_Listview);
//		mHandler = new Handler();
//		topic_Listview.setPullLoadEnable(true);
//		topic_Listview.setXListViewListener(new XListView.IXListViewListener() {
//
//			@Override
//			public void onRefresh() {
//				// TODO Auto-generated method stub
//				mHandler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						Dialog progressDialog = new Dialog_Http_Util()
//								.showWaitingDialog(Main_Home.this, "正在刷新",
//										false);
//						RequestParams param = new RequestParams();
//						param.setContentEncoding("utf-8");
//						param.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
//						param.put("pager", 0);// pager
//						new AsyncHttpClientUtil().post(APIURL.CHECK.TOPICMEMU,
//								context, param, new JsonHandlerInit(
//										Main_Home.this, progressDialog));
//					}
//				}, 10);
//			}
//
//			@Override
//			public void onLoadMore() {
//				// TODO Auto-generated method stub
//				mHandler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						int load;
//						if(topiclist.size()<20){
//							load=0;
//						}else{
//							load=topiclist.size();
//						}
//						Dialog progressDialog = new Dialog_Http_Util()
//								.showWaitingDialog(Main_Home.this, "正在加载..",
//										false);
//						RequestParams param = new RequestParams();
//						param.setContentEncoding("utf-8");
//						param.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
//						param.put("pager",load);// pager
//						new AsyncHttpClientUtil().post(APIURL.CHECK.TOPICMEMU,
//								context, param, new JsonHandlerload(
//										Main_Home.this, progressDialog));
//					}
//				}, 10);
//			}
//		});
//		topic_Listview.setRefreshTime(sp.getString(SPConstant.REFRESHTIME, ""));
//		topic_Listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				if (arg2 > 0) {
//					if (topiclist.size() > 0) {
//						topiclist.get(arg2 - 1).setReadflag(true);
//						topicDao.insert(topiclist.get(arg2 - 1));
//						topicAdapter.notifyDataSetChanged();
//						xxTopic();
//						startActivity(new Intent().setClass(Main_Home.this,
//								TopicDetailsActivity.class).putExtra(
//								"topic_code",
//								String.valueOf(topiclist.get(arg2 - 1).getTopic_code())));
//					}
//				}
//			}
//		});
//		topicAdapter = new TopicList_Adapter(context);
//		topic_Listview.setAdapter(topicAdapter);
//		List<Topic_Entity> xtopic= topicDao.queryBuilder().orderAsc(Topic_EntityDao.Properties.Istop).orderDesc(Topic_EntityDao.Properties.Create_time).list();
//		if (xtopic.size()<1) {
//			Dialog progressDialog = new Dialog_Http_Util().showWaitingDialog(
//					Main_Home.this, "正在刷新", false);
//			RequestParams param = new RequestParams();
//			param.setContentEncoding("utf-8");
//			param.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
//			param.put("pager", 0);// pager
//			new AsyncHttpClientUtil().post(APIURL.CHECK.TOPICMEMU, context,
//					param, new JsonHandlerInit(Main_Home.this, progressDialog));
//		} else {
//			topiclist.clear();
//			topiclist=(ArrayList<Topic_Entity>) xtopic;
//
//			topicAdapter.setList(topiclist);
//			topicAdapter.notifyDataSetChanged();
//			xxTopic();
//			Dialog progressDialog = new Dialog_Http_Util().showWaitingDialog(
//					Main_Home.this, "正在刷新", false);
//			RequestParams param = new RequestParams();
//			param.setContentEncoding("utf-8");
//			param.put("key", sp.getString(SPConstant.MY_TOKEN, ""));
//			param.put("pager", 0);// pager
//			new AsyncHttpClientUtil().post(APIURL.CHECK.TOPICMEMU, context,
//					param, new JsonHandlerInit(Main_Home.this, progressDialog));
//		}
//
//	}
//
//	public void xxTopic() {
//		Long sz = topicDao.queryBuilder().where(
//				Topic_EntityDao.Properties.Readflag.eq("false")).count();
//		if (sz > 0) {
//			topic_linearout.setVisibility(ViewGroup.VISIBLE);
//			topic_cont.setText(""+sz);
//		} else {
//			topic_linearout.setVisibility(ViewGroup.GONE);
//		}
//	}
//
//	// 公告list初始化
//	private class JsonHandlerInit extends MJsonHttpHandler {
//		private Dialog progressDialog;
//
//		protected JsonHandlerInit(Context context, Dialog progressDialog) {
//			super(context, progressDialog);
//			this.progressDialog = progressDialog;
//			// TODO Auto-generated constructor stub
//		}
//
//		@SuppressLint("NewApi")
//		@Override
//		public void onSuccess(int statusCode, Header[] headers,
//				JSONObject response) {
//			progressDialog.dismiss();
//			if (!response.isNull("errorMsg")) {
//				try {
//					ToastManager.getInstance(context).showToast(
//							response.getString("errorMsg"));
//					stopService(new Intent()
//							.setAction("com.meiyin.services.Meiyinservice"));
//					startActivity(new Intent().setClass(context, Login.class)
//							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//					System.exit(0);
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
////
//			try {
//				if(response.get("message").equals("error")){
//				ToastManager.getInstance(context).showToast("公告数据出错！！！");
//				return;
//				}
//			} catch (JSONException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			JSONArray data = null;
//			try {
//				data = response.getJSONArray("data");
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			ArrayList<Topic_Entity> topic_memu = new Gson().fromJson(
//					data.toString(), new TypeToken<List<Topic_Entity>>() {
//					}.getType());// 公告list
//			topiclist.clear();
//			for (Topic_Entity topic_item : topic_memu) {
//				topicDao.insert(topic_item);
//			}
//			topiclist=(ArrayList<Topic_Entity>) topicDao.queryBuilder().orderAsc(Topic_EntityDao.Properties.Istop).orderDesc(Topic_EntityDao.Properties.Create_time).list();
//
//			topicAdapter.setList(topiclist);
//			topicAdapter.notifyDataSetChanged();
//			topic_Listview.stopRefresh();
//			xxTopic();
//		}
//
//	}
//
//	// 公告list_load
//	private class JsonHandlerload extends MJsonHttpHandler {
//		private Dialog progressDialog;
//
//		protected JsonHandlerload(Context context, Dialog progressDialog) {
//			super(context, progressDialog);
//			this.progressDialog = progressDialog;
//			// TODO Auto-generated constructor stub
//		}
//
//		@SuppressLint("NewApi")
//		@Override
//		public void onSuccess(int statusCode, Header[] headers,
//				JSONObject response) {
//			Log.e("lyt", response.toString());
//			progressDialog.dismiss();
//			if (!response.isNull("errorMsg")) {
//				try {
//					ToastManager.getInstance(context).showToast(
//							response.getString("errorMsg"));
//					stopService(new Intent()
//							.setAction("com.meiyin.services.Meiyinservice"));
//					startActivity(new Intent().setClass(context, Login.class)
//							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//					System.exit(0);
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
//			JSONArray data = null;
//			try {
//				data = response.getJSONArray("data");
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			ArrayList<Topic_Entity> topic_memu = new Gson().fromJson(
//					data.toString(), new TypeToken<List<Topic_Entity>>() {
//					}.getType());// 公告list
//			for (Topic_Entity topic_item : topic_memu) {
//				topicDao.insert(topic_item);
//
//			}
//			topiclist=(ArrayList<Topic_Entity>) topicDao.queryBuilder().orderAsc(Topic_EntityDao.Properties.Istop).orderDesc(Topic_EntityDao.Properties.Create_time).list();
//
//			topicAdapter.setList(topiclist);
//			topicAdapter.notifyDataSetChanged();
//			topic_Listview.stopLoadMore();
//			xxTopic();
//		}
//
//	}
//
//	private void item5(View item5) {
//		ListView myset_listview = (ListView) item5
//				.findViewById(R.id.myset_listview);
//		final ArrayList<Setting_Entity> setlist = new ArrayList<Setting_Entity>();
//		setlist.add(new Setting_Entity(getResources().getDrawable(
//				R.drawable.gengxin1), "版本更新"));
//		// setlist.add(new
//		// Setting_Entity(getResources().getDrawable(R.drawable.xiaoxitu),
//		// "系统通知"));
//		// setlist.add(new Setting_Entity(getResources().getDrawable(
//		// R.drawable.phonepic), "公司通讯"));
//		// setlist.add(new Setting_Entity(getResources().getDrawable(
//		// R.drawable.personality), "个性装扮"));
//		setlist.add(new Setting_Entity(getResources().getDrawable(
//				R.drawable.guanyu1), "关于美音"));
//		setlist.add(new Setting_Entity(getResources().getDrawable(
//				R.drawable.yonghu1), "退出帐号"));
//		Setting_Adapter adapters = new Setting_Adapter(context);
//		adapters.setList(setlist);
//		myset_listview.setAdapter(adapters);
//		myset_listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				if (setlist.get(arg2).getName().equals("版本更新")) {
//					new UpdateManager(Main_Home.this).checkUpdateInfo(false);
//				} else if (setlist.get(arg2).getName().equals("个性装扮")) {
//					Intent intents = new Intent();
//					intents.setClass(Main_Home.this, Personality.class);
//					Main_Home.this.startActivity(intents);
//				} else if (setlist.get(arg2).getName().equals("系统通知")) {
//					ToastManager.getInstance(context).showToast("暂无系统通知！");
//				} else if (setlist.get(arg2).getName().equals("公司通讯")) {
//					Intent intents = new Intent();
//					intents.setClass(Main_Home.this,
//							CommunicationActivity.class);
//					Main_Home.this.startActivity(intents);
//					pluslistview_relative.setVisibility(ViewGroup.GONE);
//				} else if (setlist.get(arg2).getName().equals("关于美音")) {
//					Intent intents = new Intent();
//					intents.setClass(Main_Home.this, About_Activity.class);
//					Main_Home.this.startActivity(intents);
//				} else if (setlist.get(arg2).getName().equals("退出帐号")) {
//					LinearLayout submit_dialog = (LinearLayout) LayoutInflater
//							.from(context)
//							.inflate(R.layout.submit_dialog, null);
//					TextView heads = (TextView) submit_dialog
//							.findViewById(R.id.heads);
//					Button button_t = (Button) submit_dialog
//							.findViewById(R.id.button_t);
//					Button button_f = (Button) submit_dialog
//							.findViewById(R.id.button_f);
//					heads.setText("确定要退出当前帐号吗？             ");
//					dialog.show();
//					dialog.getWindow().setContentView(submit_dialog);
//					button_t.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View arg0) {
//							// TODO Auto-generated method stub
//							dialog.dismiss();
//							sp.edit().remove(SPConstant.MY_NAME).commit();
//							sp.edit().remove(SPConstant.MY_LOGIN_HOST).commit();
//							sp.edit().remove(SPConstant.MY_LOGINID).commit();
//							sp.edit().remove(SPConstant.MY_LOGININFO).commit();
//							sp.edit().remove(SPConstant.MY_TOKEN).commit();
//							sp.edit().remove(SPConstant.USERNAME).commit();
//							sp.edit().remove(SPConstant.MY_GOOUTBRUSH).commit();
//							sp.edit().remove(SPConstant.MY_GOOUTBRUSHLIST).commit();
//							// meiyin.deleteAll();删除所有消息记录
//							topicDao.deleteAll();
//							stopService(new Intent()
//									.setAction("com.meiyin.services.Meiyinservice"));
//							Intent intent1 = new Intent();
//							intent1.setClass(Main_Home.this, Login.class);
//							intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//							startActivity(intent1);
//							System.exit(0);
//						}
//					});
//					button_f.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View arg0) {
//							// TODO Auto-generated method stub
//							dialog.dismiss();
//						}
//					});
//				}
//
//			}
//		});
//	}
//
//	private void frist(String MIL) {
//		mlist = new Gson().fromJson(MIL,
//				new TypeToken<List<OverTimeTask_Entity>>() {
//				}.getType());
//		mDatas.clear();
//		// 添加人员数据到Data
//		String myname = getIntent().getStringExtra("name");
//
//		int pids = 0;
//		for (OverTimeTask_Entity overTimeTask_Entity : mlist) {
//
//			if(myname.equals(overTimeTask_Entity.getName())){
//				pids=overTimeTask_Entity.getPid();
//			}
//			if(!overTimeTask_Entity.getName().equals("开福区")&&!overTimeTask_Entity.getName().equals("张主任")){
//
//				mDatas.add(new FileBean(overTimeTask_Entity.getId(),
//						overTimeTask_Entity.getPid(),
//						overTimeTask_Entity.getName(), false));
//
//		}
//
//		}
//
//		for (OverTimeTask_Entity overTimeTask_Entity : mlist) {
//			if(pids==overTimeTask_Entity.getId()){
//				mysection.setText(overTimeTask_Entity.getName());
//			}
//		}
//
//		try {
//
//			mAdapter = new SimpleTreeAdapter<FileBean>(myaddress_item_list,
//					Main_Home.this, mDatas, 0);
//
//			mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
//
//				@Override
//				public void onClick(Node node, int position) {
//					if (node.isLeaf()) {
//						for (final OverTimeTask_Entity fileEntity : mlist) {
//							if (fileEntity.getId() == node.getId()) {
//								if (null == fileEntity.getUser_code()) {
//									Toast.makeText(getApplicationContext(),
//											"请选择公司职员！", Toast.LENGTH_SHORT)
//											.show();
//								} else {
//									final AlertDialog dialog_app = builder
//											.create();
//									LinearLayout shenpi = (LinearLayout) LayoutInflater
//											.from(context).inflate(
//													R.layout.submit_dialog,
//													null);
//									TextView heads = (TextView) shenpi
//											.findViewById(R.id.heads);
//									Button button_t = (Button) shenpi
//											.findViewById(R.id.button_t);
//									Button button_f = (Button) shenpi
//											.findViewById(R.id.button_f);
//									heads.setText("您确定要拨打" + node.getName()
//											+ "的号码吗？");
//									dialog_app.show();
//									dialog_app.getWindow().setContentView(
//											shenpi);
//									button_t.setOnClickListener(new OnClickListener() {
//
//										@Override
//										public void onClick(View arg0) {
//											String phoneString;
//											if (fileEntity.getPhone_num()
//													.equals("")) {
//												phoneString = fileEntity
//														.getTelephone();
//											} else {
//												phoneString = fileEntity
//														.getPhone_num();
//											}
//											if (phoneString.equals("")) {
//												ToastManager
//														.getInstance(context)
//														.showToastcenter(
//																"该成员号码为空！");
//												return;
//											}
//
//											Intent intent = new Intent();
//											intent.setAction(intent.ACTION_CALL);
//											intent.setData(Uri.parse("tel:"
//													+ phoneString));
//											startActivity(intent);
//											dialog_app.dismiss();
//										}
//									});
//
//									button_f.setOnClickListener(new OnClickListener() {
//
//										@Override
//										public void onClick(View arg0) {
//											// TODO Auto-generated method stub
//											dialog_app.dismiss();
//										}
//									});
//
//								}
//							}
//						}
//
//					}
//				}
//
//			});
//			myaddress_item_list.setAdapter(mAdapter);
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	@SuppressLint("NewApi")
//	private void setCurrent(int arg0, ImageView img, TextView text,
//			ImageView img2, TextView text2, ImageView img3, TextView text3,
//			ImageView img4, TextView text4) {
//		// TODO Auto-generated method stub
//		vp.setCurrentItem(arg0);
//		switch (arg0) {
//		case 0:
//			img.setBackground(getResources().getDrawable(R.drawable.page2));
//			// img1.setBackground(getResources().getDrawable(R.drawable.message1));
//			img2.setBackground(getResources().getDrawable(R.drawable.address1));
//			img3.setBackground(getResources().getDrawable(R.drawable.topic1));
//			img4.setBackground(getResources().getDrawable(R.drawable.my1));
//			text.setTextColor(getResources().getColor(R.color.text_red));
//			// text1.setTextColor(getResources().getColor(R.color.text_grays));
//			text2.setTextColor(getResources().getColor(R.color.text_grays));
//			text3.setTextColor(getResources().getColor(R.color.text_grays));
//			text4.setTextColor(getResources().getColor(R.color.text_grays));
//			reload_img.setVisibility(ViewGroup.GONE);
//			break;
//		// case 1:
//		// img.setBackground(getResources().getDrawable(R.drawable.page1));
//		// img1.setBackground(getResources().getDrawable(R.drawable.message2));
//		// img2.setBackground(getResources().getDrawable(R.drawable.address1));
//		// img3.setBackground(getResources().getDrawable(R.drawable.topic1));
//		// img4.setBackground(getResources().getDrawable(R.drawable.my1));
//		// text.setTextColor(getResources().getColor(R.color.text_grays));
//		// text1.setTextColor(getResources().getColor(R.color.text_red));
//		// text2.setTextColor(getResources().getColor(R.color.text_grays));
//		// text3.setTextColor(getResources().getColor(R.color.text_grays));
//		// text4.setTextColor(getResources().getColor(R.color.text_grays));
//		// reload_img.setVisibility(ViewGroup.GONE);
//		// break;
//		case 1:
//			img.setBackground(getResources().getDrawable(R.drawable.page1));
//			// img1.setBackground(getResources().getDrawable(R.drawable.message1));
//			img2.setBackground(getResources().getDrawable(R.drawable.address2));
//			img3.setBackground(getResources().getDrawable(R.drawable.topic1));
//			img4.setBackground(getResources().getDrawable(R.drawable.my1));
//			text.setTextColor(getResources().getColor(R.color.text_grays));
//			// text1.setTextColor(getResources().getColor(R.color.text_grays));
//			text2.setTextColor(getResources().getColor(R.color.text_red));
//			text3.setTextColor(getResources().getColor(R.color.text_grays));
//			text4.setTextColor(getResources().getColor(R.color.text_grays));
//			reload_img.setVisibility(ViewGroup.VISIBLE);
//			break;
//		case 2:
//			img.setBackground(getResources().getDrawable(R.drawable.page1));
//			// img1.setBackground(getResources().getDrawable(R.drawable.message1));
//			img2.setBackground(getResources().getDrawable(R.drawable.address1));
//			img3.setBackground(getResources().getDrawable(R.drawable.topic2));
//			img4.setBackground(getResources().getDrawable(R.drawable.my1));
//			text.setTextColor(getResources().getColor(R.color.text_grays));
//			// text1.setTextColor(getResources().getColor(R.color.text_grays));
//			text2.setTextColor(getResources().getColor(R.color.text_grays));
//			text3.setTextColor(getResources().getColor(R.color.text_red));
//			text4.setTextColor(getResources().getColor(R.color.text_grays));
//			reload_img.setVisibility(ViewGroup.GONE);
//			break;
//		case 3:
//			img.setBackground(getResources().getDrawable(R.drawable.page1));
//			// img1.setBackground(getResources().getDrawable(R.drawable.message1));
//			img2.setBackground(getResources().getDrawable(R.drawable.address1));
//			img3.setBackground(getResources().getDrawable(R.drawable.topic1));
//			img4.setBackground(getResources().getDrawable(R.drawable.my2));
//			text.setTextColor(getResources().getColor(R.color.text_grays));
//			// text1.setTextColor(getResources().getColor(R.color.text_grays));
//			text2.setTextColor(getResources().getColor(R.color.text_grays));
//			text3.setTextColor(getResources().getColor(R.color.text_grays));
//			text4.setTextColor(getResources().getColor(R.color.text_red));
//			reload_img.setVisibility(ViewGroup.GONE);
//			break;
//		default:
//			break;
//		}
//	}
//
//	/*
//	 * 注册广播接受者
//	 */
//	public void registerReceiver() {
//		Receiver = new ServiceReceiver();
//		Filters = new IntentFilter();
//		Filters.addAction("meiyin");
//		registerReceiver(Receiver, Filters);
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		boolean back = true;
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			LinearLayout submit_dialog = (LinearLayout) LayoutInflater.from(
//					context).inflate(R.layout.submit_dialog, null);
//			TextView heads = (TextView) submit_dialog.findViewById(R.id.heads);
//			Button button_t = (Button) submit_dialog
//					.findViewById(R.id.button_t);
//			Button button_f = (Button) submit_dialog
//					.findViewById(R.id.button_f);
//			heads.setText("确定要退出吗？                           ");
//			dialog.show();
//			dialog.getWindow().setContentView(submit_dialog);
//			button_t.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					dialog.dismiss();
//					Main_Home.this.finish();
//
//				}
//			});
//			button_f.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					dialog.dismiss();
//				}
//			});
//			back = false;
//		}
//		return back;
//
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//		switch (arg0.getId()) {
//		case R.id.imageView_setting:
//			ListView plus_listview = (ListView) findViewById(R.id.plus_listview);
//			ArrayList<Setting_Entity> setlist = new ArrayList<Setting_Entity>();
//			setlist.add(new Setting_Entity(getResources().getDrawable(
//					R.drawable.gengxin), "版本更新"));
//			// setlist.add(new
//			// Setting_Entity(getResources().getDrawable(R.drawable.xiaoxitu),
//			// "系统通知"));
//			setlist.add(new Setting_Entity(getResources().getDrawable(
//					R.drawable.phonepic), "公司通讯"));
//			setlist.add(new Setting_Entity(getResources().getDrawable(
//					R.drawable.guanyu), "关于美音"));
//			setlist.add(new Setting_Entity(getResources().getDrawable(
//					R.drawable.yonghu), "退出帐号"));
//			Setting_Adapter adapters = new Setting_Adapter(context);
//			adapters.setList(setlist);
//			plus_listview.setAdapter(adapters);
//			plus_listview.setOnItemClickListener(new click(setlist));
//			pluslistview_relative.setVisibility(ViewGroup.VISIBLE);
//			quanview.setVisibility(ViewGroup.VISIBLE);
//
//			break;
//		case R.id.quanview:
//			quanview.setVisibility(ViewGroup.GONE);
//			pluslistview_relative.setVisibility(ViewGroup.GONE);
//			break;
//		default:
//			break;
//		}
//	}
//
//	public class click implements OnItemClickListener {
//		ArrayList<Setting_Entity> setlist;
//
//		public click(ArrayList<Setting_Entity> setlist) {
//			// TODO Auto-generated constructor stub
//			this.setlist = setlist;
//		}
//
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//				long arg3) {
//			// TODO Auto-generated method stub
//			if (setlist.get(arg2).getName().equals("版本更新")) {
//				new UpdateManager(Main_Home.this).checkUpdateInfo(true);
//				quanview.setVisibility(ViewGroup.GONE);
//				pluslistview_relative.setVisibility(ViewGroup.GONE);
//			} else if (setlist.get(arg2).getName().equals("系统通知")) {
//				ToastManager.getInstance(context).showToast("暂无系统通知！");
//				quanview.setVisibility(ViewGroup.GONE);
//				pluslistview_relative.setVisibility(ViewGroup.GONE);
//			} else if (setlist.get(arg2).getName().equals("公司通讯")) {
//				Intent intents = new Intent();
//				intents.setClass(Main_Home.this, CommunicationActivity.class);
//				Main_Home.this.startActivity(intents);
//				quanview.setVisibility(ViewGroup.GONE);
//				pluslistview_relative.setVisibility(ViewGroup.GONE);
//			} else if (setlist.get(arg2).getName().equals("关于美音")) {
//				Intent intents = new Intent();
//				intents.setClass(Main_Home.this, About_Activity.class);
//				Main_Home.this.startActivity(intents);
//				quanview.setVisibility(ViewGroup.GONE);
//				pluslistview_relative.setVisibility(ViewGroup.GONE);
//			} else if (setlist.get(arg2).getName().equals("退出帐号")) {
//				quanview.setVisibility(ViewGroup.GONE);
//				pluslistview_relative.setVisibility(ViewGroup.GONE);
//
//				LinearLayout submit_dialog = (LinearLayout) LayoutInflater
//						.from(context).inflate(R.layout.submit_dialog, null);
//				TextView heads = (TextView) submit_dialog
//						.findViewById(R.id.heads);
//				Button button_t = (Button) submit_dialog
//						.findViewById(R.id.button_t);
//				Button button_f = (Button) submit_dialog
//						.findViewById(R.id.button_f);
//				heads.setText("确定要退出当前帐号吗？             ");
//				dialog.show();
//				dialog.getWindow().setContentView(submit_dialog);
//				button_t.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						dialog.dismiss();
//						sp.edit().remove(SPConstant.MY_NAME).commit();
//						sp.edit().remove(SPConstant.MY_LOGIN_HOST).commit();
//						sp.edit().remove(SPConstant.MY_LOGINID).commit();
//						sp.edit().remove(SPConstant.MY_LOGININFO).commit();
//						sp.edit().remove(SPConstant.MY_TOKEN).commit();
//						sp.edit().remove(SPConstant.USERNAME).commit();
//						sp.edit().remove(SPConstant.MY_GOOUTBRUSH).commit();
//						sp.edit().remove(SPConstant.MY_GOOUTBRUSHLIST).commit();
//						// meiyin.deleteAll();删除所有消息记录
//						stopService(new Intent()
//								.setAction("com.meiyin.services.Meiyinservice"));
//						Intent intent1 = new Intent();
//						intent1.setClass(Main_Home.this, Login.class);
//						intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						topicDao.deleteAll();
//						startActivity(intent1);
//						System.exit(0);
//					}
//				});
//				button_f.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						dialog.dismiss();
//					}
//				});
//			}
//		}
//
//	}
//
//	public class ServiceReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context arg0, Intent arg1) {
//			// TODO Auto-generated method stub
//			// setRefresh();
//			for (MyPage_Entity iterable_element : pagelist) {
//				if (iterable_element.getxName().equals("待审批事项")) {
//					iterable_element
//							.setxMessage(String.valueOf(meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp
//									.getString(SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Type.eq("办公管理系统"), MeiyinnewsDao.Properties.Biaoshi.eq("0")).count()));
//				} else if (iterable_element.getxName().equals("待处理事件")) {
//					Long lonss = meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(
//							SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Type.eq("IT运维管理"), MeiyinnewsDao.Properties.Biaoshi.eq("0")).count();
//					iterable_element.setxMessage(String.valueOf(lonss));
//				}
//			}
//			PageAdapter.setList(pagelist);
//			PageAdapter.notifyDataSetChanged();
//
//			// Long size = meiyin.querysize(sp.getString(SPConstant.USERNAME,
//			// ""));
//			// if (size > 0) {
//			// message_linearout.setVisibility(ViewGroup.VISIBLE);
//			// message_cont.setText(String.valueOf(size));
//			// } else {
//			// message_linearout.setVisibility(ViewGroup.GONE);
//			// }
//
//		}
//
//	}
//
//	// public void setRefresh() {
//	// meiyinlist = meiyin.queryAlluser(sp.getString(SPConstant.USERNAME, ""));
//	// if (meiyinlist.size() < 1) {
//	// mtextView.setVisibility(ViewGroup.VISIBLE);
//	// } else {
//	// mtextView.setVisibility(ViewGroup.GONE);
//	// }
//	// madapter.setList(meiyinlist);
//	// mlistview.setOnItemClickListener(new OnItemClickListener() {
//	//
//	// @Override
//	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//	// long arg3) {
//	// // TODO Auto-generated method stub
//	// long id = meiyinlist.get(arg2).get_id();
//	// Meiyinnews ms = meiyin.queryById(id);
//	// if (ms.getBiaoshi().equals("0")) {
//	// Intent intent = new Intent();
//	// intent.setAction("meiyin");
//	// intent.putExtra("sys", false);
//	// Main_Home.this.sendBroadcast(intent);
//	//
//	// ms.setBiaoshi("1");
//	// meiyin.update(ms);
//	// meiyinlist = meiyin.query_id();
//	// madapter.setList(meiyinlist);
//	// madapter.notifyDataSetChanged();
//	// }
//	// if (meiyinlist.get(arg2).getType().equals("办公管理系统")) {
//	// startActivity(new Intent().setClass(Main_Home.this,
//	// Memulist.class).putExtra("name", "待审批事项"));
//	// } else if (meiyinlist.get(arg2).getType().equals("IT运维管理")) {
//	// startActivity(new Intent().setClass(Main_Home.this,
//	// IT_Management_Activity.class));
//	// } else {
//	// ToastUtil.showToast(context, "暂未开放");
//	// }
//	//
//	// }
//	// });
//	// mlistview.setAdapter(madapter);
//	// }
//
//	/**
//	 * 初始化侧边栏
//	 */
//	private void initSlidingMenu(Bundle savedInstanceState) {
//		// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
//		if (savedInstanceState != null) {
//			mContent = getSupportFragmentManager().getFragment(
//					savedInstanceState, "mContent");
//		}
//		// 设置左侧滑动菜单
//		setBehindContentView(R.layout.menu_frame_left);
//		getSupportFragmentManager().beginTransaction()
//				.replace(R.id.menu_frame, new LeftFragment()).commit();
//
//		// 实例化滑动菜单对象
//		SlidingMenu sm = getSlidingMenu();
//		// 设置可以左右滑动的菜单
//		sm.setMode(SlidingMenu.LEFT);
//		// 设置滑动阴影的宽度
//		sm.setShadowWidthRes(R.dimen.shadow_width);
//		// 设置滑动菜单阴影的图像资源
//		sm.setShadowDrawable(null);
//		// 设置滑动菜单视图的宽度
//		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//		// 设置渐入渐出效果的值
//		sm.setFadeDegree(0.35f);
//		// 设置触摸屏幕的模式,这里设置为全屏
//		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//		// 设置下方视图的在滚动时的缩放比例
//		sm.setBehindScrollScale(0.0f);
//		sm.addIgnoredView(vp);
//
//	}
//
//	/**
//	 * 切换Fragment
//	 *
//	 * @paramfragment
//	 */
//	public void switchConent(String title) {
//		Image.ImageLoader(title, head_photo);
//	}
//
}
