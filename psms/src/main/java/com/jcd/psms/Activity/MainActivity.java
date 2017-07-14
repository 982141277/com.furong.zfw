package com.jcd.psms.Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.jcd.psms.CalledByJs;
import com.jcd.psms.R;
import com.jcd.psms.Service.PsmsService;
import com.jcd.psms.Util.APIUtil;
import com.jcd.psms.Util.AndroidUtil;
import com.jcd.psms.Util.SlowlyProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class MainActivity extends Activity {
    @BindView(R.id.webview)
    WebView webview;

    private SlowlyProgressBar slowlyProgressBar;

    private boolean mBound = false;
    private PsmsService mService;
    boolean isrun=true;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PsmsService.mBinder binder = (PsmsService.mBinder) service;
            mService = binder.getService();

            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
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
        slowlyProgressBar =
                new SlowlyProgressBar
                        (
                                findViewById(R.id.mprogress),
                                getWindowManager().getDefaultDisplay().getWidth()
                        )
                        .setViewHeight(3);
        webview.loadUrl(APIUtil.PSMS_MAIN);
        webview.addJavascriptInterface(new CalledByJs(MainActivity.this,MainActivity.this), "Main");
        webview.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                slowlyProgressBar.setProgress(newProgress);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
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
                        // 开启定时器，每隔10秒刷新一次
                        //注意调用的JS方法名要对应上
                        // 调用javascript的callJS()方法
                            if (mBound) {
                                boolean msg=mService.getmessage();
                                if(msg){
                                    mService.setmessage(false);
                                    webview.loadUrl("javascript:ShowMessage()");
                                }
                            }
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

        Observable<String> sender = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("测试用！");
            }

        });
        Observer<String> receiver = new Observer<String>() {

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
                Log.e("lyt",s);

            }
        };
        sender.subscribe(receiver);
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

        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }

        if (mBound) {
            isrun=false;
//            mService.unregisterreceiver();
            unbindService(mConnection);
            mBound = false;
        }
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
        if(slowlyProgressBar!=null){
            slowlyProgressBar.destroy();
            slowlyProgressBar = null;
        }
    }
}

