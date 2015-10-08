package com.mycompany.btrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mycompany.btrack.utils.ErrorUtil;
import com.mycompany.btrack.utils.UserUtil;


public class LoginActivity extends ActionBarActivity {

    private static final String TAG = "LoginActivity";
    // extras
    protected static final String EXTRA_EMAIL = "com.mycompany.btrack.EMAIL";
    protected static final String EXTRA_PASSWORD = "com.mycompany.btrack.PASSWORD";
    // result codes
    protected static final int INTENT_SIGN_UP = 1;

    private EditText emailET, passwordET;
    private final ErrorUtil error;

    public LoginActivity() {
        error = new ErrorUtil();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = (EditText) findViewById(R.id.email);
        passwordET = (EditText) findViewById(R.id.password);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void login(View view) {

        Log.i(TAG, "login()");

        final String email = String.valueOf(emailET.getText());
        final String password = String.valueOf(passwordET.getText());

        Log.i(TAG, "email:" + email);
        Log.i(TAG, "password:" + password);
        // invalid email error
        if (!UserUtil.isValidEmail(email, error)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    error.getMessage(),
                    Toast.LENGTH_SHORT);
            toast.show();
        // invalid password error
        } else if (!UserUtil.isValidPassword(password, error)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    error.getMessage(),
                    Toast.LENGTH_SHORT);
            toast.show();
        // email and passwords valid, attempt to login user
        } else {
            authUser(email, password);
        }

    }

    /**
     * authenticate/login user with email and password
     * if login is successful, the all transactions activity will start
     * @param email
     * @param password
     */
    private final void authUser(final String email, String password) {
        final App app = (App) getApplicationContext();
        app.getFirebase().authWithPassword(email, password,
                new Firebase.AuthResultHandler() {

            @Override
            public void onAuthenticated(AuthData authData) {
                // login successful, move to next activity
                Log.i(TAG, authData.toString());
                app.setUser(new User(email, authData.getUid()));
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // login unsuccessful, show error
                Log.i(TAG, firebaseError.toString());
                Toast toast = Toast.makeText(getApplicationContext(),
                        firebaseError.getMessage(),
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    /**
     * start sign up activity, passing email and password values
     * on onActivityResult, the user is logged in and directed to all transactions activity
     * @param view
     */
    public void signUp(View view) {
        Log.i(TAG, "signUp()");
        final String email = String.valueOf(emailET.getText());
        final String password = String.valueOf(passwordET.getText());
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_PASSWORD, password);
        startActivityForResult(intent, INTENT_SIGN_UP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_SIGN_UP) {
            if (resultCode == RESULT_OK) {
                final String email = data.getStringExtra(EXTRA_EMAIL);
                final String password = data.getStringExtra(EXTRA_PASSWORD);
                emailET.setText(email);
                passwordET.setText(password);
                authUser(email, password);
            }
        }
    }

    public void recover(View view) {
        Log.i(TAG, "recover()");
        Intent intent = new Intent(this, RecoverActivity.class);
        startActivity(intent);
    }
}
