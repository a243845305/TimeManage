package com.timemanage.presenter.activity_presenter;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.timemanage.TimeManageAppliaction;
import com.timemanage.bean.User;
import com.timemanage.db.DataBaseManager;
import com.timemanage.presenter.activity_presenter_interface.IRegisterPresenter;
import com.timemanage.utils.ACache;
import com.timemanage.utils.ConstantUtil;
import com.timemanage.view.activity.MainActivity;
import com.timemanage.view.activity_interface.IRegisterActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 注册操作的逻辑类
 * Created by Yawen_Li on 2016/4/20.
 */
public class RegisterPresenter implements IRegisterPresenter {

    private static long WAITTIME = 1500;                     //正式提交请求前等待时间
    private static boolean SHOWPROGRESS = true;             //显示progress
    private static boolean CLOSEPROGRESS = false;           //关闭progress


    private DataBaseManager dbManager;
    private String VERIFY_UUID = new String();
    private IRegisterActivity iRegisterActivity;
    private String username;
    private String password;
    private String password2;
    private User user;
    private ACache mCache;



    public RegisterPresenter(IRegisterActivity iRegisterActivity) {
        this.iRegisterActivity = iRegisterActivity;
        mCache = ACache.get(TimeManageAppliaction.getContext());
    }


//----------------------接口方法开始----------------------

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        username = iRegisterActivity.getUsername();
        password = iRegisterActivity.getPassword();
        password2 = iRegisterActivity.getPassword2();
        dbManager = new DataBaseManager(TimeManageAppliaction.getContext());
    }

    /**
     * 判断准则：
     * userName 用户名(电话号码)   用正则判断
     * password 密码              高于6位 低于45位
     */

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
     * 验证用户名的有效性（手机号码）是否有效
     *
     * @return true or false
     */
    @Override
    public boolean isUsernameEnable() {
        boolean b = false;
        if (username.length() == 11) {
            Pattern pattern = null;
            Matcher matcher = null;
            pattern = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
            matcher = pattern.matcher(username);
            b = matcher.matches();
        }
        return b;
    }

    @Override
    public boolean isPwdSame() {
        if (password.equals(password2)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void doRegist() {

        iRegisterActivity.showProgress(SHOWPROGRESS);

        /**
         * 要实现progressdialog至少持续1.5秒，需要将请求延迟1.5秒后才发起请求，同时还不可以阻塞上级线程
         * 所以要new出新的线程来完成sleep操作。
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(WAITTIME);
                    if (!dbManager.isExiteByUserName(username)){
                        //数据库中没有该用户，允许注册
                        user = new User();
                        user.setUserName(username);
                        user.setPassWord(password);
                        user.setUserImg(null);
                        boolean flog = dbManager.insertUserInfo(user);
                        if (flog){
                            //将用户信息查出，存入缓存中
                            user = dbManager.findUserByUNameandPwd(username,password);
                            mCache.put(ConstantUtil.CACHE_KEY, user);

                            //注册成功，跳转到主界面
                            iRegisterActivity.showProgress(CLOSEPROGRESS);
                            iRegisterActivity.showRegisteSucceed();
                            Intent intent = new Intent(TimeManageAppliaction.getContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            TimeManageAppliaction.getContext().startActivity(intent);
                            //将注册页面finish掉
                            iRegisterActivity.finishRegisterActivity();
                        }else {
                            //注册失败，不跳转，提示信息
                            iRegisterActivity.showProgress(CLOSEPROGRESS);
                            iRegisterActivity.showRegistFailed();
                        }
                    }else {
                        //数据库中已经存在该用户
                        iRegisterActivity.showProgress(CLOSEPROGRESS);
                        iRegisterActivity.showRegistFailed();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
//----------------------接口方法结束---------------------
}
