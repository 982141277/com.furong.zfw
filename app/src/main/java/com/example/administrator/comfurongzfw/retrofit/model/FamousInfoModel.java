package com.example.administrator.comfurongzfw.retrofit.model;

import android.content.Context;

import com.example.administrator.comfurongzfw.retrofit.Constant;
import com.example.administrator.comfurongzfw.retrofit.RetrofitWrapper;
import com.example.administrator.comfurongzfw.retrofit.bean.FamousInfo;
import com.example.administrator.comfurongzfw.retrofit.bean.FamousInfoReq;
import com.example.administrator.comfurongzfw.retrofit.intf.IFamousInfo;

import retrofit2.Call;

public class FamousInfoModel {
    private static FamousInfoModel famousInfoModel;
    private IFamousInfo mIFamousInfo;

    public FamousInfoModel(Context context) {
        mIFamousInfo = RetrofitWrapper.getInstance(Constant.BASEURL).create(IFamousInfo.class);
    }

    public static FamousInfoModel getInstance(Context context){
        if(famousInfoModel == null) {
            famousInfoModel = new FamousInfoModel(context);
        }
        return famousInfoModel;
    }

    public Call<FamousInfo> queryLookUp(FamousInfoReq famousInfoReq) {
        Call<FamousInfo> infoCall = mIFamousInfo.getFamousResult(famousInfoReq.apiKey, famousInfoReq.keyword, famousInfoReq.page, famousInfoReq.rows);
        return infoCall;
    }
}
