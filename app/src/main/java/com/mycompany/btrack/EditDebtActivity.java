package com.mycompany.btrack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mycompany.btrack.models.Debt;
import com.mycompany.btrack.models.Debtor;
import com.mycompany.btrack.models.UserInfo;
import com.mycompany.btrack.utils.MoneyTextWatcher;
import com.mycompany.btrack.utils.StringTextWatcher;

import java.util.Date;
import java.util.UUID;



public class EditDebtActivity extends ActionBarActivity implements DateTimePickerFragment.Callbacks{
    public static final String TAG = EditDebtActivity.class.getSimpleName();
    public static final String EXTRA_DEBT_ID = "debt_id";
    public static final String EXTRA_DEBTOR_NAME = "debtor_name";
    private static final String DIALOG_DATE = "date";
    private Debt mDebt;
    private Debtor mDebtor;
    private Button mDateButton;
    private EditText mAmount;
    private EditText mDescription;
    private Button mUpdate;
    private Date mDate;
    private TextWatcher mT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_debt);

        String name = (String)getIntent().getStringExtra(EXTRA_DEBTOR_NAME);
        mDebtor = UserInfo.get(this.getApplicationContext()).getDebtor(name);
        UUID id = (UUID)getIntent().getSerializableExtra(EXTRA_DEBT_ID);
        mDebt = mDebtor.getDebt(id);

        setTitle("Edit Debt");
        mAmount = (EditText) findViewById(R.id.debt_amount_EditText);
        mDescription = (EditText) findViewById(R.id.debt_description_EditText);
        mDateButton = (Button) findViewById(R.id.debt_date_button);
        mUpdate = (Button) findViewById(R.id.debt_update_button);

        mAmount.setText(mDebt.getFormattedAmount());
        //mAmount.setFilters(new InputFilter[] {new MoneyValueFilter()});
        mAmount.addTextChangedListener(new MoneyTextWatcher(mAmount));



        mDescription.setText(mDebt.getEditTextDescription());
        mDescription.addTextChangedListener(new StringTextWatcher(mDescription, Debt.DESCRIPTION_SIZE_LIMIT));

        mDateButton.setText(mDebt.getFormattedDate());
        mDate = mDebt.getDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                //DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                //TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                DateTimePickerFragment dialog = DateTimePickerFragment.newInstance(mDate);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountString = String.valueOf(mAmount.getText());
                if (amountString.equals("") || amountString.equals("-") || amountString.equals(".")) {
                    mDebt.setAmount(0.00);
                } else {
                    mDebt.setAmount(Double.parseDouble(String.valueOf(mAmount.getText())));
                }

                mDebt.setDescription(String.valueOf(mDescription.getText()));
                mDebt.setDate(mDate);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
        Log.d(TAG, "onCreate()");
    }



    @Override
    public void updateDate(Date date) {
        mDate = date;
        String dateString = (String) DateFormat.format("EEEE, MMM dd, yyyy, h:mm a", mDate);
        mDateButton.setText(dateString);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onPause() {
        super.onPause();
        UserInfo.get(getApplicationContext()).saveUserInfo();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }


}
