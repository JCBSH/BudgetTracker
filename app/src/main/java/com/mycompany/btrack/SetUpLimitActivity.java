package com.mycompany.btrack;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mycompany.btrack.models.UserInfo;
import com.mycompany.btrack.utils.ErrorUtil;


public class SetUpLimitActivity extends AppCompatActivity {

    public static final String TAG = SetUpLimitActivity.class.getSimpleName();

    private EditText mAmount;
    private Button mSetLimit;
    private ErrorUtil error;

    private UserInfo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_limit);

        mAmount = (EditText) findViewById(R.id.limit_amount_EditText);
        mSetLimit = (Button) findViewById(R.id.set_up_limit_button);

        error = new ErrorUtil();

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_up_limit, menu);
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
*/

    public void setSpendingLimit(View view) {
        Log.i(TAG, "signUp()");

        final String limitAmount = mAmount.getText().toString();

        Log.i(TAG, "limit:" + limitAmount);

        if (limitAmount.matches("[0-9]+") || limitAmount.matches("[0-9]+.[0-9][0-9]")) {
            Double d = Double.parseDouble(limitAmount);
            //Log.d(TAG, "the double value is now : " + d);
            user.get(getApplicationContext()).setSpendingLimit(d);
            mSetLimit.append(" = " + d);

            Toast.makeText(getApplicationContext(), "Limit has been set to " + d, Toast.LENGTH_SHORT).show();


        } else {
            Log.e(TAG, "onError():"+ "invalid amount" + "!!");
            Toast.makeText(getApplicationContext(), "Invalid amount", Toast.LENGTH_LONG).show();
        }
        setResult(Activity.RESULT_OK);
        finish();
    }

}
