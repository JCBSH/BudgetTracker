package com.mycompany.btrack.utils;

import android.text.TextUtils;

/**
 * Created by godfreytruong on 1/10/2015.
 */
public class UserUtil {

    private static final int MIN_PASS_LEN = 8;
    private static UserUtil instance = null;

    private UserUtil() {
        // singleton class
    }

    public static UserUtil getInstance() {
        if (instance == null) {
            instance = new UserUtil();
        }
        return instance;
    }

    public static final boolean isValidEmail(String email, ErrorUtil error) {
        System.out.println(email);
        if (TextUtils.isEmpty(email)) {
            error.setMessage("email is empty");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error.setMessage("email is invalid");
            return false;
        }
        return true;
    }
    public static final boolean isValidPassword(String p1, String p2, ErrorUtil error) {
        if (p1 == null || p2 == null) {
            return false;
        } else if (p1.length() < MIN_PASS_LEN || p2.length() < MIN_PASS_LEN) {
            error.setMessage("password is less than " + MIN_PASS_LEN + " characters");
            return false;
        } else if (!p1.equals(p2)) {
            error.setMessage("passwords do not match");
            return false;
        }
        return true;
    }
    public static final boolean isValidPassword(String p1, ErrorUtil error) {
        if (p1 == null) {
            return false;
        } else if (p1.length() < MIN_PASS_LEN) {
            error.setMessage("password is less than " + MIN_PASS_LEN + " characters");
            return false;
        }
        return true;
    }
}
