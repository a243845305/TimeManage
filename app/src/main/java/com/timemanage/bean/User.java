package com.timemanage.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Yawen_Li on 2017/5/6.
 */
public class User implements Serializable {
    private int userId;
    private String userName;
    private String passWord;
    private Drawable userImg;

    public User(String userName, String passWord, Drawable userImg, int userId){
        this.userName = userName;
        this.passWord = passWord;
        this.userImg = userImg;
        this.userId = userId;
    }

    public User(){

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Drawable getUserImg() {
        return userImg;
    }

    public void setUserImg(Drawable userImg) {
        this.userImg = userImg;
    }
}
