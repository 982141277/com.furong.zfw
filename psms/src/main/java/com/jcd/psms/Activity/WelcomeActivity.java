package com.jcd.psms.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.jcd.psms.Application.PsmsApplication;
import com.jcd.psms.Entity.User;
import com.jcd.psms.GreenDao.UserDao;
import com.jcd.psms.R;
import com.jcd.psms.Util.UpdateVersion;

import java.util.List;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class WelcomeActivity extends Activity {
    private int recLen = 3;
    private Button welcometextview;
    private Handler handler = new Handler();
    List<User> user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        welcometextview=(Button)findViewById(R.id.welcometextview);
        ProgressBar ProgressBars=(ProgressBar)findViewById(R.id.ProgressBars);
        new UpdateVersion(WelcomeActivity.this,ProgressBars).checkUpdateInfo();
        UserDao mUserDao = PsmsApplication.getInstances().getDaoSession().getUserDao();
        user = mUserDao.loadAll();

        welcometextview.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                recLen=5;
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
        });
        // 开启定时器
        handler.postDelayed(runnable, 1000);
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen--;
            welcometextview.setText("跳过 " + recLen);
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
        }
    };


}
