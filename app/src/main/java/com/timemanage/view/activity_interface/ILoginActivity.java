package com.timemanage.view.activity_interface;

import android.content.Context;

/**
 * Created by Yawen_Li on 2017/5/9.
 */
public interface ILoginActivity {
    String getPhone();
    String getPassword();
    void showProgress(boolean flag);
    void showLoginSucceed();
    void showLoginFailed();
    void showRequestTimeout();
    void finishLoginActivity();
    Context getContext();

}

