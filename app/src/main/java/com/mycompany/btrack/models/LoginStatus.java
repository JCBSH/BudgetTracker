package com.mycompany.btrack.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JCBSH on 29/09/2015.
 */
public class LoginStatus {
    private static final String JSON_LOGIN_STATUS = "loginStatus";
    private boolean mLoginStatus;

    public LoginStatus () {
        mLoginStatus = false;
    }

    public LoginStatus(JSONObject json) throws JSONException {;
        if (json.has(JSON_LOGIN_STATUS)) {
            mLoginStatus = json.getBoolean(JSON_LOGIN_STATUS);
        }
    }


    public boolean isLoginStatus() {
        return mLoginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        mLoginStatus = loginStatus;
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_LOGIN_STATUS, mLoginStatus);
        return json;
    }

}
