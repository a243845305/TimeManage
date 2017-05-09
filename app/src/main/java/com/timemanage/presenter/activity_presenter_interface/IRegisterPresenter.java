package com.timemanage.presenter.activity_presenter_interface;

/**
 * Created by Yawen_Li on 2016/4/20.
 */
public interface IRegisterPresenter {
    boolean isPasswordEnable();
    boolean isUsernameEnable();
    boolean isPwdSame();
    void initData();
    void doRegist();
}
