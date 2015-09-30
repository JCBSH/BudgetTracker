package com.mycompany.btrack.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by JCBSH on 29/09/2015.
 */
public class StringTextWatcher implements TextWatcher {
    public final String TAG = StringTextWatcher.class.getSimpleName();
    private final EditText mEditText;
    private int mSizeLimit;
    private String mBefore;
    private Toast mToast;

    public StringTextWatcher (EditText editText, int sizeLimit) {
        mEditText = editText;
        mSizeLimit = sizeLimit;
        mToast = Toast.makeText(mEditText.getContext(), "blah", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);

    }
    public int getSizeLimit() {
        return mSizeLimit;
    }

    public void setSizeLimit(int sizeLimit) {
        mSizeLimit = sizeLimit;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int indexOfTheChange, int removeCount, int replaceCount) {
        mBefore = String.valueOf(new StringBuilder(charSequence));

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int indexOfTheChange, int removeCount, int replaceCount) {
        if (charSequence.length() > mSizeLimit) {
            mEditText.removeTextChangedListener(this);
            mEditText.setText(mBefore);
            mEditText.addTextChangedListener(this);
            mEditText.setSelection(indexOfTheChange);
            mToast.setText("exceeded maximum char limit of " + String.valueOf(mSizeLimit));
            mToast.show();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
