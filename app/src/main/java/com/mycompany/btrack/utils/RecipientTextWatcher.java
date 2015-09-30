package com.mycompany.btrack.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by godfreytruong on 1/10/2015.
 */
public class RecipientTextWatcher implements TextWatcher {

    private String before;
    private final ErrorUtil error;
    private final Toast toast;
    private final EditText recipientET;

    public RecipientTextWatcher(EditText recipientET) {
        this.recipientET = recipientET;
        toast = Toast.makeText(recipientET.getContext(), "", Toast.LENGTH_SHORT);
        this.error = new ErrorUtil();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        before = String.valueOf(s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TransactionUtil.isValidRecipient(String.valueOf(s), error)) {
            recipientET.removeTextChangedListener(this);
            recipientET.setText(this.before);
            recipientET.addTextChangedListener(this);
            recipientET.setSelection(this.before.length());
            toast.setText(error.getMessage());
            toast.show();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
