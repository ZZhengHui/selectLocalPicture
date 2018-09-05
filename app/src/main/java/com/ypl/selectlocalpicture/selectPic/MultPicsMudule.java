package com.ypl.selectlocalpicture.selectPic;

import java.io.Serializable;

/**
 * Created by 00829bill on 2017/12/26.
 */
public class MultPicsMudule implements Serializable {
    private String path;
    private String picName;
    private boolean isCheck;

    public void setPath(String path) {
        this.path = path;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getPath() {

        return path;
    }

    public String getPicName() {
        return picName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public MultPicsMudule(String path, String picName, boolean isCheck) {
        this.path = path;
        this.picName = picName;
        this.isCheck = isCheck;
    }
}
