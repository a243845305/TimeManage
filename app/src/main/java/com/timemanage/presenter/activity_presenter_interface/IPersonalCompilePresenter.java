package com.timemanage.presenter.activity_presenter_interface;


import com.timemanage.bean.User;

/**
 * Created by Yawen_Li on 2016/4/22.
 */
public interface IPersonalCompilePresenter {
    void doUpdate();
    User getData();
    void requestUserData();
    void setImagePath(String photoPath);
}
