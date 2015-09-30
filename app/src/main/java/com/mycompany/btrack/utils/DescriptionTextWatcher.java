package com.mycompany.btrack.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by godfreytruong on 1/10/2015.
 */
public class DescriptionTextWatcher implements TextWatcher {

    private String before;
    private final ErrorUtil error;
    private final Toast toast;
    private final EditText descriptionET;

    public DescriptionTextWatcher(EditText descriptionET) {
        this.descriptionET = descriptionET;
        toast = Toast.makeText(descriptionET.getContext(), "", Toast.LENGTH_SHORT);
        this.error = new ErrorUtil();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        before = String.valueOf(s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TransactionUtil.isValidDescription(String.valueOf(s), error)) {
            descriptionET.removeTextChangedListener(this);
            descriptionET.setText(this.before);
            descriptionET.addTextChangedListener(this);
            descriptionET.setSelection(this.before.length());
            toast.setText(error.getMessage());
            toast.show();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
