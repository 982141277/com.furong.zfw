package com.example.administrator.comfurongzfw.retrofit.model;

import android.content.Context;

import com.example.administrator.comfurongzfw.retrofit.Constant;
import com.example.administrator.comfurongzfw.retrofit.RetrofitWrapper;
import com.example.administrator.comfurongzfw.retrofit.bean.HttpResponse;
import com.example.administrator.comfurongzfw.retrofit.bean.IpInfo;
import com.example.administrator.comfurongzfw.retrofit.intf.ApiRxService;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;

public class IpInfoRxModel {
    private static IpInfoRxModel famousInfoModel;
    private ApiRxService mApiService;

    public IpInfoRxModel(Context context) {
        mApiService = RetrofitWrapper.getInstance(Constant.BASEURL_IP)
                .create(ApiRxService.class);
    }

    public static IpInfoRxModel getInstance(Context context){
        if(famousInfoModel == null) {
            famousInfoModel = new IpInfoRxModel(context);
        }
        return famousInfoModel;
    }

    public Observable<IpInfo> queryIpInfo(String ip) {
        Observable<IpInfo> infoCall = mApiService.getIpinfoResult(ip);
        return infoCall;
    }

    public Observable<HttpResponse<String>> uploadFile(String des, Map<String,RequestBody> body) {
        return mApiService.uploadFile(des, body);
    }
}
