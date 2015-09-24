package com.mycompany.btrack;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SignUpActivity extends Activity {

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void signUp(View view) {
        Log.i(TAG, "signUp()");
    }
}
