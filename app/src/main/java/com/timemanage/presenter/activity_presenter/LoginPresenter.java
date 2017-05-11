package com.timemanage.presenter.activity_presenter;

import android.content.Intent;
import android.telecom.Call;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.timemanage.TimeManageAppliaction;
import com.timemanage.bean.User;
import com.timemanage.db.DataBaseManager;
import com.timemanage.presenter.activity_presenter_interface.ILoginPresenter;
import com.timemanage.service.TimeManageService;
import com.timemanage.utils.ACache;
import com.timemanage.utils.ConstantUtil;
import com.timemanage.utils.LogUtil;
import com.timemanage.view.activity.MainActivity;
import com.timemanage.view.activity_interface.ILoginActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登录逻辑处理类
 *
 * @author Yawen_Li on 2016/4/19.
 */
public class LoginPresenter implements ILoginPresenter {

    private static long WAITTIME = 1500;                    //正式提交请求前等待时间
    private static boolean SHOWPROGRESS = true;             //显示progress
    private static boolean CLOSEPROGRESS = false;           //关闭progress
    private ILoginActivity iLoginActivty;
    private String strPhone;
    private String password;
    private ACache mCache;
    private DataBaseManager dbManager;

    public LoginPresenter(ILoginActivity iLoginActivty) {
        this.iLoginActivty = iLoginActivty;
        LogUtil.e("LoginAContext::::",TimeManageAppliaction.getContext().toString());
        mCache = ACache.get(TimeManageAppliaction.getContext());
    }


//--------------------接口方法开始--------------------

    @Override
    public void initData() {
        dbManager = new DataBaseManager(TimeManageAppliaction.getContext());
        password = iLoginActivty.getPassword();
        strPhone = iLoginActivty.getPhone();
    }

    /**
     * 验证密码格式是否符合要求
     *
     * @return true or false
     */
    @Override
    public boolean isPasswordEnable() {
        if (password.length() > 5 && password.length() < 45) {
            return true;
        } else
            return false;
    }

    /**
     * 验证电话号码是否符合格式
     *
     * @return true or false
     */
    @Override
    public boolean isPhoneEnable() {
        boolean b = false;
        if (strPhone.length() == 11) {
            Pattern pattern = null;
            Matcher matcher = null;
            pattern = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
            matcher = pattern.matcher(strPhone);
            b = matcher.matches();
        }
        return b;
    }


    /**
     * 在本地数据库中找是否存在该user
     *
     */
    @Override
    public void doLogin() {

        iLoginActivty.showProgress(SHOWPROGRESS);
        LogUtil.d("LoginPresenter", strPhone + "");

        /**
         * 要实现progressdialog至少持续1.5秒，需要将请求延迟1.5秒后才发起请求，同时还不可以阻塞上级线程
         * 所以要new出新的线程来完成sleep操作。
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(WAITTIME);
                    User user = null;
                    user = dbManager.findUserByUNameandPwd(strPhone,password);
                    if (user.getUserName() != null){
                        //查询成功，将用户信息保存
                        mCache.put(ConstantUtil.CACHE_KEY, user);
                        //登录成功,获取用户信息成功，跳转到主界面
                        iLoginActivty.showProgress(CLOSEPROGRESS);



                        Intent intent = new Intent(TimeManageAppliaction.getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        TimeManageAppliaction.getContext().startActivity(intent);
                        //将登录页面finish掉
                        iLoginActivty.finishLoginActivity();

                        iLoginActivty.showLoginSucceed();
                    }else {
                        //查询失败
                        iLoginActivty.showProgress(CLOSEPROGRESS);
                        iLoginActivty.showLoginFailed();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}