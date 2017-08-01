package com.meiyin.erp.service;

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

import java.util.Timer;
import java.util.TimerTask;

public class Watchservice extends Service{
	private Intent intents;
	private String Tag;
	private Timer timer;
	@Override
	public void onCreate() {
		Tag=this.getClass().getSimpleName();
		// TODO Auto-generated method stub
		timer = new Timer();
		TimerTask mTimerTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//				if(mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)<mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)/3){
//					Log.e("lyt", "音量"+mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
//					mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)/2, 1);

//				}
				boolean isRun = AndroidUtil.isServiceRunning(Watchservice.this, "com.meiyin.erp.service.Meiyinservice");
				if(isRun){
					Log.e(Tag,"---MeiyinServiceRun---"+ DateUtil.getCurrentTimeStr());
				}else{
					Log.e(Tag,"---MeiyinServiceStop---"+DateUtil.getCurrentTimeStr());
				}
			}
		}; 
		timer.scheduleAtFixedRate(mTimerTask, 0, 10000);
		super.onCreate();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
//		Calendar calendar=Calendar.getInstance();
//		calendar.setTimeInMillis(System.currentTimeMillis());
//		calendar.add(Calendar.SECOND, 10);
////		Intent intent1 = new Intent(Watchservice.this, Meiyinservice.class);
//		AlarmManager am=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
//		PendingIntent pen = PendingIntent.getService(this, 0, intent,0);
//		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10000, pen);
		return START_REDELIVER_INTENT;
	}
	@Override
	public IBinder onBind(Intent arg0) {
		Log.v(Tag,"onBind");
		// TODO Auto-generated method stub
		return null;
	}
    @Override
    public void onDestroy() {
		Log.v(Tag,"onDestroy");
		timer.cancel();
    	// TODO Auto-generated method stub
    	super.onDestroy();
    }
    
	public class BootReceiver extends BroadcastReceiver {
		public static final String action_boot="android.intent.action.BOOT_COMPLETED";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action_boot.equals(action)) {
				SharedPreferences sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
				boolean isRun = AndroidUtil.isServiceRunning(Watchservice.this, "com.meiyin.erp.service.Meiyinservice");
				if(!isRun){
					startService(new Intent(Watchservice.this, Meiyinservice.class)
					.putExtra("name", sp.getString(SPConstant.MY_NAME, ""))
					.putExtra("token", sp.getString(SPConstant.MY_TOKEN, "")));
				}
			}
		}

	}
}
