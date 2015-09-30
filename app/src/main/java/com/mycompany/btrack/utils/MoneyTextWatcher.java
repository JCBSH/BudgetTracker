package com.mycompany.btrack.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by JCBSH on 29/09/2015.
 */
public class MoneyTextWatcher implements TextWatcher{
    public final String TAG = MoneyTextWatcher.class.getSimpleName();
    private final EditText mAmount;
    private String mBefore;
    private Toast mToast;
    private static int MAX_DECIMAL_PLACE = 2;
    private static double MAX_AMOUNT_LIMIT = 10000000000.00;
    private static double MAX_AMOUNT = 9999999999.99;
    private final ErrorUtil error;

    public MoneyTextWatcher (EditText amount) {
        mAmount = amount;
        mToast = Toast.makeText(mAmount.getContext(), "blah", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);

        error = new ErrorUtil();
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int indexOfTheChange, int removeCount, int replaceCount) {
//        Log.d(TAG, String.valueOf(charSequence));
//        Log.d(TAG, String.valueOf(indexOfTheChange));
//        Log.d(TAG, String.valueOf(removeCount));
//        Log.d(TAG, String.valueOf(replaceCount));
        mBefore = String.valueOf(new StringBuilder(charSequence));

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int indexOfTheChange, int removeCount, int replaceCount) {
//        Log.d(TAG, String.valueOf(charSequence));
//        Log.d(TAG, String.valueOf(indexOfTheChange));
//        Log.d(TAG, String.valueOf(removeCount));
//        Log.d(TAG, String.valueOf(replaceCount));
//        Log.d(TAG, String.valueOf(charSequence.length()));
//        Log.d(TAG, mBefore);
        if (!TransactionUtil.isValidAmount(String.valueOf(charSequence), error)) {
            mAmount.removeTextChangedListener(this);
            if (error.getCode() == 2 || error.getCode() == 1) {
                mBefore = "0";
            }
            mAmount.setText(mBefore);
            mAmount.addTextChangedListener(this);
            mAmount.setSelection(mBefore.length());
            mToast.setText(error.getMessage());
            mToast.show();
        }
/*
        for (int i = 0; i < (indexOfTheChange + replaceCount); ++i) {
            if (charSequence.charAt(i) == '.') {
//                Log.d(TAG, "!!!!greater");
//                Log.d(TAG, String.valueOf(charSequence.length()));
//                Log.d(TAG, String.valueOf(i));
//                Log.d(TAG, String.valueOf((charSequence.length() - i - 1)));
                if ((charSequence.length() - i - 1) > MAX_DECIMAL_PLACE) {
//                    Log.d(TAG, "greater");
                    mAmount.removeTextChangedListener(this);
                    mAmount.setText(mBefore);
                    mAmount.addTextChangedListener(this);
                    mAmount.setSelection(indexOfTheChange);
                    mToast.setText("max two decimal place");
                    mToast.show();
                }
            }
        }

        String current = String.valueOf(new StringBuilder(charSequence));
        double currentAmount = Double.parseDouble(current);
//        Log.d(TAG, String.valueOf(currentAmount));
//        Log.d(TAG, String.valueOf(MAX_AMOUNT));
        if (currentAmount > MAX_AMOUNT_LIMIT) {
            Log.d(TAG, "greater");
            mAmount.removeTextChangedListener(this);
            DecimalFormat df = new DecimalFormat("#.00");
            String formatted = df.format(MAX_AMOUNT);
            mAmount.setText(formatted);
            mAmount.addTextChangedListener(this);
            mAmount.setSelection(indexOfTheChange);
            mToast.setText("exceeded maximum limit");
            mToast.show();
        }
*/
    }
    @Override
    public void afterTextChanged(Editable editable) {
    }
}
