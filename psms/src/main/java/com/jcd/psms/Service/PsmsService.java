package com.jcd.psms.Service;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.Toast;

import com.jcd.psms.Activity.WelcomeActivity;
import com.jcd.psms.Application.PsmsApplication;
import com.jcd.psms.Entity.User;
import com.jcd.psms.GreenDao.MessageDao;
import com.jcd.psms.GreenDao.UserDao;
import com.jcd.psms.R;
import com.jcd.psms.Util.APIUtil;
import com.jcd.psms.Util.AssignConnection;
import com.jcd.psms.Util.LogUtil;
import com.jcd.psms.Util.NetUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author liuyuting
 * @version 2015年9月17日 上午09:34:52
 */
public class PsmsService extends Service {

    NetReceiver mReceiver = new NetReceiver();
    IntentFilter mFilter = new IntentFilter();
    private static int MOOD_NOTIFICATIONS = 1;
    private SocketHandler mHandler;
    public Timer timer;
    private TimerTask mTimerTask;
    private Thread getArticlesThread,mThread;
    private boolean connected = true;
    private UserDao mUserDao;
    public mBinder mBinder;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        LogUtil.e("out", "service创建！");
        mBinder = new mBinder();
        mUserDao = PsmsApplication.getInstances().getDaoSession().getUserDao();
        mHandler = new SocketHandler();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mFilter.addAction("PsmsService");
        registerReceiver(mReceiver, mFilter);
        // 开启新的线程从服务器获取数据
        getArticlesThread = new Thread(null, mTask, "PsmsArticles");
        mThread = new Thread(null,watchTask,"mPsmsArticles");
        getArticlesThread.start();
        mThread.start();

    }
    public void unregisterreceiver(){
        unregisterReceiver(mReceiver);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        LogUtil.e("out", "service启动！");
        return START_REDELIVER_INTENT;
    }
    boolean isrun=true;
    Runnable2 mTask = new Runnable2 ()  {

        private AssignConnection con;


        public void stoprunable(){
            con.close();
        }
        @Override
        public void run() {
            try {
                User mud=mUserDao.loadByRowId((long)1);
                if(mud==null){
                    return;
                }
                while(isrun){
                    String Username=mud.getUsername();
                    String getkeys=mud.getGetkey();
                    String URL_ASSIGN = APIUtil.PSMS_MESSAGE+"&Username="+Username;
                     con = new AssignConnection(URL_ASSIGN,"jfjk") {
                        @Override
                        public void mage(String str) throws IOException{
                            LogUtil.e("lyt",str);
                            mHandler.obtainMessage(0, str).sendToTarget();

                        }
                    };
                    Map map = new HashMap();
                    map.put("key",getkeys);
                    con.connect(map);
                    if(!connected){
                        //网络故障处理！！！
                        getArticlesThread.sleep(10000);
                    }
                }

                LogUtil.e("lyt","通讯关闭");
            }catch (Exception e){

            }
        }
    };
    Runnable  watchTask = new Runnable() {
        @Override
        public void run() {

            // 开启定时器，每隔10秒刷新一次
            timer = new Timer();
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    boolean isalive = getArticlesThread.isAlive();
                    if (isalive) {
                        LogUtil.e("out", "线程正常运行！");
                    } else {
                        LogUtil.e("out", "线程挂了");

                    }
                }
            };
            timer.scheduleAtFixedRate(mTimerTask, 0, 5000);
        }
    };

    @SuppressWarnings("deprecation")
    private void showNotification(String contentText,CharSequence contentTitle) {
            NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
            // 点击顶部状态栏的消息后，MainActivity为需要跳转的页面
            Intent mI=new Intent();
            mI.setClass(PsmsService.this, WelcomeActivity.class);
            mI.putExtra("psms_message", contentText);
            mI.putExtra("psms",true);
            PendingIntent mP = PendingIntent.getActivity (PsmsService.this, MOOD_NOTIFICATIONS, mI,PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(this);
                    builder.setContentTitle(contentTitle);
                    builder.setContentText(contentText);
                    builder.setSmallIcon(R.mipmap.logo);
                    builder.setDefaults(Notification.DEFAULT_SOUND);
                    builder.setContentIntent(mP);
                    Notification mN=builder.getNotification();
                    mN.flags|= Notification.FLAG_AUTO_CANCEL;
                    builder .build();
                    notificationManager.notify(MOOD_NOTIFICATIONS, mN);
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.e("out", "开始绑定");
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.e("out", "解除绑定");
        return super.onUnbind(intent);
    }
    @Override
    public void onRebind(Intent intent) {
        LogUtil.e("out", "重连服务");
        super.onRebind(intent);
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        LogUtil.e("out", "服务销毁了");
        super.onDestroy();
    }
    public class mBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply,
                                     int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
        public PsmsService getService() {
            return PsmsService.this;
        }
    }
    boolean ismsg=false;
    public void setmessage(boolean msg){
        ismsg=msg;
    }
    public boolean getmessage(){
        return ismsg;
    }

    class SocketHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonobject=new JSONObject((String) msg.obj);
                        String message=(String) jsonobject.get("message");
                        String to=(String) jsonobject.get("to");
                        String type=(String) jsonobject.get("type");

                        MOOD_NOTIFICATIONS++;
                        CharSequence contentTitle = "网鹰机房云值守";// 标题

                        User mud=mUserDao.loadByRowId((long)1);
                        if(mud==null){
                            return;
                        }
                        String Username=mud.getUsername();
                        showNotification(message,contentTitle);
                        com.jcd.psms.Entity.Message list = new com.jcd.psms.Entity.Message(null,null,Username,to,type,message);
                        MessageDao mMessageDao = PsmsApplication.getInstances().getDaoSession().getMessageDao();
                        mMessageDao.insert(list);
                        setmessage(true);
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
                    connected = true;
                } else {
                    Toast.makeText(context, "网络连接已中断,请检查网络设置！",
                            Toast.LENGTH_LONG).show();
                    connected = false;
                }
            }else if(action.equals("PsmsService")){
                LogUtil.e("out", "连接结束！----------------------------------------------------------------");
                isrun=false;
                mTask.stoprunable();
                timer.cancel();
                unregisterreceiver();

            }
        }

    }

    public interface Runnable2 extends Runnable{
        public void stoprunable();
    }
}
