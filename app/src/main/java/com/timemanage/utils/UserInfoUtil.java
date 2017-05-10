package com.timemanage.utils;

import android.util.Log;


import com.timemanage.TimeManageAppliaction;
import com.timemanage.bean.User;

/**
 * 返回用户存储在本地的用户信息
 *
 * @author Yawen_Li on 2016/6/6.
 */
public class UserInfoUtil {

    private static String userAvatar;
    private static String userId;
    private static ACache mCache = ACache.get(TimeManageAppliaction.getContext());


    public static String getUserImg(){
        User user = (User) mCache.getAsObject(ConstantUtil.CACHE_KEY);
        if (user != null && user.getUserImg() != null) {
            userAvatar = user.getUserImg();
            Log.e("UserIU", "getUserImg:" + userAvatar);
            return userAvatar;
        }else {
            return null;
        }
    }

    public static String getUserId(){
        User user = (User) mCache.getAsObject(ConstantUtil.CACHE_KEY);
        if (user != null) {
            userId = user.getUserId()+"";
            Log.e("UserIU", "userId" + userId);
        }else {
            userId = "";
        }
        return userId;
    }
}
