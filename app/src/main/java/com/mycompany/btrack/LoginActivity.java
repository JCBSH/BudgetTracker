package com.mycompany.btrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {

        Log.i(TAG, "login()");
    }

    public void signUp(View view) {
        Log.i(TAG, "signUp()");
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void recover(View view) {
        Log.i(TAG, "recover()");
    }
}