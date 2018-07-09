package com.example.administrator.riskreject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2018/7/2.
 */

public interface GitHubApi {

    @GET("user/intimacyinfo?targetUserId=3739199")
    Call<ResponseBody> getUserInfo();
}
