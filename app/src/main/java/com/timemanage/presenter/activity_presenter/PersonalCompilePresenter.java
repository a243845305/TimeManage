package com.timemanage.presenter.activity_presenter;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.timemanage.bean.User;
import com.timemanage.presenter.activity_presenter_interface.IPersonalCompilePresenter;
import com.timemanage.utils.ACache;
import com.timemanage.view.activity_interface.IPersonalCompileActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yawen_Li on 2016/4/22.
 * <p/>
 * 目前的问题：服务器更新用户信息接口有问题，错误代码500 错误信息Server error
 */
public class PersonalCompilePresenter implements IPersonalCompilePresenter {

    private IPersonalCompileActivity iPersonalCompileActivity;
    private String authToken;
    private ACache mCache;
    private Handler handler;
    private Context context;
    private String uploadFilePath;
    private UploadManager uploadManager;
    private User user;

    private String uploadToken;
    private String domain;

    private String industry;
    private String scale;
    private String avatar;
    private String realName;
    private String address;
    private String id;


    /**
     * 请求部分还没写完
     *
     * @param iPersonalCompileActivity
     * @param context
     * @param handler
     */

    public PersonalCompilePresenter(IPersonalCompileActivity iPersonalCompileActivity, Context context, Handler handler) {
        this.iPersonalCompileActivity = iPersonalCompileActivity;
        this.mCache = ACache.get(context);
        this.handler = handler;
        this.context = context;
        authToken = UserInfoUtil.findAuthToken();
        user = getData();
        setAvatar();
        getUploadToken();
    }


    //获取上传图片权限token
    private void getUploadToken() {
        Call<JsonObject> call = RetrofitUtil.getRetrofit().create(IUploadToken.class).getUploadToken(authToken);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Response<JsonObject> response, Retrofit retrofit) {
                Log.e("UPLOADTOKEN", response.code() + "");
                if (response.isSuccess()) {
                    JsonObject joResponseBody = response.body();
                    JsonObject getData = joResponseBody.getAsJsonObject("success");
                    uploadToken = getData.get("message").getAsString();
                    domain = ConstantUtil.DOMAIN;

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, "获取token失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAvatar() {
        if (user.getAvatar() != null){
            String str = user.getAvatar();
            int start = ConstantUtil.DOMAIN.length();
            int end = str.length() - ConstantUtil.UPLOAD_PIC_SUFFIX.length();
            avatar = str.substring(start,end);
            Log.e("PersonalCP", "avatar:" + str);
        }
        Log.e("PersonalCP", "Changeavatar:" + avatar);
    }

    //=============================必须的===================================
    private void upload(final String uploadToken, final String domain) {
        if (this.uploadManager == null) {
            this.uploadManager = new UploadManager();
        }
        File uploadFile = new File(this.uploadFilePath);
        UploadOptions uploadOptions = new UploadOptions(null, null, false,
                null, null);
        this.uploadManager.put(uploadFile, null, uploadToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo respInfo,
                                         JSONObject jsonData) {
                        if (respInfo.isOK()) {
                            try {
                                String fileKey = jsonData.getString("key");
                                DisplayMetrics dm = new DisplayMetrics();
                                final int width = dm.widthPixels;
                                final String imageUrl = domain + "/" + fileKey + "?imageView2/0/w/" + width + "/format/jpg";
                                avatar = fileKey;
                                Log.e("imageUrl11111111", imageUrl);
                            } catch (JSONException e) {
                                Toast.makeText(
                                        context,
                                        "上传回复解析错误",
                                        Toast.LENGTH_LONG).show();
                                Log.e(QiniuLabConfig.LOG_TAG, e.getMessage());
                            }
                        } else {
                            Toast.makeText(
                                    context,
                                    "上传图片失败",
                                    Toast.LENGTH_LONG).show();
                            Log.e(QiniuLabConfig.LOG_TAG, respInfo.toString());
                        }
                    }

                }, uploadOptions);
    }

    //-----------------------接口方法---------------------

    @Override
    public void setImagePath(String photoPath) {
        LogUtil.e("qiniu", "photoUri:" + photoPath);
        this.uploadFilePath = photoPath;
        //在选择完图片之后就执行上传操作
        upload(uploadToken, domain);
    }

    @Override
    public void doUpdate() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);

                    realName = iPersonalCompileActivity.getRealName();
                    address = iPersonalCompileActivity.getUserPosition();
                    industry = iPersonalCompileActivity.getUserIndustry();
                    scale = iPersonalCompileActivity.getUserScale();
                    id = user.getId();

                    Map map = new HashMap();
                    map.put("realName", realName);
                    map.put("address", address);
                    LogUtil.d("P", "avatar:" + avatar);
                    LogUtil.d("P", "id:" + id);
                    map.put("avatar", avatar);
                    map.put("industry", industry);
                    map.put("scale", scale);

                    Call<JsonObject> call = RetrofitUtil.getRetrofit().create(IUserData.class).upDataUserData(authToken, id, map);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Response<JsonObject> response, Retrofit retrofit) {
                            if (response.isSuccess()) {
                                //更新请求成功
                                iPersonalCompileActivity.showProgress(false);
                                Log.d("P", "code:" + response.code() + "  body:" + response.body() + "  message:" + response.message());
                                iPersonalCompileActivity.showUpdataSuccess();
                            } else {
                                //更新请求失败
                                iPersonalCompileActivity.showProgress(false);
                                Log.d("P", "code:" + response.code() + "  body:" + response.body() + "  message:" + response.message());
                                iPersonalCompileActivity.showUpdataFailed();
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            //请求出错
                            iPersonalCompileActivity.showProgress(false);
                            Log.e("p", "请求错误");
                            iPersonalCompileActivity.showUpdataFailed();
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    @Override
    public User getData() {
        user = (User) mCache.getAsObject(ConstantUtil.CACHE_KEY);
        return user;
    }

    /**
     * 获取用户信息数据
     */
    @Override
    public void requestUserData() {
        authToken = UserInfoUtil.findAuthToken();
        LogUtil.d("PersonalCompileP", authToken);
        Call<User> call = RetrofitUtil.getRetrofit().create(IUserData.class).getUserData(authToken);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    User user = response.body();
                    //因为请求下来的数据是不包含token的，所以需要手动添加进去，保存后不丢失
                    user.setAuthToken(authToken);
//                    user.setAvatar("http://storage.workerhub.cn//"+user.getAvatar()+"?imageView2/0/w/0/format/jpg");
                    LogUtil.d("PersonalCompileP", "username:" + user.getName() + "  phone:" + user.getPhone() + "  address:" + user.getAddress() + "  token:" + user.getAuthToken());
                    LogUtil.e("PersonalCP", "avatar:" + user.getAvatar());
                    //存储到缓存中，一定包含用户名和用户的电话
                    mCache.put(ConstantUtil.CACHE_KEY, user);
                    //通知主线程更新UI数据
                    handler.sendEmptyMessage(ConstantUtil.GET_NET_DATA);
                } else {
                    //请求失败
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //请求失败
            }
        });
    }

//------------------接口方法结束---------------------
}
