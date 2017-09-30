package com.jcd.psms.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jcd.psms.CalledByJs;
import com.jcd.psms.R;
import com.jcd.psms.Util.APIUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/6/8 0008.
 */

public class LoginActivity extends Activity {
    @BindView(R.id.webview)
    WebView webview;

    @BindView(R.id.progress)
    ProgressBar mprogress;
    CalledByJs  js;
    boolean isAnimStart;
    int currentProgress;
    String Tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
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




        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mprogress.setVisibility(View.VISIBLE);
                mprogress.setAlpha(1.0f);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);
                webview.reload();
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return true;
            }
        });//限制在webview中打开网页，不用默认浏览器
        js= new CalledByJs(LoginActivity.this,LoginActivity.this);
        webview.loadUrl(APIUtil.PSMS_LOGIN);
        webview.addJavascriptInterface(js, "login");
        webview.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                currentProgress = mprogress.getProgress();
                if (newProgress >= 100 && !isAnimStart) {
                    // 防止调用多次动画
                    isAnimStart = true;
                    mprogress.setProgress(newProgress);
                    // 开启属性动画让进度条平滑消失
                    startDismissAnimation(mprogress.getProgress());
                } else {
                    // 开启属性动画让进度条平滑递增
                    startProgressAnimation(newProgress);
                }
            }

        });
    }

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
/**
 * progressBar消失动画
 */
        private void startDismissAnimation(final int progress) {
            ObjectAnimator anim = ObjectAnimator.ofFloat(mprogress, "alpha", 1.0f, 0.0f);
            anim.setDuration(1500);  // 动画时长
            anim.setInterpolator(new DecelerateInterpolator());     // 减速
            // 关键, 添加动画进度监听器
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float fraction = valueAnimator.getAnimatedFraction();      // 0.0f ~ 1.0f
                    int offset = 100 - progress;
                    mprogress.setProgress((int) (progress + offset * fraction));
                }
            });

            anim.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    // 动画结束
                    mprogress.setProgress(0);
                    mprogress.setVisibility(View.GONE);
                    isAnimStart = false;
                }
            });
            anim.start();
        }

    /**
     * progressBar递增动画
     */
    private void startProgressAnimation(int newProgress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(mprogress, "progress", currentProgress, newProgress);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        js.setmContext(null,null);
        Log.e(Tag,"onDestroy");
        try {
            if (webview != null) {
                webview.removeAllViews();
                webview.destroy();
                webview = null;
             }
             if(mprogress!=null){
                 mprogress=null;
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //		Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri content_url = Uri.parse("http://10.85.239.165:8080/jfjk/html5/psms/login.html");
//        intent.setData(content_url);
//        startActivity(intent);



}
