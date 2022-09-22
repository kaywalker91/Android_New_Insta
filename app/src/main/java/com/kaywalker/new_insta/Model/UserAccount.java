package com.kaywalker.new_insta.Model;

public class UserAccount {
    private String idToken;
    private String user_email;
    private String user_pw;

    public UserAccount() { }

    public String getIdToken() {
        return idToken;
    }

    public String setIdToken(String idToken) {
        return this.idToken = idToken;
    }

    public String getUser_email(){return user_email;}

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

}