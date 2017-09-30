package com.jcd.psms.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.jcd.psms.Application.PsmsApplication;
import com.jcd.psms.Application.SPConstant;
import com.jcd.psms.Entity.User;
import com.jcd.psms.GreenDao.UserDao;
import com.jcd.psms.R;
import com.jcd.psms.Util.UpdateVersion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class WelcomeActivity extends Activity implements View.OnClickListener{
    @BindView(R.id.welcometextview)
    Button welcometextview;
    @BindView(R.id.ProgressBars)
    ProgressBar ProgressBars;
    private int recLen = 3;
    private Handler handler = new Handler();
    List<User> user;
    SharedPreferences sp;
    boolean USERINFO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        ButterKnife.bind(this);
        new UpdateVersion(getApplicationContext()).checkUpdateInfo();
        UserDao mUserDao = PsmsApplication.getInstances().getDaoSession().getUserDao();
        user = mUserDao.loadAll();
        sp = getSharedPreferences(SPConstant.SHAREDPREFERENCES_NAME,
                Context.MODE_PRIVATE);
        USERINFO = sp.getBoolean(SPConstant.USERINFO, false);
        // 开启定时器
        handler.postDelayed(runnable, 1000);
    }

    @Override
    @OnClick({ R.id.welcometextview })
    public void onClick(View v) {
        if(v.getId() == R.id.welcometextview) {
            recLen=5;
            if(!USERINFO){
                startActivity(new Intent().setClass(WelcomeActivity.this, GuideActivity.class));
                WelcomeActivity.this.finish();
            }else{

        if(user.size()==0) {
            Intent intent = new Intent(WelcomeActivity.this,
                    LoginActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(WelcomeActivity.this,
                    MainActivity.class);
            startActivity(intent);

        }
            }
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen--;
            welcometextview.setText("跳过 " + recLen);
            if(USERINFO){
            if(recLen<1){
                if(user.size()==0) {
                    Intent intent = new Intent(WelcomeActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }else{
                    Intent intent = new Intent(WelcomeActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }
            }else if(recLen>3){
                WelcomeActivity.this.finish();
            }else{
                handler.postDelayed(this, 1000);
            }
            }else{
                // 首次进入
                // TODO Auto-generated method stub
                if(recLen<1){
                startActivity(new Intent().setClass(WelcomeActivity.this, GuideActivity.class));
                WelcomeActivity.this.finish();
                }else if(recLen>3){
                    WelcomeActivity.this.finish();
                }else{
                 handler.postDelayed(this, 1000);
                }
            }
        }
    };

}
