package com.mycompany.btrack;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mycompany.btrack.models.Transaction;
import com.mycompany.btrack.models.UserInfo;
import com.mycompany.btrack.utils.MoneyTextWatcher;
import com.mycompany.btrack.utils.StringTextWatcher;

import java.util.Date;
import java.util.UUID;


public class EditTransactionActivity extends ActionBarActivity implements DateTimePickerFragment.Callbacks{
    public static final String TAG = EditTransactionActivity.class.getSimpleName();
    public static final String EXTRA_TRANSACTION_ID = "com.mycompany.btrack.EditTransactionActivity.transaction_id";
    private static final String DIALOG_DATE = "date";
    private Transaction mTransaction;
    private EditText mRecipient;
    private Button mDateButton;
    private EditText mAmount;
    private EditText mDescription;
    private Button mUpdate;
    private Date mDate;
    private TextWatcher mT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction_actitvity);
        UUID id = (UUID)getIntent().getSerializableExtra(EXTRA_TRANSACTION_ID);
        mTransaction = UserInfo.get(this.getApplicationContext()).getTransaction(id);
        setTitle(UserInfo.get(this.getApplicationContext()).getTransaction(id).getRecipient());
        mAmount = (EditText) findViewById(R.id.transaction_amount_EditText);
        mRecipient = (EditText) findViewById(R.id.transaction_recipient_EditText);
        mDescription = (EditText) findViewById(R.id.transaction_description_EditText);
        mDateButton = (Button) findViewById(R.id.transaction_date_button);
        mUpdate = (Button) findViewById(R.id.transaction_update_button);
        mAmount.setText(mTransaction.getFormattedAmount());
        //mAmount.setFilters(new InputFilter[] {new MoneyValueFilter()});
        mAmount.addTextChangedListener(new MoneyTextWatcher(mAmount));


        mRecipient.setText(mTransaction.getEditTextRecipient());
        mRecipient.addTextChangedListener(new StringTextWatcher(mRecipient, 20));

        mDescription.setText(mTransaction.getEditTextDescription());
        mDescription.addTextChangedListener(new StringTextWatcher(mDescription, 50));

        mDateButton.setText(mTransaction.getFormattedDate());
        mDate = mTransaction.getDate();
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
                mTransaction.setAmount(Double.parseDouble(String.valueOf(mAmount.getText())));
                mTransaction.setRecipient(String.valueOf(mRecipient.getText()));
                mTransaction.setDescription(String.valueOf(mDescription.getText()));
                mTransaction.setDate(mDate);
                UserInfo.get(getApplicationContext()).sortTransactions();
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
        UserInfo.get(getApplicationContext()).saveTransactions();
        UserInfo.get(getApplicationContext()).saveUserInfo();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }


}
