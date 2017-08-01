package com.meiyin.erp.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.widget.Toast;

import com.meiyin.erp.GreenDao.MeiyinnewsDao;
import com.meiyin.erp.R;
import com.meiyin.erp.activity.IT_Management_Sq;
import com.meiyin.erp.activity.Main_Home;
import com.meiyin.erp.activity.Memulist;
import com.meiyin.erp.activity.TopicDetailsActivity;
import com.meiyin.erp.application.APIURL;
import com.meiyin.erp.application.MyApplication;
import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.entity.Meiyinnews;
import com.meiyin.erp.util.DateUtil;
import com.meiyin.erp.util.LogUtil;
import com.meiyin.erp.util.NetUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author liuyuting
 * @version 2015年9月17日 上午09:34:52
 */
public class Meiyinservice extends Service {

	NetReceiver mReceiver = new NetReceiver();
	IntentFilter mFilter = new IntentFilter();
	private static int MOOD_NOTIFICATIONS = 1;

	private String ip = APIURL.API;
	private int port = 8888;
	private Socket mSocket;
	private boolean isStartRecieveMsg;

	private SocketHandler mHandler;
	protected BufferedReader mReader;
	protected BufferedWriter mWriter;
	private Timer timer;
	private TimerTask mTimerTask;

	private Thread getArticlesThread;
	private MeiyinnewsDao meiyin;
	private boolean connected = true;
	private String name;
	private String token;
	SharedPreferences sp ;
	private String Tag;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Tag=this.getClass().getSimpleName();
		Log.v(Tag,"onCreate");
		super.onCreate();
	}

	@SuppressLint("InlinedApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v(Tag,"onStartCommand");
		sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
		mHandler = new SocketHandler();
		meiyin = MyApplication.getInstance().getDaoSession().getMeiyinnewsDao();
		if(null!=intent&&null!=intent.getStringExtra("name")){
		name=intent.getStringExtra("name");
		LogUtil.e("lyt", name);
		token=intent.getStringExtra("token");
		}else{
			name=sp.getString(SPConstant.MY_NAME, "");
			token=sp.getString(SPConstant.MY_TOKEN, "");
		}
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);

		// 开启新的线程从服务器获取数据
		getArticlesThread = new Thread(null, mTask, "getNewArticles");
		if (!token.equals("")) {
			getArticlesThread.start();
			Log.v(Tag,"getArticlesThread-start");
			// 开启定时器，每隔10秒刷新一次
			timer = new Timer();
			mTimerTask = new TimerTask() {

				@Override
				public void run() {
					Log.v(Tag,"mTimerTask-run");
					// TODO Auto-generated method stub
					boolean isalive = getArticlesThread.isAlive();
					if (isalive) {
						Log.i(Tag,"getArticlesThread-run");
					} else {
						Log.i(Tag,"getArticlesThread-death");
//
//						mTimerTask.cancel();
//						mTimerTask=null;
//						if(!getSharedPreferences(
//								SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE).getString(SPConstant.MY_TOKEN, "").equals("")){
//							LogUtil.e("out", "重新开启一个线程");
//							try {
//							getArticlesThread.destroy();
//							Thread.sleep(5000);
//						} catch (Exception e) {
//							getArticlesThread.start();
//							e.printStackTrace();
//						}
//						getArticlesThread.start();
//						}else{
//							LogUtil.e("out", "无key");
//						}
					}
				}
			};
			timer.scheduleAtFixedRate(mTimerTask, 0, 10000);
		}
		return START_REDELIVER_INTENT;
	}

	Runnable mTask = new Runnable() {

		@Override
		public void run() {

			if (!isStartRecieveMsg) {
				isStartRecieveMsg = true;
				while (isStartRecieveMsg) {
					try {
						LogUtil.e("out", "Task进入");
						mSocket = new Socket(ip, port);
						boolean ss = mSocket.isBound();
						boolean is = mSocket.isConnected();
						LogUtil.e("out", "Socket是否连接:---" + is);
						LogUtil.e("out", "Socket是否绑定:---" + ss);
						mReader = new BufferedReader(new InputStreamReader(
								mSocket.getInputStream(), "utf-8"));
						mWriter = new BufferedWriter(new OutputStreamWriter(
								mSocket.getOutputStream(), "utf-8"));
						send();
						mWriter.write("6\n");
						int count = 0;
						while (isStartRecieveMsg) {
							count++;
							if (count > 60) {
								LogUtil.e("out", "由于长时间收不到心跳包,socket准备重连" + count);
								break;
							}
							
							while (mReader.ready()&isStartRecieveMsg) {
								String xx = mReader.readLine();
								if (xx.length() > 3) {
									mHandler.obtainMessage(0, xx)
											.sendToTarget();
									LogUtil.e("out", "读取到消息：" + xx);
									JSONObject json = new JSONObject(xx);
									String id = json.getString("id");
									JSONObject jsons = new JSONObject();
									jsons.put("id", id);
									jsons.put("message", "success");
									mWriter.write(jsons.toString() + "\n");
									count = 0;
								} else if ("6".equals(xx)) {
									count = 0;
									LogUtil.e("out", "心跳：" + xx);
									mWriter.write(xx + "\n");

								}
						
							}
							mWriter.flush();
							Thread.sleep(1000);

						}
						mWriter.close();
						mReader.close();
						mSocket.close();
					} catch (Exception e) {
						LogUtil.e("out", "mSocket连接超时!");
						while (!connected) {
							try {
								LogUtil.e("out", "没有网络!");
								Thread.sleep(10000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							e.printStackTrace();
						}
					}
					LogUtil.e(
							"service",
							"服务关闭时间："
									+ DateUtil
											.convertLongToDate(System
													.currentTimeMillis()));

				}

			}
		}
	};

	private void send() {
		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				sendMsg();
				return null;
			}
		}.execute();
	}

	protected void sendMsg() {
		try {
			JSONObject json = new JSONObject();
			json.put("id", "666");
			json.put("message", name);
			json.put("key", token);
			mWriter.write(json.toString() + "\n");
			mWriter.flush();
			Log.e(Tag,"message:"+json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private String showNotification(String contentText,
			CharSequence contentTitle, String url) {
		LogUtil.e("lyt", url);
		String type = "";
//		/check/topic!showTopic.ac?topic_code=319&type=1&menuid=01090801&menu_value=FBGG&user_id=5f0a1cc1c5e84cee923b3394e77cc3f5
//		/itsm_new/incedent!getIncedentProcessInfo.it?menu_id=01020405&menu_value=SJCL&event_no=20160811005&user_id=91b03b27f6154081b61fb2a74d843cd6
		if (url.contains("itsm_new")) {
			NotificationManager notificationManager = (NotificationManager) this
					.getSystemService(NOTIFICATION_SERVICE);
			// 点击顶部状态栏的消息后，MainActivity为需要跳转的页面
			Intent mI=new Intent();
			int start = url.indexOf("_no=");
			int end = url.indexOf("&user");
			String event_no = url.substring(start + 4, end);
			Log.e(Tag,"itsm_new:"+event_no);
			mI.setClass(Meiyinservice.this, IT_Management_Sq.class);
			mI.putExtra("event_no", event_no);
			PendingIntent mP = PendingIntent.getActivity (Meiyinservice.this, MOOD_NOTIFICATIONS, mI,PendingIntent.FLAG_UPDATE_CURRENT);
			Notification.Builder builder = new Notification.Builder(this);
			builder.setContentTitle("湖南美音--IT运维管理系统");
			builder.setContentText(Html.fromHtml(contentText,imgGetter, null));
			builder.setSmallIcon(R.mipmap.logoss);
			builder.setDefaults(Notification.DEFAULT_SOUND);
			builder.setContentIntent(mP);
			Notification mN=builder.getNotification();
			mN.flags|= Notification.FLAG_AUTO_CANCEL;
			builder.build();
			notificationManager.notify(MOOD_NOTIFICATIONS, mN);
			type = "IT运维管理";
		} else if (url.contains("check")) {
			NotificationManager notificationManager = (NotificationManager) this
					.getSystemService(NOTIFICATION_SERVICE);
			// 点击顶部状态栏的消息后，MainActivity为需要跳转的页面
			Intent mI=new Intent();
			if(url.contains("topic")){
				String[] urls = url.split("&");
				String idString = "";
				for (int i = 0; i < urls.length; i++) {
					if(urls[i].contains("topic_code")){
						idString=urls[i].substring(11);
					}
				}
				type = "公告";
				mI.setClass(this, TopicDetailsActivity.class);
				mI.putExtra("topic_code", idString);
			}else{
				type = "办公管理系统";
				mI.setClass(this, Memulist.class);
				mI.putExtra("name", "待审批事项");
			}

			PendingIntent mP = PendingIntent.getActivity (Meiyinservice.this, MOOD_NOTIFICATIONS, mI,PendingIntent.FLAG_UPDATE_CURRENT);
			Notification.Builder builder = new Notification.Builder(this);
			builder.setContentTitle("湖南美音--办公管理系统");
			builder.setContentText(Html.fromHtml(contentText,imgGetter, null));
			builder.setSmallIcon(R.mipmap.logoss);
			builder.setDefaults(Notification.DEFAULT_SOUND);
			builder.setContentIntent(mP);
			Notification mN=builder.getNotification();
			mN.flags|= Notification.FLAG_AUTO_CANCEL;
			builder.build();
			notificationManager.notify(MOOD_NOTIFICATIONS, mN);

		} else {
			NotificationManager notificationManager = (NotificationManager) this
					.getSystemService(NOTIFICATION_SERVICE);
			// 点击顶部状态栏的消息后，MainActivity为需要跳转的页面
			Intent mI=new Intent();
			mI.setClass(Meiyinservice.this, Main_Home.class);
			PendingIntent mP = PendingIntent.getActivity (Meiyinservice.this, MOOD_NOTIFICATIONS, mI,PendingIntent.FLAG_UPDATE_CURRENT);
			Notification.Builder builder = new Notification.Builder(this);
			builder.setContentTitle("湖南美音");
			builder.setContentText(Html.fromHtml(contentText,imgGetter, null));
			builder.setSmallIcon(R.mipmap.logoss);
			builder.setDefaults(Notification.DEFAULT_SOUND);
			builder.setContentIntent(mP);
			Notification mN=builder.getNotification();
			mN.flags|= Notification.FLAG_AUTO_CANCEL;
			builder.build();
			notificationManager.notify(MOOD_NOTIFICATIONS, mN);
			type = "其他系统";

		}
		return type;
	}

	@Override
	public IBinder onBind(Intent intent) {

		return mBinder;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		LogUtil.e(Tag, "*****meiyinservice服务关闭了*****");
		// notificationManager.cancel(MOOD_NOTIFICATIONS);
		isStartRecieveMsg=false;
		timer.cancel();
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	private final IBinder mBinder = new Binder() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
				int flags) throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};

	@SuppressLint("HandlerLeak")
	class SocketHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				try {
					JSONObject json = new JSONObject((String) msg.obj);
					String msgs = json.getString("message");
					JSONObject msgs2 = new JSONObject(msgs);
					String content = msgs2.getString("content");// 内容
					String time = json.getString("time");
					Long times = DateUtil.getLongFromStr(time);
					MOOD_NOTIFICATIONS++;
					CharSequence contentTitle = "湖南美音";// 标题
					String type = showNotification(content, contentTitle,
							msgs2.getString("url"));
					String[] urls = msgs2.getString("url").split("&");
					String idString = "";
					for (int i = 0; i < urls.length; i++) {
						if(urls[i].contains("applyId")){
							idString=urls[i].substring(8);
						}
					}
					Meiyinnews list = new Meiyinnews(null, times,
							json.getString("id"), content, "0", type,sp.getString(SPConstant.USERNAME, ""),idString);
					meiyin.insert(list);
					Intent intent = new Intent();
					intent.setAction("meiyin");
					intent.putExtra("sys", true);
					Meiyinservice.this.sendBroadcast(intent);
					if (MOOD_NOTIFICATIONS > 1000) {
						MOOD_NOTIFICATIONS = 1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;

			default:
				break;
			}
		}
	}

	public class NetReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
				boolean isConnected = NetUtils.isNetworkConnected(context);
				System.out.println("网络状态：" + isConnected);
				System.out.println("wifi状态："
						+ NetUtils.isWifiConnected(context));
				System.out.println("移动网络状态："
						+ NetUtils.isMobileConnected(context));
				System.out.println("网络连接类型："
						+ NetUtils.getConnectedType(context));
				if (isConnected) {
//					Toast.makeText(context, "已经连接网络", Toast.LENGTH_LONG).show();
					connected = true;
				} else {
					Toast.makeText(context, "网络连接已中断,请检查网络设置！",
							Toast.LENGTH_LONG).show();
					connected = false;
				}
			}
		}

	}
	ImageGetter imgGetter = new Html.ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			// Log.i("zkr", source);
			int i = 0;
			try {
				Field field = R.drawable.class.getField(source);
				i = field.getInt(new R.drawable());
				drawable = getResources().getDrawable(i);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth() - 10,
						drawable.getIntrinsicHeight() - 10);
				// Log.d("zkr", i + "");
			} catch (Exception e) {
				// LogUtil.e("zkr", e.toString());
			}

			return drawable;
		}
	};
}
