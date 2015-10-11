package com.mycompany.btrack;

public class User {
    private static final int MIN_PASS_LEN = 8;
    private String email, uid;

    protected User(String email, String password) {
        this.email = email;
        this.uid = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
