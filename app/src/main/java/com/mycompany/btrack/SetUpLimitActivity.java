package com.mycompany.btrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mycompany.btrack.models.UserInfo;
import com.mycompany.btrack.utils.ErrorUtil;
import com.mycompany.btrack.utils.MoneyTextWatcher;


public class SetUpLimitActivity extends ActionBarActivity {

    public static final String TAG = SetUpLimitActivity.class.getSimpleName();

    public static final String EXTRA_NEW_LIMIT =
            "com.mycompany.btrack.SetUpLimitActivity.limit";

    private EditText mAmount;
    private Button mSetLimit;
    private ErrorUtil error;

    private UserInfo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_limit);

        mAmount = (EditText) findViewById(R.id.limit_amount_EditText);
        mAmount.addTextChangedListener(new MoneyTextWatcher(mAmount));
        mSetLimit = (Button) findViewById(R.id.set_up_limit_button);

        mSetLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double result =  Double.parseDouble(mAmount.getText().toString());
                Intent i = new Intent();
                i.putExtra(EXTRA_NEW_LIMIT, result);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });

        error = new ErrorUtil();
        user.get(getApplicationContext());

    }


    public void setSpendingLimit(View view) {
        Log.i(TAG, "signUp()");

        String limitAmount = String.valueOf(mAmount.getText());

        //Log.i(TAG, "limit:" + limitAmount);

        if (limitAmount.equals("") || limitAmount.equals("-") || limitAmount.equals(".")) {
            user.get(getApplicationContext()).setSpendingLimit(0.00);
            Log.e(TAG, "onError():"+ "invalid amount" + "!!");
            Toast.makeText(getApplicationContext(), "Invalid amount", Toast.LENGTH_LONG).show();
        } else {
            Double d = Double.parseDouble(limitAmount);

            user.get(getApplicationContext()).setSpendingLimit(d);
            //mSetLimit.append(" = " + d);

            user.get(getApplicationContext()).saveUserInfo();
            Toast.makeText(getApplicationContext(), "Limit has been set to " + d, Toast.LENGTH_SHORT).show();
        }

        setResult(Activity.RESULT_OK);
        finish();
    }

}
