package com.companyname.loginforms.loginapp;

/**
 * Created by Mohamed Ahmed on 5/24/2018.
 */

public class User {
    private String nickName;
    private String loginPassword;
    private String mobileNo;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
