package com.my.android.library;


        import android.content.Context;
        import android.os.Looper;

        import com.loopj.android.http.AsyncHttpClient;
        import com.loopj.android.http.FileAsyncHttpResponseHandler;
        import com.loopj.android.http.RequestHandle;
        import com.loopj.android.http.RequestParams;
        import com.loopj.android.http.SyncHttpClient;
        import com.loopj.android.http.TextHttpResponseHandler;
        import com.my.android.library.MJsonHttpHandler;

/**
 * Created by Administrator on 2017/1/24 0024.
 */
public class AsyncHttpClientUtil {

    /**
     * POST请求
     */

    private static AsyncHttpClient asyncHttpClient =new AsyncHttpClient();
    private static SyncHttpClient syncHttpClient =new SyncHttpClient();

    public static AsyncHttpClient getClient() {
        if (Looper.myLooper() == null)
        {
            return syncHttpClient;
        }
        else{
            return asyncHttpClient;
        }
    }

    public void post(String url,Context context,RequestParams params,MJsonHttpHandler jsonhttphandler) {
        AsyncHttpClient client=getClient();
        if (client!= null) {
            try {
                client.post(url,params,jsonhttphandler);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 文件下载
     */
    public RequestHandle download(String url,Context context,FileAsyncHttpResponseHandler FileHttpHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestHandle requestHandle = null;
        if (client != null) {
            try {
                requestHandle = client.get(url,null,FileHttpHandler);
            } catch (Exception e) {
            }
        }
        return requestHandle;
    }
    /**
     * POST请text
     */
    public static void postText(String url,Context context,RequestParams params,TextHttpResponseHandler textHttpResponseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        if (client != null) {
            try {
                client.post(url,params,textHttpResponseHandler);
            } catch (Exception e) {
            }
        }
    }
}
