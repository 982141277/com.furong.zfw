package com.example.administrator.comfurongzfw.retrofit.intf;


import com.example.administrator.comfurongzfw.retrofit.bean.FamousInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface IFamousInfo {

    @GET("/avatardata/mingrenmingyan/lookup")
    Call<FamousInfo> getFamousResult(@Header("apiKey") String apiKey,
                                     @Query("keyword") String keyword,
                                     @Query("page") int page,
                                     @Query("rows") int rows);
}
