package com.mycompany.btrack;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;


public class SignUpActivity extends ActionBarActivity {

    private static final String TAG = "SignUpActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signUp(View view) {
        Log.i(TAG, "signUp()");

        EditText emailET = (EditText) findViewById(R.id.email);
        EditText passwordET = (EditText) findViewById(R.id.password);
        EditText passwordConfET = (EditText) findViewById(R.id.password_conf);
        final String email = String.valueOf(emailET.getText());
        final String password = String.valueOf(passwordET.getText());
        final String passwordConf = String.valueOf(passwordConfET.getText());

        Log.i(TAG, "email:" + email);
        Log.i(TAG, "password:" + password);
        Log.i(TAG, "passwordConf:" + passwordConf);
        // invalid email error
        if (!User.isValidEmail(email)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Invalid Email",
                    Toast.LENGTH_SHORT);
            toast.show();
        // invalid passwords error
        } else if (!User.isValidPassword(password, passwordConf)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Password must be 8 or more characters and must match",
                    Toast.LENGTH_SHORT);
            toast.show();
        // email and passwords valid, attempt to create user
        } else {
            final App app = (App) getApplicationContext();
            app.getFirebase().createUser(email, password,
                    new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    Log.i(TAG, "onSuccess():"+result.toString());
                    // sign up successful, save user credentials in app
                    app.setUser(new User(email, String.valueOf(result.get("uid"))));
                    // TODO: go to all transactions activity
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    // sign up error
                    Log.i(TAG, "onError():"+firebaseError.toString()+"!!"+firebaseError.getMessage());
                    Toast toast = Toast.makeText(getApplicationContext(),
                            firebaseError.getMessage(),
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        }

    }

}
