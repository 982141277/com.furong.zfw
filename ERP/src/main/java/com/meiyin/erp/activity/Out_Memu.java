package com.meiyin.erp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.meiyin.erp.R;
import com.meiyin.erp.adapter.Spinner_Adapter;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.datepicker.NumericWheelAdapter;
import com.meiyin.erp.datepicker.OnWheelScrollListener;
import com.meiyin.erp.datepicker.WheelView;
import com.meiyin.erp.entity.SellClientInfoEntity;
import com.meiyin.erp.entity.Spinner_Entity;
import com.meiyin.erp.util.AndroidUtil;
import com.meiyin.erp.util.DateUtil;
import com.meiyin.erp.util.Dialog_Http_Util;
import com.my.android.library.AsyncHttpClientUtil;
import com.my.android.library.MJsonHttpHandler;
import com.my.android.library.ToastManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * @param新增外出申请单页
 * @Time 2016-5-20
 */
public class Out_Memu extends Activity implements OnClickListener,
		OnItemSelectedListener, OnCheckedChangeListener {

	private TextView start_time, end_time;
	private Context context;
	private LinearLayout linerlayout_8, linerlayout_9, linerlayout_10;
	private Dialog dialog;
	private String leixin;
	private TextView textss;
	private EditText gotonRoute;//乘车路线
	private View view_8, view_9, view_10;
	private LinearLayout suoqukehu;
	private AsyncHttpClientUtil async ;
	private Dialog_Http_Util dialog_util;
	private SharedPreferences sp ;
	private EditText predictFare_outmemu;//预计交通费
	private EditText mainContent_outmemu;//外出事由
	private EditText startLocation_outmemu;//起始地点
	private EditText toLocation_outmemu;//到达地点
	private String stateMent;
	private boolean sell;
	private int isCar=1;;

	private TextView out_client_name;
	private Activity activity;
	/*
	 * 时间日期选择
	 */
	private LayoutInflater inflater = null;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private WheelView time;
	private WheelView min;
	private WheelView sec;
	private int i;
	private String FID;
	private String vehicle="公交车";
	View view = null;
	boolean isMonthSetted = false, isDaySetted = false;
	private ArrayList<Spinner_Entity> sList,sp_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.out_memu);
		inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		context = getApplicationContext();
		activity=this;
		sp = getSharedPreferences(
				SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		/*
		 * 初始化
		 */
		initHeader();
		initDate();
		views();
	}

	/*
	 * 初始化时间选择器
	 */
	private void initDate() {
		// TODO Auto-generated method stub
		stateMent=getIntent().getStringExtra("stateMent");
		sell=getIntent().getBooleanExtra("sell",false);
		async = new AsyncHttpClientUtil();
		dialog_util=new Dialog_Http_Util();
		dialog = new AlertDialog.Builder(this).create();
		start_time = (TextView) findViewById(R.id.start_time);
		end_time = (TextView) findViewById(R.id.end_time);
		start_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				i = 0;
				RelativeLayout date_dialog = (RelativeLayout) LayoutInflater
						.from(context).inflate(R.layout.date_dialog, null);
				date_dialog.addView(getDataPick());
				dialog.show();
				dialog.getWindow().setContentView(date_dialog);
				start_time.setText(DateUtil.getCurrentTimeStr());
			}
		});
		end_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				i = 1;
				final RelativeLayout date_dialogs = (RelativeLayout) LayoutInflater
						.from(context).inflate(R.layout.date_dialogs, null);
				date_dialogs.addView(getDataPick());
				dialog.show();
				dialog.getWindow().setContentView(date_dialogs);
				end_time.setText(DateUtil.getCurrentTimeStr());
			}
		});
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
		headtitletext.setText("外出申请单");
	}

	public void views() {
		Spinner spinner = (Spinner) findViewById(R.id.spn1);
		spinner.setOnItemSelectedListener(this);
		sList=new ArrayList<Spinner_Entity>();
		if(!sell){
			sList.add(new Spinner_Entity("外出办事", "2"));
		}else{
			sList.add(new Spinner_Entity("客户拜访", "1"));
			sList.add(new Spinner_Entity("外出办事", "2"));
		}

		suoqukehu = (LinearLayout) findViewById(R.id.suoqukehu);

		Spinner_Adapter spinnerAdapter1 = new Spinner_Adapter(context);
		spinnerAdapter1.setList(sList);
		spinner.setAdapter(spinnerAdapter1);

		RadioGroup Rg = (RadioGroup) findViewById(R.id.rgs);
		Rg.setOnCheckedChangeListener(this);


		textss = (TextView) findViewById(R.id.textss);
		gotonRoute = (EditText) findViewById(R.id.edittextss);//乘车路线、打的原因
		predictFare_outmemu = (EditText) findViewById(R.id.predictFare_outmemu);//预计交通费
		mainContent_outmemu = (EditText) findViewById(R.id.mainContent_outmemu);//外出事由
		startLocation_outmemu = (EditText) findViewById(R.id.startLocation_outmemu);//起始地点
		toLocation_outmemu = (EditText) findViewById(R.id.toLocation_outmemu);//到达地点

		linerlayout_8 = (LinearLayout) findViewById(R.id.linerlayout_8);
		linerlayout_9 = (LinearLayout) findViewById(R.id.linerlayout_9);
		linerlayout_10 = (LinearLayout) findViewById(R.id.linerlayout_10);
		view_8 = findViewById(R.id.view_8);
		view_9 = findViewById(R.id.view_9);
		view_10 = findViewById(R.id.view_10);
		out_client_name = (TextView) findViewById(R.id.out_client_name);
		out_client_name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, ClientListActivity.class);
				intent.putExtra("result",SPConstant.TAGOUTMEMUACTIVITY);
				startActivityForResult(intent,SPConstant.TAGOUTMEMUACTIVITY);
			}
		});
		Button submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
		Spinner spn_type = (Spinner) findViewById(R.id.spn_type);//交通工具
		spn_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				vehicle = sp_list.get(arg2).getType_name();
				if(vehicle.equals("公交车")){
					textss.setText("乘车路线：");
					gotonRoute.setHint("请填写具体的乘坐的车次与路线!");
				}else if(vehicle.equals("地铁")){
					textss.setText("乘车路线：");
					gotonRoute.setHint("请填写具体的乘坐的车次与路线!");
				}else if(vehicle.equals("的士")){
					textss.setText("打的原因：");
					gotonRoute.setHint("请详细填写乘坐的士的原因!");
				}else if(vehicle.equals("公车")){
					textss.setText("乘车路线：");
					gotonRoute.setHint("请填写具体的乘坐的车次与路线!");
				}else if(vehicle.equals("其他")){
					textss.setText("乘车路线：");
					gotonRoute.setHint("请填写具体的乘坐的车次与路线!");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		sp_list=new ArrayList<Spinner_Entity>();
		sp_list.add(new Spinner_Entity("公交车", "1"));
		sp_list.add(new Spinner_Entity("地铁", "2"));
		sp_list.add(new Spinner_Entity("的士", "3"));
		sp_list.add(new Spinner_Entity("公车", "4"));
		sp_list.add(new Spinner_Entity("其他", "5"));
		Spinner_Adapter sp_adapter = new Spinner_Adapter(context);
		sp_adapter.setList(sp_list);
		spn_type.setAdapter(sp_adapter);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.submit:
			LinearLayout submit_dialog = (LinearLayout) LayoutInflater.from(
					context).inflate(R.layout.submit_dialog, null);
			Button button_t = (Button) submit_dialog
					.findViewById(R.id.button_t);
			Button button_f = (Button) submit_dialog
					.findViewById(R.id.button_f);
			dialog.show();
			dialog.getWindow().setContentView(submit_dialog);
			button_t.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String key = sp.getString(SPConstant.MY_TOKEN, "");
					
					String stime = start_time.getText().toString();
					String etime = end_time.getText().toString();
					String mgotonRoute=gotonRoute.getText().toString();
					String mpredictFare=predictFare_outmemu.getText().toString();
					String mmainContent=mainContent_outmemu.getText().toString();
					String mstartLocation=startLocation_outmemu.getText().toString();
					String mtoLocation_outmemu=toLocation_outmemu.getText().toString();
					String mout_client_name=out_client_name.getText().toString();//客户名称
					if(leixin.equals("")||stime.equals("")||etime.equals("")||mstartLocation.equals("")||mtoLocation_outmemu.equals("")){
						ToastManager.getInstance(context).showToastcenter("必填项不能为空！");
						dialog.dismiss();
						return;
					}
					long starts = DateUtil.getLongFromStr(stime);
					long ends = DateUtil.getLongFromStr(etime);
//					String now = DateUtil.convertLongToDate(System.currentTimeMillis()).substring(0, 10);
//					String s1=stime.substring(0, 10);
//					String s2=etime.substring(0, 10);
//					if (!s1.equals(now))  {
//						com.my.android.ToastManager.getInstance(context)
//								.showToastcenter("开始时间填写错误，必须在一个工作日之内！");
//						dialog.dismiss();
//						return;
//					}
//					if (!s2.equals(now))  {
//						com.my.android.ToastManager.getInstance(context)
//								.showToastcenter("结束时间填写错误，必须在一个工作日之内！");
//						dialog.dismiss();
//						return;
//					}
					long time2 = ends-starts;
					long times2 = time2 / 1000 / 3600;
					if (times2 < 0)  {
						ToastManager.getInstance(context)
								.showToastcenter("结束时间错误，结束时间不能早于开始时间！");
						dialog.dismiss();
						return;
					}
						RequestParams param = new RequestParams();
							param.setContentEncoding("utf-8");
							param.put("key", key);
							param.put("outTime", stime);//外出时间
							param.put("backTime", etime);//归来时间;
							param.put("startLocation", mstartLocation);//起始地点
							param.put("toLocation", mtoLocation_outmemu);//到达地点
							param.put("isCar",isCar);//交通费
							if(isCar==1){
								if(mpredictFare.equals("")||vehicle.equals("")||mgotonRoute.equals("")){
									dialog.dismiss();
									return;
								}
								param.put("predictFare",mpredictFare);//预计交通费
								param.put("vehicle",vehicle);//交通工具
								if(vehicle.equals("的士")){
									param.put("taxiTereason", mgotonRoute);//打的原因
								}else{
									param.put("taxiTereason", "");//打的原因
								param.put("gotonRoute", mgotonRoute);//乘车路线
								}
							}
							param.put("accompanyId","");//随行人员
							param.put("state", 0);//外出类型
							param.put("Type",leixin);//外出类型
							param.put("mainContent", mmainContent);//外出事由
							param.put("stateDesc", 0);
							param.put("proc_inst_id", 0);
							param.put("stateMent",stateMent);
							if(leixin.equals("客户拜访")){
								if(mout_client_name.equals("")||FID.equals("")){
									ToastManager.getInstance(context).showToastcenter("客户名称不能为空！");
									dialog.dismiss();
									return;
								}
								param.put("clientFID",FID);
//								param.put("clientName",mout_client_name);

							}else{
								if(mmainContent.equals("")){
									ToastManager.getInstance(context).showToastcenter("外出事由不能为空！");
									dialog.dismiss();
									return;
								}
							}	
							Dialog progressDialog = dialog_util.showWaitingDialog(Out_Memu.this,
									 "正在刷新", false);
							async.post(APIURL.CHECK.ADDMEMULIST, context, param, new JsonHandler(Out_Memu.this,progressDialog));		   		    
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
		default:
			break;
		}
	}

	// 外出申请单提交回调类
	private class JsonHandler extends MJsonHttpHandler {
		private Dialog progressDialog;
		protected JsonHandler(Context context,Dialog progressDialog) {
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
			try {
				if (!response.isNull("errorMsg")) {
					String errorMsg=response.getString("errorMsg");
					AndroidUtil.LoginOut(activity,errorMsg);
					return;
				}
				String message = response.getString("message");
				if(message.equals("success")){
					ToastManager.getInstance(context).showToastcenter("提交成功！");
					Out_Memu.this.finish();
				}else{
					ToastManager.getInstance(context).showToastcenter("提交失败！！！");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.spn1:
			leixin = sList.get(arg2).getType_name();

			if (leixin.equals("客户拜访")) {
				suoqukehu.setVisibility(ViewGroup.VISIBLE);
		

			} else if (leixin.equals("外出办事")) {
				suoqukehu.setVisibility(ViewGroup.GONE);

			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.rgs) {
			switch (arg1) {
			case R.id.rgs_1:
				isCar=1;
				linerlayout_8.setVisibility(ViewGroup.VISIBLE);
				linerlayout_9.setVisibility(ViewGroup.VISIBLE);
				linerlayout_10.setVisibility(ViewGroup.VISIBLE);
				view_8.setVisibility(ViewGroup.VISIBLE);
				view_9.setVisibility(ViewGroup.VISIBLE);
				view_10.setVisibility(ViewGroup.VISIBLE);
				break;
			case R.id.rgs_2:
				isCar=0;
				linerlayout_8.setVisibility(ViewGroup.GONE);
				linerlayout_9.setVisibility(ViewGroup.GONE);
				linerlayout_10.setVisibility(ViewGroup.GONE);
				view_8.setVisibility(ViewGroup.GONE);
				view_9.setVisibility(ViewGroup.GONE);
				view_10.setVisibility(ViewGroup.GONE);
				break;
			default:
				break;
			}
		} 

	}

	private View getDataPick() {
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR)+1;
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH);
		;
		int curDate = c.get(Calendar.DAY_OF_MONTH);
		int curmin = c.get(Calendar.HOUR);
		int cursec = c.get(Calendar.MINUTE);

		view = inflater.inflate(R.layout.wheel_date_picker, null);

		year = (WheelView) view.findViewById(R.id.year);
		NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(
				this, 1950, norYear);
		numericWheelAdapter1.setLabel("年");
		year.setViewAdapter(numericWheelAdapter1);
		year.setCyclic(true);// 是否可循环滑动
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);
		NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(
				this, 01, 12, "%02d");
		numericWheelAdapter2.setLabel("月");
		month.setViewAdapter(numericWheelAdapter2);
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);
		// NumericWheelAdapter numericWheelAdapterday=new
		// NumericWheelAdapter(this,1, 31, "%02d");
		// numericWheelAdapterday.setLabel("日");
		// day.setViewAdapter(numericWheelAdapterday);
		day.setCyclic(true);
		day.addScrollingListener(scrollListener);
		initDay(curYear, curMonth);
		// day.setCyclic(true);

		// time= (WheelView) view.findViewById(R.id.time);
		// String[] times = {"上午","下午"} ;
		// ArrayWheelAdapter<String> arrayWheelAdapter=new
		// ArrayWheelAdapter<String>(MainActivity.this,times );
		// time.setViewAdapter(arrayWheelAdapter);
		// time.setCyclic(false);
		// time.addScrollingListener(scrollListener);

		min = (WheelView) view.findViewById(R.id.min);
		NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(
				this, 00, 23, "%02d");
		numericWheelAdapter3.setLabel("时");
		min.setViewAdapter(numericWheelAdapter3);
		min.setCyclic(true);
		min.addScrollingListener(scrollListener);

		sec = (WheelView) view.findViewById(R.id.sec);
		NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(
				this, 00, 59, "%02d");
		numericWheelAdapter4.setLabel("分");
		sec.setViewAdapter(numericWheelAdapter4);
		sec.setCyclic(true);
		sec.addScrollingListener(scrollListener);

		year.setVisibleItems(7);// 设置显示行数
		month.setVisibleItems(7);
		day.setVisibleItems(7);
		// time.setVisibleItems(7);
		min.setVisibleItems(7);
		sec.setVisibleItems(7);

		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth);
		day.setCurrentItem(curDate - 1);
		min.setCurrentItem(curmin - 1);
		sec.setCurrentItem(cursec - 1);

		return view;
	}

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 1950;// 年
			int n_month = month.getCurrentItem() + 1;// 月

			initDay(n_year, n_month);
			int nday = day.getCurrentItem() + 1;
			int nmin = min.getCurrentItem() ;
			int nsec = sec.getCurrentItem() ;
			StringBuilder sb = new StringBuilder();
			sb.append(n_year);
			sb.append("-");
			sb.append(n_month < 10 ? "0" + (n_month) : (n_month));
			sb.append("-");
			sb.append(((nday) < 10) ? "0" + (nday) : (nday));
			sb.append(" ");
			sb.append(((nmin) < 10) ? "0" + (nmin) : (nmin));
			sb.append(":");
			sb.append(((nsec) < 10) ? "0" + (nsec) : (nsec));
			sb.append(":00");
			if (i == 0) {
				start_time.setText(sb.toString());
			} else if (i == 1) {
				end_time.setText(sb.toString().toString());
			}
		}
	};

	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	/**
	 */
	private void initDay(int arg1, int arg2) {
		NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,
				1, getDay(arg1, arg2), "%02d");
		numericWheelAdapter.setLabel("日");
		day.setViewAdapter(numericWheelAdapter);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case SPConstant.TAGOUTMEMUACTIVITY:
			Bundle bundle=data.getExtras();
			if(null!=bundle){
				@SuppressWarnings("unchecked")
				ArrayList<SellClientInfoEntity> mlist= (ArrayList<SellClientInfoEntity>) bundle.getSerializable("mlist");
				StringBuffer sb = new StringBuffer();
				StringBuffer msb = new StringBuffer();
				StringBuffer sbs = new StringBuffer();
				for (SellClientInfoEntity sellClientInfoEntity : mlist) {
					sb.append(sellClientInfoEntity.getsClientName());
					sb.append("、");
					msb.append(sellClientInfoEntity.getsClientWorkAddr());
					msb.append("、");
					sbs.append(sellClientInfoEntity.getfID()+"||||"+sellClientInfoEntity.getsClientName()+"||||"+sellClientInfoEntity.getsClientWorkAddr());
					sbs.append("、");
					
				}
				int end = sbs.toString().lastIndexOf("、");
				FID=sbs.toString().substring(0, end);
				int ends =sb.toString().lastIndexOf("、");
				out_client_name.setText(sb.toString().substring(0, ends));
				toLocation_outmemu.setText(msb.toString());
			}
			break;

		default:
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
