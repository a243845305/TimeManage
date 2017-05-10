package com.timemanage.utils;

/**
 * 常量定义类
 * Created by Yawen_Li on 2017/1/13.
 */
public class ConstantUtil {
    //============================字符常量的定义===================================
    //包名
    public static String pkgName = "com.timemanage";
    //服务名
    public static String serviceName = "com.timemanage.service.TimeManageService";

    //============================整形常量的定义===================================

    //获取网络的数据
    public static final int GET_NET_DATA = 0x00000002;
    //启动activity
    public static final int START_ACTIVITY = 0x00000009;

    //登录成功之后的缓存文件名（主要为了保存authToken）
    public static final String CACHE_KEY = "Cache_User";
    //用户头像存放文件名
    public static final String AVATAR_FILE_PATH = "/timamanage_avatar.jpg";

}
