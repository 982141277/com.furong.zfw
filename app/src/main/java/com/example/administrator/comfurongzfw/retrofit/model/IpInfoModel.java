package com.example.administrator.comfurongzfw.retrofit.model;

import android.content.Context;

import com.example.administrator.comfurongzfw.retrofit.Constant;
import com.example.administrator.comfurongzfw.retrofit.RetrofitWrapper;
import com.example.administrator.comfurongzfw.retrofit.bean.IpInfo;
import com.example.administrator.comfurongzfw.retrofit.intf.ApiService;

import retrofit2.Call;

public class IpInfoModel {
    private static IpInfoModel famousInfoModel;
    private ApiService mApiService;

    public IpInfoModel(Context context) {
        mApiService = RetrofitWrapper.getInstance(Constant.BASEURL_IP).create(ApiService.class);
    }

    public static IpInfoModel getInstance(Context context){
        if(famousInfoModel == null) {
            famousInfoModel = new IpInfoModel(context);
        }
        return famousInfoModel;
    }

    public Call<IpInfo> queryIpInfo(String ip) {
        Call<IpInfo> infoCall = mApiService.getIpinfoResult(ip);
        return infoCall;
    }
}
