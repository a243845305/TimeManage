package com.timemanage.api;

import com.google.gson.JsonObject;
import com.timemanage.bean.Developer;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Created by Yawen_Li on 2017/5/17.
 */
public interface IDeveloperDatas {
    @GET("DeveloperServlet")
    Call<JsonObject> getDepDatas();
}
