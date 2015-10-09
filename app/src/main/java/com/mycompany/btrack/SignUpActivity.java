package com.mycompany.btrack;

import android.content.Intent;
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
import com.mycompany.btrack.utils.ErrorUtil;
import com.mycompany.btrack.utils.UserUtil;

import java.util.Map;


public class SignUpActivity extends ActionBarActivity {

    private static final String TAG = "SignUpActivity";

    private EditText emailET, passwordET, passwordConfET;
    private ErrorUtil error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailET = (EditText) findViewById(R.id.email);
        passwordET = (EditText) findViewById(R.id.password);
        passwordConfET = (EditText) findViewById(R.id.password_conf);
        // get login intent (the intent that started this activity)
        Intent intent = getIntent();
        // fill email and password fields with values from login activity
        emailET.setText(intent.getExtras().getString(LoginActivity.EXTRA_EMAIL));
        passwordET.setText(intent.getExtras().getString(LoginActivity.EXTRA_PASSWORD));
        error = new ErrorUtil();
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

        final String email = String.valueOf(emailET.getText());
        final String password = String.valueOf(passwordET.getText());
        final String passwordConf = String.valueOf(passwordConfET.getText());

        Log.i(TAG, "email:" + email);
        Log.i(TAG, "password:" + password);
        Log.i(TAG, "passwordConf:" + passwordConf);
        // invalid email error
        if (!UserUtil.isValidEmail(email, error)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    error.getMessage(),
                    Toast.LENGTH_SHORT);
            toast.show();
        // invalid passwords error
        } else if (!UserUtil.isValidPassword(password, passwordConf, error)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    error.getMessage(),
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
                    // sign up successful, does not login user to firebase
//                    // save user credentials in app
//                    app.setUser(new User(email, String.valueOf(result.get("uid"))));
//                    // login user
//                    // TODO: go to login activity and login, or call login()?
                    // return to login activity
                    Intent intent = new Intent();
                    intent.putExtra(LoginActivity.EXTRA_EMAIL, email);
                    intent.putExtra(LoginActivity.EXTRA_PASSWORD, password);
                    setResult(RESULT_OK, intent);
                    finish();
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
