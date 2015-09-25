package com.mycompany.btrack;

import android.text.TextUtils;
import android.util.Patterns;

public class User {
    private static final int MIN_PASS_LEN = 8;
    private String email, password;

    protected User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    protected String getEmail() {
        return email;
    }

    protected void setEmail(String email) {
        this.email = email;
    }

    protected String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    protected static final boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    protected static final boolean isValidPassword(String p1, String p2) {
        return p1.length() >= MIN_PASS_LEN && p1.equals(p2);
    }
}
