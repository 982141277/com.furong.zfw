package com.example.administrator.comfurongzfw.retrofit.intf;


import com.example.administrator.comfurongzfw.retrofit.bean.IpInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("service/getIpInfo.php")
    Call<IpInfo> getIpinfoResult(@Query("ip") String keyword);

}
