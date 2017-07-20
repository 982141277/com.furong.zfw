package com.meiyin.erp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.meiyin.erp.GreenDao.MeiyinnewsDao;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.Xxls_Adapter;
import com.meiyin.erp.application.MyApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Meiyinnews;
import com.meiyin.erp.util.ToastUtil;

import java.util.List;

public class Xxls extends Activity {
	private ListView listviewlishi;
	private IntentFilter Filters;
	private ServiceReceiver Receiver;
	private MeiyinnewsDao meiyin;
	private Context context;
	private ImageView imageView1;
	private List<Meiyinnews> list;
	private Xxls_Adapter adapter;
	private Intent intent;
	private TextView tixing;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xxls);
		context = getApplicationContext();
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
		meiyin = MyApplication.getInstance().getDaoSession().getMeiyinnewsDao();
		intent = new Intent();
		registerReceiver();
		adapter = new Xxls_Adapter(context);

		listviewlishi = (ListView) findViewById(R.id.listviewlishi);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		tixing = (TextView) findViewById(R.id.tixing);

		imageView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Xxls.this.finish();
			}
		});

		setRefresh();
		super.onCreate(savedInstanceState);
	}

	public void registerReceiver() {
		Receiver = new ServiceReceiver();
		Filters = new IntentFilter();
		Filters.addAction("meiyin");
		registerReceiver(Receiver, Filters);

	}

	public void setRefresh() {
		list = meiyin.queryBuilder().where(MeiyinnewsDao.Properties.Username.eq(sp.getString(SPConstant.USERNAME, ""))).orderDesc(MeiyinnewsDao.Properties.Time).list();
		if (list.size() < 1) {
			tixing.setVisibility(ViewGroup.VISIBLE);
		} else {
			tixing.setVisibility(ViewGroup.GONE);
		}
		adapter.setList(list);
		listviewlishi.setOnItemClickListener(new Adaperclick());
		listviewlishi.setAdapter(adapter);
	}

	public class ServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			boolean sys = arg1.getBooleanExtra("sys", false);
			Log.e("lyt", "首页广播收到消息" + sys);
			if (sys) {
				setRefresh();
			}

		}

	}

	public class Adaperclick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			long id = list.get(arg2).get_id();
			Meiyinnews ms = meiyin.load(id);
			if (ms.getBiaoshi().equals("0")) {
				intent.setAction("meiyin");
				intent.putExtra("sys", false);
				Xxls.this.sendBroadcast(intent);

				ms.setBiaoshi("1");
				meiyin.update(ms);
				list = meiyin.queryBuilder().orderDesc(MeiyinnewsDao.Properties._id).list();
				adapter.setList(list);
				adapter.notifyDataSetChanged();
			}
			if(list.get(arg2).getType().equals("办公管理系统")){
			startActivity(new Intent().setClass(Xxls.this,Memulist.class).putExtra("name", "待审批事项")); 
			}else if(list.get(arg2).getType().equals("IT运维管理")){
				startActivity(new Intent().setClass(Xxls.this,IT_Management_Activity.class)); 
			}else{
				ToastUtil.showToast(context, "暂未开放");
			}
			}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(Receiver);
		super.onDestroy();
	}
}
