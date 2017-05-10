package com.timemanage.view.activity_interface;

/**
 * Created by Yawen_Li on 2016/4/22.
 */
public interface IPersonalCompileActivity {
    String getNickName();
    String getUserSex();
    String getUserSignature();
    void showUpdataSuccess();
    void showUpdataFailed();
    void showNoNet();
    void showProgress(boolean flag);
}
