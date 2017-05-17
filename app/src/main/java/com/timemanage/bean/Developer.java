package com.timemanage.bean;

import java.io.Serializable;

/**
 * Created by Yawen_Li on 2017/5/17.
 */
public class Developer implements Serializable {
    private String depName;
    private String depPosition;
    private String depIntroduction;
    private String depImg;

    public String getdepImg() {
        return depImg;
    }

    public void setdepImg(String depImg) {
        this.depImg = depImg;
    }

    public String getdepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getdepPosition() {
        return depPosition;
    }

    public void setdepPosition(String depPosition) {
        this.depPosition = depPosition;
    }

    public String getdepIntroduction() {
        return depIntroduction;
    }

    public void setdepIntroduction(String depIntroduction) {
        this.depIntroduction = depIntroduction;
    }

}
