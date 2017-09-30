package com.jcd.psms.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jcd.psms.CalledByJs;
import com.jcd.psms.R;
import com.jcd.psms.Service.PsmsService;
import com.jcd.psms.Util.APIUtil;
import com.jcd.psms.Util.AndroidUtil;
import com.jcd.psms.Util.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class MainActivity extends Activity {
    @BindView(R.id.webview)
    WebView webview;

    @BindView(R.id.main_progress)
    ProgressBar m_progress;

    private boolean mBound = false;
    private PsmsService mService;
    boolean isAnimStart;
    int currentProgress;
    SensorManager mSensorManager;

    String Tag;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PsmsService.mBinder binder = (PsmsService.mBinder) service;
            mService = binder.getService(receiver);

            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    Subscriber<String> receiver = new Subscriber<String>() {

        @Override
        public void onCompleted() {

            //数据接收完成时调用
        }

        @Override
        public void onError(Throwable e) {

            //发生错误调用
        }

        @Override
        public void onNext(String s) {
            Log.e(Tag,s);
            webview.loadUrl("javascript:ShowMessage()");
        }
    };
//    protected ScreenSwitchUtils instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        LogUtil.e(Tag,"onCreate");
//        instance = ScreenSwitchUtils.init(this.getApplicationContext());
//        MessageDao mMessageDao = PsmsApplication.getInstances().getDaoSession().getMessageDao();
//        mMessageDao.insert(new Message(null,null,"liuyuting","刘宇庭","d","30楼机房总线红外感应告警，告警值为：1 状态为：闯入 时间：2017-7-24 19:22:22！"));
        Tag=getClass().getSimpleName();
        webview.setBackgroundColor(0);
        webview.getBackground().setAlpha(2);
        //设置Web视图
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);//支持Html5标签
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webview.getSettings().setDefaultTextEncodingName("GBK");//设置编码格式

        webview.loadUrl(APIUtil.PSMS_MAIN);
        webview.addJavascriptInterface(new CalledByJs(MainActivity.this,MainActivity.this), "Main");
        webview.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                currentProgress = m_progress.getProgress();
                if (newProgress >= 100 && !isAnimStart) {
                    // 防止调用多次动画
                    isAnimStart = true;
                    m_progress.setProgress(newProgress);
                    // 开启属性动画让进度条平滑消失
                    startDismissAnimation(m_progress.getProgress());
                } else {
                    // 开启属性动画让进度条平滑递增
                    startProgressAnimation(newProgress);
                }
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                m_progress.setVisibility(View.VISIBLE);
                m_progress.setAlpha(1.0f);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);
                webview.reload();
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                // 必须另开线程进行JS方法调用(否则无法调用)
                webview.post(new Thread(new Runnable(){
                    @Override
                    public void run() {

                        //注意调用的JS方法名要对应上
                        // 调用javascript的callJS()方法
//                            if (mBound) {
//                                boolean msg=mService.getmessage();
//                                if(msg){
//                                    mService.setmessage(false);
//                                    webview.loadUrl("javascript:ShowMessage()");
//                                }
//                            }
                    }
                }));

                super.onPageFinished(view, url);
            }
        });//限制在webview中打开网页，不用默认浏览器

        Intent intent = new Intent(MainActivity.this,
                PsmsService.class);
        if(!AndroidUtil.isServiceRunning(getApplicationContext(),"com.jcd.psms.Service.PsmsService")){
            startService(intent);
            bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
        }else{
            bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
        }


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensor = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuffer  str = new StringBuffer();
        str.append("该手机有" + sensor.size() + "个传感器,分别是:\n");
        for (int i = 0; i < sensor.size(); i++) {
            Sensor s = sensor.get(i);
            switch (s.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    str.append(i + "加速度传感器");
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    str.append(i + "陀螺仪传感器");
                    break;
                case Sensor.TYPE_LIGHT:
                    str.append(i + "环境光线传感器");
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    str.append(i + "电磁场传感器");
                    break;
                case Sensor.TYPE_ORIENTATION:
                    str.append(i + "方向传感器");
                    break;
                case Sensor.TYPE_PRESSURE:
                    str.append(i + "压力传感器");
                    break;
                case Sensor.TYPE_PROXIMITY:
                    str.append(i + "距离传感器");
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    str.append(i + "温度传感器");
                    break;
                default:
                    str.append(i + "未知传感器");
                    break;
            }
        }
        LogUtil.e(Tag,str.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_GAME);
        LogUtil.e(Tag,"onResume");
    }
    public final SensorEventListener mSensorEventListener=new SensorEventListener(){

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            // 获取传感器类型
            int type = event.sensor.getType();
            StringBuilder sb;
            switch (type){
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    sb = new StringBuilder();
                    sb.append("\n温度传感器返回数据：");
                    sb.append("\n当前温度为：");
                    sb.append(values[0]);

                    break;

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     * progressBar消失动画
     */
    private void startDismissAnimation(final int progress) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(m_progress, "alpha", 1.0f, 0.0f);
        anim.setDuration(1500);  // 动画时长
        anim.setInterpolator(new DecelerateInterpolator());     // 减速
        // 关键, 添加动画进度监听器
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();      // 0.0f ~ 1.0f
                int offset = 100 - progress;
                m_progress.setProgress((int) (progress + offset * fraction));
            }
        });

        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束
                m_progress.setProgress(0);
                m_progress.setVisibility(View.GONE);
                isAnimStart = false;
            }
        });
        anim.start();
    }
    /**
     * progressBar递增动画
     */
    private void startProgressAnimation(int newProgress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(m_progress, "progress", currentProgress, newProgress);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        LogUtil.e(Tag,"onDestroy");
        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }

        if (mBound) {
//            mService.unregisterreceiver();
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        instance.start(this);
    }

    @Override
    protected void onRestart() {
        LogUtil.e(Tag,"onRestart");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e(Tag,"onStop");
//        instance.stop();
        mSensorManager.unregisterListener(mSensorEventListener);
        mService.unsubscribe();

    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//            if (instance.isPortrait()) {
//                // 切换成竖屏
//                instance.toggleScreen();
//            } else {
//                // 切换成横屏
//                instance.toggleScreen();
//            }
//        }


    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e(Tag,"onPause");
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "再按一次退出应用！", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
    }
    @Override
    public void finish() {
        super.finish();
        LogUtil.e(Tag,"finish");
//        if(m_progress!=null){
//            m_progress = null;
//        }
    }
}

