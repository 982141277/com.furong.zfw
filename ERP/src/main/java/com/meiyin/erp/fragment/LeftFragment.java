package com.meiyin.erp.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meiyin.erp.GreenDao.MeiyinnewsDao;
import com.meiyin.erp.R;
import com.meiyin.erp.activity.Home;
import com.meiyin.erp.activity.IT_Management_Activity;
import com.meiyin.erp.activity.Xxls;
import com.meiyin.erp.adapter.Main_Home_Adapter;
import com.meiyin.erp.application.MyApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Get_Logins;
import com.meiyin.erp.ui.CircleImageView;
import com.meiyin.erp.ui.Image;
import com.meiyin.erp.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @date 2016/7/14
 * @description 侧边栏菜单
 */
public class LeftFragment extends Fragment implements OnClickListener {
	public static BroadcastReceiver ServiceReceiver = null;
	private View function_textview;
	private View discussView;
	private View favoritesView;
	private View tvLastlist;

	private CircleImageView head_photo;
	private SharedPreferences sp;
	// private View settingsView;
	private Context context;
	private View view;
	private int background;
	private TextView user_name;

	private List<Get_Logins> list;
	private MeiyinnewsDao meiyin;
	private Main_Home_Adapter adapter;
	private ListView listview;
	private ServiceReceiver Receiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		sp = context.getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
				Context.MODE_PRIVATE);
		initData();
		registerReceiver();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout_menu, null);
		background = sp.getInt(SPConstant.BACKGROUND, 0);
		background();
		findViews(view);

		return view;
	}

	private void initData() {
		list = new ArrayList<Get_Logins>();
		meiyin = new MyApplication().getDaoSession().getMeiyinnewsDao();
		adapter = new Main_Home_Adapter(context);
		tojson(((Activity) context).getIntent().getStringExtra("login"));
		// Long in = meiyin.querysize(sp.getString(SPConstant.USERNAME, ""));
		// list.add(new Get_Logins("12345678", "消息通知", in));
	}

	public void findViews(View view) {
		head_photo = (CircleImageView) view.findViewById(R.id.head_photo);
		String s = sp.getString("imguri", "");
		if (!s.equals("")) {
			Image.ImageLoader(s.toString(), head_photo);
		}
		listview = (ListView) view.findViewById(R.id.listview);
		adapter.setList(list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				if (list.get(arg2).getSystem_code().equals("06484438")) {
					intent.setClass(context, Home.class);
					startActivity(intent);
				} else if (list.get(arg2).getSystem_code().equals("92165969")) {
					intent.setClass(context, IT_Management_Activity.class);
					startActivity(intent);
				} else if (list.get(arg2).getSystem_code().equals("12345678")) {
					intent.setClass(context, Xxls.class);
					startActivity(intent);
				} else {

					ToastUtil.showToast(context, "暂未开放");
				}

			}
		});
		// User_Entity user_Entity =
		// WpyUser.queryByName(sp.getString(SPConstant.USER_NAME,""));

		// if
		// (null!=head_photo&&null!=user_Entity&&!user_Entity.getImageurl().equals(""))
		// {
		//
		// }
		head_photo.setOnClickListener(this);
		// // // todayView = view.findViewById(R.id.tvToday);
		//
		// tvLastlist = (TextView) view.findViewById(R.id.tvLastlist);
		user_name = (TextView) view.findViewById(R.id.user_name);
		user_name.setText(((Activity) context).getIntent().getStringExtra(
				"name"));
		// // discussView = view.findViewById(R.id.tvDiscussMeeting);
		// // favoritesView = view.findViewById(R.id.tvMyFavorites);
		// // searchs = view.findViewById(R.id.searchs);
		// settingsView = view.findViewById(R.id.tvMySettings);
		function_textview = view.findViewById(R.id.function_textview);
		function_textview.setOnClickListener(this);
		// // // todayView.setOnClickListener(this);
		// // discussView.setOnClickListener(this);
		// // favoritesView.setOnClickListener(this);
		// tvLastlist.setOnClickListener(this);
		// settingsView.setOnClickListener(this);

	}

	// 亮哥权限还没有搞定、预备用
	public void tojson(String response) {

		List<Get_Logins> list = new Gson().fromJson(response,
				new TypeToken<List<Get_Logins>>() {
				}.getType());
		Long lons = meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Type.eq("办公管理系统"), MeiyinnewsDao.Properties.Biaoshi.eq("0")).count();
		Long lonss = meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Type.eq("IT运维管理"), MeiyinnewsDao.Properties.Biaoshi.eq("0")).count();
		for (Get_Logins item : list) {
			if (item.getSystem_code().equals("06484438")) {
				item.setSize(lons);
			} else if (item.getSystem_code().equals("92165969")) {
				item.setSize(lonss);
			} else {
				item.setSize(0l);
			}

			if (item.getSystem_name().equals("办公管理系统")) {
				this.list.add(item);
			} else if (item.getSystem_name().equals("IT运维管理系统")) {
				this.list.add(item);
			} else {

			}

		}

	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	// text_fours = (Spinner) findViewById(R.id.text_fours);
	// text_fours.setOnItemSelectedListener(this);
	// String[] mItems = getResources().getStringArray(R.array.spinner);
	// ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
	// mContext, android.R.layout.simple_spinner_item, mItems);
	// spinnerAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
	// text_fours.setAdapter(spinnerAdapter);
	@Override
	public void onClick(View v) {
		// Fragment newContent = null;
		// String title = null;
		switch (v.getId()) {
		case R.id.head_photo: // 头像
			Intent intent = new Intent();
			intent.setType("image/*");
			/* 使用Intent.ACTION_GET_CONTENT这个Action */
			intent.setAction(Intent.ACTION_GET_CONTENT);
			/* 取得相片后返回本画面 */
			startActivityForResult(intent, 1);
			break;

		// case R.id.tvMySettings: // 设置
		// Intent intent4 = new Intent();
		// intent4.setClass(context, Setting.class);
		// startActivity(intent4);
		// break;
		case R.id.function_textview: // 功能介绍
			Intent intent5 = new Intent();
			intent5.setClass(context, FunctionActivity.class);
			startActivity(intent5);
			break;
		default:
			break;
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == -1) {
			Uri uri = data.getData();
			sp.edit().putString("imguri", uri.toString()).commit();
			Image.ImageLoader(uri.toString(), head_photo);
			switchFragment(uri.toString());
		}
	}

	/*
	 * 注册广播接受者
	 */
	public void registerReceiver() {

		Receiver = new ServiceReceiver();
		IntentFilter Filters = new IntentFilter();
		Filters.addAction(SPConstant.XXACTION);
		Filters.addAction(SPConstant.MYBACKGOUNDACTION);
		context.registerReceiver(Receiver, Filters);
		ServiceReceiver = Receiver;

	}

	public class ServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub

			if (arg1.getAction().equals(SPConstant.MYBACKGOUNDACTION)) {
				int bl = arg1.getIntExtra("bl", 0);
				background = bl;
				background();
			} else if (arg1.getAction().equals(SPConstant.XXACTION)) {
				boolean sys = arg1.getBooleanExtra("sys", false);
				for (Get_Logins item : list) {
					if (item.getSystem_code().equals("06484438")) {
						item.setSize( meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Type.eq("办公管理系统"), MeiyinnewsDao.Properties.Biaoshi.eq("0")).count());
					} else if (item.getSystem_code().equals("92165969")) {
						item.setSize( meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Type.eq("IT运维管理"), MeiyinnewsDao.Properties.Biaoshi.eq("0")).count());
					} else if (item.getSystem_code().equals("12345678")) {
						item.setSize(meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(SPConstant.USERNAME, "")), MeiyinnewsDao.Properties.Biaoshi.eq("0")).count());
					}
				}
				adapter.setList(list);
				listview.setAdapter(adapter);

			}
		}

	}

	private void background() {
		switch (background) {
		case 0:
			view.setBackgroundResource(R.mipmap.my_red);
			break;
		case 1:
			view.setBackgroundResource(R.mipmap.biz_news_local_weather_bg_big);

			break;
		case 2:
			view.setBackgroundResource(R.mipmap.my_green);
			break;

		default:
			break;
		}
	}

	/**
	 * 切换fragment
	 * 
	 * @paramfragment
	 */
	private void switchFragment(String title) {
		if (getActivity() == null) {
			return;
		}
//		if (getActivity() instanceof Main_Home) {
//			Main_Home fca = (Main_Home) getActivity();
//			fca.switchConent(title);
//		}
	}

}
