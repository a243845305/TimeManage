package com.timemanage.view.activity_interface;

/**
 * Created by Yawen_Li on 2016/4/20.
 */
public interface IRegisterActivity {
    String getUsername();
    String getPassword();
    String getPassword2();

    void showProgress(boolean flag);
    void showRegisteSucceed();
    void showRegistFailed();

    void finishRegisterActivity();
}
