package com.jcd.psms.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jcd.psms.CalledByJs;
import com.jcd.psms.R;
import com.jcd.psms.Util.APIUtil;
import com.jcd.psms.Util.SlowlyProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends Activity {

    @BindView(R.id.aboutwebview)
    WebView webview;

    private SlowlyProgressBar slowlyProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        ButterKnife.bind(this);
        webview = (WebView) findViewById(R.id.aboutwebview);
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
                                findViewById(R.id.aboutprogress),
                                getWindowManager().getDefaultDisplay().getWidth()
                        )
                        .setViewHeight(3);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);
                webview.reload();
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return true;
            }
        });//限制在webview中打开网页，不用默认浏览器

        webview.loadUrl(APIUtil.PSMS_ABOUT);
        webview.addJavascriptInterface(new CalledByJs(AboutActivity.this, AboutActivity.this), "about");
        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                slowlyProgressBar.setProgress(newProgress);
            }

        });
    }

    @Override
    public void finish() {
        super.finish();
        if (slowlyProgressBar != null) {
            slowlyProgressBar.destroy();
            slowlyProgressBar = null;
        }
    }

}
