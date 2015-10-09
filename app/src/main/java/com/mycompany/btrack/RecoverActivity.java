package com.mycompany.btrack;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mycompany.btrack.utils.ErrorUtil;
import com.mycompany.btrack.utils.UserUtil;


public class RecoverActivity extends ActionBarActivity {

    private static final String TAG = "RecoverActivity";
    private EditText emailET;
    private ErrorUtil error;
    private Toast toast;
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);
        emailET = (EditText) findViewById(R.id.email);
        Intent intent = getIntent();
        emailET.setText(intent.getExtras().getString(LoginActivity.EXTRA_EMAIL));
        error = new ErrorUtil();
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        app = (App) getApplicationContext();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_recover, menu);
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

    public void recover(View view) {
        Log.i(TAG, "recover()");
        final String email = String.valueOf(emailET.getText());
        if (!UserUtil.isValidEmail(email, error)) {
            toast.setText(error.getMessage());
            toast.show();
        } else {
            app.getFirebase().resetPassword(email,
                    new Firebase.ResultHandler() {
                        @Override
                        public void onSuccess() {
                            toast.setText("email has been sent to " + email);
                            toast.show();
                            Intent intent = new Intent(RecoverActivity.this, LoginActivity.class);
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
