package com.jcd.psms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jcd.psms.Activity.MainActivity;
import com.jcd.psms.Application.BaseApplication;
import com.jcd.psms.Application.PsmsApplication;
import com.jcd.psms.Entity.Message;
import com.jcd.psms.Entity.User;
import com.jcd.psms.GreenDao.MessageDao;
import com.jcd.psms.GreenDao.UserDao;
import com.jcd.psms.Service.PsmsService;
import com.jcd.psms.Util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class CalledByJs {
    private Context mContext;
    private Activity activity;

    public CalledByJs(Context context){
        this.mContext = context;
    }
    public CalledByJs(Context context,Activity activity){
        this.mContext = context;
        this.activity=activity;
    }

    public void setmContext(Context context,Activity activity) {
        this.activity = activity;
        this.mContext = context;
    }

    //在js中被调用的方法
    @JavascriptInterface
    public void callFromJsToast(String js){
        Toast.makeText(mContext, js, Toast.LENGTH_LONG).show();

    }
    //&跳转主界面
    @JavascriptInterface
    public void startActivity(String js){
        UserDao mUserDao = PsmsApplication.getInstances().getDaoSession().getUserDao();
        LogUtil.e("lyt",js);
        try {
            JSONObject jsonobject=new JSONObject(js);
            String username=(String) jsonobject.get("username");
            String getkey="";
            if(!jsonobject.isNull("getkey")){
                getkey=(String) jsonobject.get("getkey");
            }
            //      增数据库的增删改查我们都将通过UserDao来进行，插入操作如下：
            User mUser = new User((long)1,username,"",getkey);
            mUserDao.insertOrReplace(mUser);//添加一个
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(mContext,
                MainActivity.class);
        mContext.startActivity(intent);
        activity.finish();
    }

    //&跳转界面
    @JavascriptInterface
    public void StartActivity(String activityName){
        Class activity= null;
        try {
            activity = Class.forName(activityName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(mContext, activity);
        mContext.startActivity(intent);
    }
    //退出登录
    @JavascriptInterface
    public void logoff(){
        UserDao mUserDao = PsmsApplication.getInstances().getDaoSession().getUserDao();
        mUserDao.deleteAll();
        Intent intent = new Intent();
        intent.setAction("PsmsService");
        activity.sendBroadcast(intent);
        activity.stopService(new Intent(activity, PsmsService.class));
        activity.finish();
        BaseApplication.getInstance().AppExit();
    }
    //&退出AccountActivity
    @JavascriptInterface
    public void finishAccountActivity(){
        activity.finish();
    }
    //&查询消息
    @JavascriptInterface
    public String selectMessage(String js){
        MessageDao mMessageDao = PsmsApplication.getInstances().getDaoSession().getMessageDao();
        List<Message> users = mMessageDao.queryBuilder().where(MessageDao.Properties.User.eq(js)).orderDesc(MessageDao.Properties.Id).list();
        if(users!=null){
        Gson gson = new Gson();
        String str = gson.toJson(users);
            return str;
        }else{
            return "";
        }

    }
    //&删除消息
    @JavascriptInterface
    public void deleteMessage(String js){
        MessageDao mMessageDao = PsmsApplication.getInstances().getDaoSession().getMessageDao();
        String[] msg = js.split(",");
        for (int i=0;i<msg.length;i++) {
            String m=msg[i];
            mMessageDao.deleteByKey(Long.valueOf(m));
        }

    }
    //&拨打电话
    @JavascriptInterface
    public void callPhone(String js){
        Intent intent = new Intent();
        intent.setAction(intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+js));
        mContext.startActivity(intent);
    }
    //&手机分享
    @JavascriptInterface
    public void shareApp(String uri){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(Intent.createChooser(intent,activity.getTitle()));

    }
    //在js中被调用的测试方法、、、、
    @JavascriptInterface
    public String callFromJava(String js){
        String oc=js+"dddddddddd";
        return oc;
    }

}
