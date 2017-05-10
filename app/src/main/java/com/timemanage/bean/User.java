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
    private String sex;
    private String signature;
    private String nickName;
    private String userImg;

    public User(String userName, String passWord, String userImg, int userId){
        this.userName = userName;
        this.passWord = passWord;
        this.userImg = userImg;
        this.userId = userId;
    }

    public User(){

    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }
}
