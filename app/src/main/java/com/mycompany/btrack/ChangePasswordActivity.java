package com.mycompany.btrack;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mycompany.btrack.utils.ErrorUtil;
import com.mycompany.btrack.utils.UserUtil;


public class ChangePasswordActivity extends ActionBarActivity {


    private App app;
    private EditText passwordET;
    private EditText newPasswordET;
    private EditText newPasswordConfET;
    private ErrorUtil error;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        passwordET = (EditText) findViewById(R.id.password);
        newPasswordET = (EditText) findViewById(R.id.new_password);
        newPasswordConfET = (EditText) findViewById(R.id.new_password_conf);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        app = (App) getApplicationContext();
        error = new ErrorUtil();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_change_password, menu);
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

    public void changePassword(View view) {
        final String password = String.valueOf(passwordET.getText());
        final String newPassword = String.valueOf(newPasswordET.getText());
        final String newPasswordConf = String.valueOf(newPasswordConfET.getText());
        if (!UserUtil.isValidPassword(password, error)) {
            toast.setText(error.getMessage());
            toast.show();
        } else if (!UserUtil.isValidPassword(newPassword, newPasswordConf, error)) {
            toast.setText("new " + error.getMessage());
            toast.show();
        } else {
            app.getFirebase().changePassword(app.getUser().getEmail(),
                    password,
                    newPassword,
                    new Firebase.ResultHandler() {

                        @Override
                        public void onSuccess() {
                            toast.setText("password changed");
                            toast.show();
                            Intent intent = new Intent(ChangePasswordActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            toast.setText(firebaseError.getMessage());
                            toast.show();
                        }
                    });
        }
    }
}
