package com.timemanage.presenter.activity_presenter_interface;

/**
 * Created by Yawen_Li on 2017/5/9.
 */
public interface ILoginPresenter {
    boolean isPasswordEnable();
    boolean isPhoneEnable();
    void initData();
    void doLogin();
}