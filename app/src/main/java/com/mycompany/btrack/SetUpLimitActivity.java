package com.mycompany.btrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mycompany.btrack.utils.InternetUtil;
import com.mycompany.btrack.utils.MoneyTextWatcher;


public class SetUpLimitActivity extends ActionBarActivity {

    public static final String TAG = SetUpLimitActivity.class.getSimpleName();

    public static final String EXTRA_NEW_LIMIT =
            "com.mycompany.btrack.SetUpLimitActivity.limit";

    private EditText mAmount;
    private Button mSetLimit;

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


                if (InternetUtil.isNetworkConnected(SetUpLimitActivity.this)) {
                    Intent i = new Intent();
                    String amountString = String.valueOf(mAmount.getText());
                    double result = 0.00;
                    if (amountString.equals("") || amountString.equals("-") || amountString.equals(".")) {
                        result = 0.00;
                    } else {
                        result = Double.parseDouble(String.valueOf(mAmount.getText()));
                    }
                    i.putExtra(EXTRA_NEW_LIMIT, result);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                } else {
                    InternetUtil.alertUserNetwork(SetUpLimitActivity.this);
                }

            }
        });

    }



}
