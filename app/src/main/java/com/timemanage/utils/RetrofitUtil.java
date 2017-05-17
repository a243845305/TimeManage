package com.timemanage.utils;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * 网络请求的Util
 *
 * @author 高海龙
 */
public class RetrofitUtil {
    /**
     * 获取retroit对象
     * @return retrofit
     */
    public static Retrofit getRetrofit() {
        //1.创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())//解析方法
                .baseUrl(ConstantUtil.BASE_URL)//主机地址
                .build();
        return retrofit;
    }
}
