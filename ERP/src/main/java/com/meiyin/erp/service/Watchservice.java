package com.meiyin.erp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.meiyin.erp.application.SPConstant;
import com.meiyin.erp.util.AndroidUtil;
import com.meiyin.erp.util.DateUtil;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Watchservice extends Service{
	private Intent intents;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.e("wuyu", "Watch服务创建");
		Timer timer = new Timer();
		TimerTask mTimerTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//				if(mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)<mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)/3){
//					Log.e("lyt", "音量"+mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
//					mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)/2, 1);
//					
//				}
				boolean isRun = AndroidUtil.isServiceRunning(Watchservice.this, "com.meiyin.services.Meiyinservice");
				if(isRun){
					Log.e("wuyu","---service运行正常---"+ DateUtil.getCurrentTimeStr());
				}else{
					Log.e("wuyu","---service挂了---"+DateUtil.getCurrentTimeStr());
					stopService(new Intent()
					.setAction("com.meiyin.services.Watchservice"));
				}
			}
		}; 
		timer.scheduleAtFixedRate(mTimerTask, 0, 10000);
		super.onCreate();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.e("wuyu", "闹钟响了");
		
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 10);
//		Intent intent1 = new Intent(Watchservice.this, Meiyinservice.class);
		AlarmManager am=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pen = PendingIntent.getService(this, 0, intent,0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10000, pen);
		return START_REDELIVER_INTENT;
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	Log.e("service","---守护服务挂了---");
    	super.onDestroy();
    }
    
	public class BootReceiver extends BroadcastReceiver {
		public static final String action_boot="android.intent.action.BOOT_COMPLETED";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action_boot.equals(action)) {
				SharedPreferences sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
				boolean isRun = AndroidUtil.isServiceRunning(Watchservice.this, "com.meiyin.services.Meiyinservice");
				if(!isRun){
					startService(new Intent(Watchservice.this, Meiyinservice.class)
					.putExtra("name", sp.getString(SPConstant.MY_NAME, ""))
					.putExtra("token", sp.getString(SPConstant.MY_TOKEN, "")));
				}
			}
		}

	}
}
